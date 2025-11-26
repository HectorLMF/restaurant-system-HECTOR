@echo off
echo ===================================
echo LANZAMIENTO COMPLETO DEL SISTEMA
echo ===================================
echo.

REM Paso 1: Levantar Docker con MySQL
echo [1/4] Iniciando base de datos MySQL con Docker...
docker compose up -d

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: No se pudo iniciar Docker
    pause
    exit /b 1
)

echo.
echo [2/4] Esperando a que MySQL este listo...
timeout /t 10 /nobreak > nul

REM Verificar que MySQL este realmente listo
:CHECK_MYSQL
docker exec restaurant-mysql mysqladmin ping -h localhost -u root -prootpass --silent > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Esperando conexion a MySQL...
    timeout /t 2 /nobreak > nul
    goto CHECK_MYSQL
)

echo MySQL esta listo!
echo.

REM Paso 2: Compilar el servidor
echo [3/4] Compilando el servidor...
cd server
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo
    cd ..
    pause
    exit /b 1
)

echo.
echo Compilacion exitosa!
echo.

REM Paso 3: Iniciar el servidor
echo [4/4] Iniciando servidor Spring Boot...
echo.
echo ===================================
echo SERVIDOR DISPONIBLE EN:
echo http://localhost:8080
echo.
echo ENDPOINTS DE PRUEBA:
echo - Health Check: http://localhost:8080/api/health
echo - DB Check: http://localhost:8080/api/db-check
echo ===================================
echo.
echo Presiona Ctrl+C para detener el servidor
echo.

call mvn spring-boot:run
