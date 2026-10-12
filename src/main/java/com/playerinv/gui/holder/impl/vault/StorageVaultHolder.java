package com.playerinv.gui.holder.impl.vault;

import com.playerinv.PlayerInv;
import com.playerinv.api.VaultType;
import com.playerinv.api.event.VaultCloseEvent;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.manager.PermissionManager;
import com.playerinv.gui.holder.CustomVaultHolder;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.object.container.InventoryContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

import static com.playerinv.util.LocaleUtil.sendMessages;

public class StorageVaultHolder implements InventoryHolder, CustomVaultHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    private InventoryContainer container;

    private int num;

    private boolean isCommand;

    private boolean onlyPickup;

    private String fileName;

    public StorageVaultHolder(int num) {
        this.num = num;
        this.inventory = null;
        this.fileName = null;
    }

    public StorageVaultHolder(int num,String fileName) {
        this.num = num;
        this.inventory = null;
        this.fileName = fileName;
    }

    public void setIsCommand(boolean command) {
        isCommand = command;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOnlyPickup(boolean onlyPickup) {
        this.onlyPickup = onlyPickup;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setContainer(InventoryContainer container) {
        this.container = container;
    }

    public int getNum() {
        return num;
    }

    public void update(Player player){
        this.container.syncAndUpload(player,this.num);
    }

    public boolean isCommand() {
        return isCommand;
    }

    private void returnMenu(Player player){
        if(isCommand){
            if(ConfigConstants.RETURN_MENU_BY_COMMAND){
                PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                    PlayerInv.getInstance().getMenuManager().openVaultMenu(player,ConfigConstants.RETURN_MENU_BY_COMMAND_VALUE,false);
                },null,1);
            }
            if(ConfigConstants.RETURN_CMD_BY_COMMAND){
                ConfigConstants.RETURN_CMD_BY_COMMAND_VALUE.runCommandToPlayer(player,"%vault_num%", String.valueOf(this.num));
            }
            return;
        }
        if(ConfigConstants.FUNCTION_AUTO_RETURN){
            PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                PlayerInv.getInstance().getMenuManager().openVaultMenu(player,this.fileName,false);
            },null,1);
        }
    }

    public boolean isOnlyPickup() {
        return onlyPickup;
    }

    private int getVaultSize(){
        return this.container.getSize();
    }

    private boolean isEmptyItem(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    @Override
    public void runClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!JDBCUtil.isConnected()){
            sendMessages(player, LocaleEnums.Connection_invalid_refuse);
            event.setCancelled(true);
            return;
        }
        switch(event.getAction().name()){
            case "HOTBAR_SWAP":
            case "HOTBAR_MOVE_AND_READD":
                event.setCancelled(true);
                return;

            case "PLACE_ALL":
            case "PLACE_ONE":
            case "PLACE_SOME":
            case "NOTHING":
                if(event.getRawSlot() < event.getInventory().getSize()){
                    if(PermissionManager.hasIgnorePlacePermission(player)){
                        break;
                    }
                    if(!PlayerInv.getInstance().getVaultManager().isAllowVaultPlace() || this.isOnlyPickup()){
                        event.setCancelled(true);
                        return;
                    }
                }
                break;

            case "PICKUP_ALL":
            case "PICKUP_ONE":
            case "PICKUP_SOME":
            case "PICKUP_HALF":
            case "DROP_ALL_CURSOR":
            case "DROP_ALL_SLOT":
            case "DROP_ONE_CURSOR":
            case "DROP_ONE_SLOT":
                if(event.getRawSlot() < event.getInventory().getSize()){
                    if(PermissionManager.hasIgnorePickupPermission(player)){
                        break;
                    }
                    if(!PlayerInv.getInstance().getVaultManager().isAllowVaultPickup()){
                        event.setCancelled(true);
                        return;
                    }
                }
                break;

            case "MOVE_TO_OTHER_INVENTORY":
                if(event.getRawSlot() < event.getInventory().getSize()){
                    if(PermissionManager.hasIgnorePickupPermission(player)){
                        break;
                    }
                    if(!PlayerInv.getInstance().getVaultManager().isAllowVaultPickup()){
                        event.setCancelled(true);
                        return;
                    }
                } else if(event.getRawSlot() >= event.getInventory().getSize()){
                    if(PermissionManager.hasIgnorePlacePermission(player)){
                        break;
                    }
                    if(!PlayerInv.getInstance().getVaultManager().isAllowVaultPlace() || this.isOnlyPickup()){
                        event.setCancelled(true);
                        return;
                    }
                }
                break;

            case "SWAP_WITH_CURSOR":
            case "COLLECT_TO_CURSOR":
            case "CLONE_STACK":
                if(PermissionManager.hasIgnorePlacePermission(player) || PermissionManager.hasIgnorePickupPermission(player)){
                    break;
                }
                if(PlayerInv.getInstance().getVaultManager().isDisallowAnEvent() || this.isOnlyPickup()){
                    event.setCancelled(true);
                    return;
                }
                break;
        }
        ItemStack item = event.getCurrentItem();
        if(item != null && event.getRawSlot() >= event.getInventory().getSize()){
            if(PlayerInv.getInstance().getVaultManager().isMaterialBlacklist(item.getType())
                    && !PermissionManager.hasPermission(player, PermissionEnums.BLACKLIST_LORE_IGNORE)){
                event.setCancelled(true);
                sendMessages(player,LocaleEnums.Vault_blacklist_items);
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()
                    && !PermissionManager.hasPermission(player, PermissionEnums.BLACKLIST_LORE_IGNORE)) {
                if(PlayerInv.getInstance().getVaultManager().isLoreBlacklist(item.getItemMeta().getLore())){
                    event.setCancelled(true);
                    sendMessages(player,LocaleEnums.Vault_blacklist_items);
                    return;
                }
            }
        }
        this.update((Player) event.getWhoClicked());
    }

    @Override
    public void runDragEvent(InventoryDragEvent event) {
        if(event.getRawSlots().stream().min(Integer::compareTo).get() <= (this.getVaultSize()-1)) {
            Player player = (Player) event.getWhoClicked();
            if(this.isOnlyPickup()){
                event.setCancelled(true);
                return;
            }
            if(!PlayerInv.getInstance().getVaultManager().isAllowVaultPlace() && !PermissionManager.hasIgnorePlacePermission(player)){
                event.setCancelled(true);
                return;
            }
            this.update(player);
            return;
        }
    }

    @Override
    public void runCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        MessageUtil.sendDetail("玩家 " + player.getName() + " 关闭个人仓库: " + this.container.getType() + "//" + player.getUniqueId() + ":" + this.num);
        this.update(player);
        VaultCloseEvent closeEvent = new VaultCloseEvent(player,event.getInventory(), VaultType.fromInteger(this.container.getType()), this.num);
        Bukkit.getPluginManager().callEvent(closeEvent);
        PlayerInv.getInstance().getSoundManager().vaultClose(player);
        this.returnMenu(player);
    }
}
