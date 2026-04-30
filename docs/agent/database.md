# 📊 Diseño de Base de Datos - RA3 Projecte 2

## Esquema: Entidad Product

### Tabla: products

```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    stock INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    rating DECIMAL(3,2),
    condition VARCHAR(50) NOT NULL,  -- ENUM: nou, bon_estat, acceptable, mal_estat
    status BOOLEAN DEFAULT true,      -- true: activo, false: borrado lógico
    data_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## Entidad Product (Java/JPA)

```java
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 20, nullable = false)
    private String name;
    
    @Column(length = 100)
    private String description;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal rating;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCondition condition;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean status = true;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_created", nullable = false, updatable = false)
    private Date dataCreated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_updated")
    private Date dataUpdated;
    
    @PrePersist
    protected void onCreate() {
        dataCreated = new Date();
        dataUpdated = new Date();
        status = true;
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataUpdated = new Date();
    }
}
```

---

## Enum: ProductCondition

```java
public enum ProductCondition {
    NOU,              // nuevo
    BON_ESTAT,        // buen estado
    ACCEPTABLE,       // aceptable
    MAL_ESTAT         // mal estado
}
```

---

## Notas de Diseño

- **Status = borrado lógico:** Si `status = false`, el producto se considera "borrado" pero sigue en BD
- **Auditoría:** `dataCreated` (inmutable) y `dataUpdated` (auto-actualización) con `@PrePersist` y `@PreUpdate`
- **Rating nullable:** Un producto nuevo puede no tener rating
- **Precio en DECIMAL:** No usar `double` para dinero, usar `BigDecimal`
- **Condition como ENUM:** Valores controlados, no strings libres

---

## Índices Recomendados (Futuros)

```sql
-- Para búsquedas frecuentes
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_condition ON products(condition);
CREATE INDEX idx_product_status ON products(status);
CREATE INDEX idx_product_price ON products(price);
CREATE INDEX idx_product_rating ON products(rating);
```

---
