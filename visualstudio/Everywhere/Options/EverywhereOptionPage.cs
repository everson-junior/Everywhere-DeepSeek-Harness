using System.ComponentModel;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.Shell;

namespace Everywhere.Options
{
    [Guid("470559a1-cb9b-4f92-933e-e673f47e24a7")]
    [ComVisible(true)]
    public class EverywhereOptionPage : DialogPage
    {
        [Category("EveryWhere Harness")]
        [DisplayName("Auto Start")]
        [Description("Automatically start EveryWhere Harness when the tool window is opened.")]
        public bool AutoStart { get; set; } = true;

        [Category("EveryWhere Harness")]
        [DisplayName("Port")]
        [Description("Port for EveryWhere Harness Web server (0 for auto-assign).")]
        public int Port { get; set; } = 0;

        [Category("EveryWhere Harness")]
        [DisplayName("Profile")]
        [Description("EveryWhere Harness profile to boot (e.g. 'web').")]
        public string Profile { get; set; } = "web";

        [Category("EveryWhere Harness")]
        [DisplayName("Custom DSH Path")]
        [Description("Custom command or path to harness executable. If empty, auto-detects from PATH or npm.")]
        public string CustomDshPath { get; set; } = "";

        [Category("EveryWhere API")]
        [DisplayName("API Key")]
        [Description("EveryWhere API Key. If empty, reads EVERYWHERE_API_KEY or DEEPSEEK_API_KEY environment variable.")]
        [PasswordPropertyText(true)]
        public string ApiKey { get; set; } = "";

        [Category("EveryWhere API")]
        [DisplayName("Base URL")]
        [Description("EveryWhere API Base URL.")]
        public string BaseUrl { get; set; } = "https://api.deepseek.com";
    }
}
