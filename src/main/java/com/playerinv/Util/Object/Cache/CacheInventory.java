package com.playerinv.Util.Object.Cache;

import com.playerinv.Util.NodeUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.jdbcUtil;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.NodeUtil.*;

public class CacheInventory {

    private LinkedHashMap<Integer, VaultStatistics> large_map;

    private LinkedHashMap<Integer, VaultStatistics> medium_map;

    private String uuid;

    private String locale;

    private boolean ready;

    public CacheInventory(Player player) {
        this.uuid = player.getUniqueId().toString();
        scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
            this.locale = player.getLocale();
            sendLog("Got player locale: " + this.locale);
        }, null,15);
        CompletableFuture<Void> largeFuture = CompletableFuture.runAsync(() -> {
            LinkedHashMap<Integer, InventoryContainer> large = jdbcUtil.getFullLargeInventoryAsync(player);
            if (large == null) {
                sendError(player.getName() + " 's UUID: " + this.uuid);
                sendError(player.getName() + " 's get data failed! pls check the database connection!");
                return;
            }
            large_map = new LinkedHashMap<>();
            String locale = player.getLocale();
            if (!large.isEmpty()) {
                for (Map.Entry<Integer, InventoryContainer> entry : large.entrySet()) {
                    large_map.put(entry.getKey(), new VaultStatistics(uuid, entry.getValue(), 1, entry.getKey(), locale));
                }
            }
        });
        CompletableFuture<Void> mediumFuture = CompletableFuture.runAsync(() -> {
            LinkedHashMap<Integer, InventoryContainer> medium = jdbcUtil.getFullMediumInventoryAsync(player);
            if (medium == null) {
                sendError(player.getName() + " 's UUID: " + this.uuid);
                sendError(player.getName() + " 's get data failed! pls check the database connection!");
                return;
            }
            medium_map = new LinkedHashMap<>();
            String locale = player.getLocale();
            if (!medium.isEmpty()) {
                for (Map.Entry<Integer, InventoryContainer> entry : medium.entrySet()) {
                    medium_map.put(entry.getKey(), new VaultStatistics(uuid, entry.getValue(), 2, entry.getKey(), locale));
                }
            }
        });
        CompletableFuture.allOf(largeFuture, mediumFuture).thenRunAsync(() -> {
            this.ready = true;
        });
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isCacheBroken(){
        return large_map == null || medium_map == null;
    }

    public VaultStatistics getVaultStatistics(int type,int num){
        return type == 1 ? large_map.get(num) : medium_map.get(num);
    }

    public VaultStatistics getOrCreateVaultStats(int type,int num){
        if(type == 1){
            if(large_map.containsKey(num)){
                return large_map.get(num);
            }
            VaultStatistics stat = new VaultStatistics(uuid,type,num,locale);
            large_map.put(num,stat);
            return stat;
        } else {
            if(medium_map.containsKey(num)){
                return medium_map.get(num);
            }
            VaultStatistics stat = new VaultStatistics(uuid,type,num,locale);
            medium_map.put(num,stat);
            return stat;
        }
    }

    public int getRemain(int type, int num){
        switch(type){
            case 1:
                if(large_map.containsKey(num)){
                    return large_map.get(num).getRemain();
                }
                return 54;
            case 2:
                if(medium_map.containsKey(num)){
                    return medium_map.get(num).getRemain();
                }
                return 27;
        }
        return 0;
    }

    public LinkedHashMap<Integer, VaultStatistics> getLarge_map() {
        return large_map;
    }

    public LinkedHashMap<Integer, VaultStatistics> getMedium_map() {
        return medium_map;
    }

    public Inventory getInventory(int type,int num) {
        switch(type){
            case 2:
                if(medium_map.containsKey(num)){
                    return medium_map.get(num).getInventory();
                }
                return null;
            default:
            case 1:
                if(large_map.containsKey(num)){
                    return large_map.get(num).getInventory();
                }
                return null;
        }
    }

    public InventoryContainer getInventoryContainer(int type,int num) {
        switch(type){
            case 2:
                if(medium_map.containsKey(num)){
                    return medium_map.get(num).getInventoryContainer();
                }
                return null;
            default:
            case 1:
                if(large_map.containsKey(num)){
                    return large_map.get(num).getInventoryContainer();
                }
                return null;
        }
    }

    public void syncAndUpload(int type,int num,Inventory inventory){
        this.getOrCreateVaultStats(type,num).syncCacheAndUpload(inventory);
    }

    public void syncFromDatabase(int type,int num){
        this.getVaultStatistics(type,num).syncInventoryFromDatabase();
    }

    public List<String> getAllInventorySQL(){
        List<String> list = new ArrayList<>();
        for(VaultStatistics vaultStatistics : large_map.values()){
            list.add(vaultStatistics.getSQL());
        }
        for(VaultStatistics vaultStatistics : medium_map.values()){
            list.add(vaultStatistics.getSQL());
        }
        return list;
    }
}
