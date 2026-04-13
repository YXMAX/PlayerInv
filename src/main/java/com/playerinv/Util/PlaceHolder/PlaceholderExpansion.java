package com.playerinv.Util.PlaceHolder;

import com.playerinv.PlayerInv;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LocaleUtil.placeholder_expiry;


public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private PlayerInv plugin;

    public PlaceholderExpansion(PlayerInv plugin){
        this.plugin = plugin;
    }


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
            return getVaultAmount(player);
        }
        if(identifier.startsWith("vault_large_expiry_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_large_expiry_", ""));
            return VaultExpiry_Large(player,i);
        }
        if(identifier.startsWith("vault_medium_expiry_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_medium_expiry_", ""));
            return VaultExpiry_Medium(player,i);
        }
        if(identifier.startsWith("vault_large_name_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_large_name_", ""));
            return getVaultName(player,1,i);
        }
        if(identifier.startsWith("vault_medium_name_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_medium_name_", ""));
            return getVaultName(player,2,i);
        }
        if(identifier.startsWith("vault_large_remain_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_large_remain_", ""));
            return getVaultRemain(player,1,i);
        }
        if(identifier.startsWith("vault_medium_remain_")){
            int i = Integer.parseInt(identifier.replaceAll("vault_medium_remain_", ""));
            return getVaultRemain(player,2,i);
        }
        return null;
    }

    private String getVaultName(OfflinePlayer player,int type,int num){
        if(player.isOnline()){
            Player p = (Player) player.getPlayer();
            return vaultAttributesManager.replaceVaultName(p,type,num);
        }
        return null;
    }

    private String getVaultRemain(OfflinePlayer player,int type,int num){
        if(player.isOnline()){
            Player p = (Player) player.getPlayer();
            if(cacheInventoryManager.get(p).getVaultStatistics(type,num)==null){
                if(type == 1){
                    return String.valueOf(54);
                } else {
                    return String.valueOf(27);
                }
            } else {
                return String.valueOf(cacheInventoryManager.get(p).getVaultStatistics(type, num).getRemain());
            }
        }
        return null;
    }

    private String getVaultAmount(OfflinePlayer player){
        return String.valueOf(0);
//        if(!Placeholder_VaultAmount.containsKey(player.getUniqueId().toString())){
//            int count = 0;
//            if(perms.playerHas(null,player,"playerinv.large.inv.*")){
//                count = count + largeVaultAmount;
//            } else {
//                for (int i = 1; i <= largeVaultAmount; i++) {
//                    if(i <= 10){
//                        if(perms.playerHas(null,player,"playerinv.large.inv." + i) || perms.playerHas(null,player,"playerinv.inv." + i)){
//                            count = count + 1;
//                        }
//                    } else {
//                        if(perms.playerHas(null,player,"playerinv.large.inv." + i)){
//                            count = count + 1;
//                        }
//                    }
//                }
//            }
//            if(perms.playerHas(null,player,"playerinv.medium.inv.*")){
//                count = count + mediumVaultAmount;
//            } else {
//                for (int i = 1; i <= mediumVaultAmount; i++) {
//                    if(i <= 15){
//                        if(perms.playerHas(null,player,"playerinv.medium.inv." + i) || perms.playerHas(null,player,"playerinv.inv." + i)){
//                            count = count + 1;
//                        }
//                    } else {
//                        if(perms.playerHas(null,player,"playerinv.medium.inv." + i)){
//                            count = count + 1;
//                        }
//                    }
//                }
//            }
//            if(perms.playerHas(null,player,"playerinv.inv.*")){
//                count = largeVaultAmount + mediumVaultAmount;
//            }
//            Placeholder_VaultAmount.put(player.getUniqueId().toString(),count);
//            return String.valueOf(count);
//        } else {
//            return String.valueOf(Placeholder_VaultAmount.get(player.getUniqueId().toString()));
//        }
    }

    private String VaultExpiry_Large(OfflinePlayer player, int num){
        if(player.isOp()){
            return placeholder_expiry(1);
        }
        if(!playerExpiryMap_Large.containsKey(player.getUniqueId().toString())){
            permissionManager.getLuckpermsService().loadExpiryMap(player);
        }
        return playerExpiryMap_Large.get(player.getUniqueId().toString()).getDisplay(num);
    }

    private String VaultExpiry_Medium(OfflinePlayer player, int num){
        if(player.isOp()){
            return placeholder_expiry(1);
        }
        if(playerExpiryMap_Medium.containsKey(player.getUniqueId().toString())){
            permissionManager.getLuckpermsService().loadExpiryMap(player);
        }
        return playerExpiryMap_Medium.get(player.getUniqueId().toString()).getDisplay(num);
    }
}
