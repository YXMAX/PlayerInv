package com.playerinv.LPVerify;

import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import static com.playerinv.LocaleUtil.placeholder_expiry;
import static com.playerinv.PlayerInv.*;

public class CheckPermission {

    public static String VaultExpiry_Large(Player player, int num){
        if(player.isOp()){
            return placeholder_expiry(1);
        }
        if(PlayerExpiryMap_Large.containsKey(player.getName() + ":" + num)){
            long day = PlayerExpiryMap_Large.get(player.getName() + ":" + num);
            return placeholder_expiry(2).replaceAll("%day_value%", String.valueOf(day));
        }
        Collection<Node> player_nodes = API.getUserManager().getUser(player.getUniqueId()).getDistinctNodes();
        for(Node node : player_nodes){
            if(node.getKey().equals("playerinv.large.inv." + num)){
                if(node.hasExpiry()){
                    Instant time_instant = node.getExpiry();
                    Date currentTime = new Date();
                    Instant current_instant = currentTime.toInstant();
                    long day = ChronoUnit.DAYS.between(current_instant,time_instant);
                    PlayerExpiryMap_Large.put(player.getName() + ":" + num,day);
                    return placeholder_expiry(2).replaceAll("%day_value%", String.valueOf(day));
                } else {
                    return placeholder_expiry(1);
                }
            }
        }
        return placeholder_expiry(3);
    }

    public static String VaultExpiry_Medium(Player player, int num){
        if(player.isOp()){
            return placeholder_expiry(1);
        }
        if(PlayerExpiryMap_Medium.containsKey(player.getName() + ":" + num)){
            long day = PlayerExpiryMap_Medium.get(player.getName() + ":" + num);
            return placeholder_expiry(2).replaceAll("%day_value%", String.valueOf(day));
        }
        Collection<Node> player_nodes = API.getUserManager().getUser(player.getUniqueId()).getDistinctNodes();
        for(Node node : player_nodes){
            if(node.getKey().equals("playerinv.medium.inv." + num)){
                if(node.hasExpiry()){
                    Instant time_instant = node.getExpiry();
                    Date currentTime = new Date();
                    Instant current_instant = currentTime.toInstant();
                    long day = ChronoUnit.DAYS.between(current_instant,time_instant);
                    PlayerExpiryMap_Medium.put(player.getName() + ":" + num,day);
                    return placeholder_expiry(2).replaceAll("%day_value%", String.valueOf(day));
                } else {
                    return placeholder_expiry(1);
                }
            }
        }
        return placeholder_expiry(3);
    }
}
