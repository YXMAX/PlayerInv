package com.playerinv.manager;

import com.playerinv.enums.PermissionEnums;
import com.playerinv.PlayerInv;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.object.temp.PlaceholderTemp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import static com.playerinv.util.LocaleUtil.*;

public class PlayerCacheManager {

    private final HashMap<String,Integer> vaultAmountMap = new HashMap<>();

    private final HashMap<String, PlaceholderTemp> largePlaceholders = new HashMap<>();

    private final HashMap<String, PlaceholderTemp> mediumPlaceholders = new HashMap<>();

    public void startResetScheduler(){
        PlayerInv.getScheduler().asyncScheduler().runAtFixedRate(task -> {
            vaultAmountMap.clear();
            largePlaceholders.clear();
            mediumPlaceholders.clear();
        },Duration.ofMinutes(5),Duration.ofMinutes(5));
    }

    public void loadPlayerCache(Player player){
        this.loadExpiryCache(player);
        CompletableFuture.runAsync(() -> {
            this.runVaultCounter(player.getUniqueId().toString());
        });
    }

    public void removePlayerCache(Player player){
        this.largePlaceholders.remove(player.getUniqueId().toString());
        this.mediumPlaceholders.remove(player.getUniqueId().toString());
        this.vaultAmountMap.remove(player.getUniqueId().toString());
    }

    public int runVaultCounter(String uuid){
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player == null) return 0;
        int result = 0;
        if(PermissionManager.hasPermission(player, PermissionEnums.INV_ALL)){
            vaultAmountMap.put(uuid,PlayerInv.getInstance().getVaultManager().getTotalVaultAmount());
            return PlayerInv.getInstance().getVaultManager().getTotalVaultAmount();
        }
        if(PermissionManager.hasPermission(player,PermissionEnums.LARGE_ALL)){
            result = result + PlayerInv.getInstance().getVaultManager().getLargeVaultAmount();
        } else {
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getLargeVaultAmount();i++){
                if(PermissionManager.hasPermission(player,"playerinv.large.inv." + i)){
                    result = result + 1;
                }
            }
        }
        if(PermissionManager.hasPermission(player,PermissionEnums.MEDIUM_ALL)){
            result = result + PlayerInv.getInstance().getVaultManager().getMediumVaultAmount();
        } else {
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getMediumVaultAmount();i++){
                if(PermissionManager.hasPermission(player,"playerinv.medium.inv." + i)){
                    result = result + 1;
                }
            }
        }
        vaultAmountMap.put(uuid,result);
        return result;
    }

    public Integer getVaultAmount(String uuid){
        return vaultAmountMap.get(uuid);
    }

    public void loadExpiryCache(Player player){
        if(!PlayerInv.getInstance().getPermissionManager().isLuckpermsServiceEnabled()){
            return;
        }
        CompletableFuture.runAsync(() -> {
            PlayerInv.getInstance().getPermissionManager().getLuckpermsService().loadExpiryMap(player);
        });
    }

    public PlaceholderTemp getPlaceholders(OfflinePlayer player,int type){
        if(!largePlaceholders.containsKey(player.getUniqueId().toString()) || !mediumPlaceholders.containsKey(player.getUniqueId().toString())){
            PlayerInv.getInstance().getPermissionManager().getLuckpermsService().loadExpiryMap(player);
        }
        if(type == 1){
            return largePlaceholders.get(player.getUniqueId().toString());
        } else {
            return mediumPlaceholders.get(player.getUniqueId().toString());
        }
    }

    public String getPlaceholderDisplay(OfflinePlayer player,int type,int num){
        if(PlayerInv.getInstance().getPermissionManager().isLuckpermsServiceEnabled()){
            return this.getPlaceholders(player,type).getDisplay(num);
        }
        return getExpiryPlaceholderDisplay(1);
    }

    public void insertPlaceholders(String uuid,PlaceholderTemp large,PlaceholderTemp medium){
        largePlaceholders.put(uuid,large);
        mediumPlaceholders.put(uuid,medium);
    }
}
