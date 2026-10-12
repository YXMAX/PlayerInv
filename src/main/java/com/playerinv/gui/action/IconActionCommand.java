package com.playerinv.gui.action;

import com.playerinv.PlayerInv;
import com.playerinv.constants.DependencyConstants;
import com.playerinv.util.message.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IconActionCommand {

    // 1: 玩家
    // 2: OP
    // 3: 控制台
    private int type;

    private String command;

    public IconActionCommand(String command) {
        if(command != null){
            if(command.contains("[") || command.contains("]")){
                String type = command.substring(command.indexOf("[") + 1, command.indexOf("]"));
                switch(type){
                    case "console":
                        this.type = 3;
                        command = command.replace("[console]"," ");
                        break;
                    case "op":
                        this.type = 2;
                        command = command.replace("[op]"," ");
                        break;
                    case "player":
                        this.type = 1;
                        command = command.replace("[player]"," ");
                        break;
                }
                this.command = command.trim();
            } else {
                this.type = 1;
                this.command = command.trim();
            }
        }
    }

    public IconActionCommand(String command,boolean send) {
        if(command != null){
            if(command.contains("[") || command.contains("]")){
                String type = command.substring(command.indexOf("[") + 1, command.indexOf("]"));
                switch(type){
                    case "console":
                        this.type = 3;
                        command = command.replace("[console]"," ");
                        break;
                    case "op":
                        this.type = 2;
                        command = command.replace("[op]"," ");
                        break;
                    case "player":
                        this.type = 1;
                        command = command.replace("[player]"," ");
                        break;
                }
                this.command = command.trim();
            } else {
                this.type = 3;
                this.command = command.trim();
            }
        }
    }

    public void runCommandToPlayer(Player player,String... replace){
        if(this.command == null) return;
        String result = new String(this.command);
        if(DependencyConstants.PAPI_ENABLED){
             result = PlaceholderAPI.setPlaceholders(player,result);
        }
        result = result.replace("%player%",player.getName());
        result = MessageUtil.replaceMessageVariable(result,replace);
        result = result.trim();
        if(this.type == 3){
            String s = result;
            PlayerInv.getScheduler().globalRegionalScheduler().run(task -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),s);
            });
        } else if(this.type == 2){
            if(player.isOp()){
                player.performCommand(result);
                return;
            }
            try {
                player.setOp(true);
                player.performCommand(result);
            } finally {
                player.setOp(false);
            }
        } else {
            player.performCommand(result);
        }
    }
}
