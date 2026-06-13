package com.playerinv.Util.Object.Cache;

import com.playerinv.Util.InvHolder.VaultHolder_Large;
import com.playerinv.Util.InvHolder.VaultHolder_Medium;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.playerinv.PlayerInv.cacheInventoryManager;
import static com.playerinv.PlayerInv.vaultAttributesManager;
import static com.playerinv.Util.NodeUtil.inventoryToBase64;

public class InventoryContainer {

    private Inventory inventory;

    private boolean hasError;

    private boolean isReady;

    private int type;

    public InventoryContainer(int type) {
        this.type = type;
        this.inventory = null;
        this.hasError = false;
        this.isReady = true;
    }

    public InventoryContainer(int type,boolean notReady) {
        this.type = type;
        this.inventory = null;
        this.hasError = false;
        this.isReady = !notReady;
    }

    public InventoryContainer(int type,int num,String uuid,Inventory inventory) {
        this.type = type;
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(type == 1){
            VaultHolder_Large vaultHolder = new VaultHolder_Large(num);
            this.inventory = Bukkit.createInventory(vaultHolder, 54, vaultAttributesManager.replaceVaultTitle(player,type,num));
            vaultHolder.setInventory(this.inventory);
            vaultHolder.setContainer(this);
            this.inventory.setContents(inventory.getContents());
        } else {
            VaultHolder_Medium vaultHolder = new VaultHolder_Medium(num);
            this.inventory = Bukkit.createInventory(vaultHolder, 27, vaultAttributesManager.replaceVaultTitle(player,type,num));
            vaultHolder.setInventory(this.inventory);
            vaultHolder.setContainer(this);
            this.inventory.setContents(inventory.getContents());
        }
        this.hasError = false;
        this.isReady = true;
    }

    public void insertInventory(int num,String uuid,Inventory inventory) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(type == 1){
            VaultHolder_Large vaultHolder = new VaultHolder_Large(num);
            this.inventory = Bukkit.createInventory(vaultHolder, 54, vaultAttributesManager.replaceVaultTitle(player,type,num));
            vaultHolder.setInventory(this.inventory);
            vaultHolder.setContainer(this);
            this.inventory.setContents(inventory.getContents());
        } else {
            VaultHolder_Medium vaultHolder = new VaultHolder_Medium(num);
            this.inventory = Bukkit.createInventory(vaultHolder, 27, vaultAttributesManager.replaceVaultTitle(player,type,num));
            vaultHolder.setInventory(this.inventory);
            vaultHolder.setContainer(this);
            this.inventory.setContents(inventory.getContents());
        }
    }

    public boolean isNotReady() {
        return !this.isReady;
    }

    public boolean isNull(){
        return this.inventory == null;
    }

    public void setContents(ItemStack[] contents) {
        this.inventory.setContents(contents);
    }

    public String getStrings(){
        return inventoryToBase64(this.inventory);
    }

    public int getSize(){
        return this.inventory.getSize();
    }

    public ItemStack getItem(int index){
        return this.inventory.getItem(index);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setHolderSettings(boolean isCommand,boolean isOnlyPickup){
        if(this.type == 1){
            VaultHolder_Large holder = (VaultHolder_Large) this.inventory.getHolder();
            holder.setIsCommand(isCommand);
            holder.setOnlyPickup(isOnlyPickup);
        } else {
            VaultHolder_Medium holder = (VaultHolder_Medium) this.inventory.getHolder();
            holder.setIsCommand(isCommand);
            holder.setOnlyPickup(isOnlyPickup);
        }
    }

    public void syncAndUpload(Player player,int vault_num){
        CacheInventory cacheInventory = cacheInventoryManager.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.uploadCache(type,vault_num);
    }

    public boolean hasError() {
        return hasError;
    }

    public void setError() {
        this.hasError = true;
    }
}
