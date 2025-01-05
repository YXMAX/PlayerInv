package com.playerinv.SQLite;

import com.playerinv.TempHolder.TempPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;


public class SQLiteConnect {

    public static String allowPublicKeyRetrieval(){
        Boolean publickey = plugin.getConfig().getBoolean("DataBases.allowPublicKeyRetrieval");
        if(publickey && mysql){
            String key = "allowPublicKeyRetrieval=true";
            return key;
        }
        return null;
    }

    public static Boolean mysql = plugin.getConfig().getBoolean("DataBases.MySQL");

    public static Connection con;

    public static void FixMySQL_DataType_Large() throws SQLException {
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
                 return;
             }
         }
         rs.close();
         pst.close();
         return;
    }

    public static void FixMySQL_DataType_Medium() throws SQLException {
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
                return;
            }
        }
        rs.close();
        pst.close();
        return;
    }


    public static Connection getConnection() throws Exception {
        sendLog("创建HikariCP 数据池");
        HikariConfig config = new HikariConfig();
        String url = System.getProperty("user.dir");
        config.setJdbcUrl("jdbc:sqlite:"+url+"/plugins/PlayerInv/"+"Database.db");
        config.setDriverClassName("org.sqlite.JDBC");
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }

    public static void createLargeTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS vault_large (\n"
                + "	uuid string,\n"
                + "	num integer,\n"
                + "	inv string\n"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void createMediumTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS vault_medium (\n"
                + "	uuid string,\n"
                + "	num integer,\n"
                + "	inv string\n"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void createToggleTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS keys_toggle (\n"
                + "	uuid string,\n"
                + "	toggle boolean\n"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void createReturnToggleTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS return_toggle (\n"
                + "	uuid string,\n"
                + "	toggle boolean\n"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void dropTable(Connection con)throws Exception {
        String sql = "alter table InvData rename to InvData_backup";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void FixTable_InsertNew(Connection con){
        String sql = "select * from InvData";
        Statement stat = null;
        try {
            stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            sendConvertDataMessage();
            Insert_TempList_Large = new ArrayList<>();
            Insert_TempList_Medium = new ArrayList<>();
            while (rs.next()){
                if(rs.getInt("num") <= 10){
                    TempPlayer tempPlayer = new TempPlayer(rs.getString("uuid"),rs.getInt("num"),rs.getString("inv"));
                    Insert_TempList_Large.add(tempPlayer);
                } else {
                    int num = rs.getInt("num");
                    int new_num = num - 10;
                    TempPlayer tempPlayer = new TempPlayer(rs.getString("uuid"),new_num,rs.getString("inv"));
                    Insert_TempList_Medium.add(tempPlayer);
                }
            }
            insert_LargeBatch(con);
            insert_MediumBatch(con);
            rs.close();
            dropTable(con);
            con.setAutoCommit(true);
            sendConvertSuccessMessage();
        } catch (SQLException e) {
            return;
        } catch (Exception e) {
            return;
        }
    }

    public static void insert_LargeBatch(Connection con)throws Exception {
        String sql = "insert into vault_large (uuid, num, inv) values(?,?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        con.setAutoCommit(false);
        for(TempPlayer player : Insert_TempList_Large){
            pst.setString(1, player.getUuid());
            pst.setInt(2, player.getNum());
            pst.setString(3, player.getInv());
            pst.addBatch();
        }
        pst.executeBatch();
        con.commit();
        pst.close();
    }

    public static void insert_MediumBatch(Connection con)throws Exception {
        String sql = "insert into vault_medium (uuid, num, inv) values(?,?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        for(TempPlayer player : Insert_TempList_Medium){
            pst.setString(1, player.getUuid());
            pst.setInt(2, player.getNum());
            pst.setString(3, player.getInv());
            pst.addBatch();
        }
        pst.executeBatch();
        con.commit();
        pst.close();
    }

    public static void insert_Large(Connection con, String uuid, Integer num, String Inv)throws Exception {
        String sql = "insert into vault_large (uuid, num, inv) values(?,?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        pst.setString(3, Inv);
        pst.executeUpdate();

    }

    public static void insert_Medium(Connection con, String uuid, Integer num, String Inv)throws Exception {
        String sql = "insert into vault_medium (uuid, num, inv) values(?,?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        pst.setString(3, Inv);
        pst.executeUpdate();

    }

    public static void insertToggle(Connection con, String uuid, Boolean toggle)throws Exception {
        String sql = "insert into keys_toggle (uuid,toggle) values(?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setBoolean(2, toggle);
        pst.executeUpdate();

    }

    public static void insertReturnToggle(Connection con, String uuid, Boolean toggle)throws Exception {
        String sql = "insert into return_toggle (uuid,toggle) values(?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setBoolean(2, toggle);
        pst.executeUpdate();

    }

    public static void updateInv_Large(Connection con, String uuid, String Inv, Integer num)throws Exception {
        String sql = "update vault_large set inv = ? where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, Inv);
        pst.setString(2, uuid);
        pst.setInt(3, num);
        int bool = pst.executeUpdate();
    }

    public static void updateInv_Medium(Connection con, String uuid, String Inv, Integer num)throws Exception {
        String sql = "update vault_medium set inv = ? where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, Inv);
        pst.setString(2, uuid);
        pst.setInt(3, num);
        int bool = pst.executeUpdate();
    }

    public static void updateToggle(Connection con, String uuid, Boolean toggle)throws Exception {
        String sql = "update keys_toggle set toggle = ? where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setBoolean(1, toggle);
        pst.setString(2, uuid);
        pst.executeUpdate();
    }

    public static void updateReturnToggle(Connection con, String uuid, Boolean toggle)throws Exception {
        String sql = "update return_toggle set toggle = ? where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setBoolean(1, toggle);
        pst.setString(2, uuid);
        pst.executeUpdate();
    }

    public static String InvCode_Large(Connection con, String uuid, Integer num)throws Exception {
        String invcode = null;
        String sql = "select inv from vault_large where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String invbase = rs.getString("inv");
            invcode = invbase;
        }
        rs.close();
        pst.close();
        return invcode;
    }

    public static String InvCode_Medium(Connection con, String uuid, Integer num)throws Exception {
        String invcode = null;
        String sql = "select inv from vault_medium where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String invbase = rs.getString("inv");
            invcode = invbase;
        }
        rs.close();
        pst.close();
        return invcode;
    }

    public static Boolean getToggle(Connection con, String uuid)throws Exception {
        Boolean toggle = false;
        String sql = "select toggle from keys_toggle where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            Boolean in = rs.getBoolean("toggle");
            toggle = in;
        }
        rs.close();
        pst.close();
        return toggle;
    }

    public static Boolean getReturnToggle(Connection con, String uuid)throws Exception {
        Boolean toggle = false;
        String sql = "select toggle from return_toggle where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            Boolean in = rs.getBoolean("toggle");
            toggle = in;
        }
        rs.close();
        pst.close();
        return toggle;
    }

    public static boolean hasData_Large(Connection con, String uuid, Integer num)throws Exception {
        String sql = "select uuid, inv from vault_large where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String Data = rs.getString("uuid");
           if(uuid.equals(Data)){
               String inv = rs.getString("inv");
               if(inv != null){
                   rs.close();
                   pst.close();
                   return true;
               } else {
                   String new_inv = "rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==";
                   String sql_update = "update vault_large set inv = ? where uuid = ? and num = ?";
                   PreparedStatement pst_update = null;
                   pst_update = con.prepareStatement(sql_update);
                   pst_update.setString(1, new_inv);
                   pst_update.setString(2, uuid);
                   pst_update.setInt(3, num);
                   pst_update.executeUpdate();
                   pst_update.close();
                   return true;
               }
           }
        }
        return false;
    }

    public static boolean hasData_Medium(Connection con, String uuid, Integer num)throws Exception {
        String sql = "select uuid, inv from vault_medium where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String Data = rs.getString("uuid");
            if(uuid.equals(Data)){
                String inv = rs.getString("inv");
                if(inv != null){
                    rs.close();
                    pst.close();
                    return true;
                } else {
                    String new_inv = "rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==";
                    String sql_update = "update vault_medium set inv = ? where uuid = ? and num = ?";
                    PreparedStatement pst_update = null;
                    pst_update = con.prepareStatement(sql_update);
                    pst_update.setString(1, new_inv);
                    pst_update.setString(2, uuid);
                    pst_update.setInt(3, num);
                    pst_update.executeUpdate();
                    pst_update.close();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasDataToggle(Connection con, String uuid)throws Exception {
        String hasData = null;
        String sql = "select uuid from keys_toggle where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String Data = rs.getString("uuid");
            hasData = Data;
        }
        if(uuid.equals(hasData)){
            rs.close();
            pst.close();
            return true;
        } else {
            rs.close();
            pst.close();
            return false;
        }
    }

    public static boolean hasDataReturnToggle(Connection con, String uuid)throws Exception {
        String hasData = null;
        String sql = "select uuid from return_toggle where uuid = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String Data = rs.getString("uuid");
            hasData = Data;
        }
        if(uuid.equals(hasData)){
            rs.close();
            pst.close();
            return true;
        } else {
            rs.close();
            pst.close();
            return false;
        }
    }

    public static void MysqlcreateLargeTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS vault_large"
                + "("
                + "uuid VARCHAR(255),"
                + "num INT,"
                + "inv LONGTEXT"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void MysqlcreateMediumTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS vault_medium"
                + "("
                + "uuid VARCHAR(255),"
                + "num INT,"
                + "inv LONGTEXT"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void MysqlcreateToggleTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS keys_toggle"
                + "("
                + "uuid VARCHAR(255),"
                + "toggle BOOLEAN"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void MysqlcreateReturnToggleTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS return_toggle"
                + "("
                + "uuid VARCHAR(255),"
                + "toggle BOOLEAN"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }
}
