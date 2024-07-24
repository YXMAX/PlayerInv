package com.playerinv.ReturnMain;

import com.playerinv.MainGUI.MainMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.playerinv.PlayerInv.plugin;

public class OpenInventoryRunnable_Spigot {
    public static void ReturnMain(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(MainMenu.Main_GUI(player));
                this.cancel();
            }
        }.runTaskLater(plugin, 2);
    }
}
