package com.playerinv.gui.holder.impl.menu;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.gui.icon.MenuIcon;
import com.playerinv.gui.holder.CustomMenuHolder;
import com.playerinv.object.chat.TypeAttribute;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.playerinv.manager.PermissionManager.hasPermission;
import static com.playerinv.manager.PermissionManager.hasVaultPermission;
import static com.playerinv.util.LocaleUtil.sendMessages;

public class VaultMenuHolder implements InventoryHolder, CustomMenuHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private String file_name;

    private Player player;

    private Inventory inventory;

    private HashMap<Integer,MenuIcon> icons;

    private boolean lock;

    public VaultMenuHolder(Player player,String file_name,HashMap<Integer,MenuIcon> icons) {
        this.file_name = file_name;
        this.player = player;
        this.icons = icons;
        this.lock = false;
    }

    public void loadInventory(Inventory inventory) {
        this.inventory = inventory;
        for(MenuIcon icon : icons.values()){
            if(icon.getRawSlot() == -1) continue;
            this.inventory.setItem(icon.getRawSlot(),icon.generateIcon(player));
        }
    }

    public void setItemToInventory(int slot,ItemStack itemStack){
        this.inventory.setItem(slot,itemStack);
    }

    public void lock(){
        this.lock = true;
    }

    public void unlock(){
        this.lock = false;
    }

    public boolean isLock() {
        return lock;
    }

    public MenuIcon getClickedIcon(int clicked_slot){
        return this.icons.get(clicked_slot);
    }

    @Override
    public void runClickEvent(InventoryClickEvent event) {
        String click_type = event.getClick().name();
        if(this.isLock()) return;
        MenuIcon clickedIcon = this.getClickedIcon(event.getRawSlot());
        if(clickedIcon == null) return;
        if(clickedIcon.isCustomIcon()){
            if(clickedIcon.hasCustomKey()){
                switch(clickedIcon.getCustomKey()){
                    case "pickup-switch":
                        if(!ConfigConstants.FUNCTION_AUTO_PICKUP){
                            break;
                        }
                        Bukkit.dispatchCommand(player,"playerinv pickup toggle");
                        break;
                    case "close-menu":
                        PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                            player.closeInventory();
                        },null,1);
                        break;
                    case "open-menu":
                        PlayerInv.getInstance().getMenuManager().openVaultMenu(player,clickedIcon.getCustomKeyValue().trim(),true);
                        break;
                }
            }
            clickedIcon.runActions(player,click_type);
            return;
        } else {
            int type = clickedIcon.getVaultType();
            int vault_num = clickedIcon.getVaultNum();
            switch(click_type){
                case "LEFT":
                    PlayerInv.getInstance().getVaultManager().openVault(player,type,vault_num,false, this.file_name);
                    return;
                case "SHIFT_RIGHT":
                    if(!ConfigConstants.FUNCTION_CHANGE_VAULT_ATTRIBUTES){
                        sendMessages(player,LocaleEnums.Vault_attribute_change_disabled);
                        return;
                    }
                    if (!hasVaultPermission(player,type,vault_num)) {
                        return;
                    }
                    if (!hasPermission(player, PermissionEnums.ALTER_NAME)) {
                        sendMessages(player, LocaleEnums.No_permission_command);
                        return;
                    }
                    PlayerInv.getInstance().getChatBarManager().addAttributeAlter(player.getUniqueId(),new TypeAttribute(player,type,vault_num,this.file_name));
                    sendMessages(player,LocaleEnums.Vault_attribute_change_notice);
                    PlayerInv.getInstance().getSoundManager().typeChatBar(player);
                    PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                        player.closeInventory();
                    },null,2);
                    return;
            }
        }
        return;
    }

    @Override
    public void runCloseEvent(InventoryCloseEvent event) {
        return;
    }
}
