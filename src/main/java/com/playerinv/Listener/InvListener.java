package com.playerinv.Listener;

import com.playerinv.ContextNode;
import com.playerinv.InvHolder.MainMenuHolder;
import com.playerinv.InvHolder.OtherMenuHolder;
import com.playerinv.InvHolder.VaultHolder_Large;
import com.playerinv.InvHolder.VaultHolder_Medium;
import com.playerinv.MainGUI.OtherMenu;
import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.playerinv.LocaleUtil.*;
import static com.playerinv.MainGUI.MainMenu.Main_GUI;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;
import static com.playerinv.PluginSet.VaultSoundPitch;
import static com.playerinv.ReturnMain.OpenInventoryRunnable_Spigot.ReturnMain;
import static com.playerinv.SQLite.SQLiteConnect.*;
import static com.playerinv.Scheduler.CheckView.checkPlayerViewLarge;
import static com.playerinv.Scheduler.CheckView.checkPlayerViewMedium;


public class InvListener implements Listener {

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) throws Exception {
        if(event.getInventory().getHolder() instanceof MainMenuHolder){
            int clicked_slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() == null){
                event.setCancelled(true);
                return;
            }
            if(event.getCurrentItem() != null){
                if(MainMenuItemMap.containsKey(clicked_slot + 1)){
                    switch (MainMenuItemMap.get(clicked_slot + 1)){
                        case "close":
                            player.closeInventory();
                            event.setCancelled(true);
                            return;
                        default:
                            if(MainMenuItemMap.get(clicked_slot + 1) != null){
                                if(OtherMenuFileList.contains(MainMenuItemMap.get(clicked_slot + 1))){
                                    String menu = MainMenuItemMap.get(clicked_slot + 1);
                                    FileConfiguration MenuFile = VaultMenuMap.get(menu);
                                    player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                    player.openInventory(OtherMenu.Other_GUI(MenuFile,player));
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
                if(MainMenuVaultSlotMap_Large.containsKey(clicked_slot + 1)){
                    Integer vault_num = MainMenuVaultSlotMap_Large.get(clicked_slot + 1);
                    if(vault_num <= 10){
                        if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + vault_num) || player.hasPermission(LargeFullInv)){
                            if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if(vault_num > 10){
                        if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission(LargeFullInv)){
                            if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                if(MainMenuVaultSlotMap_Medium.containsKey(clicked_slot + 1)){
                    int vault_num = MainMenuVaultSlotMap_Medium.get(clicked_slot + 1);
                    if(vault_num <= 15){
                        int old_vault_num = vault_num + 10;
                        if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + old_vault_num) || player.hasPermission(MediumFullInv)){
                            if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if(vault_num > 15){
                        if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission(MediumFullInv)){
                            if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
        if(event.getInventory().getHolder() instanceof OtherMenuHolder){
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() == null){
                event.setCancelled(true);
                return;
            }
            if(event.getCurrentItem() != null){
                String menu_key = OtherMenuInventoryMap.get(event.getInventory());
                String final_key = menu_key + ":" + (event.getRawSlot()+1);
                if(OtherMenuItemMap.containsKey(final_key)){
                    switch (OtherMenuItemMap.get(final_key)){
                        case "close":
                            player.closeInventory();
                            event.setCancelled(true);
                            return;
                        default:
                            if(OtherMenuItemMap.get(final_key) != null){
                                if(OtherMenuFileList.contains(OtherMenuItemMap.get(final_key))){
                                    String menu = OtherMenuItemMap.get(final_key);
                                    FileConfiguration MenuFile = VaultMenuMap.get(menu);
                                    player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                    player.openInventory(OtherMenu.Other_GUI(MenuFile,player));
                                    event.setCancelled(true);
                                } else if(OtherMenuItemMap.get(final_key) == "Main"){
                                    player.openInventory(Main_GUI(player));
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
                if(OtherMenuVaultSlotMap_Large.containsKey(final_key)){
                    Integer vault_num = OtherMenuVaultSlotMap_Large.get(final_key);
                    if(vault_num <= 10){
                        if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + vault_num) || player.hasPermission(LargeFullInv)){
                            if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if(vault_num > 10){
                        if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission(LargeFullInv)){
                            if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewLarge(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                if(OtherMenuVaultSlotMap_Medium.containsKey(final_key)){
                    int vault_num = OtherMenuVaultSlotMap_Medium.get(final_key);
                    if(vault_num <= 15){
                        int old_vault_num = vault_num + 10;
                        if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + old_vault_num) || player.hasPermission(MediumFullInv)){
                            if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if(vault_num > 15){
                        if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission(MediumFullInv)){
                            if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            } else {
                                event.setCancelled(true);
                                player.playSound(player.getLocation(), Sound.valueOf(VaultSoundValue), VaultSoundVolume,VaultSoundPitch);
                                player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                checkPlayerViewMedium(vault_num,player);
                                return;
                            }
                        } else {
                            player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvSaveTemp(InventoryClickEvent event){
        if (event.getInventory().getHolder() instanceof VaultHolder_Large){
            Player player = (Player) event.getWhoClicked();
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            TempInventory_Large.put(player.getUniqueId().toString() + ":" + Integer.valueOf(vault_num),event.getInventory());
            sendLog(player.getName() + ":L" + vault_num + " save temp");
        }
        if (event.getInventory().getHolder() instanceof VaultHolder_Medium){
            Player player = (Player) event.getWhoClicked();
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            TempInventory_Medium.put(player.getUniqueId().toString() + ":" + Integer.valueOf(vault_num),event.getInventory());
            sendLog(player.getName() + ":M" + vault_num + " save temp");
        }
    }

    @EventHandler
    public void onInvCloseSelf(InventoryCloseEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getHolder() instanceof VaultHolder_Large){
            TempPlayerInInventory_Large.put(player,true);
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            SQLiteConnect.updateInv_Large(con, player.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
            if(Return_to_main() && getReturnToggle(con, String.valueOf(player.getUniqueId()))) {
                if(isFolia){
                    FoliaLib.scheduling().entitySpecificScheduler(player).runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.openInventory(com.playerinv.MainGUI.MainMenu.Main_GUI(player));
                        }
                    },null,2);
                } else {
                    ReturnMain(player);
                }
            }
        }
        if (event.getInventory().getHolder() instanceof VaultHolder_Medium){
            TempPlayerInInventory_Medium.put(player,true);
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            String vault_title = event.getView().getTitle().toString();
            String vault_substring = vault_title.substring(vault_title.length()-3);
            String vault_num = vault_substring.replaceAll("[^(0-9)]","");
            vault_num.trim();
            SQLiteConnect.updateInv_Medium(con, player.getUniqueId().toString(), invdata, Integer.valueOf(vault_num));
            if(Return_to_main() && getReturnToggle(con, String.valueOf(player.getUniqueId()))) {
                if(isFolia){
                    FoliaLib.scheduling().entitySpecificScheduler(player).runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.openInventory(com.playerinv.MainGUI.MainMenu.Main_GUI(player));
                        }
                    },null,2);
                } else {
                    ReturnMain(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) throws Exception {
        Player player = (Player) event.getPlayer();
        if(!SQLiteConnect.hasDataToggle(con,player.getUniqueId().toString())){
            SQLiteConnect.insertToggle(con,player.getUniqueId().toString(),false);
        }
        if(!SQLiteConnect.hasDataReturnToggle(con,player.getUniqueId().toString())){
            SQLiteConnect.insertReturnToggle(con,player.getUniqueId().toString(),true);
        }
    }

    @EventHandler
    public void PressF(PlayerSwapHandItemsEvent event)throws Exception {
        Player player = (Player) event.getPlayer();
        if (player.hasPermission("playerinv.gui.open")) {
            Boolean onPress = plugin.getConfig().getBoolean("KeysOpen");
            Boolean isOn = SQLiteConnect.getToggle(con,player.getUniqueId().toString());
            if (onPress && isOn) {
                if(need_sneaking() && player.isSneaking()){
                    event.setCancelled(true);
                    player.performCommand("inv");
                } else {
                    event.setCancelled(true);
                    player.performCommand("inv");
                }
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event){
        Boolean lp_proxy = PlayerInv.plugin.getConfig().getBoolean("Luckperms-proxy-support");
        Boolean lp_give = plugin.getConfig().getBoolean("Luckperms-give-permissions");
        String prefix = prefix();
        Boolean title = PlayerInv.plugin.getConfig().getBoolean("Voucher.Title");
        Player p = event.getPlayer();
        if(p.getItemInHand() == null){
            return;
        }
        if(p.getItemInHand().getItemMeta() == null){
            return;
        }
        if(p.getItemInHand().getItemMeta().getDisplayName() == null){
            return;
        }
        if(p.getItemInHand().getItemMeta().getDisplayName().equals(color(Locale_Voucher_Large_DisplayName()))){
            String item_target = getValueFromLore(p.getItemInHand(),"Owner");
            if((item_target.equals(p.getName()) && Voucher_Set_Owner()) || !Voucher_Set_Owner()){
                if(p.isOp() || p.hasPermission("playerinv.admin") || p.hasPermission(LargeFullInv)){
                    p.sendMessage(color(prefix + Messages_Voucher_full()));
                    return;
                }
                for(int i=1;i<(Large_Amount + 1);i++){
                    if(i <= 10) {
                        if (!p.hasPermission("playerinv.inv." + i)) {
                            if(!p.hasPermission("playerinv.large.inv." + i)) {
                                if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                    plugin.perms.playerAdd(null, p, "playerinv.large.inv." + i);
                                }
                                if (hasLuckPerms && lp_give && lp_proxy) {
                                    ContextNode.addPermissionWithContext_Large(p, i,0);
                                }
                                if (hasLuckPerms && lp_give && !lp_proxy) {
                                    ContextNode.addPermission_Large(p, i,0);
                                }
                                if(p.getItemInHand().getAmount() == 1){
                                    p.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                                } else {
                                    int before_amount = p.getItemInHand().getAmount();
                                    p.getItemInHand().setAmount(before_amount - 1);
                                }
                                p.sendMessage(color(prefix + Messages_Voucher_use_large(i)));
                                if (title) {
                                    p.sendTitle(color(Locale_Voucher_Large_Title(i)), color(Locale_Voucher_Large_Subtitle(i)), 10, 60, 10);
                                }
                                if (Voucher_sound_bool()) {
                                    p.playSound(p.getLocation(), Sound.valueOf(VoucherSoundValue), VoucherSoundVolume, VoucherSoundPitch);
                                }
                                return;
                            } else {
                                continue;
                            }
                        }
                    }
                    if(i > 10) {
                        if (!p.hasPermission("playerinv.large.inv." + i)) {
                            if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                plugin.perms.playerAdd(null, p, "playerinv.large.inv." + i);
                            }
                            if (hasLuckPerms && lp_give && lp_proxy) {
                                ContextNode.addPermissionWithContext_Large(p, i,0);
                            }
                            if (hasLuckPerms && lp_give && !lp_proxy) {
                                ContextNode.addPermission_Large(p, i,0);
                            }
                            if(p.getItemInHand().getAmount() == 1){
                                p.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            } else {
                                int before_amount = p.getItemInHand().getAmount();
                                p.getItemInHand().setAmount(before_amount - 1);
                            }
                            p.sendMessage(color(prefix + Messages_Voucher_use_large(i)));
                            if (title) {
                                p.sendTitle(color(Locale_Voucher_Large_Title(i)), color(Locale_Voucher_Large_Subtitle(i)), 10, 60, 10);
                            }
                            if (Voucher_sound_bool()) {
                                p.playSound(p.getLocation(), Sound.valueOf(VoucherSoundValue), VoucherSoundVolume, VoucherSoundPitch);
                            }
                            return;
                        }
                    }
                }
                p.sendMessage(color(prefix + Messages_Voucher_full()));
                return;
            } else if(!item_target.equals(p.getName()) && Voucher_Set_Owner()){
                p.sendMessage(color(prefix + Voucher_cannot_use()));
                return;
            }
        }
        if(p.getItemInHand().getItemMeta().getDisplayName().equals(color(Locale_Voucher_Medium_DisplayName()))){
            String item_target = getValueFromLore(p.getItemInHand(),"Owner");
            if((item_target.equals(p.getName()) && Voucher_Set_Owner()) || !Voucher_Set_Owner()){
                if(p.isOp() || p.hasPermission("playerinv.admin") || p.hasPermission(MediumFullInv)){
                    p.sendMessage(color(prefix + Messages_Voucher_full()));
                    return;
                }
                for(int i=1;i<(Medium_Amount + 1);i++){
                    if(i <= 15){
                        int old_num = i + 10;
                        if(!p.hasPermission("playerinv.inv." + old_num)) {
                            if(!p.hasPermission("playerinv.medium.inv." + i)) {
                                if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                    plugin.perms.playerAdd(null, p, "playerinv.medium.inv." + i);
                                }
                                if (hasLuckPerms && lp_proxy && lp_give) {
                                    ContextNode.addPermissionWithContext_Medium(p, i,0);
                                }
                                if (hasLuckPerms && !lp_proxy && lp_give) {
                                    ContextNode.addPermission_Medium(p, i,0);
                                }
                                if(p.getItemInHand().getAmount() == 1){
                                    p.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                                } else {
                                    int before_amount = p.getItemInHand().getAmount();
                                    p.getItemInHand().setAmount(before_amount - 1);
                                }
                                p.sendMessage(color(prefix + Messages_Voucher_use_medium(i)));
                                if (title) {
                                    p.sendTitle(color(Locale_Voucher_Medium_Title(i)), color(Locale_Voucher_Medium_Subtitle(i)), 10, 60, 10);
                                }
                                if (Voucher_sound_bool()) {
                                    p.playSound(p.getLocation(), Sound.valueOf(VoucherSoundValue), VoucherSoundVolume, VoucherSoundPitch);
                                }
                                return;
                            } else {
                                continue;
                            }
                        }
                    }
                    if(i > 15){
                        if(!p.hasPermission("playerinv.medium.inv." + i)) {
                            if((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)){
                                plugin.perms.playerAdd(null, p, "playerinv.medium.inv." + i);
                            }
                            if(hasLuckPerms && lp_proxy && lp_give){
                                ContextNode.addPermissionWithContext_Medium(p,i,0);
                            }
                            if (hasLuckPerms && !lp_proxy && lp_give) {
                                ContextNode.addPermission_Medium(p, i,0);
                            }
                            if(p.getItemInHand().getAmount() == 1){
                                p.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            } else {
                                int before_amount = p.getItemInHand().getAmount();
                                p.getItemInHand().setAmount(before_amount - 1);
                            }
                            p.sendMessage(color(prefix + Messages_Voucher_use_medium(i)));
                            if (title) {
                                p.sendTitle(color(Locale_Voucher_Medium_Title(i)), color(Locale_Voucher_Medium_Subtitle(i)), 10, 60, 10);
                            }
                            if (Voucher_sound_bool()) {
                                p.playSound(p.getLocation(), Sound.valueOf(VoucherSoundValue), VoucherSoundVolume, VoucherSoundPitch);
                            }
                            return;
                        }
                    }
                }
                p.sendMessage(color(prefix + Messages_Voucher_full()));
                return;
            } else if(!item_target.equals(p.getName()) && Voucher_Set_Owner()){
                p.sendMessage(color(prefix + Voucher_cannot_use()));
                return;
            }
        }
    }

    @EventHandler
    public void onBlackListBlockClickEvent(InventoryClickEvent event){
        if((event.getInventory().getHolder() instanceof VaultHolder_Large) || (event.getInventory().getHolder() instanceof VaultHolder_Medium)) {
            String prefix = prefix();
            Player player = (Player) event.getWhoClicked();
            if(event.getAction() == InventoryAction.HOTBAR_SWAP){
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
            ItemStack item = event.getCurrentItem();
            if(item == null){
                return;
            }
            if(plugin.getConfig().getStringList("Inventory.Blacklist").isEmpty()){
                return;
            }
            if(plugin.getConfig().getStringList("Inventory.Blacklist").contains(item.getType().toString())) {
                Boolean cancel = true;
                switch (event.getAction()) {
                    case DROP_ALL_SLOT:
                    case DROP_ONE_SLOT:
                    case HOTBAR_MOVE_AND_READD:
                    case PICKUP_ALL:
                    case PICKUP_HALF:
                    case PICKUP_ONE:
                    case PICKUP_SOME:
                    case PLACE_ALL:
                    case PLACE_ONE:
                    case PLACE_SOME:
                    case SWAP_WITH_CURSOR:
                    case MOVE_TO_OTHER_INVENTORY:
                        cancel = true;
                        break;
                }
                if (cancel) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    player.sendMessage(color(prefix + Messages_Vault_blacklist_items()));
                }
            }
        }
    }
}
