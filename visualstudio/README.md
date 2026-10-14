<div align="center">

<img src="./Everywhere/Resources/Preview.png" alt="Everywhere - Visual Studio 2022 Extension" width="100%" />

# Everywhere DeepSeek Harness for Visual Studio 2022

[English](#-english) • [Português](#-português)

[![Visual Studio 2022](https://img.shields.io/badge/Visual%20Studio-2022%20(v17.0%2B)-purple?logo=visualstudio)](https://visualstudio.microsoft.com/)
[![.NET Framework](https://img.shields.io/badge/.NET-4.8%20%7C%20WPF-blue)](https://dotnet.microsoft.com/)
[![WebView2](https://img.shields.io/badge/Browser-Microsoft%20WebView2-teal)](https://developer.microsoft.com/en-us/microsoft-edge/webview2/)
[![DeepSeek](https://img.shields.io/badge/AI-DeepSeek--V3%20%7C%20DeepSeek--R1-007acc)](https://deepseek.com)

</div>

---

## 🇺🇸 English

**Everywhere DeepSeek Harness** is the official extension built specifically for **Microsoft Visual Studio 2022** (classic IDE, x64 architecture).

It embeds the official **DeepSeek Harness (`dsh`)** runtime inside a native **Tool Window** using **Microsoft Edge WebView2 + WPF**, providing an agile, hardware-accelerated interface directly integrated with your C#, C++, .NET, and Web solutions in Visual Studio.

### 🌟 Features

- 🪟 **Native Tool Window with WebView2**: Responsive, smooth, and hardware-accelerated rendering of the DeepSeek Harness web interface.
- 📂 **Workspace Solution Binding**: Starts automatically bound to the currently open Solution folder in Visual Studio (`DTE.Solution.FullName`).
- 🔄 **Intelligent Auto-Recovery (Exit Code 1)**: If old processes or ports (3080/3000) are busy, the manager automatically detects exit code 1, terminates orphaned processes, and restarts connection in seconds.
- ⚙️ **Integrated Options Page**: Settings directly available in `Tools -> Options -> Everywhere` (API keys, ports, profiles, auto-start).
- 🧭 **Quick Action Toolbar**: One-click buttons for Start (▶), Stop (🛑), Restart (🔄), Reload View (🔁), and Settings shortcut.
- 📋 **Dedicated Output Channel**: Real-time stdout and stderr logs in the Visual Studio **Output** window (select *Everywhere (DeepSeek Harness)*).

### 🚀 Build & Run

#### Prerequisites
1. **Visual Studio 2022** (Community, Professional, or Enterprise) with the following workload:
   - **Visual Studio extension development**
2. **DeepSeek Harness CLI**:
   ```cmd
   npm install -g @deepseek-ai/dsh
   ```

#### Build Steps
1. Open the solution `visualstudio/Everywhere.sln` in Visual Studio 2022.
2. Restore NuGet packages (Visual Studio performs this automatically upon build).
3. Select `Debug` or `Release` configuration and `Any CPU` or `x64` platform.
4. Press **F5** to start the Experimental Instance of Visual Studio (**Exp**).
5. In the experimental instance, navigate to:
   `View` ➔ `Other Windows` ➔ `Everywhere (DeepSeek Harness)`.

#### Generating `.vsix` for Direct Installation
- Build in `Release` mode: the installer `Everywhere.vsix` is generated at:
  `visualstudio/Everywhere/bin/Release/everywhere-visualstudio-1.2.6.vsix`.
- Double-click the generated `.vsix` file to install it into your local Visual Studio 2022.

### ⚙️ Settings (Tools ➔ Options ➔ Everywhere)

| Option | Default | Description |
|---|---|---|
| **Auto Start** | `true` | Automatically starts the agent when opening the Tool Window |
| **Port** | `0` | Web server port (0 assigns an available port automatically) |
| **Profile** | `"web"` | DeepSeek Harness execution profile (`web`, `headless`, `tui`) |
| **API Key** | `""` | DeepSeek API key (also accepted via `DEEPSEEK_API_KEY`) |
| **Base URL** | `https://api.deepseek.com` | DeepSeek API base endpoint |
| **Custom DSH Path** | `""` | Custom path to the `dsh` executable if not in system PATH |

---

## 🇧🇷 Português

Esta é a extensão oficial do **Everywhere** desenvolvida especificamente para o **Microsoft Visual Studio 2022** (IDE clássica, arquitetura x64).

Ela incorpora o runtime oficial do **DeepSeek Harness (`dsh`)** em uma janela de ferramentas nativa (**Tool Window**) utilizando **Microsoft Edge WebView2 + WPF**, oferecendo uma interface ágil e integrada diretamente ao seu fluxo de trabalho de soluções e projetos C#, C++, .NET e Web no Visual Studio.

### 🌟 Funcionalidades

- 🪟 **Tool Window Nativa com WebView2**: Interface responsiva e fluida com aceleração por hardware para renderização do DeepSeek Harness.
- 📂 **Workspace Solution Binding**: Inicia automaticamente vinculado à pasta da Solution aberta no Visual Studio (`DTE.Solution.FullName`).
- 🔄 **Auto-Recuperação Inteligente (Exit Code 1)**: Se houver processos antigos ou portas presas (3080/3000), o gerenciador detecta a saída com código 1, encerra serviços órfãos automaticamente e reinicia a conexão em segundos.
- ⚙️ **Página de Opções Integrada**: Configurações diretamente em `Tools -> Options -> Everywhere` (chaves de API, portas, perfis, auto-start).
- 🧭 **Barra de Ações Rápida**: Botões de início (▶), parada (🛑), reinicialização (🔄), recarregamento da página (🔁) e atalho para configurações.
- 📋 **Canal de Saída Dedicado**: Logs em tempo real na janela **Output** do Visual Studio (selecionando *Everywhere (DeepSeek Harness)*).

### 🚀 Como Compilar e Executar

#### Pré-requisitos
1. **Visual Studio 2022** (Community, Professional ou Enterprise) com a carga de trabalho:
   - **Desenvolvimento de extensão do Visual Studio** (Visual Studio extension development).
2. **DeepSeek Harness CLI**:
   ```cmd
   npm install -g @deepseek-ai/dsh
   ```

#### Passos de Compilação
1. Abra a solution `visualstudio/Everywhere.sln` no Visual Studio 2022.
2. Restaure os pacotes NuGet (o Visual Studio faz isso automaticamente na compilação).
3. Selecione a configuração `Debug` ou `Release` e a plataforma `Any CPU` ou `x64`.
4. Pressione **F5** para iniciar a Instância Experimental do Visual Studio (**Exp**).
5. Na instância experimental, vá em:
   `View` ➔ `Other Windows` ➔ `Everywhere (DeepSeek Harness)`.

#### Gerando o arquivo `.vsix` para Instalação Direta
- Compile em modo `Release`: o instalador `Everywhere.vsix` será gerado em:
  `visualstudio/Everywhere/bin/Release/everywhere-visualstudio-1.2.6.vsix`.
- Para instalar na sua máquina, dê dois cliques no arquivo `.vsix` gerado.

### ⚙️ Configurações (Tools ➔ Options ➔ Everywhere)

| Opção | Padrão | Descrição |
|---|---|---|
| **Auto Start** | `true` | Iniciar o agente automaticamente ao abrir a Tool Window |
| **Port** | `0` | Porta do servidor web (0 atribui porta livre automaticamente) |
| **Profile** | `"web"` | Perfil de execução do DeepSeek Harness (`web`, `headless`, `tui`) |
| **API Key** | `""` | Chave da API DeepSeek (também aceita via `DEEPSEEK_API_KEY`) |
| **Base URL** | `https://api.deepseek.com` | Endpoint base da API DeepSeek |
| **Custom DSH Path** | `""` | Caminho personalizado para o binário `dsh` se não estiver no PATH padrão |
