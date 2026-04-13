package com.playerinv.Util.Scheduler;

import com.playerinv.GUI.*;
import org.bukkit.entity.Player;

import static com.playerinv.PlayerInv.menuManager;
import static com.playerinv.Util.InitUtil.scheduler;

public class ReturnScheduler {

    public static void returnVaultMenu(Player player, String menu_name){
        scheduler.scheduling().entitySpecificScheduler(player).runDelayed(task -> {
            menuManager.openVaultMenu(player,menu_name,false);
        },null,1);
    }
}
