<#
    scripts/start-front.ps1

    Script PowerShell simple para iniciar la aplicación "front" (GUI).
    - Requisitos: Java y Maven en PATH (si quieres ejecutar vía Maven).
    - Comportamiento:
      1) Si hay `mvn` disponible, ejecuta `mvn exec:java` usando la clase principal indicada.
      2) Si no hay `mvn` pero hay clases compiladas en `target/classes`, intenta iniciar con `java -cp`.
      3) Si no se cumple nada de lo anterior, muestra instrucciones para compilar/instalar.

    Ajustes: modifica `$MainClass` si la clase con `main` es otra.
#>

$MainClass = "es.ull.esit.app.JavaApplication2"  # Cambia esto si tu main es otra clase

Write-Host "[front] Iniciando front (clase principal: $MainClass)..."

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    # Usar Maven para ejecutar con el classpath correcto (resuelve dependencias automáticamente)
    Write-Host "[front] Maven detectado: lanzando con 'mvn exec:java'..."
    & mvn -DskipTests exec:java -Dexec.mainClass="$MainClass"
    exit $LASTEXITCODE
}

if (Test-Path "target/classes") {
    # Intento directo contra clases compiladas (útil si ya ejecutaste 'mvn package' o 'mvn compile')
    Write-Host "[front] Maven no encontrado; ejecutando clases compiladas en 'target/classes'..."
    $cp = "target/classes;target/dependency/*"
    & java -cp $cp $MainClass
    exit $LASTEXITCODE
}

Write-Host "[front] No se encontró Maven ni clases compiladas. Ejecuta 'mvn package' o instala Maven/Java en PATH." -ForegroundColor Yellow
Exit 1
