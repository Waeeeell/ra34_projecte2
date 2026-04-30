package com.ra34.projecte2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Product - Modelo de datos para productos de la tienda online.
 * 
 * Includes:
 * - Atributos básicos (nombre, descripción, stock, precio, valoración)
 * - Condición del producto (enum)
 * - Borrado lógico (status)
 * - Auditoría (fechas de creación y actualización)
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede exceder 20 caracteres")
    @Column(nullable = false)
    private String name;

    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    @Column(length = 100)
    private String description;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "La valoración no puede ser menor a 0")
    @DecimalMax(value = "5.0", message = "La valoración no puede ser mayor a 5")
    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition = Condition.NUEVO;

    // Borrado lógico - true = activo, false = eliminado
    @NotNull
    @Column(nullable = false)
    private Boolean status = true;

    // Auditoría - Fecha de creación
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCreated;

    // Auditoría - Fecha de actualización
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime dataUpdated;

}
