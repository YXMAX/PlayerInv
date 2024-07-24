package com.playerinv.PlaceHolder;

import com.playerinv.PlayerInv;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.playerinv.LPVerify.CheckPermission.VaultExpiry_Large;
import static com.playerinv.LPVerify.CheckPermission.VaultExpiry_Medium;
import static com.playerinv.PlayerInv.Large_Amount;
import static com.playerinv.PlayerInv.Medium_Amount;
import static com.playerinv.PluginSet.getVault;


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
    public String onPlaceholderRequest(Player player, String identifier){
        if(identifier.equals("vault_amount")){
            return String.valueOf(getVault(player));
        }
        for(int i=1;i<=Large_Amount;i++){
            if(identifier.equals("vault_large_expiry_" + i)){
                return VaultExpiry_Large(player,i);
            }
        }
        for(int i=1;i<=Medium_Amount;i++){
            if(identifier.equals("vault_medium_expiry_" + i)){
                return VaultExpiry_Medium(player,i);
            }
        }
        return null;
    }
}
