package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.object.cache.CacheInventory;
import com.playerinv.object.container.InventoryContainer;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.util.message.MessageUtil.sendDebug;

public class CacheInventoryManager {

    private final HashMap<UUID, CacheInventory> cacheInventoryMap = new HashMap<>();

    public void put(Player player,CacheInventory cacheInventory){
        this.cacheInventoryMap.put(player.getUniqueId(),cacheInventory);
    }

    public void remove(Player player){
        this.cacheInventoryMap.remove(player.getUniqueId());
    }

    public void loadCacheInventory(Player player){
        PlayerInv.getScheduler().asyncScheduler().runDelayed(task -> {
            if(player.isOnline() && !this.containsKey(player)){
                this.put(player, new CacheInventory(player));
            }
        }, Duration.ofSeconds(3));
    }

    public void removeCacheInventory(Player player){
        CompletableFuture.runAsync(() -> {
            CacheInventory cache = this.cacheInventoryMap.get(player.getUniqueId());
            if(cache != null){
                cache.checkSyncFinished().whenComplete((result,error) -> {
                    this.cacheInventoryMap.remove(player.getUniqueId());
                    sendDebug("卸载玩家仓库缓存数据: " + player.getUniqueId());
                });
            }
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
        CacheInventory cacheInventory = this.cacheInventoryMap.get(player.getUniqueId());
        if(cacheInventory == null){
            return false;
        }
        return cacheInventory.isReady();
    }

    public boolean containsKey(Player player){
        return this.cacheInventoryMap.containsKey(player.getUniqueId());
    }

    public CacheInventory get(Player player){
        return this.cacheInventoryMap.get(player.getUniqueId());
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
            if(this.cacheInventoryMap.isEmpty()){
                return;
            }
            List<String> list = new ArrayList<>();
            for(CacheInventory cacheInventory : this.cacheInventoryMap.values()){
                list.addAll(cacheInventory.getAllInventorySQL());
            }
            if(!list.isEmpty()){
                JDBCUtil.updateVaultBatch(list);
            }
        });
    }

    public void resetCacheVaultTitle(Player player, int type, int num){
        InventoryContainer inventoryContainer = this.get(player).getInventoryContainer(type, num);
        if(inventoryContainer == null) return;
        inventoryContainer.resetVaultTitle(player);
    }

    public int getRemainSlot(Player player,int type,int num){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return type == 1 ? 54 : 27;
        }
        return cacheInventory.getRemain(type,num);
    }
}
