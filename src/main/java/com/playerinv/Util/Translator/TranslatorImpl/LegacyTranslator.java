package com.playerinv.Util.Translator.TranslatorImpl;

import com.google.gson.Gson;
import com.playerinv.Enums.Lang.Lang;
import com.playerinv.Enums.Lang.Legacy.*;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Translator.Translator;
import org.apache.commons.io.FileUtils;
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
        NodeUtil.sendConsole("&f正在注入自定义语言翻译文件内容..","&fInit custom translations...");
        cnTransFile = new File(plugin.getDataFolder() + "/translate/", "zh_cn.json");
        if(!cnTransFile.exists()){
            cnTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("translate/zh_cn.json"),new File(plugin.getDataFolder() + "/translate/", "zh_cn.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        usTransFile = new File(plugin.getDataFolder() + "/translate/", "en_us.json");
        if(!usTransFile.exists()){
            usTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("translate/en_us.json"),new File(plugin.getDataFolder() + "/translate/", "en_us.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        twTransFile = new File(plugin.getDataFolder() + "/translate/", "zh_tw.json");
        if(!twTransFile.exists()){
            twTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("translate/zh_tw.json"),new File(plugin.getDataFolder() + "/translate/", "zh_tw.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(cnTransFile, "utf-8"), HashMap.class);
            customMap.put("zh_cn", hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_cn 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(usTransFile, "utf-8"), HashMap.class);
            customMap.put("en_us", hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 en_us 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(twTransFile, "utf-8"), HashMap.class);
            customMap.put("zh_tw", hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_tw 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        NodeUtil.sendConsole("&f注入自定义语言翻译文件内容完成","&fCustom translations imported...");
    }

    @Override
    public void reloadCustomTranslations() {
        this.initCustomTranslations();
    }
}
