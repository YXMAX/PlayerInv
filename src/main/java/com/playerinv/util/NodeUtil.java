package com.playerinv.util;

import com.playerinv.enums.PermissionEnums;
import com.playerinv.util.message.MessageUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.playerinv.manager.PermissionManager.hasPermission;
import static com.playerinv.util.LocaleUtil.sendMessages;

public class NodeUtil {

    public static void sendConsole(String msg) {
        MessageUtil.sendMsgToConsole(msg);
    }

    public static boolean isConsole(CommandSender commandSender){
        return !(commandSender instanceof Player);
    }

    public static void sendHelp(CommandSender commandSender, String msg){
        if(isConsole(commandSender)){
            sendConsole(msg);
        } else {
            commandSender.sendMessage(MessageUtil.parseString(msg));
        }
    }

    public static void sendMenuError(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/MENU-ERROR] " + msg);
    }

    public static void sendSerializerError(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/SERIALIZE-ERROR] " + msg);
    }

    public static void sendError(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/COMMON-ERROR] " + msg);
    }

    public static void sendSQLError(String method,String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/SQL-ERROR] " + method + " THROW-SQLException: " + msg);
    }

    public static void sendSQLConnectionError(String method,String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/SQL-ERROR] " + method + " THROW-SQLTransientConnectionException: " + msg);
    }

    public static void sendSQLConnectionError(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/SQL-ERROR] SQLTransientConnectionException: " + msg);
    }

    public static void sendError(String enUS,String zhCN){
        if(LocaleUtil.isChinese()){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/COMMON-ERROR] " + zhCN);
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv/COMMON-ERROR] " + enUS);
        }
    }

    public static void printHelp(CommandSender commandSender){
        sendHelp(commandSender," ");
        sendHelp(commandSender, "&e&l&m------&6&l&m=====&a&l PlayerInv Remastered &6&l&m=====&e&l&m------");
        sendHelp(commandSender," ");
        if(LocaleUtil.isChinese()) {
            if (hasPermission(commandSender, PermissionEnums.GUI_OPEN)) {
                sendHelp(commandSender, "&e/PlayerInv &f或 &e/Inv &f或 &e/Pinv &f或 &e/Pi &8▶ &8&o打开仓库主菜单");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-main &6[玩家] &8▶ &8&o为玩家打开仓库主菜单 (无视权限)");
            }
            if (hasPermission(commandSender, PermissionEnums.PICKUP_TOGGLE)) {
                sendHelp(commandSender, "&e/inv &fpickup toggle &8▶ &8&o切换 自动收纳掉落物 开关");
            }
            if (hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                sendHelp(commandSender, "&e/inv &falter-name &6[仓库类型] [仓库编号] &8▶ &8&o更改对应仓库的名称");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                sendHelp(commandSender, "&e/inv &fcheck &6[玩家] &8▶ &8&o查看某玩家仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                sendHelp(commandSender, "&e/inv &fgive &6[玩家] [类型] &8▶ &8&o给予玩家某一类型的仓库兑换券");
            }
            if (hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen &6[类型] [编号] &8▶ &8&o打开某一类型和编号的仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-vault &6[玩家] [large/medium] [编号] &8▶ &8&o为玩家打开相应类型和编号的仓库 (无视权限)");
            }
            if (hasPermission(commandSender, PermissionEnums.VAULT_GIVE) || hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                sendHelp(commandSender, "&e/inv &fvault &6[给予类型] [仓库类型] [数值] [玩家] &8▶ &8&o给予或追加给予玩家相应数值的仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_RELOAD)) {
                sendHelp(commandSender, "&e/inv &freload &8▶ &8&o重载插件");
            }
            sendHelp(commandSender, " ");
            if (isConsole(commandSender) || commandSender.isOp()) {
                sendHelp(commandSender, "&e更多关于插件的信息 查看: https://gitee.com/yxmax_w/player-inv-reloaded-wiki/wikis");
                sendHelp(commandSender, " ");
            }
        } else {
            if (hasPermission(commandSender, PermissionEnums.GUI_OPEN)) {
                sendHelp(commandSender, "&e/PlayerInv &fOR &e/Inv &fOR &e/Pinv &fOR &e/Pi &8▶ &8&oOpen main GUI");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-main &6[PLAYER] &8▶ &8&oOpen the main menu for players (Ignore Permission)");
            }
            if (hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                sendHelp(commandSender, "&e/inv &falter-name &6[Vault-type] [Vault-num] &8▶ &8&oAlter the vault name");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                sendHelp(commandSender, "&e/inv &fcheck &6[PLAYER] &8▶ &8&oChceck the player vault contents");
            }
            if (hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                sendHelp(commandSender, "&e/inv &fgive &6[PLAYER] [TYPE] &8▶ &8&oGive player a type of vault-voucher");
            }
            if (hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen &6[TYPE] [NUM] &8▶ &8&oOpen a vault of a certain type and number");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-vault &6[PLAYER] [VAULT_TYPE] [VAULT_NUM] &8▶ &8&oOpen a vault for players (Ignore Permission)");
            }
            if (hasPermission(commandSender, PermissionEnums.VAULT_GIVE) || hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                sendHelp(commandSender, "&e/inv &fvault &6[GIVEN_TYPE] [VAULT_TYPE] [NUM] [PLAYER] &8▶ &8&oAppend or add vault that give players corresponding values");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_RELOAD)) {
                sendHelp(commandSender, "&e/inv &freload &8▶ &8&oReload the plugin");
            }
            sendHelp(commandSender, " ");
        }
        sendHelp(commandSender, "&e&l&m------&6&l&m=====&a&l PlayerInv Remastered &6&l&m=====&e&l&m------");
    }

    public static void sendConsole(String zhCN,String enUS){
        if (LocaleUtil.isChinese()) {
            MessageUtil.sendMsgToConsole(zhCN);
        } else {
            MessageUtil.sendMsgToConsole(enUS);
        }
    }
}
