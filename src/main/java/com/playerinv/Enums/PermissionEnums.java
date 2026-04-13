package com.playerinv.Enums;

public enum PermissionEnums {

    COMMAND_ADMIN("playerinv.admin"),

    COMMAND_CHECK("playerinv.check"),

    COMMAND_RELOAD("playerinv.reload"),

    INV_ALL("playerinv.inv.*"),

    VOUCHER_GIVE("playerinv.give"),

    GUI_OPEN("playerinv.gui.open"),

    INV_OPEN("playerinv.invopen"),

    LARGE_ALL("playerinv.large.inv.*"),

    MEDIUM_ALL("playerinv.medium.inv.*"),

    VAULT_GIVE("playerinv.vault.give"),

    VAULT_APPEND("playerinv.vault.append"),

    DEATH_STORE("playerinv.death.store"),

    PICKUP_TOGGLE("playerinv.pickup.toggle"),

    COMMAND_MAIN_OPEN("playerinv.command.main.open"),

    COMMAND_VAULT_OPEN("playerinv.command.vault.open"),

    AUTO_PICKUP("playerinv.auto.pickup"),

    ALTER_NAME("playerinv.alter.name"),

    EVENT_PICKUP_IGNORE("playerinv.vault.event.pickup.ignore"),

    EVENT_PLACE_IGNORE("playerinv.vault.event.place.ignore");

    private String desc;

    private PermissionEnums(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
