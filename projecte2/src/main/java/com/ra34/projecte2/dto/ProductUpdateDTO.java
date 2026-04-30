package com.ra34.projecte2.dto;

import com.ra34.projecte2.model.Condition;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para actualizar productos.
 * Todos los campos son opcionales (excepto el ID que viene en la URL).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

    @Size(max = 20, message = "El nombre no puede exceder 20 caracteres")
    private String name;

    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    private String description;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "La valoración no puede ser menor a 0")
    @DecimalMax(value = "5.0", message = "La valoración no puede ser mayor a 5")
    private BigDecimal rating;

    private Condition condition;
}
