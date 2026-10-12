package com.playerinv;

import com.google.common.base.Charsets;
import com.playerinv.api.PlayerInvAPI;
import com.playerinv.api.impl.VaultUnitManagerImpl;
import com.playerinv.command.CommandHandler;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.constants.DependencyConstants;
import com.playerinv.listener.CustomMenuListener;
import com.playerinv.listener.CustomVaultListener;
import com.playerinv.listener.player.EnderChestListener;
import com.playerinv.listener.player.PlayerDeathListener;
import com.playerinv.listener.player.PlayerInteractListener;
import com.playerinv.listener.channel.PluginChannelListener;
import com.playerinv.manager.*;
import com.playerinv.service.gson.GsonParser;
import com.playerinv.service.serializer.InventorySerializer;
import com.playerinv.util.*;
import com.playerinv.service.bstats.BStatsService;
import com.playerinv.manager.SoundManager;
import com.playerinv.service.translator.Translator;
import com.playerinv.service.translator.impl.LegacyTranslator;
import com.playerinv.service.translator.impl.ModernTranslator;
import com.playerinv.service.yaml.LegacyYamlConfiguration;
import com.playerinv.util.jdbc.JDBCUtil;
import com.playerinv.util.message.MessageUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.morepaperlib.scheduling.GracefulScheduling;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import static com.playerinv.util.NodeUtil.sendConsole;

public class PlayerInv extends JavaPlugin {

    // 插件主类
    private static PlayerInv instance;

    // 插件调度器
    private static MorePaperLib scheduler;

    // 配置文件
    private FileConfiguration config;

    // 语言文件
    private FileConfiguration localeConfig;

    private SoundManager soundManager;
    private Translator itemTranslator;
    private final PermissionManager permissionManager = new PermissionManager();
    private final VoucherManager voucherManager = new VoucherManager();
    private final PlayerCacheManager playerCacheManager = new PlayerCacheManager();
    private final VaultAttributesManager vaultAttributesManager = new VaultAttributesManager();
    private final CacheInventoryManager cacheInventoryManager = new CacheInventoryManager();
    private final ChatBarManager chatBarManager = new ChatBarManager();
    private final PickupManager pickupManager = new PickupManager();
    private final MenuManager menuManager = new MenuManager();
    private final VaultManager vaultManager = new VaultManager();
    private final GsonParser gsonParser = new GsonParser();

    public static PlayerInv getInstance() {
        return instance;
    }

    public static GracefulScheduling getScheduler(){
        return scheduler.scheduling();
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Translator getTranslator() {
        return itemTranslator;
    }

    public VoucherManager getVoucherManager() {
        return voucherManager;
    }

    public PlayerCacheManager getPlayerCacheManager() {
        return playerCacheManager;
    }

    public VaultAttributesManager getVaultAttributesManager() {
        return vaultAttributesManager;
    }

    public CacheInventoryManager getCacheInventoryManager() {
        return cacheInventoryManager;
    }

    public PickupManager getPickupManager() {
        return pickupManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public ChatBarManager getChatBarManager() {
        return chatBarManager;
    }

    public GsonParser getGsonParser() {
        return gsonParser;
    }

    public void onEnable(){
        instance = this;
        scheduler = new MorePaperLib(this);
        new BStatsService(this, 20554);

        if(!this.initVault()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        VersionUtil.checkServerVersion();
        LocaleUtil.checkServerLanguage();
        MessageUtil.initMiniMessageSupport();
        NBT.preloadApi();

        // 可重载
        this.reloadConfig();
        MessageUtil.loadPrefix();
        LocaleUtil.checkConfigLanguage();
        LocaleUtil.reloadLocaleConfig();
        //

        if(!JDBCUtil.connect()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        ConfigConstants.ONLINE_MODE = Bukkit.getOnlineMode();
        this.loadTranslator();
        this.initCommands();
        DependencyConstants.initPlaceholderAPI();
        DependencyConstants.initLuckpermsService();

        // 可重载
        ConfigConstants.reloadConfigConstants();
        InventorySerializer.loadPolicy();

        vaultAttributesManager.reload();
        voucherManager.reload();
        vaultManager.reload();
        pickupManager.reload();
        menuManager.reload();
        this.loadSoundManager();
        //

        this.initListener();

        playerCacheManager.startResetScheduler();

        // 数据库就绪后再暴露 API, 供第三方插件调用 PlayerInvAPI 的静态方法
        PlayerInvAPI.register(new VaultUnitManagerImpl());

        this.start();
    }

    @Override
    public void onDisable() {
        PlayerInvAPI.unregister();
        this.shutdown();
    }

    @Override
    public void reloadConfig() {
        File file = new File(this.getDataFolder(), "config.yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            this.createConfig();
        }
        config = new YamlConfiguration();
        if(VersionUtil.isUpToVer1_18()){
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config = LegacyYamlConfiguration.loadConfiguration(file);
        }
        InputStream defConfigStream = this.getResource("config/" + Locale.getDefault().toLanguageTag() + "/config.yml");
        if (defConfigStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
        this.updateConfig();
    }

    private void createConfig(){
        Locale locale = Locale.getDefault();
        String tag = locale.toLanguageTag();
        InputStream resource = this.getResource("config/" + tag + "/config.yml");
        if(resource == null){
            resource = this.getResource("config/en-US/config.yml");
        }
        try {
            FileUtils.copyInputStreamToFile(resource, new File(this.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileConfiguration getConfig(){
        if(config == null){
            this.reloadConfig();
        }
        return config;
    }

    public void updateConfig() {
        File file = new File(instance.getDataFolder(), "config.yml");
        boolean change = false;
        Configuration defaults = config.getDefaults();
        for (String defaultKey : defaults.getKeys(true)) {
            if (!config.contains(defaultKey, true)) {
                config.set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public FileConfiguration getLocaleConfig(){
        return localeConfig;
    }

    public void setLocaleConfig(FileConfiguration localeConfig) {
        this.localeConfig = localeConfig;
    }

    public void shutdown(){
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
        sendConsole("&f插件卸载...","&fPlugin disabled...");
        cacheInventoryManager.uploadAll().thenRun(() -> {
            if(JDBCUtil.MYSQL_STATUS){
                JDBCUtil.close();
                sendConsole("&f已断开与 MySQL 数据库的连接...","&fDisconnected from MySQL database...");
            } else {
                JDBCUtil.close();
                sendConsole("&f已断开与 SQLite 数据库的连接...","&fDisconnected from SQLite database...");
            }
            pickupManager.closeThreadPool();
            vaultManager.closeThreadPool();
        });
    }

    public void start(){
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",new PluginChannelListener());
        String papi = "&cPlaceholderAPI";
        String lp = "&cLuckperms";
        if(DependencyConstants.PAPI_ENABLED){
            papi = "&aPlaceholderAPI";
        }
        if(DependencyConstants.LUCKPERMS_ENABLED){
            lp = "&aLuckperms";
        }
        sendConsole("&e插件类型: &aLIGHTWEIGHT","&ePlugin type: &aLIGHTWEIGHT");
        sendConsole("&e插件运行于: " + Bukkit.getBukkitVersion(),"&eRunning on: " + Bukkit.getBukkitVersion());
        sendConsole("&e插件版本: " + getInstance().getDescription().getVersion(),"&ePlugin version: " + getInstance().getDescription().getVersion());
        sendConsole("&e软依赖插件支持: " + papi + " &e, " + lp,"&eSoft-dependency support: " + papi + " &e, " + lp);
    }

    public void loadSoundManager(){
        if(soundManager == null){
            soundManager = new SoundManager();
        }
        soundManager.reloadSound();
    }

    public void loadTranslator(){
        if(VersionUtil.isBelowVer1_13()){
            itemTranslator = new LegacyTranslator();
            itemTranslator.init();
        } else {
            itemTranslator = new ModernTranslator();
            itemTranslator.init();
        }
    }

    public boolean initVault(){
        try {
            if(Bukkit.getPluginManager().getPlugin("Vault") == null){
                throw new NoClassDefFoundError();
            }
            RegisteredServiceProvider<Permission> rsp = getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if(rsp == null){
                throw new NoClassDefFoundError();
            }
            permissionManager.initVaultDepend(rsp.getProvider());
            return true;
        } catch (Exception e) {
            NodeUtil.sendError("Missing Vault or Permission-Management plugin, make sure is installed!","缺少 Vault或权限组 插件,请确保前置插件已安装!");
            e.printStackTrace();
            return false;
        }
    }

    public void initListener(){
        Bukkit.getServer().getPluginManager().registerEvents(new CustomMenuListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomVaultListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        if(ConfigConstants.FUNCTION_ENDERCHEST){
            Bukkit.getServer().getPluginManager().registerEvents(new EnderChestListener(), this);
        }
        if(ConfigConstants.FUNCTION_DEATH_STORE){
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        }
    }

    public void initCommands(){
        Bukkit.getPluginCommand("playerinv").setExecutor(new CommandHandler());
    }
}
