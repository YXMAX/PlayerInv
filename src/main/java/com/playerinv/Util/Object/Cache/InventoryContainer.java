package com.playerinv.Util.Object.Cache;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryContainer {

    private Inventory inventory;

    private boolean hasError;

    private boolean isReady;

    public InventoryContainer() {
        this.inventory = null;
        this.hasError = false;
        this.isReady = true;
    }

    public InventoryContainer(boolean notReady) {
        this.inventory = null;
        this.hasError = false;
        this.isReady = !notReady;
    }

    public boolean isNotReady() {
        return !this.isReady;
    }

    public ItemStack[] getContents() {
        return inventory.getContents();
    }

    public void setContents(ItemStack[] contents) {
        if(inventory == null) {
            inventory = Bukkit.createInventory(null, contents.length, "CLONE");
        }
        inventory.setContents(contents);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setError() {
        this.hasError = true;
    }
}
