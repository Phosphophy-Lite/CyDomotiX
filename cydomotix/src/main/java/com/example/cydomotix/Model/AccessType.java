package com.example.cydomotix.Model;

public enum AccessType {
    USER("Utilisateur"),
    ADMIN("Administrateur"),
    DEV("Développeur");

    private final String displayName;

    AccessType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
