package com.playerinv.Listener;

import com.playerinv.InvHolder.VaultHolder_Large;
import com.playerinv.InvHolder.VaultHolder_Medium;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.playerinv.PluginSet.sendLog;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class VaultMoveItemListener implements Listener {

    @EventHandler
    public void onMoveItemEvent(InventoryClickEvent event)throws Exception{
        if((event.getInventory().getHolder() instanceof VaultHolder_Large)) {
            switch(event.getAction()){
                case MOVE_TO_OTHER_INVENTORY:
                case DROP_ALL_CURSOR:
                case DROP_ALL_SLOT:
                case DROP_ONE_CURSOR:
                case DROP_ONE_SLOT:
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                    Player player = (Player) event.getWhoClicked();
                    sendLog(player.getName() + " moving large vault item: " + event.getAction());
                    String invdata = PluginSet.inventoryToBase64(event.getInventory());
                    String vault_title = event.getView().getTitle().toString();
                    String vault_substring = vault_title.substring(vault_title.length()-3);
                    String vault_num = vault_substring.replaceAll("[^(0-9)]","");
                    vault_num.trim();
                    SQLiteConnect.updateInv_Large(con, player.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
                    break;
            }
            return;
        }
        if((event.getInventory().getHolder() instanceof VaultHolder_Medium)) {
            switch(event.getAction()){
                case MOVE_TO_OTHER_INVENTORY:
                case DROP_ALL_CURSOR:
                case DROP_ALL_SLOT:
                case DROP_ONE_CURSOR:
                case DROP_ONE_SLOT:
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                    Player player = (Player) event.getWhoClicked();
                    sendLog(player.getName() + " moving medium vault item: " + event.getAction());
                    String invdata = PluginSet.inventoryToBase64(event.getInventory());
                    String vault_title = event.getView().getTitle().toString();
                    String vault_substring = vault_title.substring(vault_title.length()-3);
                    String vault_num = vault_substring.replaceAll("[^(0-9)]","");
                    vault_num.trim();
                    SQLiteConnect.updateInv_Medium(con, player.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
                    break;
            }
            return;
        }
    }
}
