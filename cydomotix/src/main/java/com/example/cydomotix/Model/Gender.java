package com.example.cydomotix.Model;

public enum Gender {
    MALE("Homme"), // display names
    FEMALE("Femme"),
    OTHER("Autre");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
