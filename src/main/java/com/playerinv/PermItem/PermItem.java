package com.playerinv.PermItem;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.playerinv.LocaleUtil.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;

public class PermItem {

    public static void linvitem(Player player){
        if(player.getInventory().firstEmpty() != -1){
            player.getInventory().addItem(largeitem_owner(player));
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(),largeitem_owner(player));
        }
    }

    public static void sinvitem(Player player){
        if(player.getInventory().firstEmpty() != -1){
            player.getInventory().addItem(mediumitem_owner(player));
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(),mediumitem_owner(player));
        }
    }

    public static ItemStack largeitem_owner(Player player){
        ItemStack litem = new ItemStack(Material.getMaterial(voucher_owner_large_material()));
        ItemMeta lim = litem.getItemMeta();
        if(voucher_owner_large_enchant_glow()){
            lim.addEnchant(Enchantment.ARROW_FIRE, 1, false);
            lim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(plugin.getConfig().contains("Voucher.large.custom-model-data")){
            if(!is113 && !isBelow113){
                lim.setCustomModelData(voucher_owner_large_custom_data());
            }
        }
        lim.setDisplayName(color(Locale_Voucher_Large_DisplayName()));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = Locale_Voucher_Large_Lore();
        for(String s : list){
            lorelist.add(color(s));
        }
        lorelist.add(color(""));
        lorelist.add(color( "&bOwner: &e" + player.getName()));
        lim.setLore(lorelist);
        litem.setItemMeta(lim);
        return litem;
    }

    public static ItemStack mediumitem_owner(Player player){
        ItemStack sitem = new ItemStack(Material.getMaterial(voucher_owner_medium_material()));
        ItemMeta sim = sitem.getItemMeta();
        if(voucher_owner_medium_enchant_glow()){
            sim.addEnchant(Enchantment.ARROW_FIRE, 1, false);
            sim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(plugin.getConfig().contains("Voucher.medium.custom-model-data")){
            if(!is113 && !isBelow113){
                sim.setCustomModelData(voucher_owner_medium_custom_data());
            }
        }
        sim.setDisplayName(color(Locale_Voucher_Medium_DisplayName()));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = Locale_Voucher_Medium_Lore();
        for(String s : list){
            lorelist.add(color(s));
        }
        lorelist.add(color(""));
        lorelist.add(color("&bOwner: &e" + player.getName()));
        sim.setLore(lorelist);
        sitem.setItemMeta(sim);
        return sitem;
    }
}
