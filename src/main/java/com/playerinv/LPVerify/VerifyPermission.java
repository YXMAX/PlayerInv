package com.playerinv.LPVerify;

import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.playerinv.ContextNode.*;
import static com.playerinv.LocaleUtil.*;
import static com.playerinv.PlayerInv.API;
import static com.playerinv.PlayerInv.Large_Amount;
import static com.playerinv.PluginSet.color;

public class VerifyPermission {

    private static String prefix = PluginSet.prefix();

    public static Boolean verifyGivePermissions(Player player, int i, long day, CommandSender commandSender,int type){
        String vault_type = null;
        if(type == 1){
            vault_type = "large";
        } else {
            vault_type = "medium";
        }
        User user = API.getPlayerAdapter(Player.class).getUser(player);
        PermissionNode expiry_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .expiry(Duration.ofSeconds(1))
                .build();
        PermissionNode permanent_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .build();
        if(i <= 10 && player.hasPermission("playerinv.inv." + i) && type == 1){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(i <= 15 && player.hasPermission("playerinv.inv." + (i+10)) && type == 2){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(user.data().contains(permanent_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        } else if(user.data().contains(expiry_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Collection<Node> collection = user.data().toCollection();
            Iterator iterator = collection.iterator();
            while(iterator.hasNext()){
                Node next_node = (Node) iterator.next();
                if(next_node.getKey().equals("playerinv." + vault_type + ".inv." + i)){
                    Duration duration = next_node.getExpiryDuration();
                    Duration new_duration = duration.plusDays(day);
                    PermissionNode new_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                            .expiry(new_duration)
                            .build();
                    user.data().remove(next_node);
                    user.data().add(new_node);
                    API.getUserManager().saveUser(user);
                    if(type == 1){
                        commandSender.sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),new_duration.toDays())));
                    } else {
                        commandSender.sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),new_duration.toDays())));
                    }
                    return true;
                }
            }
        } else {
            if(type == 1){
                addPermission_Large(player,i,day);
                commandSender.sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),day)));
            } else {
                addPermission_Medium(player,i,day);
                commandSender.sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),day)));
            }
        }
        return true;
    }

    public static Boolean verifyGivePermissions_Context(Player player, int i, long day, CommandSender commandSender,int type){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        String vault_type = null;
        if(type == 1){
            vault_type = "large";
        } else {
            vault_type = "medium";
        }
        User user = API.getPlayerAdapter(Player.class).getUser(player);
        PermissionNode expiry_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .withContext(full_set)
                .expiry(Duration.ofSeconds(1))
                .build();
        PermissionNode permanent_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .withContext(full_set)
                .build();
        if(i <= 10 && player.hasPermission("playerinv.inv." + i) && type == 1){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(i <= 15 && player.hasPermission("playerinv.inv." + (i+10)) && type == 2){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(user.data().contains(permanent_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            commandSender.sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        } else if(user.data().contains(expiry_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Collection<Node> collection = user.data().toCollection();
            Iterator iterator = collection.iterator();
            while(iterator.hasNext()){
                Node next_node = (Node) iterator.next();
                if(next_node.getKey().equals("playerinv." + vault_type + ".inv." + i)){
                    Duration duration = next_node.getExpiryDuration();
                    Duration new_duration = duration.plusDays(day);
                    PermissionNode new_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                            .expiry(new_duration)
                            .build();
                    user.data().remove(next_node);
                    user.data().add(new_node);
                    API.getUserManager().saveUser(user);
                    if(type == 1){
                        commandSender.sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),new_duration.toDays())));
                    } else {
                        commandSender.sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),new_duration.toDays())));
                    }
                    return true;
                }
            }
        } else {
            if(type == 1){
                addPermissionWithContext_Large(player,i,day);
                commandSender.sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),day)));
            } else {
                addPermissionWithContext_Medium(player,i,day);
                commandSender.sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),day)));
            }
        }
        return true;
    }

    public static Boolean verifyGivePermissions_Console(Player player, int i, long day,int type){
        String vault_type = null;
        if(type == 1){
            vault_type = "large";
        } else {
            vault_type = "medium";
        }
        User user = API.getPlayerAdapter(Player.class).getUser(player);
        PermissionNode expiry_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .expiry(Duration.ofSeconds(1))
                .build();
        PermissionNode permanent_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .build();
        if(i <= 10 && player.hasPermission("playerinv.inv." + i) && type == 1){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(i <= 15 && player.hasPermission("playerinv.inv." + (i+10)) && type == 2){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(user.data().contains(permanent_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        } else if(user.data().contains(expiry_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Collection<Node> collection = user.data().toCollection();
            Iterator iterator = collection.iterator();
            while(iterator.hasNext()){
                Node next_node = (Node) iterator.next();
                if(next_node.getKey().equals("playerinv." + vault_type + ".inv." + i)){
                    Duration duration = next_node.getExpiryDuration();
                    Duration new_duration = duration.plusDays(day);
                    PermissionNode new_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                            .expiry(new_duration)
                            .build();
                    user.data().remove(next_node);
                    user.data().add(new_node);
                    API.getUserManager().saveUser(user);
                    if(type == 1){
                        Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),new_duration.toDays())));
                    } else {
                        Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),new_duration.toDays())));
                    }
                    return true;
                }
            }
        } else {
            if(type == 1){
                addPermission_Large(player,i,day);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),day)));
            } else {
                addPermission_Medium(player,i,day);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),day)));
            }
        }
        return true;
    }

    public static Boolean verifyGivePermissions_Context_Console(Player player, int i, long day,int type){
        List<String> server_list = PlayerInv.plugin.getConfig().getStringList("Sync-server");
        ImmutableContextSet.Builder set1 = ImmutableContextSet.builder();
        for(String s : server_list){
            set1.add("server", s);
        }
        ImmutableContextSet full_set = set1.build();
        String vault_type = null;
        if(type == 1){
            vault_type = "large";
        } else {
            vault_type = "medium";
        }
        User user = API.getPlayerAdapter(Player.class).getUser(player);
        PermissionNode expiry_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .withContext(full_set)
                .expiry(Duration.ofSeconds(1))
                .build();
        PermissionNode permanent_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                .withContext(full_set)
                .build();
        if(i <= 10 && player.hasPermission("playerinv.inv." + i) && type == 1){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(i <= 15 && player.hasPermission("playerinv.inv." + (i+10)) && type == 2){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        }
        if(user.data().contains(permanent_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
            return true;
        } else if(user.data().contains(expiry_node, NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE).asBoolean()){
            Collection<Node> collection = user.data().toCollection();
            Iterator iterator = collection.iterator();
            while(iterator.hasNext()){
                Node next_node = (Node) iterator.next();
                if(next_node.getKey().equals("playerinv." + vault_type + ".inv." + i)){
                    Duration duration = next_node.getExpiryDuration();
                    Duration new_duration = duration.plusDays(day);
                    PermissionNode new_node = PermissionNode.builder("playerinv." + vault_type + ".inv." + i)
                            .expiry(new_duration)
                            .build();
                    user.data().remove(next_node);
                    user.data().add(new_node);
                    API.getUserManager().saveUser(user);
                    if(type == 1){
                        Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),new_duration.toDays())));
                    } else {
                        Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),new_duration.toDays())));
                        player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),new_duration.toDays())));
                    }
                    return true;
                }
            }
        } else {
            if(type == 1){
                addPermissionWithContext_Large(player,i,day);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_large_notice(String.valueOf(i),day)));
            } else {
                addPermissionWithContext_Medium(player,i,day);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, String.valueOf(i),day)));
                player.sendMessage(color(prefix + Vault_player_give_medium_notice(String.valueOf(i),day)));
            }
        }
        return true;
    }
}
