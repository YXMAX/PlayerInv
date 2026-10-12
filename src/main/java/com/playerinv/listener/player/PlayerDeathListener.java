package com.playerinv.listener.player;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.gui.holder.impl.vault.StorageVaultHolder;
import com.playerinv.manager.PermissionManager;
import com.playerinv.util.VersionUtil;
import org.bukkit.GameRule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event){
        Boolean value = false;
        if(VersionUtil.isBelowVer1_13()){
            String v = event.getEntity().getWorld().getGameRuleValue("keepInventory");
            value = Boolean.parseBoolean(v);
        } else {
            value = event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        }
        if(ConfigConstants.FUNCTION_DEATH_STORE && (event.getEntity().getType() == EntityType.PLAYER)) {
            if (value && !ConfigConstants.DEATH_STORE_IGNORE) {
                return;
            } else {
                if(event.getEntity().getOpenInventory().getTopInventory().getHolder() instanceof StorageVaultHolder){
                    event.getEntity().closeInventory();
                }
                if (PermissionManager.hasPermission(event.getEntity(), PermissionEnums.DEATH_STORE)) {
                    List<ItemStack> item_list = event.getDrops();
                    PlayerInv.getInstance().getVaultManager().sendVaultByDeathStore(event.getEntity(), item_list);
                    event.getDrops().clear();
                } else {
                    return;
                }
            }
        }
    }
}
