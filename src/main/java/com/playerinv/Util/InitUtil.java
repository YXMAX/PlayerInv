package com.playerinv.Util;

import com.playerinv.Command.CommandHandler;
import com.playerinv.Listener.*;
import com.playerinv.Util.PlaceHolder.PlaceholderExpansion;
import com.playerinv.Util.Translator.TranslatorImpl.LegacyTranslator;
import com.playerinv.Util.Translator.TranslatorImpl.ModernTranslator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.jpountz.lz4.LZ4Factory;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import space.arim.morepaperlib.MorePaperLib;

import java.sql.SQLException;

import static com.playerinv.PlayerInv.*;

public class InitUtil {

    public static MorePaperLib scheduler;

    public static LZ4Factory factory;

    public static boolean isOnlineMode;

    public static boolean isDebug = false;

    public static boolean is113 = false;

    public static boolean isBelow17 = false;

    public static boolean isBelow113 = false;

    public static boolean Below116 = false;

    public static boolean is121 = false;

    public static boolean isUp118 = false;

    public static boolean isUp1193 = false;

    public static boolean isUp1205 = false;

    public static boolean hasPAPI = false;

    public static boolean isMySQL = false;


    public void initMorePaperLib(){
        scheduler = new MorePaperLib(plugin);
    }

    public void initLz4Factory() {
        factory = LZ4Factory.fastestInstance();
    }

    public void checkOnlineMode(){
        isOnlineMode = Bukkit.getOnlineMode();
    }

    public void isDebugMode(){
        isDebug = plugin.getConfig().getBoolean("Debug-mode");
    }

    public void initItemsLangAPI(){
        if(isBelow113){
            itemTranslator = new LegacyTranslator();
            itemTranslator.init();
        } else {
            itemTranslator = new ModernTranslator();
            itemTranslator.init();
        }
    }

    public void detectServerVersion(){
        String v = Bukkit.getBukkitVersion().split("-")[0];
        String[] split = v.split("\\.");
        if(!split[0].equals("1")){
            isUp1193 = true;
            isUp118 = true;
            is121 = true;
            return;
        }
        switch(split[1]){
            case "6":
            case "7":
                isBelow17 = true;
                isBelow113 = true;
                return;
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
                isBelow113 = true;
                return;
            case "13":
                is113 = true;
                return;
            case "14":
            case "15":
                Below116 = true;
                return;
            case "18":
                isUp118 = true;
                return;
            case "19":
                if(Integer.parseInt(v.split("\\.")[2]) >= 3){
                    isUp1193 = true;
                }
                isUp118 = true;
                return;
            case "20":
                if(Integer.parseInt(v.split("\\.")[2]) >= 5){
                    isUp1205 = true;
                }
                isUp1193 = true;
                isUp118 = true;
                return;
            case "21":
                isUp1205 = true;
                isUp1193 = true;
                isUp118 = true;
                is121 = true;
                return;
        }
    }

    public boolean connectToDatabases(){
        boolean mysql = plugin.getConfig().getBoolean("DataBases.MySQL");
        return operationManager.runCheckTable(mysql);
    }

    public HikariDataSource getMySQLDataSource() {
        String host = plugin.getConfig().getString("DataBases.host");
        String port = plugin.getConfig().getString("DataBases.port");
        String user = plugin.getConfig().getString("DataBases.user");
        String password = plugin.getConfig().getString("DataBases.password");
        String database = plugin.getConfig().getString("DataBases.database");
        int maxPoolSize = plugin.getConfig().getInt("DataBases.maxPoolSize");
        int minIdle = plugin.getConfig().getInt("DataBases.minIdle");
        boolean public_key = plugin.getConfig().getBoolean("DataBases.allowPublicKeyRetrieval");
        boolean ssl = plugin.getConfig().getBoolean("DataBases.useSSL");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=" + public_key + "&rewriteBatchedStatements=true&useSSL=" + ssl;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setMaxLifetime(1800000);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(3500);
        config.setLeakDetectionThreshold(2000);
        config.setConnectionTestQuery("SELECT 1");
        HikariDataSource ds = new HikariDataSource(config);
        try{
            ds.getConnection();
        } catch (SQLException e){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] Failed to connect to database. Check MySQL is enabled");
            e.printStackTrace();
            return null;
        }
        return ds;
    }

    public HikariDataSource getDataSource(){
        HikariConfig config = new HikariConfig();
        String url = System.getProperty("user.dir");
        config.setJdbcUrl("jdbc:sqlite:"+url+"/plugins/PlayerInv/"+"Database.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(25);
        config.setMaxLifetime(1800000);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(5000);
        config.setConnectionTestQuery("SELECT 1");
        config.setLeakDetectionThreshold(2000);
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    public void initListeners() {
        plugin.getServer().getPluginManager().registerEvents((Listener)new VaultMenuListener(), (Plugin)plugin);
        plugin.getServer().getPluginManager().registerEvents((Listener)new VaultStorageListener(), (Plugin)plugin);
        plugin.getServer().getPluginManager().registerEvents((Listener)new CheckOperationListener(), (Plugin)plugin);
        plugin.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)plugin);
        plugin.getServer().getPluginManager().registerEvents((Listener)new AutoPickupListener(), (Plugin)plugin);
    }

    public void initCommand(){
        Bukkit.getPluginCommand("playerinv").setExecutor(new CommandHandler());
    }

    public void initPlaceHolderAPI(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderExpansion(plugin).register();
            hasPAPI = true;
        }
    }

    public void initLuckperms(){
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                if(provider != null){
                    permissionManager.initLuckpermService();
                    permissionManager.getLuckpermsService().setAPI(provider.getProvider());
                    hasLuckPerms = true;
                }
            } catch (NoClassDefFoundError e) {
                return;
            }
        }
    }
}
