package com.playerinv.util;

import com.playerinv.PlayerInv;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.util.message.MessageUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.util.NodeUtil.*;

public class LocaleUtil {

    private static boolean CHINESE_VERSION;

    private static String LOCALE;

    private static String getLocaleMessages(LocaleEnums enums){
        return PlayerInv.getInstance().getLocaleConfig().getString(enums.getDesc());
    }

    public static void checkServerLanguage(){
        CHINESE_VERSION = Locale.getDefault().getLanguage().equals("zh");
    }

    public static void checkConfigLanguage(){
        LOCALE = getInstance().getConfig().getString("Language","en-US");
    }

    public static boolean isChinese(){
        return CHINESE_VERSION;
    }

    public static String getLocale(){
        return LOCALE;
    }

    public static void reloadLocaleConfig() {
        String[] strings = new String[]{"zh-CN.yml","zh-TW.yml","zh-HK.yml","en-US.yml"};
        for(String s : strings){
            File file = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(),"locale",s).normalize().toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                PlayerInv.getInstance().saveResource("locale/" + s, false);
            }
        }
        File result = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(),"locale",LOCALE + ".yml").normalize().toFile();
        if(!result.exists()){
            LOCALE = "en-US";
            result = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(),"locale","en-US.yml").normalize().toFile();
        }
        PlayerInv.getInstance().setLocaleConfig(new YamlConfiguration());
        try {
            PlayerInv.getInstance().getLocaleConfig().load(result);
            updateLocaleConfig(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateLocaleConfig(File file){
        boolean change = false;
        Configuration defaults = null;
        try {
            InputStream resource = getInstance().getResource("locale/en-US.yml");
            if(resource == null) return;
            File temp = new File(getInstance().getDataFolder(),"locale.tmp");
            temp.createNewFile();
            FileUtils.copyInputStreamToFile(resource,temp);
            defaults = YamlConfiguration.loadConfiguration(temp);
            temp.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String defaultKey : defaults.getKeys(true)) {
            if (!PlayerInv.getInstance().getLocaleConfig().contains(defaultKey)) {
                PlayerInv.getInstance().getLocaleConfig().set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            try {
                PlayerInv.getInstance().getLocaleConfig().save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendMessages(CommandSender commandSender, LocaleEnums enums, String ...replace){
        if(commandSender == null){
            return;
        }
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i<count;i++){
            msg = msg.replace(replace[2 * i],replace[(2*i)+1]);
        }
        if(isConsole(commandSender)){
            MessageUtil.sendMsgToConsole(msg);
        } else {
            MessageUtil.sendMsgToChatBar((Player) commandSender,msg);
        }
    }

    public static void sendMessages(Player player, LocaleEnums enums, String ...replace){
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i< count;i++){
            msg = msg.replace(replace[2 * i],replace[(2*i)+1]);
        }
        MessageUtil.sendMsgToChatBar(player,msg);
    }

    public static void sendActionBar(Player player, LocaleEnums enums, String ...replace){
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i< count;i++){
            msg = msg.replace(replace[2 * i],replace[(2*i)+1]);
        }
        MessageUtil.sendMsgToActionBar(player,msg);
    }

    public static String getShareVaultPermissionDisplay(int type){
        switch(type){
            case 1:
                return PlayerInv.getInstance().getLocaleConfig().getString("Sharing_vault_permission.Low");
            case 2:
                return PlayerInv.getInstance().getLocaleConfig().getString("Sharing_vault_permission.Medium");
            case 3:
                return PlayerInv.getInstance().getLocaleConfig().getString("Sharing_vault_permission.High");
            case 4:
            default:
                return PlayerInv.getInstance().getLocaleConfig().getString("Sharing_vault_permission.Max");
        }
    }

    /**
     *
     * @param num
     * 1: 永久
     * 2: 限时
     * 3: 未拥有
     * 4: 不足一天
     * @return
     */
    public static String getExpiryPlaceholderDisplay(int num){
        switch (num){
            case 1:
                return PlayerInv.getInstance().getLocaleConfig().getString("Placeholder_expiry.Permanent");
            case 2:
                return PlayerInv.getInstance().getLocaleConfig().getString("Placeholder_expiry.Expiry");
            case 3:
                return PlayerInv.getInstance().getLocaleConfig().getString("Placeholder_expiry.Not_exist");
            case 4:
                return PlayerInv.getInstance().getLocaleConfig().getString("Placeholder_expiry.Day_less");
            default:
                return PlayerInv.getInstance().getLocaleConfig().getString("Placeholder_expiry.Permanent");
        }
    }
}
