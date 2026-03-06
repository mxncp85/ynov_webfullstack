@echo off
REM Nuclear option: Clean everything

echo.
echo ========================================
echo NETTOYAGE NUCLEAIRE
echo ========================================
echo.

echo 1. Fermeture IntelliJ...
taskkill /F /IM idea64.exe 2>nul
timeout /t 2 /nobreak

echo.
echo 2. Nettoyage du projet...
cd /d c:\Users\ASUS\Downloads\adventures

if exist ".idea\caches" rmdir /s /q ".idea\caches"
if exist ".idea\indexedRefs" rmdir /s /q ".idea\indexedRefs"
if exist ".idea\plugins" rmdir /s /q ".idea\plugins"
if exist ".idea\shelf" rmdir /s /q ".idea\shelf"
if exist ".idea\workspace.xml" del /f /q ".idea\workspace.xml"
if exist "out" rmdir /s /q "out"
if exist "target" rmdir /s /q "target"
if exist ".gradle" rmdir /s /q ".gradle"
if exist "build" rmdir /s /q "build"

echo.
echo 3. Nettoyage du cache system IntelliJ...
if exist "%APPDATA%\JetBrains\IntelliJIdea*\system\caches" (
    rmdir /s /q "%APPDATA%\JetBrains\IntelliJIdea*\system\caches"
)

echo.
echo ========================================
echo Nettoyage complet termine!
echo ========================================
echo.
echo Maintenant:
echo   1. Attendez 10 secondes
echo   2. Ouvrez IntelliJ IDEA
echo   3. Attendez l'indexation complete
echo   4. Executez: Maven ^> Reload All Maven Projects
echo.
pause
