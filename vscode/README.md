<div align="center">

<img src="./media/everdock-banner.jpg" alt="Everywhere - Conectando suas IDEs ao DeepSeek" width="100%" />

# Everywhere DeepSeek Harness for Visual Studio Code

[English](#-english) • [Português](#-português)

</div>

---

## 🇺🇸 English

Run the official **DeepSeek Harness** autonomous AI agent everywhere directly inside Visual Studio Code.

**Everywhere** embeds and manages the authentic DeepSeek Harness agent engine (`dsh`) bound to your current workspace, providing the full web interface, autonomous tool pipeline, file inspection/editing, and session history directly within your IDE.

### Features

#### 1. Embedded DeepSeek Harness Webview
- **Sidebar Panel (Activity Bar)**: Open the DeepSeek Harness interface at any time from the Activity Bar icon.
- **Full Editor Tab (`DeepSeek Harness: Open Harness in Editor Tab`)**: Maximize your workspace by running the full Harness UI inside an editor tab.
- **Autonomous Agent Tools**: Full access to all Harness capabilities:
  - File reading, searching (ripgrep/glob), and editing.
  - Subprocess and terminal execution.
  - Session history, branching, and multi-turn planning.
  - Model switching and reasoning tokens (DeepSeek-V3 and DeepSeek-R1 Reasoner).

#### 2. Lifecycle & Workspace Process Management
- **Automatic Workspace Binding**: Harness starts with your open VS Code workspace folder as its working directory.
- **Status Bar Integration**:
  - `$(hubot) DSH: Active (:PORT)` — Shows current status and active port.
  - Quick access to start, stop, restart, or view logs.
- **Dedicated Output Channel**:
  - Live stdout/stderr logs from the Harness process available under the `DeepSeek Harness` output channel.

#### 3. Flexible Launch Modes
- Detects an installed `dsh` command from your system `PATH` or the extension's private storage.
- Configurable custom command/path (e.g. `pnpm dsh`).
- Configurable port and startup flags.

### Configuration

Open VS Code Settings (`Ctrl+,` or `Cmd+,`) and search for **DeepSeek**:

| Setting | Type | Default | Description |
|---|---|---|---|
| `deepseek.dshPath` | `string` | `""` | Custom command or path to dsh (e.g. `dsh`, `pnpm dsh`). Auto-detects if empty. |
| `deepseek.port` | `number` | `0` | Port for the Harness Web server (0 assigns a random available port). |
| `deepseek.profile` | `string` | `"web"` | DSH profile to boot (e.g. `web`). |
| `deepseek.autoStart` | `boolean` | `true` | Automatically boot the Harness runtime when the view is opened. |
| `deepseek.apiKey` | `string` | `""` | DeepSeek API key. Reads `DEEPSEEK_API_KEY` from environment or `.env` if blank. |
| `deepseek.baseUrl` | `string` | `https://api.deepseek.com` | DeepSeek API endpoint base URL. |

### Build & Install

```sh
# 1. Build extension bundle
node scripts/build.mjs

# 2. Package into .vsix
node scripts/package.mjs

# 3. Install into VS Code
code --install-extension everywhere-1.2.6.vsix
```

---

## 🇧🇷 Português

Execute o agente autônomo oficial **DeepSeek Harness** em qualquer lugar diretamente no Visual Studio Code.

O **Everywhere** incorpora e gerencia o motor autêntico do DeepSeek Harness (`dsh`) vinculado ao seu workspace atual, fornecendo interface web completa, pipeline de ferramentas autônomas, inspeção e edição de arquivos e histórico contínuo de sessões diretamente na sua IDE.

### Funcionalidades

#### 1. Webview Embutido do DeepSeek Harness
- **Painel na Barra Lateral (Activity Bar)**: Abra a interface do DeepSeek Harness a qualquer momento clicando no ícone da Activity Bar.
- **Aba Dedicada no Editor (`DeepSeek Harness: Open Harness in Editor Tab`)**: Maximize sua área de trabalho executando a interface completa do Harness em uma aba do editor.
- **Ferramentas do Agente Autônomo**: Acesso irrestrito a todos os recursos do Harness:
  - Leitura, busca (ripgrep/glob) e edição de arquivos em tempo real.
  - Execução de comandos no terminal e subprocessos.
  - Histórico de sessões, ramificações e planejamento multi-turn.
  - Alternância de modelos e tokens de raciocínio (DeepSeek-V3 e DeepSeek-R1 Reasoner).

#### 2. Gerenciamento do Ciclo de Vida e Workspace
- **Vínculo Automático com o Workspace**: O Harness inicia automaticamente com a pasta aberta no VS Code como diretório raiz de trabalho.
- **Integração com a Barra de Status**:
  - `$(hubot) DSH: Active (:PORT)` — Exibe o status atual e a porta em uso.
  - Acesso rápido para iniciar, parar, reiniciar ou exibir logs.
- **Canal de Saída Dedicado**:
  - Logs em tempo real de stdout/stderr disponíveis no canal de saída `DeepSeek Harness`.

#### 3. Modos Flexíveis de Inicialização
- Detecta automaticamente o comando `dsh` instalado no `PATH` do sistema ou no armazenamento privado da extensão.
- Permite configurar comando/caminho personalizado (ex: `pnpm dsh`).
- Permite personalizar porta e parâmetros de inicialização.

### Configurações

Abra as Configurações do VS Code (`Ctrl+,` ou `Cmd+,`) e pesquise por **DeepSeek**:

| Configuração | Tipo | Padrão | Descrição |
|---|---|---|---|
| `deepseek.dshPath` | `string` | `""` | Comando ou caminho customizado para o dsh (ex: `dsh`, `pnpm dsh`). Auto-detecta se vazio. |
| `deepseek.port` | `number` | `0` | Porta do servidor web do Harness (0 atribui uma porta livre aleatória). |
| `deepseek.profile` | `string` | `"web"` | Perfil do DSH a ser iniciado (ex: `web`). |
| `deepseek.autoStart` | `boolean` | `true` | Inicializar automaticamente o runtime ao abrir a visualização. |
| `deepseek.apiKey` | `string` | `""` | Chave de API DeepSeek (também lê `DEEPSEEK_API_KEY` do ambiente ou `.env`). |
| `deepseek.baseUrl` | `string` | `https://api.deepseek.com` | URL base do endpoint da API DeepSeek. |

### Compilação e Instalação

```sh
# 1. Compilar o bundle da extensão
node scripts/build.mjs

# 2. Empacotar no formato .vsix
node scripts/package.mjs

# 3. Instalar no VS Code
code --install-extension everywhere-1.2.6.vsix
```
