# 📱 API REST - Tienda Online de Productos

## ✅ Estado del Proyecto

**Construido con:**
- ✅ Spring Boot 4.0.6
- ✅ Spring Data JPA (ORM)
- ✅ H2 Database (desarrollo)
- ✅ Validaciones con Bean Validation
- ✅ DTOs para request/response
- ✅ Transacciones @Transactional
- ✅ Profiles dev/prod
- ✅ Paginación

---

## 🚀 Ejecutar la Aplicación

```bash
cd projecte2
java -jar target/projecte2-0.0.1-SNAPSHOT.jar
```

La API estará disponible en: **http://localhost:8080**

---

## 📋 ENDPOINTS IMPLEMENTADOS

### 1️⃣ CRUD BÁSICO

#### GET todos los productos
```bash
curl -X GET http://localhost:8080/api/products
```

#### GET producto por ID
```bash
curl -X GET http://localhost:8080/api/products/1
```

#### POST crear producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor 27",
    "description": "Monitor IPS 1440p",
    "stock": 10,
    "price": 349.99,
    "rating": 4.6,
    "condition": "NUEVO"
  }'
```

#### PUT actualizar producto (todos los campos)
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor 27 actualizado",
    "stock": 5,
    "price": 299.99
  }'
```

#### PATCH actualizar solo stock
```bash
curl -X PATCH http://localhost:8080/api/products/1/stock?stock=15
```

#### PATCH actualizar solo precio
```bash
curl -X PATCH http://localhost:8080/api/products/1/price?price=279.99
```

#### DELETE borrado físico
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

#### DELETE borrado lógico
```bash
curl -X DELETE http://localhost:8080/api/products/1/logical
```

---

### 2️⃣ CARGA MASIVA CSV (Transaccional)

```bash
curl -X POST http://localhost:8080/api/products/upload \
  -F "file=@data.csv"
```

**Formato esperado del CSV:**
```
name,description,stock,price,rating,condition
Laptop,Laptop para desarrollo,5,1299.99,4.5,NUEVO
Mouse,Mouse inalámbrico,20,45.99,4.2,NUEVO
```

⚠️ **Si falla una línea, se revierte toda la transacción (no se guarda nada)**

---

### 3️⃣ BÚSQUEDA POR NOMBRE (Query Derivation)

```bash
# Busca productos cuyo nombre contenga "laptop"
curl -X GET "http://localhost:8080/api/products/search/nombre?prefix=laptop"
```

---

### 4️⃣ BÚSQUEDA POR CONDICIÓN (Query Derivation)

```bash
# Busca productos en condición NUEVO
curl -X GET "http://localhost:8080/api/products/search/condition?condition=NUEVO"
```

Opciones: `NUEVO`, `BON_ESTAT`, `ACCEPTABLE`, `MAL_ESTAT`

---

### 5️⃣ ORDENAR POR CAMPO (Query Derivation)

```bash
# Ordenar por precio ascendente
curl -X GET "http://localhost:8080/api/products/search/order?campo=precio&order=asc"

# Ordenar por precio descendente
curl -X GET "http://localhost:8080/api/products/search/order?campo=precio&order=desc"

# Ordenar por rating descendente
curl -X GET "http://localhost:8080/api/products/search/order?campo=rating&order=desc"
```

---

### 6️⃣ RANGO DE PRECIOS (JPQL)

```bash
# Productos entre 100€ y 500€
curl -X GET "http://localhost:8080/api/products/search/price-range?priceMin=100&priceMax=500"
```

---

### 7️⃣ TOP 5 MEJOR RELACIÓN CALIDAD-PRECIO (JPQL)

```bash
curl -X GET http://localhost:8080/api/products/search/best-quality-price
```

Ordena por rating descendente y precio ascendente.

---

### 8️⃣ RANGO DE RATING (JPQL)

```bash
# Productos con rating entre 4.0 y 5.0
curl -X GET "http://localhost:8080/api/products/search/rating-range?ratingMin=4.0&ratingMax=5.0"
```

---

### 9️⃣ TOP 10 PRODUCTOS NUEVOS MEJOR VALORADOS (JPQL)

```bash
curl -X GET http://localhost:8080/api/products/search/top-new-rated
```

Filtra productos con `condition=NUEVO` y ordena por rating.

---

### 🔟 PAGINACIÓN

```bash
# Primera página (5 productos por página)
curl -X GET "http://localhost:8080/api/products/paginated?page=0&size=5"

# Segunda página
curl -X GET "http://localhost:8080/api/products/paginated?page=1&size=5"

# 10 productos por página
curl -X GET "http://localhost:8080/api/products/paginated?page=0&size=10"
```

---

## 🏗️ Estructura del Proyecto

```
src/main/java/com/ra34/projecte2/
├── controller/
│   └── ProductController.java      (19 endpoints REST)
├── service/
│   └── ProductService.java         (Lógica de negocio)
├── repository/
│   └── ProductRepository.java      (Query Derivation + JPQL)
├── model/
│   ├── Product.java                (Entidad JPA)
│   └── Condition.java              (Enum)
├── dto/
│   ├── ProductDTO.java             (Response)
│   ├── ProductCreateDTO.java       (Request - crear)
│   ├── ProductUpdateDTO.java       (Request - actualizar)
│   └── ErrorDTO.java               (Error handling)
├── exception/
│   └── ResourceNotFoundException.java
├── config/
│   └── AppConfig.java              (Configuración JPA)
└── Projecte2Application.java       (Main)
```

---

## 📊 Atributos del Producto

| Campo | Tipo | Validación | Descripción |
|-------|------|-----------|-------------|
| `id` | Long | PK, Auto | Identificador único |
| `name` | String | NotNull, Max 20 | Nombre del producto |
| `description` | String | Max 100 | Descripción (opcional) |
| `stock` | Integer | NotNull, Min 0 | Stock disponible |
| `price` | BigDecimal | NotNull, Min 0 | Precio |
| `rating` | BigDecimal | Min 0, Max 5 | Valoración (opcional) |
| `condition` | Enum | NotNull | Estado: NUEVO, BON_ESTAT, ACCEPTABLE, MAL_ESTAT |
| `status` | Boolean | NotNull, Default true | Borrado lógico (true=activo) |
| `dataCreated` | LocalDateTime | Auto | Fecha de creación |
| `dataUpdated` | LocalDateTime | Auto | Fecha de actualización |

---

## 🎯 Funcionalidades Implementadas

✅ CRUD completo (crear, leer, actualizar, eliminar)  
✅ Borrado físico y lógico  
✅ Query Derivation (búsquedas simples)  
✅ JPQL (búsquedas complejas)  
✅ Paginación  
✅ Carga masiva CSV transaccional  
✅ DTOs para separación de capas  
✅ Validaciones con anotaciones  
✅ Manejo de excepciones con ErrorDTO  
✅ Auditoria (fechas creación/actualización)  
✅ Profiles dev/prod  
✅ Comentarios en código  
✅ Logging con SLF4J  

---

## 🧪 Pruebas Rápidas

### 1. Cargar CSV de ejemplo
```bash
curl -X POST http://localhost:8080/api/products/upload \
  -F "file=@src/main/resources/data.csv"
```

### 2. Obtener todos los productos
```bash
curl http://localhost:8080/api/products | jq
```

### 3. Buscar por nombre
```bash
curl "http://localhost:8080/api/products/search/nombre?prefix=Laptop" | jq
```

### 4. Top 5 mejor relación calidad-precio
```bash
curl http://localhost:8080/api/products/search/best-quality-price | jq
```

### 5. Paginación
```bash
curl "http://localhost:8080/api/products/paginated?page=0&size=5" | jq
```

---

## 📝 Notas

- **BD en memoria**: Por defecto usa H2 (create-drop)
- **Profile dev**: Logs detallados, BD temporal
- **Profile prod**: Logs mínimos, BD MySQL
- **Transacciones**: Activadas en carga CSV
- **CORS**: Deshabilitado (configurar si necesario)

---

## 👥 Integración Git

```bash
git clone https://github.com/[usuario]/AD_PROJECTE_2.git
git checkout -b feature/productos
git commit -m "feat: implementar CRUD productos"
git push origin feature/productos
```

---

## 📚 Dependencias Principales

- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- h2database
- lombok
- opencsv
- hibernate

---

**Versión:** 0.0.1-SNAPSHOT  
**Última actualización:** 29/04/2026
