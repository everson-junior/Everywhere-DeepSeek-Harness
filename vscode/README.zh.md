# EveryWhere Harness for Visual Studio Code

[English](README.md) | 中文

EveryWhere Harness 是专为 Visual Studio Code 打造的 AI 编码助手和自主 Agent 扩展，集成了 **EveryWhere Harness** 自主智能体运行时。

---

## 功能特性

### 1. 专用侧边栏聊天（Activity Bar）
- **EveryWhere 侧边栏面板**：随时从活动栏图标打开对话。
- **原生主题适配**：完美适配深色、浅色与高对比度 VS Code 主题。
- **上下文关联**：一键将活动文件与选中的代码片段附带至提问中。

### 2. 双模式运行引擎
- **直接 API 模式**：通过 API 实现实时流式传输。
- **自主 Agent 模式（EveryWhere Harness）**：接入本地 `ewh` / `dsh` CLI，支持文件检索、跨文件编辑与命令执行等自主工作流。

---

## 配置项

打开 VS Code 设置（`Ctrl+,` 或 `Cmd+,`），搜索 **EveryWhere**：

| 配置项 | 类型 | 默认值 | 描述 |
|---|---|---|---|
| `everywhere.dshPath` | `string` | `""` | 运行时可执行文件路径（如 `ewh`, `dsh`）。 |
| `everywhere.projectPath` | `string` | `""` | 工作区路径。 |
| `everywhere.port` | `number` | `0` | Web 端口。 |
| `everywhere.profile` | `string` | `"web"` | 运行 profile。 |
| `everywhere.autoStart` | `boolean` | `true` | 是否自动启动。 |
| `everywhere.apiKey` | `string` | `""` | EveryWhere API 密钥。 |
| `everywhere.baseUrl` | `string` | `https://api.deepseek.com` | API 终端节点地址。 |

---

## 构建与安装

```sh
# 1. 构建扩展产物
node apps/vscode/scripts/build.mjs

# 2. 打包为 .vsix 安装包
node apps/vscode/scripts/package.mjs

# 3. 安装到 VS Code
code --install-extension everywhere-1.2.6.vsix
```
