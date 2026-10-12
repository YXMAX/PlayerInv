package com.playerinv.gui.holder.impl.menu;

import com.playerinv.PlayerInv;
import com.playerinv.gui.CheckMenuStruct;
import com.playerinv.gui.holder.CustomMenuHolder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CheckMenuHolder implements InventoryHolder, CustomMenuHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private int page;

    private OfflinePlayer target;

    private Inventory inventory;

    private boolean lock;

    public CheckMenuHolder(OfflinePlayer target) {
        this.page = 1;
        this.target = target;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.updateView(true);
    }

    public void updateView(boolean boot){
        this.lock = true;
        int currentPageLarge = Math.min(27, PlayerInv.getInstance().getVaultManager().getLargeVaultAmount() - ((this.page-1) * 27));
        int currentPageMedium = Math.min(18, PlayerInv.getInstance().getVaultManager().getMediumVaultAmount() - ((this.page-1) * 18));
        CompletableFuture.runAsync(() -> {
            CheckMenuStruct checkMenu = CheckMenuStruct.getInstance();
            if(!boot){
                inventory.setContents(checkMenu.getBasicInventory().getContents());
            }
            for(int i=0;i<currentPageLarge;i++){
                int vault_num = ((page-1)*27) + (i+1);
                inventory.setItem(i,checkMenu.getVaultLargeIcon(vault_num));
            }
            for(int i=0;i<currentPageMedium;i++){
                int vault_num = ((page-1)*18) + (i+1);
                inventory.setItem(i+27,checkMenu.getVaultMediumIcon(vault_num));
            }
            this.lock = false;
        });
    }

    public void nextPage(){
        if(this.lock) return;
        if(this.page == CheckMenuStruct.getInstance().getMaxPage()){
            return;
        }
        this.page = this.page + 1;
        this.updateView(false);
    }

    public void previousPage(){
        if(this.lock) return;
        if(this.page == 1){
            return;
        }
        this.page = this.page - 1;
        this.updateView(false);
    }

    @Override
    public void runClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        int clicked_slot = event.getRawSlot();
        switch(clicked_slot){
            case 45:
            case 46:
                this.previousPage();
                return;
            case 52:
            case 53:
                this.nextPage();
                return;
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
                return;
            default:
                if(this.lock) return;
                if(clicked_slot >= 0 && clicked_slot < 27){
                    int vault_num = ((page-1)*27) + clicked_slot + 1;
                    PlayerInv.getInstance().getVaultManager().checkVault(player,this.inventory,target,1,vault_num);
                    return;
                } else if(clicked_slot >= 27){
                    int vault_num = ((page-1)*18) + (clicked_slot - 27) + 1;
                    PlayerInv.getInstance().getVaultManager().checkVault(player,this.inventory,target,2,vault_num);
                    return;
                }
                return;
        }
    }

    @Override
    public void runCloseEvent(InventoryCloseEvent event) {
        return;
    }
}
