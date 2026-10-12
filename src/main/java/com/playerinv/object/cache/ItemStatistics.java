package com.playerinv.object.cache;

import com.playerinv.PlayerInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashSet;
import java.util.List;

public class ItemStatistics {

    private ItemStack itemStack;

    private int slot;

    private String displayName;

    private List<String> lore;

    private int vaultType;

    private int vaultNum;

    private boolean isShulkerBox;

    private HashSet<String> nameSet;

    public ItemStatistics(ItemStack itemStack,int vaultType,int vaultNum,int slot,String locale) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
        this.isShulkerBox = false;
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            this.displayName = itemStack.getItemMeta().getDisplayName();
        } else {
            this.displayName = PlayerInv.getInstance().getTranslator().translate(this.itemStack,locale);
        }
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            this.lore = itemStack.getItemMeta().getLore();
        } else {
            this.lore = null;
        }
        if(itemStack.getType().toString().toLowerCase().contains("shulker_box")) {
            this.isShulkerBox = true;
            if(itemStack.getItemMeta() instanceof BlockStateMeta){
                BlockStateMeta im = (BlockStateMeta)itemStack.getItemMeta();
                if(im.getBlockState() instanceof ShulkerBox){
                    ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                    Inventory inv = Bukkit.createInventory(null, 27, "Shulker Box");
                    inv.setContents(shulker.getInventory().getContents());
                    this.statShulkerBox(inv,locale);
                }
            }
        }
    }

    public void statShulkerBox(Inventory inventory,String locale) {
        this.nameSet = new HashSet<>();
        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item == null || item.getType() == Material.AIR) {
                continue;
            }
            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                this.nameSet.add(item.getItemMeta().getDisplayName());
            } else {
                this.nameSet.add(PlayerInv.getInstance().getTranslator().translate(item, locale));
            }
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack isTarget(String search, boolean searchName, boolean searchLore) {
        if(searchName && searchLore){
            return allContains(search);
        }
        if(searchLore){
            return loreContains(search);
        }
        return nameContains(search);
    }

    private ItemStack nameContains(String search){
        if(isShulkerBox){
            if(nameSet.stream().anyMatch(s -> s.toLowerCase().contains(search.toLowerCase()))){
                return this.itemStack;
            }
        }
        return this.displayName.toLowerCase().contains(search.toLowerCase()) ? this.itemStack : null;
    }

    private ItemStack loreContains(String search){
        if(this.lore == null) return null;
        return this.lore.stream().anyMatch(s -> s.toLowerCase().contains(search.toLowerCase())) ? this.itemStack : null;
    }

    private ItemStack allContains(String search){
        if(nameContains(search) == null && loreContains(search) == null){
            return null;
        }
        return this.itemStack;
    }

    public int getVaultType() {
        return vaultType;
    }

    public int getVaultNum() {
        return vaultNum;
    }

    public int getSlot() {
        return slot;
    }
}
