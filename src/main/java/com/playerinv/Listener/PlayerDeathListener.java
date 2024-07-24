package com.playerinv.Listener;

import com.playerinv.InvHolder.VaultHolder_Large;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.GameRule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.playerinv.LocaleUtil.player_death_store_notice;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class PlayerDeathListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event){
        Boolean value = false;
        if(isBelow113){
            String v = event.getEntity().getWorld().getGameRuleValue("keepInventory");
            if(v.equals("false")){
                value = false;
            } else if(v.equals("true")){
                value = true;
            }
        } else {
            value = event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        }
        Boolean enable = plugin.getConfig().getBoolean("Player-death-store.Enabled");
        Boolean ignore = plugin.getConfig().getBoolean("Player-death-store.ignore-keepInventory");
        if(enable && (event.getEntity().getType() == EntityType.PLAYER)) {
            if (value && !ignore) {
                return;
            } else {
                if(event.getEntity().getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large){
                    event.getEntity().closeInventory();
                }
                if (event.getEntity().hasPermission("playerinv.death.store")) {
                    List<ItemStack> item_list = event.getDrops();
                    for(int i=1;i < (Large_Amount + 1);i++){
                        if(event.getEntity().hasPermission("playerinv.inv." + i) || event.getEntity().hasPermission("playerinv.large.inv." + i)){
                            int empty = getLargeEmptySlots(i,event.getEntity());
                            if(empty > item_list.size()){
                                try {
                                    Inventory inventory = inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, String.valueOf(event.getEntity().getUniqueId()),i),i);
                                    for (ItemStack itemStack : item_list) {
                                        inventory.addItem(itemStack);
                                    }
                                    String code = inventoryToBase64(inventory);
                                    SQLiteConnect.updateInv_Large(con, String.valueOf(event.getEntity().getUniqueId()),code,i);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                event.getDrops().removeAll(item_list);
                                event.getEntity().sendMessage(color(prefix() + player_death_store_notice(String.valueOf(i))));
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }
}
