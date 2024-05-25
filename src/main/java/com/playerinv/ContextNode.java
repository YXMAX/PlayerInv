package com.playerinv;

import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static com.playerinv.PlayerInv.API;

public class ContextNode {

    public static void addPermissionWithContext_Large(Player p, int i){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        PermissionNode node = PermissionNode.builder("playerinv.large.inv." + i)
                .withContext(full_set)
                .build();
        user.data().add(node);
        API.getUserManager().saveUser(user);
    }

    public static void addPermissionWithContext_Medium(Player p, int i){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        PermissionNode node = PermissionNode.builder("playerinv.medium.inv." + i)
                .withContext(full_set)
                .build();
        user.data().add(node);
        API.getUserManager().saveUser(user);
    }
}
