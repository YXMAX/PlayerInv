package com.playerinv.MainGUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.LinkedList;
import java.util.List;

import static com.playerinv.Command.InvCommand.checktarget;

public class GUICheck {

    public static Inventory CheckGUI(CommandSender commandSender){
        Player player = checktarget;
        Inventory checkgui = Bukkit.createInventory(null, 45, player.getName() + " ����ҳ");
        List<String> lore = new LinkedList<>();
        lore.add("��");
        lore.add("��b�����");
        if (player.hasPermission("playerinv.inv.1") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv1 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv1m = inv1.getItemMeta();
            inv1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a1&f]"));
            inv1m.setLore(lore);
            inv1.setItemMeta(inv1m);
            checkgui.setItem(2, inv1);
        } else if(!(player.hasPermission("playerinv.inv.1") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv1 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv1m = inv1.getItemMeta();
            inv1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv1.setItemMeta(inv1m);
            checkgui.setItem(2, inv1);
        }
        if (player.hasPermission("playerinv.inv.2") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv2 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv2m = inv2.getItemMeta();
            inv2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a2&f]"));
            inv2m.setLore(lore);
            inv2.setItemMeta(inv2m);
            checkgui.setItem(3, inv2);
        } else if(!(player.hasPermission("playerinv.inv.2") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv2 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv2m = inv2.getItemMeta();
            inv2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv2.setItemMeta(inv2m);
            checkgui.setItem(3, inv2);
        }
        if (player.hasPermission("playerinv.inv.3") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv3 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv3m = inv3.getItemMeta();
            inv3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a3&f]"));
            inv3m.setLore(lore);
            inv3.setItemMeta(inv3m);
            checkgui.setItem(4, inv3);
        } else if(!(player.hasPermission("playerinv.inv.3") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv3 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv3m = inv3.getItemMeta();
            inv3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv3.setItemMeta(inv3m);
            checkgui.setItem(4, inv3);
        }
        if (player.hasPermission("playerinv.inv.4") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv4 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv4m = inv4.getItemMeta();
            inv4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a4&f]"));
            inv4m.setLore(lore);
            inv4.setItemMeta(inv4m);
            checkgui.setItem(5, inv4);
        } else if(!(player.hasPermission("playerinv.inv.4") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv4 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv4m = inv4.getItemMeta();
            inv4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv4.setItemMeta(inv4m);
            checkgui.setItem(5, inv4);
        }
        if (player.hasPermission("playerinv.inv.5") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv5 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv5m = inv5.getItemMeta();
            inv5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a5&f]"));
            inv5m.setLore(lore);
            inv5.setItemMeta(inv5m);
            checkgui.setItem(6, inv5);
        } else if(!(player.hasPermission("playerinv.inv.5") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv5 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv5m = inv5.getItemMeta();
            inv5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv5.setItemMeta(inv5m);
            checkgui.setItem(6, inv5);
        }
        if (player.hasPermission("playerinv.inv.6") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv6 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv6m = inv6.getItemMeta();
            inv6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a6&f]"));
            inv6m.setLore(lore);
            inv6.setItemMeta(inv6m);
            checkgui.setItem(11, inv6);
        } else if(!(player.hasPermission("playerinv.inv.6") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv6 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv6m = inv6.getItemMeta();
            inv6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv6.setItemMeta(inv6m);
            checkgui.setItem(11, inv6);
        }
        if (player.hasPermission("playerinv.inv.7") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv7 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv7m = inv7.getItemMeta();
            inv7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a7&f]"));
            inv7m.setLore(lore);
            inv7.setItemMeta(inv7m);
            checkgui.setItem(12, inv7);
        } else if(!(player.hasPermission("playerinv.inv.7") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv7 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv7m = inv7.getItemMeta();
            inv7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv7.setItemMeta(inv7m);
            checkgui.setItem(12, inv7);
        }
        if (player.hasPermission("playerinv.inv.8") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv8 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv8m = inv8.getItemMeta();
            inv8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a8&f]"));
            inv8m.setLore(lore);
            inv8.setItemMeta(inv8m);
            checkgui.setItem(13, inv8);
        } else if(!(player.hasPermission("playerinv.inv.8") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv8 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv8m = inv8.getItemMeta();
            inv8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv8.setItemMeta(inv8m);
            checkgui.setItem(13, inv8);
        }
        if (player.hasPermission("playerinv.inv.9") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv9 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv9m = inv9.getItemMeta();
            inv9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a9&f]"));
            inv9m.setLore(lore);
            inv9.setItemMeta(inv9m);
            checkgui.setItem(14, inv9);
        } else if(!(player.hasPermission("playerinv.inv.9") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv9 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv9m = inv9.getItemMeta();
            inv9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv9.setItemMeta(inv9m);
            checkgui.setItem(14, inv9);
        }
        if (player.hasPermission("playerinv.inv.10") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv10 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv10m = inv10.getItemMeta();
            inv10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a10&f]"));
            inv10m.setLore(lore);
            inv10.setItemMeta(inv10m);
            checkgui.setItem(15, inv10);
        } else if(!(player.hasPermission("playerinv.inv.10") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv10 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv10m = inv10.getItemMeta();
            inv10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv10.setItemMeta(inv10m);
            checkgui.setItem(15, inv10);
        }
        if (player.hasPermission("playerinv.inv.11") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv11 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv11m = inv11.getItemMeta();
            inv11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a11&f]"));
            inv11m.setLore(lore);
            inv11.setItemMeta(inv11m);
            checkgui.setItem(20, inv11);
        } else if(!(player.hasPermission("playerinv.inv.11") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv11 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv11m = inv11.getItemMeta();
            inv11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv11.setItemMeta(inv11m);
            checkgui.setItem(20, inv11);
        }
        if (player.hasPermission("playerinv.inv.12") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv12 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv12m = inv12.getItemMeta();
            inv12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a12&f]"));
            inv12m.setLore(lore);
            inv12.setItemMeta(inv12m);
            checkgui.setItem(21, inv12);
        } else if(!(player.hasPermission("playerinv.inv.12") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv12 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv12m = inv12.getItemMeta();
            inv12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv12.setItemMeta(inv12m);
            checkgui.setItem(21, inv12);
        }
        if (player.hasPermission("playerinv.inv.13") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv13 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv13m = inv13.getItemMeta();
            inv13m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a13&f]"));
            inv13m.setLore(lore);
            inv13.setItemMeta(inv13m);
            checkgui.setItem(22, inv13);
        } else if(!(player.hasPermission("playerinv.inv.13") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv13 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv13m = inv13.getItemMeta();
            inv13m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv13.setItemMeta(inv13m);
            checkgui.setItem(22, inv13);
        }
        if (player.hasPermission("playerinv.inv.14") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv14 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv14m = inv14.getItemMeta();
            inv14m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a14&f]"));
            inv14m.setLore(lore);
            inv14.setItemMeta(inv14m);
            checkgui.setItem(23, inv14);
        } else if(!(player.hasPermission("playerinv.inv.14") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv14 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv14m = inv14.getItemMeta();
            inv14m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv14.setItemMeta(inv14m);
            checkgui.setItem(23, inv14);
        }
        if (player.hasPermission("playerinv.inv.15") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv15 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv15m = inv15.getItemMeta();
            inv15m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a15&f]"));
            inv15m.setLore(lore);
            inv15.setItemMeta(inv15m);
            checkgui.setItem(24, inv15);
        } else if(!(player.hasPermission("playerinv.inv.15") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv15 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv15m = inv15.getItemMeta();
            inv15m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv15.setItemMeta(inv15m);
            checkgui.setItem(24, inv15);
        }
        if (player.hasPermission("playerinv.inv.16") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv16 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv16m = inv16.getItemMeta();
            inv16m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a16&f]"));
            inv16m.setLore(lore);
            inv16.setItemMeta(inv16m);
            checkgui.setItem(29, inv16);
        } else if(!(player.hasPermission("playerinv.inv.16") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv16 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv16m = inv16.getItemMeta();
            inv16m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv16.setItemMeta(inv16m);
            checkgui.setItem(29, inv16);
        }
        if (player.hasPermission("playerinv.inv.17") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv17 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv17m = inv17.getItemMeta();
            inv17m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a17&f]"));
            inv17m.setLore(lore);
            inv17.setItemMeta(inv17m);
            checkgui.setItem(30, inv17);
        } else if(!(player.hasPermission("playerinv.inv.17") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv17 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv17m = inv17.getItemMeta();
            inv17m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv17.setItemMeta(inv17m);
            checkgui.setItem(30, inv17);
        }
        if (player.hasPermission("playerinv.inv.18") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv18 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv18m = inv18.getItemMeta();
            inv18m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a18&f]"));
            inv18m.setLore(lore);
            inv18.setItemMeta(inv18m);
            checkgui.setItem(31, inv18);
        } else if(!(player.hasPermission("playerinv.inv.18") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv18 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv18m = inv18.getItemMeta();
            inv18m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv18.setItemMeta(inv18m);
            checkgui.setItem(31, inv18);
        }
        if (player.hasPermission("playerinv.inv.19") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv19 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv19m = inv19.getItemMeta();
            inv19m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a19&f]"));
            inv19m.setLore(lore);
            inv19.setItemMeta(inv19m);
            checkgui.setItem(32, inv19);
        } else if(!(player.hasPermission("playerinv.inv.19") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv19 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv19m = inv19.getItemMeta();
            inv19m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv19.setItemMeta(inv19m);
            checkgui.setItem(32, inv19);
        }
        if (player.hasPermission("playerinv.inv.20") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv20 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv20m = inv20.getItemMeta();
            inv20m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a20&f]"));
            inv20m.setLore(lore);
            inv20.setItemMeta(inv20m);
            checkgui.setItem(33, inv20);
        } else if(!(player.hasPermission("playerinv.inv.20") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv20 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv20m = inv20.getItemMeta();
            inv20m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv20.setItemMeta(inv20m);
            checkgui.setItem(33, inv20);
        }
        if (player.hasPermission("playerinv.inv.21") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv21 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv21m = inv21.getItemMeta();
            inv21m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a21&f]"));
            inv21m.setLore(lore);
            inv21.setItemMeta(inv21m);
            checkgui.setItem(38, inv21);
        } else if(!(player.hasPermission("playerinv.inv.21") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv21 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv21m = inv21.getItemMeta();
            inv21m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv21.setItemMeta(inv21m);
            checkgui.setItem(38, inv21);
        }
        if (player.hasPermission("playerinv.inv.22") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv22 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv22m = inv22.getItemMeta();
            inv22m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a22&f]"));
            inv22m.setLore(lore);
            inv22.setItemMeta(inv22m);
            checkgui.setItem(39, inv22);
        } else if(!(player.hasPermission("playerinv.inv.22") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv22 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv22m = inv22.getItemMeta();
            inv22m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv22.setItemMeta(inv22m);
            checkgui.setItem(39, inv22);
        }
        if (player.hasPermission("playerinv.inv.23") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv23 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv23m = inv23.getItemMeta();
            inv23m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a23&f]"));
            inv23m.setLore(lore);
            inv23.setItemMeta(inv23m);
            checkgui.setItem(40, inv23);
        } else if(!(player.hasPermission("playerinv.inv.23") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv23 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv23m = inv23.getItemMeta();
            inv23m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv23.setItemMeta(inv23m);
            checkgui.setItem(40, inv23);
        }
        if (player.hasPermission("playerinv.inv.24") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv24 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv24m = inv24.getItemMeta();
            inv24m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a24&f]"));
            inv24m.setLore(lore);
            inv24.setItemMeta(inv24m);
            checkgui.setItem(41, inv24);
        } else if(!(player.hasPermission("playerinv.inv.24") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv24 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv24m = inv24.getItemMeta();
            inv24m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv24.setItemMeta(inv24m);
            checkgui.setItem(41, inv24);
        }
        if (player.hasPermission("playerinv.inv.25") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            ItemStack inv25 = new ItemStack(Material.CHEST, 1);
            ItemMeta inv25m = inv25.getItemMeta();
            inv25m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a�ֿ� &f[&a25&f]"));
            inv25m.setLore(lore);
            inv25.setItemMeta(inv25m);
            checkgui.setItem(42, inv25);
        } else if(!(player.hasPermission("playerinv.inv.25") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin"))){
            ItemStack inv25 = new ItemStack(Material.BARRIER,1);
            ItemMeta inv25m = inv25.getItemMeta();
            inv25m.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lδӵ��"));
            inv25.setItemMeta(inv25m);
            checkgui.setItem(42, inv25);
        }

        return checkgui;
    }
}
