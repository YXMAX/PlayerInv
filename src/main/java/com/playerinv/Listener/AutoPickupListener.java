package com.playerinv.Listener;

import com.playerinv.Util.InvHolder.VaultHolder_Large;
import com.playerinv.Util.InvHolder.VaultHolder_Medium;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LoadUtil.*;

public class AutoPickupListener implements Listener {

    @EventHandler
    public void onPlayerPickupEvent(EntityPickupItemEvent event) {
        if(!autoPickupBool){
            return;
        }
        if(event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        if(!player.hasPermission("playerinv.auto.pickup")){
            return;
        }
        if(!pickupManager.getToggle(player.getUniqueId().toString())){
            return;
        }
        if(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large ||
                player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Medium){
            return;
        }
        ItemStack item = event.getItem().getItemStack();
        if(pickupManager.runPickup(player,item)){
            event.setCancelled(true);
            event.getItem().remove();
        }
    }
}
