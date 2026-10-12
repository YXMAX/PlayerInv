package com.playerinv.api.impl;

import com.playerinv.PlayerInv;
import com.playerinv.api.VaultType;
import com.playerinv.api.unit.VaultUnit;
import com.playerinv.object.container.InventoryContainer;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

public class VaultUnitImpl implements VaultUnit {

    private final OfflinePlayer player;

    private final int num;

    private final InventoryContainer container;

    public VaultUnitImpl(OfflinePlayer player, int num, InventoryContainer container) {
        this.player = player;
        this.num = num;
        this.container = container;
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    @Override
    public Inventory getInventory() {
        return this.container.getInventory();
    }

    @Override
    public void updateInventory() {
        if (player.isOnline()) {
            this.container.syncAndUpload(player.getPlayer(), this.num);
            return;
        }
        JDBCUtil.updateVaultStringAsync(this.container.getType(), player.getUniqueId().toString(), this.container.getStrings(), this.num);
    }

    @Override
    public int getVaultNumber() {
        return this.num;
    }

    @Override
    public VaultType getVaultType() {
        return VaultType.fromInteger(this.container.getType());
    }

    @Override
    public String getCustomVaultName() {
        return PlayerInv.getInstance().getVaultAttributesManager().get(this.player).getAttribute(this.container.getType(), this.num);
    }

    @Override
    public void setCustomVaultName(String customName) {
        PlayerInv.getInstance().getVaultAttributesManager().get(this.player).addAttribute(this.container.getType(), this.num, customName);
    }
}
