# 🔧 Arquitectura de Código - RA3 Projecte 2

## Estructura de Carpetas

```
src/main/java/com/ra34/projecte2/
├── config/
│   └── AppConfig.java (si se necesita)
├── controller/
│   └── ProductController.java
├── service/
│   └── ProductService.java
├── repository/
│   └── ProductRepository.java
├── model/
│   ├── Product.java
│   └── ProductCondition.java (ENUM)
├── dto/
│   ├── ProductRequestDTO.java
│   ├── ProductResponseDTO.java
│   └── ErrorDTO.java
├── exception/
│   ├── ProductNotFoundException.java
│   └── GlobalExceptionHandler.java
└── Projecte2Application.java

src/main/resources/
├── application.properties
├── application-dev.properties
└── application-prod.properties
```

---

## 1️⃣ CRUD Endpoints

### GET /api/products (Listar todos)
- **Método:** ProductService.getAllProducts()
- **Query:** productRepository.findAll()
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/{id} (Obtener por ID)
- **Método:** ProductService.getProductById(Long id)
- **Query:** productRepository.findById(id)
- **Status:** 200 OK o 404 Not Found
- **Response:** `ProductResponseDTO`

### POST /api/products (Crear)
- **Método:** ProductService.createProduct(ProductRequestDTO dto)
- **Validaciones:** name (not null, 20 chars), stock (not null), price (not null)
- **Status:** 201 Created
- **Response:** `ProductResponseDTO`

### PUT /api/products/{id} (Actualizar completo)
- **Método:** ProductService.updateProduct(Long id, ProductRequestDTO dto)
- **Validaciones:** todos los campos
- **Status:** 200 OK o 404 Not Found
- **Response:** `ProductResponseDTO`

### PATCH /api/products/{id}/stock (Actualizar stock)
- **Método:** ProductService.updateStock(Long id, Integer newStock)
- **Status:** 200 OK o 404 Not Found
- **Response:** `ProductResponseDTO`

### PATCH /api/products/{id}/price (Actualizar precio)
- **Método:** ProductService.updatePrice(Long id, BigDecimal newPrice)
- **Status:** 200 OK o 404 Not Found
- **Response:** `ProductResponseDTO`

### DELETE /api/products/{id} (Borrado físico)
- **Método:** ProductService.deleteProduct(Long id)
- **Status:** 204 No Content
- **Nota:** Elimina físicamente del BD

### PATCH /api/products/{id}/logical-delete (Borrado lógico)
- **Método:** ProductService.logicalDelete(Long id)
- **Status:** 200 OK
- **Response:** `ProductResponseDTO` (con status = false)

---

## 2️⃣ CSV Upload

### POST /api/products/upload (Cargar CSV)
- **Parámetro:** `file` (multipart)
- **Método:** ProductService.uploadProductsCSV(MultipartFile file)
- **Anotación:** `@Transactional` (rollback si falla)
- **Validaciones:** 
  - Formato CSV correcto
  - Campos obligatorios presentes
  - Cada línea se valida
- **Status:** 200 OK (con resumen) o 400 Bad Request
- **Response:** 
  ```json
  {
    "successCount": 20,
    "errorCount": 0,
    "message": "Cargados 20 productos correctamente"
  }
  ```

---

## 3️⃣ Query Methods (Query Derivation)

### GET /api/products/search/name (Buscar por nombre)
- **Parámetro:** `prefix` (string)
- **Método:** ProductService.searchByName(String prefix)
- **Query:** `findByNameContainsAndStatusTrue(String name)`
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/search/condition (Filtrar por condición)
- **Parámetro:** `condition` (enum: nou, bon_estat, acceptable, mal_estat)
- **Método:** ProductService.filterByCondition(ProductCondition condition)
- **Query:** `findByConditionAndStatusTrue(ProductCondition condition)`
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/search/order (Ordenar por precio)
- **Parámetros:** `field` (price/rating), `order` (asc/desc)
- **Método:** ProductService.orderByField(String field, String order)
- **Queries:** 
  - `findAllByStatusTrueOrderByPriceAsc()`
  - `findAllByStatusTrueOrderByPriceDesc()`
  - `findAllByStatusTrueOrderByRatingAsc()`
  - `findAllByStatusTrueOrderByRatingDesc()`
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

---

## 4️⃣ JPQL Queries

### GET /api/products/jpql/price-range (Rango de precio)
- **Parámetros:** `priceMin`, `priceMax`, `field` (price/rating), `order` (asc/desc), `limit`
- **Método:** ProductService.filterByPriceRange(BigDecimal min, BigDecimal max, ...)
- **JPQL Query:**
  ```java
  @Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2 AND p.status = true " +
         "ORDER BY p.price DESC LIMIT ?3")
  ```
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/jpql/top-quality-price (Top 5 mejor relación calidad-precio)
- **Método:** ProductService.getTopQualityPrice()
- **JPQL Query:**
  ```java
  @Query("SELECT p FROM Product p WHERE p.status = true " +
         "ORDER BY (p.rating / p.price) DESC LIMIT 5")
  ```
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/jpql/rating-range (Filtrar por rating)
- **Parámetros:** `ratingMin`, `ratingMax`, `field`, `order`, `limit`
- **Método:** ProductService.filterByRatingRange(BigDecimal min, BigDecimal max, ...)
- **JPQL Query:**
  ```java
  @Query("SELECT p FROM Product p WHERE p.rating BETWEEN ?1 AND ?2 AND p.status = true " +
         "ORDER BY p.rating DESC LIMIT ?3")
  ```
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

### GET /api/products/jpql/top-new-rated (Top 10 productos nuevos mejor valorados)
- **Método:** ProductService.getTopNewRated()
- **JPQL Query:**
  ```java
  @Query("SELECT p FROM Product p WHERE p.condition = 'NOU' AND p.status = true " +
         "ORDER BY p.rating DESC LIMIT 10")
  ```
- **Status:** 200 OK
- **Response:** `List<ProductResponseDTO>`

---

## 5️⃣ Paginación

### GET /api/products/paginated (Productos paginados)
- **Parámetros:** `page` (0-indexed), `size` (por defecto 5)
- **Método:** ProductService.getProductsPaginated(Pageable pageable)
- **Query:** `productRepository.findByStatusTruePageable(pageable)`
- **Status:** 200 OK
- **Response:** 
  ```json
  {
    "content": [{ ProductResponseDTO }, ...],
    "totalElements": 100,
    "totalPages": 20,
    "currentPage": 0,
    "pageSize": 5
  }
  ```

---

## DTOs

### ProductRequestDTO
```java
public class ProductRequestDTO {
    private String name;                    // @NotNull, @Size(max=20)
    private String description;             // @Size(max=100)
    private Integer stock;                  // @NotNull, @Min(0)
    private BigDecimal price;               // @NotNull, @Positive
    private BigDecimal rating;              // @Min(0), @Max(5)
    private ProductCondition condition;     // @NotNull
}
```

### ProductResponseDTO
```java
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal price;
    private BigDecimal rating;
    private ProductCondition condition;
    private Boolean status;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;
}
```

### ErrorDTO
```java
public class ErrorDTO {
    private int status;                 // HTTP status code
    private String message;             // Descripción del error
    private LocalDateTime timestamp;    // Cuándo ocurrió
    private String path;                // Endpoint donde pasó
}
```

---

## Manejo de Excepciones

### GlobalExceptionHandler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(...) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidation(...) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneric(...) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
```

---

## Anotaciones Clave

- `@RestController` - Endpoints
- `@Service` - Lógica de negocio
- `@Repository` - Acceso a datos (extends JpaRepository)
- `@Transactional` - Para CSV upload (rollback si falla)
- `@Valid` - Validar DTOs de entrada
- `@RequestParam` - Parámetros en URL
- `@PathVariable` - Parámetros en ruta
- `@RequestBody` - JSON en cuerpo
- `@ResponseStatus` - HTTP status codes
- `@PrePersist` / `@PreUpdate` - Hooks de auditoría

---
