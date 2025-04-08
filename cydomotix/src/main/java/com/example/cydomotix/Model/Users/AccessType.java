package com.example.cydomotix.Model.Users;

public enum AccessType {
    USER("Utilisateur", 0),
    ADMIN("Administrateur", 30),
    DEV("Développeur", 45);

    private final String displayName;
    private final int cost;

    AccessType(String displayName, int cost) {
        this.displayName = displayName;
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCost(){
        return cost;
    }
}
