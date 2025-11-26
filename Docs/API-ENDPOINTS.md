# API REST Endpoints - Restaurant System

## 🔗 Base URL
```
http://localhost:8080
```

---

## 📋 Índice de Endpoints

1. [Health & Database](#health--database)
2. [Menu Completo](#menu-completo)
3. [Main Course (Platos Principales)](#main-course-platos-principales)
4. [Appetizers (Aperitivos)](#appetizers-aperitivos)
5. [Drinks (Bebidas)](#drinks-bebidas)
6. [Cashiers (Cajeros)](#cashiers-cajeros)

---

## Health & Database

### Health Check
Verifica que el servidor está funcionando.

**GET** `/api/health`

**Response:**
```json
{
  "status": "UP",
  "timestamp": 1700000000000,
  "service": "Restaurant Server"
}
```

### Database Check
Verifica la conectividad con la base de datos.

**GET** `/api/db-check`

**Response:**
```json
{
  "status": "UP",
  "database": "Connected",
  "catalog": "project3",
  "url": "jdbc:mysql://localhost:3306/project3",
  "timestamp": 1700000000000
}
```

---

## Menu Completo

### Obtener Menú Completo
Devuelve todos los platos, aperitivos y bebidas en un solo endpoint.

**GET** `/api/menu`

**Response:**
```json
{
  "mainCourses": [
    {
      "id": 1,
      "name": "Buratta Pizza",
      "price": 54
    },
    ...
  ],
  "appetizers": [
    {
      "id": 1,
      "name": "Truffel Fries",
      "price": 23
    },
    ...
  ],
  "drinks": [
    {
      "id": 1,
      "name": "cola",
      "price": 6
    },
    ...
  ],
  "totalItems": 15
}
```

---

## Main Course (Platos Principales)

### Listar Todos los Platos Principales
**GET** `/api/maincourse`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Buratta Pizza",
    "price": 54
  },
  {
    "id": 2,
    "name": "Pink Pasta",
    "price": 12
  }
]
```

### Obtener un Plato Principal por ID
**GET** `/api/maincourse/{id}`

**Ejemplo:** `/api/maincourse/1`

**Response:**
```json
{
  "id": 1,
  "name": "Buratta Pizza",
  "price": 54
}
```

### Crear Nuevo Plato Principal
**POST** `/api/maincourse`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Lasagna",
  "price": 45
}
```

**Response:**
```json
{
  "id": 6,
  "name": "Lasagna",
  "price": 45
}
```

### Actualizar Plato Principal
**PUT** `/api/maincourse/{id}`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Lasagna Premium",
  "price": 55
}
```

**Response:**
```json
{
  "id": 6,
  "name": "Lasagna Premium",
  "price": 55
}
```

### Eliminar Plato Principal
**DELETE** `/api/maincourse/{id}`

**Ejemplo:** `/api/maincourse/6`

**Response:** `204 No Content`

---

## Appetizers (Aperitivos)

### Listar Todos los Aperitivos
**GET** `/api/appetizers`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Truffel Fries",
    "price": 23
  },
  {
    "id": 2,
    "name": "Molten Chocolate",
    "price": 12
  }
]
```

### Obtener un Aperitivo por ID
**GET** `/api/appetizers/{id}`

### Crear Nuevo Aperitivo
**POST** `/api/appetizers`

**Body:**
```json
{
  "name": "Onion Rings",
  "price": 15
}
```

### Actualizar Aperitivo
**PUT** `/api/appetizers/{id}`

### Eliminar Aperitivo
**DELETE** `/api/appetizers/{id}`

---

## Drinks (Bebidas)

### Listar Todas las Bebidas
**GET** `/api/drinks`

**Response:**
```json
[
  {
    "id": 1,
    "name": "cola",
    "price": 6
  },
  {
    "id": 2,
    "name": "7up",
    "price": 6
  }
]
```

### Obtener una Bebida por ID
**GET** `/api/drinks/{id}`

### Crear Nueva Bebida
**POST** `/api/drinks`

**Body:**
```json
{
  "name": "Sprite",
  "price": 6
}
```

### Actualizar Bebida
**PUT** `/api/drinks/{id}`

### Eliminar Bebida
**DELETE** `/api/drinks/{id}`

---

## Cashiers (Cajeros)

### Listar Todos los Cajeros
**GET** `/api/cashiers`

**Response:**
```json
[
  {
    "id": 4231,
    "name": "abdualmajeed",
    "salary": 7000
  },
  {
    "id": 4232,
    "name": "abdualrahman",
    "salary": 7000
  }
]
```

### Obtener un Cajero por ID
**GET** `/api/cashiers/{id}`

**Ejemplo:** `/api/cashiers/4231`

### Obtener un Cajero por Nombre
**GET** `/api/cashiers/name/{name}`

**Ejemplo:** `/api/cashiers/name/abdualmajeed`

### Crear Nuevo Cajero
**POST** `/api/cashiers`

**Body:**
```json
{
  "id": 4233,
  "name": "Carlos",
  "salary": 7500
}
```

### Actualizar Cajero
**PUT** `/api/cashiers/{id}`

**Body:**
```json
{
  "id": 4233,
  "name": "Carlos Martinez",
  "salary": 8000
}
```

### Eliminar Cajero
**DELETE** `/api/cashiers/{id}`

---

## 🧪 Ejemplos de Uso con cURL

### Obtener todos los platos
```bash
curl http://localhost:8080/api/maincourse
```

### Crear un nuevo plato
```bash
curl -X POST http://localhost:8080/api/maincourse \
  -H "Content-Type: application/json" \
  -d '{"name":"Carbonara","price":40}'
```

### Actualizar un plato
```bash
curl -X PUT http://localhost:8080/api/maincourse/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Buratta Pizza Premium","price":60}'
```

### Eliminar un plato
```bash
curl -X DELETE http://localhost:8080/api/maincourse/6
```

---

## 📝 Códigos de Estado HTTP

- `200 OK` - Operación exitosa
- `201 Created` - Recurso creado exitosamente
- `204 No Content` - Eliminación exitosa
- `404 Not Found` - Recurso no encontrado
- `503 Service Unavailable` - Error de conectividad con la base de datos

---

## 🔄 Patrones CRUD

Todos los recursos (MainCourse, Appetizers, Drinks, Cashiers) siguen el mismo patrón CRUD:

| Operación | Método HTTP | Endpoint | Descripción |
|-----------|-------------|----------|-------------|
| Create | POST | `/api/{recurso}` | Crear nuevo |
| Read All | GET | `/api/{recurso}` | Listar todos |
| Read One | GET | `/api/{recurso}/{id}` | Obtener por ID |
| Update | PUT | `/api/{recurso}/{id}` | Actualizar |
| Delete | DELETE | `/api/{recurso}/{id}` | Eliminar |

---

## 💡 Notas

1. Todos los endpoints devuelven JSON
2. Los IDs son autogenerados para MainCourse, Appetizers y Drinks
3. Para Cashiers, el ID debe ser proporcionado manualmente
4. El servidor debe estar corriendo en el puerto 8080
5. La base de datos MySQL debe estar activa en Docker
