package com.playerinv.object.cache;


import com.playerinv.PlayerInv;
import com.playerinv.object.container.InventoryContainer;
import com.playerinv.service.serializer.InventorySerializer;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.playerinv.util.message.MessageUtil.sendDebug;

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

    public boolean isSyncFinished() {
        return sync.get();
    }

    public CompletableFuture<Void> getSyncFuture(){
        sendDebug("启动 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 数据同步等待调度器");
        CompletableFuture<Void> future = new CompletableFuture<>();
        PlayerInv.getScheduler().asyncScheduler().runAtFixedRate(task -> {
            if(this.sync.get()){
                sendDebug("位于 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 的调度器完成任务");
                future.complete(null);
                task.cancel();
            }
        },Duration.ofMillis(300),Duration.ofMillis(500));
        return future;
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
        this.inventoryContainer.setContents(inventory.getContents());
        this.uploadAsync();
    }

    public void uploadAsync(){
        this.sync.set(false);
        int current = this.uploadCount.incrementAndGet();
        PlayerInv.getScheduler().asyncScheduler().runDelayed(task -> {
            if(this.uploadCount.get() != current){
                task.cancel();
                return;
            }
            this.refreshInventory();
            JDBCUtil.updateVaultStringAsync(vaultType,uuid,this.inventoryContainer.getStrings(),vaultNum);
            this.sync.set(true);
        }, Duration.ofMillis(1250));
    }

    public void syncInventoryFromDatabase(){
        PlayerInv.getScheduler().asyncScheduler().runDelayed(task -> {
            String vaultString = JDBCUtil.getVaultString(this.vaultType, this.uuid, this.vaultNum);
            InventoryContainer inventoryContainer = InventorySerializer.deserializeVaultInventory(null,this.vaultType,this.uuid,this.vaultNum,vaultString);
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
        sendDebug("获取 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 的仓库数据SQL");
        return "update " + this.getVaultType() + " set inv='" + this.inventoryContainer.getStrings() + "' where uuid='" + this.uuid + "' and num=" + this.vaultNum;
    }

    public String takeUploadSQL(){
        this.uploadCount.incrementAndGet();
        this.sync.set(true);
        sendDebug("强制上传 " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum + " 的仓库数据");
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
            for(int i = 0; i< inventoryContainer.getSize(); i++){
                ItemStack stack = inventoryContainer.getItem(i);
                if(stack == null || stack.getType().equals(Material.AIR)){
                    continue;
                }
                remain = remain - 1;
            }
            sendDebug("仓库空闲格数和搜索缓存计算完毕: " + this.vaultType + "//" + this.uuid + ":" + this.vaultNum);
        });
    }

    public int getRemain() {
        return remain;
    }

    public int getVaultNum() {
        return this.vaultNum;
    }

    public int getType(){
        return this.vaultType;
    }
}
