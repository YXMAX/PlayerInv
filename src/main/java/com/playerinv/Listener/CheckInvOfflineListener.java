package com.playerinv.Listener;


import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static com.playerinv.Command.InvCommand.checktarget;
import static com.playerinv.Command.InvCommand.offlinechecktarget;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class CheckInvOfflineListener implements Listener {

    @EventHandler
    public void onCheckGUIClick(InventoryClickEvent event) throws Exception {
        if(event.getView().getTitle().contains("的主GUI")) {
            String prefix = PlayerInv.plugin.getConfig().getString("Prefix");
            Player player = (Player) event.getWhoClicked();
            OfflinePlayer target = offlinechecktarget;
            int slot = event.getRawSlot();
            if (event.getView().getTitle().contains("的主GUI")) {
                if (slot == 2) {
                    checkvaultnum = 1;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 1) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 1)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 1) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 3) {
                    checkvaultnum = 2;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 2) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 2)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 2) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 4) {
                    checkvaultnum = 3;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 3) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 3)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 3) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 5) {
                    checkvaultnum = 4;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 4) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 4)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 4) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 6) {
                    checkvaultnum = 5;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 5) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 5)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 5) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 11) {
                    checkvaultnum = 6;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 6) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 6)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 6) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 12) {
                    checkvaultnum = 7;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 7) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 7)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 7) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 13) {
                    checkvaultnum = 8;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 8) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 8)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 8) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 14) {
                    checkvaultnum = 9;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 9) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 9)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 9) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 15) {
                    checkvaultnum = 10;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 10) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 10)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 10) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 20) {
                    checkvaultnum = 11;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 11) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 11)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 11) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 21) {
                    checkvaultnum = 12;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 12) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 12)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 12) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 22) {
                    checkvaultnum = 13;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 13) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 13)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 13) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 23) {
                    checkvaultnum = 14;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 14) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 14)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 14) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 24) {
                    checkvaultnum = 15;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 15) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 15)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 15) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 29) {
                    checkvaultnum = 16;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 16) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 16)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 16) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 30) {
                    checkvaultnum = 17;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 17) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 17)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 17) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 31) {
                    checkvaultnum = 18;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 18) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 18)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 18) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 32) {
                    checkvaultnum = 19;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 19) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 19)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 19) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 33) {
                    checkvaultnum = 20;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 20) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 20)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 20) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 38) {
                    checkvaultnum = 21;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 21) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 21)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 21) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 39) {
                    checkvaultnum = 22;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 22) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 22)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 22) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 40) {
                    checkvaultnum = 23;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 23) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 23)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 23) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 41) {
                    checkvaultnum = 24;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 24) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 24)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 24) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
                if (slot == 42) {
                    checkvaultnum = 25;
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 25) != null) {
                        player.openInventory(checkofflineinventoryFromBase64(SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 25)));
                    }
                    if (SQLiteConnect.InvCode(con, target.getUniqueId().toString(), 25) == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        OfflinePlayer target = offlinechecktarget;
        String invdata = PluginSet.inventoryToBase64(event.getInventory());
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 1 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 1);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 2 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 2);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 3 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 3);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 4 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 4);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 5 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 5);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 6 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 6);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 7 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 7);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 8 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 8);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 9 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 9);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 10 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 10);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 11 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 11);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 12 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 12);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 13 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 13);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 14 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 14);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 15 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 15);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 16 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 16);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 17 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 17);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 18 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 18);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 19 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 19);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 20 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 20);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 21 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 21);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 22 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 22);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 23 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 23);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 24 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 24);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库数据[ 25 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 25);
        }
    }
}
