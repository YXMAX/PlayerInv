package com.playerinv.gui.icon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MenuIcon {

    int getRawSlot();

    ItemStack generateIcon(Player player);

    ItemStack generateIcon();

    void runActions(Player player,String clickType);

    boolean isCustomIcon();

    int getVaultType();

    int getVaultNum();

    String getVaultUUID();

    int getVaultPermission();

    boolean hasCustomKey();

    String getCustomKey();

    String getCustomKeyValue();
}
