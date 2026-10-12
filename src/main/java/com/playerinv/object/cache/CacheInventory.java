package com.playerinv.object.cache;

import com.playerinv.object.container.InventoryContainer;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.util.message.MessageUtil.sendDebug;
import static com.playerinv.util.NodeUtil.*;

public class CacheInventory {

    private LinkedHashMap<Integer, VaultStatistics> large_map;

    private LinkedHashMap<Integer, VaultStatistics> medium_map;

    private String uuid;

    private String locale;

    private boolean ready;

    public CacheInventory(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.locale = player.getLocale();
        sendDebug("获取到玩家语言: " + this.locale);
        CompletableFuture<Void> largeFuture = CompletableFuture.runAsync(() -> {
            this.large_map = JDBCUtil.getVaultStatisticsCache(player,this.locale,1);
            if (this.large_map == null) {
                sendError("玩家 " + player.getName() + " 的UUID: " + this.uuid);
                sendError("玩家 " + player.getName() + " 的大型仓库数据获取失败! 请检查数据库连接以及是否可用");
            }
        });
        CompletableFuture<Void> mediumFuture = CompletableFuture.runAsync(() -> {
            this.medium_map = JDBCUtil.getVaultStatisticsCache(player,this.locale,2);
            if (this.medium_map == null) {
                sendError("玩家 " + player.getName() + " 的UUID: " + this.uuid);
                sendError("玩家 " + player.getName() + " 的中型仓库数据获取失败! 请检查数据库连接以及是否可用");
            }
        });
        CompletableFuture.allOf(largeFuture, mediumFuture).thenRunAsync(() -> {
            this.ready = true;
        });
    }

    public CompletableFuture<Void> checkSyncFinished(){
        sendDebug("检查玩家 " + this.uuid + " 的仓库数据是否完成同步");
        List<CompletableFuture<Void>> list = new ArrayList<>();
        if(this.large_map != null){
            for(VaultStatistics large : this.large_map.values()){
                if(!large.isSyncFinished()){
                    list.add(large.getSyncFuture());
                }
            }
        }
        if(this.medium_map != null){
            for(VaultStatistics medium : this.medium_map.values()){
                if(!medium.isSyncFinished()){
                    list.add(medium.getSyncFuture());
                }
            }
        }
        return CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
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

    public void syncContentsAndUpload(int type, int num, Inventory inventory){
        this.getOrCreateVaultStats(type,num).syncContentsAndUpload(inventory);
    }

    public void uploadCache(int type,int num){
        this.getOrCreateVaultStats(type,num).uploadAsync();
    }

    public void syncFromDatabase(int type,int num){
        this.getVaultStatistics(type,num).syncInventoryFromDatabase();
    }

    public void insertInventoryContainer(int type, int num, InventoryContainer container){
        this.getOrCreateVaultStats(type,num).setInventoryContainer(container);
    }

    public List<String> getAllInventorySQL(){
        List<String> list = new ArrayList<>();
        for(VaultStatistics vaultStatistics : large_map.values()){
            String sql = vaultStatistics.getSQL();
            if(sql != null){
                list.add(sql);
            }
        }
        for(VaultStatistics vaultStatistics : medium_map.values()){
            String sql = vaultStatistics.getSQL();
            if(sql != null){
                list.add(sql);
            }
        }
        return list;
    }
}
