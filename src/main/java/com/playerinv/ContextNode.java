package com.playerinv;

import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.util.Tristate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.playerinv.LocaleUtil.Vault_command_give_large_success;
import static com.playerinv.LocaleUtil.Vault_player_give_large_notice;
import static com.playerinv.PlayerInv.API;
import static com.playerinv.PlayerInv.plugin;
import static com.playerinv.PluginSet.color;

public class ContextNode {

    public static void addPermissionWithContext_Large(Player p, int i, long day){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        if(day != 0){
            long value = day * 24;
            PermissionNode node = PermissionNode.builder("playerinv.large.inv." + i)
                    .withContext(full_set)
                    .expiry(Duration.ofHours(value))
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        } else if(day == 0){
            PermissionNode node = PermissionNode.builder("playerinv.large.inv." + i)
                    .withContext(full_set)
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        }
    }

    public static void addPermissionWithContext_Medium(Player p, int i, long day){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        if(day != 0){
            long value = day * 24;
            PermissionNode node = PermissionNode.builder("playerinv.medium.inv." + i)
                    .withContext(full_set)
                    .expiry(Duration.ofHours(value))
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        } else if(day == 0){
            PermissionNode node = PermissionNode.builder("playerinv.medium.inv." + i)
                    .withContext(full_set)
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        }
    }

    public static void addPermission_Large(Player p, int i, long day){
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        if(day != 0){
            long value = day * 24;
            PermissionNode node = PermissionNode.builder("playerinv.large.inv." + i)
                    .expiry(Duration.ofHours(value))
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        } else if(day == 0){
            PermissionNode node = PermissionNode.builder("playerinv.large.inv." + i)
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        }
    }

    public static void addPermission_Medium(Player p, int i, long day){
        User user = API.getPlayerAdapter(Player.class).getUser(p);
        if(day != 0){
            long value = day * 24;
            PermissionNode node = PermissionNode.builder("playerinv.medium.inv." + i)
                    .expiry(Duration.ofHours(value))
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        } else if(day == 0){
            PermissionNode node = PermissionNode.builder("playerinv.medium.inv." + i)
                    .build();
            user.data().add(node);
            API.getUserManager().saveUser(user);
        }
    }
}
