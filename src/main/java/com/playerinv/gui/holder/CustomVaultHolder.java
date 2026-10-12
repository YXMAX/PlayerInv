package com.playerinv.gui.holder;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface CustomVaultHolder {

    void runClickEvent(InventoryClickEvent event);

    void runDragEvent(InventoryDragEvent event);

    void runCloseEvent(InventoryCloseEvent event);
}
