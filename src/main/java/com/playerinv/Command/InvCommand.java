package com.playerinv.Command;

import com.playerinv.ContextNode;
import com.playerinv.PermItem.PermItem;
import com.playerinv.PlayerInv;
import com.playerinv.PluginSet;
import com.playerinv.SQLite.SQLiteConnect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
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
                commandSender.sendMessage("§f/PlayerInv Check [玩家] §e查看某玩家仓库");
                commandSender.sendMessage("§f/PlayerInv Give [玩家] [类型] §e给予玩家某一类型的仓库兑换券");
                commandSender.sendMessage("§f/PlayerInv Reload §e插件重载");
                return true;
            } else if(accessToggle && !player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
            } else if(accessToggle && player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Give [玩家] [类型] §e给予玩家某一类型的仓库兑换券");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
            } else {
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
            }
        }
        if(args.length == 1 && args[0].equals("help") && (commandSender instanceof Player) && locale().equals("en-US")){
            Player player = (Player) commandSender;
            if(player.isOp() || player.hasPermission("playerinv.admin")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen Main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle Press F To Open Main GUI");
                commandSender.sendMessage("§f/PlayerInv Give [Player] [Type] §eGive Player A Type Of Vault Ticket");
                commandSender.sendMessage("§f/PlayerInv Reload §ePlugin Reload");
                return true;
            } else if(accessToggle && !player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen Main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle Press F To Open Main GUI");
            } else if(accessToggle && player.hasPermission("playerinv.give")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §eOpen Main GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §eToggle Press F To Open Main GUI");
                commandSender.sendMessage("§f/PlayerInv Give [Player] [Type] §eGive Player A Type Of Vault Ticket");
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
                plugin.reloadConfig();
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
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Command_reload()));
                return true;
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_No_permission_command()));
            }
        }

        if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && (commandSender instanceof Player)) {
            Player player = (Player) commandSender;
            if (player.hasPermission("playerinv.keys.toggle") && (accessToggle) && (KeysOpen)) {
                ToggleF(player);
                return true;
            } else if(player.hasPermission("playerinv.keys.toggle") && !(accessToggle)){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_Toggle_unable_command()));
            } else if(!(KeysOpen)){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_Toggle_unable_keys()));
            } else if(!(player.hasPermission("playerinv.keys.toggle"))){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_No_permission_command()));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_No_permission_command()));
            }
        } else if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && !(commandSender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Messages_Not_Console_Command()));
        }

        if(args.length == 1 && args[0].equals("reload")) {
            if (!(commandSender instanceof Player)) {
                OtherMenuFileMap = new HashMap<>();
                Check_OtherMenuFileMap = new HashMap<>();
                plugin.reloadConfig();
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
                Bukkit.getServer().getConsoleSender().sendMessage(color(prefix + Command_reload()));
                return true;
            }
        }

        Boolean openaccess = plugin.getConfig().getBoolean("OpenGUIMessage");
        if(args.length == 0 && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.gui.open")){
                if(openaccess) {
                    commandSender.sendMessage(color(prefix + Messages_Open_main_gui()));
                }
                player.openInventory(Main_GUI(player));
                player.playSound(player.getLocation(), Sound.valueOf(GUISoundValue), GUISoundVolume,GUISoundPitch);
            } else {
                commandSender.sendMessage(color( prefix + Messages_Open_main_gui()));
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
                    if(!tp.hasPlayedBefore()){
                        commandSender.sendMessage(color(prefix + Messages_Check_unknown_player()));
                    }
                    if(tp.hasPlayedBefore()){
                        Check_OfflinePlayerMap.put(player,tp);
                        player.openInventory(Check_Main_GUI_Offline(tp));
                    }
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
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + Messages_Voucher_give(targetonline)));
                } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("medium")){
                    Player targetonline = Bukkit.getServer().getPlayerExact(target);
                    PermItem.sinvitem(targetonline);
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + Messages_Voucher_give(targetonline)));
                }
            } else if(!player.hasPermission("playerinv.give")){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + Messages_No_permission_command()));
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
            List<String> firstArgList = Arrays.asList("keys", "help", "open");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check", "give", "open");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check", "open");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "open");
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
            List<String> firstArgList = Arrays.asList("check", "keys", "help", "open");
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
                        if(thirdArg.toLowerCase().startsWith(args[2].toLowerCase())) {
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
        return tips;
    }
}
