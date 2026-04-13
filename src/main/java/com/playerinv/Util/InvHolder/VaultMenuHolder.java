package com.playerinv.Util.InvHolder;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.Object.Operation.TypeAttribute;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.playerinv.Enums.LocaleEnums.No_permission_command;
import static com.playerinv.Listener.PlayerListener.typeAttributeMap;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.hasPAPI;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.LoadUtil.vaultNameChange;
import static com.playerinv.Util.LocaleUtil.sendMessages;
import static com.playerinv.Util.NodeUtil.*;
import static com.playerinv.Util.NodeUtil.hasPermission;

public class VaultMenuHolder implements InventoryHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private String file_name;

    private Player player;

    private HashMap<Integer, String> click_map;

    private HashMap<Integer, Integer> large;

    private HashMap<Integer, Integer> medium;

    private Inventory inventory;

    public VaultMenuHolder(Player player,String file_name, HashMap<Integer, String> click_map,HashMap<Integer, Integer> large,HashMap<Integer, Integer> medium) {
        this.file_name = file_name;
        this.player = player;
        this.large = large;
        this.medium = medium;
        this.click_map = click_map;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getFile_name() {
        return file_name;
    }

    public HashMap<Integer, String> getClick_map() {
        return click_map;
    }

    public void runOperation(int clicked_slot,String click_type) {
        if(this.click_map.containsKey(clicked_slot + 1)) {
            String[] split = this.click_map.get(clicked_slot + 1).split("@");
            switch (split[0]){
                case "PRESET":
                    switch(split[1]){
                        case "pickup-switch":
                            if(!autoPickupBool){
                                return;
                            }
                            Bukkit.dispatchCommand(player,"playerinv pickup toggle");
                            return;
                        case "close":
                            scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
                                player.closeInventory();
                            },null,1);
                            return;
                        default:
                            if(split[1].contains("menu%%")){
                                String[] s = split[1].split("%%");
                                String menu_name = s[1];
                                menuManager.openVaultMenu(player,menu_name);
                            }
                            return;
                    }
                case "COMMAND":
                    String command = split[1];
                    if(!command.contains("##")){
                        return;
                    }
                    String[] command_detail = command.split("##");
                    String reset_string = command_detail[1];
                    if(hasPAPI){
                        reset_string = PlaceholderAPI.setPlaceholders(player,reset_string);
                    }
                    reset_string = reset_string.replaceAll("%player%",player.getName());
                    reset_string = reset_string.trim();
                    switch(command_detail[0]){
                        case "console":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),reset_string);
                            return;
                        case "op":
                            if(player.isOp()){
                                player.performCommand(reset_string);
                                return;
                            }
                            try {
                                player.setOp(true);
                                player.performCommand(reset_string);
                            } finally {
                                player.setOp(false);
                            }
                            return;
                        default:
                            player.performCommand(reset_string);
                            return;
                    }
            }
            return;
        }
        int vault_num = 0;
        int type = 0;
        if(this.large.containsKey(clicked_slot + 1)) {
            vault_num = this.large.get(clicked_slot + 1);
            type = 1;
        } else if(this.medium.containsKey(clicked_slot + 1)) {
            vault_num = this.medium.get(clicked_slot + 1);
            type = 2;
        }
        if(type == 0){
            return;
        }
        switch(click_type){
            case "LEFT":
                vaultManager.openStorageVault(player,type,vault_num,this.file_name,false);
                return;
            case "SHIFT_RIGHT":
                if(!vaultNameChange){
                    sendMessages(player,LocaleEnums.Vault_attribute_change_disabled);
                    return;
                }
                if (!hasVaultPermission(player,type,vault_num)) {
                    return;
                }
                if (!hasPermission(player, PermissionEnums.ALTER_NAME)) {
                    sendMessages(player, No_permission_command);
                    return;
                }
                typeAttributeMap.put(player,new TypeAttribute(player,type,vault_num,this.file_name));
                sendMessages(player,LocaleEnums.Vault_attribute_change_notice);
                soundManager.playSoundEffect(player,soundManager.typeChatBar);
                scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
                    player.closeInventory();
                },null,2);
                return;
        }
        return;
    }
}
