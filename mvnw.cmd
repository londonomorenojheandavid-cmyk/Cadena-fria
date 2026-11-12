@echo off
setlocal enabledelayedexpansion

:: 1. Buscar un JDK 17 o superior (con javac.exe)
set "FOUND_JDK="

for /d %%D in (
    "C:\Program Files\Eclipse Adoptium\jdk-21*"
    "C:\Program Files\Eclipse Adoptium\jdk-17*"
    "C:\Program Files\Java\jdk-21*"
    "C:\Program Files\Java\jdk-17*"
    "C:\Program Files\Microsoft\jdk-21*"
    "C:\Program Files\Microsoft\jdk-17*"
    "C:\Program Files\BellSoft\LibericaJDK-21*"
    "C:\Program Files\BellSoft\LibericaJDK-17*"
    "%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk*"
    "%USERPROFILE%\.jdks\*"
    "%USERPROFILE%\.m2\wrapper\jdk-21\*"
) do (
    if exist "%%~fD\bin\javac.exe" (
        set "FOUND_JDK=%%~fD"
        goto :use_jdk
    )
)

:: Buscar dentro de la extension de Java de VS Code si tiene javac.exe
for /d %%E in ("%USERPROFILE%\.vscode\extensions\redhat.java*") do (
    for /d %%J in ("%%~fE\jre\*") do (
        if exist "%%~fJ\bin\javac.exe" (
            set "FOUND_JDK=%%~fJ"
            goto :use_jdk
        )
    )
)

:: Si no hay JDK 17/21 instalado, descargar OpenJDK 21 portable automaticamente
set "JDK_WRAPPER_DIR=%USERPROFILE%\.m2\wrapper\jdk-21"
if not exist "%JDK_WRAPPER_DIR%" mkdir "%JDK_WRAPPER_DIR%"
echo No se detecto JDK 17/21 en el sistema. Descargando OpenJDK 21 portable por unica vez...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ProgressPreference = 'SilentlyContinue'; " ^
    "$zip = '%JDK_WRAPPER_DIR%\jdk21.zip'; " ^
    "Invoke-WebRequest -Uri 'https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk' -OutFile $zip; " ^
    "Expand-Archive -Path $zip -DestinationPath '%JDK_WRAPPER_DIR%' -Force; " ^
    "Remove-Item $zip -Force"

for /d %%D in ("%JDK_WRAPPER_DIR%\*") do (
    if exist "%%~fD\bin\javac.exe" (
        set "FOUND_JDK=%%~fD"
        goto :use_jdk
    )
)

:use_jdk
if defined FOUND_JDK (
    set "JAVA_HOME=%FOUND_JDK%"
    set "PATH=%FOUND_JDK%\bin;%PATH%"
    echo Usando JDK: %JAVA_HOME%
)

:: 2. Descargar o usar Apache Maven 3.9.9
set "MAVEN_VERSION=3.9.9"
set "WRAPPER_DIR=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%"
set "MAVEN_BIN=%WRAPPER_DIR%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd"

if not exist "%MAVEN_BIN%" (
    echo Descargando Apache Maven %MAVEN_VERSION% por unica vez...
    if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$ProgressPreference = 'SilentlyContinue'; " ^
        "$zip = '%WRAPPER_DIR%\maven.zip'; " ^
        "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile $zip; " ^
        "Expand-Archive -Path $zip -DestinationPath '%WRAPPER_DIR%' -Force; " ^
        "Remove-Item $zip -Force"
)

if exist "%MAVEN_BIN%" (
    call "%MAVEN_BIN%" %*
    exit /b %ERRORLEVEL%
) else (
    echo [ERROR] No se pudo descargar Maven automaticamente.
    exit /b 1
)
