package com.playerinv.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.playerinv.PlayerInv.has_update;
import static com.playerinv.PlayerInv.newer_version;
import static com.playerinv.PluginSet.*;

public class UpdateNotice implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.isOp()){
            if(getUpdateBool() && has_update){
                player.sendMessage(color(prefix() + "&bThere is a newer version (" + newer_version + ") available:"));
                player.sendMessage(color("&e https://www.spigotmc.org/resources/playerinv.114372/"));
            }
        }
    }
}
