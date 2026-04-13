package com.playerinv.Util.Scheduler;

import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Backup.TotalBackup;
import com.playerinv.Util.Object.Backup.VaultBackup;
import de.tr7zw.changeme.nbtapi.utils.DataFixerUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.playerinv.PlayerInv.plugin;
import static com.playerinv.Util.JDBCUtil.ds;
import static com.playerinv.Util.NodeUtil.sendConsole;

public class BackupScheduler {

    private static ScheduledExecutorService main = Executors.newScheduledThreadPool(1);

    private static ScheduledExecutorService threads = Executors.newScheduledThreadPool(2);

    public void runExport() {
        if(!plugin.getConfig().contains("backup-vault")){
            return;
        }
        boolean bool = plugin.getConfig().getBoolean("backup-vault");
        if(!bool){
            return;
        }
        sendConsole("&6请记下你的游戏版本号: " + DataFixerUtil.getCurrentVersion());
        sendConsole("&6游戏版本号将用于你在新服务端环境下 数据的转换和修复");
        sendConsole("&e三秒后 将开始数据的备份操作...");
        threads.schedule(new Runnable() {
            public void run() {
                runBackupVault();
            }
        },3, TimeUnit.SECONDS);
    }

    private void runBackupVault() {
        File file = new File(plugin.getDataFolder(), "vault_large_backup.bak");
        File file2 = new File(plugin.getDataFolder(), "vault_medium_backup.bak");
        if(file.exists() || file2.exists()){
            sendConsole("&c插件配置文件目录下含有备份文件, 请移动该文件以进行备份操作! 备份操作已取消...");
            return;
        }
        plugin.getConfig().set("backup-vault", null);
        plugin.saveConfig();
        List<VaultBackup> large_list = new ArrayList<>();
        List<VaultBackup> medium_list = new ArrayList<>();
        sendConsole("&6即将进行数据备份! 期间请勿对数据进行任何操作!");
        try {
            Connection con = ds.getConnection();
            sendConsole("&e正在获取: 大型仓库数据");
            String sql = "select * from vault_large";
            Statement stat = null;
            stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()){
                large_list.add(new VaultBackup(rs.getString("uuid"),rs.getInt("num"),rs.getString("inv")));
            }
            rs.close();
            stat.close();
            con.close();
            sendConsole("&a获取并生成对象完成: 大型仓库数据");

            Connection con1 = ds.getConnection();
            sendConsole("&e正在获取: 中型仓库数据");
            String sql1 = "select * from vault_medium";
            Statement stat1 = null;
            stat1 = con1.createStatement();
            ResultSet rs1 = stat1.executeQuery(sql1);
            while(rs1.next()){
                medium_list.add(new VaultBackup(rs1.getString("uuid"),rs1.getInt("num"),rs1.getString("inv")));
            }
            rs1.close();
            stat1.close();
            con1.close();
            sendConsole("&a获取并生成对象完成: 中型仓库数据");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TotalBackup large = new TotalBackup();
        large.setVaultBackups(large_list);
        TotalBackup medium = new TotalBackup();
        medium.setVaultBackups(medium_list);
        sendConsole("&e写入到备份文件: 大型仓库数据");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(large.saveToString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendConsole("&a完成写入: 大型仓库数据");
        sendConsole("&e写入到备份文件: 中型仓库数据");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file2))){
            bw.write(medium.saveToString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendConsole("&a完成写入: 中型仓库数据");
        try {
            file.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendConsole("&a备份操作完成!");
    }

    public void runImport() {
        if(!plugin.getConfig().contains("import-backup")){
            return;
        }
        int version = plugin.getConfig().getInt("import-backup",0);
        switch(version){
            case 4671:
            case 4554:
            case 4440:
            case 4435:
            case 4323:
            case 4189:
            case 4080:
            case 3953:
            case 3837:
            case 3700:
            case 3465:
            case 3337:
            case 3120:
            case 2975:
            case 2730:
            case 2586:
            case 1343:
                break;
            default:
                sendConsole("&c未知的游戏版本号! 请输入正确的游戏版本号以继续导入操作! 导入操作已取消...");
                return;
        }
        plugin.getConfig().set("import-backup", null);
        plugin.saveConfig();
        File file = new File(plugin.getDataFolder(), "vault_large_backup.bak");
        File file2 = new File(plugin.getDataFolder(), "vault_medium_backup.bak");
        if(!file.exists() || !file2.exists()){
            sendConsole("&c不存在备份文件 vault_large_backup.bak 和 vault_medium_backup.bak, 导入操作已取消...");
            return;
        }
        sendConsole("&6即将进行数据修复并恢复! 期间请勿对数据进行任何操作!");
        main.schedule(new Runnable() {
            public void run() {
                threads.execute(new Runnable() {
                    public void run() {
                        importLarge(version,file);
                    }
                });
                threads.execute(new Runnable() {
                    public void run() {
                        importMedium(version,file2);
                    }
                });
            }
        },3, TimeUnit.SECONDS);
    }

    private void importLarge(int before_version,File file){
        sendConsole("&e正在解析文件: vault_large_backup.bak");
        StringBuilder large_sb = new StringBuilder();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                large_sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LineIterator.closeQuietly(it);
        }
        String large = large_sb.toString();
        TotalBackup large_backup = (TotalBackup) NodeUtil.objectFromString(large);
        sendConsole("&a解析文件完成: vault_large_backup.bak");
        if(large_backup.getVaultBackups().isEmpty()){
            sendConsole("&a大型仓库数据为空 无需插入");
            return;
        }
        try {
            sendConsole("&6数据反序列化并插入: 大型仓库数据");
            Connection con = ds.getConnection();
            con.setAutoCommit(false);
            String sql = "insert into vault_large (uuid, num, inv) values(?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            for(VaultBackup vault_backup : large_backup.getVaultBackups()){
                pst.setString(1, vault_backup.getUuid());
                pst.setInt(2, vault_backup.getNum());
                pst.setString(3,NodeUtil.inventoryToBase64_Basic(vault_backup.getContents(before_version),54));
                pst.addBatch();
            }
            pst.executeBatch();
            pst.close();
            con.commit();
            con.setAutoCommit(true);
            con.close();
            sendConsole("&a数据插入完成: 大型仓库数据");
            sendConsole("&a大型仓库数据已完成导入..");
        } catch (SQLException e) {
            NodeUtil.sendError("数据恢复操作失败 失败原因: " + e.getMessage());
        }
    }

    private void importMedium(int before_version,File file){
        sendConsole("&e正在解析文件: vault_medium_backup.bak");
        StringBuilder large_sb = new StringBuilder();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                large_sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LineIterator.closeQuietly(it);
        }
        String medium = large_sb.toString();
        TotalBackup medium_backup = (TotalBackup) NodeUtil.objectFromString(medium);
        sendConsole("&a解析文件完成: vault_medium_backup.bak");
        if(medium_backup.getVaultBackups().isEmpty()){
            sendConsole("&a中型仓库数据为空 无需插入");
            return;
        }
        try {
            sendConsole("&6数据反序列化并插入: 中型仓库数据");
            Connection con1 = ds.getConnection();
            con1.setAutoCommit(false);
            String sql1 = "insert into vault_medium (uuid, num, inv) values(?,?,?)";
            PreparedStatement pst1 = con1.prepareStatement(sql1);
            for(VaultBackup vault_backup : medium_backup.getVaultBackups()){
                pst1.setString(1, vault_backup.getUuid());
                pst1.setInt(2, vault_backup.getNum());
                pst1.setString(3,NodeUtil.inventoryToBase64_Basic(vault_backup.getContents(before_version),27));
                pst1.addBatch();
            }
            pst1.executeBatch();
            pst1.close();
            con1.commit();
            con1.setAutoCommit(true);
            con1.close();
            sendConsole("&a数据导入完成: 中型仓库数据");
            sendConsole("&a中型仓库数据已完成导入..");
        } catch (SQLException e) {
            NodeUtil.sendError("数据恢复操作失败 失败原因: " + e.getMessage());
        }
    }
}
