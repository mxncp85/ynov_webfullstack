package com.ynov.adventures.domain;

public enum Classe {
    GUERRIER("Guerrier"),
    MAGE("Mage"),
    RODEUR("Rodeur"),
    VOLEUR("Voleur"),
    CLERC("Clerc"),
    BARDE("Barde"),
    PALADIN("Paladin"),
    DRUIDE("Druide"),
    BARBARE("Barbare"),
    SORCIER("Sorcier");

    private final String displayName;

    Classe(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
