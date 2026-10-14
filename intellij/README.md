<div align="center">

# Everywhere DeepSeek Harness for IntelliJ IDEA 🌐⚡

[English](#-english) • [Português](#-português)

[![IntelliJ IDEA 2024.1+](https://img.shields.io/badge/IntelliJ%20IDEA-2024.1.2%20(Community%20%7C%20Ultimate)-red?logo=intellijidea)](https://www.jetbrains.com/idea/)
[![Java Runtime](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Embedded Browser](https://img.shields.io/badge/Webview-JCEF%20(Chromium)-teal)]()
[![DeepSeek](https://img.shields.io/badge/AI-DeepSeek--V3%20%7C%20DeepSeek--R1-007acc)](https://deepseek.com)

</div>

---

## 🇺🇸 English

**Everywhere DeepSeek Harness** is the official plugin developed specifically for the JetBrains ecosystem, with validated native support for **IntelliJ IDEA 2024.1+ (Community Edition and Ultimate)** and subsequent versions.

It embeds the official **DeepSeek Harness (`dsh`)** runtime inside a native **Tool Window** using hardware-accelerated **JCEF (Java Chromium Embedded Framework)**, offering a seamless and integrated experience directly inside your Java, Kotlin, Spring, Android, and Web development workflow.

### 🌟 Features

- 🪟 **Native Tool Window with JCEF**: Smooth, modern hardware-accelerated Chromium webview embedded directly into the right-hand tool window strip of IntelliJ.
- 📂 **Workspace Project Binding**: Contextually bound to the root directory of the currently open IntelliJ IDEA project (`project.basePath`).
- 🔄 **Intelligent Auto-Recovery (Exit Code 1)**: If old processes or ports (3080/3000) are busy, the plugin automatically detects failure, terminates orphaned processes (`taskkill` / `fuser` / `pkill`), and restores the service in seconds.
- ⚙️ **Integrated Settings Page**: Full configuration under `Settings -> Tools -> Everywhere DeepSeek Harness` (API keys, ports, profiles, auto-start, and binary path).
- 🧭 **Quick Action Toolbar**:
  - ▶ **Start**: Boots the official `dsh` runtime.
  - 🛑 **Stop**: Halts the process and releases network ports.
  - 🔄 **Restart**: Quick session restart.
  - 🔁 **Reload**: Refreshes the embedded webview page.
  - 🌐 **Open in Browser**: Opens the running Harness web interface in the system default browser.
  - ⚙️ **Settings**: Direct shortcut to plugin preferences.
- 🔔 **Integrated Notifications**: Desktop balloon notifications indicating when the service becomes ready with the active local URL.

### 🚀 Build & Package

#### Prerequisites
1. **IntelliJ IDEA 2024.1+** (Community or Ultimate) or **JDK 17+** installed.
2. **DeepSeek Harness CLI**:
   ```cmd
   npm install -g @deepseek-ai/dsh
   ```

#### Option 1: Build via Command Line (Gradle)

Inside the `intellij` directory:

**Windows (PowerShell / Command Prompt):**
```cmd
cd intellij
gradlew.bat buildPlugin
```

**Linux / macOS:**
```bash
cd intellij
./gradlew buildPlugin
```

The packaged plugin distribution will be generated at:
```
intellij/build/distributions/everywhere-intellij-1.2.6.zip
```

#### Option 2: Open and Build Directly in IntelliJ IDEA

1. In **IntelliJ IDEA**:
   - Go to `File` ➔ `Open...`
   - Select the `intellij` folder of this repository and confirm opening as a Gradle project.
2. Wait for Gradle sync to complete.
3. In the **Gradle** sidebar tab (on the right):
   - Expand `Tasks` ➔ `intellij` ➔ double-click `buildPlugin`.
   - Or run `runIde` to test live inside a sandboxed experimental IntelliJ instance.

### 📦 How to Install the Plugin in IntelliJ IDEA

1. In IntelliJ IDEA, open Settings:
   - **Windows/Linux**: `File` ➔ `Settings...` (`Ctrl + Alt + S`).
   - **macOS**: `IntelliJ IDEA` ➔ `Settings...` (`Cmd + ,`).
2. In the left navigation, select **Plugins**.
3. Click the gear icon ⚙️ at the top and select:
   **"Install Plugin from Disk..."**
4. Navigate to `intellij/build/distributions/` and select the ZIP file:
   `everywhere-intellij-1.2.6.zip`
5. Click **OK** and restart IntelliJ IDEA if prompted.

### 🎯 How to Use

1. After installation, locate the **Everywhere** tab on the right sidebar of the editor (or go to `Tools` ➔ `Everywhere (DeepSeek Harness)` ➔ `Start DeepSeek Harness`).
2. If **Auto-Start** is enabled, the DeepSeek Harness boots automatically when the tool window is first opened.
3. The agent has instant access to your project workspace files and terminal tools.

### ⚙️ Settings (Settings ➔ Tools ➔ Everywhere)

| Option | Default | Description |
|---|---|---|
| **Auto Start** | `true` | Automatically starts the agent when opening the Tool Window |
| **Port** | `0` | Web server port (0 assigns an available port automatically) |
| **Profile** | `"web"` | DeepSeek Harness execution profile (`web`, `headless`, `tui`) |
| **DeepSeek API Key** | `""` | DeepSeek API key (also accepted via `DEEPSEEK_API_KEY` environment variable) |
| **Base URL** | `https://api.deepseek.com` | DeepSeek API base endpoint URL |
| **Custom DSH Path** | `""` | Custom path to the `dsh` executable if not in system PATH |

---

## 🇧🇷 Português

Esta é a extensão oficial do **Everywhere** desenvolvida especificamente para o ecossistema JetBrains, com suporte nativo e validado para **IntelliJ IDEA 2024.1.2 (Community Edition / Ultimate, Build #IC-241.17011.79)** e versões posteriores.

Ela incorpora o runtime oficial do **DeepSeek Harness (`dsh`)** em uma janela de ferramentas nativa (**Tool Window**) utilizando **JCEF (Java Chromium Embedded Framework)** acelerado por hardware, oferecendo uma experiência interativa e integrada diretamente ao seu fluxo de desenvolvimento em Java, Kotlin, Spring, Android e projetos Web.

### 🌟 Funcionalidades

- 🪟 **Tool Window Nativa com JCEF**: Renderização fluida e moderna com Chromium embutido na barra lateral direita do IntelliJ.
- 📂 **Workspace Project Binding**: Conectado contextualmente à pasta raiz do projeto aberto no IntelliJ IDEA (`project.basePath`).
- 🔄 **Auto-Recuperação Inteligente (Exit Code 1)**: Se houver processos antigos ou portas travadas (3080/3000), o plugin detecta a falha, encerra automaticamente os processos órfãos (`taskkill` / `fuser` / `pkill`) e retoma o serviço em segundos.
- ⚙️ **Página de Configurações Integrada**: Configurações em `Settings -> Tools -> Everywhere DeepSeek Harness` (chaves de API, portas, perfis, auto-start e caminho do executável).
- 🧭 **Barra de Ações Rápida (Toolbar)**:
  - ▶ **Iniciar**: Inicia o runtime oficial `dsh`.
  - 🛑 **Parar**: Interrompe o processo e desocupa portas.
  - 🔄 **Reiniciar**: Reinicia a sessão rapidamente.
  - 🔁 **Recarregar**: Atualiza a página do webview.
  - 🌐 **Abrir no Navegador**: Abre o link do Harness no navegador padrão do sistema.
  - ⚙️ **Configurações**: Atalho direto para as preferências do plugin.
- 🔔 **Notificações Integradas**: Notificações balão informando quando o serviço fica ativo com a URL local pronta para uso.

### 🚀 Como Compilar e Gerar o Pacote do Plugin

#### Pré-requisitos
1. **IntelliJ IDEA 2024.1.2** (Community Edition ou Ultimate) ou **JDK 17** instalado.
2. **DeepSeek Harness CLI**:
   ```cmd
   npm install -g @deepseek-ai/dsh
   ```

#### Opção 1: Compilar via Linha de Comando (Gradle)

Dentro da pasta `intellij`:

**No Windows (PowerShell / Prompt de Comando):**
```cmd
cd intellij
gradlew.bat buildPlugin
```

**No Linux / macOS:**
```bash
cd intellij
./gradlew buildPlugin
```

O arquivo do plugin empacotado será gerado em:
```
intellij/build/distributions/everywhere-intellij-1.2.6.zip
```

#### Opção 2: Abrir e Compilar Diretamente no IntelliJ IDEA

1. No **IntelliJ IDEA 2024.1.2**:
   - Vá em `File` ➔ `Open...`
   - Selecione a pasta `intellij` deste repositório e confirme a abertura como projeto Gradle.
2. Aguarde a sincronização dos scripts Gradle pelo IntelliJ.
3. Na aba lateral **Gradle** (à direita):
   - Expanda `Tasks` ➔ `intellij` ➔ dê duplo clique em `buildPlugin`.
   - Ou para testar ao vivo em uma instância de desenvolvimento do IntelliJ, execute `runIde`.

### 📦 Como Instalar o Plugin no IntelliJ IDEA

1. No IntelliJ IDEA, acesse as preferências:
   - **Windows/Linux**: `File` ➔ `Settings...` (atalho `Ctrl + Alt + S`).
   - **macOS**: `IntelliJ IDEA` ➔ `Settings...` (atalho `Cmd + ,`).
2. No menu à esquerda, selecione **Plugins**.
3. Clique no ícone de engrenagem ⚙️ no topo e escolha:
   **"Install Plugin from Disk..."**
4. Navegue até a pasta `intellij/build/distributions/` e selecione o arquivo:
   `everywhere-intellij-1.2.6.zip`
5. Clique em **OK** e reinicie o IntelliJ IDEA caso solicitado.

### 🎯 Como Usar

1. Após a instalação, localize a aba **Everywhere** na barra lateral direita do editor (ou vá no menu superior `Tools` ➔ `Everywhere (DeepSeek Harness)` ➔ `Iniciar DeepSeek Harness`).
2. Se a opção **Auto-Start** estiver habilitada, o DeepSeek Harness iniciará automaticamente na primeira abertura.
3. O agente terá acesso imediato à árvore de diretórios e arquivos do seu projeto.

### ⚙️ Configurações (Settings ➔ Tools ➔ Everywhere)

| Opção | Padrão | Descrição |
|---|---|---|
| **Auto Start** | `true` | Iniciar o agente automaticamente ao abrir a Tool Window |
| **Port** | `0` | Porta do servidor web (0 atribui porta livre automaticamente) |
| **Profile** | `"web"` | Perfil de execução do DeepSeek Harness (`web`, `headless`, `tui`) |
| **DeepSeek API Key** | `""` | Chave da API DeepSeek (também aceita via variável `DEEPSEEK_API_KEY`) |
| **Base URL** | `https://api.deepseek.com` | Endpoint base da API DeepSeek |
| **Custom DSH Path** | `""` | Caminho personalizado para o binário `dsh` se não estiver no PATH padrão |
