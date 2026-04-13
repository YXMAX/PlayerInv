package com.playerinv.Util.InvHolder;

import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Scheduler.ReturnScheduler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static com.playerinv.Util.LoadUtil.*;

public class VaultHolder_Large implements InventoryHolder {
    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    private int num;

    private String menu_name;

    private boolean isCommand;

    private boolean onlyPickup;

    public VaultHolder_Large(int num,String menu_name,boolean isCommand,boolean onlyPickup) {
        this.menu_name = menu_name;
        this.num = num;
        this.isCommand = isCommand;
        this.onlyPickup = onlyPickup;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public int getNum() {
        return num;
    }

    public boolean isCommand() {
        return isCommand;
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
