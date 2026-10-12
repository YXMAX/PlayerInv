package com.playerinv.gui.icon.impl;

import com.playerinv.gui.action.IconActionCommand;
import com.playerinv.gui.adapter.MenuValueAdapter;
import com.playerinv.manager.MenuManager;
import com.playerinv.util.exception.InvalidConfigSectionException;
import com.playerinv.util.exception.InvalidMenuSlotException;
import com.playerinv.gui.icon.MenuIcon;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.VersionUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ItemIcon implements MenuIcon {

    private final ConfigurationSection current;

    private final String topKey;

    private final int slot;

    private final String material;

    private final Material itemMaterial;

    private final int amount;

    private final boolean enchantGlow;

    private final String displayName;

    private final List<String> lore;

    private final Short itemSubId;

    private final String headTexture;

    private Integer customModelData;

    private String customKey;

    private String customKeyValue;

    private List<IconActionCommand> leftClickCommands;

    private List<IconActionCommand> rightClickCommands;

    private List<IconActionCommand> shiftLeftClickCommands;

    private List<IconActionCommand> shiftRightClickCommands;

    public ItemIcon(ConfigurationSection section, String topKey, int menu_size, String file_name, boolean barSlot) throws InvalidMenuSlotException{
        this.topKey = topKey;
        this.current = section;
        this.slot = MenuValueAdapter.parseSlot(this.current.getInt("slot"),menu_size,file_name,this.getPath());
        this.material = this.current.getString("material");
        this.itemMaterial = MenuValueAdapter.parseMaterial(file_name,this.getPath(),this.material);
        this.amount = MenuValueAdapter.parseItemAmount(this.current.getInt("amount",1),file_name,this.getPath());
        this.enchantGlow = this.current.getBoolean("enchant-glow");
        if(this.current.contains("item-subid")){
            this.itemSubId = (short) this.current.getInt("item-subid");
        } else {
            this.itemSubId = MenuValueAdapter.parseLegacySubId(this.material);
        }
        this.displayName = this.current.getString("display-name");
        this.lore = this.current.getStringList("lore");
        this.headTexture = this.current.getString("head-texture");
        if(VersionUtil.isCustomModelDataSupport()){
            this.customModelData = this.current.getInt("custom-model-data",0);
        }
        if(this.current.contains("command")){
            this.leftClickCommands = new ArrayList<>();
            if(this.current.isList("command")){
                for(String s : this.current.getStringList("command")){
                    this.leftClickCommands.add(new IconActionCommand(s));
                }
            } else {
                this.leftClickCommands.add(new IconActionCommand(this.current.getString("command")));
            }
        }
        if(this.current.contains("left-click-commands")){
            this.leftClickCommands = new ArrayList<>();
            if(this.current.isList("left-click-commands")){
                for(String s : this.current.getStringList("left-click-commands")){
                    this.leftClickCommands.add(new IconActionCommand(s));
                }
            } else {
                this.leftClickCommands.add(new IconActionCommand(this.current.getString("left-click-commands")));
            }
        }
        if(this.current.contains("right-click-commands")){
            this.rightClickCommands = new ArrayList<>();
            if(this.current.isList("right-click-commands")){
                for(String s : this.current.getStringList("right-click-commands")){
                    this.rightClickCommands.add(new IconActionCommand(s));
                }
            } else {
                this.rightClickCommands.add(new IconActionCommand(this.current.getString("right-click-commands")));
            }
        }
        if(this.current.contains("shift-left-click-commands")){
            this.shiftLeftClickCommands = new ArrayList<>();
            if(this.current.isList("shift-left-click-commands")){
                for(String s : this.current.getStringList("shift-left-click-commands")){
                    this.shiftLeftClickCommands.add(new IconActionCommand(s));
                }
            } else {
                this.shiftLeftClickCommands.add(new IconActionCommand(this.current.getString("shift-left-click-commands")));
            }
        }
        if(this.current.contains("shift-right-click-commands")){
            this.shiftRightClickCommands = new ArrayList<>();
            if(this.current.isList("shift-right-click-commands")){
                for(String s : this.current.getStringList("shift-right-click-commands")){
                    this.shiftRightClickCommands.add(new IconActionCommand(s));
                }
            } else {
                this.shiftRightClickCommands.add(new IconActionCommand(this.current.getString("shift-right-click-commands")));
            }
        }
    }

    @Override
    public int getRawSlot() {
        return slot - 1;
    }

    public void importCustomKey(HashSet<String> set){
        if(set == null) return;
        for(String s : set){
            if(this.current.contains(s)){
                String string = this.current.getString(s);
                if(string != null && string.equalsIgnoreCase("false")){
                    continue;
                }
                this.customKey = s;
                this.customKeyValue = string;
                break;
            }
        }
    }

    @Override
    public ItemStack generateIcon(Player player){
        ItemStack itemStack = new ItemStack(this.itemMaterial,this.amount);
        if(this.itemSubId != null){
            itemStack.setDurability(this.itemSubId);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(this.displayName != null){
            itemMeta.setDisplayName(MessageUtil.parseString(this.displayName,player));
        }
        if(this.lore != null && !this.lore.isEmpty()){
            itemMeta.setLore(MessageUtil.parseLore(this.lore,player));
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

    @Override
    public ItemStack generateIcon(){
        ItemStack itemStack = new ItemStack(this.itemMaterial,this.amount);
        if(this.itemSubId != null){
            itemStack.setDurability(this.itemSubId);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(this.displayName != null){
            itemMeta.setDisplayName(MessageUtil.parseString(this.displayName));
        }
        if(this.lore != null && !this.lore.isEmpty()){
            itemMeta.setLore(MessageUtil.parseLore(this.lore));
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

    @Override
    public void runActions(Player player, String clickType) {
        switch(clickType){
            case "LEFT":
                if(leftClickCommands == null) return;
                for(IconActionCommand command : leftClickCommands){
                    command.runCommandToPlayer(player);
                }
                return;
            case "RIGHT":
                if(rightClickCommands == null) return;
                for(IconActionCommand command : rightClickCommands){
                    command.runCommandToPlayer(player);
                }
                return;
            case "SHIFT_LEFT":
                if(shiftLeftClickCommands == null) return;
                for(IconActionCommand command : shiftLeftClickCommands){
                    command.runCommandToPlayer(player);
                }
                return;
            case "SHIFT_RIGHT":
                if(shiftRightClickCommands == null) return;
                for(IconActionCommand command : shiftRightClickCommands){
                    command.runCommandToPlayer(player);
                }
                return;
        }
    }

    @Override
    public boolean isCustomIcon() {
        return true;
    }

    @Override
    public int getVaultType() {
        return -99;
    }

    @Override
    public int getVaultNum() {
        return -99;
    }

    @Override
    public String getVaultUUID() {
        return null;
    }

    @Override
    public int getVaultPermission() {
        return -99;
    }

    @Override
    public String getCustomKey() {
        return customKey;
    }

    @Override
    public boolean hasCustomKey(){
        return customKey != null;
    }

    @Override
    public String getCustomKeyValue() {
        return customKeyValue;
    }

    private String getPath(){
        return this.current.getCurrentPath();
    }

    private boolean isSkullItem(){
        return (this.material.equalsIgnoreCase("skull_item") && this.itemSubId == 3) || this.material.equalsIgnoreCase("player_head");
    }

    public static ItemIcon build(ConfigurationSection section,String topKey,int menu_size,String file_name,boolean barSlot) throws Exception{
        ConfigurationSection current = topKey == null ? section : section.getConfigurationSection(topKey);
        if (current == null) throw new InvalidConfigSectionException(topKey);
        return new ItemIcon(current,topKey,menu_size,file_name,barSlot);
    }
}
