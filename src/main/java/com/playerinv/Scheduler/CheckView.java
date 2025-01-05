package com.playerinv.Scheduler;

import com.playerinv.InvHolder.VaultHolder_Large;
import com.playerinv.InvHolder.VaultHolder_Medium;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.inventoryToBase64;
import static com.playerinv.PluginSet.sendLog;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class CheckView {

    public static void checkPlayerViewLarge(int id, Player player){
        if(isFolia){
            sendLog(player.getName() + " run checkView large:" + id + " by folia");
            TempPlayerInInventory_Large.put(player,false);
            if(!TempInventory_Large.containsKey(player.getUniqueId().toString() + ":" + id)){
                TempInventory_Large.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
            }
            FoliaLib.scheduling().entitySpecificScheduler(player).runAtFixedRate(scheduledTask -> {
                sendLog(player.getName() + " run cycle large: " + id + "by folia");
                if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large)){
                    sendLog(player.getName() + ":L" + id + " out the vault, try to save (folia)");
                    if(!TempPlayerInInventory_Large.get(player)){
                        sendLog(player.getName() + ":L" + id + " exit unusual, save in folia");
                        Inventory last_inventory = TempInventory_Large.get(player.getUniqueId().toString() + ":" + id);
                        try {
                            SQLiteConnect.updateInv_Large(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        TempPlayerInInventory_Large.put(player,true);
                        scheduledTask.cancel();
                    } else {
                        sendLog(player.getName() + ":L" + id + " exit by close inventory, not save (folia)");
                        scheduledTask.cancel();
                    }
                }
            },null,2,4);
        } else {
            if(isBelow113){
                sendLog(player.getName() + " run checkView large:" + id + " by 1.12");
                TempPlayerInInventory_Large.put(player,false);
                if(!TempInventory_Large.containsKey(player.getUniqueId().toString() + ":" + id)){
                    TempInventory_Large.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
                }
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendLog(player.getName() + " run cycle large:" + id + " by 1.12");
                        if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large)){
                            sendLog(player.getName() + ":L" + id + " out the vault, try to save (1.12)");
                            if(!TempPlayerInInventory_Large.get(player)){
                                sendLog(player.getName() + ":L" + id + " exit unusual, save in 1.12");
                                Inventory last_inventory = TempInventory_Large.get(player.getUniqueId().toString() + ":" + id);
                                try {
                                    SQLiteConnect.updateInv_Large(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                TempInventory_Large.remove(player.getUniqueId().toString() + ":" + id);
                                TempPlayerInInventory_Large.put(player,true);
                                this.cancel();
                            } else {
                                sendLog(player.getName() + ":L" + id + " exit by close inventory, not save (1.12)");
                                this.cancel();
                            }
                        }
                    }
                };
                task.runTaskTimerAsynchronously(plugin,2,4);
            } else {
                sendLog(player.getName() + " run checkView large:" + id + " by 1.13+");
                TempPlayerInInventory_Large.put(player,false);
                if(!TempInventory_Large.containsKey(player.getUniqueId().toString() + ":" + id)){
                    TempInventory_Large.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
                }
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,bukkitTask -> {
                    sendLog(player.getName() + " run cycle large:" + id + " by 1.13+");
                    if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Large)){
                        sendLog(player.getName() + ":L" + id + " out the vault, try to save (1.13+)");
                        if(!TempPlayerInInventory_Large.get(player)){
                            sendLog(player.getName() + ":L" + id + " exit unusual, save in 1.13+");
                            Inventory last_inventory = TempInventory_Large.get(player.getUniqueId().toString() + ":" + id);
                            try {
                                SQLiteConnect.updateInv_Large(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            TempInventory_Large.remove(player.getUniqueId().toString() + ":" + id);
                            TempPlayerInInventory_Large.put(player,true);
                            bukkitTask.cancel();
                        } else {
                            sendLog(player.getName() + ":L" + id + " exit by close inventory, not save (1.13+)");
                            bukkitTask.cancel();
                        }
                    }
                },2,4);
            }
        }
    }

    public static void checkPlayerViewMedium(int id, Player player){
        if(isFolia){
            sendLog(player.getName() + " run checkView medium:" + id + " by folia");
            TempPlayerInInventory_Medium.put(player,false);
            if(!TempInventory_Medium.containsKey(player.getUniqueId().toString() + ":" + id)){
                TempInventory_Medium.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
            }
            FoliaLib.scheduling().entitySpecificScheduler(player).runAtFixedRate(scheduledTask -> {
                sendLog(player.getName() + " run cycle medium: " + id + "by folia");
                if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Medium)){
                    sendLog(player.getName() + ":M" + id + " out the vault, try to save (folia)");
                    if(!TempPlayerInInventory_Medium.get(player)){
                        sendLog(player.getName() + ":M" + id + " exit unusual, save in folia");
                        Inventory last_inventory = TempInventory_Medium.get(player.getUniqueId().toString() + ":" + id);
                        try {
                            SQLiteConnect.updateInv_Medium(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        TempInventory_Medium.remove(player.getUniqueId().toString() + ":" + id);
                        TempPlayerInInventory_Medium.put(player,true);
                        scheduledTask.cancel();
                    } else {
                        sendLog(player.getName() + ":M" + id + " exit by close inventory, not save (folia)");
                        scheduledTask.cancel();
                    }
                }
            },null,2,4);
        } else {
            if(isBelow113){
                sendLog(player.getName() + " run checkView medium:" + id + " by 1.12");
                TempPlayerInInventory_Medium.put(player,false);
                if(!TempInventory_Medium.containsKey(player.getUniqueId().toString() + ":" + id)){
                    TempInventory_Medium.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
                }
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendLog(player.getName() + " run cycle medium:" + id + " by 1.12");
                        if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Medium)){
                            sendLog(player.getName() + ":M" + id + " out the vault, try to save (1.12)");
                            if(!TempPlayerInInventory_Medium.get(player)){
                                sendLog(player.getName() + ":M" + id + " exit unusual, save in 1.12");
                                Inventory last_inventory = TempInventory_Medium.get(player.getUniqueId().toString() + ":" + id);
                                try {
                                    SQLiteConnect.updateInv_Medium(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                TempInventory_Medium.remove(player.getUniqueId().toString() + ":" + id);
                                TempPlayerInInventory_Medium.put(player,true);
                                this.cancel();
                            } else {
                                sendLog(player.getName() + ":M" + id + " exit by close inventory, not save (1.12)");
                                this.cancel();
                            }
                        }
                    }
                };
                task.runTaskTimerAsynchronously(plugin,2,4);
            } else {
                sendLog(player.getName() + " run checkView medium:" + id + " by 1.13+");
                TempPlayerInInventory_Medium.put(player,false);
                if(!TempInventory_Medium.containsKey(player.getUniqueId().toString() + ":" + id)){
                    TempInventory_Medium.put(player.getUniqueId().toString() + ":" + id,player.getOpenInventory().getTopInventory());
                }
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,bukkitTask -> {
                    sendLog(player.getName() + " run cycle medium:" + id + " by 1.13+");
                    if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof VaultHolder_Medium)){
                        sendLog(player.getName() + ":M" + id + " out the vault, try to save (1.13+)");
                        if(!TempPlayerInInventory_Medium.get(player)){
                            sendLog(player.getName() + ":M" + id + " exit unusual, save in 1.13+");
                            Inventory last_inventory = TempInventory_Medium.get(player.getUniqueId().toString() + ":" + id);
                            try {
                                SQLiteConnect.updateInv_Medium(con,player.getUniqueId().toString(),inventoryToBase64(last_inventory),id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            TempPlayerInInventory_Medium.put(player,true);
                            bukkitTask.cancel();
                        } else {
                            sendLog(player.getName() + ":M" + id + " exit by close inventory, not save (1.13+)");
                            bukkitTask.cancel();
                        }
                    }
                },2,4);
            }
        }
    }
}
