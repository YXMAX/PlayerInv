package com.playerinv.api;

public enum VaultType {

    LARGE(1),

    MEDIUM(2);

    private final int code;

    VaultType(int code) {
        this.code = code;
    }

    public int getType() {
        return code;
    }

    public static VaultType fromInteger(int code) {
        for (VaultType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return LARGE;
    }

    public static int fromName(String name) {
        if (name == null) {
            return -1;
        }
        if (name.equalsIgnoreCase("large")) {
            return 1;
        }
        if (name.equalsIgnoreCase("medium")) {
            return 2;
        }
        return -1;
    }
}
