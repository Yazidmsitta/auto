# Script PowerShell pour exécuter l'application facilement

Write-Host "Application de Gestion d'Atelier Automobile" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green
Write-Host ""

# Vérifier Java
Write-Host "Vérification de Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "Java trouvé: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "ERREUR: Java n'est pas installé ou pas dans le PATH" -ForegroundColor Red
    Write-Host "Veuillez installer Java 11 ou supérieur" -ForegroundColor Red
    exit 1
}

# Vérifier si mvnw.cmd existe
if (Test-Path "mvnw.cmd") {
    Write-Host "Utilisation du wrapper Maven..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Compilation et exécution de l'application..." -ForegroundColor Cyan
    Write-Host ""
    
    # Exécuter avec le wrapper
    & .\mvnw.cmd javafx:run
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "Erreur lors de l'exécution. Vérifiez les messages ci-dessus." -ForegroundColor Red
    }
} else {
    Write-Host "ERREUR: mvnw.cmd introuvable" -ForegroundColor Red
    Write-Host "Assurez-vous d'être dans le répertoire du projet" -ForegroundColor Red
    exit 1
}

