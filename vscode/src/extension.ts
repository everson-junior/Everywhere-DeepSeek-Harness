import * as vscode from 'vscode'
import { EveryWhereHarnessViewProvider, openHarnessInEditorTab } from './chatViewProvider.ts'
import { DshProcessManager } from './dshManager.ts'
import { ExtensionConfig, HarnessInfo } from './types.ts'

/**
 * Resolve runtime configuration combining VS Code settings and environment variables.
 */
function resolveConfig(): ExtensionConfig {
  const wsConfig = vscode.workspace.getConfiguration('everywhere')
  const apiKeySetting = wsConfig.get<string>('apiKey', '')
  const envApiKey = process.env.EVERYWHERE_API_KEY ?? process.env.DEEPSEEK_API_KEY ?? ''

  const baseUrlSetting = wsConfig.get<string>('baseUrl', 'https://api.deepseek.com')
  const envBaseUrl = process.env.EVERYWHERE_BASE_URL ?? process.env.DEEPSEEK_BASE_URL ?? ''

  return {
    dshPath: wsConfig.get<string>('dshPath', ''),
    projectPath: wsConfig.get<string>('projectPath', ''),
    port: wsConfig.get<number>('port', 0),
    profile: wsConfig.get<string>('profile', 'web'),
    autoStart: wsConfig.get<boolean>('autoStart', false),
    apiKey: apiKeySetting || envApiKey,
    baseUrl: envBaseUrl || baseUrlSetting,
  }
}

/**
 * Update the Status Bar item according to the Harness state.
 */
function updateStatusBar(item: vscode.StatusBarItem, info: HarnessInfo): void {
  switch (info.status) {
    case 'running':
      item.text = '$(hubot) EveryWhere Harness: Active'
      item.tooltip = 'EveryWhere Harness is active.\nClick to focus chat.'
      item.command = 'everywhere.focusHarness'
      item.backgroundColor = undefined
      break
    case 'starting':
      item.text = '$(sync~spin) EveryWhere: Starting...'
      item.tooltip = 'EveryWhere Harness is starting...'
      item.command = 'everywhere.showLogs'
      item.backgroundColor = undefined
      break
    case 'installing':
      item.text = '$(sync~spin) EveryWhere: Installing...'
      item.tooltip = 'Installing EveryWhere Harness...'
      item.command = 'everywhere.showLogs'
      item.backgroundColor = undefined
      break
    case 'not_installed':
      item.text = '$(cloud-download) EveryWhere: Not Installed'
      item.tooltip = 'EveryWhere Harness is not installed.\nClick to install.'
      item.command = 'everywhere.installHarness'
      item.backgroundColor = new vscode.ThemeColor('statusBarItem.warningBackground')
      break
    case 'error':
      item.text = '$(error) EveryWhere: Error'
      item.tooltip = `EveryWhere Harness error: ${info.error ?? 'Unknown'}\nClick to view logs.`
      item.command = 'everywhere.showLogs'
      item.backgroundColor = new vscode.ThemeColor('statusBarItem.errorBackground')
      break
    case 'stopped':
    default:
      item.text = '$(circle-slash) EveryWhere: Stopped'
      item.tooltip = 'EveryWhere Harness is stopped.\nClick to start.'
      item.command = 'everywhere.startHarness'
      item.backgroundColor = undefined
      break
  }
}

let dshManagerInstance: DshProcessManager | null = null

/**
 * Extension activation entrypoint.
 */
export function activate(context: vscode.ExtensionContext): void {
  let currentConfig = resolveConfig()

  const outputChannel = vscode.window.createOutputChannel('EveryWhere Harness')
  context.subscriptions.push(outputChannel)

  const dshManager = new DshProcessManager(
    currentConfig,
    outputChannel,
    context.globalStorageUri.fsPath,
  )
  dshManagerInstance = dshManager
  context.subscriptions.push({
    dispose: () => {
      dshManager.dispose()
    },
  })

  // Status Bar item
  const statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 100)
  context.subscriptions.push(statusBarItem)
  updateStatusBar(statusBarItem, dshManager.info)
  statusBarItem.show()

  const updateContextState = (info: HarnessInfo) => {
    updateStatusBar(statusBarItem, info)
    vscode.commands.executeCommand('setContext', 'everywhere.isRunning', info.status === 'running')
  }

  dshManager.onStatusChanged((info) => {
    updateContextState(info)
  })

  updateContextState(dshManager.info)

  // Register Webview View Provider for the sidebar chat
  const harnessViewProvider = new EveryWhereHarnessViewProvider(
    context.extensionUri,
    dshManager,
    currentConfig,
  )

  context.subscriptions.push(
    vscode.window.registerWebviewViewProvider(
      EveryWhereHarnessViewProvider.viewType,
      harnessViewProvider,
      {
        webviewOptions: { retainContextWhenHidden: true },
      },
    ),
  )

  // Listen to configuration changes
  context.subscriptions.push(
    vscode.workspace.onDidChangeConfiguration((e) => {
      if (e.affectsConfiguration('everywhere')) {
        currentConfig = resolveConfig()
        dshManager.updateConfig(currentConfig)
        harnessViewProvider.updateConfig(currentConfig)
      }
    }),
  )

  // Register user commands
  context.subscriptions.push(
    vscode.commands.registerCommand('everywhere.startHarness', async () => {
      try {
        await vscode.commands.executeCommand('everywhere.harnessView.focus')
        await vscode.window.withProgress(
          {
            location: vscode.ProgressLocation.Notification,
            title: 'Starting EveryWhere Harness...',
            cancellable: false,
          },
          async () => {
            await dshManager.start()
          },
        )
        await vscode.commands.executeCommand('everywhere.harnessView.focus')
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : String(err)
        if (msg.includes('exited with code 1')) {
          try {
            const existing = await dshManager.findRunningDsh()
            if (existing) {
              await dshManager.attachToRunningService(existing)
              await vscode.commands.executeCommand('everywhere.harnessView.focus')
              return
            }
            outputChannel.appendLine('[EveryWhere Harness] Handling exit code 1 fallback: stopping service and retrying...')
            await dshManager.stop()
            await new Promise((r) => setTimeout(r, 1000))
            await dshManager.start()
            await vscode.commands.executeCommand('everywhere.harnessView.focus')
            return
          } catch (retryErr: unknown) {
            const retryMsg = retryErr instanceof Error ? retryErr.message : String(retryErr)
            vscode.window.showErrorMessage(`Failed to start EveryWhere Harness: ${retryMsg}`, 'Show Logs').then((action) => {
              if (action === 'Show Logs') {
                outputChannel.show(true)
              }
            })
            return
          }
        }
        vscode.window.showErrorMessage(`Failed to start EveryWhere Harness: ${msg}`, 'Show Logs').then((action) => {
          if (action === 'Show Logs') {
            outputChannel.show(true)
          }
        })
      }
    }),

    vscode.commands.registerCommand('everywhere.stopHarness', async () => {
      await dshManager.stop()
      vscode.window.showInformationMessage('EveryWhere Harness stopped.')
    }),

    vscode.commands.registerCommand('everywhere.debugHarness', async () => {
      const selected = await vscode.window.showOpenDialog({
        canSelectFiles: false,
        canSelectFolders: true,
        canSelectMany: false,
        openLabel: 'Use Project for Debug',
        title: 'Select the project directory for EveryWhere Harness',
      })
      const project = selected?.[0]
      if (!project) return

      await vscode.workspace.getConfiguration('everywhere').update(
        'projectPath',
        project.fsPath,
        vscode.ConfigurationTarget.Workspace,
      )
      await dshManager.stop()
      try {
        await dshManager.startFromWorkspaceSource(project.fsPath)
        await vscode.commands.executeCommand('everywhere.harnessView.focus')
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : String(err)
        vscode.window.showErrorMessage(`Failed to start EveryWhere Harness in debug project: ${msg}`)
      }
    }),

    vscode.commands.registerCommand('everywhere.restartHarness', async () => {
      try {
        await vscode.window.withProgress(
          {
            location: vscode.ProgressLocation.Notification,
            title: 'Restarting EveryWhere Harness...',
            cancellable: false,
          },
          async () => {
            await dshManager.restart()
          },
        )
        await vscode.commands.executeCommand('everywhere.harnessView.focus')
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : String(err)
        vscode.window.showErrorMessage(`Failed to restart EveryWhere Harness: ${msg}`, 'Show Logs').then((action) => {
          if (action === 'Show Logs') {
            outputChannel.show(true)
          }
        })
      }
    }),

    vscode.commands.registerCommand('everywhere.openFullView', () => {
      openHarnessInEditorTab(context, dshManager)
    }),

    vscode.commands.registerCommand('everywhere.focusHarness', async () => {
      await vscode.commands.executeCommand('everywhere.harnessView.focus')
    }),

    vscode.commands.registerCommand('everywhere.moveToRightSidebar', async () => {
      // 1. Open/reveal the Secondary Side Bar on the right
      try {
        await vscode.commands.executeCommand('workbench.action.focusAuxiliaryBar')
      } catch {
        // Ignore
      }

      // 2. Guide the user cleanly without triggering VS Code's modal error
      vscode.window.showInformationMessage(
        'A barra direita foi aberta! Para fixar o chat nela: clique com o botão direito no título "EveryWhere Harness" e selecione "Mover para a Barra Lateral Secundária" (ou simplesmente arraste o ícone do polvo para lá).',
      )
    }),

    vscode.commands.registerCommand('everywhere.openInBrowser', async () => {
      if (dshManager.info.status === 'running' && dshManager.info.url) {
        await vscode.env.openExternal(vscode.Uri.parse(dshManager.info.url))
      } else {
        const start = await vscode.window.showInformationMessage(
          'EveryWhere Harness is not currently running.',
          'Start Harness',
        )
        if (start === 'Start Harness') {
          try {
            const url = await dshManager.start()
            await vscode.env.openExternal(vscode.Uri.parse(url))
          } catch (err: unknown) {
            const msg = err instanceof Error ? err.message : String(err)
            vscode.window.showErrorMessage(`Failed to start EveryWhere Harness: ${msg}`)
          }
        }
      }
    }),

    vscode.commands.registerCommand('everywhere.showLogs', () => {
      outputChannel.show(true)
    }),

    vscode.commands.registerCommand('everywhere.installHarness', async () => {
      try {
        await dshManager.installHarness()
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : String(err)
        vscode.window.showErrorMessage(`Falha na instalação do EveryWhere Harness: ${msg}`)
      }
    }),

    vscode.commands.registerCommand('everywhere.checkHarness', async () => {
      await vscode.window.withProgress(
        {
          location: vscode.ProgressLocation.Notification,
          title: 'Verificando instalação do EveryWhere Harness...',
          cancellable: false,
        },
        async () => {
          await dshManager.initStatus()
        },
      )
      if (dshManager.info.status === 'not_installed') {
        vscode.window.showWarningMessage(
          'EveryWhere Harness (dsh) não foi encontrado no sistema.',
          'Instalar Automaticamente',
        ).then((choice) => {
          if (choice === 'Instalar Automaticamente') {
            vscode.commands.executeCommand('everywhere.installHarness')
          }
        })
      } else {
        vscode.window.showInformationMessage('EveryWhere Harness está pronto e disponível!')
      }
    }),

    vscode.commands.registerCommand('everywhere.openSettings', () => {
      vscode.commands.executeCommand('workbench.action.openSettings', 'everywhere')
    }),
  )
}

/**
 * Extension deactivation hook.
 */
export function deactivate(): void {
  if (dshManagerInstance) {
    dshManagerInstance.dispose()
    dshManagerInstance = null
  }
}
