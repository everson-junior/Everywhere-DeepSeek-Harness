<div align="center">

# EveryWhere Harness for Eclipse IDE 🌐⚡

[English](#-english) • [Português](#-português)

[![Eclipse IDE](https://img.shields.io/badge/Eclipse%20IDE-2026--09%20(4.41.0)-2c2255?logo=eclipseide)](https://eclipse.org/)
[![Java Runtime](https://img.shields.io/badge/Java-17%2B%20%7C%2025-orange?logo=openjdk)](https://openjdk.org/)
[![Embedded Browser](https://img.shields.io/badge/SWT%20Browser-WebKitGTK%20%7C%20Edge%20%7C%20WebKit-blue)]()
[![EveryWhere](https://img.shields.io/badge/AI-EveryWhere-007acc)](https://deepseek.com)
[![Status](https://img.shields.io/badge/Status-Ready-success)]()

<br/>

<div align="center">
  <a href="https://marketplace.eclipse.org/content/everywhere-deepseek-harness">
    <img src="https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png" alt="Drag and drop into a running Eclipse IDE to install"/>
  </a>
</div>

</div>

---

## 🇺🇸 English

**EveryWhere Harness** is the extension developed for the **Eclipse IDE**, with validated native support for **Eclipse IDE for Java Developers (2026-09 / 4.41.0)** and compatible releases.

It embeds the **EveryWhere Harness (`ewh` / `dsh`)** runtime inside a native Eclipse View using the **SWT Browser** widget, directly connecting autonomous AI reasoning to your development workspace.

### 🌟 Features

- 🪟 **Native View with Embedded Browser**: Displays the EveryWhere Harness interface inside a dedicated Eclipse View (`Window -> Show View -> Other... -> Everywhere`).
- 📂 **Workspace & Project Binding**: Automatically booted with the active Eclipse workspace or project directory (`IWorkspace` / `IProject`), enabling contextual code inspection and editing.
- 🔄 **Intelligent Auto-Recovery (Exit Code 1)**: Automated port conflict detection (3080/3000) or unexpected terminations, terminating orphaned processes (`fuser`, `pkill`, `taskkill`) and reconnecting transparently.
- 📄 **Integrated Output Console**: Real-time log stream in the standard Eclipse Console view (`Console` ➔ `EveryWhere Harness`) with color-coded standard output and errors.
- 🧭 **Dedicated View Toolbar**:
  - ▶ **Start**: Boots the `ewh` / `dsh` runtime.
  - 🛑 **Stop**: Halts the process and releases network ports.
  - 🔄 **Restart**: Restarts the assistant session.
  - 🔁 **Reload**: Refreshes the embedded browser view.
  - 🌐 **Open in Browser**: Opens the web interface in your default system browser.
  - ⚙️ **Settings**: Direct shortcut to plugin preferences.
- ⚙️ **Eclipse Preferences Page**: Fully integrated under `Window` ➔ `Preferences` ➔ `EveryWhere Harness`.
- ⌨️ **Workbench Menu & Commands**: Dedicated `Everywhere` menu on the main menu bar to start, stop, and toggle the view.

### 🚀 Build & Package

#### Prerequisites
1. **Eclipse IDE 2026-09 (4.41.0)** or any compatible Eclipse distribution.
2. **Java 17+** or **Java 25** (Eclipse bundled compiler is automatically detected).
3. **EveryWhere Harness CLI**:
   ```bash
   npm install -g @everywhere-harness/ewh
   ```

#### Quick Build & Local Installation

Inside the `eclipse` directory:

```bash
# Add execution permissions (if needed)
chmod +x build.sh

# Build and install directly into your local Eclipse
./build.sh --install
```

The script will:
1. Detect the Java compiler (bundled in Eclipse or system JDK).
2. Locate Eclipse target libraries (`org.eclipse.ui`, `swt`, `core.runtime`, etc.).
3. Compile all source files under `eclipse/src/`.
4. Package the OSGi bundle at `eclipse/build/com.deepseek.everywhere_1.0.0.jar`.
5. Install into `dropins` and `plugins` folders, updating `bundles.info`.

#### Manual Installation

If you prefer copying the `.jar` bundle manually:
1. Run `./build.sh` to produce the package.
2. Copy the generated file:
   ```bash
   cp eclipse/build/com.deepseek.everywhere_1.0.0.jar <eclipse_path>/dropins/
   ```
3. Start or restart Eclipse with `-clean`:
   ```bash
   eclipse -clean
   ```

### 🎯 How to Use in Eclipse

1. Open **Eclipse IDE**.
2. Open the Everywhere view:
   - Menu `Window` ➔ `Show View` ➔ `Other...`
   - Expand the **Everywhere** category and select **EveryWhere Harness**.
   - Or click the Everywhere icon on the top workbench toolbar.
3. If **Auto Start** is enabled, EveryWhere Harness will boot automatically.
4. To monitor diagnostics, switch to the **Console** view and select `EveryWhere Harness`.

### ⚙️ Preferences (Window ➔ Preferences ➔ Everywhere)

| Option | Default | Description |
|---|---|---|
| **Auto Start** | `true` | Automatically starts the agent when opening the View |
| **HTTP Server Port** | `0` | Local port (0 assigns an available port automatically) |
| **Harness Profile** | `"web"` | Execution profile (`web`, `headless`, `tui`) |
| **EveryWhere API Key** | `""` | EveryWhere API key (also inherited from `EVERYWHERE_API_KEY` / `DEEPSEEK_API_KEY`) |
| **API Base URL** | `https://api.deepseek.com` | EveryWhere API endpoint base URL |
| **Custom 'dsh' Path** | `""` | Custom binary path if `ewh` / `dsh` is not in standard PATH |

### 🏛️ Architecture & Project Structure

```
eclipse/
├── .classpath                   # Eclipse PDE classpath configuration
├── .project                     # Eclipse project descriptor
├── build.properties             # PDE build configuration
├── build.sh                     # Compilation and auto-installation script
├── pom.xml                      # Maven Tycho configuration
├── plugin.xml                   # Views, Menus, Commands, and Preferences registry
├── META-INF/
│   └── MANIFEST.MF              # OSGi Bundle manifest and dependencies
├── icons/                       # Action and view icons (PNG and SVG)
└── src/com/deepseek/everywhere/
    ├── Activator.java           # Bundle lifecycle and cleanup on Eclipse shutdown
    ├── actions/                 # Toolbar actions (Start, Stop, Restart, etc.)
    ├── console/                 # Integration with Eclipse MessageConsole
    ├── handlers/                # Workbench command handlers
    ├── model/                   # State enums and listener interfaces
    ├── preferences/             # Preference pages and default initializers
    ├── service/                 # Process lifecycle and resilience manager (DshManager)
    └── ui/                      # SWT Browser view integration (EverywhereView)
```

---

## 🇧🇷 Português

Esta é a extensão oficial do **EveryWhere** desenvolvida para o **Eclipse IDE**, com suporte nativo e validado para **Eclipse IDE for Java Developers (2026-09 / 4.41.0, Build id: 20260903-0720)** e versões compatíveis.

Ela incorpora o runtime oficial do **EveryWhere Harness (`ewh` / `dsh`)** em uma View nativa do Eclipse utilizando o widget **SWT Browser**, conectando os modelos autônomos diretamente ao seu workspace de desenvolvimento.

### 🌟 Funcionalidades

- 🪟 **View Nativa com Navegador Embutido**: Exibe a interface do EveryWhere Harness diretamente em uma aba/view do Eclipse (`Window -> Show View -> Other... -> Everywhere`).
- 📂 **Workspace & Project Binding**: O assistente é iniciado com o diretório do projeto ou workspace atualmente ativo (`IWorkspace` / `IProject`), permitindo inspeção, criação e edição de código contextual.
- 🔄 **Auto-Recuperação Inteligente (Exit Code 1)**: Detecção automática de conflito de portas (3080/3000) ou encerramento inesperado, finalizando processos órfãos (`fuser`, `pkill`, `taskkill`) e reiniciando a conexão de forma transparente.
- 📄 **Console de Logs Integrado**: Canal de log em tempo real na aba padrão do Eclipse (`Console` ➔ `EveryWhere Harness`) com formatação e diferenciação por cores de saída e erro.
- 🧭 **Barra de Ferramentas Dedicada (View Toolbar)**:
  - ▶ **Iniciar**: Inicia o runtime `ewh`.
  - 🛑 **Parar**: Interrompe o processo e libera portas de rede.
  - 🔄 **Reiniciar**: Reinicia a sessão do assistente.
  - 🔁 **Recarregar**: Atualiza a página do navegador embutido.
  - 🌐 **Abrir no Navegador**: Abre a interface web no browser padrão do sistema operacional.
  - ⚙️ **Configurações**: Acesso direto às preferências do plugin.
- ⚙️ **Página de Preferências do Eclipse**: Integrada ao menu padrão `Window` ➔ `Preferences` ➔ `EveryWhere Harness`.
- ⌨️ **Menu e Atalhos no Workbench**: Menu `EveryWhere` na barra principal com comandos para iniciar, parar e abrir a visão.

### 🚀 Como Compilar e Empacotar o Plugin

#### Pré-requisitos
1. **Eclipse IDE 2026-09 (4.41.0)** ou qualquer instalação Eclipse compatível.
2. **Java 17+** ou **Java 25** (o compilador bundled do Eclipse é detectado automaticamente).
3. **EveryWhere Harness CLI**:
   ```bash
   npm install -g @everywhere-harness/ewh
   ```

#### Compilação e Instalação Rápida

Dentro do diretório `eclipse`:

```bash
# Dar permissão de execução (se necessário)
chmod +x build.sh

# Compilar e instalar diretamente no Eclipse local
./build.sh --install
```

O script irá:
1. Detectar o compilador Java (bundled no Eclipse ou JDK do sistema).
2. Localizar as bibliotecas do Eclipse (`org.eclipse.ui`, `swt`, `core.runtime`, etc.).
3. Compilar os arquivos fontes em `eclipse/src/`.
4. Gerar o pacote OSGi `eclipse/build/com.deepseek.everywhere_1.0.0.jar`.
5. Instalar nas pastas `dropins` e `plugins` da sua instalação Eclipse, registrando no `bundles.info`.

#### Instalação Manual

Se preferir copiar manualmente o arquivo `.jar`:

1. Gere o pacote executando `./build.sh`.
2. Copie o arquivo gerado:
   ```bash
   cp eclipse/build/com.deepseek.everywhere_1.0.0.jar <caminho_do_eclipse>/dropins/
   ```
   *(No Snap do Ubuntu, o diretório é: `~/snap/eclipse/common/eclipse/dropins/`)*
3. Inicie ou reinicie o Eclipse com o parâmetro `-clean` se necessário:
   ```bash
   eclipse -clean
   ```

### 🎯 Como Usar no Eclipse

1. Abra o **Eclipse IDE 2026-09**.
2. Abra a visão EveryWhere:
   - Menu `Window` ➔ `Show View` ➔ `Other...`
   - Expanda a categoria **EveryWhere** e selecione **EveryWhere Harness**.
   - Ou clique no ícone do EveryWhere na barra de ferramentas superior.
3. Se a opção **Auto Start** estiver habilitada, o EveryWhere Harness iniciará automaticamente.
4. Caso esteja parado, clique no botão **▶ Iniciar EveryWhere Harness** na tela inicial ou na barra de ações.
5. Para acompanhar diagnósticos, abra a visualização **Console** e selecione o console `EveryWhere Harness`.

### ⚙️ Configurações (Window ➔ Preferences ➔ EveryWhere Harness)

| Opção | Padrão | Descrição |
|---|---|---|
| **Iniciar automaticamente** | `true` | Inicia o runtime automaticamente ao abrir a visão |
| **Porta do Servidor HTTP** | `0` | Porta local (0 atribui porta livre automaticamente) |
| **Perfil do Harness** | `"web"` | Perfil de inicialização do EveryWhere Harness (`web`, `headless`, `tui`) |
| **EveryWhere API Key** | `""` | Chave de API EveryWhere (também herdada de `EVERYWHERE_API_KEY` ou `DEEPSEEK_API_KEY`) |
| **URL Base da API** | `https://api.deepseek.com` | Endpoint da API EveryWhere |
| **Caminho customizado do executável** | `""` | Caminho personalizado para o binário `ewh` ou `dsh` caso não esteja no PATH |

### 🏛️ Estrutura do Código

```
eclipse/
├── .classpath                   # Configuração de classpath do Eclipse PDE
├── .project                     # Configuração de projeto do Eclipse
├── build.properties             # Descritor de build PDE
├── build.sh                     # Script de compilação e auto-instalação
├── pom.xml                      # Configuração para Maven Tycho
├── plugin.xml                   # Registro de Views, Menus, Commands e Preferences
├── META-INF/
│   └── MANIFEST.MF              # Manifesto OSGi com dependências do Eclipse
├── icons/                       # Ícones de ações e janelas (PNG e SVG)
└── src/com/deepseek/everywhere/
    ├── Activator.java           # Ciclo de vida do bundle e limpeza ao fechar o Eclipse
    ├── actions/                 # Ações da barra de ferramentas (Start, Stop, Restart, etc.)
    ├── console/                 # Integração com MessageConsole do Eclipse
    ├── handlers/                # Manipuladores de comandos do Workbench
    ├── model/                   # Enums de estado e interfaces de ouvintes
    ├── preferences/             # Páginas e inicializadores de configurações
    ├── service/                 # Gerenciador de ciclo de vida e resiliência de processos (DshManager)
    └── ui/                      # Visualização SWT com navegador embutido (EverywhereView)
```
