package com.playerinv.Listener;


import com.playerinv.Util.InvHolder.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class CheckOperationListener implements Listener {

    @EventHandler
    public void onCheckMenuClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof CheckMenuHolder) {
            int clicked_slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            switch(event.getAction().name()) {
                case "HOTBAR_SWAP":
                case "HOTBAR_MOVE_AND_READD":
                case "MOVE_TO_OTHER_INVENTORY":
                    event.setCancelled(true);
                    return;
            }
            if(clicked_slot >= event.getInventory().getSize()){
                return;
            }
            event.setCancelled(true);
            CheckMenuHolder holder = (CheckMenuHolder) event.getInventory().getHolder();
            holder.runOperation(player,clicked_slot);
        }
    }

    @EventHandler
    public void onCheckVaultClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof CheckVaultHolder) {
            if(event.getClick().name().equals("SHIFT_RIGHT") && (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)){
                Player player = (Player) event.getWhoClicked();
                CheckVaultHolder holder = (CheckVaultHolder) event.getInventory().getHolder();
                holder.switchSave(player);
            }
        }
    }

    @EventHandler
    public void onCheckMenuDrag(InventoryDragEvent event){
        if (event.getInventory().getHolder() instanceof CheckMenuHolder){
            if(event.getRawSlots().stream().anyMatch(num -> num < event.getInventory().getSize())){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onCheckVaultClose(InventoryCloseEvent event){
        if (event.getInventory().getHolder() instanceof CheckVaultHolder){
            Player player = (Player) event.getPlayer();
            CheckVaultHolder holder = (CheckVaultHolder) event.getInventory().getHolder();
            holder.saveVault();
            holder.backToMenu(player);
        }
    }
}
