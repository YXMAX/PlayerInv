package com.playerinv.MainGUI;

import com.playerinv.InvHolder.Check_MainMenuHolder;
import com.playerinv.InvHolder.Check_MainMenuHolder_Offline;
import com.playerinv.InvHolder.MainMenuHolder;
import com.playerinv.PlayerInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.playerinv.MainGUI.MenuItemException.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;

public class MainMenu {

    public static Inventory Main_GUI(Player player) {
        int size = MainMenuConfig.getInt("size");
        String title = MainMenuConfig.getString("menu_title");
        Inventory main = Bukkit.createInventory(new MainMenuHolder(), size, color(title));
        for (String vault : MainMenuConfig.getStringList("vault_items.large_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            MainMenuVaultSlotMap_Large.putIfAbsent(slot,vault_num);
            String path = null;
            if(vault_num <= 10){
                if (player.hasPermission("playerinv.large.inv." + vault_num) || player.isOp() || player.hasPermission(LargeFullInv)) {
                    path = "vault_items.large_unlocked.";
                } else if (player.hasPermission("playerinv.inv." + vault_num) || player.isOp()){
                    path = "vault_items.large_unlocked.";
                } else {
                    path = "vault_items.large_locked.";
                }
            }
            if(vault_num > 10){
                if (player.hasPermission("playerinv.large.inv." + vault_num) || player.isOp() || player.hasPermission(LargeFullInv)) {
                    path = "vault_items.large_unlocked.";
                } else {
                    path = "vault_items.large_locked.";
                }
            }
            if (MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.MainMenu, MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        if(hasPAPI){
                            lore = PlaceholderAPI.setPlaceholders(player,lore);
                        }
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (MainMenuConfig.contains(path + "item-subid")) {
                            int id = MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (MainMenuConfig.contains(path + "enchant-glow")) {
                        if (MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String vault : MainMenuConfig.getStringList("vault_items.medium_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            MainMenuVaultSlotMap_Medium.putIfAbsent(slot,vault_num);
            String path = null;
            int old_num = vault_num + 10;
            if(vault_num <= 15){
                if (player.hasPermission("playerinv.medium.inv." + vault_num) || player.isOp() || player.hasPermission(MediumFullInv)) {
                    path = "vault_items.medium_unlocked.";
                } else if (player.hasPermission("playerinv.inv." + old_num) || player.isOp()){
                    path = "vault_items.medium_unlocked.";
                } else {
                    path = "vault_items.medium_locked.";
                }
            }
            if(vault_num > 15){
                if (player.hasPermission("playerinv.medium.inv." + vault_num) || player.isOp() || player.hasPermission(MediumFullInv)) {
                    path = "vault_items.medium_unlocked.";
                } else {
                    path = "vault_items.medium_locked.";
                }
            }
            if (MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.MainMenu, MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        if(hasPAPI){
                            lore = PlaceholderAPI.setPlaceholders(player,lore);
                        }
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (MainMenuConfig.contains(path + "item-subid")) {
                            int id = MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (MainMenuConfig.contains(path + "enchant-glow")) {
                        if (MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String path : MainMenuConfig.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (MainMenuConfig.contains(path_prefix + "slot")) {
                int slot = MainMenuConfig.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = MainMenuConfig.getString(path_prefix + "display-name");
                List<String> lore = MainMenuConfig.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MainMenuConfig.getInt(path_prefix + "amount") != 0) {
                    amount = MainMenuConfig.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (MainMenuConfig.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(MainMenuConfig.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!MainMenuConfig.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                MainMenuConfig.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    MainMenuConfig.save(MainMenu);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(MainMenuConfig.getString(path_prefix + "material")) == null) {
                        invalidMaterial(PlayerInv.MainMenu, MainMenuConfig.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MainMenuConfig.getString(path_prefix + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        if(hasPAPI){
                            lore = PlaceholderAPI.setPlaceholders(player,lore);
                        }
                        for (String s : lore) {
                            lore_list.add(color(s));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if (MainMenuConfig.contains(path_prefix + "enchant-glow")) {
                        if (MainMenuConfig.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if(isBelow113){
                        if (MainMenuConfig.contains(path_prefix + "item-subid")) {
                            int id = MainMenuConfig.getInt(path_prefix + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (MainMenuConfig.contains(path_prefix + "close-menu")) {
                        if(MainMenuConfig.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (MainMenuConfig.getBoolean(path_prefix + "close-menu")) {
                            MainMenuItemMap.put(slot, "close");
                        }
                    }
                    if(!is113 && !isBelow113){
                        if(MainMenuConfig.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(MainMenuConfig.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(MainMenuConfig.contains(path_prefix + "open-menu")){
                        String menu = MainMenuConfig.getString(path_prefix + "open-menu");
                        if(VaultMenuMap.containsKey(menu)){
                            MainMenuItemMap.put(slot,menu);
                        } else {
                            invalidMenu(path);
                            continue;
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            } else {
                invalidSlot(path);
            }
        }
        return main;
    }

    public static Inventory Check_Main_GUI(Player player) {
        int size = Check_MainMenuConfig.getInt("size");
        String title = Check_MainMenuConfig.getString("menu_title");
        Inventory main = Bukkit.createInventory(new Check_MainMenuHolder(), size, color(title));
        for (String vault : Check_MainMenuConfig.getStringList("vault_items.large_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_MainMenuVaultSlotMap_Large.putIfAbsent(slot,vault_num);
            String path = null;
            if(vault_num <= 10){
                if (player.hasPermission("playerinv.large.inv." + vault_num) || player.isOp() || player.hasPermission(LargeFullInv)) {
                    path = "vault_items.large_unlocked.";
                } else if (player.hasPermission("playerinv.inv." + vault_num) || player.isOp()){
                    path = "vault_items.large_unlocked.";
                } else {
                    path = "vault_items.large_locked.";
                }
            }
            if(vault_num > 10){
                if (player.hasPermission("playerinv.large.inv." + vault_num) || player.isOp() || player.hasPermission(LargeFullInv)) {
                    path = "vault_items.large_unlocked.";
                } else {
                    path = "vault_items.large_locked.";
                }
            }
            if (Check_MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = Check_MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (!lore.isEmpty()) {
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (Check_MainMenuConfig.contains(path + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String vault : Check_MainMenuConfig.getStringList("vault_items.medium_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_MainMenuVaultSlotMap_Medium.putIfAbsent(slot,vault_num);
            String path = null;
            int old_num = vault_num + 10;
            if(vault_num <= 15){
                if (player.hasPermission("playerinv.medium.inv." + vault_num) || player.isOp() || player.hasPermission(MediumFullInv)) {
                    path = "vault_items.medium_unlocked.";
                } else if (player.hasPermission("playerinv.inv." + old_num) || player.isOp()){
                    path = "vault_items.medium_unlocked.";
                } else {
                    path = "vault_items.medium_locked.";
                }
            }
            if(vault_num > 15){
                if (player.hasPermission("playerinv.medium.inv." + vault_num) || player.isOp() || player.hasPermission(MediumFullInv)) {
                    path = "vault_items.medium_unlocked.";
                } else {
                    path = "vault_items.medium_locked.";
                }
            }
            if (Check_MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = Check_MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (!lore.isEmpty()) {
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (Check_MainMenuConfig.contains(path + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String path : Check_MainMenuConfig.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (Check_MainMenuConfig.contains(path_prefix + "slot")) {
                int slot = Check_MainMenuConfig.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path_prefix + "display-name");
                List<String> lore = Check_MainMenuConfig.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path_prefix + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (Check_MainMenuConfig.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(Check_MainMenuConfig.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!Check_MainMenuConfig.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                Check_MainMenuConfig.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    Check_MainMenuConfig.save(Check_MainMenu);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path_prefix + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path_prefix + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (!lore.isEmpty()) {
                        for (String s : lore) {
                            lore_list.add(color(s));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if (Check_MainMenuConfig.contains(path_prefix + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (Check_MainMenuConfig.contains(path_prefix + "close-menu")) {
                        if(Check_MainMenuConfig.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (Check_MainMenuConfig.getBoolean(path_prefix + "close-menu")) {
                            Check_MainMenuItemMap.put(slot, "close");
                        }
                    }
                    if(!is113 && !isBelow113){
                        if(Check_MainMenuConfig.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(Check_MainMenuConfig.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(Check_MainMenuConfig.contains(path_prefix + "open-menu")){
                        String menu = Check_MainMenuConfig.getString(path_prefix + "open-menu");
                        if(Check_VaultMenuMap.containsKey(menu)){
                            Check_MainMenuItemMap.put(slot,menu);
                        } else {
                            invalidMenu(path);
                            continue;
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            } else {
                invalidSlot(path);
            }
        }
        return main;
    }

    public static Inventory Check_Main_GUI_Offline(OfflinePlayer player) {
        HashMap<Integer, String> Map = new HashMap<>();
        Check_MainMenuItemMap = Map;
        Check_MainMenuVaultSlotMap_Large = new HashMap<>();
        Check_MainMenuVaultSlotMap_Medium = new HashMap<>();
        int size = Check_MainMenuConfig.getInt("size");
        String title = Check_MainMenuConfig.getString("menu_title");
        Inventory main = Bukkit.createInventory(new Check_MainMenuHolder_Offline(), size, color(title));
        for (String vault : Check_MainMenuConfig.getStringList("vault_items.large_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_MainMenuVaultSlotMap_Large.putIfAbsent(slot,vault_num);
            String path = "vault_items.large_unlocked.";
            if (Check_MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = Check_MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(isBelow113){
                        if (MainMenuConfig.contains(path+ "item-subid")) {
                            int id = MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (Check_MainMenuConfig.contains(path + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String vault : Check_MainMenuConfig.getStringList("vault_items.medium_vault_set")) {
            if(!vault.contains(":")){
                unknownVaultNum(vault);
                continue;
            }
            String[] brand_vault = vault.split(":");
            int slot = Integer.parseInt(brand_vault[0]);
            int vault_num = Integer.parseInt(brand_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_MainMenuVaultSlotMap_Medium.putIfAbsent(slot,vault_num);
            String path = "vault_items.medium_unlocked.";
            if (Check_MainMenuConfig.getString(path + "material") != null) {
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = Check_MainMenuConfig.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        for (String s : lore) {
                            lore_list.add(color(s.replaceAll("%vault_num%", String.valueOf(vault_num))));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (Check_MainMenuConfig.contains(path + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            }
        }
        for (String path : Check_MainMenuConfig.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (Check_MainMenuConfig.contains(path_prefix + "slot")) {
                int slot = Check_MainMenuConfig.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = Check_MainMenuConfig.getString(path_prefix + "display-name");
                List<String> lore = Check_MainMenuConfig.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (Check_MainMenuConfig.getInt(path_prefix + "amount") != 0) {
                    amount = Check_MainMenuConfig.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (Check_MainMenuConfig.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(Check_MainMenuConfig.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!Check_MainMenuConfig.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                Check_MainMenuConfig.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    Check_MainMenuConfig.save(Check_MainMenu);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(Check_MainMenuConfig.getString(path_prefix + "material")) == null) {
                        invalidMaterial(PlayerInv.Check_MainMenu, Check_MainMenuConfig.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(Check_MainMenuConfig.getString(path_prefix + "material")), amount);
                    if (displayName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(color(displayName));
                        item.setItemMeta(meta);
                    }
                    if (lore != null) {
                        for (String s : lore) {
                            lore_list.add(color(s));
                        }
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore_list);
                        item.setItemMeta(meta);
                    }
                    if(isBelow113){
                        if (Check_MainMenuConfig.contains(path_prefix + "item-subid")) {
                            int id = Check_MainMenuConfig.getInt(path_prefix + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") && Check_MainMenuConfig.contains(path + "head-textures") && !isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, Check_MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if(item.getType().toString().equalsIgnoreCase("SKULL_ITEM") && Check_MainMenuConfig.contains(path + "head-textures") && isBelow113){
                        if(Check_MainMenuConfig.getString(path + "head-textures") != null){
                            setCustomSkull(item, MainMenuConfig.getString(path + "head-textures"));
                        }
                    }
                    if (Check_MainMenuConfig.contains(path_prefix + "enchant-glow")) {
                        if (Check_MainMenuConfig.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if (Check_MainMenuConfig.contains(path_prefix + "close-menu")) {
                        if(Check_MainMenuConfig.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (Check_MainMenuConfig.getBoolean(path_prefix + "close-menu")) {
                            Check_MainMenuItemMap.put(slot, "close");
                        }
                    }
                    if(!is113 && !isBelow113){
                        if(Check_MainMenuConfig.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(Check_MainMenuConfig.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(Check_MainMenuConfig.contains(path_prefix + "open-menu")){
                        String menu = Check_MainMenuConfig.getString(path_prefix + "open-menu");
                        if(Check_VaultMenuMap.containsKey(menu)){
                            Check_MainMenuItemMap.put(slot,menu);
                        } else {
                            invalidMenu(path);
                            continue;
                        }
                    }
                    main.setItem(slot - 1, item);
                }
            } else {
                invalidSlot(path);
            }
        }
        return main;
    }
}
