package com.playerinv.Listener;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.InvHolder.VaultHolder_Large;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Operation.TypeAttribute;
import com.playerinv.Util.Scheduler.ReturnScheduler;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.*;
import static com.playerinv.Util.LocaleUtil.sendMessages;
import static com.playerinv.Util.NodeUtil.color;

public class PlayerListener implements Listener {

    public static HashMap<Player, TypeAttribute> typeAttributeMap = new HashMap<>();

    private void removeTypeTemp(Player player){
        typeAttributeMap.remove(player);
    }


    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event){
        Boolean value = false;
        if(isBelow113){
            String v = event.getEntity().getWorld().getGameRuleValue("keepInventory");
            value = Boolean.parseBoolean(v);
        } else {
            value = event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        }
        if(deathStoreBool && (event.getEntity().getType() == EntityType.PLAYER)) {
            if (value && !deathStoreIgnore) {
                return;
            } else {
                if(event.getEntity().getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large){
                    event.getEntity().closeInventory();
                }
                if (NodeUtil.hasPermission(event.getEntity(), PermissionEnums.DEATH_STORE)) {
                    List<ItemStack> item_list = event.getDrops();
                    operationManager.sendVault_deathStore(event.getEntity(), item_list);
                    event.getDrops().clear();
                } else {
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onOpenEnderChest(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!enderChestOpenBool){
            return;
        }
        if(event.getClickedBlock() == null){
            return;
        }
        if(event.getClickedBlock().getType() == null){
            return;
        }
        if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST) && player.hasPermission("playerinv.enderchest.open")){
            if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                return;
            }
            event.setCancelled(true);
            menuManager.openVaultMenu(player,null);
        }
    }

    @EventHandler
    public void onUseVoucher(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(!voucherBool){
            return;
        }
        if(p.getEquipment().getItemInMainHand() == null){
            return;
        }
        if(!p.getEquipment().getItemInMainHand().hasItemMeta()){
            return;
        }
        if(!p.getEquipment().getItemInMainHand().getItemMeta().hasDisplayName()){
            return;
        }
        ItemStack item = p.getEquipment().getItemInMainHand();
        if(item.getType().toString().equalsIgnoreCase(voucherLargeMaterial) || item.getType().toString().equalsIgnoreCase(voucherMediumMaterial)){
            permissionManager.runVoucher(p,item);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = (Player) event.getPlayer();
        operationManager.initAttributes(player);
        operationManager.loadCacheInventory(player);
        operationManager.loadExpiry(player);
        if(player.isOp()){
            if(checkUpdate && has_update){
                player.sendMessage(color(prefix + "&bThere is a newer version (" + newer_version + ") available:"));
                player.sendMessage(color("&e https://www.spigotmc.org/resources/playerinv.114372/"));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = (Player) event.getPlayer();
        this.removeTypeTemp(player);
        operationManager.removeAttributes(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = (Player) event.getPlayer();
        if(typeAttributeMap.containsKey(player)){
            event.setCancelled(true);
            String message = event.getMessage();
            TypeAttribute typeAttribute = typeAttributeMap.get(player);
            int type = typeAttribute.getVault_type();
            int num = typeAttribute.getVault_num();
            if(message.equalsIgnoreCase("#")){
                ReturnScheduler.returnVaultMenu(player,typeAttribute.getMenu_name());
                typeAttributeMap.remove(player);
                return;
            }
            if(message.equalsIgnoreCase("@")){
                vaultAttributesManager.get(player).resetAttribute(type,num);
                typeAttributeMap.remove(player);
                ReturnScheduler.returnVaultMenu(player,typeAttribute.getMenu_name());
                return;
            }
            vaultAttributesManager.get(player).addAttribute(type,num,message);
            typeAttributeMap.remove(player);
            scheduler.scheduling().entitySpecificScheduler(player).run(task -> {
                ReturnScheduler.returnVaultMenu(player,typeAttribute.getMenu_name());
            },null);
            if(type == 1){
                sendMessages(player, LocaleEnums.Vault_attribute_change_large_success,"%vault_num%",String.valueOf(num),"%vault_name%",message.replaceAll("%vault_num%",String.valueOf(num)));
            } else {
                sendMessages(player,LocaleEnums.Vault_attribute_change_medium_success,"%vault_num%",String.valueOf(num),"%vault_name%",message.replaceAll("%vault_num%",String.valueOf(num)));
            }
            return;
        }
    }
}
