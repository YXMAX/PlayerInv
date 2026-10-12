package com.playerinv.listener;

import com.playerinv.constants.ConfigConstants;
import com.playerinv.gui.holder.CustomMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class CustomMenuListener implements Listener {

    @EventHandler
    public void onCustomMenuClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof CustomMenuHolder){
            switch(event.getAction().name()) {
                case "HOTBAR_SWAP":
                case "HOTBAR_MOVE_AND_READD":
                case "MOVE_TO_OTHER_INVENTORY":
                    event.setCancelled(true);
                    break;
            }
            if(ConfigConstants.ALLOW_CLICK_INVENTORY_IN_MENU && event.getRawSlot() >= event.getInventory().getSize()){
                return;
            }
            event.setCancelled(true);
            CustomMenuHolder holder = (CustomMenuHolder) event.getInventory().getHolder();
            holder.runClickEvent(event);
        }
    }

    @EventHandler
    public void onCustomMenuDrag(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof CustomMenuHolder){
            if(event.getRawSlots().stream().anyMatch(num -> num < event.getInventory().getSize())){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onCustomMenuClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof CustomMenuHolder){
            CustomMenuHolder holder = (CustomMenuHolder) event.getInventory().getHolder();
            holder.runCloseEvent(event);
        }
    }
}
