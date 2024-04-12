package com.playerinv.Listener;

import com.playerinv.Command.InvCommand;
import com.playerinv.PermItem.PermItem;
import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import java.sql.SQLException;

import static com.playerinv.PlayerInv.plugin;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.*;


public class InvListener implements Listener {

    public static Player optarget;

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) throws Exception {
        String guititle = PlayerInv.plugin.getConfig().getString("GUI.Title");
        String noinv = plugin.getConfig().getString("NoPermissionMessage.Inv");
        String prefix = plugin.getConfig().getString("Prefix");
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if(event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
            Inventory Inv_1 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#1"));
            Inventory Inv_2 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#2"));
            Inventory Inv_3 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#3"));
            Inventory Inv_4 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#4"));
            Inventory Inv_5 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#5"));
            Inventory Inv_6 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#6"));
            Inventory Inv_7 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#7"));
            Inventory Inv_8 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#8"));
            Inventory Inv_9 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#9"));
            Inventory Inv_10 = Bukkit.createInventory((InventoryHolder) player, 54, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#10"));
            Inventory Inv_11 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#11"));
            Inventory Inv_12 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#12"));
            Inventory Inv_13 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#13"));
            Inventory Inv_14 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#14"));
            Inventory Inv_15 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#15"));
            Inventory Inv_16 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#16"));
            Inventory Inv_17 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#17"));
            Inventory Inv_18 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#18"));
            Inventory Inv_19 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#19"));
            Inventory Inv_20 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#20"));
            Inventory Inv_21 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#21"));
            Inventory Inv_22 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#22"));
            Inventory Inv_23 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#23"));
            Inventory Inv_24 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#24"));
            Inventory Inv_25 = Bukkit.createInventory((InventoryHolder) player, 27, ChatColor.translateAlternateColorCodes('&', "²Ö¿â#25"));
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 2) {
                    if (player.hasPermission("playerinv.inv.1") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 1;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 1) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 1)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 1) == null) {
                            player.openInventory(Inv_1);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 3) {
                    if (player.hasPermission("playerinv.inv.2") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 2;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 2) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 2)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 2) == null) {
                            player.openInventory(Inv_2);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 4) {
                    if (player.hasPermission("playerinv.inv.3") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 3;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 3) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 3)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 3) == null) {
                            player.openInventory(Inv_3);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 5) {
                    if (player.hasPermission("playerinv.inv.4") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 4;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 4) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 4)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 4) == null) {
                            player.openInventory(Inv_4);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 6) {
                    if (player.hasPermission("playerinv.inv.5") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 5;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 5) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 5)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 5) == null) {
                            player.openInventory(Inv_5);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 11) {
                    if (player.hasPermission("playerinv.inv.6") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 6;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 6) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 6)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 6) == null) {
                            player.openInventory(Inv_6);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 12) {
                    if (player.hasPermission("playerinv.inv.7") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 7;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 7) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 7)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 7) == null) {
                            player.openInventory(Inv_7);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 13) {
                    if (player.hasPermission("playerinv.inv.8") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 8;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 8) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 8)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 8) == null) {
                            player.openInventory(Inv_8);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 14) {
                    if (player.hasPermission("playerinv.inv.9") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 9;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 9) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 9)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 9) == null) {
                            player.openInventory(Inv_9);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 15) {
                    if (player.hasPermission("playerinv.inv.10") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 10;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 10) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 10)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 10) == null) {
                            player.openInventory(Inv_10);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 29) {
                    if (player.hasPermission("playerinv.inv.11") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 11;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 11) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 11)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 11) == null) {
                            player.openInventory(Inv_11);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 30) {
                    if (player.hasPermission("playerinv.inv.12") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 12;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 12) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 12)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 12) == null) {
                            player.openInventory(Inv_12);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 31) {
                    if (player.hasPermission("playerinv.inv.13") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 13;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 13) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 13)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 13) == null) {
                            player.openInventory(Inv_13);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 32) {
                    if (player.hasPermission("playerinv.inv.14") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 14;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 14) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 14)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 14) == null) {
                            player.openInventory(Inv_14);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 33) {
                    if (player.hasPermission("playerinv.inv.15") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 15;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 15) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 15)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 15) == null) {
                            player.openInventory(Inv_15);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 38) {
                    if (player.hasPermission("playerinv.inv.16") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 16;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 16) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 16)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 16) == null) {
                            player.openInventory(Inv_16);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 39) {
                    if (player.hasPermission("playerinv.inv.17") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 17;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 17) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 17)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 17) == null) {
                            player.openInventory(Inv_17);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 40) {
                    if (player.hasPermission("playerinv.inv.18") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 18;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 18) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 18)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 18) == null) {
                            player.openInventory(Inv_18);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 41) {
                    if (player.hasPermission("playerinv.inv.19") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 19;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 19) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 19)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 19) == null) {
                            player.openInventory(Inv_19);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 42) {
                    if (player.hasPermission("playerinv.inv.20") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 20;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 20) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 20)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 20) == null) {
                            player.openInventory(Inv_20);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 47) {
                    if (player.hasPermission("playerinv.inv.21") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 21;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 21) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 21)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 21) == null) {
                            player.openInventory(Inv_21);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 48) {
                    if (player.hasPermission("playerinv.inv.22") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 22;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 22) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 22)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 22) == null) {
                            player.openInventory(Inv_22);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 49) {
                    if (player.hasPermission("playerinv.inv.23") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 23;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 23) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 23)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 23) == null) {
                            player.openInventory(Inv_23);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 50) {
                    if (player.hasPermission("playerinv.inv.24") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 24;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 24) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 24)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 24) == null) {
                            player.openInventory(Inv_24);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',guititle))) {
                if (slot == 51) {
                    if (player.hasPermission("playerinv.inv.20") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
                        optarget = player;
                        vaultnum = 25;
                        if(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 25) != null){
                            player.openInventory(inventoryFromBase64(SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 25)));
                        }
                        if (SQLiteConnect.InvCode(con, optarget.getPlayer().getUniqueId().toString(), 25) == null) {
                            player.openInventory(Inv_25);
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + noinv));
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    public static Player ptarget;


    @EventHandler
    public void onInvClose(InventoryCloseEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#1"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 1);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#2"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 2);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#3"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 3);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#4"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 4);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#5"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 5);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#6"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 6);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#7"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 7);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#8"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 8);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#9"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 9);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#10"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 10);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#11"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 11);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#12"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 12);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#13"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 13);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#14"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 14);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#15"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 15);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#16"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 16);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#17"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 17);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#18"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 18);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#19"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 19);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#20"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 20);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#21"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 21);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#22"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 22);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#23"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 23);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#24"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 24);
        }
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "²Ö¿â#25"))){
            String invdata = PluginSet.inventoryToBase64(event.getInventory());
            ptarget = player;
            SQLiteConnect.update(con, ptarget.getUniqueId().toString(), invdata, 25);
        }
    }

    @EventHandler
    public void PressF(PlayerSwapHandItemsEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.hasPermission("playerinv.gui.open")) {
            Boolean onPress = plugin.getConfig().getBoolean("KeysOpen");
            Boolean isOn = InvCommand.pressToggle.get(player);
            if (onPress && isOn) {
                event.setCancelled(true);
                player.performCommand("inv");
            }
        }
    }

    @EventHandler
    public void ToggleMap(PlayerJoinEvent event){
        Player player = (Player) event.getPlayer();
        if(InvCommand.pressToggle.get(player) == null){
            InvCommand.pressToggle.put(player, true);
        }
    }

    @EventHandler
    public void DATAinDB(PlayerJoinEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        Boolean mysql = plugin.getConfig().getBoolean("DataBases.MySQL");
        if(!mysql){
            if(!SQLiteConnect.hasData(con, event.getPlayer().getUniqueId().toString())){
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 1, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 2, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 3, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 4, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 5, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 6, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 7, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 8, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 9, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 10, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 11, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 12, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 13, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 14, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 15, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 16, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 17, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 18, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 19, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 20, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 21, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 22, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 23, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 24, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 25, null);
            }
        }
        if(mysql){
            if(!SQLiteConnect.mysqlhasData(con, event.getPlayer().getUniqueId().toString())){
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 1, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 2, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 3, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 4, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 5, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 6, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 7, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 8, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 9, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 10, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 11, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 12, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 13, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 14, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 15, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 16, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 17, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 18, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 19, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 20, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 21, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 22, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 23, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 24, null);
                SQLiteConnect.insert(con, player.getPlayer().getUniqueId().toString(), 25, null);
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event){
        String largechartitle = PlayerInv.plugin.getConfig().getString("Item.Large.Title");
        String mediumchartitle = PlayerInv.plugin.getConfig().getString("Item.Medium.Title");
        String prefix = PlayerInv.plugin.getConfig().getString("Prefix");
        String full = PlayerInv.plugin.getConfig().getString("ItemMessage.Full");
        Boolean title = PlayerInv.plugin.getConfig().getBoolean("UseItem.Title");
        Boolean sound = PlayerInv.plugin.getConfig().getBoolean("UseItem.Sound");
        Player p = event.getPlayer();
        if(p.getItemInHand().getItemMeta() == null){
            return;
        }
        if(p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&',largechartitle))){
            Itemtarget = getValueFromLore(p.getItemInHand(),"Owner");
            if(p.hasPermission("playerinv.inv.1")
                    && p.hasPermission("playerinv.inv.2")
                    && p.hasPermission("playerinv.inv.3")
                    && p.hasPermission("playerinv.inv.4")
                    && p.hasPermission("playerinv.inv.5")
                    && p.hasPermission("playerinv.inv.6")
                    && p.hasPermission("playerinv.inv.7")
                    && p.hasPermission("playerinv.inv.8")
                    && p.hasPermission("playerinv.inv.9")
                    && p.hasPermission("playerinv.inv.10")
                    && Itemtarget.equals(p.getName())){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + full));
            }
            if(Itemtarget.equals(p.getName())){
                for(int i=1;i<11;i++){
                    if(!p.hasPermission("playerinv.inv." + i)) {
                        plugin.perms.playerAdd(null, p, "playerinv.inv." + i);
                        p.getInventory().removeItem(PermItem.largeitem(p));
                        String message = PlayerInv.plugin.getConfig().getString("ItemMessage.Use.Large").replaceAll("%large_vault_number%", String.valueOf(i));
                        String largetitle = PlayerInv.plugin.getConfig().getString("UseItem.Large.Title").replaceAll("%large_vault_number%", String.valueOf(i));
                        String largesubtitle = PlayerInv.plugin.getConfig().getString("UseItem.Large.Subtitle").replaceAll("%large_vault_number%", String.valueOf(i));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
                        if (title) {
                            p.sendTitle(ChatColor.translateAlternateColorCodes('&', largetitle), ChatColor.translateAlternateColorCodes('&', largesubtitle), 10, 60, 10);
                        }
                        if (sound) {
                            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1.05F);
                        }
                        break;
                    }
                }
            } else if(!Itemtarget.equals(p.getName())){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cÄãÎÞ·¨Ê¹ÓÃ¸Ã¶Ò»»È¯"));
            }
        }
        if(p.getItemInHand().getItemMeta() == null){
            return;
        }
        if(p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&',mediumchartitle))){
            Itemtarget = getValueFromLore(p.getItemInHand(),"Owner");
            if(p.hasPermission("playerinv.inv.11")
                    && p.hasPermission("playerinv.inv.12")
                    && p.hasPermission("playerinv.inv.13")
                    && p.hasPermission("playerinv.inv.14")
                    && p.hasPermission("playerinv.inv.15")
                    && p.hasPermission("playerinv.inv.16")
                    && p.hasPermission("playerinv.inv.17")
                    && p.hasPermission("playerinv.inv.18")
                    && p.hasPermission("playerinv.inv.19")
                    && p.hasPermission("playerinv.inv.20")
                    && p.hasPermission("playerinv.inv.21")
                    && p.hasPermission("playerinv.inv.22")
                    && p.hasPermission("playerinv.inv.23")
                    && p.hasPermission("playerinv.inv.24")
                    && p.hasPermission("playerinv.inv.25")
                    && Itemtarget.equals(p.getName())){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + full));
            }
            if(Itemtarget.equals(p.getName())){
                for(int i=11;i<26;i++){
                    if(!p.hasPermission("playerinv.inv." + i)) {
                        plugin.perms.playerAdd(null, p, "playerinv.inv." + i);
                        p.getInventory().removeItem(PermItem.mediumitem(p));
                        String message = PlayerInv.plugin.getConfig().getString("ItemMessage.Use.Medium").replaceAll("%medium_vault_number%", String.valueOf(i));
                        String mediumtitle = PlayerInv.plugin.getConfig().getString("UseItem.Medium.Title").replaceAll("%medium_vault_number%", String.valueOf(i));
                        String mediumsubtitle = PlayerInv.plugin.getConfig().getString("UseItem.Medium.Subtitle").replaceAll("%medium_vault_number%", String.valueOf(i));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
                        if (title) {
                            p.sendTitle(ChatColor.translateAlternateColorCodes('&', mediumtitle), ChatColor.translateAlternateColorCodes('&', mediumsubtitle), 10, 60, 10);
                        }
                        if (sound) {
                            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1.05F);
                        }
                        break;
                    }
                }
            } else if(!Itemtarget.equals(p.getName())){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cÄãÎÞ·¨Ê¹ÓÃ¸Ã¶Ò»»È¯"));
            }
        }
    }
}
