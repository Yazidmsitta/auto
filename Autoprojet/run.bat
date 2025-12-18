@echo off
REM Script batch pour exécuter l'application facilement

echo Application de Gestion d'Atelier Automobile
echo =============================================
echo.

REM Vérifier Java
echo Verification de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERREUR: Java n'est pas installe ou pas dans le PATH
    echo Veuillez installer Java 11 ou superieur
    pause
    exit /b 1
)

REM Vérifier si mvnw.cmd existe
if exist "mvnw.cmd" (
    echo Utilisation du wrapper Maven...
    echo.
    echo Compilation et execution de l'application...
    echo.
    
    REM Exécuter avec le wrapper
    call mvnw.cmd javafx:run
    
    if %errorlevel% neq 0 (
        echo.
        echo Erreur lors de l'execution. Verifiez les messages ci-dessus.
        pause
    )
) else (
    echo ERREUR: mvnw.cmd introuvable
    echo Assurez-vous d'etre dans le repertoire du projet
    pause
    exit /b 1
)

