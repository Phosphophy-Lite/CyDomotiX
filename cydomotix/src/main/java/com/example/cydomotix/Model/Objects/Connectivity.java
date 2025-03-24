package com.example.cydomotix.Model.Objects;

public enum Connectivity {
    WIFI_WEAK("Wi-Fi, signal faible"),
    WIFI_STRONG("Wi-Fi, signal fort"),
    BLUETOOTH("Bluetooth"),
    ZIGBEE("Zigbee"),
    Z_WAVE("Z-Wave"),
    ETHERNET("Ethernet"),
    CELLULAR("Réseau mobile (4G/5G)"),
    NFC("NFC (communication de proximité)"),
    INFRARED("Infrarouge"),
    NONE("Aucune connectivité");

    private final String displayName;

    Connectivity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}