@echo off
REM Script rapide pour nettoyer et build

echo.
echo ======================================
echo 1. Nettoyage des caches IntelliJ
echo ======================================

cd /d c:\Users\ASUS\Downloads\adventures
if exist ".idea\caches" rmdir /s /q ".idea\caches" 2>nul
if exist "out" rmdir /s /q "out" 2>nul
if exist "target" rmdir /s /q "target" 2>nul

echo Caches nettoyes!

echo.
echo ======================================
echo 2. Build Maven
echo ======================================

call mvnw.cmd clean install -DskipTests

echo.
if %ERRORLEVEL% EQU 0 (
    echo ✓ BUILD REUSSI!
    echo.
    echo Prochaine etape: mvnw.cmd spring-boot:run
) else (
    echo ✗ BUILD FAILED
    echo Verifie les erreurs ci-dessus
)

pause
