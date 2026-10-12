package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.object.attribute.VaultAttributes;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VaultAttributesManager {

    private final HashMap<UUID, VaultAttributes> vaultAttributes = new HashMap<>();

    private String defaultLargeVaultTitle;

    private String defaultMediumVaultTitle;

    private String defaultLargeVaultName;

    private String defaultMediumVaultName;

    public void reload(){
        defaultLargeVaultName = PlayerInv.getInstance().getLocaleConfig().getString("Vault_name.Large_default");
        defaultMediumVaultName = PlayerInv.getInstance().getLocaleConfig().getString("Vault_name.Medium_default");
        defaultLargeVaultTitle = PlayerInv.getInstance().getLocaleConfig().getString("Vault.Large_title");
        defaultMediumVaultTitle = PlayerInv.getInstance().getLocaleConfig().getString("Vault.Medium_title");
    }

    public CompletableFuture<Void> loadAttributes(Player player) {
        return CompletableFuture.runAsync(() -> {
            this.vaultAttributes.put(player.getUniqueId(), new VaultAttributes(player.getUniqueId(), JDBCUtil.getVaultName(player.getUniqueId().toString())));
        });
    }

    public void removeAttributes(Player player) {
        this.vaultAttributes.remove(player.getUniqueId());
    }

    public VaultAttributes get(OfflinePlayer player) {
        if(!this.vaultAttributes.containsKey(player.getUniqueId())){
            this.vaultAttributes.put(player.getUniqueId(), new VaultAttributes(player.getUniqueId(),JDBCUtil.getVaultName(player.getUniqueId().toString())));
        }
        return this.vaultAttributes.get(player.getUniqueId());
    }

    public VaultAttributes get(String uuid) {
        UUID result = UUID.fromString(uuid);
        if(!this.vaultAttributes.containsKey(result)){
            this.vaultAttributes.put(result, new VaultAttributes(result,JDBCUtil.getVaultName(result.toString())));
        }
        return this.vaultAttributes.get(result);
    }

    public String replaceVaultTitle(String uuid,int type,int num){
        VaultAttributes vaultAttributes = this.get(uuid);
        String attribute = vaultAttributes.getAttribute(type, num);
        if(attribute == null){
            if(type == 1){
                attribute = defaultLargeVaultTitle;
                if(attribute.contains("%vault_num%")){
                    attribute = attribute.replace("%vault_num%", String.valueOf(num));
                } else {
                    attribute = attribute + " " + String.valueOf(num);
                }
            } else {
                attribute = defaultMediumVaultTitle;
                if(attribute.contains("%vault_num%")){
                    attribute = attribute.replace("%vault_num%", String.valueOf(num));
                } else {
                    attribute = attribute + " " + String.valueOf(num);
                }
            }
        }
        return attribute;
    }

    public String replaceVaultTitle(OfflinePlayer player,int type,int num) {
        return this.replaceVaultTitle(player.getUniqueId().toString(),type,num);
    }

    public String replaceVaultName(Player player,int type,int num) {
        VaultAttributes vaultAttributes = this.get(player);
        String attribute = vaultAttributes.getAttribute(type, num);
        if(attribute == null){
            if(type == 1){
                attribute = defaultLargeVaultName.replace("%vault_num%", String.valueOf(num));
            } else {
                attribute = defaultMediumVaultName.replace("%vault_num%", String.valueOf(num));
            }
        }
        return attribute;
    }

    public String replaceVaultMenuName(Player player,String name,int type,int num){
        VaultAttributes vaultAttributes = this.get(player);
        String attribute = vaultAttributes.getAttribute(type, num);
        if(attribute == null || !ConfigConstants.FUNCTION_CHANGE_VAULT_ATTRIBUTES){
            if(type == 1){
                attribute = name.replace("%vault_num%", String.valueOf(num));
            } else {
                attribute = name.replace("%vault_num%", String.valueOf(num));
            }
        } else {
            if(type == 1){
                attribute = attribute.replace("%vault_num%", String.valueOf(num));
            } else {
                attribute = attribute.replace("%vault_num%", String.valueOf(num));
            }
        }
        return attribute;
    }
}
