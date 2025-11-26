# Script PowerShell para probar la conectividad del servidor
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "PRUEBA DE CONECTIVIDAD" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

# Probar Health Check
Write-Host "[1/2] Probando Health Check..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/health" -Method Get -TimeoutSec 5
    Write-Host "✓ Health Check OK" -ForegroundColor Green
    Write-Host "  Status: $($response.status)" -ForegroundColor White
    Write-Host "  Service: $($response.service)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "✗ Health Check FAILED" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

# Probar Database Check
Write-Host "[2/2] Probando Database Check..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/db-check" -Method Get -TimeoutSec 5
    Write-Host "✓ Database Check OK" -ForegroundColor Green
    Write-Host "  Status: $($response.status)" -ForegroundColor White
    Write-Host "  Database: $($response.database)" -ForegroundColor White
    Write-Host "  Catalog: $($response.catalog)" -ForegroundColor White
    Write-Host "  URL: $($response.url)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "✗ Database Check FAILED" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Prueba completada" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
