<div align="center">

<img src="./assets/everdock-banner.jpg" alt="Everywhere DeepSeek Harness - Conectando suas IDEs ao DeepSeek" width="100%" />

# Everywhere DeepSeek Harness 🌐⚡

**Everywhere DeepSeek Harness — Connecting your IDEs to DeepSeek**

[![VS Code Extension](https://img.shields.io/badge/VS%20Code-Extension-blue?logo=visualstudiocode)](./vscode)
[![Visual Studio 2022](https://img.shields.io/badge/Visual%20Studio-2022%20(v17.0%2B)-purple?logo=visualstudio)](./visualstudio)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-2024.1%2B-red?logo=intellijidea)](./intellij)
[![Eclipse IDE](https://img.shields.io/badge/Eclipse%20IDE-2026--09%20(4.41.0)-2c2255?logo=eclipseide)](./eclipse)
[![DeepSeek](https://img.shields.io/badge/AI-DeepSeek--V3%20%7C%20DeepSeek--R1-007acc)](https://deepseek.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active%20Development-success)]()

[English](#-english) • [Português](#-português)

</div>

---

## 🇺🇸 English

*Everywhere is the definitive bridge to run the DeepSeek Harness anywhere — bringing the autonomous intelligence of DeepSeek models directly into your daily development environment and workflow.*

### 📖 Overview

**Everywhere** turns your editor into a complete command center for **DeepSeek** intelligent agents. The name **Everywhere** reflects the flexibility of running the assistant anywhere: it automates the installation, boot, and lifecycle management of the official **DeepSeek Harness (`dsh`)** runtime, binding it natively to your active workspace.

With Everywhere, **DeepSeek-V3** and **DeepSeek-R1 (Reasoner)** models gain supervised access to your real project context: code inspection, file editing, integrated terminal execution, continuous session history, and autonomous tools with zero complex setup.

### ✨ Key Features

- 🌐 **DeepSeek Everywhere**: Seamless management and execution of the `dsh` runtime across any project.
- 🤖 **Embedded Autonomous Agent**: High-performance Webview panel in sidebars and dedicated full editor tabs.
- 🔄 **Auto-Recovery & Resilience**: Automatic handling for busy ports (3080/3000) and cleanup of orphaned processes with transparent reconnection on exit code 1.
- 📂 **Real Workspace Context**: The agent runs bound to your open project directory, ensuring pinpoint accuracy in terminal commands and code modifications.
- 🛡️ **Secure Session Proxy**: Automatic cookie-signed authentication handshake and iframe restriction neutralization (`CSP`/`X-Frame-Options`).
- ⚡ **Full Lifecycle Control**: Fast start, restart, and shutdown with live real-time output channel logs.
- ⚙️ **Multi-Profile Support**: Compatible with DeepSeek Harness profiles (`web`, `headless`, `tui`) and custom API keys.

### 🧩 Available Extensions

| IDE | Technology | Distribution | Documentation |
|---|---|---|---|
| **VS Code** | TypeScript, Webview | Visual Studio Marketplace (`.vsix`) | [VS Code README](./vscode/README.md) |
| **Visual Studio 2022** | C#, WPF, Microsoft Edge WebView2 | Visual Studio Marketplace (`.vsix`) | [Visual Studio README](./visualstudio/README.md) |
| **IntelliJ IDEA** | Kotlin, Gradle, JCEF Chromium | JetBrains Plugin (`.zip`) | [IntelliJ README](./intellij/README.md) |
| **Eclipse IDE** | Java 17+, OSGi Tycho, SWT Browser | [Eclipse Marketplace](https://marketplace.eclipse.org/content/everywhere-deepseek-harness) (p2 Site) | [Eclipse README](./eclipse/README.md) |

<br/>

<div align="center">
  <a href="https://marketplace.eclipse.org/content/everywhere-deepseek-harness">
    <img src="https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png" alt="Drag and drop into a running Eclipse IDE to install Everywhere DeepSeek Harness in Eclipse"/>
  </a>
</div>

---

## 🇧🇷 Português

*Everywhere é a ponte definitiva para rodar o DeepSeek Harness em qualquer lugar — trazendo a inteligência autônoma dos modelos DeepSeek diretamente para o seu ambiente de desenvolvimento e fluxo de trabalho diário.*

### 📖 Visão Geral

O **Everywhere** transforma o seu editor em uma central completa para agentes inteligentes do **DeepSeek**. O nome **Everywhere** reflete a flexibilidade de poder rodar o assistente em qualquer lugar: ele automatiza a instalação, inicialização e gerenciamento do runtime oficial do **DeepSeek Harness (`dsh`)**, integrando-o nativamente ao seu workspace.

Com isso, os modelos **DeepSeek-V3** e **DeepSeek-R1 (Reasoner)** ganham acesso supervisionado ao contexto real do projeto: edição e inspeção de código, execução no terminal integrado, histórico contínuo de sessões e ferramentas autônomas sem a necessidade de configurações complexas.

### ✨ Recursos Principais

- 🌐 **DeepSeek em Qualquer Lugar**: Gerenciamento e instalação transparente do runtime `dsh`, permitindo o uso ágil em qualquer projeto.
- 🤖 **Agente Autônomo Embutido**: Painel Webview de alta performance na barra lateral e em abas dedicadas do editor (`Editor Tab`).
- 🔄 **Auto-Recuperação & Resiliência**: Tratamento automático para portas ocupadas e encerramento de processos órfãos — reconexão transparente caso ocorra falha inicial de conexão (`exit code 1`).
- 📂 **Contexto Real do Workspace**: O agente roda vinculado à pasta do seu projeto aberto, garantindo precisão em comandos de terminal e edição de código.
- 🛡️ **Proxy de Sessão Seguro**: Handshake de autenticação automático via cookies assinados e neutralização de restrições de iframe (`CSP`/`X-Frame-Options`).
- ⚡ **Gerenciamento de Ciclo de Vida**: Início rápido, reinicialização e parada direta de processos com logs em tempo real no canal de saída.
- ⚙️ **Suporte a Múltiplos Perfis**: Compatível com perfis do DeepSeek Harness (`web`, `headless`, `tui`) e chaves personalizadas de API.

### 🧩 Extensões Disponíveis

| IDE | Tecnologia | Distribuição | Documentação |
|---|---|---|---|
| **VS Code** | TypeScript, Webview | Visual Studio Marketplace (`.vsix`) | [README do VS Code](./vscode/README.md) |
| **Visual Studio 2022** | C#, WPF, Microsoft Edge WebView2 | Visual Studio Marketplace (`.vsix`) | [README do Visual Studio](./visualstudio/README.md) |
| **IntelliJ IDEA** | Kotlin, Gradle, JCEF Chromium | Plugin JetBrains (`.zip`) | [README do IntelliJ](./intellij/README.md) |
| **Eclipse IDE** | Java 17+, OSGi PDE, SWT Browser | Plugin Eclipse (`.jar`) | [README do Eclipse](./eclipse/README.md) |

---

## 📁 Estrutura do Projeto / Project Structure

```text
everywhere/
├── .github/workflows/
│   └── release.yml             # CI/CD: Multi-IDE packaging & Marketplace publishing
├── assets/                     # Banners & icons
├── visualstudio/               # Microsoft Visual Studio 2022 extension (C# / WPF / VSIX)
├── intellij/                   # IntelliJ IDEA 2024.1+ plugin (Kotlin / Gradle / JCEF)
├── eclipse/                    # Eclipse IDE 2026-09 plugin (Java 17+ / OSGi / SWT Browser)
├── vscode/                     # Visual Studio Code extension (TypeScript / Webview)
└── README.md                   # Main Project Hub
```

---

## 📄 Licença / License

Distribuído sob os termos da licença [MIT](LICENSE).
Distributed under the terms of the [MIT](LICENSE) License.
