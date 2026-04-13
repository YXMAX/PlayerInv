package com.playerinv.Manager;

import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.NodeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.NodeUtil.isEmpty;

public class PickupManager {

    private HashMap<String,Boolean> pickupToggleMap;

    private boolean isReverse;

    private HashSet<String> material_list;

    private HashSet<String> lore_list;

    public PickupManager(){
        this.pickupToggleMap = new HashMap<>();
        this.isReverse = false;
        this.material_list = new HashSet<>();
        this.lore_list = new HashSet<>();
    }

    public void init(boolean isReverse, HashSet<String> material_list, HashSet<String> lore_list){
        this.isReverse = isReverse;
        this.lore_list = lore_list;
        this.material_list = new HashSet<>();
        for(String s : material_list){
            if(Material.getMaterial(s) == null){
                continue;
            }
            this.material_list.add(s);
        }
    }

    public void putToggle(Player player){
        CompletableFuture.runAsync(() -> {
            if(NodeUtil.hasPermission(player, PermissionEnums.AUTO_PICKUP)){
                pickupToggleMap.put(player.getUniqueId().toString(), jdbcUtil.getPickupToggle(player.getUniqueId().toString()));
            }
        });
    }

    public void putToggle(String uuid,boolean bool){
        pickupToggleMap.put(uuid, bool);
    }

    public boolean getToggle(String uuid){
        if(!pickupToggleMap.containsKey(uuid)){
            pickupToggleMap.put(uuid, jdbcUtil.getPickupToggle(uuid));
        }
        return pickupToggleMap.get(uuid);
    }

    private boolean containsMaterial(String material){
        if(materialPickupBool){
            return isReverse != material_list.contains(material);
        }
        return false;
    }

    private List<String> containsLore(ItemStack item){
        if(!item.hasItemMeta()){
            return null;
        }
        if(!item.getItemMeta().hasLore()){
            return null;
        }
        List<String> lore = item.getItemMeta().getLore();
        List<String> send = new LinkedList<>();
        if(lore.isEmpty()){
            return null;
        }
        for(String each_lore : lore_list){
            if(isEmpty(each_lore)){
                continue;
            }
            if(lore.stream().anyMatch(str -> str.contains(each_lore))){
                send.add(each_lore);
            }
        }
        return send.isEmpty() ? null : send;
    }

    public boolean runPickup(Player player,ItemStack item){
        if(materialPickupBool && this.containsMaterial(item.getType().toString())){
            operationManager.pickupItems(player,item);
            return true;
        }
        List<String> s = this.containsLore(item);
        if(s != null){
            operationManager.pickupItems(player,item);
            return true;
        }
        return false;
    }
}
