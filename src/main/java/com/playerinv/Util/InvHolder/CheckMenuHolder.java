package com.playerinv.Util.InvHolder;

import com.playerinv.GUI.CheckMenuStruct;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;

public class CheckMenuHolder implements InventoryHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private int page;

    private HashSet<Integer> large_set;

    private HashSet<Integer> medium_set;

    private OfflinePlayer target;

    private Inventory inventory;

    public CheckMenuHolder(OfflinePlayer target) {
        this.page = 1;
        this.target = target;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.initBasicView().whenComplete((result,throwable) -> {
            this.getCheckVaults();
        });
    }

    private void getCheckVaults(){
        CompletableFuture.runAsync(() -> {
            this.large_set = jdbcUtil.getCheckVaults(target.getUniqueId().toString(), 1);
            this.medium_set = jdbcUtil.getCheckVaults(target.getUniqueId().toString(), 2);
            this.initVaultView();
        });
    }

    public CompletableFuture<Void> initBasicView(){
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduler.scheduling().asyncScheduler().run(task -> {
            CheckMenuStruct checkMenu = menuManager.getCheckMenu();
            for(int i=0;i<27;i++){
                int vault_num = ((page-1)*27) + (i+1);
                if(vault_num > largeVaultAmount){
                    break;
                }
                inventory.setItem(i,checkMenu.setVaultLarge_NotExist(vault_num));
            }
            for(int i=0;i<18;i++){
                int vault_num = ((page-1)*18) + (i+1);
                if(vault_num > mediumVaultAmount){
                    break;
                }
                inventory.setItem(i+27,checkMenu.setVaultMedium_NotExist(vault_num));
            }
            future.complete(null);
        });
        return future;
    }

    public void initVaultView(){
        scheduler.scheduling().asyncScheduler().run(task -> {
            CheckMenuStruct checkMenu = menuManager.getCheckMenu();
            for(int vault : large_set){
                if(vault <= ((page-1)*27) || vault > (page*27)){
                    continue;
                }
                int current_vault = vault - ((page-1)*27);
                inventory.setItem(current_vault-1,checkMenu.setVaultLarge_Exist(vault));
            }
            for(int vault : medium_set){
                if(vault <= ((page-1)*18) || vault > (page*18)){
                    continue;
                }
                int current_vault = vault - ((page-1)*18);
                inventory.setItem((current_vault-1) + 27,checkMenu.setVaultMedium_Exist(vault));
            }
        });
    }

    public void updateView(){
        scheduler.scheduling().asyncScheduler().run(task -> {
            CheckMenuStruct checkMenu = menuManager.getCheckMenu();
            this.inventory.setContents(checkMenu.basicInventory.getContents());
            for(int i=0;i<27;i++){
                int vault_num = ((page-1)*27) + (i+1);
                if(vault_num > largeVaultAmount){
                    break;
                }
                inventory.setItem(i,checkMenu.setVaultLarge_NotExist(vault_num));
            }
            for(int i=0;i<18;i++){
                int vault_num = ((page-1)*18) + (i+1);
                if(vault_num > mediumVaultAmount){
                    break;
                }
                inventory.setItem(i+27,checkMenu.setVaultMedium_NotExist(vault_num));
            }
            this.initVaultView();
        });
    }

    public void nextPage(){
        if(this.page == menuManager.getCheckMenuMaxPage()){
            return;
        }
        this.page = this.page + 1;
        this.updateView();
    }

    public void previousPage(){
        if(this.page == 1){
            return;
        }
        this.page = this.page - 1;
        this.updateView();
    }

    public void runOperation(Player player, int clicked_slot){
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
                if(clicked_slot >= 0 && clicked_slot < 27){
                    int vault_num = ((page-1)*27) + clicked_slot + 1;
                    vaultManager.checkingPlayerVault(player,this.inventory,target,1,vault_num);
                    return;
                } else if(clicked_slot >= 27){
                    int vault_num = ((page-1)*18) + (clicked_slot-27) + 1;
                    vaultManager.checkingPlayerVault(player,this.inventory,target,2,vault_num);
                    return;
                }
                return;
        }
    }
}
