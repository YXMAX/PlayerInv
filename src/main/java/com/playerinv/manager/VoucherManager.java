package com.playerinv.manager;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.enums.PermissionEnums;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.VersionUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.playerinv.manager.PermissionManager.hasPermission;
import static com.playerinv.manager.PermissionManager.hasVaultPermission;
import static com.playerinv.util.LocaleUtil.*;

public class VoucherManager {

    private HashSet<String> voucherCooldown = new HashSet<>();

    private String largeMaterial;

    private int largeCustomModelData;

    private boolean largeGlow;

    private String largeDisplayName;

    private List<String> largeLore;

    private String largeTitle;

    private String largeSubTitle;

    private String mediumMaterial;

    private int mediumCustomModelData;

    private boolean mediumGlow;

    private String mediumDisplayName;

    private List<String> mediumLore;

    private String mediumTitle;

    private String mediumSubTitle;

    private boolean naturalDrop;

    private boolean displayTitle;

    private boolean checkVoucherOwner;

    private boolean voucherEnabled;

    public void reload(){
        voucherEnabled = PlayerInv.getInstance().getConfig().getBoolean("Voucher.Enabled");
        largeMaterial = PlayerInv.getInstance().getConfig().getString("Voucher.large.material","PAPER");
        largeCustomModelData = PlayerInv.getInstance().getConfig().getInt("Voucher.large.custom-model-data",0);
        largeGlow = PlayerInv.getInstance().getConfig().getBoolean("Voucher.large.enchant-glow");
        largeDisplayName = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.large.display-name","&b大型仓库兑换券");
        largeLore = PlayerInv.getInstance().getLocaleConfig().getStringList("Voucher.large.lore");
        largeTitle = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.large.title","&eCongratulation!!");
        largeSubTitle = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.large.subtitle","&a获得 &b%large_vault_number% &a号大型仓库");
        mediumMaterial = PlayerInv.getInstance().getConfig().getString("Voucher.medium.material","PAPER");
        mediumCustomModelData = PlayerInv.getInstance().getConfig().getInt("Voucher.medium.custom-model-data",0);
        mediumGlow = PlayerInv.getInstance().getConfig().getBoolean("Voucher.medium.enchant-glow");
        mediumDisplayName = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.medium.display-name","&a小型仓库兑换券");
        mediumLore = PlayerInv.getInstance().getLocaleConfig().getStringList("Voucher.medium.lore");
        mediumTitle = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.medium.title","&eCongratulation!!");
        mediumSubTitle = PlayerInv.getInstance().getLocaleConfig().getString("Voucher.medium.subtitle","&a获得 &b%medium_vault_number% &a号小型仓库");
        naturalDrop = PlayerInv.getInstance().getConfig().getBoolean("Voucher.Natural_drop");
        displayTitle = PlayerInv.getInstance().getConfig().getBoolean("Voucher.Title");
        checkVoucherOwner = PlayerInv.getInstance().getConfig().getBoolean("Voucher.Set-owner");
    }

    public boolean isVoucherEnabled(){
        return this.voucherEnabled;
    }

    public boolean isSameMaterial(ItemStack itemStack){
        return itemStack.getType().toString().equalsIgnoreCase(largeMaterial) || itemStack.getType().toString().equalsIgnoreCase(mediumMaterial);
    }

    public void sendLargeVoucher(Player player,int amount){
        if(Material.getMaterial(this.largeMaterial) == null){
            this.largeMaterial = "PAPER";
        }
        ItemStack litem = new ItemStack(Material.getMaterial(this.largeMaterial),amount);
        ItemMeta lim = litem.getItemMeta();
        if(this.largeGlow){
            lim.addEnchant(Enchantment.KNOCKBACK, 1, false);
            lim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(this.largeCustomModelData > 0 && VersionUtil.isCustomModelDataSupport()){
            lim.setCustomModelData(this.largeCustomModelData);
        }
        lim.setDisplayName(MessageUtil.parseString(this.largeDisplayName,"%voucher_player%",player.getName()));
        lim.setLore(MessageUtil.parseLore(this.largeLore,"%voucher_player%",player.getName()));
        litem.setItemMeta(lim);
//        litem = NBTEditor.set(litem, player.getName(), NBTEditor.CUSTOM_DATA, "playerinv:large");
        NBT.modify(litem, nbt -> {
            nbt.setString("playerinv:large",player.getName());
        });
        MessageUtil.sendDebug(NBTItem.convertItemtoNBT(litem).toString());
        ItemStack item = litem.clone();
        PlayerInv.getScheduler().entitySpecificScheduler(player).run(scheduledTask -> {
            if(naturalDrop){
                player.getWorld().dropItem(player.getLocation(),item);
                return;
            }
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(item);
            if(!map.isEmpty()){
                player.getWorld().dropItem(player.getLocation(),item);
            }
            return;
        },null);
    }

    public void sendMediumVoucher(Player player,int amount){
        if(Material.getMaterial(this.mediumMaterial) == null){
            this.mediumMaterial = "PAPER";
        }
        ItemStack sitem = new ItemStack(Material.getMaterial(this.mediumMaterial),amount);
        ItemMeta sim = sitem.getItemMeta();
        if(this.mediumGlow){
            sim.addEnchant(Enchantment.KNOCKBACK, 1, false);
            sim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(this.mediumCustomModelData > 0 && VersionUtil.isCustomModelDataSupport()){
            sim.setCustomModelData(this.mediumCustomModelData);
        }
        sim.setDisplayName(MessageUtil.parseString(this.mediumDisplayName,"%voucher_player%",player.getName()));
        sim.setLore(MessageUtil.parseLore(this.mediumLore,"%voucher_player%",player.getName()));
        sitem.setItemMeta(sim);
//        sitem = NBTEditor.set(sitem, player.getName(), NBTEditor.CUSTOM_DATA, "playerinv:medium");
        NBT.modify(sitem, nbt -> {
            nbt.setString("playerinv:medium",player.getName());
        });
        MessageUtil.sendDebug(NBTItem.convertItemtoNBT(sitem).toString());
        ItemStack item = sitem.clone();
        PlayerInv.getScheduler().entitySpecificScheduler(player).run(scheduledTask -> {
            if(naturalDrop){
                player.getWorld().dropItem(player.getLocation(),item);
                return;
            }
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(item);
            if(!map.isEmpty()){
                player.getWorld().dropItem(player.getLocation(),item);
            }
            return;
        },null);
    }

    public void runVoucher(Player player, ItemStack item){
        if(voucherCooldown.contains(player.getName())){
            return;
        }
        voucherCooldown.add(player.getName());
        PlayerInv.getScheduler().asyncScheduler().runDelayed(task -> {
            voucherCooldown.remove(player.getName());
        }, Duration.ofSeconds(1));
        if(player.isOp() || hasPermission(player, PermissionEnums.INV_ALL) || hasPermission(player,PermissionEnums.COMMAND_ADMIN)){
            sendMessages(player, LocaleEnums.Voucher_full);
            return;
        }
        String large = NBT.get(item,nbt -> (String) nbt.getString("playerinv:large"));
        MessageUtil.sendDebug("获取到的兑换券value: " + large);
        if(large != null && !large.trim().isEmpty()){
            if(hasPermission(player,PermissionEnums.LARGE_ALL)){
                sendMessages(player, LocaleEnums.Voucher_full);
                return;
            }
            if(checkVoucherOwner){
                if(large.equals(player.getName())){
                    this.verifyVoucher(player,item,1);
                } else {
                    sendMessages(player,LocaleEnums.Voucher_cannot_use);
                }
            } else {
                this.verifyVoucher(player,item,1);
            }
            return;
        }
        String medium = NBT.get(item,nbt -> (String) nbt.getString("playerinv:medium"));
        MessageUtil.sendDebug("获取到的兑换券value: " + medium);
        if(medium != null && !medium.trim().isEmpty()){
            if(hasPermission(player,PermissionEnums.MEDIUM_ALL)){
                sendMessages(player, LocaleEnums.Voucher_full);
                return;
            }
            if(checkVoucherOwner){
                if(medium.equals(player.getName())){
                    this.verifyVoucher(player,item,2);
                } else {
                    sendMessages(player,LocaleEnums.Voucher_cannot_use);
                }
            } else {
                this.verifyVoucher(player,item,2);
            }
            return;
        }
    }

    private void verifyVoucher(Player player, ItemStack item, int type){
        PlayerInv.getScheduler().entitySpecificScheduler(player).run(task -> {
            this.givePermissionByVoucher(player,type,item);
        },null);
    }

    private void givePermissionByVoucher(Player player, int type,ItemStack item){
        if(PlayerInv.getInstance().getPermissionManager().isLuckpermsServiceEnabled()){
            int result = PlayerInv.getInstance().getPermissionManager().getLuckpermsService().giveLoopPermission(player,type,0);
            if(result == 0){
                sendMessages(player,LocaleEnums.Voucher_full);
                return;
            }
            item.setAmount(item.getAmount() - 1);
            if(type == 1){
                sendMessages(player,LocaleEnums.Voucher_use_large,"%large_vault_number%", String.valueOf(result));
                if (displayTitle) {
                    MessageUtil.sendTitle(player,this.largeTitle,this.largeSubTitle,"%large_vault_number%",String.valueOf(result));
                }
                MessageUtil.sendDetail("玩家 " + player.getName() + " 使用了兑换券并获得了大型仓库: " + result);
            } else {
                sendMessages(player,LocaleEnums.Voucher_use_medium,"%medium_vault_number%", String.valueOf(result));
                if (displayTitle) {
                    MessageUtil.sendTitle(player,this.mediumTitle,this.mediumSubTitle,"%medium_vault_number%",String.valueOf(result));
                }
                MessageUtil.sendDetail("玩家 " + player.getName() + " 使用了兑换券并获得了中型仓库: " + result);
            }
            PlayerInv.getInstance().getSoundManager().voucherUse(player);
            ConfigConstants.runCommandBySendVault(type,player,result);
            return;
        }
        if(type == 1){
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getLargeVaultAmount();i++){
                if(!hasVaultPermission(player,1,i)){
                    item.setAmount(item.getAmount() - 1);
                    PlayerInv.getInstance().getPermissionManager().getVaultPermissionService().playerAdd(null,player,"playerinv.large.inv." + i);
                    sendMessages(player,LocaleEnums.Voucher_use_large,"%large_vault_number%", String.valueOf(i));
                    if (displayTitle) {
                        MessageUtil.sendTitle(player,this.largeTitle,this.largeSubTitle,"%large_vault_number%",String.valueOf(i));
                    }
                    ConfigConstants.runCommandBySendVault(1,player,i);
                    PlayerInv.getInstance().getSoundManager().voucherUse(player);
                    return;
                }
            }
            sendMessages(player,LocaleEnums.Voucher_full);
        } else {
            for(int i=1;i<=PlayerInv.getInstance().getVaultManager().getMediumVaultAmount();i++){
                if(!hasVaultPermission(player,2,i)){
                    item.setAmount(item.getAmount() - 1);
                    PlayerInv.getInstance().getPermissionManager().getVaultPermissionService().playerAdd(null,player,"playerinv.medium.inv." + i);
                    sendMessages(player,LocaleEnums.Voucher_use_medium,"%medium_vault_number%", String.valueOf(i));
                    if (displayTitle) {
                        MessageUtil.sendTitle(player,this.mediumTitle,this.mediumSubTitle,"%medium_vault_number%",String.valueOf(i));
                    }
                    ConfigConstants.runCommandBySendVault(2,player,i);
                    PlayerInv.getInstance().getSoundManager().voucherUse(player);
                    return;
                }
            }
            sendMessages(player,LocaleEnums.Voucher_full);
        }
    }
}
