package com.playerinv.gui.holder;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CustomMenuHolder {

    void runClickEvent(InventoryClickEvent event);

    void runCloseEvent(InventoryCloseEvent event);
}
