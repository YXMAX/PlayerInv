package com.playerinv.Util.Object.Cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.HashMap;

import static com.playerinv.PlayerInv.jdbcUtil;
import static com.playerinv.Util.InitUtil.scheduler;

public class VaultAttributes {

    private HashMap<Integer,String> large_attributes;

    private HashMap<Integer,String> medium_attributes;

    private Player player;

    public VaultAttributes(Player player,String[] attributes) {
        this.player = player;
        this.large_attributes = new HashMap<>();
        this.medium_attributes = new HashMap<>();
        Type type = new TypeToken<HashMap<Integer,String>>(){}.getType();
        this.large_attributes = new Gson().fromJson(attributes[0], type);
        this.medium_attributes = new Gson().fromJson(attributes[1], type);
    }

    public Player getPlayer() {
        return player;
    }

    public String getAttribute(int type, int vault_num) {
        switch (type) {
            case 2:
                return this.medium_attributes.get(vault_num);
            case 1:
            default:
                return this.large_attributes.get(vault_num);
        }
    }

    public void addAttribute(int type, int vault_num, String attribute) {
        switch (type) {
            case 2:
                medium_attributes.put(vault_num,attribute);
                break;
            case 1:
            default:
                large_attributes.put(vault_num,attribute);
                break;
        }
        this.updateAttribute(type);
    }

    public void resetAttribute(int type, int vault_num) {
        switch (type) {
            case 2:
                medium_attributes.remove(vault_num);
                break;
            case 1:
            default:
                large_attributes.remove(vault_num);
                break;
        }
        this.updateAttribute(type);
    }

    public void updateAttribute(int type) {
        scheduler.scheduling().asyncScheduler().run(task -> {
            if(type == 1){
                jdbcUtil.updateVaultAttributeAsync(type,player.getUniqueId().toString(),new Gson().toJson(this.large_attributes));
            } else {
                jdbcUtil.updateVaultAttributeAsync(type,player.getUniqueId().toString(),new Gson().toJson(this.medium_attributes));
            }
        });
    }
}
