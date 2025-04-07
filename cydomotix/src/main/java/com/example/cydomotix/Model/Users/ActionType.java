package com.example.cydomotix.Model.Users;

public enum ActionType {
    LOGIN("Connexion"),
    ADD_OBJECT("Ajout d'objet"),
    UPDATE_OBJECT("Mise à jour d'objet"),
    DELETE_OBJECT("Suppression d'objet"),
    ON_OBJECT("Allumer un objet"),
    OFF_OBJECT("Éteindre un objet"),
    UPDATE_USER("Mise à jour d'utilisateur"),
    DELETE_USER("Suppression d'utilisateur"),
    ADD_TYPE("Ajout de type d'objet"),
    DELETE_TYPE("Suppression de type d'objet");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
