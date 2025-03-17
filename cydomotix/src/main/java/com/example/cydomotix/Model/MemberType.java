package com.example.cydomotix.Model;

public enum MemberType {
    MOTHER("Mère"),
    FATHER("Père"),
    CHILD("Enfant");

    private final String displayName;

    MemberType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
