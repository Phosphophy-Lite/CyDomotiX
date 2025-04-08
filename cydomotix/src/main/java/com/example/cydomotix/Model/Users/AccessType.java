package com.example.cydomotix.Model.Users;

public enum AccessType {
    USER("Crewmate", 0),
    GESTION("Detective", 20),
    ADMIN("Engineer", 30),
    DEV("Impostor", 45);

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
