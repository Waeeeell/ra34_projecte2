package com.ra34.projecte2.dto;

import com.ra34.projecte2.model.Condition;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para crear nuevos productos.
 * Contiene validaciones necesarias para la creación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede exceder 20 caracteres")
    private String name;

    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    private String description;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "La valoración no puede ser menor a 0")
    @DecimalMax(value = "5.0", message = "La valoración no puede ser mayor a 5")
    private BigDecimal rating;

    @NotNull(message = "La condición no puede ser nula")
    private Condition condition = Condition.NUEVO;
}
