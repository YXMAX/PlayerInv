package com.playerinv;

import com.playerinv.Command.InvCommand;
import com.playerinv.Metric.Metrics;
import com.playerinv.PlaceHolder.PlaceholderExpansion;
import com.playerinv.Scheduler.MySQLScheduler;
import com.playerinv.Scheduler.PlaceHolderScheduler;
import com.playerinv.TempHolder.TempPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.*;

public class PlayerInv extends JavaPlugin {

    public static PlayerInv plugin;
    public static Permission perms = null;

    public static LuckPerms API;

    public static Boolean hasLuckPerms = false;

    private static File CN_File;

    private static File US_File;

    public static FileConfiguration MainMenuConfig;

    public static File MainMenu;

    public static FileConfiguration Check_MainMenuConfig;

    public static File Check_MainMenu;

    public static List<TempPlayer> Insert_TempList_Large;

    public static List<TempPlayer> Insert_TempList_Medium;

    public static File LocaleConfigFile;
    public static FileConfiguration LocaleConfig;

    public static HashMap<Integer,String> MainMenuItemMap = new HashMap<>();

    public static HashMap<Integer,Integer> MainMenuVaultSlotMap_Large = new HashMap<>();

    public static HashMap<Integer,Integer> MainMenuVaultSlotMap_Medium = new HashMap<>();

    public static HashMap<String,FileConfiguration> VaultMenuMap;

    public static HashMap<String,String> OtherMenuItemMap = new HashMap<>();

    public static HashMap<String,Integer> OtherMenuVaultSlotMap_Large = new HashMap<>();

    public static HashMap<String,Integer> OtherMenuVaultSlotMap_Medium = new HashMap<>();

    public static HashMap<Inventory,String> OtherMenuInventoryMap = new HashMap<>();

    public static List<String> OtherMenuFileList;

    public static HashMap<String,File> OtherMenuFileMap = new HashMap<>();

    public static HashMap<Integer,String> Check_MainMenuItemMap = new HashMap<>();

    public static HashMap<Integer,Integer> Check_MainMenuVaultSlotMap_Large = new HashMap<>();

    public static HashMap<Integer,Integer> Check_MainMenuVaultSlotMap_Medium = new HashMap<>();

    public static HashMap<String,FileConfiguration> Check_VaultMenuMap;

    public static HashMap<String,String> Check_OtherMenuItemMap = new HashMap<>();

    public static HashMap<String,Integer> Check_OtherMenuVaultSlotMap_Large = new HashMap<>();

    public static HashMap<String,Integer> Check_OtherMenuVaultSlotMap_Medium = new HashMap<>();

    public static HashMap<Inventory,String> Check_OtherMenuInventoryMap = new HashMap<>();

    public static List<String> Check_OtherMenuFileList;

    public static HashMap<String,File> Check_OtherMenuFileMap = new HashMap<>();

    public static HashMap<Player, Player> Check_OnlinePlayerMap = new HashMap<>();

    public static HashMap<Player, OfflinePlayer> Check_OfflinePlayerMap = new HashMap<>();

    public static LinkedHashMap<Player,String> Placeholder_Vault_Amount = new LinkedHashMap<>();

    public static List<String> Placeholder_List = new ArrayList<>();

    public static int Large_Amount;

    public static int Medium_Amount;

    public static Boolean is113 = false;

    public static Boolean isBelow113 = false;

    public static Boolean Below116 = false;

    public static Boolean is121 = false;

    public static Boolean lpsupport = false;

    public static Boolean isFolia = false;
    public static Boolean hasPAPI = false;

    public static MorePaperLib FoliaLib;

    public static HashMap<String,Long> PlayerExpiryMap_Large = new HashMap<>();

    public static HashMap<String,Long> PlayerExpiryMap_Medium = new HashMap<>();

    public static HashMap<Player,Boolean> TempPlayerInInventory_Large = new HashMap<>();

    public static HashMap<Player,Boolean> TempPlayerInInventory_Medium = new HashMap<>();

    public static HashMap<String,Inventory> TempInventory_Large = new HashMap<>();

    public static HashMap<String,Inventory> TempInventory_Medium = new HashMap<>();

    public static boolean has_update = false;

    public static String newer_version = null;

    public void onEnable(){
        int pluginId = 20554;
        Metrics metrics = new Metrics(this, pluginId);
        saveDefaultConfig();
        plugin = this;
        isDebug();
        DetectFolia();
        Large_Amount = Vault_large_amount();
        Medium_Amount = Vault_medium_amount();
        createLocaleConfig_Detect();
        DetectServerVersion();
        MySQL_Detect();
        setupPermissions();
        loadMainMenuConfig();
        createOtherMenuConfig();
        getFile_writeMap();
        getCheckFile_writeMap();
        reloadSplitSoundValue_Voucher();
        FixTable_InsertNew(con);
        Bukkit.getPluginCommand("playerinv").setExecutor(new InvCommand());
        initListeners();
        PlaceHolderScheduler.Placeholder_start();
        Luckperms_Detect();
        PlaceHolderAPI_Register();
        Update_Locale_Config();
        Update_Config();
        initMorePaperLib();
        PluginStartUp();
        checkUpdate();
    }

    @Override
    public void onDisable() {
        String locale = locale();
        if(locale.equals("zh-CN")){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "插件卸载中...");
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "已断开与本地数据库连接");
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Plugin Disabled..");
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Disconnected from local database..");
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save the database, please check if there is any behavior of occupying the database", e);
        }
        saveConfig();
        plugin = null;
    }

    public Connection getMySQLConnection() {
        String host = getConfig().getString("DataBases.host");
        String port = getConfig().getString("DataBases.port");
        String user = getConfig().getString("DataBases.user");
        String password = getConfig().getString("DataBases.password");
        String database = getConfig().getString("DataBases.database");
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + allowPublicKeyRetrieval() + "&rewriteBatchedStatements=true&useSSL=" + sslBool();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            HikariDataSource ds = new HikariDataSource(config);
            return ds.getConnection();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Failed to connect to database. Check MySQL is enabled");
            e.printStackTrace();
            return null;
        }
    }

    private boolean setupPermissions() {
        try {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            perms = rsp.getProvider();
            return perms != null;
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException("Detecting Vault failed, make sure is installed and restarted to load the plugin", e);
        }
    }

    private void createLocaleConfig_Detect() {
        CN_File = new File(getDataFolder() + "/locale/", "zh-CN.yml");
        if (!CN_File.exists()) {
            CN_File.getParentFile().mkdirs();
            saveResource("locale/zh-CN.yml", false);
        }

        US_File = new File(getDataFolder() + "/locale/", "en-US.yml");
        if (!US_File.exists()) {
            US_File.getParentFile().mkdirs();
            saveResource("locale/en-US.yml", false);
            String current_locale = locale();
            Locale locale = Locale.getDefault();
            String language = locale.toLanguageTag();
            if(language.equals("zh-CN") && current_locale.equals("en-US")){
                plugin.getConfig().set("Language", "zh-CN");
                saveConfig();
            }
        }
        String fixed_locale = locale();
        if(fixed_locale.equals("zh-CN")){
            LocaleConfigFile = CN_File;
            LocaleConfig = new YamlConfiguration();
            try {
                LocaleConfig.load(LocaleConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            LocaleConfigFile = US_File;
            LocaleConfig = new YamlConfiguration();
            try {
                LocaleConfig.load(LocaleConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void reloadLocale_GUI(){
        String Fixed_locale = locale();
        if(LocaleMap.containsKey(Fixed_locale + ".yml")){
            File locale_file = LocaleMap.get(Fixed_locale + ".yml");
            LocaleConfigFile = locale_file;
            reloadLocaleConfig();
        } else {
            LocaleConfigFile = US_File;
            reloadLocaleConfig();
        }
    }

    public static void reloadLocaleConfig(){
        YamlConfiguration guiconfig = YamlConfiguration.loadConfiguration(LocaleConfigFile);
        try {
            guiconfig.save(LocaleConfigFile);
            LocaleConfig = guiconfig;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMainMenuConfig(){
        String language = locale();
        MainMenu = new File(getDataFolder() + "/vault_menu/", "Main.yml");
        Check_MainMenu = new File(getDataFolder() + "/check_menu/", "Main.yml");
        if(!MainMenu.exists()){
            MainMenu.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-zh-CN/Main.yml"),new File(getDataFolder() + "/vault_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-en-US/Main.yml"),new File(getDataFolder() + "/vault_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(!Check_MainMenu.exists()){
            Check_MainMenu.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-zh-CN/Main.yml"),new File(getDataFolder() + "/check_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-en-US/Main.yml"),new File(getDataFolder() + "/check_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            Check_MainMenuConfig = new YamlConfiguration();
            Check_MainMenuConfig.load(Check_MainMenu);
            MainMenuConfig = new YamlConfiguration();
            MainMenuConfig.load(MainMenu);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load main menu file",e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException("Cannot read the main menu file, check the file is exist",e);
        }
    }

    private void createOtherMenuConfig(){
        String language = locale();
        File other = new File(getDataFolder() + "/vault_menu/", "vault_page_2.yml");
        File other_3 = new File(getDataFolder() + "/vault_menu/", "vault_page_3.yml");
        if(!other.exists()){
            other.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-zh-CN/vault_page_2.yml"),new File(getDataFolder() + "/vault_menu/", "vault_page_2.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-en-US/vault_page_2.yml"),new File(getDataFolder() + "/vault_menu/", "vault_page_2.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(!other_3.exists()){
            other_3.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-zh-CN/vault_page_3.yml"),new File(getDataFolder() + "/vault_menu/", "vault_page_3.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("vault_menu-en-US/vault_page_3.yml"),new File(getDataFolder() + "/vault_menu/", "vault_page_3.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        File check_other = new File(getDataFolder() + "/check_menu/", "check_vault_page_2.yml");
        File check_other_3 = new File(getDataFolder() + "/check_menu/", "check_vault_page_3.yml");
        if(!check_other.exists()){
            check_other.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-zh-CN/check_vault_page_2.yml"),new File(getDataFolder() + "/check_menu/", "check_vault_page_2.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-en-US/check_vault_page_2.yml"),new File(getDataFolder() + "/check_menu/", "check_vault_page_2.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(!check_other_3.exists()){
            check_other_3.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-zh-CN/check_vault_page_3.yml"),new File(getDataFolder() + "/check_menu/", "check_vault_page_3.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(getResource("check_menu-en-US/check_vault_page_3.yml"),new File(getDataFolder() + "/check_menu/", "check_vault_page_3.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void MySQL_Detect(){
        Boolean mysql = getConfig().getBoolean("DataBases.MySQL");
        if (!mysql) {
            try {
                con = getConnection();
                if(con != null){
                    sendLog("HikariCP 数据池创建成功");
                } else {
                    sendLog("HikariCP 数据池创建失败");
                }
                createLargeTable(con);
                createMediumTable(con);
                createToggleTable(con);
                createReturnToggleTable(con);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (mysql) {
            try {
                con = getMySQLConnection();
                if(con != null){
                    sendLog("HikariCP 数据池连接MySQL成功");
                } else {
                    sendLog("HikariCP 数据池连接MySQL失败");
                }
                MysqlcreateLargeTable(con);
                MysqlcreateMediumTable(con);
                MysqlcreateToggleTable(con);
                MysqlcreateReturnToggleTable(con);
                MySQLScheduler.Mysqlconnect();
                FixMySQL_DataType_Large();
                FixMySQL_DataType_Medium();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void Luckperms_Detect(){
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                if(provider != null){
                    API = provider.getProvider();
                    hasLuckPerms = true;
                }
                lpsupport = true;
            } catch (NoClassDefFoundError e) {

            }
        }
    }

    private void PlaceHolderAPI_Register(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderExpansion(this).register();
            hasPAPI = true;
        }
    }

    private void initMorePaperLib(){
        if(isFolia){
            FoliaLib = new MorePaperLib(plugin);
        }
    }

}
