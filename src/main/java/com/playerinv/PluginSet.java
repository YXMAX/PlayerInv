package com.playerinv;

import com.playerinv.Listener.CheckInvListener;
import com.playerinv.Listener.CheckInvOfflineListener;
import com.playerinv.Listener.InvListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.playerinv.Command.InvCommand.checktarget;
import static com.playerinv.Command.InvCommand.offlinechecktarget;
import static com.playerinv.Listener.InvListener.optarget;
import static com.playerinv.PlayerInv.plugin;

public class PluginSet extends JavaPlugin {
    protected void initListeners() {
        getServer().getPluginManager().registerEvents((Listener)new InvListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new CheckInvListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new CheckInvOfflineListener(), (Plugin)this);
    }

    public static String inventoryToBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
            //Converts the inventory and its contents to base64, This also saves item meta-data and inventory type
        } catch (Exception e) {
            throw new IllegalStateException("Could not convert inventory to base64.", e);
        }
    }

    public static Integer vaultnum;

    public static Inventory inventoryFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(optarget, dataInput.readInt(), "仓库#" + vaultnum);
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static String Itemtarget;

    public static Integer checkvaultnum;

    public static Inventory checkinventoryFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(checktarget, dataInput.readInt(), checktarget.getName() + " 的仓库[ " + checkvaultnum + " ]");
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static Inventory checkofflineinventoryFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(offlinechecktarget.getPlayer(), dataInput.readInt(), offlinechecktarget.getName() + " 的仓库数据[ " + checkvaultnum + " ]");
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static String getValueFromLore(ItemStack item, String value) {

        String returnVal = "null";
        ItemMeta meta = item.getItemMeta();
        try {
            List<String> lore = meta.getLore();
            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains(value)) {
                        String vals = lore.get(i).split(":")[1];
                        vals = ChatColor.stripColor(vals);
                        returnVal = vals.trim();
                    }
                }
            }
        } catch (Exception e) {
            return returnVal;
        }
        return returnVal;
    }
}
