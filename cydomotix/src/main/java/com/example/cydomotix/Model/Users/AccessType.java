package com.example.cydomotix.Model.Users;

public enum AccessType {
    USER("Coéquipier", 0),
    GESTION("Détective", 20),
    ADMIN("Ingénieur", 30),
    DEV("Imposteur", 45);

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
