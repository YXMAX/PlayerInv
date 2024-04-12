package com.playerinv.MainGUI;

import com.playerinv.PlayerInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.playerinv.PlayerInv.plugin;


public class GUIManager{

    public static String limeglass = plugin.getConfig().getString("GUIItem.lime_glass.material");

    public static String orangeglass = plugin.getConfig().getString("GUIItem.orange_glass.material");

    public static String yellowglass = plugin.getConfig().getString("GUIItem.yellow_glass.material");

    public static String lockedchest = plugin.getConfig().getString("GUIItem.locked_chest.material");

    public static String unlockchest = plugin.getConfig().getString("GUIItem.unlock_chest.material");

    public static Boolean lgglow = plugin.getConfig().getBoolean("GUIItem.lime_glass.enchant_glow");

    public static Boolean ogglow = plugin.getConfig().getBoolean("GUIItem.orange_glass.enchant_glow");

    public static Boolean ygglow = plugin.getConfig().getBoolean("GUIItem.yellow_glass.enchant_glow");

    public static Boolean lcglow = plugin.getConfig().getBoolean("GUIItem.locked_chest.enchant_glow");

    public static Boolean ucglow = plugin.getConfig().getBoolean("GUIItem.unlock_chest.enchant_glow");

    public static Boolean modeldata = plugin.getConfig().getBoolean("CustomModelData.enabled");

    public static Integer limemodel = plugin.getConfig().getInt("CustomModelData.lime_glass");

    public static Integer orangemodel = plugin.getConfig().getInt("CustomModelData.orange_glass");

    public static Integer yellowmodel = plugin.getConfig().getInt("CustomModelData.yellow_glass");

    public static Integer lockmodel = plugin.getConfig().getInt("CustomModelData.locked_chest");

    public static Integer unlockmodel = plugin.getConfig().getInt("CustomModelData.unlock_chest");

    public static Inventory MainGUI(CommandSender commandSender) {
        String guititle = PlayerInv.plugin.getConfig().getString("GUI.Title");
        String lime = PlayerInv.plugin.getConfig().getString("GUIGlass.LIME");
        String orange = PlayerInv.plugin.getConfig().getString("GUIGlass.ORANGE");
        String yellow = PlayerInv.plugin.getConfig().getString("GUIGlass.YELLOW");
        String ltitle = PlayerInv.plugin.getConfig().getString("GUI.Large.Title");
        String mtitle = PlayerInv.plugin.getConfig().getString("GUI.Medium.Title");
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = PlayerInv.plugin.getConfig().getStringList("GUI.Large.Lore");
        Player player = (Player) commandSender;
        for(String s : list){
            lorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        ArrayList<String> mlorelist = new ArrayList<String>();
        List<String> mlist = PlayerInv.plugin.getConfig().getStringList("GUI.Medium.Lore");
        for(String s : mlist){
            mlorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        ArrayList<String> ulorelist = new ArrayList<String>();
        List<String> ulist = PlayerInv.plugin.getConfig().getStringList("GUI.Large.Lore");
        for(String s : ulist){
            ulorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        ArrayList<String> mulorelist = new ArrayList<String>();
        List<String> mulist = PlayerInv.plugin.getConfig().getStringList("GUI.Medium.Lore");
        for(String s : mulist){
            mulorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        String stat = PlayerInv.plugin.getConfig().getString("Status.Lock");
        String ustat = PlayerInv.plugin.getConfig().getString("Status.Unlock");
        lorelist.add(ChatColor.translateAlternateColorCodes('&', stat));
        mlorelist.add(ChatColor.translateAlternateColorCodes('&', stat));
        Inventory maingui = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',guititle));
        ItemStack inv1 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv1m = inv1.getItemMeta();
        inv1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a1&f]"));
        if(lcglow){
            inv1m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv1m.setCustomModelData(lockmodel);
        }
        inv1m.setLore(lorelist);
        inv1.setItemMeta(inv1m);
        ItemStack inv2 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv2m = inv2.getItemMeta();
        inv2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a2&f]"));
        if(lcglow){
            inv2m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv2m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv2m.setCustomModelData(lockmodel);
        }
        inv2m.setLore(lorelist);
        inv2.setItemMeta(inv2m);
        ItemStack inv3 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv3m = inv3.getItemMeta();
        inv3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a3&f]"));
        if(lcglow){
            inv3m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv3m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv3m.setCustomModelData(lockmodel);
        }
        inv3m.setLore(lorelist);
        inv3.setItemMeta(inv3m);
        ItemStack inv4 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv4m = inv4.getItemMeta();
        inv4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a4&f]"));
        if(lcglow){
            inv4m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv4m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv4m.setCustomModelData(lockmodel);
        }
        inv4m.setLore(lorelist);
        inv4.setItemMeta(inv4m);
        ItemStack inv5 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv5m = inv5.getItemMeta();
        inv5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a5&f]"));
        if(lcglow){
            inv5m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv5m.setCustomModelData(lockmodel);
        }
        inv5m.setLore(lorelist);
        inv5.setItemMeta(inv5m);
        ItemStack inv6 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv6m = inv6.getItemMeta();
        inv6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a6&f]"));
        if(lcglow){
            inv6m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv6m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv6m.setCustomModelData(lockmodel);
        }
        inv6m.setLore(lorelist);
        inv6.setItemMeta(inv1m);
        ItemStack inv7 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv7m = inv7.getItemMeta();
        inv7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a7&f]"));
        if(lcglow){
            inv7m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv7m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv7m.setCustomModelData(lockmodel);
        }
        inv7m.setLore(lorelist);
        inv7.setItemMeta(inv2m);
        ItemStack inv8 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv8m = inv8.getItemMeta();
        inv8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a8&f]"));
        if(lcglow){
            inv8m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv8m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv8m.setCustomModelData(lockmodel);
        }
        inv8m.setLore(lorelist);
        inv8.setItemMeta(inv8m);
        ItemStack inv9 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv9m = inv9.getItemMeta();
        inv9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a9&f]"));
        if(lcglow){
            inv9m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv9m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv9m.setCustomModelData(lockmodel);
        }
        inv9m.setLore(lorelist);
        inv9.setItemMeta(inv9m);
        ItemStack inv10 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv10m = inv10.getItemMeta();
        inv10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a10&f]"));
        if(lcglow){
            inv10m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv10m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv10m.setCustomModelData(lockmodel);
        }
        inv10m.setLore(lorelist);
        inv10.setItemMeta(inv10m);
        ItemStack inv11 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv11m = inv11.getItemMeta();
        inv11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a11&f]"));
        if(lcglow){
            inv11m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv11m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv11m.setCustomModelData(lockmodel);
        }
        inv11m.setLore(mlorelist);
        inv11.setItemMeta(inv11m);
        ItemStack inv12 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv12m = inv12.getItemMeta();
        inv12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a12&f]"));
        if(lcglow){
            inv12m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv12m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv12m.setCustomModelData(lockmodel);
        }
        inv12m.setLore(mlorelist);
        inv12.setItemMeta(inv12m);
        ItemStack inv13 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv13m = inv13.getItemMeta();
        inv13m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a13&f]"));
        if(lcglow){
            inv13m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv13m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv13m.setCustomModelData(lockmodel);
        }
        inv13m.setLore(mlorelist);
        inv13.setItemMeta(inv13m);
        ItemStack inv14 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv14m = inv14.getItemMeta();
        inv14m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a14&f]"));
        if(lcglow){
            inv14m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv14m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv14m.setCustomModelData(lockmodel);
        }
        inv14m.setLore(mlorelist);
        inv14.setItemMeta(inv14m);
        ItemStack inv15 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv15m = inv15.getItemMeta();
        inv15m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a15&f]"));
        if(lcglow){
            inv15m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv15m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv15m.setCustomModelData(lockmodel);
        }
        inv15m.setLore(mlorelist);
        inv15.setItemMeta(inv15m);
        ItemStack inv16 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv16m = inv16.getItemMeta();
        inv16m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a16&f]"));
        if(lcglow){
            inv16m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv16m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv16m.setCustomModelData(lockmodel);
        }
        inv16m.setLore(mlorelist);
        inv16.setItemMeta(inv16m);
        ItemStack inv17 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv17m = inv17.getItemMeta();
        inv17m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a17&f]"));
        if(lcglow){
            inv17m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv17m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv17m.setCustomModelData(lockmodel);
        }
        inv17m.setLore(mlorelist);
        inv17.setItemMeta(inv17m);
        ItemStack inv18 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv18m = inv18.getItemMeta();
        inv18m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a18&f]"));
        if(lcglow){
            inv18m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv18m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv18m.setCustomModelData(lockmodel);
        }
        inv18m.setLore(mlorelist);
        inv18.setItemMeta(inv18m);
        ItemStack inv19 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv19m = inv19.getItemMeta();
        inv19m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a19&f]"));
        if(lcglow){
            inv19m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv19m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv19m.setCustomModelData(lockmodel);
        }
        inv19m.setLore(mlorelist);
        inv19.setItemMeta(inv19m);
        ItemStack inv20 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv20m = inv20.getItemMeta();
        inv20m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a20&f]"));
        if(lcglow){
            inv20m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv20m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv20m.setCustomModelData(lockmodel);
        }
        inv20m.setLore(mlorelist);
        inv20.setItemMeta(inv20m);
        ItemStack inv21 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv21m = inv21.getItemMeta();
        inv21m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a21&f]"));
        if(lcglow){
            inv21m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv21m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv21m.setCustomModelData(lockmodel);
        }
        inv21m.setLore(mlorelist);
        inv21.setItemMeta(inv21m);
        ItemStack inv22 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv22m = inv22.getItemMeta();
        inv22m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a22&f]"));
        if(lcglow){
            inv22m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv22m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv22m.setCustomModelData(lockmodel);
        }
        inv22m.setLore(mlorelist);
        inv22.setItemMeta(inv22m);
        ItemStack inv23 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv23m = inv23.getItemMeta();
        inv23m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a23&f]"));
        if(lcglow){
            inv23m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv23m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv23m.setCustomModelData(lockmodel);
        }
        inv23m.setLore(mlorelist);
        inv23.setItemMeta(inv23m);
        ItemStack inv24 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv24m = inv24.getItemMeta();
        inv24m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a24&f]"));
        if(lcglow){
            inv24m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv24m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv24m.setCustomModelData(lockmodel);
        }
        inv24m.setLore(mlorelist);
        inv24.setItemMeta(inv24m);
        ItemStack inv25 = new ItemStack(Material.getMaterial(lockedchest), 1);
        ItemMeta inv25m = inv25.getItemMeta();
        inv25m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a25&f]"));
        if(lcglow){
            inv25m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            inv25m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            inv25m.setCustomModelData(lockmodel);
        }
        inv25m.setLore(mlorelist);
        inv25.setItemMeta(inv25m);


        ulorelist.add(ChatColor.translateAlternateColorCodes('&', ustat));
        mulorelist.add(ChatColor.translateAlternateColorCodes('&', ustat));
        if (player.hasPermission("playerinv.inv.1") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv1 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv1m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv1m.setCustomModelData(unlockmodel);
            }
            inv1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a1&f]"));
            inv1m.setLore(ulorelist);
            inv1.setItemMeta(inv1m);
        }
        if (player.hasPermission("playerinv.inv.2") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv2 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv2m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv2m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv2m.setCustomModelData(unlockmodel);
            }
            inv2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a2&f]"));
            inv2m.setLore(ulorelist);
            inv2.setItemMeta(inv2m);
        }
        if (player.hasPermission("playerinv.inv.3") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv3 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv3m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv3m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv3m.setCustomModelData(unlockmodel);
            }
            inv3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a3&f]"));
            inv3m.setLore(ulorelist);
            inv3.setItemMeta(inv3m);
        }
        if (player.hasPermission("playerinv.inv.4") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv4 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv4m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv4m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv4m.setCustomModelData(unlockmodel);
            }
            inv4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a4&f]"));
            inv4m.setLore(ulorelist);
            inv4.setItemMeta(inv4m);
        }
        if (player.hasPermission("playerinv.inv.5") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv5 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv5m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv5m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv5m.setCustomModelData(unlockmodel);
            }
            inv5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a5&f]"));
            inv5m.setLore(ulorelist);
            inv5.setItemMeta(inv5m);
        }
        if (player.hasPermission("playerinv.inv.6") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv6 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv6m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv6m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv6m.setCustomModelData(unlockmodel);
            }
            inv6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a6&f]"));
            inv6m.setLore(ulorelist);
            inv6.setItemMeta(inv6m);
        }
        if (player.hasPermission("playerinv.inv.7") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv7 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv7m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv7m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv7m.setCustomModelData(unlockmodel);
            }
            inv7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a7&f]"));
            inv7m.setLore(ulorelist);
            inv7.setItemMeta(inv7m);
        }
        if (player.hasPermission("playerinv.inv.8") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv8 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv8m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv8m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv8m.setCustomModelData(unlockmodel);
            }
            inv8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a8&f]"));
            inv8m.setLore(ulorelist);
            inv8.setItemMeta(inv8m);
        }
        if (player.hasPermission("playerinv.inv.9") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv9 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv9m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv9m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv9m.setCustomModelData(unlockmodel);
            }
            inv9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a9&f]"));
            inv9m.setLore(ulorelist);
            inv9.setItemMeta(inv9m);
        }
        if (player.hasPermission("playerinv.inv.10") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv10 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv10m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv10m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv10m.setCustomModelData(unlockmodel);
            }
            inv10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',ltitle + " &f[&a10&f]"));
            inv10m.setLore(ulorelist);
            inv10.setItemMeta(inv10m);
        }
        if (player.hasPermission("playerinv.inv.11") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv11 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv11m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv11m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv11m.setCustomModelData(unlockmodel);
            }
            inv11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a11&f]"));
            inv11m.setLore(mulorelist);
            inv11.setItemMeta(inv11m);
        }
        if (player.hasPermission("playerinv.inv.12") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv12 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv12m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv12m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv12m.setCustomModelData(unlockmodel);
            }
            inv12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a12&f]"));
            inv12m.setLore(mulorelist);
            inv12.setItemMeta(inv12m);
        }
        if (player.hasPermission("playerinv.inv.13") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv13 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv13m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv13m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv13m.setCustomModelData(unlockmodel);
            }
            inv13m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a13&f]"));
            inv13m.setLore(mulorelist);
            inv13.setItemMeta(inv13m);
        }
        if (player.hasPermission("playerinv.inv.14") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv14 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv14m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv14m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv14m.setCustomModelData(unlockmodel);
            }
            inv14m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a14&f]"));
            inv14m.setLore(mulorelist);
            inv14.setItemMeta(inv14m);
        }
        if (player.hasPermission("playerinv.inv.15") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv15 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv15m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv15m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv15m.setCustomModelData(unlockmodel);
            }
            inv15m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a15&f]"));
            inv15m.setLore(mulorelist);
            inv15.setItemMeta(inv15m);
        }
        if (player.hasPermission("playerinv.inv.16") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv16 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv16m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv16m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv16m.setCustomModelData(unlockmodel);
            }
            inv16m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a16&f]"));
            inv16m.setLore(mulorelist);
            inv16.setItemMeta(inv16m);
        }
        if (player.hasPermission("playerinv.inv.17") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv17 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv17m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv17m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv17m.setCustomModelData(unlockmodel);
            }
            inv17m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a17&f]"));
            inv17m.setLore(mulorelist);
            inv17.setItemMeta(inv17m);
        }
        if (player.hasPermission("playerinv.inv.18") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv18 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv18m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv18m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv18m.setCustomModelData(unlockmodel);
            }
            inv18m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a18&f]"));
            inv18m.setLore(mulorelist);
            inv18.setItemMeta(inv18m);
        }
        if (player.hasPermission("playerinv.inv.19") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv19 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv19m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv19m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv19m.setCustomModelData(unlockmodel);
            }
            inv19m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a19&f]"));
            inv19m.setLore(mulorelist);
            inv19.setItemMeta(inv19m);
        }
        if (player.hasPermission("playerinv.inv.20") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv20 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv20m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv20m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv20m.setCustomModelData(unlockmodel);
            }
            inv20m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a20&f]"));
            inv20m.setLore(mulorelist);
            inv20.setItemMeta(inv20m);
        }
        if (player.hasPermission("playerinv.inv.21") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv21 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv21m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv21m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv21m.setCustomModelData(unlockmodel);
            }
            inv21m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a21&f]"));
            inv21m.setLore(mulorelist);
            inv21.setItemMeta(inv21m);
        }
        if (player.hasPermission("playerinv.inv.22") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv22 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv22m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv22m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv22m.setCustomModelData(unlockmodel);
            }
            inv22m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a22&f]"));
            inv22m.setLore(mulorelist);
            inv22.setItemMeta(inv22m);
        }
        if (player.hasPermission("playerinv.inv.23") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv23 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv23m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv23m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv23m.setCustomModelData(unlockmodel);
            }
            inv23m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a23&f]"));
            inv23m.setLore(mulorelist);
            inv23.setItemMeta(inv23m);
        }
        if (player.hasPermission("playerinv.inv.24") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv24 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv24m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv24m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv24m.setCustomModelData(unlockmodel);
            }
            inv24m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a24&f]"));
            inv24m.setLore(mulorelist);
            inv24.setItemMeta(inv24m);
        }
        if (player.hasPermission("playerinv.inv.25") || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin")) {
            inv25 = new ItemStack(Material.getMaterial(unlockchest), 1);
            if(ucglow){
                inv25m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                inv25m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(modeldata){
                inv25m.setCustomModelData(unlockmodel);
            }
            inv25m.setDisplayName(ChatColor.translateAlternateColorCodes('&',mtitle + " &f[&a25&f]"));
            inv25m.setLore(mulorelist);
            inv25.setItemMeta(inv25m);
        }

        maingui.setItem(2, inv1);
        maingui.setItem(3, inv2);
        maingui.setItem(4, inv3);
        maingui.setItem(5, inv4);
        maingui.setItem(6, inv5);
        maingui.setItem(11, inv6);
        maingui.setItem(12, inv7);
        maingui.setItem(13, inv8);
        maingui.setItem(14, inv9);
        maingui.setItem(15, inv10);
        maingui.setItem(29, inv11);
        maingui.setItem(30, inv12);
        maingui.setItem(31, inv13);
        maingui.setItem(32, inv14);
        maingui.setItem(33, inv15);
        maingui.setItem(38, inv16);
        maingui.setItem(39, inv17);
        maingui.setItem(40, inv18);
        maingui.setItem(41, inv19);
        maingui.setItem(42, inv20);
        maingui.setItem(47, inv21);
        maingui.setItem(48, inv22);
        maingui.setItem(49, inv23);
        maingui.setItem(50, inv24);
        maingui.setItem(51, inv25);

        ItemStack glass1 = new ItemStack(Material.getMaterial(yellowglass), 1);
        ItemMeta glass1m = glass1.getItemMeta();
        glass1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',yellow));
        if(ygglow){
            glass1m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            glass1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            glass1m.setCustomModelData(yellowmodel);
        }
        glass1.setItemMeta(glass1m);
        ItemStack glass2 = new ItemStack(Material.getMaterial(yellowglass), 1);
        ItemMeta glass2m = glass2.getItemMeta();
        glass2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',yellow));
        if(ygglow){
            glass2m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            glass2m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            glass2m.setCustomModelData(yellowmodel);
        }
        glass2.setItemMeta(glass2m);
        ItemStack glass3 = new ItemStack(Material.getMaterial(yellowglass), 1);
        ItemMeta glass3m = glass3.getItemMeta();
        glass3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',yellow));
        if(ygglow){
            glass3m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            glass3m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            glass3m.setCustomModelData(yellowmodel);
        }
        glass3.setItemMeta(glass3m);
        ItemStack glass4 = new ItemStack(Material.getMaterial(yellowglass), 1);
        ItemMeta glass4m = glass4.getItemMeta();
        glass4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',yellow));
        if(ygglow){
            glass4m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            glass4m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            glass4m.setCustomModelData(yellowmodel);
        }
        glass4.setItemMeta(glass4m);
        ItemStack glass5 = new ItemStack(Material.getMaterial(yellowglass), 1);
        ItemMeta glass5m = glass5.getItemMeta();
        glass5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',yellow));
        if(ygglow){
            glass5m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            glass5m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            glass5m.setCustomModelData(yellowmodel);
        }
        glass5.setItemMeta(glass5m);
        maingui.setItem(20, glass1);
        maingui.setItem(21, glass2);
        maingui.setItem(22, glass3);
        maingui.setItem(23, glass4);
        maingui.setItem(24, glass5);

        ItemStack g1 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g1m = g1.getItemMeta();
        g1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g1m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g1m.setCustomModelData(limemodel);
        }
        g1.setItemMeta(g1m);
        ItemStack g2 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g2m = g2.getItemMeta();
        g2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g2m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g2m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g2m.setCustomModelData(limemodel);
        }
        g2.setItemMeta(g2m);
        ItemStack g3 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g3m = g3.getItemMeta();
        g3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g3m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g3m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g3m.setCustomModelData(limemodel);
        }
        g3.setItemMeta(g3m);
        ItemStack g4 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g4m = g4.getItemMeta();
        g4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g4m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g4m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g4m.setCustomModelData(limemodel);
        }
        g4.setItemMeta(g4m);
        ItemStack g5 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g5m = g5.getItemMeta();
        g5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g5m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g5m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g5m.setCustomModelData(limemodel);
        }
        g5.setItemMeta(g5m);
        ItemStack g6 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g6m = g6.getItemMeta();
        g6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g6m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g6m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g6m.setCustomModelData(limemodel);
        }
        g6.setItemMeta(g6m);
        ItemStack g7 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g7m = g7.getItemMeta();
        g7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g7m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g7m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g7m.setCustomModelData(limemodel);
        }
        g7.setItemMeta(g7m);
        ItemStack g8 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g8m = g8.getItemMeta();
        g8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g8m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g8m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g8m.setCustomModelData(limemodel);
        }
        g8.setItemMeta(g8m);
        ItemStack g9 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g9m = g9.getItemMeta();
        g9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g9m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g9m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g9m.setCustomModelData(limemodel);
        }
        g9.setItemMeta(g9m);
        ItemStack g10 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g10m = g10.getItemMeta();
        g10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g10m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g10m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g10m.setCustomModelData(limemodel);
        }
        g10.setItemMeta(g10m);
        ItemStack g11 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g11m = g11.getItemMeta();
        g11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g11m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g11m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g11m.setCustomModelData(limemodel);
        }
        g11.setItemMeta(g11m);
        ItemStack g12 = new ItemStack(Material.getMaterial(limeglass), 1);
        ItemMeta g12m = g12.getItemMeta();
        g12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',lime));
        if(lgglow){
            g12m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            g12m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            g12m.setCustomModelData(limemodel);
        }
        g12.setItemMeta(g12m);

        maingui.setItem(0, g1);
        maingui.setItem(8, g2);
        maingui.setItem(10, g3);
        maingui.setItem(16, g4);
        maingui.setItem(18, g5);
        maingui.setItem(26, g6);
        maingui.setItem(28, g7);
        maingui.setItem(34, g8);
        maingui.setItem(36, g9);
        maingui.setItem(44, g10);
        maingui.setItem(46, g11);
        maingui.setItem(52, g12);

        ItemStack sg1 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg1m = sg1.getItemMeta();
        sg1m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg1m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg1m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg1m.setCustomModelData(orangemodel);
        }
        sg1.setItemMeta(sg1m);
        ItemStack sg2 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg2m = sg2.getItemMeta();
        sg2m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg2m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg2m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg2m.setCustomModelData(orangemodel);
        }
        sg2.setItemMeta(sg2m);
        ItemStack sg3 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg3m = sg3.getItemMeta();
        sg3m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg3m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg3m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg3m.setCustomModelData(orangemodel);
        }
        sg3.setItemMeta(sg3m);
        ItemStack sg4 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg4m = sg4.getItemMeta();
        sg4m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg4m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg4m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg4m.setCustomModelData(orangemodel);
        }
        sg4.setItemMeta(sg4m);
        ItemStack sg5 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg5m = sg5.getItemMeta();
        sg5m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg5m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg5m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg5m.setCustomModelData(orangemodel);
        }
        sg5.setItemMeta(sg5m);
        ItemStack sg6 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg6m = sg6.getItemMeta();
        sg6m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg6m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg6m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg6m.setCustomModelData(orangemodel);
        }
        sg6.setItemMeta(sg6m);
        ItemStack sg7 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg7m = sg7.getItemMeta();
        sg7m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg7m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg7m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg7m.setCustomModelData(orangemodel);
        }
        sg7.setItemMeta(sg7m);
        ItemStack sg8 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg8m = sg8.getItemMeta();
        sg8m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg8m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg8m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg8m.setCustomModelData(orangemodel);
        }
        sg8.setItemMeta(sg8m);
        ItemStack sg9 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg9m = sg9.getItemMeta();
        sg9m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg9m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg9m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg9m.setCustomModelData(orangemodel);
        }
        sg9.setItemMeta(sg9m);
        ItemStack sg10 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg10m = sg10.getItemMeta();
        sg10m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg10m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg10m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg10m.setCustomModelData(orangemodel);
        }
        sg10.setItemMeta(sg10m);
        ItemStack sg11 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg11m = sg11.getItemMeta();
        sg11m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg11m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg11m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg11m.setCustomModelData(orangemodel);
        }
        sg11.setItemMeta(sg11m);
        ItemStack sg12 = new ItemStack(Material.getMaterial(orangeglass), 1);
        ItemMeta sg12m = sg12.getItemMeta();
        sg12m.setDisplayName(ChatColor.translateAlternateColorCodes('&',orange));
        if(ogglow){
            sg12m.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
            sg12m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(modeldata){
            sg12m.setCustomModelData(orangemodel);
        }
        sg12.setItemMeta(sg12m);

        maingui.setItem(1, sg1);
        maingui.setItem(7, sg2);
        maingui.setItem(9, sg3);
        maingui.setItem(17, sg4);
        maingui.setItem(19, sg5);
        maingui.setItem(25, sg6);
        maingui.setItem(27, sg7);
        maingui.setItem(35, sg8);
        maingui.setItem(37, sg9);
        maingui.setItem(43, sg10);
        maingui.setItem(45, sg11);
        maingui.setItem(53, sg12);
        return maingui;

    }
}
