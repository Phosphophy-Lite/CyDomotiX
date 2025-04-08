package com.example.cydomotix.Model.Users;

public enum AccessType {
    USER("Crewmate"),
    GESTION("Detective"),
    ADMIN("Engineer"),
    DEV("Impostor");

    private final String displayName;

    AccessType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
