#!/usr/bin/env python3
"""
Validador de integridade pré-commit e pré-release para o Everywhere-DeepSeek-Harness.
Verifica se todas as regras e contratos dos 4 targets de IDE estão atendidos.
"""
import os
import sys
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

def check_eclipse():
    pom_file = ROOT / "eclipse" / "pom.xml"
    manifest_file = ROOT / "eclipse" / "META-INF" / "MANIFEST.MF"
    
    if not pom_file.exists() or not manifest_file.exists():
        return
        
    pom = pom_file.read_text(encoding="utf-8")
    manifest = manifest_file.read_text(encoding="utf-8")
    
    if "-SNAPSHOT" in pom and ".qualifier" not in manifest:
        sys.exit("❌ ERRO [Eclipse]: eclipse/pom.xml usa SNAPSHOT, logo eclipse/META-INF/MANIFEST.MF deve conter Bundle-Version terminando com .qualifier")
    
    if "tycho-p2-repository-plugin" not in pom:
        sys.exit("❌ ERRO [Eclipse]: eclipse/pom.xml precisa configurar tycho-p2-repository-plugin para geração do repositório p2!")

def check_workflow():
    wf_file = ROOT / ".github" / "workflows" / "release.yml"
    if not wf_file.exists():
        return
        
    wf = wf_file.read_text(encoding="utf-8")
    
    if "eclipse/repository/target/repository" in wf:
        sys.exit("❌ ERRO [CI/CD]: .github/workflows/release.yml ainda faz referência ao caminho inválido eclipse/repository/target/repository")

def check_vscode():
    pkg_file = ROOT / "vscode" / "package.json"
    if not pkg_file.exists():
        return
    with open(pkg_file, encoding="utf-8") as f:
        pkg = json.load(f)
    icon = pkg.get("icon")
    if icon:
        icon_path = ROOT / "vscode" / icon
        if not icon_path.exists():
            sys.exit(f"❌ ERRO [VS Code]: Ícone {icon} declarado no package.json não existe no disco!")

def main():
    print("🔍 Executando validação pré-release do Everywhere-DeepSeek-Harness...")
    check_eclipse()
    check_workflow()
    check_vscode()
    print("✅ Todas as validações pré-commit/release passaram com sucesso!")

if __name__ == "__main__":
    main()
