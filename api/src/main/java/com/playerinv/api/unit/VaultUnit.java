package com.playerinv.api.unit;

import com.playerinv.api.VaultType;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

public interface VaultUnit {

    OfflinePlayer getOfflinePlayer();

    Inventory getInventory();

    void updateInventory();

    int getVaultNumber();

    VaultType getVaultType();

    String getCustomVaultName();

    void setCustomVaultName(String customName);
}
