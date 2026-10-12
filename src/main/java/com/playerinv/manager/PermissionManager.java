package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.constants.DependencyConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.service.luckperms.LuckpermsService;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.playerinv.util.LocaleUtil.*;
import static com.playerinv.util.NodeUtil.*;

public class PermissionManager {

    private Permission perms;

    private LuckpermsService luckpermsService;

    public void initVaultDepend(Permission perm) {
        this.perms = perm;
    }

    public void initLuckpermService() {
        luckpermsService = new LuckpermsService();
    }

    public LuckpermsService getLuckpermsService() {
        return luckpermsService;
    }

    public boolean isLuckpermsServiceEnabled(){
        return luckpermsService != null && DependencyConstants.LUCKPERMS_ENABLED && luckpermsService.isEnabled();
    }

    public Permission getVaultPermissionService(){
        return perms;
    }

    private boolean hasAdminPermission(Player player){
        return player.isOp() || hasPermission(player,PermissionEnums.COMMAND_ADMIN) || hasPermission(player,PermissionEnums.INV_ALL);
    }

    public void giveCertainPermission(Player target, int type, int num, long expire, CommandSender sender){
        if(this.hasAdminPermission(target)){
            sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
            return;
        }
        if(this.isLuckpermsServiceEnabled()){
            long day = 0;
            if(expire > 0){
                day = expire;
            }
            if(luckpermsService.giveSpecificPermission(target,type,num,day)){
                if(type == 1){
                    sendMessages(sender, LocaleEnums.Vault_command_give_large_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                    sendMessages(target,LocaleEnums.Vault_player_give_large_notice,"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                    ConfigConstants.runCommandBySendVault(1,target,num);
                } else {
                    sendMessages(sender, LocaleEnums.Vault_command_give_medium_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                    sendMessages(target,LocaleEnums.Vault_player_give_medium_notice,"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                    ConfigConstants.runCommandBySendVault(2,target,num);
                }
                return;
            }
            sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
            return;
        }
        if(type == 1){
            if(!hasVaultPermission(target,1,num)){
                perms.playerAdd(null,target,"playerinv.large.inv." + num);
                sendMessages(sender, LocaleEnums.Vault_command_give_large_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                sendMessages(target,LocaleEnums.Vault_player_give_large_notice,"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                ConfigConstants.runCommandBySendVault(1,target,num);
                return;
            }
        } else {
            if(!hasVaultPermission(target,2,num)){
                perms.playerAdd(null,target,"playerinv.medium.inv." + num);
                sendMessages(sender, LocaleEnums.Vault_command_give_medium_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                sendMessages(target,LocaleEnums.Vault_player_give_medium_notice,"%vault_num%",String.valueOf(num),"%expiry_time%", getExpiryDisplay(0));
                ConfigConstants.runCommandBySendVault(2,target,num);
                return;
            }
        }
        sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
    }

    private String getVaultTypeString(int type){
        if(type == 1){
            return "large";
        }
        return "medium";
    }

    public void appendPermissions(Player target,int type,int amount,long expire,CommandSender commandSender,boolean ignoreExpiry){
        if(this.hasAdminPermission(target)){
            sendMessages(commandSender,LocaleEnums.Vault_command_append_full);
            return;
        }
        if(this.isLuckpermsServiceEnabled()){
            long day = 0;
            if(expire > 0){
                day = expire;
            }
            int[] ints = luckpermsService.appendPermissions(target, type, amount, day, ignoreExpiry);
            if(ints == null){
                sendMessages(commandSender,LocaleEnums.Vault_command_append_full);
                return;
            }
            if(type == 1){
                sendMessages(commandSender, LocaleEnums.Vault_command_append_large_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", getExpiryDisplay(day),"%before_append%",String.valueOf(ints[0]-1));
                sendMessages(target, LocaleEnums.Vault_player_append_large_notice, "%vault_num_start%", String.valueOf(ints[0]),"%vault_num_end%",String.valueOf(ints[1]),"%expiry_time%", getExpiryDisplay(day));
            } else {
                sendMessages(commandSender, LocaleEnums.Vault_command_append_medium_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", getExpiryDisplay(day),"%before_append%",String.valueOf(ints[0]-1));
                sendMessages(target, LocaleEnums.Vault_player_append_medium_notice, "%vault_num_start%", String.valueOf(ints[0]),"%vault_num_end%",String.valueOf(ints[1]),"%expiry_time%", getExpiryDisplay(day));
            }
            return;
        }
        int start = -1;
        int end = -1;
        int inject = 0;
        for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getVaultAmount(type);i++){
            if(!hasVaultPermission(target,type,i)){
                if(start == -1) start = i;
                perms.playerAdd(null,target,"playerinv." + this.getVaultTypeString(type) + ".inv." + i);
                ConfigConstants.runCommandBySendVault(type,target,i);
                inject = inject + 1;
                if(inject == amount){
                    end = i;
                    break;
                }
            }
        }
        if(inject == 0){
            sendMessages(commandSender,LocaleEnums.Vault_command_append_full);
            return;
        }
        if(end == -1){
            end = PlayerInv.getInstance().getVaultManager().getVaultAmount(type);
        }
        if(type == 1){
            sendMessages(commandSender, LocaleEnums.Vault_command_append_large_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", getExpiryDisplay(0),"%before_append%",String.valueOf(start-1));
            sendMessages(target, LocaleEnums.Vault_player_append_large_notice, "%vault_num_start%", String.valueOf(start),"%vault_num_end%",String.valueOf(end),"%expiry_time%", getExpiryDisplay(0));
        } else {
            sendMessages(commandSender, LocaleEnums.Vault_command_append_medium_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", getExpiryDisplay(0),"%before_append%",String.valueOf(start-1));
            sendMessages(target, LocaleEnums.Vault_player_append_medium_notice, "%vault_num_start%", String.valueOf(start),"%vault_num_end%",String.valueOf(end),"%expiry_time%", getExpiryDisplay(0));
        }
    }

    public static boolean hasIgnorePlacePermission(Player player){
        return hasPermission(player,PermissionEnums.EVENT_PLACE_IGNORE);
    }

    public static boolean hasIgnorePickupPermission(Player player){
        return hasPermission(player,PermissionEnums.EVENT_PICKUP_IGNORE);
    }

    public static boolean hasPermission(CommandSender commandSender, PermissionEnums permission){
        if(isConsole(commandSender)){
            return true;
        }
        if(commandSender.isOp()){
            return true;
        }
        return commandSender.hasPermission(permission.getDesc());
    }

    public static boolean hasPermission(Player player, PermissionEnums permission){
        if(player.isOp()){
            return true;
        }
        return player.hasPermission(permission.getDesc());
    }

    public static boolean hasPermission(Player player, String permission){
        if(player.isOp()){
            return true;
        }
        return player.hasPermission(permission);
    }

    public static boolean hasVaultPermission(Player player,int type,int vault_number){
        if(player.isOp() || hasPermission(player,PermissionEnums.COMMAND_ADMIN) || hasPermission(player,PermissionEnums.INV_ALL)){
            return true;
        }
        if(type == 1){
            if(hasPermission(player,PermissionEnums.LARGE_ALL)){
                return true;
            }
            if(PlayerInv.getInstance().getPermissionManager().isLuckpermsServiceEnabled()){
                return PlayerInv.getInstance().getPermissionManager().getLuckpermsService().hasVaultPermission(player,"large",vault_number);
            }
            if(vault_number <= 10){
                return hasPermission(player,"playerinv.large.inv." + vault_number) || hasPermission(player,"playerinv.inv." + vault_number);
            } else {
                return hasPermission(player,"playerinv.large.inv." + vault_number);
            }
        } else {
            if(hasPermission(player,PermissionEnums.MEDIUM_ALL)){
                return true;
            }
            if(PlayerInv.getInstance().getPermissionManager().isLuckpermsServiceEnabled()){
                return PlayerInv.getInstance().getPermissionManager().getLuckpermsService().hasVaultPermission(player,"medium",vault_number);
            }
            if(vault_number <= 15){
                return hasPermission(player,"playerinv.medium.inv." + vault_number) || hasPermission(player,"playerinv.inv." + (vault_number+10));
            } else {
                return hasPermission(player,"playerinv.medium.inv." + vault_number);
            }
        }
    }

    private static String getExpiryDisplay(long day){
        String expiry = null;
        if(day == 0){
            expiry = PlayerInv.getInstance().getLocaleConfig().getString("Expiry.Permanent");
        } else {
            expiry = PlayerInv.getInstance().getLocaleConfig().getString("Expiry.Temporary").replace("%day_value%", String.valueOf(day));
        }
        return expiry;
    }
}
