package com.example.cydomotix.Model.Objects;

public enum Mode {
    AUTOMATIC("Automatique"),
    SCHEDULED("Planifié"),
    ECO("Économie d’énergie"),
    PERFORMANCE("Haute performance"),
    NIGHT("Mode nuit"),
    AWAY("Absence"),
    MANUAL("Manuel");

    private final String displayName;

    Mode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
