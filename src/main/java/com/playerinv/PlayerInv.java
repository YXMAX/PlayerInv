package com.playerinv;

import com.google.common.base.Charsets;
import com.playerinv.API.PlayerInvAPI;
import com.playerinv.Listener.PluginChannelListener;
import com.playerinv.Manager.*;
import com.playerinv.Util.InitUtil;
import com.playerinv.Util.Metric.Metrics;
import com.playerinv.Util.Object.Sound.SoundManager;
import com.playerinv.Util.PlaceHolder.PlaceholderTemp;
import com.playerinv.Util.Scheduler.*;
import com.playerinv.Util.JDBCUtil;
import com.playerinv.Util.LoadUtil;
import com.playerinv.Util.Translator.Translator;
import com.playerinv.Util.Yaml.CommentYamlConfiguration;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.playerinv.Util.InitUtil.isUp118;

public class PlayerInv extends JavaPlugin {

    public static PlayerInv plugin;

    public static Boolean hasLuckPerms = false;

    public static File localeConfigFile;

    public static FileConfiguration localeConfig;

    // 自定义翻译文件
    public static File cnTransFile;
    public static File usTransFile;
    public static File twTransFile;
    public static File hkTransFile;
    public static File lzhTransFile;
    //

    // 仓库数量
    public static int largeVaultAmount;
    public static int mediumVaultAmount;
    //

    public static HashMap<String, PlaceholderTemp> playerExpiryMap_Large = new HashMap<>();

    public static HashMap<String, PlaceholderTemp> playerExpiryMap_Medium = new HashMap<>();

    public static boolean vaultPickupEvent = false;

    public static boolean vaultPlaceEvent = false;

    private final InitUtil initUtil = new InitUtil();

    private final LoadUtil loadUtil = new LoadUtil();

    public static final JDBCUtil jdbcUtil = new JDBCUtil();

    public static final OperationManager operationManager = new OperationManager();

    public static FileConfiguration config;
    public static File configFile;

    public static SoundManager soundManager;

    public static Translator itemTranslator;

    public static final VaultAttributesManager vaultAttributesManager = new VaultAttributesManager();

    public static final CacheInventoryManager cacheInventoryManager = new CacheInventoryManager();

    public static final PickupManager pickupManager = new PickupManager();

    public static final MenuManager menuManager = new MenuManager();

    public static final VaultManager vaultManager = new VaultManager();

    public static final PermissionManager permissionManager = new PermissionManager();

    public static final PlayerInvAPI playerInvAPI = new PlayerInvAPI();

    public static boolean has_update = false;

    public static String newer_version = null;

    public void onEnable(){
        plugin = this;
        new Metrics(this, 20554);
        NBT.preloadApi();
        initUtil.detectServerVersion();
        loadUtil.loadPluginConfig(true);
        loadUtil.loadPrefix();
        loadUtil.loadLocale();
        initUtil.isDebugMode();
        initUtil.initMorePaperLib();
        initUtil.initLz4Factory();
        initUtil.initItemsLangAPI();
        initUtil.checkOnlineMode();

        if(!permissionManager.initVaultDepend()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initUtil.initPlaceHolderAPI();
        initUtil.initLuckperms();
        initUtil.initListeners();
        initUtil.initCommand();

        if(!initUtil.connectToDatabases()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",new PluginChannelListener());

        loadUtil.loadLuckpermsSettings();
        loadUtil.loadVaultAmount();

        loadUtil.loadLocaleConfig();
        loadUtil.updateLocaleConfig();

        loadUtil.loadVaultMenuConfig();

        loadUtil.loadFunction();
        loadUtil.loadSound();
        loadUtil.loadPickupList();
        loadUtil.loadVaultEvent();

        menuManager.structAll();

        loadUtil.startNotice();
        ResetScheduler.startCounter();
        new BackupScheduler().runImport();
        new BackupScheduler().runExport();
    }

    public PlayerInvAPI getInstance(){
        return playerInvAPI;
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        operationManager.shutdownPlugin();
    }

    @Override
    public void reloadConfig() {
        config = new YamlConfiguration();
        if(isUp118){
            config = YamlConfiguration.loadConfiguration(configFile);
            Locale locale = Locale.getDefault();
            String language = locale.toLanguageTag();
            InputStream defConfigStream = null;
            if(language.equals("zh-CN")){
                defConfigStream = this.getResource("config/zh-CN/config.yml");
            } else {
                defConfigStream = this.getResource("config/en-US/config.yml");
            }
            if (defConfigStream != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
        } else {
            config = CommentYamlConfiguration.loadConfiguration(configFile);
            Locale locale = Locale.getDefault();
            String language = locale.toLanguageTag();
            InputStream defConfigStream = null;
            if(language.equals("zh-CN")){
                defConfigStream = this.getResource("config/zh-CN/config.yml");
            } else {
                defConfigStream = this.getResource("config/en-US/config.yml");
            }
            if(defConfigStream != null) {
                config.setDefaults(CommentYamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
        }
    }

    @Override
    public FileConfiguration getConfig(){
        if(config == null){
            this.reloadConfig();
        }
        return config;
    }
}
