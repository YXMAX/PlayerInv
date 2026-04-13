package com.playerinv.Util.InvHolder;

import com.playerinv.Enums.LocaleEnums;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static com.playerinv.PlayerInv.operationManager;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LocaleUtil.sendMessages;

public class CheckVaultHolder implements InventoryHolder {
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
            operationManager.updateVaultToDatabase(type,target.getUniqueId().toString(),vault,vault_num);
        }
    }

    public void backToMenu(Player player){
        scheduler.scheduling().entitySpecificScheduler(player).runDelayed(() -> {
           player.openInventory(back_inventory);
        },null,1);
    }
}
