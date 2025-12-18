# Wrapper Maven en PowerShell
param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$MavenArgs
)

# Définir JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Vérifier Java
$javaExe = Join-Path $env:JAVA_HOME "bin\java.exe"
if (-not (Test-Path $javaExe)) {
    Write-Host "ERREUR: Java introuvable dans $env:JAVA_HOME" -ForegroundColor Red
    exit 1
}

# Créer le répertoire wrapper si nécessaire
$wrapperDir = ".mvn\wrapper"
$wrapperJar = "$wrapperDir\maven-wrapper.jar"
if (-not (Test-Path $wrapperJar)) {
    Write-Host "Téléchargement du wrapper Maven..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force -Path $wrapperDir | Out-Null
    $url = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    try {
        Invoke-WebRequest -Uri $url -OutFile $wrapperJar
        Write-Host "Wrapper Maven téléchargé avec succès!" -ForegroundColor Green
    } catch {
        Write-Host "ERREUR lors du téléchargement: $_" -ForegroundColor Red
        exit 1
    }
}

# Trouver le répertoire du projet (celui qui contient .mvn)
$projectDir = $PSScriptRoot
if (-not $projectDir) {
    $projectDir = Get-Location
}

# Exécuter Maven via le wrapper
$mavenArgsString = $MavenArgs -join " "
Write-Host "Exécution: mvn $mavenArgsString" -ForegroundColor Cyan

& $javaExe `
    "-Dmaven.multiModuleProjectDirectory=$projectDir" `
    "-classpath" "$wrapperJar" `
    "org.apache.maven.wrapper.MavenWrapperMain" `
    $MavenArgs


