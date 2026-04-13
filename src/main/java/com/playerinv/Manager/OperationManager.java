package com.playerinv.Manager;

import com.google.common.collect.Iterables;
import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Util.InitUtil;
import com.playerinv.Util.Object.Cache.CacheInventory;
import com.playerinv.Util.Object.Cache.VaultStatistics;
import com.playerinv.Util.PlaceHolder.PlaceholderTemp;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.InitUtil.isMySQL;
import static com.playerinv.Util.JDBCUtil.ds;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.LocaleUtil.*;
import static com.playerinv.Util.NodeUtil.*;
import static org.bukkit.Bukkit.getServer;

public class OperationManager {

    private static ExecutorService singleThreadForPickup = Executors.newSingleThreadExecutor();

    private static ExecutorService singleThreadForDeathStore = Executors.newSingleThreadExecutor();

    public void initAttributes(Player player) {
        pickupManager.putToggle(player);
        vaultAttributesManager.insert(player);
    }
    public void loadCacheInventory(Player player){
        cacheInventoryManager.put(player,new CacheInventory(player));
    }

    public void removeAttributes(Player player){
        cacheInventoryManager.remove(player);
        vaultAttributesManager.remove(player);
    }

    public void loadExpiry(Player player){
        if(!luckperms_givePermissions){
            return;
        }
        CompletableFuture.runAsync(() -> {
            permissionManager.getLuckpermsService().loadExpiryMap(player);
        });
    }

    public PlaceholderTemp getPlaceholders(Player player,int type){
        if(!playerExpiryMap_Large.containsKey(player.getUniqueId().toString())){
            permissionManager.getLuckpermsService().loadExpiryMap(player);
        }
        if(type == 1){
            return playerExpiryMap_Large.get(player.getUniqueId().toString());
        } else {
            return playerExpiryMap_Medium.get(player.getUniqueId().toString());
        }
    }

    public void sendCheckResult(int type,String uuid,int num){
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteOut);

            // 指定使用 Forward
            out.writeUTF("Forward");
            // 目标服务器（例如 "lobby" 或 "ALL"）
            out.writeUTF("ALL");
            // 自定义子频道名称（你自己定义）
            out.writeUTF("PlayerInvCheck");

            // 实际要发送的数据
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream msgOut = new DataOutputStream(msgBytes);

            msgOut.writeUTF(type + "@" + uuid + "@" + num);  // 发送字符串
            msgOut.writeInt(1);      // 也可以发送更多数据

            // 写入数据长度 + 数据体
            out.writeShort(msgBytes.toByteArray().length);
            out.write(msgBytes.toByteArray());

            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if(player == null){
                return;
            }
            player.sendPluginMessage(plugin, "BungeeCord", byteOut.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveCheckResult(String result){
        String[] split = result.split("@");
        int type = Integer.parseInt(split[0]);
        String uuid = split[1];
        int num = Integer.parseInt(split[2]);
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player == null){
            return;
        }
        cacheInventoryManager.syncInventoryFromDatabase(player,type,num);
    }

    public void updateVaultToDatabase(int type, String uuid, Inventory inventory, Integer num){
        CompletableFuture.runAsync(() -> {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if(player != null){
                cacheInventoryManager.syncInventoryAndUpload(player,type,num,inventory);
                return;
            }
            if(isMySQL){
                jdbcUtil.updateVaultAsync(type,uuid,inventoryToBase64(inventory),num);
            }
        });
    }

    public void shutdownPlugin(){
        sendConsole("&f插件卸载...","&fPlugin disabled...");
        sendConsole("保存仓库缓存中...","Saving vault cache...");
        cacheInventoryManager.uploadAll().thenRun(() -> {
            sendConsole("保存仓库缓存成功","Save vault cache success");
            if(isMySQL){
                ds.close();
                sendConsole("&f已断开与 MySQL 数据库的连接...","&fDisconnected from MySQL database...");
            } else {
                ds.close();
                sendConsole("&f已断开与 本地 数据库的连接...","&fDisconnected from local database...");
            }
            singleThreadForPickup.shutdown();
            singleThreadForDeathStore.shutdown();
        });
    }

    public boolean runCheckTable(boolean mysql){
        if (mysql) {
            ds = new InitUtil().getMySQLDataSource();
            if(ds == null){
                return false;
            }
            isMySQL = true;
        } else {
            ds = new InitUtil().getDataSource();
            if(ds == null){
                return false;
            }
        }
        CompletableFuture.runAsync(() -> {
            jdbcUtil.createLargeTable();
            jdbcUtil.createMediumTable();
            jdbcUtil.createVaultNameTable();
            jdbcUtil.createPickupToggleTable();
            if(mysql){
                jdbcUtil.fixMySQLDataType_Large();
                jdbcUtil.fixMySQLDataType_Medium();
            }
            jdbcUtil.createIndex();
            jdbcUtil.createUniqueIndex();
        });
        return true;
    }

    public void pickupItems(Player target,ItemStack itemStack){
        this.sendVault_pickup(target,itemStack);
    }

    private void sendVault_pickup(Player target, ItemStack itemStack){
        int total = itemStack.getAmount();
        singleThreadForPickup.execute(() -> {
            int remain_amount = itemStack.getAmount();
            LinkedHashMap<Integer, VaultStatistics> largeMap = cacheInventoryManager.get(target).getLarge_map();
            if(!largeMap.isEmpty()){
                for(Map.Entry<Integer,VaultStatistics> entry : largeMap.entrySet()){
                    VaultStatistics vaultStatistics = entry.getValue();
                    Inventory inventory = vaultStatistics.getInventory();
                    int else_amount = itemStack.getAmount();
                    HashMap<Integer, ItemStack> remain_map = inventory.addItem(itemStack);
                    if(remain_map.isEmpty()){
                        remain_amount = 0;
                        vaultStatistics.uploadAsync();
                        scheduler.scheduling().asyncScheduler().run(task -> {
                            sendAutoPickupNotice(1,target,itemStack,total,entry.getKey(),!autoPickupActionBar);
                            soundManager.playSoundEffect(target,soundManager.autoPickup);
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
            LinkedHashMap<Integer, VaultStatistics> mediumMap = cacheInventoryManager.get(target).getMedium_map();
            if(remain_amount != 0 && !mediumMap.isEmpty()){
                for(Map.Entry<Integer,VaultStatistics> entry : mediumMap.entrySet()){
                    VaultStatistics vaultStatistics = entry.getValue();
                    Inventory inventory = vaultStatistics.getInventory();
                    int else_amount = itemStack.getAmount();
                    HashMap<Integer, ItemStack> remain_map = inventory.addItem(itemStack);
                    if(remain_map.isEmpty()){
                        remain_amount = 0;
                        vaultStatistics.uploadAsync();
                        scheduler.scheduling().asyncScheduler().run(task -> {
                            sendAutoPickupNotice(2,target,itemStack,total,entry.getKey(),!autoPickupActionBar);
                            soundManager.playSoundEffect(target,soundManager.autoPickup);
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
                if(autoPickupActionBar){
                    target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color(getLocaleMessages(LocaleEnums.Auto_pickup_vault_full))));
                } else {
                    sendMessages(target,LocaleEnums.Auto_pickup_vault_full);
                }
                ItemStack item = itemStack.clone();
                item.setAmount(remain_amount);
                HashMap<Integer, ItemStack> map = target.getInventory().addItem(itemStack);
                if(!map.isEmpty()){
                    target.getWorld().dropItemNaturally(target.getLocation(), itemStack);
                }
            }
        });
    }

    public void sendVault_deathStore(Player target, List<ItemStack> list){
        List<ItemStack> item_list = new ArrayList<>(list);
        singleThreadForDeathStore.execute(new Runnable() {
            @Override
            public void run() {
                LinkedHashMap<Integer, VaultStatistics> largeMap = cacheInventoryManager.get(target).getLarge_map();
                LinkedHashMap<Integer, VaultStatistics> mediumMap = cacheInventoryManager.get(target).getMedium_map();
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
                    scheduler.scheduling().entitySpecificScheduler(target).run(task -> {
                        for(ItemStack item : item_list){
                            target.getInventory().addItem(item);
                        }
                    },null);
                }
            }
        });
    }
}
