<#
    scripts/launch-system.ps1

    Script PowerShell "todo en uno" para lanzar el sistema:
      1) Levanta Docker (docker compose up -d)
      2) Espera a que MySQL dentro del contenedor `restaurant-mysql` responda
      3) Compila el middleware (server) y lo arranca en segundo plano
      4) Lanza el front (GUI)

    Comentarios:
    - Ejecuta este script desde la raíz del proyecto (o simplemente ejecútalo con la ruta completa).
    - Requiere: Docker, Java y (recomendado) Maven en PATH.
    - Usa `start-front.ps1` y arranca el server con `mvn spring-boot:run` en un proceso separado.

    Uso:
      powershell -ExecutionPolicy Bypass -File .\scripts\launch-system.ps1

#>

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

Write-Host "[launch] Script de lanzamiento iniciado" -ForegroundColor Cyan

# Ruta base del repo (scripts/..)
$ScriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$RepoRoot = Resolve-Path (Join-Path $ScriptRoot "..")
Write-Host "[launch] Repo root: $RepoRoot"

function Check-Command($name) {
    $cmd = Get-Command $name -ErrorAction SilentlyContinue
    return $null -ne $cmd
}

if (-not (Check-Command docker)) {
    Write-Error "Docker no está disponible en PATH. Instala/añade Docker antes de continuar."
    exit 1
}

Write-Host "[launch] Levantando Docker Compose (detached)..."
Push-Location $RepoRoot
try {
    & docker compose up -d
} catch {
    Write-Error "Fallo al ejecutar 'docker compose up -d': $_"
    Pop-Location
    exit 1
}

Write-Host "[launch] Esperando a que el contenedor 'restaurant-mysql' esté listo (ping mysqladmin)..."

$maxAttempts = 60
$attempt = 0
$mysqlReady = $false
while ($attempt -lt $maxAttempts) {
    $attempt++
    # Comprueba si el contenedor existe
    $container = (& docker ps --filter "name=restaurant-mysql" --format "{{.Names}}") -join ""
    if (-not [string]::IsNullOrEmpty($container)) {
        # Intenta hacer ping con mysqladmin. Redireccionamos salida a $null.
        & docker exec restaurant-mysql mysqladmin ping -h localhost -u root -prootpass --silent > $null 2>&1
        if ($LASTEXITCODE -eq 0) {
            $mysqlReady = $true
            break
        }
    }
    Write-Host "[launch] Esperando MySQL... intento $attempt/$maxAttempts"
    Start-Sleep -Seconds 2
}

if (-not $mysqlReady) {
    Write-Error "MySQL no respondió dentro del tiempo esperado. Revisa logs de Docker ('docker logs restaurant-mysql')."
    Pop-Location
    exit 1
}

Write-Host "[launch] MySQL está listo."

# Paso: compilar el servidor
Write-Host "[launch] Compilando el middleware (server)..."
if (-not (Check-Command mvn)) {
    Write-Warning "Maven no está en PATH. Intentaré continuar suponiendo que el JAR ya está empaquetado en server/target."
} else {
    Push-Location (Join-Path $RepoRoot 'server')
    $mvnCode = & mvn clean package -DskipTests
    $mvnExit = $LASTEXITCODE
    Pop-Location
    if ($mvnExit -ne 0) {
        Write-Error "La compilación del server falló (mvn exit code $mvnExit)."
        Pop-Location
        exit $mvnExit
    }
    Write-Host "[launch] Compilación del server finalizada correctamente."
}

# Iniciar el servidor en un proceso independiente (no bloqueante)
Write-Host "[launch] Iniciando el servidor (spring-boot:run) en un proceso separado..."
try {
    # Abrir un proceso mvn en el working dir server
    $proc = Start-Process -FilePath mvn -ArgumentList '-f','server','spring-boot:run' -WorkingDirectory (Join-Path $RepoRoot 'server') -NoNewWindow -PassThru
    Write-Host "[launch] Servidor arrancado (PID: $($proc.Id)). Esperando health endpoint..."
} catch {
    Write-Warning "No se pudo arrancar el servidor con Maven: $_. Intentando ejecutar JAR si existe..."
    $jar = Get-ChildItem -Path (Join-Path $RepoRoot 'server\target') -Filter 'restaurant-server*.jar' -File -ErrorAction SilentlyContinue | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($jar) {
        $proc = Start-Process -FilePath java -ArgumentList '-jar', $jar.FullName -WorkingDirectory (Join-Path $RepoRoot 'server') -NoNewWindow -PassThru
        Write-Host "[launch] Servidor arrancado desde JAR (PID: $($proc.Id))."
    } else {
        Write-Error "No se pudo arrancar el servidor ni con Maven ni con JAR."
        Pop-Location
        exit 1
    }
}

# Esperar health endpoint del server
$healthUrl = 'http://localhost:8080/api/health'
$maxHealthAttempts = 60
$healthAttempt = 0
$serverReady = $false
while ($healthAttempt -lt $maxHealthAttempts) {
    $healthAttempt++
    try {
        $resp = Invoke-WebRequest -Uri $healthUrl -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop
        if ($resp.StatusCode -ge 200 -and $resp.StatusCode -lt 300) {
            $serverReady = $true
            break
        }
    } catch {
        # ignore
    }
    Write-Host "[launch] Esperando servidor (health)... intento $healthAttempt/$maxHealthAttempts"
    Start-Sleep -Seconds 2
}

if (-not $serverReady) {
    Write-Warning "El servidor no respondió al endpoint de health. Continuando de todos modos, pero puede que no esté listo."
} else {
    Write-Host "[launch] Servidor disponible en http://localhost:8080"
}

# Lanzar el front (usa el script start-front.ps1 que ya existe)
Write-Host "[launch] Lanzando front..."
try {
    $frontScript = Join-Path $RepoRoot 'scripts\start-front.ps1'
    if (Test-Path $frontScript) {
        Start-Process -FilePath powershell -ArgumentList '-NoProfile','-ExecutionPolicy','Bypass','-File',$frontScript -WorkingDirectory $RepoRoot -NoNewWindow
        Write-Host "[launch] Front lanzado (script start-front.ps1)." -ForegroundColor Green
    } else {
        Write-Warning "No existe scripts\start-front.ps1. Ejecuta el front manualmente (ver comandos en README)."
    }
} catch {
    Write-Warning "Fallo al lanzar el front: $_"
}

Write-Host "[launch] Fin del script. El servidor sigue corriendo en segundo plano (revisa procesos o ventanas abiertas)." -ForegroundColor Cyan

Pop-Location
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
