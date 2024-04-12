package com.playerinv.Listener;


import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;


import static com.playerinv.Command.InvCommand.checktarget;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class CheckInvListener implements Listener {

    @EventHandler
    public void onCheckGUIClick(InventoryClickEvent event) throws Exception {
        if(event.getView().getTitle().contains("的主页")) {
            String prefix = PlayerInv.plugin.getConfig().getString("Prefix");
            Player player = (Player) event.getWhoClicked();
            Player target = checktarget;
            int slot = event.getRawSlot();
            if (event.getView().getTitle().contains("的主页")) {
                if (slot == 2) {
                    if (target.hasPermission("playerinv.inv.1") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 1;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 1) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 1)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 1) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 3) {
                    if (target.hasPermission("playerinv.inv.2") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 2;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 2) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 2)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 2) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 4) {
                    if (target.hasPermission("playerinv.inv.3") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 3;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 3) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 3)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 3) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 5) {
                    if (target.hasPermission("playerinv.inv.4") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 4;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 4) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 4)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 4) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 6) {
                    if (target.hasPermission("playerinv.inv.5") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 5;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 5) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 5)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 5) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 11) {
                    if (target.hasPermission("playerinv.inv.6") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 6;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 6) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 6)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 6) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 12) {
                    if (target.hasPermission("playerinv.inv.7") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 7;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 7) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 7)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 7) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 13) {
                    if (target.hasPermission("playerinv.inv.8") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 8;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 8) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 8)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 8) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 14) {
                    if (target.hasPermission("playerinv.inv.9") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 9;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 9) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 9)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 9) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 15) {
                    if (target.hasPermission("playerinv.inv.10") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 10;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 10) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 10)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 10) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 20) {
                    if (target.hasPermission("playerinv.inv.11") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 11;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 11) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 11)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 11) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 21) {
                    if (target.hasPermission("playerinv.inv.12") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 12;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 12) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 12)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 12) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 22) {
                    if (target.hasPermission("playerinv.inv.13") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 13;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 13) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 13)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 13) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 23) {
                    if (target.hasPermission("playerinv.inv.14") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 14;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 14) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 14)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 14) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 24) {
                    if (target.hasPermission("playerinv.inv.15") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 15;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 15) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 15)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 15) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 29) {
                    if (target.hasPermission("playerinv.inv.16") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 16;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 16) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 16)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 16) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 30) {
                    if (target.hasPermission("playerinv.inv.17") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 17;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 17) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 17)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 17) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 31) {
                    if (target.hasPermission("playerinv.inv.18") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 18;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 18) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 18)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 18) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 32) {
                    if (target.hasPermission("playerinv.inv.19") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 19;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 19) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 19)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 19) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 33) {
                    if (target.hasPermission("playerinv.inv.20") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 20;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 20) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 20)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 20) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 38) {
                    if (target.hasPermission("playerinv.inv.21") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 21;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 21) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 21)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 21) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 39) {
                    if (target.hasPermission("playerinv.inv.22") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 22;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 22) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 22)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 22) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 40) {
                    if (target.hasPermission("playerinv.inv.23") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 23;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 23) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 23)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 23) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 41) {
                    if (target.hasPermission("playerinv.inv.24") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 24;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 24) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 24)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 24) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
                if (slot == 42) {
                    if (target.hasPermission("playerinv.inv.25") || target.hasPermission("playerinv.inv.*") || target.hasPermission("playerinv.admin")) {
                        checkvaultnum = 25;
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 25) != null) {
                            player.openInventory(checkinventoryFromBase64(SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 25)));
                        }
                        if (SQLiteConnect.InvCode(con, target.getPlayer().getUniqueId().toString(), 25) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c该仓库尚未创建."));
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) throws Exception{
        Player player = (Player) event.getPlayer();
        Player target = checktarget;
        String invdata = PluginSet.inventoryToBase64(event.getInventory());
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 1 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 1);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 2 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 2);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 3 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 3);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 4 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 4);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 5 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 5);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 6 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 6);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 7 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 7);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 8 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 8);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 9 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 9);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 10 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 10);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 11 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 11);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 12 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 12);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 13 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 13);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 14 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 14);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 15 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 15);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 16 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 16);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 17 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 17);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 18 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 18);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 19 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 19);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 20 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 20);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 21 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 21);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 22 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 22);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 23 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 23);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 24 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 24);
        }
        if (event.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "的仓库[ 25 ]"))){
            SQLiteConnect.update(con, target.getUniqueId().toString(), invdata, 25);
        }
    }
}
