/*
 * Copyright (c) 2015 Jerrell Fang
 *
 * This project is Open Source and distributed under The MIT License (MIT)
 * (http://opensource.org/licenses/MIT)
 *
 * You should have received a copy of the The MIT License along with
 * this project.   If not, see <http://opensource.org/licenses/MIT>.
 */

package com.playerinv.Enums.Lang.Legacy;

import com.playerinv.Util.NodeUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.playerinv.PlayerInv.plugin;

public enum EnumLang {


    EN_US("en_us", new HashMap<>()),
    ZH_TW("zh_tw", new HashMap<>()),
    ZH_CN("zh_cn", new HashMap<>());

    private static final Map<String, EnumLang> langMap = new HashMap<>();

    private String locale;
    private HashMap<String, String> map;

    EnumLang(String locale, HashMap<String, String> map) {
        this.locale = locale;
        this.map = map;
    }

    public static EnumLang get(String locale) {
        EnumLang result = langMap.get(locale);
        return result == null ? EN_US : result;
    }

    public static boolean hasLang(String locale) {
        return langMap.containsKey(locale);
    }

    public static void init() {
        NodeUtil.sendConsole("&6加载语言文件..","&6Loading language files...");
        for (EnumLang lang : EnumSet.allOf(EnumLang.class)){
            langMap.put(lang.getLocale(), lang);
        }
        for (EnumLang enumLang : EnumLang.values()) {
            try {
                readFile(enumLang);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NodeUtil.sendConsole("&a成功加载语言文件: zh-CN, en-US","&aLoaded language files: zh-CN, zh-TW, en-US");
        EnumItem.init();
    }

    public static void readFile(EnumLang enumLang) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(plugin.getResource("lang/legacy/" + enumLang.getLocale() + ".lang"), StandardCharsets.UTF_8));
        String temp;
        String[] tempStringArr;
        try {
            temp = reader.readLine();
            while (temp != null) {
                if (temp.startsWith("#")) continue;
                if (temp.contains("=")) {
                    tempStringArr = temp.split("=");
                    enumLang.map.put(tempStringArr[0], tempStringArr.length > 1 ? tempStringArr[1] : "");
                }
                temp = reader.readLine();
            }
        } finally {
            reader.close();
        }
    }

    public String getLocale() {
        return locale;
    }

    public HashMap<String, String> getMap() {
        return map;
    }
}
