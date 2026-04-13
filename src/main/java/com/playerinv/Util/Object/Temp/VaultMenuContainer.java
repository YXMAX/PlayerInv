package com.playerinv.Util.Object.Temp;

import com.playerinv.Util.InvHolder.VaultMenuHolder;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Cache.VaultAttributes;
import com.playerinv.Util.PlaceHolder.PlaceholderTemp;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.PlayerInv.cacheInventoryManager;
import static com.playerinv.Util.InitUtil.hasPAPI;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.NodeUtil.color;
import static com.playerinv.Util.NodeUtil.sendLog;

public class VaultMenuContainer {

    private Inventory basicInventory;

    private String menu_title;

    private YamlConfiguration config;

    private HashMap<Integer, String> click_map;

    private HashMap<Integer, Integer> large_map;

    private HashMap<Integer, Integer> medium_map;

    private ItemStack LargeVault_Perm;

    private ItemStack LargeVault_NoPerm;

    private ItemStack MediumVault_Perm;

    private ItemStack MediumVault_NoPerm;

    public VaultMenuContainer(YamlConfiguration config) {
        this.config = config;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void init(Inventory inventory,String title,HashMap<Integer, String> click_map,HashMap<Integer,Integer> large,HashMap<Integer,Integer> medium,ItemStack largeVault_Perm,ItemStack largeVault_NoPerm,ItemStack mediumVault_Perm,ItemStack mediumVault_NoPerm) {
        this.basicInventory = inventory;
        this.menu_title = title;
        this.click_map = click_map;
        this.large_map = large;
        this.medium_map = medium;
        this.LargeVault_Perm = largeVault_Perm;
        this.LargeVault_NoPerm = largeVault_NoPerm;
        this.MediumVault_Perm = mediumVault_Perm;
        this.MediumVault_NoPerm = mediumVault_NoPerm;
    }

    public Inventory getBasicInventory() {
        return basicInventory;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public HashMap<Integer, String> getClick_map() {
        return click_map;
    }

    public ItemStack getLargeVault_Perm() {
        return LargeVault_Perm;
    }

    public ItemStack getLargeVault_NoPerm() {
        return LargeVault_NoPerm;
    }

    public ItemStack getMediumVault_Perm() {
        return MediumVault_Perm;
    }

    public ItemStack getMediumVault_NoPerm() {
        return MediumVault_NoPerm;
    }

    public void openMenu(Player player,String file_name) {
        String title = this.menu_title;
        if(hasPAPI){
            title = PlaceholderAPI.setPlaceholders(player, title);
        }
        VaultMenuHolder holder = new VaultMenuHolder(player,file_name,this.click_map,this.large_map,this.medium_map);
        Inventory inventory = Bukkit.createInventory(holder, basicInventory.getSize(), color(title));
        inventory.setContents(basicInventory.getContents());
        if(hasPAPI){
            for(int i=0;i<inventory.getSize();i++){
                ItemStack item = inventory.getItem(i);
                if(item == null || item.getType() == Material.AIR){
                    continue;
                }
                ItemMeta meta = item.getItemMeta();
                if(meta.hasDisplayName()){
                    String new_name = PlaceholderAPI.setPlaceholders(player, meta.getDisplayName());
                    meta.setDisplayName(color(new_name));
                }
                if(meta.hasLore()){
                    List<String> new_lore = PlaceholderAPI.setPlaceholders(player, meta.getLore());
                    meta.setLore(new_lore);
                }
                item.setItemMeta(meta);
            }
        }
        if(luckperms_givePermissions && replaceExpiry){
            loadVault_AsyncExpiry(player,inventory);
        } else {
            loadVault_Async(player,inventory);
        }
        holder.setInventory(inventory);
        player.openInventory(inventory);
    }


    private void loadVault_Async(Player player,Inventory inventory){
        scheduler.scheduling().asyncScheduler().run(() -> {
            VaultAttributes vaultAttributes = vaultAttributesManager.get(player);
            for(Map.Entry<Integer,Integer> entry : this.large_map.entrySet()){
                int num = entry.getValue();
                int slot = entry.getKey();
                if(NodeUtil.hasVaultPermission(player,1,num)){
                    inventory.setItem(slot-1, this.setVault(this.getLargeVault_Perm(),num,1,player,vaultAttributes.getAttribute(1,num)));
                } else {
                    inventory.setItem(slot-1, this.setVault(this.getLargeVault_NoPerm(),num,1,player,vaultAttributes.getAttribute(1,num)));
                }
            }

            for(Map.Entry<Integer,Integer> entry : this.medium_map.entrySet()){
                int num = entry.getValue();
                int slot = entry.getKey();
                if(NodeUtil.hasVaultPermission(player,2,num)){
                    inventory.setItem(slot-1, this.setVault(this.getMediumVault_Perm(),num,2,player,vaultAttributes.getAttribute(2,num)));
                } else {
                    inventory.setItem(slot-1, this.setVault(this.getMediumVault_NoPerm(),num,2,player,vaultAttributes.getAttribute(2,num)));
                }
            }
        });
    }

    private void loadVault_AsyncExpiry(Player player, Inventory inventory){
        scheduler.scheduling().asyncScheduler().run(task -> {
            PlaceholderTemp large = operationManager.getPlaceholders(player,1);
            PlaceholderTemp medium = operationManager.getPlaceholders(player,2);
            VaultAttributes vaultAttributes = vaultAttributesManager.get(player);
            for(Map.Entry<Integer,Integer> entry : this.large_map.entrySet()){
                int num = entry.getValue();
                int slot = entry.getKey();
                if(NodeUtil.hasVaultPermission(player,1,num)){
                    inventory.setItem(slot-1,this.setVault(this.getLargeVault_Perm(),num,1,player,vaultAttributes.getAttribute(1,num),large.getDisplay(player,num)));
                } else {
                    inventory.setItem(slot-1,this.setVault(this.getLargeVault_NoPerm(),num,1,player,vaultAttributes.getAttribute(1,num),large.getDisplay(player,num)));
                }
            }

            for(Map.Entry<Integer,Integer> entry : this.medium_map.entrySet()){
                int num = entry.getValue();
                int slot = entry.getKey();
                if(NodeUtil.hasVaultPermission(player,2,num)){
                    inventory.setItem(slot-1,this.setVault(this.getMediumVault_Perm(),num,2,player,vaultAttributes.getAttribute(2,num),medium.getDisplay(player,num)));
                } else {
                    inventory.setItem(slot-1,this.setVault(this.getMediumVault_NoPerm(),num,2,player,vaultAttributes.getAttribute(2,num),medium.getDisplay(player,num)));
                }
            }
        });
    }

    private ItemStack setVault(ItemStack item, int num, int type, Player player, String attribute_name){
        ItemStack new_item = item.clone();
        ItemMeta new_item_meta = new_item.getItemMeta();
        if(vaultNameChange && attribute_name != null){
            new_item_meta.setDisplayName(color(attribute_name.replaceAll("%vault_num%", String.valueOf(num))));
        } else {
            if(new_item_meta.hasDisplayName()){
                new_item_meta.setDisplayName(color(new_item_meta.getDisplayName().replaceAll("%vault_num%", String.valueOf(num))));
            }
        }
        if(new_item_meta.hasLore()){
            int remain_slot;
            if(cacheInventoryManager.containsKey(player)){
                remain_slot = cacheInventoryManager.get(player).getRemain(type,num);
            } else {
                if(type == 1){
                    remain_slot = 54;
                } else {
                    remain_slot = 27;
                }
            }
            List<String> copy_lore = new_item_meta.getLore();
            List<String> new_lore = new ArrayList<>();
            for(String s : copy_lore){
                new_lore.add(color(s.replaceAll("%remain_slot%", String.valueOf(remain_slot))));
            }
            new_item_meta.setLore(new_lore);
        }
        new_item.setItemMeta(new_item_meta);
        return new_item;
    }

    private ItemStack setVault(ItemStack item,int num,int type,Player player,String attribute_name,String day_display){
        ItemStack new_item = item.clone();
        ItemMeta new_item_meta = new_item.getItemMeta();
        if(vaultNameChange && attribute_name != null){
            new_item_meta.setDisplayName(color(attribute_name.replaceAll("%vault_num%", String.valueOf(num))));
        } else {
            if(new_item_meta.hasDisplayName()){
                new_item_meta.setDisplayName(color(new_item_meta.getDisplayName().replaceAll("%vault_num%", String.valueOf(num))));
            }
        }
        if(new_item_meta.hasLore()){
            int remain_slot;
            if(cacheInventoryManager.containsKey(player)){
                remain_slot = cacheInventoryManager.get(player).getRemain(type,num);
            } else {
                if(type == 1){
                    remain_slot = 54;
                } else {
                    remain_slot = 27;
                }
            }
            List<String> copy_lore = new_item_meta.getLore();
            List<String> new_lore = new ArrayList<>();
            for(String s : copy_lore){
                new_lore.add(color(s.replaceAll("%remain_slot%", String.valueOf(remain_slot))
                        .replaceAll("%vault_expiry%", day_display)));
            }
            new_item_meta.setLore(new_lore);
        }
        new_item.setItemMeta(new_item_meta);
        return new_item;
    }
}
