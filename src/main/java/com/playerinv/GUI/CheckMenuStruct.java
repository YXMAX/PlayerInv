package com.playerinv.GUI;

import com.playerinv.Util.InvHolder.CheckMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.isBelow113;
import static com.playerinv.Util.InitUtil.isBelow17;
import static com.playerinv.Util.NodeUtil.color;

public class CheckMenuStruct {

    public Inventory basicInventory;

    private ItemStack vaultLarge_exist;

    private ItemStack vaultLarge_not_exist;

    private ItemStack vaultMedium_exist;

    private ItemStack vaultMedium_not_exist;

    private int max_page;

    public void structCheckMenu(){
        Inventory inventory = Bukkit.createInventory(null,54,"Inventory");
        ItemStack previous_Button;
        ItemStack next_Button;
        if(isBelow113){
            previous_Button = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 14);
            next_Button = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 5);
        } else {
            previous_Button = new ItemStack(Material.RED_STAINED_GLASS_PANE,1);
            next_Button = new ItemStack(Material.LIME_STAINED_GLASS_PANE,1);
        }
        ItemMeta meta1 = previous_Button.getItemMeta();
        ItemMeta meta2 = next_Button.getItemMeta();
        meta1.setDisplayName(color("&c上一页"));
        meta2.setDisplayName(color("&a下一页"));
        previous_Button.setItemMeta(meta1);
        next_Button.setItemMeta(meta2);

        if(isBelow17){
            vaultLarge_exist = new ItemStack(Material.CHEST,1);
            vaultLarge_not_exist = new ItemStack(Material.ENDER_CHEST,1);
        } else {
            vaultLarge_exist = new ItemStack(Material.WHITE_SHULKER_BOX,1);
            vaultLarge_not_exist = new ItemStack(Material.BLACK_SHULKER_BOX,1);
        }
        vaultMedium_exist = new ItemStack(Material.CHEST,1);
        vaultMedium_not_exist = new ItemStack(Material.ENDER_CHEST,1);

        ItemMeta meta3 = vaultLarge_exist.getItemMeta();
        List<String> list = new ArrayList<String>();
        list.add(" ");
        list.add(color("&f- &a查看该仓库物品内容"));
        meta3.setLore(list);
        vaultLarge_exist.setItemMeta(meta3);

        ItemMeta meta4 = vaultLarge_not_exist.getItemMeta();
        List<String> list2 = new ArrayList<String>();
        list2.add(" ");
        list2.add(color("&f- &c该仓库暂未创建 无法查看"));
        meta4.setLore(list2);
        vaultLarge_not_exist.setItemMeta(meta4);

        ItemMeta meta5 = vaultMedium_exist.getItemMeta();
        List<String> list3 = new ArrayList<String>();
        list3.add(" ");
        list3.add(color("&f- &a查看该仓库物品内容"));
        meta5.setLore(list3);
        vaultMedium_exist.setItemMeta(meta5);

        ItemMeta meta6 = vaultMedium_not_exist.getItemMeta();
        List<String> list4 = new ArrayList<String>();
        list4.add(" ");
        list4.add(color("&f- &c该仓库暂未创建 无法查看"));
        meta6.setLore(list4);
        vaultMedium_not_exist.setItemMeta(meta6);

        inventory.setItem(45,previous_Button);
        inventory.setItem(46,previous_Button);
        inventory.setItem(52,next_Button);
        inventory.setItem(53,next_Button);

        basicInventory = inventory;
        int large = largeVaultAmount / 27;
        if(largeVaultAmount % 27 != 0){
            large = large + 1;
        }
        int medium = mediumVaultAmount / 18;
        if(mediumVaultAmount % 18 != 0){
            medium = medium + 1;
        }
        max_page = Math.max(medium,large);
    }

    public int getMax_page() {
        return max_page;
    }

    public Inventory buildCheckMenu(OfflinePlayer target){
        CheckMenuHolder checkMenuHolder = new CheckMenuHolder(target);
        Inventory inventory = Bukkit.createInventory(checkMenuHolder,54,"玩家 " + target.getName() + " 的仓库列表");
        inventory.setContents(basicInventory.getContents());
        checkMenuHolder.setInventory(inventory);
        return inventory;
    }

    public ItemStack setVaultLarge_Exist(int num){
        ItemStack clone = vaultLarge_exist.clone();
        String display = "&d大型仓库 &f[&a" + num + "&f]";
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(color(display));
        clone.setItemMeta(meta);
        return clone;
    }

    public ItemStack setVaultLarge_NotExist(int num){
        ItemStack clone = vaultLarge_not_exist.clone();
        String display = "&d大型仓库 &f[&a" + num + "&f]";
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(color(display));
        clone.setItemMeta(meta);
        return clone;
    }

    public ItemStack setVaultMedium_Exist(int num){
        ItemStack clone = vaultMedium_exist.clone();
        String display = "&d中型仓库 &f[&a" + num + "&f]";
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(color(display));
        clone.setItemMeta(meta);
        return clone;
    }

    public ItemStack setVaultMedium_NotExist(int num){
        ItemStack clone = vaultMedium_not_exist.clone();
        String display = "&d中型仓库 &f[&a" + num + "&f]";
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(color(display));
        clone.setItemMeta(meta);
        return clone;
    }


}
