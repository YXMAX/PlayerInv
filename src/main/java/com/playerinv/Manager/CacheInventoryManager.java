package com.playerinv.Manager;

import com.playerinv.Util.Object.Cache.CacheInventory;
import com.playerinv.Util.Object.Cache.InventoryContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.jdbcUtil;

public class CacheInventoryManager {

    public HashMap<UUID, CacheInventory> CacheInventoryMap;

    public CacheInventoryManager() {
        this.CacheInventoryMap = new HashMap<>();
    }

    public void put(Player player,CacheInventory cacheInventory){
        this.CacheInventoryMap.put(player.getUniqueId(),cacheInventory);
    }

    public void remove(Player player){
        this.CacheInventoryMap.remove(player.getUniqueId());
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
        if(!this.CacheInventoryMap.containsKey(player.getUniqueId())){
            CacheInventoryMap.put(player.getUniqueId(),new CacheInventory(player));
            return null;
        }
        return this.CacheInventoryMap.get(player.getUniqueId());
    }

    public void syncInventoryAndUpload(Player player, int type, int num, Inventory inventory){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.syncAndUpload(type, num, inventory);
    }

    public void syncInventoryFromDatabase(Player player, int type, int num){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null){
            return;
        }
        cacheInventory.syncFromDatabase(type, num);
    }

    public InventoryContainer getInventoryContainer(Player player, int type, int num){
        CacheInventory cacheInventory = this.get(player);
        if(cacheInventory == null) return new InventoryContainer(true);
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
