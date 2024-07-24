package com.playerinv.Command;

import com.playerinv.ContextNode;
import com.playerinv.PermItem.PermItem;
import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

import static com.playerinv.ContextNode.addPermission_Large;
import static com.playerinv.LPVerify.VerifyPermission.*;
import static com.playerinv.LocaleUtil.*;
import static com.playerinv.MainGUI.MainMenu.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.PluginSet.*;
import static com.playerinv.SQLite.SQLiteConnect.con;

public class InvCommand implements CommandExecutor , TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String prefix = PluginSet.prefix();
        Boolean KeysOpen = plugin.getConfig().getBoolean("KeysOpen");
        Boolean accessToggle = plugin.getConfig().getBoolean("OpenToggle");
        if(args.length == 1 && args[0].equals("help") && (commandSender instanceof Player) && locale().equals("zh-CN")){
            Player player = (Player) commandSender;
            if(player.isOp() || player.hasPermission("playerinv.admin")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §e开/关 关闭仓库时返回主菜单");
                commandSender.sendMessage("§f/PlayerInv Check [玩家] §e查看某玩家仓库");
                commandSender.sendMessage("§f/PlayerInv Give [玩家] [类型] §e给予玩家某一类型的仓库兑换券");
                commandSender.sendMessage("§f/PlayerInv Open [类型] [编号] §e打开某一类型和编号的仓库");
                commandSender.sendMessage("§f/PlayerInv Vault [给予类型] [仓库类型] [数值] [玩家] §e给予或追加给予玩家相应数值的仓库");
                commandSender.sendMessage("§f/PlayerInv Reload §e插件重载");
                return true;
            } else if(accessToggle && !player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §e开/关 关闭仓库时返回主菜单");
            } else if(accessToggle && player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Give [玩家] [类型] §e给予玩家某一类型的仓库兑换券");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §e开/关 关闭仓库时返回主菜单");
            } else {
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
            }
        }
        if(args.length == 1 && args[0].equals("help") && (commandSender instanceof Player) && locale().equals("en-US")){
            Player player = (Player) commandSender;
            if(player.isOp() || player.hasPermission("playerinv.admin")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle press F to open main GUI");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §eToggle return to main menu when closing vault");
                commandSender.sendMessage("§f/PlayerInv Give [Player] [Type] §eGive player a type of vault ticket");
                commandSender.sendMessage("§f/PlayerInv Open [Type] [Num] §eOpen a vault of a certain type and number");
                commandSender.sendMessage("§f/PlayerInv Vault [Given Type] [Vault Type] [Num] [Player] §eappend or add vault that give players corresponding values");
                commandSender.sendMessage("§f/PlayerInv Reload §ePlugin Reload");
                return true;
            } else if(accessToggle && !player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle press F to open main GUI");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §eToggle return to main menu when closing vault");
            } else if(accessToggle && player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle press F to open main GUI");
                commandSender.sendMessage("§f/PlayerInv Return Toggle §eToggle return to main menu when closing vault");
                commandSender.sendMessage("§f/PlayerInv Give [Player] [Type] §eGive player a type of vault ticket");
            } else {
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen Main GUI");
            }
        }

        if(args.length == 1 && args[0].equals("reload") && (commandSender instanceof Player)) {
            Player player = (Player) commandSender;
            if (player.isOp() || player.hasPermission("playerinv.admin")) {
                OtherMenuFileMap = new HashMap<>();
                Check_OtherMenuFileMap = new HashMap<>();
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                reloadMainMenuConfig();
                getFile_writeMap();
                getCheckFile_writeMap();
                reloadLocale_GUI();
                reloadSplitSoundValue_Voucher();
                Large_Amount = Vault_large_amount();
                Medium_Amount = Vault_medium_amount();
                OtherMenuVaultSlotMap_Large = new HashMap<>();
                OtherMenuVaultSlotMap_Medium = new HashMap<>();
                OtherMenuItemMap = new HashMap<>();
                Check_OtherMenuVaultSlotMap_Large = new HashMap<>();
                Check_OtherMenuVaultSlotMap_Medium = new HashMap<>();
                Check_OtherMenuItemMap = new HashMap<>();
                Check_MainMenuItemMap = new HashMap<>();
                Check_MainMenuVaultSlotMap_Large = new HashMap<>();
                Check_MainMenuVaultSlotMap_Medium = new HashMap<>();
                MainMenuItemMap = new HashMap<>();
                MainMenuVaultSlotMap_Large = new HashMap<>();
                MainMenuVaultSlotMap_Medium = new HashMap<>();
                reloadOtherMenuInventoryMap();
                Update_Locale_Config();
                Update_Config();
                commandSender.sendMessage(color(prefix + Command_reload()));
                return true;
            } else {
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            }
        }

        if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && (commandSender instanceof Player)) {
            Player player = (Player) commandSender;
            if (player.hasPermission("playerinv.keys.toggle") && (accessToggle) && (KeysOpen)) {
                ToggleF(player);
                return true;
            } else if(player.hasPermission("playerinv.keys.toggle") && !(accessToggle)){
                commandSender.sendMessage(color(prefix + Messages_Toggle_unable_command()));
            } else if(!(KeysOpen)){
                commandSender.sendMessage(color(prefix + Messages_Toggle_unable_keys()));
            } else if(!(player.hasPermission("playerinv.keys.toggle"))){
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            } else {
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            }
        } else if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && !(commandSender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));
        }

        if(args.length == 2 && args[0].equals("return") && args[1].equals("toggle")) {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.hasPermission("playerinv.return.toggle")) {
                    if(Return_to_main()){
                        ToggleReturn(player);
                        return true;
                    } else {
                        commandSender.sendMessage(color(prefix + Messages_Return_to_main_config_disabled()));
                        return true;
                    }
                } else {
                    commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
                    return true;
                }
            } else if(!(commandSender instanceof Player)){
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));
                return true;
            }
        }

        if(args.length == 1 && args[0].equals("reload")) {
            if (!(commandSender instanceof Player)) {
                OtherMenuFileMap = new HashMap<>();
                Check_OtherMenuFileMap = new HashMap<>();
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                reloadMainMenuConfig();
                getFile_writeMap();
                getCheckFile_writeMap();
                reloadLocale_GUI();
                reloadSplitSoundValue_Voucher();
                Large_Amount = Vault_large_amount();
                Medium_Amount = Vault_medium_amount();
                OtherMenuVaultSlotMap_Large = new HashMap<>();
                OtherMenuVaultSlotMap_Medium = new HashMap<>();
                OtherMenuItemMap = new HashMap<>();
                Check_OtherMenuVaultSlotMap_Large = new HashMap<>();
                Check_OtherMenuVaultSlotMap_Medium = new HashMap<>();
                Check_OtherMenuItemMap = new HashMap<>();
                Check_MainMenuItemMap = new HashMap<>();
                Check_MainMenuVaultSlotMap_Large = new HashMap<>();
                Check_MainMenuVaultSlotMap_Medium = new HashMap<>();
                MainMenuItemMap = new HashMap<>();
                MainMenuVaultSlotMap_Large = new HashMap<>();
                MainMenuVaultSlotMap_Medium = new HashMap<>();
                reloadOtherMenuInventoryMap();
                Update_Locale_Config();
                Update_Config();
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Command_reload()));
                return true;
            }
        }

        Boolean openaccess = plugin.getConfig().getBoolean("OpenGUIMessage");
        if(args.length == 0 && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.gui.open") || player.isOp()){
                if(openaccess) {
                    commandSender.sendMessage(color(prefix + Messages_Open_main_gui()));
                }
                player.openInventory(Main_GUI(player));
                player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
            } else {
                commandSender.sendMessage(color( prefix + Messages_No_permission_command()));
            }

            return true;
        }

        if((args.length == 0 && !(commandSender instanceof Player)) || (args.length == 1 && args[0].equals("help") && !(commandSender instanceof Player))){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));

            return true;
        }

        if(args.length == 2 && args[0].equals("check") && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.check") || player.hasPermission("playerinv.admin") || player.isOp()){
                String targetp = args[1];
                if(Bukkit.getServer().getPlayerExact(targetp) == null){
                    String uuid = String.valueOf(Bukkit.getOfflinePlayer(targetp).getUniqueId());
                    OfflinePlayer tp = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    Check_OfflinePlayerMap.put(player,tp);
                    player.openInventory(Check_Main_GUI_Offline(tp));
                } else if(Bukkit.getServer().getPlayerExact(targetp) != null){
                    Player targetonline = Bukkit.getServer().getPlayerExact(targetp);
                    Check_OnlinePlayerMap.put(player,targetonline);
                    player.openInventory(Check_Main_GUI(targetonline));
                }
            } else if(!player.hasPermission("playerinv.check")){
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            }
        }


        if(args.length == 1 && args[0].equals("check") && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(!player.hasPermission("playerinv.check")){
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            }
            if(player.hasPermission("playerinv.check") || player.hasPermission("playerinv.admin") || player.isOp()){
                commandSender.sendMessage(color(prefix + Messages_Check_Player_Error()));
            }
        }

        if(args.length >= 1 && args[0].equals("check") && !(commandSender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));
        }


        if(args.length == 3 && args[0].equals("give") && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.give") || player.hasPermission("playerinv.admin") || player.isOp()){
                String target = args[1];
                if(Bukkit.getServer().getPlayerExact(target) == null){
                    commandSender.sendMessage(color(prefix + Messages_Console_give_voucher_player_error()));
                } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("large")){
                    Player targetonline = Bukkit.getServer().getPlayerExact(target);
                    PermItem.linvitem(targetonline);
                    commandSender.sendMessage(color(prefix + Messages_Voucher_give(targetonline)));
                } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("medium")){
                    Player targetonline = Bukkit.getServer().getPlayerExact(target);
                    PermItem.sinvitem(targetonline);
                    commandSender.sendMessage(color( prefix + Messages_Voucher_give(targetonline)));
                }
            } else if(!player.hasPermission("playerinv.give")){
                commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
            }
        }

        if(args.length == 3 && args[0].equals("give") && !(commandSender instanceof Player)){
            String target = args[1];
            if(Bukkit.getServer().getPlayerExact(target) == null){
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Console_give_voucher_player_error()));
            } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("large")){
                Player targetonline = Bukkit.getServer().getPlayerExact(target);
                PermItem.linvitem(targetonline);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Console_give_voucher_large_notice(targetonline)));
                targetonline.sendMessage(color(prefix + Messages_Console_give_voucher_large(targetonline)));
            } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("medium")){
                Player targetonline = Bukkit.getServer().getPlayerExact(target);
                PermItem.sinvitem(targetonline);
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Console_give_voucher_medium_notice(targetonline)));
                targetonline.sendMessage(color(prefix + Messages_Console_give_voucher_medium(targetonline)));
            }
        }

        if(args[0].equals("open") && commandSender.hasPermission("playerinv.invopen")){
            Player player = (Player) commandSender;
            if(!(commandSender instanceof Player)){
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));
                return true;
            }
            if(args.length == 1 || args.length == 2){
                player.sendMessage(color(prefix + Messages_Unknown_number()));
                return true;
            }
            if(args.length == 3){
                if(args[1].equals("large")){
                    if(isNum(args[2])){
                        Integer vault_num = Integer.valueOf(args[2]);
                        try{
                            if(vault_num <= 10){
                                if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + vault_num)){
                                    if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                        SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                        player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    } else {
                                        player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                                    return true;
                                }
                            }
                            if(vault_num > 10){
                                if(player.hasPermission("playerinv.large.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp()){
                                    if(!SQLiteConnect.hasData_Large(con,player.getUniqueId().toString(),vault_num)){
                                        SQLiteConnect.insert_Large(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                        player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    } else {
                                        player.openInventory(inventoryFromBase64_Large(SQLiteConnect.InvCode_Large(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(args[1].equals("medium")){
                    if(isNum(args[2])){
                        Integer vault_num = Integer.valueOf(args[2]);
                        try{
                            if(vault_num <= 15){
                                int old_vault_num = vault_num + 10;
                                if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp() || player.hasPermission("playerinv.inv." + old_vault_num)){
                                    if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                        SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                        player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    } else {
                                        player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                                    return true;
                                }
                            }
                            if(vault_num > 15){
                                if(player.hasPermission("playerinv.medium.inv." + vault_num) || player.hasPermission("playerinv.inv.*") || player.hasPermission("playerinv.admin") || player.isOp()){
                                    if(!SQLiteConnect.hasData_Medium(con,player.getUniqueId().toString(),vault_num)){
                                        SQLiteConnect.insert_Medium(con,player.getUniqueId().toString(),vault_num,"rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
                                        player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    } else {
                                        player.openInventory(inventoryFromBase64_Medium(SQLiteConnect.InvCode_Medium(con, player.getUniqueId().toString(), vault_num), vault_num));
                                        player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(color(prefix() + Messages_No_permission_vault()));
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(!args[1].equals("large") && !args[1].equals("medium")){
                    player.sendMessage(color(prefix + Messages_Unknown_number()));
                    return true;
                }
            }
        }
        if(args[0].equalsIgnoreCase("vault")){
            Boolean lp_proxy = PlayerInv.plugin.getConfig().getBoolean("Luckperms-proxy-support");
            Boolean lp_give = plugin.getConfig().getBoolean("Luckperms-give-permissions");
            Boolean isConsole = false;
            if(!(commandSender instanceof Player)){
                isConsole = true;
            }
            switch(args.length) {
                case 1:
                case 2:
                case 3:
                case 4:
                    if (isConsole) {
                        Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Unknown_number()));
                        return true;
                    } else {
                        commandSender.sendMessage(color(prefix + Messages_Unknown_number()));
                        return true;
                    }
                case 5:
                case 6:
                case 7:
                    if (!isConsole) {
                        Player player = Bukkit.getPlayerExact(args[4]);
                        if(player == null){
                            commandSender.sendMessage(color(prefix + Messages_Console_give_voucher_player_error()));
                            return true;
                        }
                        if(!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append")){
                            commandSender.sendMessage(color(prefix + Messages_No_permission_command()));
                            return true;
                        }
                        if (args[1].equals("give") && commandSender.hasPermission("playerinv.vault.give")) {
                            if (args[2].equals("large")) {
                                if (isNum(args[3])) {
                                    int num = Integer.parseInt(args[3]);
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if (num <= 10) {
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.large.inv." + num) && !plugin.perms.has(player, "playerinv.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.large.inv." + Integer.parseInt(args[3]));
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_large_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context(player,num,day,commandSender,1);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions(player,num,day,commandSender,1);
                                        }
                                    }
                                    if(num >= 11){
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.large.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.large.inv." + Integer.parseInt(args[3]));
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_large_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context(player,num,day,commandSender,1);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions(player,num,day,commandSender,1);
                                        }
                                    }
                                } else {
                                    commandSender.sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                            if (args[2].equals("medium")) {
                                if (isNum(args[3])) {
                                    int num = Integer.parseInt(args[3]);
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if(num <= 15) {
                                        int old_num = num + 10;
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.medium.inv." + num) && !plugin.perms.has(player, "playerinv.inv." + old_num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.medium.inv." + num);
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_medium_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context(player,num,day,commandSender,2);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions(player,num,day,commandSender,2);
                                        }
                                    }
                                    if(num >= 16) {
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.medium.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.medium.inv." + num);
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_medium_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context(player,num,day,commandSender,2);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions(player,num,day,commandSender,2);
                                        }
                                    }
                                } else {
                                    commandSender.sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                        }
                        if (args[1].equals("append") && commandSender.hasPermission("playerinv.vault.append")) {
                            if (args[2].equals("large")) {
                                if (isNum(args[3])) {
                                    long day = 0;
                                    Boolean ignore = false;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if(args.length >= 7 && !args[6].isEmpty()){
                                        ignore = Boolean.valueOf(args[6]);
                                    }
                                    if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                        for(int i=1;i<(Large_Amount + 1);i++){
                                            if(!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.large.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.large.inv." + i) && i >= 11){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.large.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && lp_proxy && lp_give) {
                                        for (int i = 1; i < (Large_Amount + 1); i++) {
                                            if (!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10) {
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for (int add = 0; add < amount; add++) {
                                                    if (i + add > Large_Amount) {
                                                        break;
                                                    }
                                                    if (player.hasPermission("playerinv.large.inv." + (i + add))) {
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Large(player, i + add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player, i - 1, String.valueOf(success), day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i), String.valueOf(i + amount - 1), day)));
                                                return true;
                                            }
                                            if (!player.hasPermission("playerinv.large.inv." + i) && i >= 11) {
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for (int add = 0; add < amount; add++) {
                                                    if (i + add > Large_Amount) {
                                                        break;
                                                    }
                                                    if (player.hasPermission("playerinv.large.inv." + (i + add))) {
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Large(player, i + add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player, i - 1, String.valueOf(success), day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i), String.valueOf(i + amount - 1), day)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && !lp_proxy && lp_give) {
                                        for(int i=1;i<(Large_Amount + 1);i++){
                                            if(!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    addPermission_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.large.inv." + i) && i >= 11){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    addPermission_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                } else {
                                    commandSender.sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                            if (args[2].equals("medium")) {
                                if (isNum(args[3])) {
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.medium.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.medium.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && lp_proxy && lp_give) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && !lp_proxy && lp_give) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermission_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermission_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                commandSender.sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                } else {
                                    commandSender.sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                        }
                    } else if(isConsole){
                        Player player = Bukkit.getPlayerExact(args[4]);
                        if(player == null){
                            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Console_give_voucher_player_error()));
                            return true;
                        }
                        if (args[1].equals("give")) {
                            if (args[2].equals("large")) {
                                if (isNum(args[3])) {
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    int num = Integer.parseInt(args[3]);
                                    if (num <= 10) {
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.large.inv." + num) && !plugin.perms.has(player, "playerinv.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.large.inv." + Integer.parseInt(args[3]));
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_large_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context_Console(player,num,day,1);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions_Console(player,num,day,1);
                                        }
                                    }
                                    if(num >= 11){
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.large.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.large.inv." + Integer.parseInt(args[3]));
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_large_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_large_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context_Console(player,num,day,1);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions_Console(player,num,day,1);
                                        }
                                    }
                                } else {
                                    Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                            if (args[2].equals("medium")) {
                                if (isNum(args[3])) {
                                    int num = Integer.parseInt(args[3]);
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if(num <= 15) {
                                        int old_num = num + 10;
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.medium.inv." + num) && !plugin.perms.has(player, "playerinv.inv." + old_num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.medium.inv." + num);
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_medium_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context_Console(player,num,day,2);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions_Console(player,num,day,2);
                                        }
                                    }
                                    if(num >= 16) {
                                        if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                            if (!plugin.perms.has(player, "playerinv.medium.inv." + num)) {
                                                plugin.perms.playerAdd(null, player, "playerinv.medium.inv." + num);
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_medium_success(player, args[3],0)));
                                                player.sendMessage(color(prefix + Vault_player_give_medium_notice(args[3],0)));
                                                return true;
                                            } else {
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_give_already_has(player)));
                                                return true;
                                            }
                                        }
                                        if (hasLuckPerms && lp_proxy && lp_give) {
                                            verifyGivePermissions_Context_Console(player,num,day,2);
                                        }
                                        if (hasLuckPerms && !lp_proxy && lp_give) {
                                            verifyGivePermissions_Console(player,num,day,2);
                                        }
                                    }
                                } else {
                                    Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                        }
                        if (args[1].equals("append")) {
                            if (args[2].equals("large")) {
                                if (isNum(args[3])) {
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                        for(int i=1;i<(Large_Amount + 1);i++){
                                            if(!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.large.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.large.inv." + i) && i >= 11){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.large.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && lp_proxy && lp_give) {
                                        for(int i=1;i<(Large_Amount + 1);i++){
                                            if(!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.large.inv." + i) && i >= 11){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && !lp_proxy && lp_give) {
                                        for(int i=1;i<(Large_Amount + 1);i++){
                                            if(!player.hasPermission("playerinv.large.inv." + i) && !player.hasPermission("playerinv.inv." + i) && i <= 10){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    addPermission_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.large.inv." + i) && i >= 11){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Large_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.large.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    addPermission_Large(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_large_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_large_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                } else {
                                    Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                            if (args[2].equals("medium")) {
                                if (isNum(args[3])) {
                                    long day = 0;
                                    if(args.length >= 6 && !args[5].isEmpty()){
                                        day = Long.parseLong(args[5]);
                                    }
                                    if ((!hasLuckPerms && !lp_proxy && !lp_give) || (hasLuckPerms && !lp_proxy && !lp_give)) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.medium.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),0)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),0)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    plugin.perms.playerAdd(null,player,"playerinv.medium.inv." + (i+add));
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && lp_proxy && lp_give) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermissionWithContext_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                    if (hasLuckPerms && !lp_proxy && lp_give) {
                                        for(int i=1;i<(Medium_Amount + 1);i++){
                                            int old_num = i + 10;
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && !player.hasPermission("playerinv.inv." + old_num) && i <= 15){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermission_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                            if(!player.hasPermission("playerinv.medium.inv." + i) && i >= 16){
                                                int success = 0;
                                                int amount = Integer.parseInt(args[3]);
                                                for(int add=0;add<amount;add++){
                                                    if(i+add > Medium_Amount){
                                                        break;
                                                    }
                                                    if(player.hasPermission("playerinv.medium.inv." + (i+add))){
                                                        continue;
                                                    }
                                                    ContextNode.addPermission_Medium(player,i+add, day);
                                                    success = success + 1;
                                                }
                                                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Vault_command_append_medium_success(player,i-1, String.valueOf(success),day)));
                                                player.sendMessage(color(prefix + Vault_player_append_medium_notice(String.valueOf(i),String.valueOf(i+amount-1),day)));
                                                return true;
                                            }
                                        }
                                    }
                                } else {
                                    Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Unknown_number()));
                                    return true;
                                }
                            }
                        }
                    }
            }
        }

        return true;
    }



    public void ToggleF(Player player){
        String prefix = prefix();
        try {
            if(SQLiteConnect.getToggle(con,player.getUniqueId().toString())){
                SQLiteConnect.updateToggle(con,player.getUniqueId().toString(),false);
                player.sendMessage(color(prefix + Messages_Toggle_disabled()));
            } else {
                SQLiteConnect.updateToggle(con,player.getUniqueId().toString(),true);
                player.sendMessage(color(prefix + Messages_Toggle_enabled()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        LinkedList<String> tips = new LinkedList<>();

        if(args.length == 1 && !(player.hasPermission("playerinv.reload") || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.check") || player.hasPermission("playerinv.give"))) {
            List<String> firstArgList = Arrays.asList("keys", "help", "open", "return");
            if(args[0].isEmpty()){
                tips.addAll(firstArgList);
                return tips;
            } else {
                for(String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }

        if(args.length == 1 && (player.hasPermission("playerinv.admin") || player.isOp())) {
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check", "give", "open", "vault", "return");
            if(args[0].isEmpty()){
                tips.addAll(firstArgList);
                return tips;
            } else {
                for(String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }

        if(args.length == 1 && player.hasPermission("playerinv.reload") && player.hasPermission("playerinv.check")) {
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check", "open", "return");
            if(args[0].isEmpty()){
                tips.addAll(firstArgList);
                return tips;
            } else {
                for(String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }

        if(args.length == 1 && player.hasPermission("playerinv.reload")) {
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "open", "return");
            if(args[0].isEmpty()){
                tips.addAll(firstArgList);
                return tips;
            } else {
                for(String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }
        if(args.length == 1 && player.hasPermission("playerinv.check")) {
            List<String> firstArgList = Arrays.asList("check", "keys", "help", "open", "return");
            if(args[0].isEmpty()){
                tips.addAll(firstArgList);
                return tips;
            } else {
                for(String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }
        if(args.length == 2){
            List<String> secondArgList = Arrays.asList(("toggle"));
            if(args[0].equalsIgnoreCase("keys".toLowerCase())) {
                if(args[1].isEmpty()){
                    tips.addAll(secondArgList);
                } else {
                    for(String secArg : secondArgList) {
                        if(secArg.toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(secArg);
                        }
                    }
                }
            }
        }
        if(args.length == 2){
            List<String> secondArgList = Arrays.asList(("toggle"));
            if(args[0].equalsIgnoreCase("return".toLowerCase())) {
                if(args[1].isEmpty()){
                    tips.addAll(secondArgList);
                } else {
                    for(String secArg : secondArgList) {
                        if(secArg.toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(secArg);
                        }
                    }
                }
            }
        }
        if(args.length == 2 && (player.hasPermission("playerinv.check") || player.hasPermission("playerinv.admin"))){
            if(args[0].equalsIgnoreCase("check".toLowerCase())) {
                if(args[1].isEmpty()){
                    Bukkit.getOnlinePlayers().forEach(p -> tips.add(p.getName()));
                    return tips;
                } else {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(p.getName());
                        }
                    });
                }
            }
        }
        if(args.length == 2 && (player.hasPermission("playerinv.give") || player.hasPermission("playerinv.admin") || player.isOp())){
            if(args[0].equalsIgnoreCase("give".toLowerCase())) {
                if(args[1].isEmpty()){
                    Bukkit.getOnlinePlayers().forEach(p -> tips.add(p.getName()));
                } else {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(p.getName());
                        }
                    });
                }
            }
        }
        if(args.length == 2 && (player.hasPermission("playerinv.invopen") || player.hasPermission("playerinv.admin") || player.isOp())){
            if(args[0].equalsIgnoreCase("open".toLowerCase())) {
                List<String> thirdArgList = Arrays.asList("large", "medium");
                if(args[1].isEmpty()){
                    tips.addAll(thirdArgList);
                } else {
                    for(String thirdArg : thirdArgList) {
                        if(thirdArg.toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(thirdArg);
                        }
                    }
                }
            }
        }
        if(args.length == 3 && (player.hasPermission("playerinv.give") || player.hasPermission("playerinv.admin") || player.isOp())){
            if(args[0].equalsIgnoreCase("give".toLowerCase())) {
                List<String> thirdArgList = Arrays.asList("large", "medium");
                if(!args[1].isEmpty()){
                    if(args[2].isEmpty()){
                        tips.addAll(thirdArgList);
                    } else {
                        for(String thirdArg : thirdArgList) {
                            if(thirdArg.toLowerCase().startsWith(args[2].toLowerCase())) {
                                tips.add(thirdArg);
                            }
                        }
                    }
                }
            }
        }
        if(args.length == 3 && (player.hasPermission("playerinv.vault.give") || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.vault.append") || player.isOp())){
            if(args[0].equalsIgnoreCase("vault".toLowerCase())) {
                List<String> thirdArgList = Arrays.asList("large", "medium");
                if(args[1].equals("give") || args[1].equals("append")){
                    if(args[2].isEmpty()){
                        tips.addAll(thirdArgList);
                    } else {
                        for(String thirdArg : thirdArgList) {
                            if(thirdArg.toLowerCase().startsWith(args[2].toLowerCase())) {
                                tips.add(thirdArg);
                            }
                        }
                    }
                }
            }
        }
        if(args.length == 2 && (player.hasPermission("playerinv.vault.give") || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.vault.append") || player.isOp())){
            if(args[0].equalsIgnoreCase("vault".toLowerCase())) {
                List<String> thirdArgList = Arrays.asList("give", "append");
                if(args[1].isEmpty()){
                    tips.addAll(thirdArgList);
                } else {
                    for(String thirdArg : thirdArgList) {
                        if(thirdArg.toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(thirdArg);
                        }
                    }
                }
            }
        }
        return tips;
    }
}
