package com.playerinv.Util.Translator.TranslatorImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.playerinv.Enums.Lang.Category;
import com.playerinv.Enums.Lang.Lang;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Translator.Translator;
import org.apache.commons.io.FileUtils;
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
        NodeUtil.sendConsole("&6加载语言文件..","&6Loading language files...");
        for (Lang lang : langs){
            map.put(lang, loadOne(lang.toString().toLowerCase()));
        }
        NodeUtil.sendConsole("&a成功加载语言文件: zh-CN, zh-TW, zh-HK, en-US, lzh","&aLoaded language files: zh-CN, zh-TW, zh-HK, en-US, lzh");
    }

    private HashMap<String, String> loadOne(String locale) {
        InputStream file = plugin.getResource("lang/" + locale + ".json");
        HashMap hashMap = new Gson().fromJson(new InputStreamReader(file), HashMap.class);
        return hashMap;
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
        hkTransFile = new File(plugin.getDataFolder() + "/translate/", "zh_hk.json");
        if(!hkTransFile.exists()){
            hkTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("translate/zh_hk.json"),new File(plugin.getDataFolder() + "/translate/", "zh_hk.json"));
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
        lzhTransFile = new File(plugin.getDataFolder() + "/translate/", "lzh.json");
        if(!lzhTransFile.exists()){
            lzhTransFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("translate/lzh.json"),new File(plugin.getDataFolder() + "/translate/", "lzh.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(cnTransFile, "utf-8"), HashMap.class);
            customMap.put(Lang.ZH_CN, hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_cn 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(usTransFile, "utf-8"), HashMap.class);
            customMap.put(Lang.EN_US, hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 en_us 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(hkTransFile, "utf-8"), HashMap.class);
            customMap.put(Lang.ZH_HK, hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_hk 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(twTransFile, "utf-8"), HashMap.class);
            customMap.put(Lang.ZH_TW, hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 zh_tw 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        try {
            HashMap hashMap = gson.fromJson(FileUtils.readFileToString(lzhTransFile, "utf-8"), HashMap.class);
            customMap.put(Lang.LZH, hashMap);
        } catch (Exception e) {
            NodeUtil.sendError("解析 lzh 自定义翻译语言文件时出现错误: " + e.getMessage());
        }
        NodeUtil.sendConsole("&f注入自定义语言翻译文件内容完成","&fCustom translations imported...");
    }

    @Override
    public void reloadCustomTranslations() {
        this.initCustomTranslations();
    }
}
