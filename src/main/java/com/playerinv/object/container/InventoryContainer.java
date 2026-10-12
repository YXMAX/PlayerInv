package com.playerinv.object.container;

import com.playerinv.PlayerInv;
import com.playerinv.api.VaultType;
import com.playerinv.api.event.VaultOpenEvent;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.service.serializer.InventorySerializer;
import com.playerinv.gui.holder.impl.vault.CheckVaultHolder;
import com.playerinv.gui.holder.impl.vault.StorageVaultHolder;
import com.playerinv.util.LocaleUtil;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.object.cache.CacheInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class InventoryContainer {

    private Inventory inventory;

    private boolean error;

    private boolean ready;

    private int type;

    private boolean cooldown;

    public InventoryContainer(int type) {
        this.type = type;
        this.inventory = null;
        this.error = false;
        this.ready = true;
    }

    public InventoryContainer(int type,Inventory inventory,boolean error){
        this.type = type;
        this.inventory = null;
        this.error = true;
        this.ready = true;
    }

    public InventoryContainer(int type, boolean notReady) {
        this.type = type;
        this.inventory = null;
        this.error = false;
        this.ready = !notReady;
    }

    public InventoryContainer(int type, int num, String uuid, Inventory inventory) {
        this.type = type;
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        StorageVaultHolder vaultHolder = new StorageVaultHolder(num);
        this.inventory = Bukkit.createInventory(vaultHolder, this.getVaultSizeByType(type), PlayerInv.getInstance().getVaultAttributesManager().replaceVaultTitle(player, type, num));
        vaultHolder.setInventory(this.inventory);
        vaultHolder.setContainer(this);
        this.inventory.setContents(inventory.getContents());
        this.error = false;
        this.ready = true;
    }

    private int getVaultSizeByType(int type) {
        return type == 1 ? 54 : 27;
    }

    public void insertInventory(int num, String uuid, Inventory inventory) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        this.setInventory(inventory.getContents(),uuid,num,player);
    }

    public int getType() {
        return type;
    }

    public boolean isNotReady() {
        return !this.ready;
    }

    public boolean isNull() {
        return this.inventory == null;
    }

    public void setContents(ItemStack[] contents) {
        this.inventory.setContents(contents);
    }

    public String getStrings() {
        return InventorySerializer.serializeInventory(this.inventory.getContents());
    }

    public int getSize() {
        return this.inventory.getSize();
    }

    public ItemStack getItem(int index) {
        return this.inventory.getItem(index);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void resetVaultTitle(Player player) {
        StorageVaultHolder holder = (StorageVaultHolder) this.inventory.getHolder();
        ItemStack[] copy = this.inventory.getContents();
        this.inventory = Bukkit.createInventory(holder, this.getVaultSizeByType(type), MessageUtil.parseString(PlayerInv.getInstance().getVaultAttributesManager().replaceVaultTitle(player, type, holder.getNum()), player));
        holder.setInventory(this.inventory);
        this.inventory.setContents(copy);
    }

    public void retryInventory(Player player,int num,boolean isCommand,boolean onlyPickup,String fileName){
        if(cooldown){
            LocaleUtil.sendMessages(player,LocaleEnums.Vault_item_retry_cooldown);
            return;
        }
        CompletableFuture.runAsync(() -> {
            LocaleUtil.sendMessages(player, LocaleEnums.Vault_item_retry);
            String s = JDBCUtil.getVaultString(type, player.getUniqueId().toString(), num);
            InventoryContainer container = InventorySerializer.deserializeVaultInventory(player, type, player.getUniqueId().toString(), num, s);
            if(container == null || container.hasError()){
                LocaleUtil.sendMessages(player, LocaleEnums.Vault_item_destroy_cancel_open);
                this.cooldown = true;
                PlayerInv.getScheduler().asyncScheduler().runDelayed(task -> {
                    this.cooldown = false;
                }, Duration.ofSeconds(10));
                return;
            }
            this.setInventory(container.getInventory());
            this.setHolderSettings(isCommand,onlyPickup,fileName);
            PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                PlayerInv.getInstance().getSoundManager().vaultOpen(player);
                VaultOpenEvent event = new VaultOpenEvent(player,this.inventory,VaultType.fromInteger(type),num);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                MessageUtil.sendDetail("玩家 " + player.getName() + " 打开个人仓库: " + type + "//" + player.getUniqueId() + ":" + num);
                player.openInventory(this.inventory);
            },null,1);
        });
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setInventory(Player player, int num){
        StorageVaultHolder holder = new StorageVaultHolder(num);
        this.inventory = Bukkit.createInventory(holder, this.getVaultSizeByType(type), MessageUtil.parseString(PlayerInv.getInstance().getVaultAttributesManager().replaceVaultTitle(player.getUniqueId().toString(), type, num), player));
        holder.setInventory(this.inventory);
        holder.setContainer(this);
    }

    public void setInventory(ItemStack[] itemStacks, String uuid, int num, Player player) {
        StorageVaultHolder holder = new StorageVaultHolder(num);
        this.inventory = Bukkit.createInventory(holder, this.getVaultSizeByType(type), MessageUtil.parseString(PlayerInv.getInstance().getVaultAttributesManager().replaceVaultTitle(uuid, type, num), player));
        holder.setInventory(this.inventory);
        holder.setContainer(this);
        this.inventory.setContents(itemStacks);
    }

    public void setInventory(ItemStack[] itemStacks, String uuid, int num, int permission, boolean checking, Player player) {
        this.setInventory(itemStacks, uuid, num, player);
    }

    public CheckVaultHolder setCheckInventoryHolder(ItemStack[] itemStacks, int type, int num, OfflinePlayer target, Inventory back_inventory) {
        CheckVaultHolder holder = new CheckVaultHolder(target, num, type, back_inventory);
        this.inventory = Bukkit.createInventory(holder, this.getVaultSizeByType(type), target.getUniqueId() + " [" + num + "]");
        this.inventory.setContents(itemStacks);
        holder.setVault(this.inventory);
        return holder;
    }

    public void setHolderSettings(boolean isCommand, boolean isOnlyPickup, String fileName) {
        StorageVaultHolder holder = (StorageVaultHolder) this.inventory.getHolder();
        holder.setIsCommand(isCommand);
        holder.setOnlyPickup(isOnlyPickup);
        holder.setFileName(fileName);
    }

    public void syncAndUpload(Player player, int vault_num) {
        CacheInventory cacheInventory = PlayerInv.getInstance().getCacheInventoryManager().get(player);
        if (cacheInventory == null) {
            return;
        }
        cacheInventory.uploadCache(type, vault_num);
    }

    public boolean hasError() {
        return error;
    }
}
