package com.playerinv.Listener;

import com.playerinv.Util.InvHolder.VaultHolder_Large;
import com.playerinv.Util.InvHolder.VaultHolder_Medium;
import com.playerinv.Util.InvHolder.VaultMenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import static com.playerinv.PlayerInv.vaultPlaceEvent;
import static com.playerinv.Util.LoadUtil.*;

public class VaultMenuListener implements Listener {

    @EventHandler
    public void onVaultMenuClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof VaultMenuHolder){
            int clicked_slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            switch(event.getAction().name()) {
                case "HOTBAR_SWAP":
                case "HOTBAR_MOVE_AND_READD":
                case "MOVE_TO_OTHER_INVENTORY":
                    event.setCancelled(true);
                    break;
            }
            if(allowClickGUI && clicked_slot >= event.getInventory().getSize()){
                return;
            }
            event.setCancelled(true);
            VaultMenuHolder holder = (VaultMenuHolder) event.getInventory().getHolder();
            holder.runOperation(clicked_slot,event.getClick().name());
        }
    }

    @EventHandler
    public void onVaultMenuDrag(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof VaultMenuHolder){
            if(event.getRawSlots().stream().anyMatch(num -> num < event.getInventory().getSize())){
                event.setCancelled(true);
                return;
            }
        }
    }
}
