package com.playerinv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.playerinv.PlayerInv.plugin;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class MySQLScheduler implements Runnable {

    public static void Mysqlconnect(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(
                new MySQLScheduler(),
                0,
                60,
                TimeUnit.SECONDS);
    }


    @Override
    public void run() {
        try {
            if (con != null && !con.isClosed()) {
                con.createStatement().execute("SELECT 1");
            }
        } catch (SQLException e) {
            con = plugin.getMySQLConnection();
        }
    }
}
