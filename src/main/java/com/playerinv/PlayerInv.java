package com.playerinv;

import com.playerinv.Command.InvCommand;
import com.playerinv.Metric.Metrics;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.playerinv.SQLite.SQLiteConnect.*;

public class PlayerInv extends PluginSet {

    public static PlayerInv plugin;
    public static Permission perms = null;

    public void onEnable(){
        int pluginId = 20554; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        saveDefaultConfig();
        plugin = this;
        Boolean mysql = getConfig().getBoolean("DataBases.MySQL");
        if (!mysql) {
            try {
                SQLiteConnect.getConnection();
                createTable(judgeconnect());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (mysql) {
            try {
                MysqlcreateTable(judgeconnect());
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "�ɹ�����MySQL���ݿ�");
                MySQLScheduler.Mysqlconnect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        setupPermissions();
        Bukkit.getPluginCommand("playerinv").setExecutor(new InvCommand());
        initListeners();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "���������");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "����汾: 1.3.65");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Powered By YXMAX");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "���ж����...");
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("�޷������������ݿ⣬�����Ƿ�ռ�����ݿ����Ϊ", e);
        }
        saveConfig();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "�ѶϿ��뱾�����ݿ�����");
        plugin = null;
        // Plugin shutdown logic
    }

    public Connection getMySQLConnection() {
        String host = getConfig().getString("DataBases.host");
        String port = getConfig().getString("DataBases.port");
        String user = getConfig().getString("DataBases.user");
        String password = getConfig().getString("DataBases.password");
        String database = getConfig().getString("DataBases.database");
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + allowPublicKeyRetrieval();
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "�������ݿ�ʧ�� ���MySQL���ݿ��Ƿ�����.");
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
            throw new RuntimeException("δ��⵽ǰ�ò�� Vault , ȷ���Ѱ�װ�������������Լ��ظò��", e);
        }
    }
}
