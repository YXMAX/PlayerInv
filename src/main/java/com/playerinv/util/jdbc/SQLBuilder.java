package com.playerinv.util.jdbc;

import com.playerinv.util.NodeUtil;

import java.sql.*;

public final class SQLBuilder {

    private String genericSQL;

    private String sqliteSQL;

    private Connection con;

    private PreparedStatement pst;

    private ResultSet rs;

    public SQLBuilder(String genericSQL){
        this.genericSQL = genericSQL;
        try {
            this.con = JDBCUtil.getConnection();
            this.pst = con.prepareStatement(this.genericSQL);
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError(e.toString());
            JDBCUtil.closeMySQLInteract();
            throw new RuntimeException(e);
        } catch (SQLException e2) {
            throw new RuntimeException(e2);
        }
    }

    public SQLBuilder(String genericSQL, String sqliteSQL){
        this.genericSQL = genericSQL;
        this.sqliteSQL = sqliteSQL;
        try {
            this.con = JDBCUtil.getConnection();
            if(JDBCUtil.MYSQL_STATUS){
                this.pst = con.prepareStatement(this.genericSQL);
            } else {
                this.pst = con.prepareStatement(this.sqliteSQL);
            }
        } catch (SQLTransientConnectionException e) {
            NodeUtil.sendSQLConnectionError(e.toString());
            JDBCUtil.closeMySQLInteract();
            throw new RuntimeException(e);
        } catch (SQLException e2) {
            throw new RuntimeException(e2);
        }
    }

    public SQLBuilder setString(int index,String s){
        try {
            this.pst.setString(index, s);
        } catch (SQLException e) {
            throw new RuntimeException("绑定 String 参数失败 [index=" + index + "]: " + e.getMessage(), e);
        }
        return this;
    }

    public SQLBuilder setInt(int index,int i){
        try {
            this.pst.setInt(index, i);
        } catch (SQLException e) {
            throw new RuntimeException("绑定 Int 参数失败 [index=" + index + "]: " + e.getMessage(), e);
        }
        return this;
    }

    public SQLBuilder setBoolean(int index,boolean b){
        try {
            this.pst.setBoolean(index, b);
        } catch (SQLException e) {
            throw new RuntimeException("绑定 Boolean 参数失败 [index=" + index + "]: " + e.getMessage(), e);
        }
        return this;
    }

    public ResultSet query(){
        try {
            this.rs = this.pst.executeQuery();
            return this.rs;
        } catch (SQLException e) {
            NodeUtil.sendError("执行查询失败: " + e.getMessage());
            return null;
        }
    }

    public int update(){
        try {
            return this.pst.executeUpdate();
        } catch (SQLException e) {
            NodeUtil.sendError("执行更新失败: " + e.toString());
        } finally {
            this.close();
        }
        return 0;
    }

    public void close(){
        try {
            if (this.rs != null && !this.rs.isClosed()) this.rs.close();
            if (this.pst != null && !this.pst.isClosed()) this.pst.close();
            if (this.con != null && !this.con.isClosed()) this.con.close();
        } catch (SQLException e) {
            NodeUtil.sendError("关闭 SQL 资源失败: " + e.toString());
        }
    }
}
