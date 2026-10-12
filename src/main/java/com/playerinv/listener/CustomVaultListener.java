package com.playerinv.listener;

import com.playerinv.gui.holder.CustomVaultHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class CustomVaultListener implements Listener {

    @EventHandler
    public void onCustomVaultClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof CustomVaultHolder){
            ((CustomVaultHolder) event.getInventory().getHolder()).runClickEvent(event);
        }
    }

    @EventHandler
    public void onCustomVaultDrag(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof CustomVaultHolder){
            ((CustomVaultHolder) event.getInventory().getHolder()).runDragEvent(event);
        }
    }

    @EventHandler
    public void onCustomVaultClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof CustomVaultHolder){
            ((CustomVaultHolder) event.getInventory().getHolder()).runCloseEvent(event);
        }
    }
}
