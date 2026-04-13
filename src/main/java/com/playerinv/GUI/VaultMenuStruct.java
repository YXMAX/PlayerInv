package com.playerinv.GUI;

import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Temp.VaultMenuContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LoadUtil.openMainMenu;
import static com.playerinv.Util.NodeUtil.*;
import static com.playerinv.Util.InitUtil.*;

public class VaultMenuStruct {

    private final MenuItemException menuItemException = new MenuItemException();

    private HashMap<String, VaultMenuContainer> vaultContainerMap = new HashMap<>();

    public void structVaultMenu(){
        this.loadVaultMenu();
        for(Map.Entry<String,VaultMenuContainer> entry : vaultContainerMap.entrySet()){
            FileConfiguration config = entry.getValue().getConfig();
            String file_name = entry.getKey();
            int size = menuItemException.checkVaultSize(entry.getKey(),config.getInt("size"));
            String title = config.getString("menu_title");
            Inventory inventory = Bukkit.createInventory(null, size, "Inventory");

            HashMap<Integer,String> click_map = new HashMap<>();
            HashMap<Integer,Integer> large_map = new HashMap<>();
            HashMap<Integer,Integer> medium_map = new HashMap<>();
            ItemStack large_lock = null;
            ItemStack large_unlock = null;
            ItemStack medium_lock = null;
            ItemStack medium_unlock = null;


            for (String path : config.getConfigurationSection("vault_items").getKeys(false)) {
                String full_path = "vault_items." + path + ".";
                switch(path.toLowerCase()){

                    case "large_locked":
                    case "large_unlocked":
                    case "medium_locked":
                    case "medium_unlocked":
                        String material = config.getString(full_path + "material");
                        int amount = config.getInt(full_path + "amount");
                        boolean enchant_glow = config.getBoolean(full_path + "enchant-glow");
                        String display_name = config.getString(full_path + "display-name");
                        List<String> lore = config.getStringList(full_path + "lore");
                        int sub_id = config.getInt(full_path + "item-subid");
                        String head_texture = config.getString(full_path + "head-texture");
                        int custom_model_data = config.getInt(full_path + "custom-model-data");
                        if(material == null || Material.getMaterial(material) == null){
                            menuItemException.invalidMaterial(file_name,full_path,material,true);
                            material = "CHEST";
                        }
                        if(amount <= 0 || amount > 64){
                            menuItemException.invalidAmount(full_path);
                            amount = 1;
                        }
                        ItemStack item = new ItemStack(Material.getMaterial(material), amount);
                        ItemMeta meta = item.getItemMeta();
                        if(enchant_glow){
                            meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        }
                        if(display_name != null){
                            meta.setDisplayName(color(display_name));
                        }
                        if(!lore.isEmpty()){
                            List<String> new_lore = new ArrayList<>();
                            for(String line : lore){
                                new_lore.add(color(line));
                            }
                            meta.setLore(new_lore);
                        }
                        if(custom_model_data > 0){
                            meta.setCustomModelData(custom_model_data);
                        }
                        item.setItemMeta(meta);
                        if(isBelow113){
                            if(sub_id > 0){
                                item.setDurability((short) sub_id);
                            }
                            if(material.equalsIgnoreCase("SKULL_ITEM") && sub_id == 3){
                                if(head_texture != null){
                                    item = setCustomSkull(item,head_texture);
                                }
                            }
                        } else {
                            if(material.equalsIgnoreCase("PLAYER_HEAD")){
                                if(head_texture != null){
                                    item = setCustomSkull(item,head_texture);
                                }
                            }
                        }
                        switch(path.toLowerCase()){
                            case "large_locked":
                                large_lock = item;
                                break;
                            case "large_unlocked":
                                large_unlock = item;
                                break;
                            case "medium_locked":
                                medium_lock = item;
                                break;
                            case "medium_unlocked":
                                medium_unlock = item;
                                break;
                        }
                        break;

                    case "large_vault_set":
                        for (String vault : config.getStringList("vault_items.large_vault_set")) {
                            if(!vault.contains(":")){
                                menuItemException.unknownVaultNum(vault);
                                continue;
                            }
                            String[] brand_vault = vault.split(":");
                            int slot = Integer.parseInt(brand_vault[0]);
                            int vault_num = Integer.parseInt(brand_vault[1]);
                            if(vault_num > largeVaultAmount){
                                menuItemException.invalidVaultAmount(file_name + ":vault_items.large_vault_set:" + vault);
                                continue;
                            }
                            large_map.put(slot,vault_num);
                        }
                        break;

                    case "medium_vault_set":
                        for (String vault : config.getStringList("vault_items.medium_vault_set")) {
                            if(!vault.contains(":")){
                                menuItemException.unknownVaultNum(vault);
                                continue;
                            }
                            String[] brand_vault = vault.split(":");
                            int slot = Integer.parseInt(brand_vault[0]);
                            int vault_num = Integer.parseInt(brand_vault[1]);
                            if(vault_num > mediumVaultAmount){
                                menuItemException.invalidVaultAmount(file_name + ":vault_items.medium_vault_set:" + vault);
                                continue;
                            }
                            medium_map.put(slot,vault_num);
                        }
                        break;
                }
            }

            for (String path : config.getConfigurationSection("items").getKeys(false)) {
                String full_path = "items." + path + ".";
                String material = config.getString(full_path + "material");
                int amount = config.getInt(full_path + "amount");
                int slot = config.getInt(full_path + "slot");
                boolean enchant_glow = config.getBoolean(full_path + "enchant-glow");
                String display_name = config.getString(full_path + "display-name");
                List<String> lore = config.getStringList(full_path + "lore");
                int sub_id = config.getInt(full_path + "item-subid");
                String head_texture = config.getString(full_path + "head-texture");
                int custom_model_data = config.getInt(full_path + "custom-model-data");
                if(slot <= 0){
                    menuItemException.invalidSlot(file_name + ":" + full_path);
                    continue;
                }
                if(slot > size){
                    menuItemException.oversizeSlot(file_name + ":" + full_path);
                    continue;
                }
                int colorful = isMaterialColorful(material);
                if(colorful != -1){
                    material = NodeUtil.resetMaterial_WoolGlass(material);
                    sub_id = colorful;
                }
                if(material == null || Material.getMaterial(material) == null){
                    menuItemException.invalidMaterial(file_name,full_path,material,false);
                    continue;
                }
                if(amount <= 0 || amount > 64){
                    menuItemException.invalidAmount(full_path);
                    amount = 1;
                }
                ItemStack item = new ItemStack(Material.getMaterial(material), amount);
                ItemMeta meta = item.getItemMeta();
                if(enchant_glow){
                    meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                if(display_name != null){
                    meta.setDisplayName(color(display_name));
                }
                if(!lore.isEmpty()){
                    List<String> new_lore = new ArrayList<>();
                    for(String line : lore){
                        new_lore.add(color(line));
                    }
                    meta.setLore(new_lore);
                }
                if(custom_model_data > 0){
                    meta.setCustomModelData(custom_model_data);
                }
                item.setItemMeta(meta);
                if(isBelow113){
                    if(sub_id > 0){
                        item.setDurability((short) sub_id);
                    }
                    if(material.equalsIgnoreCase("SKULL_ITEM") && sub_id == 3){
                        if(head_texture != null){
                            item = setCustomSkull(item,head_texture);
                        }
                    }
                } else {
                    if(material.equalsIgnoreCase("PLAYER_HEAD")){
                        if(head_texture != null){
                            item = setCustomSkull(item,head_texture);
                        }
                    }
                }
                inventory.setItem(slot-1, item);
                boolean close_menu = config.getBoolean(full_path + "close-menu");
                boolean open_share = config.getBoolean(full_path + "open-share");
                boolean open_search = config.getBoolean(full_path + "open-search");
                boolean pickup = config.getBoolean(full_path + "pickup-switch");
                String open_menu = config.getString(full_path + "open-menu");
                if(close_menu){
                    click_map.put(slot,"PRESET@close");
                    continue;
                }
                if(pickup){
                    click_map.put(slot,"PRESET@pickup-switch");
                    continue;
                }
                if(open_share){
                    click_map.put(slot,"PRESET@share");
                    continue;
                }
                if(open_search){
                    click_map.put(slot,"PRESET@open-search");
                    continue;
                }
                if(open_menu != null){
                    click_map.put(slot,"PRESET@menu%%" + open_menu);
                    continue;
                }
                String command = config.getString(full_path + "command");
                if(command != null){
                    if(command.contains("[") || command.contains("]")){
                        String type = command.substring(command.indexOf("[") + 1, command.indexOf("]"));
                        switch(type){
                            case "console":
                                command = command.replaceAll("\\[console\\]"," ");
                                click_map.put(slot, "COMMAND@console##" + command);
                                break;
                            case "op":
                                command = command.replaceAll("\\[op\\]"," ");
                                click_map.put(slot, "COMMAND@op##" + command);
                                break;
                            case "player":
                                command = command.replaceAll("\\[player\\]"," ");
                                click_map.put(slot, "COMMAND@player##" + command);
                                break;
                        }
                    } else {
                        click_map.put(slot, "COMMAND@player##" + command);
                    }
                }
            }
            entry.getValue().init(inventory,title,click_map,large_map,medium_map,large_unlock,large_lock,medium_unlock,medium_lock);
        }
    }

    public void buildVaultMenu(Player player,String file_name){
        VaultMenuContainer vaultMenu = vaultContainerMap.get(file_name);
        if(vaultMenu == null){
            file_name = openMainMenu;
        }
        vaultMenu.openMenu(player,file_name);
    }

    private void loadVaultMenu(){
        vaultContainerMap.clear();
        File file = Paths.get(plugin.getDataFolder().getAbsolutePath(), "vault_menu").normalize().toFile();
        File[] menu_array = file.listFiles();
        if(menu_array == null){
            if(isWindows()){
                menu_array = file.listFiles();
            }
            if(isLinux()){
                try {
                    Stream<Path> stream = Files.list(file.toPath());
                    menu_array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (int i = 0; i < menu_array.length; i++) {
            if (menu_array[i].isFile()) {
                try {
                    File current_file = menu_array[i].getAbsoluteFile();
                    YamlConfiguration current_fileConfig = new YamlConfiguration();
                    current_fileConfig.load(current_file);
                    String file_name = menu_array[i].getName();
                    String new_name = null;
                    if(file_name.endsWith(".yml")){
                        new_name = file_name.substring(0, file_name.lastIndexOf("."));
                    } else {
                        continue;
                    }
                    vaultContainerMap.put(new_name,new VaultMenuContainer(current_fileConfig));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
