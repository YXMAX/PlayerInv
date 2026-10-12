package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.api.VaultType;
import com.playerinv.api.event.VaultOpenEvent;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.listener.channel.PluginChannelListener;
import com.playerinv.object.cache.VaultStatistics;
import com.playerinv.service.serializer.InventorySerializer;
import com.playerinv.gui.holder.impl.vault.CheckVaultHolder;
import com.playerinv.gui.holder.impl.vault.StorageVaultHolder;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.object.container.InventoryContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.playerinv.enums.LocaleEnums.*;
import static com.playerinv.manager.PermissionManager.hasVaultPermission;
import static com.playerinv.util.LocaleUtil.sendMessages;

public class VaultManager {

    private static final ExecutorService singleThreadForDeathStore = Executors.newSingleThreadExecutor();

    private int LARGE_VAULT_AMOUNT;

    private int MEDIUM_VAULT_AMOUNT;

    private boolean VAULT_PLACE;

    private boolean VAULT_PICKUP;

    private HashSet<String> vaultMaterialBlackList = new HashSet<>();
    private HashSet<String> vaultLoreBlackList = new HashSet<>();

    public void reload(){
        LARGE_VAULT_AMOUNT = PlayerInv.getInstance().getConfig().getInt("Vault.Large_amount",36);
        MEDIUM_VAULT_AMOUNT = PlayerInv.getInstance().getConfig().getInt("Vault.Medium_amount",27);
        VAULT_PLACE = PlayerInv.getInstance().getConfig().getBoolean("Vault.Event.Place");
        VAULT_PICKUP = PlayerInv.getInstance().getConfig().getBoolean("Vault.Event.Pickup");
        vaultMaterialBlackList = PlayerInv.getInstance().getConfig()
                .getStringList("Inventory.Blacklist")
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(HashSet::new));
        vaultLoreBlackList = PlayerInv.getInstance().getConfig()
                .getStringList("Inventory.Lore_blacklist")
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public boolean isMaterialBlacklist(Material material){
        return this.vaultMaterialBlackList.contains(material.toString());
    }

    public boolean isLoreBlacklist(List<String> lore){
        if(lore == null) return false;
        if(lore.isEmpty()) return false;
        for(String each_lore : this.vaultLoreBlackList){
            if(each_lore == null || each_lore.isEmpty()) continue;
            if(lore.stream().anyMatch(str -> str.contains(each_lore))){
                return true;
            }
        }
        return false;
    }

    public boolean isAllowVaultPlace(){
        return VAULT_PLACE;
    }

    public boolean isAllowVaultPickup(){
        return VAULT_PICKUP;
    }

    public boolean isDisallowAnEvent(){
        return !VAULT_PICKUP || !VAULT_PLACE;
    }

    public int getLargeVaultAmount() {
        return LARGE_VAULT_AMOUNT;
    }

    public int getMediumVaultAmount() {
        return MEDIUM_VAULT_AMOUNT;
    }

    public int getVaultAmount(int type){
        if(type == 2){
            return MEDIUM_VAULT_AMOUNT;
        }
        return LARGE_VAULT_AMOUNT;
    }

    public int getTotalVaultAmount(){
        return LARGE_VAULT_AMOUNT + MEDIUM_VAULT_AMOUNT;
    }

    public void checkVault(Player checker, Inventory menu, OfflinePlayer target, int vault_type, int vault_num){
        if(!JDBCUtil.isConnected()){
            sendMessages(checker,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        CompletableFuture.runAsync(() -> {
            String string = JDBCUtil.getVaultString(vault_type, target.getUniqueId().toString(), vault_num);
            CheckVaultHolder checkVaultHolder = InventorySerializer.deserializeCheckInventory(checker,vault_type,vault_num,target,menu,string);
            int delay = 1;
            if(checkVaultHolder != null){
                if(checkVaultHolder.hasError()){
                    sendMessages(checker, LocaleEnums.Check_vault_error_exist);
                    delay = 100;
                }
                PlayerInv.getScheduler().entitySpecificScheduler(checker).runDelayed(() -> {
                    checker.openInventory(checkVaultHolder.getInventory());
                },null,delay);
            }
        });
    }

    public void openVault(Player player, int vault_type, int vault_num, boolean isCommand, String fileName){
        boolean perms = hasVaultPermission(player, vault_type, vault_num);
        CompletableFuture.runAsync(() -> {
            if(!JDBCUtil.isConnected()){
                sendMessages(player, Connection_invalid_refuse);
                return;
            }
            if(!PlayerInv.getInstance().getCacheInventoryManager().isCacheReady(player)){
                sendMessages(player, Cache_not_ready);
                return;
            }
            boolean only_pickup = false;
            InventoryContainer content = PlayerInv.getInstance().getCacheInventoryManager().getInventoryContainer(player,vault_type,vault_num);
            if(content != null){
                if(content.isNotReady()){
                    sendMessages(player, Cache_not_ready);
                    return;
                }
                if(!perms){
                    if(ConfigConstants.ALLOW_PICKUP_WHEN_NO_VAULT_PERMISSION){
                        only_pickup = true;
                    } else {
                        sendMessages(player,No_permission_vault);
                        return;
                    }
                }
                if(content.hasError()){
                    content.retryInventory(player,vault_num,isCommand,only_pickup,fileName);
                    return;
                }
            } else {
                if(!perms){
                    sendMessages(player,No_permission_vault);
                    return;
                }
                JDBCUtil.insertVaultAsync(vault_type,player.getUniqueId().toString(),vault_num);
                content = new InventoryContainer(vault_type);
                content.setInventory(player,vault_num);
                PlayerInv.getInstance().getCacheInventoryManager().insertInventoryContainer(player,vault_type,vault_num,content);
            }
            content.setHolderSettings(isCommand,only_pickup,fileName);
            Inventory vault = content.getInventory();
            PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                PlayerInv.getInstance().getSoundManager().vaultOpen(player);
                VaultOpenEvent event = new VaultOpenEvent(player,vault,VaultType.fromInteger(vault_type),vault_num);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                MessageUtil.sendDetail("玩家 " + player.getName() + " 打开个人仓库: " + vault_type + "//" + player.getUniqueId() + ":" + vault_num);
                player.openInventory(vault);
            },null,1);
        });
    }

    public void openVaultIgnorePerms(Player player, int vault_type, int vault_num, CommandSender sender){
        if(player.getOpenInventory().getTopInventory().getHolder() instanceof StorageVaultHolder){
            player.closeInventory();
        }
        CompletableFuture.runAsync(() -> {
            if(!JDBCUtil.isConnected()){
                sendMessages(player, Connection_invalid_refuse);
                return;
            }
            if(PlayerInv.getInstance().getCacheInventoryManager().isCacheBroken(player)){
                sendMessages(sender, Cache_not_ready);
                return;
            }
            InventoryContainer content = PlayerInv.getInstance().getCacheInventoryManager().getInventoryContainer(player,vault_type,vault_num);
            if(content != null){
                if(content.isNotReady()) {
                    sendMessages(player, Cache_not_ready);
                    return;
                }
                if(content.hasError()){
                    content.retryInventory(player,vault_num,true,false,null);
                    return;
                }
            } else {
                JDBCUtil.insertVaultAsync(vault_type,player.getUniqueId().toString(),vault_num);
                content = new InventoryContainer(vault_type);
                content.setInventory(player,vault_num);
                PlayerInv.getInstance().getCacheInventoryManager().insertInventoryContainer(player,vault_type,vault_num,content);
            }
            content.setHolderSettings(true,false,null);
            Inventory vault = content.getInventory();
            sendMessages(sender,Open_vault_medium_success,"%player%",player.getName(),"%vault_num%", String.valueOf(vault_num));
            PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
                PlayerInv.getInstance().getSoundManager().vaultOpen(player);
                VaultOpenEvent event = new VaultOpenEvent(player,vault, VaultType.fromInteger(vault_type),vault_num);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                MessageUtil.sendDetail("玩家 " + player.getName() + " 打开个人仓库: " + vault_type + "//" + player.getUniqueId() + ":" + vault_num);
                player.openInventory(vault);
            },null,1);
        });
    }

    public void uploadVaultByCheck(int type, String uuid, Inventory inventory, Integer num){
        CompletableFuture.runAsync(() -> {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if(player != null){
                PlayerInv.getInstance().getCacheInventoryManager().syncVaultContentsAndUpload(player,type,num,inventory);
                return;
            }
            if(JDBCUtil.MYSQL_STATUS){
                JDBCUtil.updateVaultStringAsync(type,uuid,InventorySerializer.serializeInventory(inventory.getContents()),num).thenRun(() -> {
                    PluginChannelListener.sendCheckResult(type,uuid,num);
                });
            }
        });
    }

    public void sendVaultByDeathStore(Player target, List<ItemStack> list){
        List<ItemStack> item_list = new ArrayList<>(list);
        singleThreadForDeathStore.execute(new Runnable() {
            @Override
            public void run() {
                LinkedHashMap<Integer, VaultStatistics> largeMap = PlayerInv.getInstance().getCacheInventoryManager().get(target).getLarge_map();
                LinkedHashMap<Integer, VaultStatistics> mediumMap = PlayerInv.getInstance().getCacheInventoryManager().get(target).getMedium_map();
                if(!largeMap.isEmpty()){
                    for(Map.Entry<Integer,VaultStatistics> entry : largeMap.entrySet()){
                        Inventory inventory = entry.getValue().getInventory();
                        if(inventory.firstEmpty() == -1){
                            continue;
                        }
                        Iterator<ItemStack> iterator = item_list.iterator();
                        while(iterator.hasNext()){
                            if(inventory.firstEmpty() == -1){
                                break;
                            }
                            ItemStack item = iterator.next();
                            int else_amount = item.getAmount();
                            HashMap<Integer, ItemStack> remain_map = inventory.addItem(item);
                            if(remain_map.isEmpty()){
                                iterator.remove();
                                entry.getValue().uploadAsync();
                            } else {
                                iterator.remove();
                                item_list.add(item);
                                if(item.getAmount() == else_amount){
                                    continue;
                                }
                                entry.getValue().uploadAsync();
                            }
                        }
                        if(item_list.isEmpty()){
                            sendMessages(target,LocaleEnums.Player_death_store_notice,"%vault_num%",String.valueOf(entry.getKey()));
                            return;
                        }
                    }
                }
                if(!mediumMap.isEmpty()){
                    for(Map.Entry<Integer,VaultStatistics> entry : mediumMap.entrySet()){
                        Inventory inventory = entry.getValue().getInventory();
                        if(inventory.firstEmpty() == -1){
                            continue;
                        }
                        Iterator<ItemStack> iterator = item_list.iterator();
                        while(iterator.hasNext()){
                            if(inventory.firstEmpty() == -1){
                                break;
                            }
                            ItemStack item = iterator.next();
                            int else_amount = item.getAmount();
                            HashMap<Integer, ItemStack> remain_map = inventory.addItem(item);
                            if(remain_map.isEmpty()){
                                iterator.remove();
                                entry.getValue().uploadAsync();
                            } else {
                                iterator.remove();
                                item_list.add(item);
                                if(item.getAmount() == else_amount){
                                    continue;
                                }
                                entry.getValue().uploadAsync();
                            }
                        }
                        if(item_list.isEmpty()){
                            sendMessages(target,LocaleEnums.Player_death_store_notice,"%vault_num%",String.valueOf(entry.getKey()));
                            return;
                        }
                    }
                }
                if(!item_list.isEmpty()){
                    sendMessages(target,LocaleEnums.Player_death_store_full);
                    PlayerInv.getScheduler().entitySpecificScheduler(target).run(task -> {
                        for(ItemStack item : item_list){
                            target.getInventory().addItem(item);
                        }
                    },null);
                }
            }
        });
    }

    public void closeThreadPool(){
        singleThreadForDeathStore.shutdown();
    }
}
