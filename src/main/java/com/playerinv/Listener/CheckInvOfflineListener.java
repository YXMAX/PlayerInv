package com.playerinv.Listener;


import com.playerinv.InvHolder.*;
import com.playerinv.MainGUI.OtherMenu;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class CheckInvOfflineListener implements Listener {

    @EventHandler
    public void onCheckGUIClick(InventoryClickEvent event) throws Exception {
        if(event.getInventory().getHolder() instanceof Check_MainMenuHolder_Offline){
            Player player = (Player) event.getWhoClicked();
            OfflinePlayer target = Check_OfflinePlayerMap.get(player);
            if(event.getCurrentItem() == null){return;}
            if(event.getCurrentItem() != null){
                if(Check_MainMenuItemMap.containsKey(event.getRawSlot() + 1)){
                    switch (Check_MainMenuItemMap.get(event.getRawSlot() + 1)){
                        case "close":
                            player.closeInventory();
                            event.setCancelled(true);
                            return;
                        default:
                            if(Check_MainMenuItemMap.get(event.getRawSlot() + 1) != null){
                                if(Check_OtherMenuFileList.contains(Check_MainMenuItemMap.get(event.getRawSlot() + 1))){
                                    String menu = Check_MainMenuItemMap.get(event.getRawSlot() + 1);
                                    FileConfiguration MenuFile = Check_VaultMenuMap.get(menu);
                                    player.openInventory(OtherMenu.Check_Other_GUI_Offline(MenuFile,target));
                                    event.setCancelled(true);
                                } else {
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                event.setCancelled(true);
                                return;
                            }
                    }
                }
                if(Check_MainMenuVaultSlotMap_Large.containsKey(event.getRawSlot() + 1)){
                    Integer vault_num = Check_MainMenuVaultSlotMap_Large.get(event.getRawSlot() + 1);
                    if(!SQLiteConnect.hasData_Large(con,target.getUniqueId().toString(),vault_num)){
                        SQLiteConnect.insert_Large(con,target.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                        player.openInventory(inventoryFromBase64_Large_Check_Offline(SQLiteConnect.InvCode_Large(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    } else {
                        player.openInventory(inventoryFromBase64_Large_Check_Offline(SQLiteConnect.InvCode_Large(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    }
                }
                if(Check_MainMenuVaultSlotMap_Medium.containsKey(event.getRawSlot() + 1)){
                    int vault_num = Check_MainMenuVaultSlotMap_Medium.get(event.getRawSlot() + 1);
                    if(!SQLiteConnect.hasData_Medium(con,target.getUniqueId().toString(),vault_num)){
                        SQLiteConnect.insert_Medium(con,target.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                        player.openInventory(inventoryFromBase64_Medium_Check_Offline(SQLiteConnect.InvCode_Medium(con, target.getUniqueId().toString(), vault_num),target, vault_num));
                        return;
                    } else {
                        player.openInventory(inventoryFromBase64_Medium_Check_Offline(SQLiteConnect.InvCode_Medium(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
        if(event.getInventory().getHolder() instanceof Check_OtherMenuHolder_Offline){
            Player player = (Player) event.getWhoClicked();
            OfflinePlayer target = Check_OfflinePlayerMap.get(player);
            if(event.getCurrentItem() == null){return;}
            if(event.getCurrentItem() != null){
                String menu_key = Check_OtherMenuInventoryMap.get(event.getInventory());
                String final_key = menu_key + ":" + (event.getRawSlot()+1);
                if(Check_OtherMenuItemMap.containsKey(final_key)){
                    switch (Check_OtherMenuItemMap.get(final_key)){
                        case "close":
                            player.closeInventory();
                            event.setCancelled(true);
                            return;
                        default:
                            if(Check_OtherMenuItemMap.get(final_key) != null){
                                if(Check_OtherMenuFileList.contains(Check_OtherMenuItemMap.get(final_key))){
                                    String menu = Check_OtherMenuItemMap.get(final_key);
                                    FileConfiguration MenuFile = Check_VaultMenuMap.get(menu);
                                    player.openInventory(OtherMenu.Check_Other_GUI_Offline(MenuFile,target));
                                    event.setCancelled(true);
                                } else if(Check_OtherMenuItemMap.get(final_key) == "Main"){
                                    player.openInventory(com.playerinv.MainGUI.MainMenu.Main_GUI(player));
                                    event.setCancelled(true);
                                    return;
                                } else {
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                event.setCancelled(true);
                                return;
                            }
                    }
                }
                if(Check_OtherMenuVaultSlotMap_Large.containsKey(final_key)){
                    Integer vault_num = Check_OtherMenuVaultSlotMap_Large.get(final_key);
                    if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                        event.setCancelled(true);
                        SQLiteConnect.insert_Large(con,target.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                        player.openInventory(inventoryFromBase64_Large_Check_Offline(SQLiteConnect.InvCode_Large(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    } else {
                        player.openInventory(inventoryFromBase64_Large_Check_Offline(SQLiteConnect.InvCode_Large(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    }
                }
                if(Check_OtherMenuVaultSlotMap_Medium.containsKey(final_key)){
                    int vault_num = Check_OtherMenuVaultSlotMap_Medium.get(final_key);
                    if(!SQLiteConnect.hasData_Medium(con,target.getUniqueId().toString(),vault_num)){
                        event.setCancelled(true);
                        SQLiteConnect.insert_Medium(con,target.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                        player.openInventory(inventoryFromBase64_Medium_Check_Offline(SQLiteConnect.InvCode_Medium(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    } else {
                        player.openInventory(inventoryFromBase64_Medium_Check_Offline(SQLiteConnect.InvCode_Medium(con, target.getUniqueId().toString(), vault_num), target,vault_num));
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getHolder() instanceof Check_VaultHolder_Large_Offline){
            OfflinePlayer target = Check_OfflinePlayerMap.get(player);
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            SQLiteConnect.updateInv_Large(con, target.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
        }
        if (event.getInventory().getHolder() instanceof Check_VaultHolder_Medium_Offline){
            OfflinePlayer target = Check_OfflinePlayerMap.get(player);
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            SQLiteConnect.updateInv_Medium(con, target.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
        }
    }
}
