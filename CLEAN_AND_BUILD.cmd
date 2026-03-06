@echo off
REM Clean all IntelliJ caches and restart

echo.
echo ========================================
echo NETTOYAGE COMPLET INTELLIJ
echo ========================================
echo.

cd /d c:\Users\ASUS\Downloads\adventures

echo Fermeture des processus IntelliJ...
taskkill /F /IM idea64.exe 2>nul
taskkill /F /IM java.exe 2>nul
timeout /t 3 /nobreak

echo.
echo Suppression des caches...
if exist ".idea\caches" rmdir /s /q ".idea\caches" 2>nul
if exist ".idea\indexedRefs" rmdir /s /q ".idea\indexedRefs" 2>nul
if exist ".idea\inspectionProfiles" rmdir /s /q ".idea\inspectionProfiles" 2>nul
if exist "out" rmdir /s /q "out" 2>nul
if exist "target" rmdir /s /q "target" 2>nul

echo Suppression de workspace.xml...
del /f /q ".idea\workspace.xml" 2>nul

echo.
echo ========================================
echo Nettoyage termine!
echo ========================================
echo.
echo Maven build (ligne de commande)...
echo.

call mvnw.cmd clean install -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo ✓ BUILD REUSSI !
    echo ========================================
    echo.
    echo Prochaines etapes:
    echo   1. Ouvrir IntelliJ IDEA
    echo   2. Menu: Maven ^> Reload All Maven Projects
    echo   3. Attendre l'indexation
    echo   4. Build ^> Build Project
    echo   5. Run ^> Run 'AdventuresApplication'
    echo.
    echo Ou en ligne de commande:
    echo   mvnw.cmd spring-boot:run
    echo.
) else (
    echo.
    echo ========================================
    echo ✗ BUILD ECHEC
    echo ========================================
    echo Regardez les erreurs ci-dessus
)

echo.
pause
