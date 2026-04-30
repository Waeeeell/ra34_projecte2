package com.ra34.projecte2.dto;

import lombok.*;

/**
 * DTO para gestionar errores no controlados.
 * Se usa para devolver información de error en respuestas HTTP.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private int status;
    private String message;
    private String error;
}
