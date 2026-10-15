<div align="center">

# Everywhere DeepSeek Harness for Eclipse IDE 🌐⚡

[English](#-english) • [Português](#-português)

[![Eclipse IDE](https://img.shields.io/badge/Eclipse%20IDE-2026--09%20(4.41.0)-2c2255?logo=eclipseide)](https://eclipse.org/)
[![Java Runtime](https://img.shields.io/badge/Java-17%2B%20%7C%2025-orange?logo=openjdk)](https://openjdk.org/)
[![Embedded Browser](https://img.shields.io/badge/SWT%20Browser-WebKitGTK%20%7C%20Edge%20%7C%20WebKit-blue)]()
[![DeepSeek](https://img.shields.io/badge/AI-DeepSeek--V3%20%7C%20DeepSeek--R1-007acc)](https://deepseek.com)
[![Maven Tycho](https://img.shields.io/badge/Build-Eclipse%20Tycho%204.0.8-blue?logo=apachemaven)](https://github.com/eclipse-tycho/tycho)
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

**Everywhere DeepSeek Harness** is the official extension developed for the **Eclipse IDE**, with validated native support for **Eclipse IDE for Java Developers (2026-09 / 4.41.0)** and compatible releases.

It embeds the official **DeepSeek Harness (`dsh`)** runtime inside a native Eclipse View using the **SWT Browser** widget, directly connecting **DeepSeek-V3** and **DeepSeek-R1 (Reasoner)** autonomous models to your development workspace.

### 🌟 Features

- 🪟 **Native View with Embedded Browser**: Displays the DeepSeek Harness interface inside a dedicated Eclipse View (`Window -> Show View -> Other... -> Everywhere`).
- 📂 **Workspace & Project Binding**: Automatically booted with the active Eclipse workspace or project directory (`IWorkspace` / `IProject`), enabling contextual code inspection and editing.
- 🔄 **Intelligent Auto-Recovery (Exit Code 1)**: Automated port conflict detection (3080/3000) or unexpected terminations, terminating orphaned processes (`fuser`, `pkill`, `taskkill`) and reconnecting transparently.
- 📄 **Integrated Output Console**: Real-time log stream in the standard Eclipse Console view (`Console` ➔ `Everywhere DeepSeek Harness`) with color-coded standard output and errors.
- 🧭 **Dedicated View Toolbar**:
  - ▶ **Start**: Boots the `dsh` runtime.
  - 🛑 **Stop**: Halts the process and releases network ports.
  - 🔄 **Restart**: Restarts the assistant session.
  - 🔁 **Reload**: Refreshes the embedded browser view.
  - 🌐 **Open in Browser**: Opens the web interface in your default system browser.
  - ⚙️ **Settings**: Direct shortcut to plugin preferences.
- ⚙️ **Eclipse Preferences Page**: Fully integrated under `Window` ➔ `Preferences` ➔ `Everywhere DeepSeek Harness`.
- ⌨️ **Workbench Menu & Commands**: Dedicated `Everywhere` menu on the main menu bar to start, stop, and toggle the view.

---

### 📦 Multi-Module Project Structure (Maven Tycho)

The project is organized in a standard multi-module Tycho structure:

```
eclipse/
├── pom.xml                                  # Root Parent POM (tycho-maven-plugin, target platforms, modules)
├── plugin/                                  # Submodule 1: OSGi Plug-in bundle
│   ├── pom.xml                              # packaging: eclipse-plugin
│   ├── META-INF/
│   │   └── MANIFEST.MF                      # OSGi Bundle manifest and dependencies
│   ├── build.properties                     # PDE build configuration
│   ├── plugin.xml                           # Extension points (Views, Menus, Commands, Preferences)
│   ├── icons/                               # Icons (PNG and SVG)
│   └── src/com/deepseek/everywhere/         # Java source files
├── feature/                                 # Submodule 2: Eclipse Feature
│   ├── pom.xml                              # packaging: eclipse-feature
│   ├── feature.xml                          # Feature definition (id: com.deepseek.everywhere.feature)
│   └── build.properties                     # Feature build properties
└── repository/                              # Submodule 3: p2 Update Site Repository
    ├── pom.xml                              # packaging: eclipse-repository (tycho-p2-repository-plugin)
    └── category.xml                         # p2 Category definition for Eclipse Marketplace
```

---

### 🚀 Building the p2 Update Site

#### Prerequisites
1. **Java JDK 17+** installed and set as default.
2. **Apache Maven 3.8+** installed (`mvn -version`).
3. **DeepSeek Harness CLI**:
   ```bash
   npm install -g @deepseek-ai/dsh
   ```

#### Maven Build Command
Inside the `eclipse` directory, run:

```bash
mvn clean verify
```

*(Optional: override target Eclipse mirror)*
```bash
mvn clean verify -Declipse.p2.repo.url=https://download.eclipse.org/releases/2024-06
```

#### 📁 Output Artifacts Location

After the build completes successfully:
- **p2 Update Site Directory**: `eclipse/repository/target/repository/`
  Inside this directory, the following required p2 repository files are generated:
  - `artifacts.jar` (or `artifacts.xml.xz`) — p2 artifact metadata repository
  - `content.jar` (or `content.xml.xz`) — p2 content metadata repository
  - `plugins/com.deepseek.everywhere_<version>.<timestamp>.jar` — compiled OSGi bundle
  - `features/com.deepseek.everywhere.feature_<version>.<timestamp>.jar` — compiled Feature bundle
- **Zipped Repository**: `eclipse/repository/target/com.deepseek.everywhere.repository-1.0.0-SNAPSHOT.zip`

---

### 🌐 Publishing to Eclipse Marketplace via GitHub Pages

1. **Deploy to GitHub Pages (`gh-pages`)**:
   Copy the contents of `eclipse/repository/target/repository/` to your `gh-pages` branch or repository `docs/` folder:
   ```bash
   # Example publishing to gh-pages branch:
   git checkout -B gh-pages
   cp -rf eclipse/repository/target/repository/* ./
   git add -A
   git commit -m "Deploy p2 Update Site v1.0.0"
   git push origin gh-pages --force
   ```
   Your p2 update site URL will be:
   `https://<username>.github.io/<repository-name>/`

2. **Register on Eclipse Marketplace**:
   - Access [Eclipse Marketplace Portal](https://marketplace.eclipse.org/).
   - Create a listing for **Everywhere DeepSeek Harness**.
   - In the **Update Site URL** field, enter your GitHub Pages URL:
     `https://<username>.github.io/<repository-name>/`
   - In the **Install Button / Feature ID** field, enter:
     `com.deepseek.everywhere.feature.group`

3. **Install in Eclipse**:
   In Eclipse IDE: `Help` ➔ `Install New Software...` ➔ Add your GitHub Pages URL ➔ Select **Everywhere - AI & DeepSeek Harness** ➔ Follow the wizard.

---

## 🇧🇷 Português

Esta é a extensão oficial do **Everywhere** desenvolvida para o **Eclipse IDE**, com suporte nativo e validado para **Eclipse IDE for Java Developers (2026-09 / 4.41.0)** e versões compatíveis.

Ela incorpora o runtime oficial do **DeepSeek Harness (`dsh`)** em uma View nativa do Eclipse utilizando o widget **SWT Browser**, conectando os modelos autônomos **DeepSeek-V3** e **DeepSeek-R1 (Reasoner)** diretamente ao seu workspace de desenvolvimento.

### 🌟 Funcionalidades

- 🪟 **View Nativa com Navegador Embutido**: Exibe a interface do DeepSeek Harness diretamente em uma aba/view do Eclipse (`Window -> Show View -> Other... -> Everywhere`).
- 📂 **Workspace & Project Binding**: O assistente é iniciado com o diretório do projeto ou workspace atualmente ativo (`IWorkspace` / `IProject`), permitindo inspeção, criação e edição de código contextual.
- 🔄 **Auto-Recuperação Inteligente (Exit Code 1)**: Detecção automática de conflito de portas (3080/3000) ou encerramento inesperado, finalizando processos órfãos (`fuser`, `pkill`, `taskkill`) e reiniciando a conexão de forma transparente.
- 📄 **Console de Logs Integrado**: Canal de log em tempo real na aba padrão do Eclipse (`Console` ➔ `Everywhere DeepSeek Harness`) com formatação e diferenciação por cores de saída e erro.
- 🧭 **Barra de Ferramentas Dedicada (View Toolbar)**:
  - ▶ **Iniciar**: Inicia o runtime `dsh`.
  - 🛑 **Parar**: Interrompe o processo e libera portas de rede.
  - 🔄 **Reiniciar**: Reinicia a sessão do assistente.
  - 🔁 **Recarregar**: Atualiza a página do navegador embutido.
  - 🌐 **Abrir no Navegador**: Abre a interface web no browser padrão do sistema operacional.
  - ⚙️ **Configurações**: Acesso direto às preferências do plugin.
- ⚙️ **Página de Preferências do Eclipse**: Integrada ao menu padrão `Window` ➔ `Preferences` ➔ `Everywhere DeepSeek Harness`.
- ⌨️ **Menu e Atalhos no Workbench**: Menu `Everywhere` na barra principal com comandos para iniciar, parar e abrir a visão.

---

### 📦 Estrutura Multi-Módulo (Maven Tycho)

O projeto está estruturado em 3 submódulos padrão Tycho:

1. **`plugin/` (`com.deepseek.everywhere`)**: Submódulo com packaging `eclipse-plugin`. Contém o código Java, `MANIFEST.MF` OSGi, `plugin.xml` e ícones.
2. **`feature/` (`com.deepseek.everywhere.feature`)**: Submódulo com packaging `eclipse-feature`. Contém o `feature.xml` que agrupa e referencia o plugin principal.
3. **`repository/` (`com.deepseek.everywhere.repository`)**: Submódulo com packaging `eclipse-repository`. Contém o `category.xml` organizando a Feature dentro de uma categoria legível e empacota o p2 Update Site completo.

---

### 🚀 Como Compilar e Gerar o p2 Update Site

#### Comando de Build Maven
No diretório `eclipse/`:

```bash
mvn clean verify
```

*(Caso precise especificar outro espelho p2 de destino)*:
```bash
mvn clean verify -Declipse.p2.repo.url=https://download.eclipse.org/releases/2024-06
```

#### 📁 Diretório de Saída dos Artefatos p2

Ao término do comando `mvn clean verify`, os artefatos finais são gerados no seguinte diretório:

```
eclipse/repository/target/repository/
```

Dentro desta pasta estarão os artefatos essenciais do p2 Update Site:
- 📄 `artifacts.jar` (índice de metadados dos artefatos)
- 📄 `content.jar` (índice de metadados de capacidades e dependências)
- 📁 `plugins/` (contendo `com.deepseek.everywhere_<versão>.<timestamp>.jar`)
- 📁 `features/` (contendo `com.deepseek.everywhere.feature_<versão>.<timestamp>.jar`)
- 📦 Arquivo ZIP completo: `eclipse/repository/target/com.deepseek.everywhere.repository-1.0.0-SNAPSHOT.zip`

---

### 🌐 Publicação no GitHub Pages e Eclipse Marketplace

1. **Publicar no GitHub Pages**:
   Envie o conteúdo de `eclipse/repository/target/repository/` para o branch `gh-pages` ou pasta `docs/` do seu repositório:
   ```bash
   git checkout -B gh-pages
   cp -rf eclipse/repository/target/repository/* ./
   git add -A
   git commit -m "Publicação p2 Update Site Everywhere DeepSeek"
   git push origin gh-pages --force
   ```
   A URL do seu Update Site será:
   `https://<usuario>.github.io/<repositorio>/`

2. **Cadastrar no Eclipse Marketplace**:
   - Faça login no [Eclipse Marketplace Portal](https://marketplace.eclipse.org/).
   - Preencha o cadastro da sua solução **Everywhere DeepSeek Harness**.
   - No campo **Update Site URL**, informe o link do GitHub Pages.
   - No campo **Feature ID / Install Button**, utilize: `com.deepseek.everywhere.feature.group`.

3. **Instalação no Eclipse IDE**:
   - No Eclipse: `Help` ➔ `Install New Software...`
   - Adicione a URL do GitHub Pages e selecione a categoria **Everywhere - AI & DeepSeek Harness**.
