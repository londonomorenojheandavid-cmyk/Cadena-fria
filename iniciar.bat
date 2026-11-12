@echo off
chcp 65001 >nul
echo ===================================================
echo   Iniciando FrioPesca - CadenaFria Uraba (Java)
echo ===================================================
if "%DB_USER%"=="" set DB_USER=root
if "%ADMIN_USER%"=="" set ADMIN_USER=admin
if "%ADMIN_PASSWORD%"=="" set ADMIN_PASSWORD=admin

echo Usuario Web: %ADMIN_USER%
echo Contrasena Web: %ADMIN_PASSWORD%
echo URL: http://localhost:8080
echo ===================================================

call "%~dp0mvnw.cmd" spring-boot:run
pause
