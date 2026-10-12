package com.playerinv.service.translator.impl;

import com.playerinv.PlayerInv;
import com.playerinv.enums.lang.legacy.*;
import com.playerinv.util.NodeUtil;
import com.playerinv.service.translator.Translator;
import com.playerinv.util.VersionUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.playerinv.PlayerInv.*;

public class LegacyTranslator implements Translator {

    private static final HashMap<String, HashMap<String, String>> customMap = new HashMap<>();

    @Override
    public void init() {
        NodeUtil.sendConsole("&f正在注入传统版本翻译API","&fInit legacy translator...");
        EnumLang.init();
        this.initCustomTranslations();
    }

    @Override
    public String translate(ItemStack stack, String locale) {
        switch (stack.getType().toString()) {
            case "POTION":
            case "SPLASH_POTION":
            case "LINGERING_POTION":
            case "TIPPED_ARROW":
                return EnumPotionEffect.getLocalizedName(stack, locale);
            case "MONSTER_EGG":
                return EnumEntity.getSpawnEggName(stack, locale);
            case "SKULL_ITEM":
                if (stack.getDurability() == 3) {
                    return EnumItem.getPlayerSkullName(stack, locale);
                }
            default: return translateToLocal(getItemUnlocalizedName(stack), locale);
        }
    }

    public static String getItemUnlocalizedName(ItemStack item) {
        EnumItem enumItem = EnumItem.get(ItemEntry.from(item));
        return enumItem != null ? enumItem.getUnlocalizedName() : item.getType().name();
    }

    public static String getItemCustomName(String material,String locale) {
        String result = customMap.get(locale).get(material.toLowerCase());
        return result == null ? material : result;
    }

    public static String translateToLocal(String unlocalizedName, String locale) {
        String result = EnumLang.get(locale.toLowerCase()).getMap().get(unlocalizedName);
        if (result != null)
            return result;
        else {
            result = EnumLang.EN_US.getMap().get(unlocalizedName);
        }
        return result == null ? getItemCustomName(unlocalizedName,locale) : result;
    }

    @Override
    public boolean hasLang(String locale){
        return EnumLang.hasLang(locale);
    }

    public void initCustomTranslations() {
        NodeUtil.sendConsole("&f正在注入自定义语言翻译文件内容","&fInit custom translations...");
        File cnTransFile = new File(getInstance().getDataFolder() + "/translate/", "zh_cn.json");
        if(!cnTransFile.exists()){
            cnTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getInstance().getResource("translate/zh_cn.json"),new File(getInstance().getDataFolder() + "/translate/", "zh_cn.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File usTransFile = new File(getInstance().getDataFolder() + "/translate/", "en_us.json");
        if(!usTransFile.exists()){
            usTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getInstance().getResource("translate/en_us.json"),new File(getInstance().getDataFolder() + "/translate/", "en_us.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File twTransFile = new File(getInstance().getDataFolder() + "/translate/", "zh_tw.json");
        if(!twTransFile.exists()){
            twTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getInstance().getResource("translate/zh_tw.json"),new File(getInstance().getDataFolder() + "/translate/", "zh_tw.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            customMap.put("zh_cn", PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(cnTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_cn 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put("en_us", PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(usTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 en_us 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put("zh_tw", PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(twTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_tw 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        NodeUtil.sendConsole("&a注入自定义语言翻译文件内容完成","&fCustom translations imported...");
    }

    @Override
    public void reloadCustomTranslations() {
        this.initCustomTranslations();
    }

    @Override
    public String itemTranslate(ItemStack itemStack, Player player) {
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }
        if(VersionUtil.isUpToVer1_20_5()){
            StringBuilder s = new StringBuilder();
            NBT.getComponents(itemStack, nbt -> {
                if(nbt.hasTag("minecraft:item_name")){
                    ReadableNBT compound = nbt.getCompound("minecraft:item_name");
                    if(compound.hasTag("translate")){
                        String translate = compound.getString("translate");
                        s.append(translate);
                    } else if(compound.hasTag("text")){
                        String text = compound.getString("text");
                        s.append(text);
                    }
                }
            });
            if(!s.toString().isEmpty()){
                return s.toString();
            }
        }
        String name = this.translate(itemStack,player.getLocale());
        if(name == null){
            return itemStack.getType().toString();
        }
        return name;
    }
}
