package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.object.cache.VaultStatistics;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.jdbc.JDBCUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.playerinv.manager.PermissionManager.hasPermission;
import static com.playerinv.util.LocaleUtil.*;
import static com.playerinv.util.LocaleUtil.sendMessages;

public class PickupManager {

    private static final ExecutorService singleThreadForPickup = Executors.newSingleThreadExecutor();

    private final HashMap<String,Boolean> pickupToggleMap = new HashMap<>();

    private boolean isReverse;

    private HashSet<String> material_list = new HashSet<>();

    private HashSet<String> lore_list = new HashSet<>();

    public void reload() {
        this.isReverse = PlayerInv.getInstance().getConfig().getBoolean("Inventory.Auto_pickup.list_reverse");
        this.lore_list = new HashSet<>(PlayerInv.getInstance().getConfig().getStringList("Inventory.Auto_pickup.detected_lore"));
        for (String s : PlayerInv.getInstance().getConfig().getStringList("Inventory.Auto_pickup.detected_material").stream().map(String::toUpperCase).collect(Collectors.toCollection(HashSet::new))) {
            if (Material.getMaterial(s) == null) {
                continue;
            }
            this.material_list.add(s);
        }
    }

    public CompletableFuture<Void> putToggle(Player player){
        return CompletableFuture.runAsync(() -> {
            if(hasPermission(player, PermissionEnums.AUTO_PICKUP)){
                pickupToggleMap.put(player.getUniqueId().toString(), JDBCUtil.getPickupToggle(player.getUniqueId().toString()));
            }
        });
    }

    public void removeToggle(Player player){
        pickupToggleMap.remove(player.getUniqueId().toString());
    }

    public void putToggle(String uuid,boolean bool){
        pickupToggleMap.put(uuid, bool);
    }

    public boolean getToggle(String uuid){
        if(!pickupToggleMap.containsKey(uuid)){
            pickupToggleMap.put(uuid, JDBCUtil.getPickupToggle(uuid));
        }
        return pickupToggleMap.get(uuid);
    }

    private boolean isContains(String material){
        if(ConfigConstants.MATERIAL_PICKUP_ENABLED){
            return isReverse != material_list.contains(material);
        }
        return false;
    }

    private List<String> getLore(ItemStack item){
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
            if(this.isStringEmpty(each_lore)){
                continue;
            }
            if(lore.stream().anyMatch(str -> str.contains(each_lore))){
                send.add(each_lore);
            }
        }
        return send.isEmpty() ? null : send;
    }

    public boolean runPickup(Player player,ItemStack item){
        if(ConfigConstants.MATERIAL_PICKUP_ENABLED && this.isContains(item.getType().toString())){
            this.pickupItems(player,item);
            return true;
        }
        List<String> s = this.getLore(item);
        if(s != null){
            this.pickupItems(player,item);
            return true;
        }
        return false;
    }

    public void pickupItems(Player target,ItemStack itemStack){
        this.pickupItemsGlobal(target,itemStack);
    }

    public void pickupItemsGlobal(Player target, ItemStack itemStack){
        int total = itemStack.getAmount();
        singleThreadForPickup.execute(() -> {
            int remain_amount = itemStack.getAmount();
            LinkedHashMap<Integer, VaultStatistics> largeMap = PlayerInv.getInstance().getCacheInventoryManager().get(target).getLarge_map();
            if(!largeMap.isEmpty()){
                for(Map.Entry<Integer,VaultStatistics> entry : largeMap.entrySet()){
                    VaultStatistics vaultStatistics = entry.getValue();
                    Inventory inventory = vaultStatistics.getInventory();
                    int else_amount = itemStack.getAmount();
                    HashMap<Integer, ItemStack> remain_map = inventory.addItem(itemStack);
                    if(remain_map.isEmpty()){
                        remain_amount = 0;
                        vaultStatistics.uploadAsync();
                        PlayerInv.getScheduler().asyncScheduler().run(task -> {
                            this.sendAutoPickupNotice(1,target,itemStack,total,entry.getKey());
                            PlayerInv.getInstance().getSoundManager().autoPickup(target,vaultStatistics.getType(),vaultStatistics.getRemain());
                        });
                        break;
                    } else {
                        if(itemStack.getAmount() == else_amount){
                            continue;
                        }
                        vaultStatistics.uploadAsync();
                        continue;
                    }
                }
            }
            LinkedHashMap<Integer, VaultStatistics> mediumMap = PlayerInv.getInstance().getCacheInventoryManager().get(target).getMedium_map();
            if(remain_amount != 0 && !mediumMap.isEmpty()){
                for(Map.Entry<Integer,VaultStatistics> entry : mediumMap.entrySet()){
                    VaultStatistics vaultStatistics = entry.getValue();
                    Inventory inventory = vaultStatistics.getInventory();
                    int else_amount = itemStack.getAmount();
                    HashMap<Integer, ItemStack> remain_map = inventory.addItem(itemStack);
                    if(remain_map.isEmpty()){
                        remain_amount = 0;
                        vaultStatistics.uploadAsync();
                        PlayerInv.getScheduler().asyncScheduler().run(task -> {
                            this.sendAutoPickupNotice(2,target,itemStack,total,entry.getKey());
                            PlayerInv.getInstance().getSoundManager().autoPickup(target,vaultStatistics.getType(),vaultStatistics.getRemain());
                        });
                        break;
                    } else {
                        if(itemStack.getAmount() == else_amount){
                            continue;
                        }
                        vaultStatistics.uploadAsync();
                        continue;
                    }
                }
            }
            if(remain_amount != 0){
                if(ConfigConstants.ACTIONBAR_AUTO_PICKUP){
                    sendActionBar(target,LocaleEnums.Auto_pickup_vault_full);
                } else {
                    sendMessages(target,LocaleEnums.Auto_pickup_vault_full);
                }
                ItemStack item = itemStack.clone();
                item.setAmount(remain_amount);
                HashMap<Integer, ItemStack> map = target.getInventory().addItem(itemStack);
                if(!map.isEmpty()){
                    PlayerInv.getScheduler().entitySpecificScheduler(target).run(task -> {
                        target.getWorld().dropItemNaturally(target.getLocation(), itemStack);
                    },null);
                }
            }
        });
    }

    private void sendAutoPickupNotice(int type, Player target, ItemStack item, int total, int vault_num){
        String vault_type = "large";
        if(type == 2){
            vault_type = "medium";
        }
        String msg = PlayerInv.getInstance().getLocaleConfig().getString("Messages.Auto_pickup_" + vault_type + "_notice");
        msg = MessageUtil.replaceMessageVariable(msg,
                "%vault_num%", String.valueOf(vault_num),
                "%item_amount%", String.valueOf(total),
                "%vault_name%", PlayerInv.getInstance().getVaultAttributesManager().replaceVaultName(target,type,vault_num)
        );
        if(!ConfigConstants.ACTIONBAR_AUTO_PICKUP){
            msg = MessageUtil.getPrefix() + msg;
        }
        String[] parts = msg.split("%item%", 2);
        TranslatableComponent translate = new TranslatableComponent(PlayerInv.getInstance().getTranslator().itemTranslate(item,target));
        BaseComponent[] components = new ComponentBuilder("")
                .append(TextComponent.fromLegacyText(MessageUtil.parseToLegacyColor(parts[0])))
                .append(translate)
                .append(parts.length > 1 ? TextComponent.fromLegacyText(MessageUtil.parseToLegacyColor(parts[1])) : TextComponent.fromLegacyText(""))
                .create();
        if(ConfigConstants.ACTIONBAR_AUTO_PICKUP){
            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
        } else {
            target.spigot().sendMessage(components);
        }
    }

    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void closeThreadPool(){
        singleThreadForPickup.shutdown();
    }
}
