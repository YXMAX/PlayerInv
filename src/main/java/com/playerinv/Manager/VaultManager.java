package com.playerinv.Manager;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Util.InvHolder.CheckVaultHolder;
import com.playerinv.Util.InvHolder.VaultHolder_Large;
import com.playerinv.Util.InvHolder.VaultHolder_Medium;
import com.playerinv.Util.JDBCUtil;
import com.playerinv.Util.LocaleUtil;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Cache.InventoryContainer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.playerinv.Enums.LocaleEnums.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.clearItemWhenError;
import static com.playerinv.Util.LoadUtil.onlyPickupNoPermission;
import static com.playerinv.Util.LocaleUtil.sendMessages;
import static com.playerinv.Util.NodeUtil.color;

public class VaultManager {

    public void checkingPlayerVault(Player checker,Inventory menu, OfflinePlayer target, int vault_type, int vault_num){
        if(!JDBCUtil.isConnected()){
            sendMessages(checker,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        CompletableFuture.runAsync(() -> {
            String string = jdbcUtil.getVaultString(vault_type, target.getUniqueId().toString(), vault_num);
            CheckVaultHolder checkVaultHolder = NodeUtil.inventoryFromBase64_Check(target, string, vault_type, vault_num, menu);
            int delay = 1;
            if(checkVaultHolder != null){
                if(checkVaultHolder.hasError()){
                    sendMessages(checker, LocaleEnums.Check_vault_error_exist);
                    delay = 100;
                }
                scheduler.scheduling().entitySpecificScheduler(checker).runDelayed(() -> {
                    checker.openInventory(checkVaultHolder.getInventory());
                },null,delay);
            }
        });
    }

    public void openStorageVault(Player player,int vault_type, int vault_num,String menu_name, boolean isCommand){
        boolean perms = NodeUtil.hasVaultPermission(player, vault_type, vault_num);
        CompletableFuture.runAsync(() -> {
            if(!JDBCUtil.isConnected()){
                sendMessages(player,LocaleEnums.Connection_invalid_refuse);
                return;
            }
            if(!cacheInventoryManager.isCacheReady(player)){
                sendMessages(player,LocaleEnums.Cache_not_ready);
                return;
            }
            boolean only_pickup = false;
            InventoryContainer content = cacheInventoryManager.getInventoryContainer(player,vault_type,vault_num);
            Inventory vault;
            if(content != null){
                if(content.isNotReady()){
                    sendMessages(player,LocaleEnums.Cache_not_ready);
                    return;
                }
                if(!perms){
                    if(onlyPickupNoPermission){
                        only_pickup = true;
                    } else {
                        sendMessages(player,No_permission_vault);
                        return;
                    }
                }
                if(content.hasError() && !clearItemWhenError){
                    sendMessages(player,Vault_item_destroy_cancel_open);
                    return;
                }
                if(vault_type == 1){
                    vault = Bukkit.createInventory(new VaultHolder_Large(vault_num,menu_name,isCommand,only_pickup),54,color(vaultAttributesManager.replaceVaultTitle(player,1,vault_num)));
                    vault.setContents(content.getContents());
                } else {
                    vault = Bukkit.createInventory(new VaultHolder_Medium(vault_num,menu_name,isCommand,only_pickup),27,color(vaultAttributesManager.replaceVaultTitle(player,2,vault_num)));
                    vault.setContents(content.getContents());
                }
            } else {
                if(!perms){
                    sendMessages(player,No_permission_vault);
                    return;
                }
                jdbcUtil.insertVaultAsync(vault_type,player.getUniqueId().toString(),vault_num);
                if(vault_type == 1){
                    vault = Bukkit.createInventory(new VaultHolder_Large(vault_num,menu_name,isCommand,only_pickup),54,color(vaultAttributesManager.replaceVaultTitle(player,1,vault_num)));
                } else {
                    vault = Bukkit.createInventory(new VaultHolder_Medium(vault_num,menu_name,isCommand,only_pickup),27,color(vaultAttributesManager.replaceVaultTitle(player,2,vault_num)));
                }
                cacheInventoryManager.syncInventoryAndUpload(player,vault_type,vault_num,vault);
            }
            scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
                soundManager.playSoundEffect(player,soundManager.vaultOpen);
                player.openInventory(vault);
            },null,1);
        });
    }

    public void openVaultIgnorePerms(Player player, int vault_type, int vault_num, CommandSender sender){
        if(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large
                || player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Medium){
            player.closeInventory();
        }
        CompletableFuture.runAsync(() -> {
            if(!JDBCUtil.isConnected()){
                sendMessages(player,LocaleEnums.Connection_invalid_refuse);
                return;
            }
            if(cacheInventoryManager.isCacheBroken(player)){
                sendMessages(sender,LocaleEnums.Cache_not_ready);
                return;
            }
            InventoryContainer content = cacheInventoryManager.getInventoryContainer(player,vault_type,vault_num);
            Inventory vault;
            if(content != null){
                if(content.isNotReady()){
                    sendMessages(player,LocaleEnums.Cache_not_ready);
                    return;
                }
                if(content.hasError() && !clearItemWhenError){
                    sendMessages(player,Vault_item_destroy_cancel_open);
                    return;
                }
                if(vault_type == 1){
                    vault = Bukkit.createInventory(new VaultHolder_Large(vault_num,null,true,false),54,color(vaultAttributesManager.replaceVaultTitle(player,1,vault_num)));
                    vault.setContents(content.getContents());
                } else {
                    vault = Bukkit.createInventory(new VaultHolder_Medium(vault_num,null,true,false),27,color(vaultAttributesManager.replaceVaultTitle(player,2,vault_num)));
                    vault.setContents(content.getContents());
                }
            } else {
                jdbcUtil.insertVaultAsync(vault_type,player.getUniqueId().toString(),vault_num);
                if(vault_type == 1){
                    vault = Bukkit.createInventory(new VaultHolder_Large(vault_num,null,true,false),54,color(vaultAttributesManager.replaceVaultTitle(player,1,vault_num)));
                } else {
                    vault = Bukkit.createInventory(new VaultHolder_Medium(vault_num,null,true,false),27,color(vaultAttributesManager.replaceVaultTitle(player,2,vault_num)));
                }
            }
            LocaleUtil.sendMessages(sender,Open_vault_medium_success,"%player%",player.getName(),"%vault_num%", String.valueOf(vault_num));
            scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
                soundManager.playSoundEffect(player,soundManager.vaultOpen);
                player.openInventory(vault);
            },null,1);
        });
    }
}
