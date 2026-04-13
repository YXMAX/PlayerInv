package com.playerinv.Util.Object.Operation;

import org.bukkit.entity.Player;

public class TypeAttribute {

    private String menu_name;

    private int vault_type;

    private int vault_num;

    private Player player;

    public TypeAttribute(Player player, int vault_type, int vault_num, String menu_name) {
        this.player = player;
        this.vault_type = vault_type;
        this.vault_num = vault_num;
        this.menu_name = menu_name;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public int getVault_type() {
        return vault_type;
    }

    public int getVault_num() {
        return vault_num;
    }
}
