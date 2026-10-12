package com.playerinv.service.luckperms;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.object.temp.PlaceholderTemp;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.data.TemporaryNodeMergeStrategy;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.util.message.MessageUtil.sendDebug;

public class LuckpermsService {

    private LuckPerms luckperms;

    private boolean luckperms_givePermissions = false;

    private boolean luckperms_proxySupport = false;

    private List<String> proxy_server = new ArrayList<>();

    private ImmutableContextSet context = null;

    public void setAPI(LuckPerms luckperms) {
        luckperms_givePermissions = getInstance().getConfig().getBoolean("Luckperms-give-permissions");
        luckperms_proxySupport = getInstance().getConfig().getBoolean("Luckperms-proxy-support");
        proxy_server = getInstance().getConfig().getStringList("Sync-server");
        this.luckperms = luckperms;
        ImmutableContextSet.Builder con_set = ImmutableContextSet.builder();
        if(proxy_server.contains("global") || !luckperms_proxySupport){
            return;
        }
        for(String s : proxy_server){
            con_set.add("server", s);
        }
        context = con_set.build();
    }

    public boolean isEnabled(){
        return luckperms_givePermissions;
    }

    private enum PermissionStatus {

        NO_PERMISSION,

        PERMANENT_PERMISSION,

        EXPIRY_PERMISSION,
    }

    public User getUser(Player player) {
        UserManager userManager = luckperms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        if(user == null) {
            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());
            return userFuture.join();
        }
        return user;
    }

    public User getUser(UUID uuid) {
        UserManager userManager = luckperms.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);
        return userFuture.join();
    }

    public void loadExpiryMap(OfflinePlayer player){
        PlaceholderTemp large = new PlaceholderTemp();
        PlaceholderTemp medium = new PlaceholderTemp();
        User user = this.getUser(player.getUniqueId());
        Collection<PermissionNode> map = user.resolveInheritedNodes(NodeType.PERMISSION, user.getQueryOptions());
        List<PermissionNode> large_nodes = map.stream().filter(n -> n.getPermission().contains("playerinv.large.inv.")).collect(Collectors.toList());
        List<PermissionNode> medium_nodes = map.stream().filter(n -> n.getPermission().contains("playerinv.medium.inv.")).collect(Collectors.toList());
        if(large_nodes.stream().anyMatch(node -> node.getPermission().equals("playerinv.large.inv.*") || node.getPermission().equals("playerinv.admin")) || player.isOp()) {
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getLargeVaultAmount();i++){
                large.add(i,-99);
            }
        } else {
            for(PermissionNode node : large_nodes){
                int vault_num = Integer.parseInt(node.getPermission().split("\\.")[3]);
                if(node.hasExpiry()){
                    Instant time_instant = node.getExpiry();
                    Date currentTime = new Date();
                    Instant current_instant = currentTime.toInstant();
                    long day = ChronoUnit.DAYS.between(current_instant,time_instant);
                    large.add(vault_num,day);
                    continue;
                }
                large.add(vault_num,-99);
            }
        }
        if(medium_nodes.stream().anyMatch(node -> node.getPermission().equals("playerinv.medium.inv.*") || node.getPermission().equals("playerinv.admin")) || player.isOp()) {
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getMediumVaultAmount();i++){
                medium.add(i,-99);
            }
        } else {
            for(PermissionNode node : medium_nodes){
                int vault_num = Integer.parseInt(node.getPermission().split("\\.")[3]);
                if(node.hasExpiry()){
                    Instant time_instant = node.getExpiry();
                    Date currentTime = new Date();
                    Instant current_instant = currentTime.toInstant();
                    long day = ChronoUnit.DAYS.between(current_instant,time_instant);
                    medium.add(vault_num,day);
                    continue;
                }
                medium.add(vault_num,-99);
            }
        }
        PlayerInv.getInstance().getPlayerCacheManager().insertPlaceholders(player.getUniqueId().toString(),large,medium);
    }

    public boolean hasVaultPermission(Player player,String type,int num){
        User user = this.getUser(player);
        return user.resolveInheritedNodes(NodeType.PERMISSION,user.getQueryOptions()).stream().anyMatch(node -> node.getPermission().equals("playerinv." + type + ".inv." + num));
    }

    private PermissionNode getPermissionNode(int type, int vault_num, long day){
        String vault_type = "large";
        if(type == 2){
            vault_type = "medium";
        }
        if(day == 0){
            if(context == null){
                return PermissionNode.builder("playerinv." + vault_type + ".inv." + vault_num).build();
            }
            return PermissionNode.builder("playerinv." + vault_type + ".inv." + vault_num).withContext(context).build();
        } else {
            if(context == null){
                return PermissionNode.builder("playerinv." + vault_type + ".inv." + vault_num).expiry(Duration.ofDays(day)).build();
            }
            return PermissionNode.builder("playerinv." + vault_type + ".inv." + vault_num).withContext(context).expiry(Duration.ofDays(day)).build();
        }
    }

    private boolean addPermission(User user, PermissionNode node,Collection<PermissionNode> map,boolean ignoreExpiry,boolean save) {
        PermissionStatus status = PermissionStatus.NO_PERMISSION;
        PermissionNode result = null;
        Optional<PermissionNode> find = map.stream().filter(n -> n.getPermission().equalsIgnoreCase(node.getPermission())).findFirst();
        if(find.isPresent()){
            result = find.get();
            if(result.hasExpiry()){
                status = PermissionStatus.EXPIRY_PERMISSION;
            } else {
                status = PermissionStatus.PERMANENT_PERMISSION;
            }
        }
        switch(status){
            case NO_PERMISSION:
                user.data().add(node,TemporaryNodeMergeStrategy.ADD_NEW_DURATION_TO_EXISTING);
                if(save){
                    luckperms.getUserManager().saveUser(user);
                }
                break;
            case EXPIRY_PERMISSION:
                if(ignoreExpiry){
                    return false;
                }
                if(node.hasExpiry()){
                    user.data().add(node,TemporaryNodeMergeStrategy.ADD_NEW_DURATION_TO_EXISTING);
                } else {
                    user.data().remove(result);
                    user.data().add(node,TemporaryNodeMergeStrategy.REPLACE_EXISTING_IF_DURATION_LONGER);
                }
                if(save){
                    luckperms.getUserManager().saveUser(user);
                }
                break;
            case PERMANENT_PERMISSION:
                return false;
        }
        return true;
    }

    public boolean giveSpecificPermission(Player player, int type, int vault_num, long day) {
        PermissionNode node = this.getPermissionNode(type, vault_num, day);
        User user = this.getUser(player);
        Collection<PermissionNode> map = user.resolveInheritedNodes(NodeType.PERMISSION, user.getQueryOptions());
        boolean bool = this.addPermission(user, node, map, false, true);
        if(bool){
            PlayerInv.getScheduler().globalRegionalScheduler().run(task -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"luckperms sync");
            });
            PlayerInv.getInstance().getPlayerCacheManager().loadExpiryCache(player);
            return true;
        }
        return false;
    }

    public int giveLoopPermission(Player player, int type, long day) {
        User user = this.getUser(player);
        Collection<PermissionNode> map = user.resolveInheritedNodes(NodeType.PERMISSION, user.getQueryOptions());
        for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getVaultAmount(type);i++){
            PermissionNode node = this.getPermissionNode(type, i, day);
            if(this.addPermission(user,node,map,false,true)){
                PlayerInv.getScheduler().globalRegionalScheduler().run(task -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"luckperms sync");
                });
                PlayerInv.getInstance().getPlayerCacheManager().loadExpiryCache(player);
                return i;
            }
        }
        return 0;
    }

    public int[] appendPermissions(Player player,int type,int amount,long day,boolean ignoreExpiry) {
        User user = this.getUser(player);
        Collection<PermissionNode> map = user.resolveInheritedNodes(NodeType.PERMISSION, user.getQueryOptions());
        int[] result = new int[2];
        int start = -1;
        int end = -1;
        int inject = 0;
        for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getVaultAmount(type);i++){
            PermissionNode node = this.getPermissionNode(type, i, day);
            if(this.addPermission(user,node,map,ignoreExpiry,false)){
                if(start == -1) start = i;
                ConfigConstants.runCommandBySendVault(type,player,i);
                sendDebug("追加给予玩家 " + player.getName() + " 为类型[" + type + "]的仓库,编号: " + i + " / 天数: " + day);
                inject = inject + 1;
                if(inject == amount){
                    end = i;
                    break;
                }
            }
        }
        if(inject == 0){
            return null;
        }
        if(end == -1){
            end = PlayerInv.getInstance().getVaultManager().getVaultAmount(type);
        }
        luckperms.getUserManager().saveUser(user);
        PlayerInv.getScheduler().globalRegionalScheduler().run(task -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"luckperms sync");
        });
        result[0] = start;
        result[1] = end;
        PlayerInv.getInstance().getPlayerCacheManager().loadExpiryCache(player);
        return result;
    }
}
