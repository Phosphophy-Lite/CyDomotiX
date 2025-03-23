package com.example.cydomotix.Model.Objects;

public enum ValueType {
    STRING("Chaîne de caractères"), // display names
    INTEGER("Entier"),
    DOUBLE("Nombre à virgule"),
    TEMPERATURE("Température (°C)"),
    HOURS("Heures (h)"),
    MINUTES("Minutes (min)"),
    SECONDS("Secondes (s)"),
    PERCENTAGE("Pourcentage (%)");

    private final String displayName;

    ValueType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
