package com.playerinv.gui.holder.impl.vault;

import com.playerinv.PlayerInv;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.gui.holder.CustomVaultHolder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static com.playerinv.util.LocaleUtil.sendMessages;

public class CheckVaultHolder implements InventoryHolder, CustomVaultHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return vault;
    }

    private int vault_num;

    private int type;

    private OfflinePlayer target;

    private Inventory back_inventory;

    private Inventory vault;

    private boolean hasError;

    private boolean isSave;

    public CheckVaultHolder(OfflinePlayer target,int vault_num, int type, Inventory back_inventory) {
        this.vault_num = vault_num;
        this.type = type;
        this.target = target;
        this.back_inventory = back_inventory;
        this.isSave = true;
        this.hasError = false;
    }

    public void setVault(Inventory vault) {
        this.vault = vault;
    }

    public void setError() {
        this.hasError = true;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public void notSave(){
        this.isSave = false;
    }

    public void switchSave(Player player){
        if(this.hasError){
            this.isSave = !this.isSave;
            if(this.isSave){
                sendMessages(player, LocaleEnums.Check_vault_set_save);
            } else {
                sendMessages(player, LocaleEnums.Check_vault_no_save);
            }
        }
        return;
    }

    public boolean isSave(){
        return isSave;
    }

    public int getVault_num() {
        return vault_num;
    }

    public void setVault_num(int vault_num) {
        this.vault_num = vault_num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void saveVault(){
        if(this.isSave){
            PlayerInv.getInstance().getVaultManager().uploadVaultByCheck(type,target.getUniqueId().toString(),vault,vault_num);
        }
    }

    public void backToMenu(Player player){
        PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(() -> {
           player.openInventory(back_inventory);
        },null,1);
    }

    @Override
    public void runClickEvent(InventoryClickEvent event) {
        if(event.getClick().name().equals("SHIFT_RIGHT") && (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)){
            this.switchSave((Player) event.getWhoClicked());
        }
    }

    @Override
    public void runDragEvent(InventoryDragEvent event) {
        return;
    }

    @Override
    public void runCloseEvent(InventoryCloseEvent event) {
        this.saveVault();
        this.backToMenu((Player) event.getPlayer());
    }
}
