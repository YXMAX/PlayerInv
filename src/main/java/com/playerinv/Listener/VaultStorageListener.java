package com.playerinv.Listener;

import com.playerinv.API.Event.VaultCloseEvent;
import com.playerinv.API.VaultType;
import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Util.InvHolder.*;
import com.playerinv.Util.JDBCUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.LocaleUtil.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.NodeUtil.*;


public class VaultStorageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVaultStorageClick(InventoryClickEvent event){
        if (event.getInventory().getHolder() instanceof VaultHolder_Large){
            Player player = (Player) event.getWhoClicked();
            if(!JDBCUtil.isConnected()){
                sendMessages(player,LocaleEnums.Connection_invalid_refuse);
                event.setCancelled(true);
                return;
            }
            ((VaultHolder_Large) event.getInventory().getHolder()).update(player);
            return;
        }
        if (event.getInventory().getHolder() instanceof VaultHolder_Medium){
            Player player = (Player) event.getWhoClicked();
            if(!JDBCUtil.isConnected()){
                sendMessages(player,LocaleEnums.Connection_invalid_refuse);
                event.setCancelled(true);
                return;
            }
            ((VaultHolder_Medium) event.getInventory().getHolder()).update(player);
            return;
        }
    }

    @EventHandler
    public void onVaultStorageClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getHolder() instanceof VaultHolder_Large){
            VaultHolder_Large holder = (VaultHolder_Large) event.getInventory().getHolder();
            holder.update(player);
            VaultCloseEvent closeEvent = new VaultCloseEvent(player,event.getInventory(), VaultType.LARGE, holder.getNum());
            Bukkit.getPluginManager().callEvent(closeEvent);

            soundManager.playSoundEffect(player,soundManager.vaultClose);
            holder.returnMenu(player);
            return;
        }
        if (event.getInventory().getHolder() instanceof VaultHolder_Medium){
            VaultHolder_Medium holder = (VaultHolder_Medium) event.getInventory().getHolder();
            holder.update(player);
            VaultCloseEvent closeEvent = new VaultCloseEvent(player,event.getInventory(), VaultType.MEDIUM, holder.getNum());
            Bukkit.getPluginManager().callEvent(closeEvent);

            soundManager.playSoundEffect(player,soundManager.vaultClose);
            holder.returnMenu(player);
            return;
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onVaultClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof VaultHolder_Large){
            VaultHolder_Large holder = (VaultHolder_Large) event.getInventory().getHolder();
            Player player = (Player) event.getWhoClicked();
            switch(event.getAction().name()){
                case "HOTBAR_SWAP":
                case "HOTBAR_MOVE_AND_READD":
                    event.setCancelled(true);
                    break;

                case "PLACE_ALL":
                case "PLACE_ONE":
                case "PLACE_SOME":
                case "NOTHING":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.place.ignore")){
                            return;
                        }
                        if(!vaultPlaceEvent){
                            event.setCancelled(true);
                            break;
                        }
                        if(holder.isOnlyPickup()){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "PICKUP_ALL":
                case "PICKUP_ONE":
                case "PICKUP_SOME":
                case "PICKUP_HALF":
                case "DROP_ALL_CURSOR":
                case "DROP_ALL_SLOT":
                case "DROP_ONE_CURSOR":
                case "DROP_ONE_SLOT":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                            return;
                        }
                        if(!vaultPickupEvent){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "MOVE_TO_OTHER_INVENTORY":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                            return;
                        }
                        if(!vaultPickupEvent){
                            event.setCancelled(true);
                            break;
                        }
                    } else if(event.getRawSlot() >= event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.place.ignore")){
                            return;
                        }
                        if(!vaultPlaceEvent){
                            event.setCancelled(true);
                            break;
                        }
                        if(holder.isOnlyPickup()){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "SWAP_WITH_CURSOR":
                case "COLLECT_TO_CURSOR":
                case "CLONE_STACK":
                    if(player.hasPermission("playerinv.vault.event.place.ignore")){
                        return;
                    }
                    if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                        return;
                    }
                    if(!vaultPlaceEvent || !vaultPickupEvent){
                        event.setCancelled(true);
                        break;
                    }
                    if(holder.isOnlyPickup()){
                        event.setCancelled(true);
                        break;
                    }
            }
            return;
        }
        if(event.getInventory().getHolder() instanceof VaultHolder_Medium){
            VaultHolder_Medium holder = (VaultHolder_Medium) event.getInventory().getHolder();
            Player player = (Player) event.getWhoClicked();
            switch(event.getAction().name()){
                case "HOTBAR_SWAP":
                case "HOTBAR_MOVE_AND_READD":
                    event.setCancelled(true);
                    break;

                case "PLACE_ALL":
                case "PLACE_ONE":
                case "PLACE_SOME":
                case "NOTHING":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.place.ignore")){
                            return;
                        }
                        if(!vaultPlaceEvent){
                            event.setCancelled(true);
                            break;
                        }
                        if(holder.isOnlyPickup()){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "PICKUP_ALL":
                case "PICKUP_ONE":
                case "PICKUP_SOME":
                case "PICKUP_HALF":
                case "DROP_ALL_CURSOR":
                case "DROP_ALL_SLOT":
                case "DROP_ONE_CURSOR":
                case "DROP_ONE_SLOT":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                            return;
                        }
                        if(!vaultPickupEvent){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "MOVE_TO_OTHER_INVENTORY":
                    if(event.getRawSlot() < event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                            return;
                        }
                        if(!vaultPickupEvent){
                            event.setCancelled(true);
                            break;
                        }
                    } else if(event.getRawSlot() >= event.getInventory().getSize()){
                        if(player.hasPermission("playerinv.vault.event.place.ignore")){
                            return;
                        }
                        if(!vaultPlaceEvent){
                            event.setCancelled(true);
                            break;
                        }
                        if(holder.isOnlyPickup()){
                            event.setCancelled(true);
                            break;
                        }
                    }
                    return;

                case "SWAP_WITH_CURSOR":
                case "COLLECT_TO_CURSOR":
                case "CLONE_STACK":
                    if(player.hasPermission("playerinv.vault.event.place.ignore")){
                        return;
                    }
                    if(player.hasPermission("playerinv.vault.event.pickup.ignore")){
                        return;
                    }
                    if(!vaultPlaceEvent || !vaultPickupEvent){
                        event.setCancelled(true);
                        break;
                    }
                    if(holder.isOnlyPickup()){
                        event.setCancelled(true);
                        break;
                    }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVaultDrag(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof VaultMenuHolder){
            if(event.getRawSlots().stream().anyMatch(num -> num < event.getInventory().getSize())){
                event.setCancelled(true);
                return;
            }
        }
        if(event.getInventory().getHolder() instanceof VaultHolder_Large) {
            if(event.getRawSlots().stream().min(Integer::compareTo).get() <= 53) {
                VaultHolder_Large holder = (VaultHolder_Large) event.getInventory().getHolder();
                Player player = (Player) event.getWhoClicked();
                if(player.hasPermission("playerinv.vault.event.place.ignore")){
                    holder.update(player);
                    return;
                }
                if(!vaultPlaceEvent){
                    event.setCancelled(true);
                    return;
                }
                if(holder.isOnlyPickup()){
                    event.setCancelled(true);
                    return;
                }
                holder.update(player);
                return;
            }
        }
        if(event.getInventory().getHolder() instanceof VaultHolder_Medium) {
            if(event.getRawSlots().stream().min(Integer::compareTo).get() <= 26) {
                VaultHolder_Medium holder = (VaultHolder_Medium) event.getInventory().getHolder();
                Player player = (Player) event.getWhoClicked();
                if(player.hasPermission("playerinv.vault.event.place.ignore")){
                    holder.update(player);
                    return;
                }
                if(!vaultPlaceEvent){
                    event.setCancelled(true);
                    return;
                }
                if(holder.isOnlyPickup()){
                    event.setCancelled(true);
                    return;
                }
                holder.update(player);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlackListBlockClickEvent(InventoryClickEvent event){
        if((event.getInventory().getHolder() instanceof VaultHolder_Large) || (event.getInventory().getHolder() instanceof VaultHolder_Medium)) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if(item == null){
                return;
            }
            if(event.getRawSlot() >= event.getInventory().getSize()) {
                if(materialBlackList.contains(item.getType().toString())){
                    event.setCancelled(true);
                    sendMessages(player,LocaleEnums.Vault_blacklist_items);
                    return;
                }
                if (loreBlackList.isEmpty()) {
                    return;
                }
                if (!item.hasItemMeta()) {
                    return;
                }
                if (!item.getItemMeta().hasLore()) {
                    return;
                }
                if(player.hasPermission("playerinv.blacklist.lore.ignore")){
                    return;
                }
                List<String> lore = item.getItemMeta().getLore();
                for (String each_lore : loreBlackList) {
                    if (isEmpty(each_lore)) {
                        continue;
                    }
                    if (lore.stream().anyMatch(str -> str.contains(each_lore))) {
                        event.setCancelled(true);
                        sendMessages(player,LocaleEnums.Vault_blacklist_items);
                        return;
                    }
                }
            }
        }
    }
}
