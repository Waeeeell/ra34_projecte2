package com.ra34.projecte2.model;

/**
 * Enum que representa la condición o estado de un producto.
 */
public enum Condition {
    NUEVO("Nuevo"),
    BON_ESTAT("Buen estado"),
    ACCEPTABLE("Aceptable"),
    MAL_ESTAT("Mal estado");

    private final String displayName;

    Condition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
