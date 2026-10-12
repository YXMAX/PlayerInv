package com.playerinv.service.translator.impl;

import com.playerinv.PlayerInv;
import com.playerinv.enums.lang.modern.Category;
import com.playerinv.enums.lang.modern.Lang;
import com.playerinv.util.NodeUtil;
import com.playerinv.service.translator.Translator;
import com.playerinv.util.VersionUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;

import static com.playerinv.PlayerInv.*;

public class ModernTranslator implements Translator {

    private final HashMap<Lang, HashMap<String, String>> map = new HashMap<>();

    private final HashMap<Lang, HashMap<String, String>> customMap = new HashMap<>();

    @Override
    public void init() {
        NodeUtil.sendConsole("&f正在注入现代版本翻译API","&fInit modern translator...");
        this.load(Lang.EN_US,Lang.ZH_CN,Lang.ZH_HK,Lang.ZH_TW,Lang.LZH);
        this.initCustomTranslations();
    }

    @Override
    public String translate(ItemStack stack, String locale) {
        String s = translateExact(Category.getNamespacedKey(stack), getLangFromString(locale));
        return s == null ? translateCustom(stack.getType().toString(),locale) : s;
    }

    public String translateExact(String namespacedKey, Lang lang){
        return map.get(lang).get(namespacedKey.toLowerCase());
    }

    public String translateCustom(String material, String locale){
        String result = customMap.get(getLangFromString(locale)).get(material.toLowerCase());
        return result != null ? result : material;
    }

    public void load(Lang... langs) {
        for (Lang lang : langs){
            map.put(lang, loadOne(lang.toString().toLowerCase()));
        }
        NodeUtil.sendConsole("&a成功加载语言文件: zh-CN, zh-TW, zh-HK, en-US, lzh","&aLoaded language files: zh-CN, zh-TW, zh-HK, en-US, lzh");
    }

    private HashMap<String, String> loadOne(String locale) {
        InputStream stream = PlayerInv.getInstance().getResource("lang/" + locale + ".json");
        return PlayerInv.getInstance().getGsonParser().parseToHashMap(stream);
    }

    private Lang getLangFromString(String lang){
        Lang lang0 = Lang.EN_US;
        try{
            lang0 = Lang.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e){
            return lang0;
        }
        return lang0;
    }

    @Override
    public boolean hasLang(String lang){
        try{
            Lang.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
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
        File hkTransFile = new File(getInstance().getDataFolder() + "/translate/", "zh_hk.json");
        if(!hkTransFile.exists()){
            hkTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getInstance().getResource("translate/zh_hk.json"),new File(getInstance().getDataFolder() + "/translate/", "zh_hk.json"));
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
        File lzhTransFile = new File(getInstance().getDataFolder() + "/translate/", "lzh.json");
        if(!lzhTransFile.exists()){
            lzhTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getInstance().getResource("translate/lzh.json"),new File(getInstance().getDataFolder() + "/translate/", "lzh.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            customMap.put(Lang.ZH_CN, PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(cnTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_cn 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put(Lang.EN_US, PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(usTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 en_us 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put(Lang.ZH_HK, PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(hkTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_hk 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put(Lang.ZH_TW, PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(twTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_tw 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            customMap.put(Lang.LZH, PlayerInv.getInstance().getGsonParser().parseToHashMap(FileUtils.readFileToString(lzhTransFile, "utf-8")));
        } catch (Exception e) {
            NodeUtil.sendError("解析 lzh 自定义翻译语言文件时出现错误: " + e.getMessage());
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
