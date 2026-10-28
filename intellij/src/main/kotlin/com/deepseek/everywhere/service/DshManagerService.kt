package com.deepseek.everywhere.service

import com.deepseek.everywhere.settings.EverywhereSettingsState
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

enum class HarnessStatus {
    CHECKING,
    NOT_INSTALLED,
    INSTALLING,
    STOPPED,
    STARTING,
    RUNNING,
    ERROR
}

interface HarnessStatusListener {
    fun onStatusChanged(status: HarnessStatus, url: String?, port: Int?, error: String?)
}

@Service(Service.Level.PROJECT)
class DshManagerService(private val project: Project) {
    private val log = Logger.getInstance(DshManagerService::class.java)

    private var currentProcess: Process? = null
    private val listeners = CopyOnWriteArrayList<HarnessStatusListener>()
    private val logBuffer = mutableListOf<String>()
    private val isInstalling = AtomicBoolean(false)

    var status: HarnessStatus = HarnessStatus.CHECKING
        private set
    var activeUrl: String? = null
        private set
    var activePort: Int? = null
        private set
    var lastError: String? = null
        private set

    fun addListener(listener: HarnessStatusListener) {
        listeners.add(listener)
        listener.onStatusChanged(status, activeUrl, activePort, lastError)
    }

    fun removeListener(listener: HarnessStatusListener) {
        listeners.remove(listener)
    }

    private fun setStatus(newStatus: HarnessStatus, url: String? = null, port: Int? = null, error: String? = null) {
        status = newStatus
        activeUrl = url
        activePort = port
        lastError = error
        for (listener in listeners) {
            listener.onStatusChanged(newStatus, url, port, error)
        }
    }

    fun getLogs(): List<String> = synchronized(logBuffer) { logBuffer.toList() }

    fun isHarnessAvailable(customPath: String = EverywhereSettingsState.instance.customDshPath): Boolean {
        if (customPath.isNotBlank() && File(customPath).isFile) return true

        val executable = findDshExecutable("")
        if (File(executable).isFile) return true

        return !(executable.equals("ewh", ignoreCase = true)
                || executable.equals("ewh.cmd", ignoreCase = true)
                || executable.equals("dsh", ignoreCase = true)
                || executable.equals("dsh.cmd", ignoreCase = true))
    }

    fun checkAvailability(onComplete: (Boolean) -> Unit) {
        setStatus(HarnessStatus.CHECKING)
        val customPath = EverywhereSettingsState.instance.customDshPath
        ApplicationManager.getApplication().executeOnPooledThread {
            val available = isHarnessAvailable(customPath)
            ApplicationManager.getApplication().invokeLater {
                setStatus(
                    if (available) HarnessStatus.STOPPED else HarnessStatus.NOT_INSTALLED,
                    error = if (available) null else "EveryWhere Code runtime não encontrado. Instale para continuar."
                )
                onComplete(available)
            }
        }
    }

    fun installHarness() {
        if (!isInstalling.compareAndSet(false, true)) return
        setStatus(HarnessStatus.INSTALLING)

        Thread {
            try {
                appendLog("Installing @everywhere-harness/ewh with npm...")
                val command = if (SystemInfo.isWindows) {
                    listOf("cmd.exe", "/c", "npm install -g @everywhere-harness/ewh")
                } else {
                    listOf("npm", "install", "-g", "@everywhere-harness/ewh")
                }
                val process = ProcessBuilder(command).redirectErrorStream(true).start()
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach(::appendLog)
                }
                val exitCode = process.waitFor()
                val available = exitCode == 0 && isHarnessAvailable()
                val message = if (available) {
                    "EveryWhere Code installed successfully."
                } else {
                    "Installation could not be verified. Run `npm install -g @everywhere-harness/ewh` in a terminal."
                }
                val type = if (available) NotificationType.INFORMATION else NotificationType.ERROR
                appendLog(message)
                setStatus(
                    if (available) HarnessStatus.STOPPED else HarnessStatus.NOT_INSTALLED,
                    error = if (available) null else message
                )
                ApplicationManager.getApplication().invokeLater {
                    NotificationGroupManager.getInstance()
                        .getNotificationGroup("Everywhere Notification Group")
                        .createNotification("EveryWhere Code", message, type)
                        .notify(project)
                }
            } catch (error: Exception) {
                val message = "Installation failed: ${error.message}. Run `npm install -g @everywhere-harness/ewh` in a terminal."
                appendLog(message)
                setStatus(HarnessStatus.NOT_INSTALLED, error = message)
                ApplicationManager.getApplication().invokeLater {
                    NotificationGroupManager.getInstance()
                        .getNotificationGroup("Everywhere Notification Group")
                        .createNotification("EveryWhere Code", message, NotificationType.ERROR)
                        .notify(project)
                }
            } finally {
                isInstalling.set(false)
            }
        }.apply {
            name = "EveryWhere Code installer"
            isDaemon = true
            start()
        }
    }

    private fun appendLog(line: String) {
        synchronized(logBuffer) {
            if (logBuffer.size > 1000) {
                logBuffer.removeAt(0)
            }
            logBuffer.add(line)
        }
        log.info("[Everywhere] $line")
    }

    fun resolveWorkspaceDirectory(): String {
        val basePath = project.basePath
        if (!basePath.isNullOrBlank() && File(basePath).exists()) {
            return basePath
        }
        return System.getProperty("user.dir") ?: "."
    }

    fun findDshExecutable(customPath: String): String {
        if (customPath.isNotBlank() && File(customPath).exists()) {
            return customPath
        }

        val userHome = System.getProperty("user.home") ?: ""

        if (SystemInfo.isWindows) {
            val appData = System.getenv("APPDATA") ?: ""
            val localAppData = System.getenv("LOCALAPPDATA") ?: ""

            val candidates = listOf(
                File(appData, "npm/ewh.cmd").absolutePath,
                File(appData, "npm/dsh.cmd").absolutePath,
                File(localAppData, "npm/ewh.cmd").absolutePath,
                File(localAppData, "npm/dsh.cmd").absolutePath,
                File(userHome, ".pnpm/ewh.cmd").absolutePath,
                File(userHome, ".pnpm/dsh.cmd").absolutePath,
                File(userHome, ".local/share/pnpm/ewh.cmd").absolutePath,
                File(userHome, ".local/share/pnpm/dsh.cmd").absolutePath
            )

            for (candidate in candidates) {
                if (File(candidate).exists()) {
                    return candidate
                }
            }

            for (cmdCandidate in listOf("ewh.cmd", "ewh", "dsh.cmd", "dsh")) {
                try {
                    val whereProc = ProcessBuilder("where.exe", cmdCandidate).start()
                    val reader = BufferedReader(InputStreamReader(whereProc.inputStream))
                    val firstLine = reader.readLine()
                    whereProc.waitFor(2, TimeUnit.SECONDS)
                    if (!firstLine.isNullOrBlank() && File(firstLine.trim()).exists()) {
                        return firstLine.trim()
                    }
                } catch (_: Exception) {}
            }

            return "ewh.cmd"
        } else {
            val nvmDir = File(userHome, ".nvm/versions/node")
            if (nvmDir.exists() && nvmDir.isDirectory) {
                val versions = nvmDir.listFiles()?.filter { it.name.startsWith("v") }
                    ?.sortedWith(compareByDescending { it.name })
                if (versions != null) {
                    for (ver in versions) {
                        for (binName in listOf("ewh", "dsh")) {
                            val candidate = File(ver, "bin/$binName")
                            if (candidate.exists()) {
                                return candidate.absolutePath
                            }
                        }
                    }
                }
            }

            val unixCandidates = listOf(
                "/usr/local/bin/ewh",
                "/usr/bin/ewh",
                File(userHome, ".local/share/pnpm/ewh").absolutePath,
                File(userHome, ".local/bin/ewh").absolutePath,
                File(userHome, ".npm-global/bin/ewh").absolutePath,
                "/usr/local/bin/dsh",
                "/usr/bin/dsh",
                File(userHome, ".local/share/pnpm/dsh").absolutePath,
                File(userHome, ".local/bin/dsh").absolutePath,
                File(userHome, ".npm-global/bin/dsh").absolutePath
            )

            for (candidate in unixCandidates) {
                if (File(candidate).exists()) {
                    return candidate
                }
            }

            val pathEntries = (System.getenv("PATH") ?: "").split(File.pathSeparator)
            for (pathEntry in pathEntries) {
                for (commandName in listOf("ewh", "dsh")) {
                    val candidate = File(pathEntry, commandName)
                    if (candidate.isFile && candidate.canExecute()) {
                        return candidate.absolutePath
                    }
                }
            }

            return "ewh"
        }
    }

    private fun extractUrlFromLine(text: String): String? {
        val dshRegex = Pattern.compile("(?:ewh|dsh) web:\\s*(https?://\\S+)", Pattern.CASE_INSENSITIVE)
        val dshMatcher = dshRegex.matcher(text)
        if (dshMatcher.find()) {
            return dshMatcher.group(1).trim()
        }

        val localRegex = Pattern.compile("(https?://(?:127\\.0\\.0\\.1|localhost):\\d+[^\\s\"'<>\\)]*)", Pattern.CASE_INSENSITIVE)
        val localMatcher = localRegex.matcher(text)
        if (localMatcher.find()) {
            return localMatcher.group(1).trim()
        }

        return null
    }

    @Synchronized
    fun start(isRetry: Boolean = false) {
        if (status == HarnessStatus.RUNNING && !activeUrl.isNullOrEmpty()) {
            return
        }

        val settings = EverywhereSettingsState.instance
        val existing = findRunningDsh(settings.port)
        if (existing != null) {
            appendLog("[EveryWhere Harness] Serviço já em execução detectado na porta ${existing.first}. Utilizando instância aberta...")
            appendLog("[EveryWhere Harness] Backend ready at: ${existing.second}")
            setStatus(HarnessStatus.RUNNING, url = existing.second, port = existing.first)
            notifySuccess(existing.second)
            return
        }

        if (!isHarnessAvailable(settings.customDshPath)) {
            setStatus(
                HarnessStatus.NOT_INSTALLED,
                error = "EveryWhere Code runtime não encontrado. Instale `@everywhere-harness/ewh` para continuar."
            )
            return
        }

        setStatus(HarnessStatus.STARTING)
        appendLog("Iniciando EveryWhere Harness runtime...")

        val executable = findDshExecutable(settings.customDshPath)
        val workingDir = resolveWorkspaceDirectory()
        val profileArg = settings.profile.ifBlank { "web" }

        val cmdList = mutableListOf<String>()
        if (SystemInfo.isWindows && (executable.lowercase().endsWith(".cmd") ||
                    executable.lowercase().endsWith(".bat") ||
                    executable.equals("ewh", ignoreCase = true) ||
                    executable.equals("ewh.cmd", ignoreCase = true) ||
                    executable.equals("dsh", ignoreCase = true) ||
                    executable.equals("dsh.cmd", ignoreCase = true))) {
            cmdList.add("cmd.exe")
            cmdList.add("/c")
            cmdList.add(executable)
        } else {
            cmdList.add(executable)
        }
        cmdList.add("--profile")
        cmdList.add(profileArg)
        cmdList.add("--no-open")
        if (settings.port > 0) {
            cmdList.add("--port")
            cmdList.add(settings.port.toString())
        }

        Thread {
            try {
                val processBuilder = ProcessBuilder(cmdList)
                processBuilder.directory(File(workingDir))

                val env = processBuilder.environment()
                if (settings.apiKey.isNotBlank()) {
                    env["EVERYWHERE_API_KEY"] = settings.apiKey
                    env["DEEPSEEK_API_KEY"] = settings.apiKey
                }
                if (settings.baseUrl.isNotBlank()) {
                    env["EVERYWHERE_BASE_URL"] = settings.baseUrl
                    env["DEEPSEEK_BASE_URL"] = settings.baseUrl
                }

                // Enhance PATH for Node/npm/pnpm on Windows
                val pathKey = if (env.containsKey("Path")) "Path" else "PATH"
                val currentPath = env[pathKey] ?: ""
                val extraDirs = mutableListOf<String>()
                if (SystemInfo.isWindows) {
                    val appData = System.getenv("APPDATA")
                    val localAppData = System.getenv("LOCALAPPDATA")
                    val progFiles = System.getenv("ProgramFiles")
                    val userHome = System.getProperty("user.home")
                    if (!appData.isNullOrBlank()) extraDirs.add("$appData\\npm")
                    if (!localAppData.isNullOrBlank()) extraDirs.add("$localAppData\\npm")
                    if (!progFiles.isNullOrBlank()) extraDirs.add("$progFiles\\nodejs")
                    if (!userHome.isNullOrBlank()) extraDirs.add("$userHome\\.pnpm")
                }
                val validExtras = extraDirs.filter { File(it).exists() && !currentPath.contains(it) }
                if (validExtras.isNotEmpty()) {
                    env[pathKey] = validExtras.joinToString(File.pathSeparator) + File.pathSeparator + currentPath
                }

                val process = processBuilder.start()
                currentProcess = process

                val urlFound = AtomicBoolean(false)

                // 1. STDOUT reader thread
                val stdoutThread = Thread {
                    try {
                        val reader = BufferedReader(InputStreamReader(process.inputStream))
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            val text = line ?: continue
                            appendLog(text)

                            if (!urlFound.get()) {
                                val detected = extractUrlFromLine(text)
                                if (detected != null && urlFound.compareAndSet(false, true)) {
                                    var detectedPort = 3080
                                    var token = ""
                                    try {
                                        val uri = URI(detected)
                                        detectedPort = uri.port.takeIf { it > 0 } ?: 3080
                                        val query = uri.query
                                        if (query != null) {
                                            for (param in query.split("&")) {
                                                val pair = param.split("=")
                                                if (pair.size == 2 && pair[0] == "token") {
                                                    token = pair[1]
                                                }
                                            }
                                        }
                                    } catch (_: Exception) {}

                                    saveSession(detected, detectedPort, token)
                                    appendLog("[EveryWhere Harness] Backend ready at: $detected")
                                    setStatus(HarnessStatus.RUNNING, url = detected, port = detectedPort)
                                    notifySuccess(detected)
                                }
                            }
                        }
                    } catch (_: Exception) {}
                }
                stdoutThread.isDaemon = true
                stdoutThread.start()

                // 2. STDERR reader thread
                val stderrThread = Thread {
                    try {
                        val reader = BufferedReader(InputStreamReader(process.errorStream))
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            appendLog(line ?: "")
                        }
                    } catch (_: Exception) {}
                }
                stderrThread.isDaemon = true
                stderrThread.start()

                // 3. Parallel HTTP health check polling (fallback if stdout is buffered)
                val healthCheckThread = Thread {
                    val targetPorts = listOfNotNull(settings.port.takeIf { it > 0 }, 3080, 3000).distinct()
                    var attempts = 0
                    while (!urlFound.get() && attempts < 30 && status == HarnessStatus.STARTING && process.isAlive) {
                        Thread.sleep(600)
                        attempts++
                        for (port in targetPorts) {
                            try {
                                val conn = (URL("http://127.0.0.1:$port/").openConnection() as HttpURLConnection).apply {
                                    connectTimeout = 600
                                    readTimeout = 600
                                    instanceFollowRedirects = false
                                }
                                val code = conn.responseCode
                                if (code in 200..399 || (code == 401 && probeDshPort(port))) {
                                    if (urlFound.compareAndSet(false, true)) {
                                        val saved = loadSavedUrl(port)
                                        val healthUrl = if (!saved.isNullOrBlank()) saved else "http://127.0.0.1:$port/"
                                        appendLog("Health check ativo detectou serviço respondendo na porta $port (HTTP $code)")
                                        appendLog("[EveryWhere Harness] Backend ready at: $healthUrl")
                                        setStatus(HarnessStatus.RUNNING, url = healthUrl, port = port)
                                        notifySuccess(healthUrl)
                                        break
                                    }
                                }
                            } catch (_: Exception) {}
                        }
                    }
                }
                healthCheckThread.isDaemon = true
                healthCheckThread.start()

                // 4. Timeout watchdog thread (30 seconds)
                val timeoutWatchdog = Thread {
                    Thread.sleep(30000)
                    if (!urlFound.get() && status == HarnessStatus.STARTING && process.isAlive) {
                        val errMsg = "Tempo limite excedido (30s) aguardando o EveryWhere Harness responder. Verifique se o executável 'ewh' ou 'dsh' funciona no terminal."
                        appendLog(errMsg)
                        setStatus(HarnessStatus.ERROR, error = errMsg)
                        notifyError(errMsg)
                    }
                }
                timeoutWatchdog.isDaemon = true
                timeoutWatchdog.start()

                val exitCode = process.waitFor()
                appendLog("Processo EveryWhere Harness finalizado com código $exitCode.")

                if (!urlFound.get()) {
                    if (exitCode == 1) {
                        val running = findRunningDsh(settings.port)
                        if (running != null) {
                            appendLog("[EveryWhere Harness] Processo encerrou com código 1 pois EveryWhere Harness já está em execução na porta ${running.first}. Utilizando instância aberta...")
                            appendLog("[EveryWhere Harness] Backend ready at: ${running.second}")
                            setStatus(HarnessStatus.RUNNING, url = running.second, port = running.first)
                            notifySuccess(running.second)
                            return@Thread
                        }

                        if (!isRetry) {
                            appendLog("Falha com código 1 (porta possivelmente ocupada). Limpando serviços órfãos e tentando reiniciar...")
                            stop(silent = true)
                            Thread.sleep(1000)
                            start(isRetry = true)
                            return@Thread
                        }
                    }

                    val errMsg = "EveryWhere Harness encerrou prematuramente com código $exitCode."
                    setStatus(HarnessStatus.ERROR, error = errMsg)
                    notifyError(errMsg)
                } else {
                    setStatus(HarnessStatus.STOPPED)
                }
            } catch (ex: Exception) {
                val errMsg = "Erro ao executar EveryWhere Harness: ${ex.message}"
                appendLog(errMsg)
                setStatus(HarnessStatus.ERROR, error = errMsg)
                notifyError(errMsg)
            }
        }.start()
    }

    @Synchronized
    fun stop(silent: Boolean = false) {
        if (!silent) {
            appendLog("Interrompendo EveryWhere Harness e limpando processos órfãos...")
        }

        currentProcess?.let { proc ->
            try {
                proc.destroy()
                proc.waitFor(2, TimeUnit.SECONDS)
                if (proc.isAlive) {
                    proc.destroyForcibly()
                }
            } catch (_: Exception) {}
        }
        currentProcess = null

        killOrphanProcesses()
        setStatus(HarnessStatus.STOPPED)
        if (!silent) {
            appendLog("EveryWhere Harness interrompido com sucesso.")
        }
    }

    fun restart() {
        stop(silent = true)
        Thread.sleep(1000)
        start()
    }

    private fun killOrphanProcesses() {
        try {
            if (SystemInfo.isWindows) {
                for (procName in listOf("ewh.exe", "dsh.exe")) {
                    try {
                        val p = ProcessBuilder("taskkill.exe", "/F", "/IM", procName, "/T").start()
                        p.waitFor(2, TimeUnit.SECONDS)
                    } catch (_: Exception) {}
                }

                val targetPorts = listOfNotNull(activePort, 3080, 3000).distinct()
                for (port in targetPorts) {
                    killWindowsPort(port)
                }
            } else {
                for (procName in listOf("ewh", "dsh")) {
                    try {
                        val p = ProcessBuilder("pkill", "-f", procName).start()
                        p.waitFor(2, TimeUnit.SECONDS)
                    } catch (_: Exception) {}
                }

                val targetPorts = listOfNotNull(activePort, 3080, 3000).distinct()
                for (port in targetPorts) {
                    try {
                        val p = ProcessBuilder("fuser", "-k", "$port/tcp").start()
                        p.waitFor(2, TimeUnit.SECONDS)
                    } catch (_: Exception) {}
                }
            }
        } catch (_: Exception) {}
    }

    private fun killWindowsPort(port: Int) {
        try {
            val netstat = ProcessBuilder("netstat.exe", "-ano").start()
            val reader = BufferedReader(InputStreamReader(netstat.inputStream))
            val regex = Pattern.compile("(?:127\\.0\\.0\\.1|0\\.0\\.0\\.0):$port\\s+.*LISTENING\\s+(\\d+)", Pattern.CASE_INSENSITIVE)

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val text = line ?: continue
                val matcher = regex.matcher(text)
                if (matcher.find()) {
                    val pid = matcher.group(1)
                    val kill = ProcessBuilder("taskkill.exe", "/PID", pid, "/T", "/F").start()
                    kill.waitFor(2, TimeUnit.SECONDS)
                }
            }
        } catch (_: Exception) {}
    }

    private fun notifySuccess(url: String) {
        try {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Everywhere Notification Group")
                ?.createNotification(
                    "EveryWhere Harness Ativo",
                    "Serviço em execução em $url",
                    NotificationType.INFORMATION
                )
                ?.notify(project)
        } catch (_: Exception) {}
    }

    private fun notifyError(message: String) {
        try {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Everywhere Notification Group")
                ?.createNotification(
                    "Erro no EveryWhere Harness",
                    message,
                    NotificationType.ERROR
                )
                ?.notify(project)
        } catch (_: Exception) {}
    }

    companion object {
        fun getInstance(project: Project): DshManagerService =
            project.getService(DshManagerService::class.java)

        fun getSavedSessionFile(): File {
            val userHome = System.getProperty("user.home", "")
            val ewhFile = File(userHome, ".ewh/last-session.json")
            if (ewhFile.exists()) return ewhFile
            return File(userHome, ".dsh/last-session.json")
        }

        fun saveSession(rawUrl: String, port: Int, token: String?) {
            try {
                val userHome = System.getProperty("user.home", "")
                val ewhDir = File(userHome, ".ewh")
                val dshDir = File(userHome, ".dsh")
                val targetFile = if (ewhDir.exists() || !dshDir.exists()) File(ewhDir, "last-session.json") else File(dshDir, "last-session.json")
                targetFile.parentFile?.mkdirs()
                val json = """
                    {
                      "rawUrl": "$rawUrl",
                      "port": $port,
                      "token": "${token ?: ""}",
                      "updatedAt": ${System.currentTimeMillis()}
                    }
                """.trimIndent()
                targetFile.writeText(json, Charsets.UTF_8)
            } catch (_: Exception) {}
        }

        fun loadSavedUrl(port: Int): String? {
            try {
                val userHome = System.getProperty("user.home", "")
                for (subDir in listOf(".ewh", ".dsh")) {
                    val file = File(userHome, "$subDir/last-session.json")
                    if (!file.exists()) continue
                    val content = file.readText(Charsets.UTF_8)
                    val mPort = Pattern.compile("\"port\"\\s*:\\s*$port").matcher(content)
                    if (mPort.find()) {
                        val mUrl = Pattern.compile("\"rawUrl\"\\s*:\\s*\"([^\"]+)\"").matcher(content)
                        if (mUrl.find()) {
                            return mUrl.group(1).trim()
                        }
                    }
                }
            } catch (_: Exception) {}
            return null
        }

        fun probeDshPort(port: Int): Boolean {
            if (port <= 0) return false
            try {
                val conn = (URL("http://127.0.0.1:$port/").openConnection() as HttpURLConnection).apply {
                    connectTimeout = 800
                    readTimeout = 800
                    instanceFollowRedirects = false
                }
                val code = conn.responseCode
                if (code in 200..399) {
                    return true
                }
                if (code == 401) {
                    val stream = conn.errorStream ?: conn.inputStream
                    if (stream != null) {
                        val body = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        if (body.contains("authentication required") || body.lowercase().contains("dsh") || body.lowercase().contains("ewh")) {
                            return true
                        }
                    }
                }
            } catch (_: Exception) {}
            return false
        }

        fun findRunningDsh(targetPort: Int): Pair<Int, String>? {
            val candidatePorts = listOfNotNull(targetPort.takeIf { it > 0 }, 3080, 3000).distinct()
            for (p in candidatePorts) {
                if (probeDshPort(p)) {
                    val saved = loadSavedUrl(p)
                    val url = if (!saved.isNullOrBlank()) saved else "http://127.0.0.1:$p/"
                    return Pair(p, url)
                }
            }
            return null
        }
    }
}
