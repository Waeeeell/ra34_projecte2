package com.ra34.projecte2.dto;

import com.ra34.projecte2.model.Condition;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de consulta de productos.
 * Contiene todos los datos del producto sin información sensible.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal price;
    private BigDecimal rating;
    private Condition condition;
    private Boolean status;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;
}
