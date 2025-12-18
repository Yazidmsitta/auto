# Script PowerShell pour compiler le projet
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

Write-Host "Compilation du projet..." -ForegroundColor Cyan
Write-Host "JAVA_HOME: '$env:JAVA_HOME'" -ForegroundColor Yellow

# Vérifier Java
$javaExe = Join-Path $env:JAVA_HOME "bin\java.exe"
if (-not (Test-Path $javaExe)) {
    Write-Host "ERREUR: Java introuvable dans $env:JAVA_HOME" -ForegroundColor Red
    exit 1
}

# Télécharger Maven si nécessaire
$mavenDir = ".maven"
if (-not (Test-Path "$mavenDir\bin\mvn.cmd")) {
    Write-Host "Maven n'est pas installé. Utilisation d'une approche alternative..." -ForegroundColor Yellow
    Write-Host "Veuillez installer Maven ou utiliser un IDE (IntelliJ IDEA, Eclipse, NetBeans)" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Pour installer Maven:" -ForegroundColor Cyan
    Write-Host "1. Téléchargez depuis https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host "2. Extrayez dans un dossier (ex: C:\Tools\apache-maven)" -ForegroundColor White
    Write-Host "3. Ajoutez le dossier\bin au PATH" -ForegroundColor White
    Write-Host ""
    Write-Host "OU utilisez un IDE qui gère Maven automatiquement!" -ForegroundColor Green
    exit 1
}

# Utiliser Maven
& "$mavenDir\bin\mvn.cmd" clean compile
