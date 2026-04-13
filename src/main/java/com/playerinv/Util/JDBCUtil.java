package com.playerinv.Util;

import com.playerinv.Util.Object.Cache.InventoryContainer;
import com.playerinv.Util.Object.Temp.RawInventory;
import com.playerinv.Util.Object.Temp.TempLarge;
import com.playerinv.Util.Object.Temp.TempMedium;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.isMySQL;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.NodeUtil.*;


public class JDBCUtil {
    
    public static HikariDataSource ds;

    public static AtomicBoolean connectionStatus = new AtomicBoolean(true);

    private static final ExecutorService LARGE_DECODER_POOL = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
    private static final ExecutorService MEDIUM_DECODER_POOL = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));

    public static void closeMySQLInteract(){
        if(!isMySQL) return;
        if(!connectionStatus.get()) return;
        connectionStatus.set(false);
        scheduler.scheduling().asyncScheduler().runAtFixedRate(task -> {
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
        if(!isMySQL) return true;
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

    public void fixMySQLDataType_Large(){
        try {
            Connection con = ds.getConnection();
            String sql = "select * from vault_large limit 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            while(rs.next()){
                String columnType = data.getColumnTypeName(3);
                if(columnType.equalsIgnoreCase("text")){
                    String alter = "alter table vault_large modify column inv longtext";
                    PreparedStatement pst_alter = con.prepareStatement(alter);
                    pst_alter.executeUpdate();
                    pst_alter.close();
                    rs.close();
                    pst.close();
                    con.close();
                    return;
                }
            }
            rs.close();
            pst.close();
            con.close();
            return;
        } catch (SQLException e) {
            NodeUtil.sendSQLError("fixMySQLDataType_Large",e.toString());
        }
    }
    public void fixMySQLDataType_Medium(){
        try {
            Connection con = ds.getConnection();
            String sql = "select * from vault_medium limit 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            while(rs.next()){
                String columnType = data.getColumnTypeName(3);
                if(columnType.equalsIgnoreCase("text")){
                    String alter = "alter table vault_medium modify column inv longtext";
                    PreparedStatement pst_alter = con.prepareStatement(alter);
                    pst_alter.executeUpdate();
                    pst_alter.close();
                    rs.close();
                    pst.close();
                    con.close();
                    return;
                }
            }
            rs.close();
            pst.close();
            con.close();
            return;
        } catch (SQLException e) {
            NodeUtil.sendSQLError("fixMySQLDataType_Medium",e.toString());
        }
    }


    public LinkedHashMap<Integer,InventoryContainer> getFullLargeInventory(Player player){
        String uuid = player.getUniqueId().toString();
        try {
            Connection con = ds.getConnection();
            LinkedHashMap<Integer,InventoryContainer> map = new LinkedHashMap<>();
            String sql = "select inv,num from vault_large where uuid = ?";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return map;
            }
            while(rs.next()){
                int num = rs.getInt("num");
                InventoryContainer inventory = NodeUtil.inventoryFromBase64_LargeCache(rs.getString("inv"),num,player.getName());
                map.put(num,inventory);
            }
            rs.close();
            pst.close();
            con.close();
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public LinkedHashMap<Integer,InventoryContainer> getFullMediumInventory(Player player){
        String uuid = player.getUniqueId().toString();
        try {
            Connection con = ds.getConnection();
            LinkedHashMap<Integer,InventoryContainer> map = new LinkedHashMap<>();
            String sql = "select inv,num from vault_medium where uuid = ?";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return map;
            }
            while(rs.next()){
                int num = rs.getInt("num");
                InventoryContainer inventory = NodeUtil.inventoryFromBase64_MediumCache(rs.getString("inv"),num,player.getName());
                map.put(num,inventory);
            }
            rs.close();
            pst.close();
            con.close();
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public LinkedHashMap<Integer,InventoryContainer> getFullLargeInventoryAsync(Player player){
        String uuid = player.getUniqueId().toString();
        List<RawInventory> rawDataList = new ArrayList<>();
        try {
            Connection con = ds.getConnection();
            String sql = "select inv,num from vault_large where uuid = ? order by num asc";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return new LinkedHashMap<>();
            }
            while(rs.next()){
                int num = rs.getInt("num");
                String s = rs.getString("inv");
                rawDataList.add(new RawInventory(num,s));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getFullLargeInventoryAsync",e.toString());
            JDBCUtil.closeMySQLInteract();
            return null;
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getFullLargeInventoryAsync",e.toString());
            return null;
        }

        if (rawDataList.isEmpty()) {
            return new LinkedHashMap<>();
        }

        HashMap<Integer, CompletableFuture<InventoryContainer>> futureMap = new HashMap<>();

        for (RawInventory data : rawDataList) {
            CompletableFuture<InventoryContainer> future = CompletableFuture.supplyAsync(() -> {
                return NodeUtil.inventoryFromBase64_LargeCache(data.invBase64, data.num, player.getName());
            },LARGE_DECODER_POOL);

            futureMap.put(data.num, future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futureMap.values().toArray(new CompletableFuture[0])
        );

        try {
            allFutures.join();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        LinkedHashMap<Integer, InventoryContainer> resultMap = new LinkedHashMap<>();

        for (RawInventory data : rawDataList) {
            CompletableFuture<InventoryContainer> future = futureMap.get(data.num);
            if (future != null && future.isDone()) {
                InventoryContainer container = future.join();
                resultMap.put(data.num, container);
                sendLog("Successfully get" + player.getName() + "'s LargeVault[" + data.num + "]");
            }
        }
        return resultMap;
    }
    public LinkedHashMap<Integer,InventoryContainer> getFullMediumInventoryAsync(Player player){
        String uuid = player.getUniqueId().toString();
        List<RawInventory> rawDataList = new ArrayList<>();
        try {
            Connection con = ds.getConnection();
            String sql = "select inv,num from vault_medium where uuid = ? order by num asc";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return new LinkedHashMap<>();
            }
            while(rs.next()){
                int num = rs.getInt("num");
                String s = rs.getString("inv");
//                InventoryContainer inventory = NodeUtil.inventoryFromBase64_LargeCache(rs.getString("inv"),num,player.getName());
//                map.put(num,inventory);
//                sendLog("载入玩家 " + player.getName() + " 的大型仓库[" + num + "]数据成功");
                rawDataList.add(new RawInventory(num,s));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getFullMediumInventoryAsync",e.toString());
            JDBCUtil.closeMySQLInteract();
            return null;
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getFullMediumInventoryAsync",e.toString());
            return null;
        }

        if (rawDataList.isEmpty()) {
            return new LinkedHashMap<>();
        }

        HashMap<Integer, CompletableFuture<InventoryContainer>> futureMap = new HashMap<>();

        for (RawInventory data : rawDataList) {
            CompletableFuture<InventoryContainer> future = CompletableFuture.supplyAsync(() -> {
                return NodeUtil.inventoryFromBase64_MediumCache(data.invBase64, data.num, player.getName());
            },MEDIUM_DECODER_POOL);

            futureMap.put(data.num, future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futureMap.values().toArray(new CompletableFuture[0])
        );

        try {
            allFutures.join();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        LinkedHashMap<Integer, InventoryContainer> resultMap = new LinkedHashMap<>();

        for (RawInventory data : rawDataList) {
            CompletableFuture<InventoryContainer> future = futureMap.get(data.num);
            if (future != null && future.isDone()) {
                InventoryContainer container = future.join();
                resultMap.put(data.num, container);
                sendLog("Successfully get" + player.getName() + "'s MediumVault[" + data.num + "]");
            }
        }
        return resultMap;
    }

    public void insertVaultAsync(int type, String uuid, Integer num){
        CompletableFuture.runAsync(() -> {
            try {
                Connection con = ds.getConnection();
                PreparedStatement pst = null;
                String sql = null;
                String inv = null;
                switch(type){
                    case 1:
                        sql = "insert or ignore into vault_large (uuid, num, inv) values(?,?,?)";
                        if(isMySQL){
                            sql = "insert ignore into vault_large (uuid, num, inv) values(?,?,?)";
                        }
                        inv = "rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==";
                        break;
                    case 2:
                        sql = "insert or ignore into vault_medium (uuid, num, inv) values(?,?,?)";
                        if(isMySQL){
                            sql = "insert ignore into vault_medium (uuid, num, inv) values(?,?,?)";
                        }
                        inv = "rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==";
                        break;
                }
                pst = con.prepareStatement(sql);
                pst.setString(1, uuid);
                pst.setInt(2, num);
                pst.setString(3, inv);
                pst.executeUpdate();
                pst.close();
                con.close();
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("insertVaultAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("insertVaultAsync",e.toString());
            }
        });
    }

    public void insertVaultNameAsync(String uuid){
        CompletableFuture.runAsync(() -> {
            try {
                String sql = "insert into vault_name (uuid,large,medium) values(?,?,?)";
                Connection con = ds.getConnection();
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setString(1, uuid);
                pst.setString(2, "{}");
                pst.setString(3, "{}");
                pst.executeUpdate();
                pst.close();
                con.close();
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("insertVaultNameAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("insertVaultNameAsync",e.toString());
            }
        });
    }

    public void insertPickupToggleAsync(String uuid){
        CompletableFuture.runAsync(() -> {
            try {
                String sql = "insert into pickup_toggle (uuid,toggle) values(?,?)";
                Connection con = ds.getConnection();
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setString(1, uuid);
                pst.setBoolean(2, true);
                pst.executeUpdate();
                pst.close();
                con.close();
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("insertPickupToggleAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("insertPickupToggleAsync",e.toString());
            }
        });
    }

    public void updateVaultAsync(int type, String uuid, String Inv, int num){
        CompletableFuture.runAsync(() -> {
            try {
                Connection con = ds.getConnection();
                String vault_type = null;
                switch(type){
                    case 1:
                        vault_type = "vault_large";
                        break;
                    case 2:
                        vault_type = "vault_medium";
                        break;
                }
                String sql = "update " + vault_type + " set inv = ? where uuid = ? and num = ?";
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setString(1, Inv);
                pst.setString(2, uuid);
                pst.setInt(3, num);
                pst.executeUpdate();
                pst.close();
                con.close();
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("updateVaultAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("updateVaultAsync",e.toString());
            }
        }).thenRun(() -> {
           operationManager.sendCheckResult(type,uuid,num);
        });
    }

    public void updateVaultBatch(List<String> list){
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
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("updateVaultBatch",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("updateVaultBatch",e.toString());
        }
    }

    public void updateVaultByCache(int type, String uuid, Inventory inventory, int num){
        CompletableFuture.runAsync(() -> {
            String inv_string = inventoryToBase64(inventory);
            try {
                Connection con = ds.getConnection();
                String vault_type = null;
                switch(type){
                    case 1:
                        vault_type = "vault_large";
                        break;
                    case 2:
                        vault_type = "vault_medium";
                        break;
                }
                String sql = "update " + vault_type + " set inv = ? where uuid = ? and num = ?";
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setString(1, inv_string);
                pst.setString(2, uuid);
                pst.setInt(3, num);
                pst.executeUpdate();
                pst.close();
                con.close();
                sendLog("VaultCache upload success: " + type + "//" + uuid + ":" + num);
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("updateVaultByCache",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("updateVaultByCache",e.toString());
            }
        });
    }

    public void updatePickupToggleAsync(String uuid, Boolean toggle){
        CompletableFuture.runAsync(() -> {
            try {
                Connection con = ds.getConnection();
                String sql = "update pickup_toggle set toggle = ? where uuid = ?";
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setBoolean(1, toggle);
                pst.setString(2, uuid);
                pst.executeUpdate();
                pst.close();
                con.close();
                pickupManager.putToggle(uuid,toggle);
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("updatePickupToggleAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("updatePickupToggleAsync",e.toString());
            }
        });
    }

    public void updateVaultAttributeAsync(int type, String uuid, String attribute){
        CompletableFuture.runAsync(() -> {
            try {
                Connection con = ds.getConnection();
                String sql = "update vault_name set large = ? where uuid = ?";
                if(type == 2){
                    sql = "update vault_name set medium = ? where uuid = ?";
                }
                PreparedStatement pst = null;
                pst = con.prepareStatement(sql);
                pst.setString(1, attribute);
                pst.setString(2, uuid);
                pst.executeUpdate();
                pst.close();
                con.close();
            } catch (SQLTransientConnectionException e) {
                NodeUtil.sendSQLConnectionError("updateVaultAttributeAsync",e.toString());
                JDBCUtil.closeMySQLInteract();
            } catch (SQLException e) {
                NodeUtil.sendSQLError("updateVaultAttributeAsync",e.toString());
            }
        });
    }

    public Inventory getVault(int type, OfflinePlayer player, Integer num){
        String uuid = player.getUniqueId().toString();
        int size = 54;
        if(type == 2){
            size = 27;
        }
        try {
            Inventory inventory = null;
            Connection con = ds.getConnection();
            String sql = "select inv from vault_large where uuid = ? and num = ?";
            if(type == 2){
                sql = "select inv from vault_medium where uuid = ? and num = ?";
            }
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            pst.setInt(2, num);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                inventory = Bukkit.createInventory(null, size, "INV");
                rs.close();
                pst.close();
                con.close();
                return inventory;
            } else {
                rs.next();
                String inv = rs.getString("inv");
                if(inv == null){
                    inventory = Bukkit.createInventory(null, size, "INV");
                    rs.close();
                    pst.close();
                    con.close();
                    return inventory;
                }
                inventory = inventoryFromBase64_Basic(inv);
                rs.close();
                pst.close();
                con.close();
                return inventory;
            }
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getVault",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getVault",e.toString());
        }
        return null;
    }

    public String getVaultString(int type, String uuid, Integer num){
        try {
            Connection con = ds.getConnection();
            String sql = "select inv from vault_large where uuid = ? and num = ?";
            if(type == 2){
                sql = "select inv from vault_medium where uuid = ? and num = ?";
            }
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            pst.setInt(2, num);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return null;
            } else {
                rs.next();
                String inv = rs.getString("inv");
                rs.close();
                pst.close();
                con.close();
                return inv;
            }
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getVaultSharing",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getVaultSharing",e.toString());
        }
        return null;
    }

    public String[] getVaultName(String uuid){
        String[] strings = new String[2];
        try {
            Connection con = ds.getConnection();
            String sql = "select large,medium from vault_name where uuid = ?";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                this.insertVaultNameAsync(uuid);
                strings[0] = "{}";
                strings[1] = "{}";
                rs.close();
                pst.close();
                con.close();
                return strings;
            } else {
                rs.next();
                strings[0] = rs.getString("large");
                strings[1] = rs.getString("medium");
                rs.close();
                pst.close();
                con.close();
                return strings;
            }
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getVaultName",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getVaultName",e.toString());
        }
        return null;
    }

    public HashSet<Integer> getCheckVaults(String uuid, int type){
        String sql;
        if(type == 1){
            sql = "select num from vault_large where uuid = ?";
        } else {
            sql = "select num from vault_medium where uuid = ?";
        }
        Connection con = null;
        try {
            con = ds.getConnection();
            HashSet<Integer> set = new HashSet<>();
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                rs.close();
                pst.close();
                con.close();
                return set;
            }
            while(rs.next()){
                set.add(rs.getInt("num"));
            }
            rs.close();
            pst.close();
            con.close();
            return set;
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getCheckVaults",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getCheckVaults",e.toString());
        }
        return null;
    }

    public Boolean getPickupToggle(String uuid){
        try {
            Connection con = ds.getConnection();
            boolean toggle = false;
            String sql = "select toggle from pickup_toggle where uuid = ?";
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                this.insertPickupToggleAsync(uuid);
                rs.close();
                pst.close();
                con.close();
                return true;
            }
            rs.next();
            toggle = rs.getBoolean("toggle");
            rs.close();
            pst.close();
            con.close();
            return toggle;
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError("getPickupToggle",e.toString());
            JDBCUtil.closeMySQLInteract();
        } catch (SQLException e) {
            NodeUtil.sendSQLError("getPickupToggle",e.toString());
        }
        return false;
    }


    public void createLargeTable(){
        try {
            Connection con = ds.getConnection();
            String sql = "CREATE TABLE IF NOT EXISTS vault_large"
                    + "("
                    + "uuid VARCHAR(36),"
                    + "num INT,"
                    + "inv LONGTEXT"
                    + ");";
            Statement stat = null;
            stat = con.createStatement();
            stat.executeUpdate(sql);
            stat.close();
            con.close();
        } catch (SQLException e) {
            NodeUtil.sendError(e.toString());
        }
    }

    public void createMediumTable(){
        try {
            Connection con = ds.getConnection();
            String sql = "CREATE TABLE IF NOT EXISTS vault_medium"
                    + "("
                    + "uuid VARCHAR(36),"
                    + "num INT,"
                    + "inv LONGTEXT"
                    + ");";
            Statement stat = null;
            stat = con.createStatement();
            stat.executeUpdate(sql);
            stat.close();
            con.close();
        } catch (SQLException e) {
            NodeUtil.sendError(e.toString());
        }
    }

    public void createPickupToggleTable(){
        try {
            Connection con = ds.getConnection();
            String sql = "CREATE TABLE IF NOT EXISTS pickup_toggle"
                    + "("
                    + "uuid VARCHAR(36),"
                    + "toggle BOOLEAN"
                    + ");";
            Statement stat = null;
            stat = con.createStatement();
            stat.executeUpdate(sql);
            stat.close();
            con.close();
        } catch (SQLException e) {
            NodeUtil.sendError(e.toString());
        }
    }

    public void createVaultNameTable(){
        try {
            Connection con = ds.getConnection();
            String sql = "CREATE TABLE IF NOT EXISTS vault_name"
                    + "("
                    + "uuid VARCHAR(36) PRIMARY KEY,"
                    + "large TEXT,"
                    + "medium TEXT"
                    + ");";
            Statement stat = null;
            stat = con.createStatement();
            stat.executeUpdate(sql);
            stat.close();
            con.close();
        } catch (SQLException e) {
            NodeUtil.sendError(e.toString());
        }
    }

    public void createIndex(){
        for(int i=1;i<=3;i++){
            String table = null;
            String index = null;
            switch(i){
                case 1:
                    index = "large_index";
                    table = "vault_large";
                    break;
                case 2:
                    index = "medium_index";
                    table = "vault_medium";
                    break;
                case 3:
                    index = "name_index";
                    table = "vault_name";
                    break;
            }
            try {
                Connection con = ds.getConnection();
                String sql = "CREATE UNIQUE INDEX " + index +  " ON " + table + " (uuid,num)";
                Statement statement = con.createStatement();
                statement.executeUpdate(sql);
                statement.close();
                con.close();
            } catch (SQLException e) {
                return;
            }
        }
    }

    public void createUniqueIndex(){
        try {
            Connection con = ds.getConnection();
            String sql = "CREATE UNIQUE INDEX pickup_key ON pickup_toggle (uuid)";
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            con.close();
        } catch (SQLException e) {
            return;
        }
    }
}
