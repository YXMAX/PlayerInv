package com.playerinv.service.papi;

import com.playerinv.PlayerInv;
import com.playerinv.object.cache.CacheInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.playerinv.util.LocaleUtil.getExpiryPlaceholderDisplay;


public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "playerinv";
    }

    @Override
    public @NotNull String getAuthor() {
        return "YXMAX";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if(identifier.equals("vault_amount")){
            return this.getVaultAmount(player);
        }
        if(identifier.startsWith("vault_large_expiry_")){
            int i = Integer.parseInt(identifier.replace("vault_large_expiry_", ""));
            return this.getLargeVaultExpiry(player,i);
        }
        if(identifier.startsWith("vault_medium_expiry_")){
            int i = Integer.parseInt(identifier.replace("vault_medium_expiry_", ""));
            return this.getMediumVaultExpiry(player,i);
        }
        if(identifier.startsWith("vault_large_name_")){
            int i = Integer.parseInt(identifier.replace("vault_large_name_", ""));
            return this.getVaultName(player,1,i);
        }
        if(identifier.startsWith("vault_medium_name_")){
            int i = Integer.parseInt(identifier.replace("vault_medium_name_", ""));
            return this.getVaultName(player,2,i);
        }
        if(identifier.startsWith("vault_large_remain_")){
            int i = Integer.parseInt(identifier.replace("vault_large_remain_", ""));
            return this.getVaultRemain(player,1,i);
        }
        if(identifier.startsWith("vault_medium_remain_")){
            int i = Integer.parseInt(identifier.replace("vault_medium_remain_", ""));
            return this.getVaultRemain(player,2,i);
        }
        return null;
    }

    private String getVaultName(OfflinePlayer player,int type,int num){
        if(player.isOnline()){
            Player p = (Player) player.getPlayer();
            return PlayerInv.getInstance().getVaultAttributesManager().replaceVaultName(p,type,num);
        }
        return null;
    }

    private String getVaultRemain(OfflinePlayer player,int type,int num){
        if(player.isOnline()){
            Player p = (Player) player.getPlayer();
            CacheInventory cache = PlayerInv.getInstance().getCacheInventoryManager().get(p);
            if(cache == null || cache.getVaultStatistics(type,num)==null){
                if(type == 1){
                    return String.valueOf(54);
                } else {
                    return String.valueOf(27);
                }
            } else {
                return String.valueOf(cache.getVaultStatistics(type, num).getRemain());
            }
        }
        return null;
    }

    private String getVaultAmount(OfflinePlayer player){
        Integer num = PlayerInv.getInstance().getPlayerCacheManager().getVaultAmount(player.getUniqueId().toString());
        if(num == null){
            return String.valueOf(PlayerInv.getInstance().getPlayerCacheManager().runVaultCounter(player.getUniqueId().toString()));
        }
        return num.toString();
    }

    private String getLargeVaultExpiry(OfflinePlayer player, int num){
        if(player.isOp()){
            return getExpiryPlaceholderDisplay(1);
        }
        return PlayerInv.getInstance().getPlayerCacheManager().getPlaceholders(player,1).getDisplay(num);
    }

    private String getMediumVaultExpiry(OfflinePlayer player, int num){
        if(player.isOp()){
            return getExpiryPlaceholderDisplay(1);
        }
        return PlayerInv.getInstance().getPlayerCacheManager().getPlaceholders(player,2).getDisplay(num);
    }
}
