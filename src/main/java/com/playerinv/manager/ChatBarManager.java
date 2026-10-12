package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.object.cache.CacheInventory;
import com.playerinv.object.cache.ItemStatistics;
import com.playerinv.object.chat.TypeAttribute;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static com.playerinv.util.LocaleUtil.sendMessages;

public class ChatBarManager {

    private final HashMap<UUID, TypeAttribute> typeAttributeMap = new HashMap<>();

    public void addAttributeAlter(UUID uuid,TypeAttribute typeAttribute){
        this.typeAttributeMap.put(uuid,typeAttribute);
    }

    public void removeAttributeAlter(UUID uuid){
        this.typeAttributeMap.remove(uuid);
    }

    public void removeTypeTemp(Player player){
        this.typeAttributeMap.remove(player.getUniqueId());
    }

    public boolean runChatBarOperation(Player player, String msg){
        UUID uuid = player.getUniqueId();
        TypeAttribute typeAttribute = this.typeAttributeMap.get(uuid);
        if(typeAttribute != null){
            this.removeAttributeAlter(uuid);
            this.returnVaultMenu(player,typeAttribute.getMenu_name());
            this.runAttributeOperation(player,msg,typeAttribute.getVault_type(),typeAttribute.getVault_num());
            return true;
        }
        return false;
    }

    public void runAttributeOperation(Player player,String msg,int type,int num){
        if(msg.equalsIgnoreCase("#")){
            return;
        }
        if(msg.equalsIgnoreCase("@")){
            PlayerInv.getInstance().getVaultAttributesManager().get(player).resetAttribute(type,num);
            PlayerInv.getInstance().getCacheInventoryManager().resetCacheVaultTitle(player,type,num);
            return;
        }
        PlayerInv.getInstance().getVaultAttributesManager().get(player).addAttribute(type,num,msg);
        PlayerInv.getInstance().getCacheInventoryManager().resetCacheVaultTitle(player,type,num);
        if(type == 1){
            sendMessages(player,
                    LocaleEnums.Vault_attribute_change_large_success,
                    "%vault_num%",String.valueOf(num),
                    "%vault_name%",msg);
        } else {
            sendMessages(player,
                    LocaleEnums.Vault_attribute_change_medium_success,
                    "%vault_num%",String.valueOf(num),
                    "%vault_name%",msg);
        }
        return;
    }

    private void returnVaultMenu(Player player, String menu_name){
        PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
            PlayerInv.getInstance().getMenuManager().openVaultMenu(player,menu_name,false);
        },null,2);
    }
}
