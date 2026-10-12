package com.playerinv.gui;

import com.playerinv.PlayerInv;
import com.playerinv.gui.holder.impl.menu.CheckMenuHolder;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckMenuStruct {

    private static final CheckMenuStruct instance = new CheckMenuStruct();

    private Inventory basicInventory;

    private ItemStack vaultLargeIcon;

    private ItemStack vaultMediumIcon;

    private int maxPage;

    public static CheckMenuStruct getInstance(){
        return instance;
    }

    public void loadCheckMenu(){
        Inventory inventory = Bukkit.createInventory(null,54,"Inventory");
        ItemStack previous_Button;
        ItemStack next_Button;
        if(VersionUtil.isBelowVer1_13()){
            previous_Button = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 14);
            next_Button = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 5);
        } else {
            previous_Button = new ItemStack(Material.RED_STAINED_GLASS_PANE,1);
            next_Button = new ItemStack(Material.LIME_STAINED_GLASS_PANE,1);
        }
        ItemMeta meta1 = previous_Button.getItemMeta();
        ItemMeta meta2 = next_Button.getItemMeta();
        meta1.setDisplayName(MessageUtil.parseString("&c上一页"));
        meta2.setDisplayName(MessageUtil.parseString("&a下一页"));
        previous_Button.setItemMeta(meta1);
        next_Button.setItemMeta(meta2);

        if(VersionUtil.isVer1_7()){
            vaultLargeIcon = new ItemStack(Material.CHEST,1);
        } else {
            vaultLargeIcon = new ItemStack(Material.WHITE_SHULKER_BOX,1);
        }
        vaultMediumIcon = new ItemStack(Material.CHEST,1);

        inventory.setItem(45,previous_Button);
        inventory.setItem(46,previous_Button);
        inventory.setItem(52,next_Button);
        inventory.setItem(53,next_Button);

        basicInventory = inventory;
        int large = PlayerInv.getInstance().getVaultManager().getLargeVaultAmount() / 27;
        if(PlayerInv.getInstance().getVaultManager().getLargeVaultAmount() % 27 != 0){
            large = large + 1;
        }
        int medium = PlayerInv.getInstance().getVaultManager().getMediumVaultAmount() / 18;
        if(PlayerInv.getInstance().getVaultManager().getMediumVaultAmount() % 18 != 0){
            medium = medium + 1;
        }
        maxPage = Math.max(medium,large);
    }

    public int getMaxPage() {
        return maxPage;
    }

    public Inventory getBasicInventory() {
        return basicInventory;
    }

    public void openCheckMenuToPlayer(Player watch, OfflinePlayer target){
        CheckMenuHolder checkMenuHolder = new CheckMenuHolder(target);
        Inventory inventory = Bukkit.createInventory(checkMenuHolder,54,"玩家 " + target.getName() + " 的仓库列表");
        inventory.setContents(basicInventory.getContents());
        checkMenuHolder.setInventory(inventory);
        PlayerInv.getScheduler().entitySpecificScheduler(watch).runDelayed(task -> {
            watch.openInventory(inventory);
        },null,1);
    }

    public ItemStack getVaultLargeIcon(int num){
        ItemStack clone = vaultLargeIcon.clone();
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(MessageUtil.parseString("&d大型仓库 &f[&a" + num + "&f]"));
        clone.setItemMeta(meta);
        return clone;
    }

    public ItemStack getVaultMediumIcon(int num){
        ItemStack clone = vaultMediumIcon.clone();
        ItemMeta meta = clone.getItemMeta();
        meta.setDisplayName(MessageUtil.parseString("&6中型仓库 &f[&a" + num + "&f]"));
        clone.setItemMeta(meta);
        return clone;
    }
}
