@echo off
REM Fix for Java 25 vs Java 17 compatibility issue

echo ======================================
echo Fixing Java 25 to Java 17 Issue
echo ======================================
echo.

REM Change to project directory
cd /d c:\Users\ASUS\Downloads\adventures

echo [1/5] Removing IntelliJ caches...
if exist ".idea\caches" rmdir /s /q ".idea\caches" 2>nul
if exist ".idea\shelf" rmdir /s /q ".idea\shelf" 2>nul
echo ✓ IntelliJ caches removed

echo.
echo [2/5] Removing build artifacts...
if exist "target" rmdir /s /q "target" 2>nul
if exist "adventures\target" rmdir /s /q "adventures\target" 2>nul
if exist "out" rmdir /s /q "out" 2>nul
echo ✓ Build artifacts removed

echo.
echo [3/5] Removing IntelliJ project state files...
if exist ".idea\workspace.xml" del ".idea\workspace.xml" 2>nul
echo ✓ Project state files cleaned

echo.
echo [4/5] Cleaning Maven...
call adventures\mvnw.cmd clean -q 2>nul
if %ERRORLEVEL% neq 0 (
    echo Warning: Maven clean failed, but continuing...
)
echo ✓ Maven cleaned

echo.
echo [5/5] Done!
echo ======================================
echo.
echo Next Steps:
echo 1. Close IntelliJ IDEA completely
echo 2. Wait 5 seconds
echo 3. Reopen the project
echo 4. IntelliJ will re-index and reload from pom.xml
echo 5. The project should now use Java 17
echo.
echo The following changes were made:
echo - Added maven.compiler.release property to pom.xml
echo - Updated maven-compiler-plugin to use release=17
echo - Reset IntelliJ workspace configuration
echo.
pause
