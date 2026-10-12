package com.playerinv.listener.player;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.manager.PermissionManager;
import com.playerinv.gui.holder.impl.vault.StorageVaultHolder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static com.playerinv.manager.PermissionManager.hasPermission;
import static com.playerinv.util.LocaleUtil.sendMessages;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onUseVoucher(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(!PlayerInv.getInstance().getVoucherManager().isVoucherEnabled()){
            return;
        }
        if(p.getEquipment().getItemInMainHand() == null){
            return;
        }
        if(!p.getEquipment().getItemInMainHand().hasItemMeta()){
            return;
        }
        if(!p.getEquipment().getItemInMainHand().getItemMeta().hasDisplayName()){
            return;
        }
        ItemStack item = p.getEquipment().getItemInMainHand();
        if(PlayerInv.getInstance().getVoucherManager().isSameMaterial(item)){
            PlayerInv.getInstance().getVoucherManager().runVoucher(p,item);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerInv.getInstance().getPlayerCacheManager().loadPlayerCache(player);
        PlayerInv.getInstance().getPickupManager().putToggle(player);
        PlayerInv.getInstance().getVaultAttributesManager().loadAttributes(player).thenRun(() -> {
            PlayerInv.getInstance().getCacheInventoryManager().loadCacheInventory(player);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerInv.getInstance().getCacheInventoryManager().removeCacheInventory(player);
        PlayerInv.getInstance().getVaultAttributesManager().removeAttributes(player);
        PlayerInv.getInstance().getPickupManager().removeToggle(player);
        PlayerInv.getInstance().getPlayerCacheManager().removePlayerCache(player);
        PlayerInv.getInstance().getChatBarManager().removeTypeTemp(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if(PlayerInv.getInstance().getChatBarManager().runChatBarOperation(event.getPlayer(),event.getMessage())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupEvent(EntityPickupItemEvent event) {
        if(!ConfigConstants.FUNCTION_AUTO_PICKUP){
            return;
        }
        if(event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        if(!PermissionManager.hasPermission(player, PermissionEnums.AUTO_PICKUP)){
            return;
        }
        if(!PlayerInv.getInstance().getPickupManager().getToggle(player.getUniqueId().toString())){
            return;
        }
        if(player.getOpenInventory().getTopInventory().getHolder() instanceof StorageVaultHolder){
            return;
        }
        ItemStack item = event.getItem().getItemStack();
        if(PlayerInv.getInstance().getPickupManager().runPickup(player,item)){
            event.setCancelled(true);
            event.getItem().remove();
        }
    }
}
