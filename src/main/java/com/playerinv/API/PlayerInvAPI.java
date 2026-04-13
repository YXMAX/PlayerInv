package com.playerinv.API;

import com.playerinv.Util.Object.Cache.InventoryContainer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedHashMap;

import static com.playerinv.PlayerInv.*;

public class PlayerInvAPI {

    public Inventory getVault(OfflinePlayer player, VaultType vaultType, int vault_num) {
        if(player.isOnline()) {
            return jdbcUtil.getVault(vaultType.getType(),player.getPlayer(), vault_num);
        } else {
            return jdbcUtil.getVault(vaultType.getType(),player, vault_num);
        }
    }

    public void saveVault(OfflinePlayer player, VaultType vaultType, int vault_num, Inventory inventory) {
        operationManager.updateVaultToDatabase(vaultType.getType(),player.getUniqueId().toString(),inventory,vault_num);
    }

    public LinkedHashMap<Integer, InventoryContainer> getVaults(Player player, VaultType vaultType){
        switch(vaultType) {
            case MEDIUM:
                return jdbcUtil.getFullMediumInventory(player);
            case LARGE:
            default:
                return jdbcUtil.getFullLargeInventory(player);

        }
    }

    public String getVaultName(Player player, VaultType vaultType, int vault_num) {
        return vaultAttributesManager.replaceVaultName(player,vaultType.getType(),vault_num);
    }


}
