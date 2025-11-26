## Comandos para lanzar

### Server

compilar server:

```powershell
mvn -f server clean package -DskipTests
```

Lanzar server:

```powershell
# Opción A - en desarrollo
mvn -f server spring-boot:run

# Opción B - usando el JAR empaquetado
java -jar server\target\restaurant-server-0.1.0.jar
```

### Front (aplicación de escritorio / GUI)

Si trabajas con el front (código en `src/main/java/es/ull/esit/app`), sigue estas opciones.

1) Verificar prerequisitos

```powershell
java -version
mvn -v   # opcional: recomendado para usar mvn exec
```

2) Compilar/empacar

```powershell
mvn clean package -DskipTests
```

3) Lanzar el front (opciones)

- Opción 1 — Usando Maven (desarrollo):

```powershell
mvn -DskipTests -Dexec.mainClass="es.ull.esit.app.JavaApplication2" exec:java
```

- Opción 2 — Ejecutar clases compiladas (después de `mvn package`):

```powershell
java -cp "target/classes;target/dependency/*" es.ull.esit.app.JavaApplication2
```

- Opción 3 — Usar el script PowerShell creado:

```powershell
.\scripts\start-front.ps1
```

Notas y troubleshooting:

- Si `mvn` no está en `PATH`, instala Maven o usa la Opción 2 con clases compiladas.
- Si la clase `main` no es `es.ull.esit.app.JavaApplication2`, edita la variable `$MainClass` en `scripts/start-front.ps1` o sustituye el nombre de la clase en los comandos.
- El front es una aplicación de escritorio (formularios `.form` en `src/main/resources/ui`), por tanto no usa `localhost` como una app web.

---

Si quieres, puedo añadir una versión `.bat` de los scripts o incluir comprobaciones automáticas (por ejemplo: esperar a que la base de datos esté disponible antes de arrancar el server). 

