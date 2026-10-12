package com.playerinv.api.event;

import com.playerinv.api.VaultType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class VaultCloseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final Inventory inventory;

    private final VaultType vaultType;

    private final int vaultNum;

    public VaultCloseEvent(Player player, Inventory inventory, VaultType vaultType, int vaultNum) {
        this.player = player;
        this.inventory = inventory;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public VaultType getVaultType() {
        return vaultType;
    }

    public int getVaultNum() {
        return vaultNum;
    }
}
