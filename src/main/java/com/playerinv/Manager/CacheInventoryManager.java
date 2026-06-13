package com.playerinv.Manager;

import com.playerinv.Util.Object.Cache.CacheInventory;
import com.playerinv.Util.Object.Cache.InventoryContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.jdbcUtil;
import static com.playerinv.PlayerInv.operationManager;
import static com.playerinv.Util.NodeUtil.sendLog;

public class CacheInventoryManager {

    private final HashMap<UUID, CacheInventory> CacheInventoryMap = new HashMap<>();

    public void put(Player player,CacheInventory cacheInventory){
        this.CacheInventoryMap.put(player.getUniqueId(),cacheInventory);
    }

    public void remove(Player player){
        this.CacheInventoryMap.remove(player.getUniqueId());
    }

    public void flushAndRemove(Player player){
        CompletableFuture.runAsync(() -> {
            CacheInventory cache = this.CacheInventoryMap.get(player.getUniqueId());
            if(cache != null){
                List<String> sqls = cache.getPendingUploadSQLs();
                if(!sqls.isEmpty()){
                    jdbcUtil.updateVaultBatch(sqls).thenRun(() -> {
                        operationManager.sendPlayerSavedSignal(player.getUniqueId().toString());
                    });
                }
            }
            this.CacheInventoryMap.remove(player.getUniqueId());
            sendLog("卸载玩家仓库数据缓存完成");
        });
    }

    public boolean isCacheBroken(Player player){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return true;
        }
        return cacheInventory.isCacheBroken();
    }

    public boolean isCacheReady(Player player){
        if(this.CacheInventoryMap.containsKey(player.getUniqueId())){
            return this.CacheInventoryMap.get(player.getUniqueId()).isReady();
        }
        return false;
    }

    public boolean containsKey(Player player){
        return this.CacheInventoryMap.containsKey(player.getUniqueId());
    }

    public CacheInventory get(Player player){
        return this.CacheInventoryMap.get(player.getUniqueId());
    }

    public void syncVaultContentsAndUpload(Player player, int type, int num, Inventory inventory){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.syncContentsAndUpload(type, num, inventory);
    }

    public void syncInventoryFromDatabase(Player player, int type, int num){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.syncFromDatabase(type, num);
    }

    public void insertInventoryContainer(Player player, int type, int num, InventoryContainer container){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.insertInventoryContainer(type, num, container);
    }

    public InventoryContainer getInventoryContainer(Player player, int type, int num){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null) return new InventoryContainer(type,true);
        return cacheInventory.getInventoryContainer(type,num);
    }

    public CompletableFuture<Void> uploadAll(){
        return CompletableFuture.runAsync(() -> {
            if(this.CacheInventoryMap.isEmpty()){
                return;
            }
            List<String> list = new ArrayList<>();
            for(CacheInventory cacheInventory : this.CacheInventoryMap.values()){
                list.addAll(cacheInventory.getAllInventorySQL());
            }
            if(!list.isEmpty()){
                jdbcUtil.updateVaultBatch(list);
            }
        });
    }
}
