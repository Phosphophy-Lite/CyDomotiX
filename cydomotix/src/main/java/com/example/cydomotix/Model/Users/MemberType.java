package com.example.cydomotix.Model.Users;

public enum MemberType {
    FATHER("Père"),
    MOTHER("Mère"),
    CHILD("Enfant");

    private final String displayName;

    MemberType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
