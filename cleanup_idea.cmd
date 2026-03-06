@echo off
REM Script pour nettoyer et reconfigurer IntelliJ IDEA

echo ======================================
echo Cleaning IntelliJ IDEA Cache
echo ======================================

REM Supprimer les dossiers de cache
cd /d c:\Users\ASUS\Downloads\adventures

echo.
echo Deleting IntelliJ cache and build directories...
if exist ".idea\caches" rmdir /s /q ".idea\caches" 2>nul
if exist ".idea\shelf" rmdir /s /q ".idea\shelf" 2>nul
if exist "out" rmdir /s /q "out" 2>nul
if exist "target" rmdir /s /q "target" 2>nul
if exist "adventures\target" rmdir /s /q "adventures\target" 2>nul

echo.
echo Deleting workspace.xml to reset IDE state...
if exist ".idea\workspace.xml" del ".idea\workspace.xml" 2>nul

echo.
echo ======================================
echo Clean complete!
echo ======================================
echo.
echo Next steps:
echo 1. Close IntelliJ IDEA completely
echo 2. Wait 5 seconds
echo 3. Reopen IntelliJ IDEA
echo 4. Wait for indexing to complete
echo 5. Right-click on 'adventures' module and select "Mark Directory as" > "Unmark as Sources Root"
echo 6. Then Maven > Reload Projects
echo.
pause
