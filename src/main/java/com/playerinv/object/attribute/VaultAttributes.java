package com.playerinv.object.attribute;

import com.playerinv.PlayerInv;
import com.playerinv.util.jdbc.JDBCUtil;

import java.util.HashMap;
import java.util.UUID;

public class VaultAttributes {

    private HashMap<Integer,String> large_attributes;

    private HashMap<Integer,String> medium_attributes;

    private UUID uuid;

    public VaultAttributes(UUID uuid, String[] attributes) {
        this.uuid = uuid;
        this.large_attributes = new HashMap<>();
        this.medium_attributes = new HashMap<>();
        this.large_attributes = PlayerInv.getInstance().getGsonParser().parseToVaultAttributes(attributes[0]);
        this.medium_attributes = PlayerInv.getInstance().getGsonParser().parseToVaultAttributes(attributes[1]);
    }

    public String getAttribute(int type, int vault_num) {
        if(type == 2){
            return this.medium_attributes.get(vault_num);
        }
        return this.large_attributes.get(vault_num);
    }

    public void addAttribute(int type, int vault_num, String attribute) {
        if(type == 2){
            medium_attributes.put(vault_num,attribute);
        } else {
            large_attributes.put(vault_num,attribute);
        }
        this.updateAttribute(type);
    }

    public void resetAttribute(int type, int vault_num) {
        if(type == 2){
            medium_attributes.remove(vault_num);
        } else {
            large_attributes.remove(vault_num);
        }
        this.updateAttribute(type);
    }

    public void updateAttribute(int type) {
        PlayerInv.getScheduler().asyncScheduler().run(task -> {
            if(type == 1){
                JDBCUtil.updateVaultAttributeAsync(type,uuid.toString(),PlayerInv.getInstance().getGsonParser().toJson(this.large_attributes));
            } else {
                JDBCUtil.updateVaultAttributeAsync(type,uuid.toString(),PlayerInv.getInstance().getGsonParser().toJson(this.medium_attributes));
            }
        });
    }
}
