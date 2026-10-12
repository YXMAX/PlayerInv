package com.playerinv.listener.player;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.manager.PermissionManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderChestListener implements Listener {

    @EventHandler
    public void onPlayerOpenEnderChest(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!ConfigConstants.FUNCTION_ENDERCHEST){
            return;
        }
        if(event.getClickedBlock() == null){
            return;
        }
        if(event.getClickedBlock().getType() == null){
            return;
        }
        if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST)
                && PermissionManager.hasPermission(player, PermissionEnums.ENDERCHEST_OPEN)){
            if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                return;
            }
            event.setCancelled(true);
            PlayerInv.getInstance().getMenuManager().openVaultMenu(player,null);
        }
    }
}
