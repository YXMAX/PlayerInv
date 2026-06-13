package com.playerinv.Util.InvHolder;

import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Cache.InventoryContainer;
import com.playerinv.Util.Scheduler.ReturnScheduler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.playerinv.PlayerInv.cacheInventoryManager;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.NodeUtil.sendLog;

public class VaultHolder_Medium implements InventoryHolder {
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

    public VaultHolder_Medium(int num) {
        this.num = num;
        this.inventory = null;
    }

    public void setOnlyPickup(boolean onlyPickup) {
        this.onlyPickup = onlyPickup;
    }

    public void setIsCommand(boolean command) {
        isCommand = command;
    }

    public void setContainer(InventoryContainer container) {
        this.container = container;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getNum() {
        return num;
    }

    public void update(Player player){
        this.container.syncAndUpload(player,this.num);
    }

    public void returnMenu(Player player){
        if(isCommand){
            if(command_returnBool){
                ReturnScheduler.returnVaultMenu(player,command_returnMenu);
            }
            if(command_returnCmdBool){
                NodeUtil.runReturnCommand(player);
            }
            return;
        }
    }

    public boolean isOnlyPickup() {
        return onlyPickup;
    }
}
