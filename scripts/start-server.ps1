<#
    scripts/start-server.ps1

    Script PowerShell simple para iniciar el servidor (Spring Boot).
    - Requisitos: Java y Maven en PATH preferiblemente.
    - Comportamiento:
      1) Si hay `mvn`, entra en la carpeta `server` y ejecuta `mvn spring-boot:run`.
      2) Si no hay `mvn`, busca un jar compilado en `server/target` y lo ejecuta con `java -jar`.
      3) Si no hay nada, imprime instrucciones para compilar.

    Este script es intencionalmente simple y da mensajes útiles al usuario.
#>

Write-Host "[server] Iniciando server..."

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "[server] Maven detectado: lanzando 'mvn -f server spring-boot:run'..."
    Push-Location server
    & mvn -DskipTests spring-boot:run
    $code = $LASTEXITCODE
    Pop-Location
    exit $code
}

# Si no hay Maven, buscar archivo JAR en server/target
$jar = Get-ChildItem -Path server/target -Filter "restaurant-server*.jar" -File -ErrorAction SilentlyContinue |
       Sort-Object LastWriteTime -Descending | Select-Object -First 1

if ($jar) {
    Write-Host "[server] Maven no disponible: ejecutando JAR encontrado: $($jar.Name)"
    & java -jar $jar.FullName
    exit $LASTEXITCODE
}

Write-Host "[server] No se encontró Maven ni JAR compilado. Ejecuta 'mvn -f server package' o instala Maven/Java." -ForegroundColor Yellow
Exit 1
