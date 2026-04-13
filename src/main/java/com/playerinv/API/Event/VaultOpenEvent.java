package com.playerinv.API.Event;

import com.playerinv.API.VaultType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class VaultOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    private Player player;

    private Inventory inventory;

    private VaultType vaultType;

    private int vaultNum;

    public VaultOpenEvent(Player player, Inventory inventory,VaultType vaultType, int vaultNum) {
        this.player = player;
        this.inventory = inventory;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
    }

    public VaultType getVaultType() {
        return vaultType;
    }

    public int getVaultNumber() {
        return vaultNum;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
