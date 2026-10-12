package com.playerinv.gui.icon.impl;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.gui.adapter.MenuValueAdapter;
import com.playerinv.manager.MenuManager;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.util.exception.InvalidConfigSectionException;
import com.playerinv.gui.icon.MenuIcon;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.playerinv.util.LocaleUtil.getShareVaultPermissionDisplay;

public class VaultIcon implements MenuIcon, Cloneable {

    private String material;

    private final Material itemMaterial;

    private final int amount;

    private boolean enchantGlow;

    private String displayName;

    private List<String> lore;

    private Short itemSubId;

    private String headTexture;

    private Integer customModelData;

    private int vaultType;

    private int vaultNum;

    private String vaultUUID;

    private int vaultPermission;

    // 仓库和确认按钮
    public VaultIcon(ConfigurationSection current,String fileName) {
        this.material = current.getString("material");
        this.itemMaterial = MenuValueAdapter.parseMaterial(fileName,current.getCurrentPath(),this.material);
        this.amount = MenuValueAdapter.parseItemAmount(current.getInt("amount",1),fileName,current.getCurrentPath());
        this.enchantGlow = current.getBoolean("enchant-glow");
        if(current.contains("item-subid")){
            this.itemSubId = (short) current.getInt("item-subid");
        } else {
            this.itemSubId = MenuValueAdapter.parseLegacySubId(this.material);
        }
        this.displayName = current.getString("display-name");
        this.lore = current.getStringList("lore");
        this.headTexture = current.getString("head-texture");
        if(VersionUtil.isCustomModelDataSupport()){
            this.customModelData = current.getInt("custom-model-data",0);
        }
    }

    // 自动拾取的材质按钮
    public VaultIcon(ItemStack itemStack){
        this.material = itemStack.getType().toString();
        this.itemMaterial = itemStack.getType();
        this.enchantGlow = false;
        this.headTexture = null;
        this.amount = 1;
        if(itemStack.getDurability() >= 0){
            this.itemSubId = itemStack.getDurability();
        }
        if(VersionUtil.isCustomModelDataSupport() && itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData()){
            this.customModelData = itemStack.getItemMeta().getCustomModelData();
        }
    }

    // 头颅按钮
    public VaultIcon(ConfigurationSection current,String fileName,boolean skull) {
        this.material = "PLAYER_HEAD";
        this.itemMaterial = MenuValueAdapter.parseMaterial(fileName,current.getCurrentPath(),this.material);
        this.amount = 1;
        this.itemSubId = MenuValueAdapter.parseLegacySubId(this.material);
        this.displayName = current.getString("display-name");
        this.lore = current.getStringList("lore");
    }

    public static VaultIcon build(ConfigurationSection section, String topKey, String fileName) throws Exception{
        ConfigurationSection current = topKey == null ? section : section.getConfigurationSection(topKey);
        if (current == null) throw new InvalidConfigSectionException(topKey);
        return new VaultIcon(current,fileName);
    }

    public static VaultIcon buildSkull(ConfigurationSection section, String topKey, String fileName) throws Exception{
        ConfigurationSection current = topKey == null ? section : section.getConfigurationSection(topKey);
        if (current == null) throw new InvalidConfigSectionException(topKey);
        return new VaultIcon(current,fileName,true);
    }

    @Override
    public int getRawSlot() {
        return -1;
    }

    @Override
    public ItemStack generateIcon(Player player) {
        return this.buildItemStackWithMeta(
                MessageUtil.parseString(this.displayName, player),
                MessageUtil.parseLore(this.lore,player)
        );
    }

    @Override
    public ItemStack generateIcon() {
        return this.buildItemStackWithMeta(null,null);
    }

    @Override
    public void runActions(Player player, String clickType) {
        return;
    }

    @Override
    public boolean isCustomIcon() {
        return false;
    }

    @Override
    public int getVaultType() {
        return vaultType;
    }

    @Override
    public int getVaultNum() {
        return vaultNum;
    }

    @Override
    public String getVaultUUID() {
        return vaultUUID;
    }

    @Override
    public int getVaultPermission() {
        return vaultPermission;
    }

    @Override
    public String getCustomKey() {
        return null;
    }

    @Override
    public boolean hasCustomKey(){
        return false;
    }

    @Override
    public String getCustomKeyValue() {
        return null;
    }

    public String getMaterial(){
        return this.itemMaterial.toString();
    }

    public void setMemberName(String name){
        this.vaultUUID = name;
    }

    public void setMemberPermission(int permission){
        this.vaultPermission = permission;
    }

    public ItemStack generatePersonalVaultIcon(Player player, int type, int num){
        this.vaultType = type;
        this.vaultNum = num;
        return this.buildItemStackWithMeta(
                MessageUtil.parseString(PlayerInv.getInstance().getVaultAttributesManager().replaceVaultMenuName(player,this.displayName,type,num),player),
                MessageUtil.parseLore(
                        this.lore,
                        player,
                        "%remain_slot%",
                        String.valueOf(PlayerInv.getInstance().getCacheInventoryManager().getRemainSlot(player,type,num)),
                        "%vault_expiry%",
                        PlayerInv.getInstance().getPlayerCacheManager().getPlaceholderDisplay(player,type,num)
                )
        );
    }

    private ItemStack buildItemStackWithMeta(String displayName,List<String> lore){
        ItemStack itemStack = new ItemStack(this.itemMaterial,this.amount);
        if(this.itemSubId != null){
            itemStack.setDurability(this.itemSubId);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(this.displayName != null){
            itemMeta.setDisplayName(displayName);
        }
        if(this.lore != null && !this.lore.isEmpty()){
            itemMeta.setLore(lore);
        }
        if(this.customModelData != null){
            itemMeta.setCustomModelData(this.customModelData);
        }
        if(enchantGlow){
            itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        if(this.isSkullItem() && this.headTexture != null){
            itemStack = MenuManager.setCustomSkull(itemStack,this.headTexture);
        }
        return itemStack;
    }

    private ItemStack setPlayerSkull(ItemStack item, String name){
        String value = PlayerInv.getInstance().getGsonParser().getHeadValue(name);
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}");
    }

    private boolean isSkullItem(){
        return (this.material.equalsIgnoreCase("skull_item") && this.itemSubId == 3) || this.material.equalsIgnoreCase("player_head");
    }

    @Override
    public VaultIcon clone(){
        try {
            return (VaultIcon) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
