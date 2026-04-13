package com.playerinv.Manager;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.Luckperms.LuckpermsService;
import com.playerinv.Util.NodeUtil;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.time.Duration;
import java.util.HashSet;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.luckperms_givePermissions;
import static com.playerinv.Util.LoadUtil.voucherSetOwner;
import static com.playerinv.Util.LocaleUtil.*;
import static com.playerinv.Util.NodeUtil.*;

public class PermissionManager {

    private HashSet<String> voucherCooldown = new HashSet<>();

    private Permission perms;

    private LuckpermsService luckpermsService;

    public boolean initVaultDepend() {
        try {
            if(Bukkit.getPluginManager().getPlugin("Vault") == null){
                throw new NoClassDefFoundError();
            }
            RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if(rsp == null){
                throw new NoClassDefFoundError();
            }
            perms = rsp.getProvider();
            return true;
        } catch (Exception e) {
            NodeUtil.sendError("缺少 Vault或权限组 插件,请确保前置插件已安装!","Missing Vault or Permission-Manage plugin, make sure is installed!");
            e.printStackTrace();
            return false;
        }
    }

    public void initLuckpermService() {
        luckpermsService = new LuckpermsService();
    }

    public LuckpermsService getLuckpermsService() {
        return luckpermsService;
    }

    public void runVoucher(Player player, ItemStack item){
        if(voucherCooldown.contains(player.getName())){
            return;
        }
        voucherCooldown.add(player.getName());
        scheduler.scheduling().asyncScheduler().runDelayed(task -> {
            voucherCooldown.remove(player.getName());
        }, Duration.ofSeconds(1));
        NBTEditor.NBTCompound compound = NBTEditor.getNBTCompound(item);
        String json = compound.toJson();
        if(json.contains("playerinv:large")){
            if(player.isOp() || NodeUtil.hasPermission(player, PermissionEnums.INV_ALL) || NodeUtil.hasPermission(player,PermissionEnums.COMMAND_ADMIN) || NodeUtil.hasPermission(player,PermissionEnums.LARGE_ALL)){
                sendMessages(player, LocaleEnums.Voucher_full);
                return;
            }
            if(voucherSetOwner){
                String owner = NBTEditor.getString(item,NBTEditor.CUSTOM_DATA,"playerinv:large");
                if(owner.equals(player.getName())){
                    this.verifyVoucher(player,item,1);
                } else {
                    sendMessages(player,LocaleEnums.Voucher_cannot_use);
                }
            } else {
                this.verifyVoucher(player,item,1);
            }
            return;
        }
        if(json.contains("playerinv:medium")){
            if(player.isOp() || NodeUtil.hasPermission(player, PermissionEnums.INV_ALL) || NodeUtil.hasPermission(player,PermissionEnums.COMMAND_ADMIN) || NodeUtil.hasPermission(player,PermissionEnums.MEDIUM_ALL)){
                sendMessages(player, LocaleEnums.Voucher_full);
                return;
            }
            if(voucherSetOwner){
                String owner = NBTEditor.getString(item,NBTEditor.CUSTOM_DATA,"playerinv:medium");
                if(owner.equals(player.getName())){
                    this.verifyVoucher(player,item,2);
                } else {
                    sendMessages(player,LocaleEnums.Voucher_cannot_use);
                }
            } else {
                this.verifyVoucher(player,item,2);
            }
            return;
        }
    }

    private void verifyVoucher(Player player, ItemStack item, int type){
        scheduler.scheduling().asyncScheduler().run(task -> {
            this.givePermissionByVoucher(player,type,item);
        });
    }

    private boolean hasAdminPermission(Player player){
        return player.isOp() || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.inv.*");
    }

    public void giveCertainPermission(Player target, int type, int num, long expire, CommandSender sender){
        if(this.hasAdminPermission(target)){
            sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
            return;
        }
        if(luckperms_givePermissions){
            long day = 0;
            if(expire > 0){
                day = expire;
            }
            if(luckpermsService.giveSpecificPermission(target,type,num,day)){
                if(type == 1){
                    sendMessages(sender, LocaleEnums.Vault_command_give_large_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                    sendMessages(target,LocaleEnums.Vault_player_give_large_notice,"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                    runCommand_Large(target.getName(),num);
                } else {
                    sendMessages(sender, LocaleEnums.Vault_command_give_medium_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                    sendMessages(target,LocaleEnums.Vault_player_give_medium_notice,"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                    runCommand_Medium(target.getName(),num);
                }
                return;
            }
            sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
            return;
        }
        if(type == 1){
            if(!hasVaultPermission(target,1,num)){
                perms.playerAdd(null,target,"playerinv.large.inv." + num);
                sendMessages(sender, LocaleEnums.Vault_command_give_large_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                sendMessages(target,LocaleEnums.Vault_player_give_large_notice,"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                runCommand_Large(target.getName(),num);
                return;
            }
        } else {
            if(!hasVaultPermission(target,2,num)){
                perms.playerAdd(null,target,"playerinv.medium.inv." + num);
                sendMessages(sender, LocaleEnums.Vault_command_give_medium_success,"%player%",target.getName(),"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                sendMessages(target,LocaleEnums.Vault_player_give_medium_notice,"%vault_num%",String.valueOf(num),"%expiry_time%",expiry_adjust(0));
                runCommand_Medium(target.getName(),num);
                return;
            }
        }
        sendMessages(sender, LocaleEnums.Vault_command_give_already_has,"%player%",target.getName());
    }

    public void givePermissionByVoucher(Player player, int type,ItemStack item){
        boolean title = plugin.getConfig().getBoolean("Voucher.Title");
        if(luckperms_givePermissions){
            int result = luckpermsService.giveLoopPermission(player,type,0);
            if(result == 0){
                sendMessages(player,LocaleEnums.Voucher_full);
                return;
            }
            item.setAmount(item.getAmount() - 1);
            if(type == 1){
                sendMessages(player,LocaleEnums.Voucher_use_large,"%large_vault_number%", String.valueOf(result));
                if (title) {
                    player.sendTitle(color(Locale_Voucher_Large_Title(result)), color(Locale_Voucher_Large_Subtitle(result)), 10, 60, 10);
                }
                runCommand_Large(player.getName(), result);
                soundManager.playSoundEffect(player,soundManager.voucherUse);
            } else {
                sendMessages(player,LocaleEnums.Voucher_use_medium,"%medium_vault_number%", String.valueOf(result));
                if (title) {
                    player.sendTitle(color(Locale_Voucher_Medium_Title(result)), color(Locale_Voucher_Medium_Subtitle(result)), 10, 60, 10);
                }
                runCommand_Medium(player.getName(), result);
                soundManager.playSoundEffect(player,soundManager.voucherUse);
            }
            return;
        }
        if(type == 1){
            for(int i=1;i<=largeVaultAmount;i++){
                if(!hasVaultPermission(player,1,i)){
                    item.setAmount(item.getAmount() - 1);
                    perms.playerAdd(null,player,"playerinv.large.inv." + i);
                    sendMessages(player,LocaleEnums.Voucher_use_large,"%large_vault_number%", String.valueOf(i));
                    if (title) {
                        player.sendTitle(color(Locale_Voucher_Large_Title(i)), color(Locale_Voucher_Large_Subtitle(i)), 10, 60, 10);
                    }
                    runCommand_Large(player.getName(), i);
                    soundManager.playSoundEffect(player,soundManager.voucherUse);
                    return;
                }
            }
            sendMessages(player,LocaleEnums.Voucher_full);
        } else {
            for(int i=1;i<=mediumVaultAmount;i++){
                if(!hasVaultPermission(player,2,i)){
                    item.setAmount(item.getAmount() - 1);
                    perms.playerAdd(null,player,"playerinv.medium.inv." + i);
                    sendMessages(player,LocaleEnums.Voucher_use_medium,"%medium_vault_number%", String.valueOf(i));
                    if (title) {
                        player.sendTitle(color(Locale_Voucher_Medium_Title(i)), color(Locale_Voucher_Medium_Subtitle(i)), 10, 60, 10);
                    }
                    runCommand_Medium(player.getName(), i);
                    soundManager.playSoundEffect(player,soundManager.voucherUse);
                    return;
                }
            }
            sendMessages(player,LocaleEnums.Voucher_full);
        }
    }


    private int getVaultAmount(int type){
        if(type == 1){
            return largeVaultAmount;
        }
        return mediumVaultAmount;
    }

    private String getVaultTypeString(int type){
        if(type == 1){
            return "large";
        }
        return "medium";
    }

    private void runCommand(String target,int type,int vault_num){
        if(type == 1){
            runCommand_Large(target,vault_num);
            return;
        }
        runCommand_Medium(target, vault_num);
    }

    public void appendPermissions(Player target,int type,int amount,long expire,CommandSender commandSender,boolean ignoreExpiry){
        if(this.hasAdminPermission(target)){
            sendMessages(commandSender,LocaleEnums.Vault_command_append_full);
            return;
        }
        if(luckperms_givePermissions){
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
                sendMessages(commandSender, LocaleEnums.Vault_command_append_large_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", expiry_adjust(day),"%before_append%",String.valueOf(ints[0]-1));
                sendMessages(target, LocaleEnums.Vault_player_append_large_notice, "%vault_num_start%", String.valueOf(ints[0]),"%vault_num_end%",String.valueOf(ints[1]),"%expiry_time%", expiry_adjust(day));
            } else {
                sendMessages(commandSender, LocaleEnums.Vault_command_append_medium_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", expiry_adjust(day),"%before_append%",String.valueOf(ints[0]-1));
                sendMessages(target, LocaleEnums.Vault_player_append_medium_notice, "%vault_num_start%", String.valueOf(ints[0]),"%vault_num_end%",String.valueOf(ints[1]),"%expiry_time%", expiry_adjust(day));
            }
            return;
        }
        int start = -1;
        int end = -1;
        int inject = 0;
        for(int i=1;i<=this.getVaultAmount(type);i++){
            if(!hasVaultPermission(target,type,i)){
                if(start == -1) start = i;
                perms.playerAdd(null,target,"playerinv." + this.getVaultTypeString(type) + ".inv." + i);
                this.runCommand(target.getName(),type,i);
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
            end = this.getVaultAmount(type);
        }
        if(type == 1){
            sendMessages(commandSender, LocaleEnums.Vault_command_append_large_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", expiry_adjust(0),"%before_append%",String.valueOf(start-1));
            sendMessages(target, LocaleEnums.Vault_player_append_large_notice, "%vault_num_start%", String.valueOf(start),"%vault_num_end%",String.valueOf(end),"%expiry_time%", expiry_adjust(0));
        } else {
            sendMessages(commandSender, LocaleEnums.Vault_command_append_medium_success, "%player%", target.getName(), "%vault_amount%", String.valueOf(amount), "%expiry_time%", expiry_adjust(0),"%before_append%",String.valueOf(start-1));
            sendMessages(target, LocaleEnums.Vault_player_append_medium_notice, "%vault_num_start%", String.valueOf(start),"%vault_num_end%",String.valueOf(end),"%expiry_time%", expiry_adjust(0));
        }
    }
}
