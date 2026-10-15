@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Script for Windows
@REM Automatically bootstraps Apache Maven 3.9.6
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set MAVEN_VERSION=3.9.6
set TARGET_DIR=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%\apache-maven-%MAVEN_VERSION%
set MVN_EXEC=%TARGET_DIR%\bin\mvn.cmd

if not exist "%MVN_EXEC%" (
    echo [mvnw] Baixando Apache Maven %MAVEN_VERSION% automaticamente...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$url = 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip';" ^
        "$zip = \"$env:TEMP\apache-maven-%MAVEN_VERSION%-bin.zip\";" ^
        "$dest = \"%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%\";" ^
        "New-Item -ItemType Directory -Force -Path $dest | Out-Null;" ^
        "Write-Host '[mvnw] Fazendo download de ' $url '...';" ^
        "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12;" ^
        "Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing;" ^
        "Write-Host '[mvnw] Extraindo Maven %MAVEN_VERSION%...';" ^
        "Expand-Archive -Path $zip -DestinationPath $dest -Force;" ^
        "Remove-Item -Force $zip;"
)

if not exist "%MVN_EXEC%" (
    echo [ERRO] Nao foi possivel inicializar o Apache Maven %MAVEN_VERSION%.
    exit /b 1
)

call "%MVN_EXEC%" %*
