package com.playerinv.Util;

import com.playerinv.Util.Object.Sound.SoundManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.*;
import static com.playerinv.Util.NodeUtil.*;
import static org.bukkit.Bukkit.getServer;

public class LoadUtil {

    public static String prefix;

    public static boolean enderChestOpenBool = false;

    public static boolean luckperms_givePermissions = false;

    public static boolean luckperms_proxySupport = false;

    public static String locale;

    public static boolean autoPickupBool = false;

    public static boolean materialPickupBool = false;

    public static boolean deathStoreBool = false;

    public static boolean deathStoreIgnore = false;

    public static boolean autoPickupActionBar = false;

    public static boolean vaultNameChange = false;

    public static HashMap<String,Integer> Placeholder_VaultAmount = new HashMap<>();

    public static HashSet<String> materialBlackList = new HashSet<>();

    public static List<String> loreBlackList = new ArrayList<>();

    public static List<String> proxy_server = new ArrayList<>();

    public static boolean voucherBool;

    public static String voucherLargeMaterial;

    public static String voucherMediumMaterial;

    public static boolean natural_drop;

    public static String default_large_vault_name;

    public static String default_medium_vault_name;

    public static String default_large_vault_title;

    public static String default_medium_vault_title;

    public static boolean command_returnBool;

    public static String command_returnMenu;

    public static boolean command_returnCmdBool;

    public static int command_returnCmdType;

    public static String command_returnCmdString;

    public static boolean clearItemWhenError;

    public static boolean replaceExpiry;

    public static boolean allowClickGUI;

    public static boolean customTitleReplace;

    public static String openMainMenu;

    public static boolean voucherSetOwner;

    public static boolean onlyPickupNoPermission;

    public static boolean checkUpdate = false;



    public void loadPrefix(){
        prefix = plugin.getConfig().getString("Prefix");
    }

    public void loadLocale(){
        locale = plugin.getConfig().getString("Language");
    }

    public void loadVaultAmount(){
        largeVaultAmount = plugin.getConfig().getInt("Vault.Large_amount");
        mediumVaultAmount = plugin.getConfig().getInt("Vault.Medium_amount");
    }

    public void loadLuckpermsSettings(){
        if(hasLuckPerms){
            luckperms_givePermissions = plugin.getConfig().getBoolean("Luckperms-give-permissions");
            luckperms_proxySupport = plugin.getConfig().getBoolean("Luckperms-proxy-support");
            proxy_server = plugin.getConfig().getStringList("Sync-server");
        }
    }

    public void loadFunction(){
        openMainMenu = plugin.getConfig().getString("Menu.Open_main");
        voucherSetOwner = plugin.getConfig().getBoolean("Voucher.Set-owner");
        enderChestOpenBool = plugin.getConfig().getBoolean("Function.Enderchest_open");
        checkUpdate = plugin.getConfig().getBoolean("check-update");
        autoPickupBool = plugin.getConfig().getBoolean("Inventory.Auto_pickup.enabled");
        materialPickupBool = plugin.getConfig().getBoolean("Inventory.Auto_pickup.material_pickup");
        List<String> mBlackList = plugin.getConfig().getStringList("Inventory.Blacklist");
        materialBlackList.clear();
        for(String s : mBlackList){
            materialBlackList.add(s.toUpperCase());
        }
        loreBlackList = plugin.getConfig().getStringList("Inventory.Lore_blacklist");
        deathStoreBool = plugin.getConfig().getBoolean("Player-death-store.Enabled");
        deathStoreIgnore = plugin.getConfig().getBoolean("Player-death-store.ignore-keepInventory");
        default_large_vault_name = localeConfig.getString("Vault_name.Large_default");
        default_medium_vault_name = localeConfig.getString("Vault_name.Medium_default");
        default_large_vault_title = localeConfig.getString("Vault.Large_title");
        default_medium_vault_title = localeConfig.getString("Vault.Medium_title");
        autoPickupActionBar = plugin.getConfig().getBoolean("Inventory.Auto_pickup.action_bar");
        vaultNameChange = plugin.getConfig().getBoolean("Vault.Name_change");
        voucherBool = plugin.getConfig().getBoolean("Voucher.Enabled");
        voucherLargeMaterial = plugin.getConfig().getString("Voucher.large.material");
        voucherMediumMaterial = plugin.getConfig().getString("Voucher.medium.material");
        natural_drop = plugin.getConfig().getBoolean("Voucher.Natural_drop");
        command_returnBool = plugin.getConfig().getBoolean("Command.Return_menu.Enabled");
        command_returnMenu = plugin.getConfig().getString("Command.Return_menu.Menu");
        command_returnCmdBool = plugin.getConfig().getBoolean("Command.Return_command.Enabled");
        command_returnCmdString = plugin.getConfig().getString("Command.Return_command.Command");
        replaceExpiry = plugin.getConfig().getBoolean("Replace_expiry_variable");
        if(command_returnCmdString != null && !command_returnCmdString.isEmpty()){
            if(command_returnCmdString.toLowerCase().startsWith("[op]")){
                command_returnCmdType = 2;
                command_returnCmdString = command_returnCmdString.replaceAll("\\[op\\]","").trim();
            } else if(command_returnCmdString.toLowerCase().startsWith("[console]")){
                command_returnCmdType = 3;
                command_returnCmdString = command_returnCmdString.replaceAll("\\[console\\]","").trim();
            } else {
                command_returnCmdType = 1;
                command_returnCmdString = command_returnCmdString.replaceAll("\\[player\\]","").trim();
            }
        }
        clearItemWhenError = plugin.getConfig().getBoolean("Clear_item_when_error");
        allowClickGUI = plugin.getConfig().getBoolean("Allow_click_in_menu");
        customTitleReplace = plugin.getConfig().getBoolean("Custom_title_replace_gui_title");
        onlyPickupNoPermission = plugin.getConfig().getBoolean("Vault_can_pickup_no_permission");
    }

    public void loadPluginConfig(boolean boot) {
        if(boot) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            recreateConfig();
        }
        plugin.reloadConfig();
        if(isBelow17){
            return;
        }
        this.updateConfig();
    }

    private void recreateConfig(){
        configFile.getParentFile().mkdirs();
        Locale server_locale = Locale.getDefault();
        String language = server_locale.toLanguageTag();
        if (language.equals("zh-CN")) {
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("config/zh-CN/config.yml"), new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                FileUtils.copyInputStreamToFile(plugin.getResource("config/en-US/config.yml"), new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadLocaleConfig() {
        File CN_File = Paths.get(plugin.getDataFolder().getAbsolutePath(),"locale","zh-CN.yml").normalize().toFile();
        if (!CN_File.exists()) {
            CN_File.getParentFile().mkdirs();
            plugin.saveResource("locale/zh-CN.yml", false);
        }

        File US_File = Paths.get(plugin.getDataFolder().getAbsolutePath(),"locale","en-US.yml").normalize().toFile();
        if (!US_File.exists()) {
            US_File.getParentFile().mkdirs();
            plugin.saveResource("locale/en-US.yml", false);
        }
        try {
            if(NodeUtil.isChinese()){
                localeConfigFile = CN_File;
            } else {
                if(loadOtherLocale()){
                    return;
                }
                localeConfigFile = US_File;
            }
            localeConfig = new YamlConfiguration();
            localeConfig.load(localeConfigFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean loadOtherLocale(){
        String path = plugin.getDataFolder().getAbsolutePath() + "/locale";
        File file = new File(path);
        File[] array = file.listFiles();
        HashMap<String,File> temp_map = new HashMap<>();
        if(array == null){
            if(isWindows()){
                array = file.listFiles();
            }
            if(isLinux()){
                try {
                    Stream<Path> stream = Files.list(Paths.get(path));
                    array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                temp_map.put(array[i].getName(),array[i].getAbsoluteFile());
            }
        }

        if(temp_map.containsKey(locale + ".yml")){
            File locale_file = temp_map.get(locale + ".yml");
            localeConfigFile = locale_file;
            localeConfig = new YamlConfiguration();
            try {
                localeConfig.load(localeConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    public void loadVaultMenuConfig(){
        Path folder_path = Paths.get(plugin.getDataFolder().getAbsolutePath(), "vault_menu").normalize();
        File menu_folder = folder_path.toFile();
        if(menu_folder.exists()){
            return;
        }
        menu_folder.mkdirs();
        File main = Paths.get(plugin.getDataFolder().getAbsolutePath(), "vault_menu","Main.yml").normalize().toFile();
        File other = Paths.get(plugin.getDataFolder().getAbsolutePath(), "vault_menu","vault_page_2.yml").normalize().toFile();
        File other_3 = Paths.get(plugin.getDataFolder().getAbsolutePath(), "vault_menu","vault_page_3.yml").normalize().toFile();
        try {
            if(!main.exists()) {
                if(locale.equalsIgnoreCase("zh-cn")){
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-zh-CN/Main.yml"),main);
                } else {
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-en-US/Main.yml"),main);
                }
            }
            if(!other.exists()) {
                if(locale.equalsIgnoreCase("zh-cn")){
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-zh-CN/vault_page_2.yml"),other);
                } else {
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-en-US/vault_page_2.yml"),other);
                }
            }
            if(!other_3.exists()) {
                if(locale.equalsIgnoreCase("zh-cn")){
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-zh-CN/vault_page_3.yml"),other_3);
                } else {
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-en-US/vault_page_3.yml"),other_3);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLocaleConfig(){
        boolean change = false;
        Configuration defaults = null;
        try {
            InputStream resource = plugin.getResource("locale/" + locale + ".yml");
            File temp = new File(plugin.getDataFolder(),"locale.tmp");
            temp.createNewFile();
            FileUtils.copyInputStreamToFile(resource,temp);
            defaults = YamlConfiguration.loadConfiguration(temp);
            temp.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String defaultKey : defaults.getKeys(true)) {
            if (!localeConfig.contains(defaultKey)) {
                localeConfig.set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            try {
                localeConfig.save(localeConfigFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateConfig(){
        boolean change = false;
        Configuration defaults = config.getDefaults();
        for (String defaultKey : defaults.getKeys(true)) {
            if (!config.contains(defaultKey,true)) {
                config.set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            try {
                plugin.getConfig().save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadSound(){
        if(soundManager == null){
            soundManager = new SoundManager();
        }
        soundManager.reloadSound();
    }

    public void loadPickupList(){
        boolean isReverse = plugin.getConfig().getBoolean("Inventory.Auto_pickup.list_reverse");
        List<String> m_list = plugin.getConfig().getStringList("Inventory.Auto_pickup.detected_material").stream().map(String::toUpperCase).collect(Collectors.toList());
        List<String> list = plugin.getConfig().getStringList("Inventory.Auto_pickup.detected_lore");
        pickupManager.init(isReverse,new HashSet<>(m_list),new HashSet<>(list));
    }

    public void startNotice(){
        Boolean mysql = plugin.getConfig().getBoolean("DataBases.MySQL");
        String version = plugin.getDescription().getVersion();
        if(locale.equals("zh-CN")){
            String database = "本地数据库已启动..";
            String lp = "&c未启用";
            String papi = "&c未启用";
            if(hasLuckPerms){
                lp = "&a启用";
            }
            if(mysql){
                database = "成功连接MySQL数据库..";
            }
            if(hasPAPI){
                papi = "&a启用";
            }
            getServer().getConsoleSender().sendMessage(color(" &b&l ____  &9&l____   "));
            getServer().getConsoleSender().sendMessage(color(" &b&l|    | &9&l ||    &dPlayerInv v" + version + " &aLightWeight"));
            getServer().getConsoleSender().sendMessage(color(" &b&l|____| &9&l ||    &eLuckPerms 权限给予: " + lp));
            getServer().getConsoleSender().sendMessage(color(" &b&l|      &9&l ||    &e" + database));
            getServer().getConsoleSender().sendMessage(color(" &b&l|      &9&l_||_   &ePlaceHolderAPI 支持: " + papi));
            getServer().getConsoleSender().sendMessage(color("                      "));
        } else {
            String database = "SQLite databases connected..";
            String lp = "&cDisabled";
            String papi = "&cDisabled";
            if(hasLuckPerms){
                lp = "&6Disabled";
            }
            if(mysql){
                database = "MySQL database connected..";
            }
            if(hasPAPI){
                papi = "&aEnabled";
            }
            getServer().getConsoleSender().sendMessage(color(" &b&l ____  &9&l____   "));
            getServer().getConsoleSender().sendMessage(color(" &b&l|    | &9&l ||    &dPlayerInv v" + version + " &aLightWeight"));
            getServer().getConsoleSender().sendMessage(color(" &b&l|____| &9&l ||    &eLuckPerms give-permission: " + lp));
            getServer().getConsoleSender().sendMessage(color(" &b&l|      &9&l ||    &e" + database));
            getServer().getConsoleSender().sendMessage(color(" &b&l|      &9&l_||_   &ePlaceHolderAPI Support: " + papi));
            getServer().getConsoleSender().sendMessage(color("                      "));
        }
    }

    public void loadVaultEvent(){
        vaultPlaceEvent = plugin.getConfig().getBoolean("Vault.Event.Place");
        vaultPickupEvent = plugin.getConfig().getBoolean("Vault.Event.Pickup");
    }

    public void checkUpdate(){
        if(!checkUpdate){
            return;
        }
        new UpdateChecker(plugin, 114372).getVersion(version -> {
            String original = version.replaceAll("v","");
            String current = plugin.getDescription().getVersion();
            String[] original_split = original.split("\\.");
            String[] current_split = current.split("\\.");
            int result = 0;
            int length = Math.max(current_split.length, original_split.length);
            for (int i = 0; i < length; i++) {
                Integer v1 = i < current_split.length ? Integer.parseInt(current_split[i]) : 0;
                Integer v2 = i < original_split.length ? Integer.parseInt(original_split[i]) : 0;
                int r = v1.compareTo(v2);
                if (r != 0) {
                    result = r;
                }
            }
            if (result >= 0) {
                sendWarn("No new update available");
            } else {
                has_update = true;
                newer_version = version;
                sendWarn("An new update for PlayerInv (" + version + ") is available at:");
                sendWarn("https://www.spigotmc.org/resources/playerinv.114372/");
            }
        });
    }
}
