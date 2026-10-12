package com.playerinv.util.jdbc;

import com.playerinv.PlayerInv;
import com.playerinv.api.impl.VaultUnitImpl;
import com.playerinv.api.impl.VaultUnitMapImpl;
import com.playerinv.api.unit.VaultUnit;
import com.playerinv.api.unit.VaultUnitMap;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.object.cache.VaultStatistics;
import com.playerinv.service.serializer.InventorySerializer;
import com.playerinv.object.container.InventoryContainer;
import com.playerinv.object.temp.RawInventory;
import com.playerinv.util.NodeUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.playerinv.util.message.MessageUtil.sendDebug;
import static com.playerinv.util.NodeUtil.*;


public class JDBCUtil {

    private static HikariDataSource ds;
    private static final ExecutorService LARGE_DECODER_POOL = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
    private static final ExecutorService MEDIUM_DECODER_POOL = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));

    public static AtomicBoolean connectionStatus = new AtomicBoolean(true);
    public static boolean MYSQL_STATUS;

    public static boolean connect(){
        sendConsole("&f正在尝试连接数据库...","Connecting to database...");
        boolean mysql = PlayerInv.getInstance().getConfig().getBoolean("DataBases.MySQL");
        if (mysql) {
            ds = getMySQLDataSource();
            MYSQL_STATUS = true;
        } else {
            ds = getSQLiteDataSource();
        }
        if(ds == null){
            return false;
        }
        JDBCUtil.createLargeTable();
        JDBCUtil.createMediumTable();
        JDBCUtil.createVaultNameTable();
        JDBCUtil.createPickupToggleTable();
        return true;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close(){
        ds.close();
    }

    public static HikariDataSource getMySQLDataSource() {
        HikariDataSource ds = null;
        String host = PlayerInv.getInstance().getConfig().getString("DataBases.host");
        String port = PlayerInv.getInstance().getConfig().getString("DataBases.port");
        String user = PlayerInv.getInstance().getConfig().getString("DataBases.user");
        String password = PlayerInv.getInstance().getConfig().getString("DataBases.password");
        String database = PlayerInv.getInstance().getConfig().getString("DataBases.database");
        int maxPoolSize = PlayerInv.getInstance().getConfig().getInt("DataBases.maxPoolSize");
        int minIdle = PlayerInv.getInstance().getConfig().getInt("DataBases.minIdle");
        boolean public_key = PlayerInv.getInstance().getConfig().getBoolean("DataBases.allowPublicKeyRetrieval");
        boolean ssl = PlayerInv.getInstance().getConfig().getBoolean("DataBases.useSSL");
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
        try{
            ds = new HikariDataSource(config);
            ds.getConnection();
        } catch (Exception e){
            NodeUtil.sendError("Failed to connect to database. Check MySQL is enabled","无法连接至MySQL数据库, 请检查数据库状态, 插件将关闭");
            e.printStackTrace();
            return null;
        }
        NodeUtil.sendConsole("&a连接 MySQL 数据库成功","&aMySQL databases connected..");
        return ds;
    }

    public static HikariDataSource getSQLiteDataSource(){
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
        NodeUtil.sendConsole("&a连接 SQLite 数据库成功","&aSQLite databases connected..");
        return ds;
    }

    public static void closeMySQLInteract(){
        if(!MYSQL_STATUS) return;
        if(!connectionStatus.get()) return;
        connectionStatus.set(false);
        PlayerInv.getScheduler().asyncScheduler().runAtFixedRate(task -> {
            if(!JDBCUtil.checkMySQLConnection()){
                NodeUtil.sendError("无法与MySQL数据库重新连接 稍后将重试...");
                return;
            }
            NodeUtil.sendConsole("成功与MySQL数据库恢复连接");
            task.cancel();
            return;
        }, Duration.ofSeconds(3),Duration.ofSeconds(5));
    }

    public static boolean checkMySQLConnection(){
        if(!MYSQL_STATUS) return true;
        try{
            Connection conn = ds.getConnection();
            if(conn == null){
                connectionStatus.set(false);
                return false;
            }
            connectionStatus.set(conn.isValid(2));
        } catch (SQLException e){
            NodeUtil.sendSQLError("checkMySQLConnection",e.toString());
            connectionStatus.set(false);
        }
        return connectionStatus.get();
    }

    public static boolean isConnected(){
        return connectionStatus.get();
    }

    public static VaultUnit getLargeVaultUnitByAPI(OfflinePlayer player, int type, int num){
        if(player.isOnline()){
            Player target = player.getPlayer();
            return new VaultUnitImpl(player,num,PlayerInv.getInstance().getCacheInventoryManager().get(target).getInventoryContainer(type,num));
        }
        InventoryContainer container = null;
        String uuid = player.getUniqueId().toString();
        SQLBuilder builder = new SQLBuilder("select inv from vault_large where uuid = ? and num = ?");
        ResultSet rs = builder.setString(1, uuid).setInt(2, num).query();
        try {
            if(!rs.isBeforeFirst()){
                return null;
            }
            rs.next();
            container = InventorySerializer.deserializeVaultInventory(null,1,uuid,num,rs.getString("inv"));
            return new VaultUnitImpl(player,num,container);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            builder.close();
        }
    }

    public static VaultUnit getMediumVaultUnitByAPI(OfflinePlayer player,int type,int num){
        if(player.isOnline()){
            Player target = player.getPlayer();
            return new VaultUnitImpl(player,num,PlayerInv.getInstance().getCacheInventoryManager().get(target).getInventoryContainer(type,num));
        }
        InventoryContainer container = null;
        String uuid = player.getUniqueId().toString();
        SQLBuilder builder = new SQLBuilder("select inv from vault_medium where uuid = ? and num = ?");
        ResultSet rs = builder.setString(1, uuid).setInt(2, num).query();
        try {
            if(!rs.isBeforeFirst()){
                return null;
            }
            rs.next();
            container = InventorySerializer.deserializeVaultInventory(null,2,uuid,num,rs.getString("inv"));
            return new VaultUnitImpl(player,num,container);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            builder.close();
        }
    }

    public static VaultUnitMap getLargeVaultUnitMapByAPI(OfflinePlayer player){
        if(player.isOnline()){
            Player target = player.getPlayer();
            return new VaultUnitMapImpl(player,PlayerInv.getInstance().getCacheInventoryManager().get(target).getMedium_map(),true);
        }
        String uuid = player.getUniqueId().toString();
        LinkedHashMap<Integer,InventoryContainer> map = new LinkedHashMap<>();
        SQLBuilder builder = new SQLBuilder("select inv,num from vault_large where uuid = ?")
                .setString(1, uuid);
        ResultSet rs = builder.query();
        try {
            if(!rs.isBeforeFirst()){
                return null;
            }
            while(rs.next()){
                int num = rs.getInt("num");
                InventoryContainer inventory = InventorySerializer.deserializeVaultInventory(null,1,player.getUniqueId().toString(),num,rs.getString("inv"));
                map.put(num,inventory);
            }
            return new VaultUnitMapImpl(player,map);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            builder.close();
        }
    }

    public static VaultUnitMap getMediumVaultUnitMapByAPI(OfflinePlayer player) {
        if(player.isOnline()){
            Player target = player.getPlayer();
            return new VaultUnitMapImpl(player,PlayerInv.getInstance().getCacheInventoryManager().get(target).getMedium_map(),true);
        }
        String uuid = player.getUniqueId().toString();
        LinkedHashMap<Integer,InventoryContainer> map = new LinkedHashMap<>();
        SQLBuilder builder = new SQLBuilder("select inv,num from vault_medium where uuid = ?")
                .setString(1, uuid);
        ResultSet rs = builder.query();
        try {
            if(!rs.isBeforeFirst()){
                return null;
            }
            while(rs.next()){
                int num = rs.getInt("num");
                InventoryContainer inventory = InventorySerializer.deserializeVaultInventory(null,2,player.getUniqueId().toString(),num,rs.getString("inv"));
                map.put(num,inventory);
            }
            return new VaultUnitMapImpl(player,map);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            builder.close();
        }
    }

    public static LinkedHashMap<Integer,VaultStatistics> getVaultStatisticsCache(Player player, String locale, int type){
        String uuid = player.getUniqueId().toString();
        List<RawInventory> rawDataList = new ArrayList<>();
        String sql = "select inv,num from vault_large where uuid = ? order by num asc";
        if(type == 2){
            sql = "select inv,num from vault_medium where uuid = ? order by num asc";
        }
        SQLBuilder builder = new SQLBuilder(sql)
                .setString(1, uuid);
        ResultSet rs = builder.query();
        try {
            if(!rs.isBeforeFirst()){
                return new LinkedHashMap<>();
            }
            while(rs.next()){
                int num = rs.getInt("num");
                String s = rs.getString("inv");
                rawDataList.add(new RawInventory(num,s));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            builder.close();
        }

        if (rawDataList.isEmpty()) {
            return new LinkedHashMap<>();
        }


        List<CompletableFuture<VaultStatistics>> futureList = new ArrayList<>();

        for (RawInventory data : rawDataList) {
            CompletableFuture<VaultStatistics> future = CompletableFuture.supplyAsync(() -> {
                return new VaultStatistics(
                        uuid,
                        InventorySerializer.deserializeVaultInventory(player,type,uuid,data.num,data.invBase64),
                        type,
                        data.num,
                        locale
                        );
            },type == 1 ? LARGE_DECODER_POOL : MEDIUM_DECODER_POOL);

            futureList.add(future);
        }

        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        LinkedHashMap<Integer, VaultStatistics> resultMap = new LinkedHashMap<>();
        for (CompletableFuture<VaultStatistics> future : futureList) {
            if (future != null && future.isDone()) {
                VaultStatistics statistics = future.join();
                resultMap.put(statistics.getVaultNum(), statistics);
                sendDebug("载入玩家 " + player.getName() + " 的仓库[" + statistics.getType() + "//" + statistics.getVaultNum() + "]数据成功 (异步)");
            }
        }
        return resultMap;
    }

    public static void insertVaultAsync(int type, String uuid, Integer num){
        CompletableFuture.runAsync(() -> {
            String sql = null;
            String sqlite = null;
            String inv = null;
            if(type == 1){
                sql = "insert ignore into vault_large (uuid, num, inv) values(?,?,?)";
                sqlite = "insert or ignore into vault_large (uuid, num, inv) values(?,?,?)";
                inv = InventorySerializer.getEmptyInventoryString(1);
            } else {
                sql = "insert ignore into vault_medium (uuid, num, inv) values(?,?,?)";
                sqlite = "insert or ignore into vault_medium (uuid, num, inv) values(?,?,?)";
                inv = InventorySerializer.getEmptyInventoryString(2);
            }
            new SQLBuilder(sql,sqlite)
                    .setString(1,uuid)
                    .setInt(2,num)
                    .setString(3,inv)
                    .update();
        });
    }

    public static void insertVaultNameAsync(String uuid){
        CompletableFuture.runAsync(() -> {
            new SQLBuilder("insert into vault_name (uuid,large,medium) values(?,?,?)")
                    .setString(1,uuid)
                    .setString(2,"{}")
                    .setString(3,"{}")
                    .update();
        });
    }

    public static void insertPickupToggleAsync(String uuid){
        CompletableFuture.runAsync(() -> {
            new SQLBuilder("insert into pickup_toggle (uuid,toggle) values(?,?)")
                    .setString(1,uuid)
                    .setBoolean(2,true)
                    .update();
        });
    }

    public static CompletableFuture<Void> updateVaultStringAsync(int type, String uuid, String inv, int num){
        return CompletableFuture.runAsync(() -> {
            String vault = null;
            if(type == 2){
                vault = "vault_medium";
            } else if(type == 3) {
                vault = "vault_sharing";
            } else {
                vault = "vault_large";
            }
            int i = new SQLBuilder("update " + vault + " set inv = ? where uuid = ? and num = ?")
                    .setString(1, inv)
                    .setString(2, uuid)
                    .setInt(3, num)
                    .update();
            if(i == 1){
                sendDebug("仓库缓存上传成功: " + type + "//" + uuid + ":" + num + " (" + inv.length() + ")");
            } else {
                sendDebug("仓库缓存上传失败?: " + type + "//" + uuid + ":" + num);
            }
        });
    }

    public static CompletableFuture<Void> updateVaultBatch(List<String> list){
        sendConsole("保存仓库缓存中...","Saving vault cache...");
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            Connection con = ds.getConnection();
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            for (String sql : list) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            con.commit();
            stmt.close();
            con.close();
            sendConsole("保存仓库缓存成功","Save vault cache success");
            future.complete(null);
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("updateVaultBatch",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("updateVaultBatch",e.toString());
        }
        return future;
    }

    public static void updatePickupToggleAsync(String uuid, Boolean toggle){
        CompletableFuture.runAsync(() -> {
            new SQLBuilder("update pickup_toggle set toggle = ? where uuid = ?")
                    .setBoolean(1,toggle)
                    .setString(2,uuid)
                    .update();
            PlayerInv.getInstance().getPickupManager().putToggle(uuid,toggle);
        });
    }

    public static void updateVaultAttributeAsync(int type, String uuid, String attribute){
        CompletableFuture.runAsync(() -> {
            String sql = "update vault_name set large = ? where uuid = ?";
            if(type == 2){
                sql = "update vault_name set medium = ? where uuid = ?";
            }
            new SQLBuilder(sql)
                    .setString(1,attribute)
                    .setString(2,uuid)
                    .update();
        });
    }

    public static String getVaultString(int type, String uuid, int num){
        String sql = "select inv from vault_large where uuid = ? and num = ?";
        if(type == 2){
            sql = "select inv from vault_medium where uuid = ? and num = ?";
        }
        SQLBuilder builder = new SQLBuilder(sql)
                .setString(1, uuid)
                .setInt(2,num);
        ResultSet rs = builder.query();
        try {
            if(!rs.isBeforeFirst()){
                return null;
            } else {
                rs.next();
                return rs.getString("inv");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            builder.close();
        }
    }

    public static String[] getVaultName(String uuid){
        String[] strings = new String[2];
        SQLBuilder builder = new SQLBuilder("select large,medium from vault_name where uuid = ?")
                .setString(1, uuid);
        ResultSet rs = builder.query();
        try {
            if(!rs.isBeforeFirst()){
                JDBCUtil.insertVaultNameAsync(uuid);
                strings[0] = "{}";
                strings[1] = "{}";
                return strings;
            }
            rs.next();
            strings[0] = rs.getString("large");
            strings[1] = rs.getString("medium");
            return strings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            builder.close();
        }
    }

    public static Boolean getPickupToggle(String uuid){
        SQLBuilder sqlBuilder = new SQLBuilder("select toggle from pickup_toggle where uuid = ?")
                .setString(1, uuid);
        ResultSet rs = sqlBuilder.query();
        try {
            if(!rs.isBeforeFirst()){
                JDBCUtil.insertPickupToggleAsync(uuid);
                return true;
            }
            rs.next();
            return rs.getBoolean("toggle");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            sqlBuilder.close();
        }
    }

    public static void createLargeTable(){
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS vault_large ("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "num INT NOT NULL,"
                    + "inv LONGTEXT,"
                    + "UNIQUE KEY uk_uuid_num (uuid, num)"
                    + ");";
            String sqlite = "CREATE TABLE IF NOT EXISTS vault_large ("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "num INT NOT NULL,"
                    + "inv LONGTEXT,"
                    + "UNIQUE(uuid, num)"
                    + ")";
            new SQLBuilder(sql,sqlite).update();
        });
    }

    public static void createMediumTable(){
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS vault_medium ("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "num INT NOT NULL,"
                    + "inv LONGTEXT,"
                    + "UNIQUE KEY uk_uuid_num (uuid, num)"
                    + ");";
            String sqlite = "CREATE TABLE IF NOT EXISTS vault_medium ("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "num INT NOT NULL,"
                    + "inv LONGTEXT,"
                    + "UNIQUE(uuid, num)"
                    + ")";
            new SQLBuilder(sql,sqlite).update();
        });
    }

    public static void createPickupToggleTable(){
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS pickup_toggle"
                    + "("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "toggle BOOLEAN,"
                    + "PRIMARY KEY (uuid)"
                    + ");";
            new SQLBuilder(sql).update();
        });
    }

    public static void createVaultNameTable(){
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS vault_name"
                    + "("
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "large TEXT,"
                    + "medium TEXT,"
                    + "PRIMARY KEY (uuid)"
                    + ");";
            new SQLBuilder(sql).update();
        });
    }
}
