package com.playerinv.api.event;

import com.playerinv.api.VaultType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class VaultOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    private final Player player;

    private final Inventory inventory;

    private final VaultType vaultType;

    private final int vaultNum;

    public VaultOpenEvent(Player player, Inventory inventory, VaultType vaultType, int vaultNum) {
        this.player = player;
        this.inventory = inventory;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
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

    public Player getPlayer() {
        return player;
    }

    public VaultType getVaultType() {
        return vaultType;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getVaultNum() {
        return vaultNum;
    }
}
