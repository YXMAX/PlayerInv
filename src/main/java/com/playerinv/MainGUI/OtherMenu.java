package com.playerinv.MainGUI;

import com.playerinv.InvHolder.Check_OtherMenuHolder;
import com.playerinv.InvHolder.Check_OtherMenuHolder_Offline;
import com.playerinv.InvHolder.OtherMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.playerinv.MainGUI.MenuItemException.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;

public class OtherMenu {

    public static Inventory Other_GUI(FileConfiguration MenuFile,Player player) {
        String menu_file_key = null;
        for(String key : OtherMenuFileList){
            if(VaultMenuMap.get(key).equals(MenuFile)){
                menu_file_key = key;
            }
        }
        int size = MenuFile.getInt("size");
        String title = MenuFile.getString("menu_title");
        Inventory main = Bukkit.createInventory(new OtherMenuHolder(), size, color(title));
        for (String path : MenuFile.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (MenuFile.contains(path_prefix + "slot")) {
                int slot = MenuFile.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = MenuFile.getString(path_prefix + "display-name");
                List<String> lore = MenuFile.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path_prefix + "amount") != 0) {
                    amount = MenuFile.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (MenuFile.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(MenuFile.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!MenuFile.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                MenuFile.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    MenuFile.save(OtherMenuFileMap.get(menu_file_key));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(MenuFile.getString(path_prefix + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path_prefix + "material")), amount);
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
                    if (MenuFile.contains(path_prefix + "enchant-glow")) {
                        if (MenuFile.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if (MenuFile.contains(path_prefix + "close-menu")) {
                        if(MenuFile.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (MenuFile.getBoolean(path_prefix + "close-menu")) {
                            OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,"close");
                        }
                    }
                    if(!is113 && !isBelow113){
                        if(MenuFile.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(MenuFile.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(isBelow113){
                        if (MenuFile.contains(path_prefix + "item-subid")) {
                            int id = MenuFile.getInt(path_prefix + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(MenuFile.contains(path_prefix + "open-menu")){
                        String menu = MenuFile.getString(path_prefix + "open-menu");
                        if(VaultMenuMap.containsKey(menu)){
                            OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,menu);
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
        for (String vault : MenuFile.getStringList("vault_items.large_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            OtherMenuVaultSlotMap_Large.putIfAbsent(menu_file_key + ":" + slot,vault_num);
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
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        for (String vault : MenuFile.getStringList("vault_items.medium_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Medium_Amount){
                invalidVaultAmount(vault);
                break;
            }
            OtherMenuVaultSlotMap_Medium.putIfAbsent(menu_file_key + ":" + slot,vault_num);
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
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        OtherMenuInventoryMap.putIfAbsent(main,menu_file_key);
        return main;
    }

    public static Inventory Check_Other_GUI(FileConfiguration MenuFile,Player player) {
        String menu_file_key = null;
        for(String key : Check_OtherMenuFileList){
            if(Check_VaultMenuMap.get(key).equals(MenuFile)){
                menu_file_key = key;
            }
        }
        int size = MenuFile.getInt("size");
        String title = MenuFile.getString("menu_title");
        Inventory main = Bukkit.createInventory(new Check_OtherMenuHolder(), size, color(title));
        for (String path : MenuFile.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (MenuFile.contains(path_prefix + "slot")) {
                int slot = MenuFile.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = MenuFile.getString(path_prefix + "display-name");
                List<String> lore = MenuFile.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path_prefix + "amount") != 0) {
                    amount = MenuFile.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (MenuFile.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(MenuFile.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!MenuFile.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                MenuFile.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    MenuFile.save(Check_OtherMenuFileMap.get(menu_file_key));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(MenuFile.getString(path_prefix + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path_prefix + "material")), amount);
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
                    if (MenuFile.contains(path_prefix + "enchant-glow")) {
                        if (MenuFile.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if (MenuFile.contains(path_prefix + "close-menu")) {
                        if(MenuFile.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (MenuFile.getBoolean(path_prefix + "close-menu")) {
                            Check_OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,"close");
                        }
                    }
                    if(!is113 && isBelow113){
                        if(MenuFile.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(MenuFile.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(isBelow113){
                        if (MenuFile.contains(path_prefix + "item-subid")) {
                            int id = MenuFile.getInt(path_prefix + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if(MenuFile.contains(path_prefix + "open-menu")){
                        String menu = MenuFile.getString(path_prefix + "open-menu");
                        if(Check_VaultMenuMap.containsKey(menu)){
                            Check_OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,menu);
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
        for (String vault : MenuFile.getStringList("vault_items.large_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_OtherMenuVaultSlotMap_Large.putIfAbsent(menu_file_key + ":" + slot,vault_num);
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
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        for (String vault : MenuFile.getStringList("vault_items.medium_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Medium_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_OtherMenuVaultSlotMap_Medium.putIfAbsent(menu_file_key + ":" + slot,vault_num);
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
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        Check_OtherMenuInventoryMap.putIfAbsent(main,menu_file_key);
        return main;
    }

    public static Inventory Check_Other_GUI_Offline(FileConfiguration MenuFile, OfflinePlayer player) {
        String menu_file_key = null;
        for(String key : Check_OtherMenuFileList){
            if(Check_VaultMenuMap.get(key).equals(MenuFile)){
                menu_file_key = key;
            }
        }
        int size = MenuFile.getInt("size");
        String title = MenuFile.getString("menu_title");
        Inventory main = Bukkit.createInventory(new Check_OtherMenuHolder_Offline(), size, color(title));
        for (String path : MenuFile.getConfigurationSection("items").getKeys(false)) {
            String path_prefix = "items." + path + ".";
            if (MenuFile.contains(path_prefix + "slot")) {
                int slot = MenuFile.getInt(path_prefix + "slot");
                int amount = 1;
                String displayName = MenuFile.getString(path_prefix + "display-name");
                List<String> lore = MenuFile.getStringList(path_prefix + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path_prefix + "amount") != 0) {
                    amount = MenuFile.getInt(path_prefix + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                }
                if (MenuFile.getString(path_prefix + "material") != null) {
                    if(isBelow113){
                        if(MenuFile.getString(path_prefix + "material").contains("STAINED_GLASS_PANE")){
                            if(!MenuFile.getString(path_prefix + "material").equals("STAINED_GLASS_PANE")){
                                MenuFile.set(path_prefix + "material","STAINED_GLASS_PANE");
                                try {
                                    MenuFile.save(Check_OtherMenuFileMap.get(menu_file_key));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (Material.getMaterial(MenuFile.getString(path_prefix + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path_prefix + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path_prefix + "material")), amount);
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
                    if (MenuFile.contains(path_prefix + "enchant-glow")) {
                        if (MenuFile.getBoolean(path_prefix + "enchant-glow")) {
                            ItemMeta meta = item.getItemMeta();
                            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(meta);
                        }
                    }
                    if(isBelow113){
                        if (MenuFile.contains(path_prefix + "item-subid")) {
                            int id = MenuFile.getInt(path_prefix + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path_prefix + "close-menu")) {
                        if(MenuFile.contains(path_prefix + "open-menu")){
                            invalidMix(path);
                            continue;
                        }
                        if (MenuFile.getBoolean(path_prefix + "close-menu")) {
                            Check_OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,"close");
                        }
                    }
                    if(!is113 && !isBelow113){
                        if(MenuFile.contains(path_prefix + "custom-model-data")){
                            ItemMeta meta = item.getItemMeta();
                            meta.setCustomModelData(MenuFile.getInt(path_prefix + "custom-model-data"));
                            item.setItemMeta(meta);
                        }
                    }
                    if(MenuFile.contains(path_prefix + "open-menu")){
                        String menu = MenuFile.getString(path_prefix + "open-menu");
                        if(Check_VaultMenuMap.containsKey(menu)){
                            Check_OtherMenuItemMap.putIfAbsent(menu_file_key + ":" + slot,menu);
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
        for (String vault : MenuFile.getStringList("vault_items.large_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Large_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_OtherMenuVaultSlotMap_Large.putIfAbsent(menu_file_key + ":" + slot,vault_num);
            String path = "vault_items.large_unlocked.";
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        for (String vault : MenuFile.getStringList("vault_items.medium_vault_set")) {
            if (!vault.contains(":")) {
                unknownVaultNum(vault);
                continue;
            }
            String[] new_vault = vault.split(":");
            int slot = Integer.parseInt(new_vault[0]);
            int vault_num = Integer.parseInt(new_vault[1]);
            if(vault_num > Medium_Amount){
                invalidVaultAmount(vault);
                break;
            }
            Check_OtherMenuVaultSlotMap_Medium.putIfAbsent(menu_file_key + ":" + slot,vault_num);
            String path = "vault_items.medium_unlocked.";
            if (MenuFile.getString(path + "material") != null) {
                int amount = 1;
                String displayName = MenuFile.getString(path + "display-name").replaceAll("%vault_num%", String.valueOf(vault_num));
                List<String> lore = MenuFile.getStringList(path + "lore");
                ArrayList<String> lore_list = new ArrayList<>();
                if (MenuFile.getInt(path + "amount") != 0) {
                    amount = MenuFile.getInt(path + "amount");
                    if (amount > 64) {
                        overloadAmount(path);
                        continue;
                    }
                    if (Material.getMaterial(MenuFile.getString(path + "material")) == null) {
                        invalidMaterial_Other(MenuFile.getString(path + "material"));
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(MenuFile.getString(path + "material")), amount);
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
                        if (MenuFile.contains(path + "item-subid")) {
                            int id = MenuFile.getInt(path + "item-subid");
                            item.setDurability((short) id);
                        }
                    }
                    if (MenuFile.contains(path + "enchant-glow")) {
                        if (MenuFile.getBoolean(path + "enchant-glow")) {
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
        Check_OtherMenuInventoryMap.putIfAbsent(main,menu_file_key);
        return main;
    }
}
