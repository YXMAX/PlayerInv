package com.playerinv.Util.Object.Cache;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.NodeUtil.*;

public class VaultStatistics {

    private InventoryContainer inventoryContainer;

    private String uuid;

    private int vaultType;

    private int vaultNum;

    private int remain;

    public AtomicInteger count = new AtomicInteger(0);

    public VaultStatistics(String uuid, InventoryContainer inventoryContainer, int vaultType, int vaultNum, String locale) {
        this.uuid = uuid;
        this.inventoryContainer = inventoryContainer;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
        this.count.set(0);
        if(this.vaultType == 1){
            this.remain = 54;
        } else {
            this.remain = 27;
        }
        refreshInventory();
    }

    public VaultStatistics(String uuid,int vaultType,int vaultNum,String locale) {
        this.uuid = uuid;
        this.inventoryContainer = null;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
        this.count.set(0);
        if(this.vaultType == 1){
            this.remain = 54;
        } else {
            this.remain = 27;
        }
    }

    private void resetRemain(){
        if(this.vaultType == 1){
            this.remain = 54;
        } else {
            this.remain = 27;
        }
    }

    public void syncCacheAndUpload(Inventory inventory){
        if(this.inventoryContainer == null){
            this.inventoryContainer = new InventoryContainer();
        }
        this.inventoryContainer.setContents(inventory.getContents());
        int current = this.count.incrementAndGet();
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            if(this.count.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            jdbcUtil.updateVaultByCache(vaultType,uuid,this.inventoryContainer.getInventory(),vaultNum);
        }, Duration.ofMillis(1250));
    }

    public void uploadAsync(){
        int current = this.count.incrementAndGet();
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            if(this.count.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            jdbcUtil.updateVaultByCache(vaultType,uuid,this.inventoryContainer.getInventory(),vaultNum);
        }, Duration.ofMillis(1250));
    }

    public void syncInventoryFromDatabase(){
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            String vaultString = jdbcUtil.getVaultString(this.vaultType, this.uuid, this.vaultNum);
            Inventory inventory = inventoryFromBase64_Basic(vaultString);
            if(inventory == null){
                return;
            }
            this.inventoryContainer.getInventory().setContents(inventory.getContents());
            this.refreshInventory();
        }, Duration.ofMillis(100));
    }

    private String getVaultType(){
        return vaultType == 1 ? "vault_large" : "vault_medium";
    }

    public String getSQL(){
        return "update " + this.getVaultType() + " set inv='" + inventoryToBase64(this.inventoryContainer.getInventory()) + "' where uuid='" + this.uuid + "' and num=" + this.vaultNum;
    }

    public Inventory getInventory() {
        return inventoryContainer.getInventory();
    }

    public InventoryContainer getInventoryContainer() {
        return inventoryContainer;
    }

    public void refreshInventory(){
        CompletableFuture.runAsync(() -> {
            this.resetRemain();
            for(int i = 0; i< inventoryContainer.getInventory().getSize(); i++){
                ItemStack stack = inventoryContainer.getInventory().getItem(i);
                if(stack == null || stack.getType().equals(Material.AIR)){
                    continue;
                }
                remain = remain - 1;
            }
            sendLog("Remain-slots calculation complete: " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum);
        });
    }

    public int getRemain() {
        return remain;
    }
}
