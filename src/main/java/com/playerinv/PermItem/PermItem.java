package com.playerinv.PermItem;

import com.playerinv.PlayerInv;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PermItem {

    public static void linvitem(Player player){
        player.getInventory().addItem(largeitem(player));
    }

    public static void sinvitem(Player player){
        player.getInventory().addItem(mediumitem(player));
    }

    public static ItemStack largeitem(Player player){
        String linvtitle = PlayerInv.plugin.getConfig().getString("Item.Large.Title");
        ItemStack litem = new ItemStack(Material.PAPER);
        ItemMeta lim = litem.getItemMeta();
        lim.addEnchant(Enchantment.ARROW_FIRE, 1, false);
        lim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lim.setDisplayName(ChatColor.translateAlternateColorCodes('&', linvtitle));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = PlayerInv.plugin.getConfig().getStringList("Item.Large.Lore");
        for(String s : list){
            lorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        lorelist.add(ChatColor.translateAlternateColorCodes('&', ""));
        lorelist.add(ChatColor.translateAlternateColorCodes('&', "&bOwner: &e" + player.getName()));
        lim.setLore(lorelist);
        litem.setItemMeta(lim);
        return litem;
    }

    public static ItemStack mediumitem(Player player){
        String sinvtitle = PlayerInv.plugin.getConfig().getString("Item.Medium.Title");
        ItemStack sitem = new ItemStack(Material.PAPER);
        ItemMeta sim = sitem.getItemMeta();
        sim.addEnchant(Enchantment.ARROW_FIRE, 1, false);
        sim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        sim.setDisplayName(ChatColor.translateAlternateColorCodes('&', sinvtitle));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = PlayerInv.plugin.getConfig().getStringList("Item.Medium.Lore");
        for(String s : list){
            lorelist.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        lorelist.add(ChatColor.translateAlternateColorCodes('&', ""));
        lorelist.add(ChatColor.translateAlternateColorCodes('&', "&bOwner: &e" + player.getName()));
        sim.setLore(lorelist);
        sitem.setItemMeta(sim);
        return sitem;
    }
}
