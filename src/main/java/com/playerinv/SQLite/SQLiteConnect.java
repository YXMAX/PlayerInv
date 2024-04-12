package com.playerinv.SQLite;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import java.sql.*;

import static com.playerinv.PlayerInv.plugin;


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

    public static Connection con = judgeconnect();

    public static Connection judgeconnect(){
        if(mysql){
            return plugin.getMySQLConnection();
        }
        try {
            return getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() throws Exception {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        SQLiteDataSource ds = new SQLiteDataSource(config);
        String url = System.getProperty("user.dir");
        ds.setUrl("jdbc:sqlite:"+url+"/plugins/PlayerInv/"+"Database.db");
        return ds.getConnection();
    }

    public static void createTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS InvData (\n"
                + "	uuid string,\n"
                + "	num integer,\n"
                + "	Inv string\n"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void dropTable(Connection con)throws Exception {
        String sql = "drop table InvData ";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static void insert(Connection con, String uuid, Integer num, String Inv)throws Exception {
        String sql = "insert into InvData (uuid, num, Inv) values(?,?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        pst.setString(3, Inv);
        pst.executeUpdate();

    }

    public static void update(Connection con, String uuid, String Inv, Integer num)throws Exception {
        String sql = "update InvData set Inv = ? where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, Inv);
        pst.setString(2, uuid);
        pst.setInt(3, num);
        pst.executeUpdate();
    }

    public static String InvCode(Connection con, String uuid, Integer num)throws Exception {
        String invcode = null;
        String sql = "select Inv from InvData where uuid = ? and num = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        pst.setString(1, uuid);
        pst.setInt(2, num);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            String invbase = rs.getString("Inv");
            invcode = invbase;
        }
        rs.close();
        pst.close();
        return invcode;
    }

    public static boolean hasData(Connection con, String uuid)throws Exception {
        String hasData = null;
        String sql = "select uuid from InvData where uuid = ? and num = 1";
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

    public static void MysqlcreateTable(Connection con)throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS InvData"
                + "("
                + "uuid VARCHAR(255),"
                + "num INT,"
                + "Inv TEXT"
                + ");";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    public static boolean mysqlhasData(Connection con, String uuid)throws Exception {
        String sql = "SELECT 1 FROM InvData WHERE uuid='"+uuid+"'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
            return true;
        } else {
            return false;
        }
    }
}
