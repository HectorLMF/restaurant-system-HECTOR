# Script PowerShell para lanzar y probar el sistema
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "LANZAMIENTO COMPLETO DEL SISTEMA" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

# Paso 1: Levantar Docker con MySQL
Write-Host "[1/4] Iniciando base de datos MySQL con Docker..." -ForegroundColor Yellow
docker compose up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: No se pudo iniciar Docker" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[2/4] Esperando a que MySQL este listo..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Verificar que MySQL este realmente listo
$maxAttempts = 30
$attempt = 0
while ($attempt -lt $maxAttempts) {
    docker exec restaurant-mysql mysqladmin ping -h localhost -u root -prootpass --silent 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "MySQL esta listo!" -ForegroundColor Green
        break
    }
    Write-Host "Esperando conexion a MySQL... (intento $($attempt + 1)/$maxAttempts)"
    Start-Sleep -Seconds 2
    $attempt++
}

if ($attempt -eq $maxAttempts) {
    Write-Host "ERROR: MySQL no respondio a tiempo" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Paso 2: Compilar el servidor
Write-Host "[3/4] Compilando el servidor..." -ForegroundColor Yellow
Set-Location server
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: La compilacion fallo" -ForegroundColor Red
    Set-Location ..
    exit 1
}

Write-Host ""
Write-Host "Compilacion exitosa!" -ForegroundColor Green
Write-Host ""

# Paso 3: Iniciar el servidor
Write-Host "[4/4] Iniciando servidor Spring Boot..." -ForegroundColor Yellow
Write-Host ""
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "SERVIDOR DISPONIBLE EN:" -ForegroundColor Cyan
Write-Host "http://localhost:8080" -ForegroundColor Green
Write-Host ""
Write-Host "ENDPOINTS DE PRUEBA:" -ForegroundColor Cyan
Write-Host "- Health Check: http://localhost:8080/api/health" -ForegroundColor White
Write-Host "- DB Check: http://localhost:8080/api/db-check" -ForegroundColor White
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Presiona Ctrl+C para detener el servidor" -ForegroundColor Yellow
Write-Host ""

mvn spring-boot:run
