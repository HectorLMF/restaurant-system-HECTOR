# Guía de Prueba del Servidor

## 🚀 Inicio Rápido

### Opción 1: Script Automático (Recomendado)

#### Windows PowerShell:
```powershell
.\launch-system.ps1
```

#### Windows CMD:
```cmd
launch-system.bat
```

Este script realiza automáticamente:
1. ✓ Inicia la base de datos MySQL con Docker
2. ✓ Espera a que MySQL esté completamente listo
3. ✓ Compila el servidor
4. ✓ Inicia el servidor Spring Boot

### Opción 2: Manual

#### 1. Iniciar la base de datos:
```bash
docker compose up -d
```

#### 2. Esperar 10-15 segundos para que MySQL inicie completamente

#### 3. Compilar e iniciar el servidor:
```bash
cd server
mvn clean package -DskipTests
mvn spring-boot:run
```

## 🧪 Pruebas de Conectividad

### Prueba Automática

Una vez que el servidor esté en ejecución, ejecuta:

```powershell
.\test-connectivity.ps1
```

Este script verifica:
- ✓ Estado del servidor (Health Check)
- ✓ Conectividad con la base de datos (DB Check)

### Prueba Manual

#### 1. Health Check del Servidor
```bash
curl http://localhost:8080/api/health
```

**Respuesta esperada:**
```json
{
  "status": "UP",
  "timestamp": 1700000000000,
  "service": "Restaurant Server"
}
```

#### 2. Verificación de Base de Datos
```bash
curl http://localhost:8080/api/db-check
```

**Respuesta esperada:**
```json
{
  "status": "UP",
  "database": "Connected",
  "catalog": "project3",
  "url": "jdbc:mysql://localhost:3306/project3",
  "timestamp": 1700000000000
}
```

### Usando PowerShell (Invoke-RestMethod)

```powershell
# Health Check
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get

# Database Check
Invoke-RestMethod -Uri "http://localhost:8080/api/db-check" -Method Get
```

### Usando el Navegador

Simplemente abre:
- http://localhost:8080/api/health
- http://localhost:8080/api/db-check

## 📊 Endpoints Disponibles

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/health` | GET | Verifica el estado del servidor |
| `/api/db-check` | GET | Verifica la conectividad con MySQL |

## 🔍 Solución de Problemas

### El servidor no inicia
1. Verifica que Docker esté ejecutándose
2. Verifica que el puerto 8080 esté disponible
3. Revisa los logs en la consola

### Error de conexión a la base de datos
1. Verifica que el contenedor MySQL esté corriendo: `docker ps`
2. Verifica que el puerto 3306 esté disponible
3. Prueba la conexión directa: `docker exec restaurant-mysql mysqladmin ping -h localhost -u root -prootpass`

### Ver logs de Docker
```bash
docker logs restaurant-mysql
```

### Ver contenedores activos
```bash
docker ps
```

### Reiniciar la base de datos
```bash
docker compose down
docker compose up -d
```

## 📝 Configuración

La configuración de la base de datos se encuentra en:
- `server/src/main/resources/application.properties`

Parámetros importantes:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/project3
spring.datasource.username=root
spring.datasource.password=rootpass
```

## 🛑 Detener el Sistema

1. **Detener el servidor**: Presiona `Ctrl+C` en la terminal donde se ejecuta el servidor
2. **Detener Docker**: 
   ```bash
   docker compose down
   ```

## ✅ Lista de Verificación

Antes de probar, asegúrate de que:
- [ ] Docker Desktop está corriendo
- [ ] El puerto 3306 está libre (MySQL)
- [ ] El puerto 8080 está libre (Spring Boot)
- [ ] Java 11 o superior está instalado
- [ ] Maven está instalado y configurado
