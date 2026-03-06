@echo off
echo ========================================
echo Starting MongoDB in Docker
echo ========================================

docker run -d -p 27017:27017 --name mongodb-adventures mongo:latest

if %errorlevel% neq 0 (
    echo MongoDB container might already exist, trying to start it...
    docker start mongodb-adventures
)

echo.
echo MongoDB started successfully on port 27017
echo Database: adventures_logs
echo Collection: api_logs
echo.
echo To stop MongoDB: docker stop mongodb-adventures
echo To remove MongoDB: docker rm mongodb-adventures
echo.
echo Now you can start the Spring Boot application!
echo.
pause
