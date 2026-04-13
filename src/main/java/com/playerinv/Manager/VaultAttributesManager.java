package com.playerinv.Manager;

import com.playerinv.Util.Object.Cache.VaultAttributes;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.jdbcUtil;
import static com.playerinv.Util.LoadUtil.*;

public class VaultAttributesManager {

    private HashMap<Player, VaultAttributes> vaultAttributes;

    public VaultAttributesManager() {
        this.vaultAttributes = new HashMap<>();
    }

    public void insert(Player player) {
        CompletableFuture.runAsync(() -> {
            this.vaultAttributes.put(player, new VaultAttributes(player,jdbcUtil.getVaultName(player.getUniqueId().toString())));
        });
    }

    public void remove(Player player) {
        this.vaultAttributes.remove(player);
    }

    public VaultAttributes get(Player player) {
        if(!this.vaultAttributes.containsKey(player)){
            this.vaultAttributes.put(player, new VaultAttributes(player,jdbcUtil.getVaultName(player.getUniqueId().toString())));
        }
        return this.vaultAttributes.get(player);
    }

    public String replaceVaultTitle(Player player,int type,int num) {
        VaultAttributes vaultAttributes = this.get(player);
        String attribute = vaultAttributes.getAttribute(type, num);
        if(customTitleReplace){
            if(attribute == null){
                if(type == 1){
                    attribute = default_large_vault_title;
                    if(attribute.contains("%vault_num%")){
                        attribute = attribute.replaceAll("%vault_num%", String.valueOf(num));
                    } else {
                        attribute = attribute + " " + String.valueOf(num);
                    }
                } else {
                    attribute = default_medium_vault_title;
                    if(attribute.contains("%vault_num%")){
                        attribute = attribute.replaceAll("%vault_num%", String.valueOf(num));
                    } else {
                        attribute = attribute + " " + String.valueOf(num);
                    }
                }
            }
            return attribute;
        } else {
            String custom = "";
            if(attribute != null){
                custom = attribute;
            }
            if(type == 1){
                attribute = default_large_vault_title;
                attribute = attribute.replaceAll("%vault_num%", String.valueOf(num));
            } else {
                attribute = default_medium_vault_title;
                attribute = attribute.replaceAll("%vault_num%", String.valueOf(num));
            }
            return attribute.replaceAll("%custom_title%", custom);
        }
    }

    public String replaceVaultName(Player player,int type,int num) {
        VaultAttributes vaultAttributes = this.get(player);
        String attribute = vaultAttributes.getAttribute(type, num);
        if(attribute == null){
            if(type == 1){
                attribute = default_large_vault_name.replaceAll("%vault_num%", String.valueOf(num));
            } else {
                attribute = default_medium_vault_name.replaceAll("%vault_num%", String.valueOf(num));
            }
        }
        return attribute;
    }
}
