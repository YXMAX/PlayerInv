package com.playerinv.Util.Object.Cache;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.NodeUtil.*;

public class VaultStatistics {

    private InventoryContainer inventoryContainer;

    private String locale;

    private String uuid;

    private int vaultType;

    private int vaultNum;

    private int remain;

    public AtomicInteger uploadCount = new AtomicInteger(0);

    public AtomicBoolean sync = new AtomicBoolean(true);

    public VaultStatistics(String uuid, InventoryContainer inventoryContainer, int vaultType, int vaultNum, String locale) {
        this.uuid = uuid;
        this.inventoryContainer = inventoryContainer;
        this.locale = locale;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
        this.uploadCount.set(0);
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
        this.locale = locale;
        this.vaultType = vaultType;
        this.vaultNum = vaultNum;
        this.uploadCount.set(0);
        if(this.vaultType == 1){
            this.remain = 54;
        } else {
            this.remain = 27;
        }
    }

    public void setInventoryContainer(InventoryContainer inventoryContainer) {
        this.inventoryContainer = inventoryContainer;
    }

    private void resetRemain(){
        if(this.vaultType == 1){
            this.remain = 54;
        } else {
            this.remain = 27;
        }
    }

    public void syncContentsAndUpload(Inventory inventory){
        if(this.inventoryContainer == null){
            this.inventoryContainer = new InventoryContainer(this.vaultType,this.vaultNum,this.uuid,inventory);
        }
        if(this.inventoryContainer.isNull()){
            this.inventoryContainer.insertInventory(this.vaultNum,this.uuid,inventory);
        }
        this.sync.set(false);
        this.inventoryContainer.setContents(inventory.getContents());
        int current = this.uploadCount.incrementAndGet();
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            if(this.uploadCount.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            jdbcUtil.updateVaultByCache(vaultType,uuid,this.inventoryContainer.getStrings(),vaultNum);
            this.sync.set(true);
        }, Duration.ofMillis(1250));
    }

    public void uploadCache(){
        this.sync.set(false);
        int current = this.uploadCount.incrementAndGet();
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            if(this.uploadCount.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            jdbcUtil.updateVaultByCache(vaultType,uuid,this.inventoryContainer.getStrings(),vaultNum);
            this.sync.set(true);
        }, Duration.ofMillis(1250));
    }

    public void uploadAsync(){
        this.sync.set(false);
        int current = this.uploadCount.incrementAndGet();
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            if(this.uploadCount.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            jdbcUtil.updateVaultByCache(vaultType,uuid,this.inventoryContainer.getStrings(),vaultNum);
            this.sync.set(true);
        }, Duration.ofMillis(1250));
    }

    public void syncInventoryFromDatabase(){
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            String vaultString = jdbcUtil.getVaultString(this.vaultType, this.uuid, this.vaultNum);
            InventoryContainer inventoryContainer = inventoryFromBase64_Cache(vaultString, this.vaultType, this.vaultNum, Bukkit.getPlayer(UUID.fromString(this.uuid)));
            if(inventoryContainer == null){
                return;
            }
            this.inventoryContainer = inventoryContainer;
            this.refreshInventory();
        }, Duration.ofMillis(100));
    }

    private String getVaultType(){
        return vaultType == 1 ? "vault_large" : "vault_medium";
    }

    public String getSQL(){
        if(this.sync.get()){
            return null;
        }
        sendLog("获取 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 的仓库数据SQL");
        return "update " + this.getVaultType() + " set inv='" + this.inventoryContainer.getStrings() + "' where uuid='" + this.uuid + "' and num=" + this.vaultNum;
    }

    public String takeUploadSQL(){
        this.uploadCount.incrementAndGet();
        this.sync.set(true);
        sendLog("强制上传 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 的仓库数据");
        return "update " + this.getVaultType() + " set inv='" + this.inventoryContainer.getStrings() + "' where uuid='" + this.uuid + "' and num=" + this.vaultNum;
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
            sendLog("仓库空闲格数计算完毕: " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum);
        });
    }

    public int getRemain() {
        return remain;
    }
}
