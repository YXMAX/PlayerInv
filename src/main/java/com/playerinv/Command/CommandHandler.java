package com.playerinv.Command;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.JDBCUtil;
import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Operation.TypeAttribute;
import com.playerinv.Util.LoadUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.playerinv.Enums.LocaleEnums.*;
import static com.playerinv.Listener.PlayerListener.typeAttributeMap;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.LocaleUtil.*;
import static com.playerinv.Util.NodeUtil.*;
import static com.playerinv.Util.InitUtil.*;


public class CommandHandler implements CommandExecutor , TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player;
        switch(args.length) {

            case 0:
                //---/inv 打开仓库主菜单
                if (isConsole(commandSender)) {
                    sendMessages(commandSender, Not_console_command);
                    return true;
                }
                if (!hasPermission(commandSender, PermissionEnums.GUI_OPEN)) {
                    sendMessages(commandSender, No_permission_command);
                    return true;
                }
                player = (Player) commandSender;
                if(!cacheInventoryManager.isCacheReady(player)){
                    sendMessages(player,Cache_not_ready);
                    return true;
                }
                if(cacheInventoryManager.isCacheBroken(player)){
                    sendMessages(player,Cache_broken);
                    return true;
                }
                menuManager.openVaultMenu(player,null);
                return true;

            case 1:

                switch (args[0].toLowerCase()) {

                    case "help":
                        printHelp(commandSender);
                        return true;

                    case "alter-name":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender,Open_vault_type_error);
                        return true;

                    case "open":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Unknown_number);
                        return true;

                    case "open-vault":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Open_vault_player_error);
                        return true;

                    case "open-main":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Open_vault_player_error);
                        return true;

                    case "pickup":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.PICKUP_TOGGLE)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Unknown_number);
                        return true;

                    case "check":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Check_unknown_player);
                        return true;

                    case "give":
                        if (!hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Check_unknown_player);
                        return true;

                    case "vault":
                        if (!hasPermission(commandSender, PermissionEnums.VAULT_GIVE) && !hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Unknown_number);
                        return true;

                    case "reload":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_RELOAD)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        LoadUtil loadUtil = new LoadUtil();
                        loadUtil.loadPluginConfig(false);
                        loadUtil.loadPrefix();
                        loadUtil.loadLocale();
                        loadUtil.loadVaultAmount();
                        loadUtil.loadLocaleConfig();
                        loadUtil.updateLocaleConfig();
                        loadUtil.loadVaultMenuConfig();
                        loadUtil.loadFunction();
                        loadUtil.loadSound();
                        loadUtil.loadPickupList();
                        loadUtil.loadVaultEvent();
                        menuManager.structAll();
                        itemTranslator.reloadCustomTranslations();
                        if(isMySQL){
                            CompletableFuture.runAsync(() -> {
                                boolean bool = JDBCUtil.checkMySQLConnection();
                                if(bool){
                                    sendMessages(commandSender,Connection_valid);
                                } else {
                                    sendMessages(commandSender,Connection_not_valid);
                                }
                            });
                        }
                        sendMessages(commandSender, Command_reload);
                        return true;

                    default:
                        sendMessages(commandSender, Unknown_number);
                        return true;

                }

            case 2:

                switch (args[0].toLowerCase()) {

                    case "alter-name":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender,Open_vault_num_error);
                        return true;

                    case "open":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Unknown_number);
                        return true;

                    case "open-vault":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Open_vault_type_error);
                        return true;

                    case "open-main":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                        if(!t.isOnline()) {
                            sendMessages(commandSender,Console_give_voucher_player_error);
                            return true;
                        }
                        Player target = t.getPlayer();
                        if(cacheInventoryManager.isCacheBroken(target)){
                            sendMessages(target,Cache_broken);
                            return true;
                        }
                        menuManager.openVaultMenu(target,null);
                        sendMessages(commandSender,Console_open_main,"%player%",target.getName());
                        return true;

                    case "pickup":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.PICKUP_TOGGLE)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        if(!args[1].equalsIgnoreCase("toggle")){
                            sendMessages(commandSender,Unknown_number);
                            return true;
                        }
                        Player tar = (Player) commandSender;
                        Boolean bool = jdbcUtil.getPickupToggle(tar.getUniqueId().toString());
                        if(bool){
                            jdbcUtil.updatePickupToggleAsync(String.valueOf(tar.getUniqueId()),false);
                            sendMessages(tar,Auto_pickup_disabled);
                        } else {
                            jdbcUtil.updatePickupToggleAsync(String.valueOf(tar.getUniqueId()),true);
                            sendMessages(tar,Auto_pickup_enabled);
                        }
                        return true;

                    case "check":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        OfflinePlayer tc;
                        try {
                            tc = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                        } catch (Exception e){
                            tc = Bukkit.getOfflinePlayer(args[1]);
                        }
                        menuManager.openCheckMenu((Player) commandSender,tc);
                        return true;

                    case "give":
                        if (!hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Open_vault_type_error);
                        return true;

                    case "vault":
                        if (!hasPermission(commandSender, PermissionEnums.VAULT_GIVE) && !hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        sendMessages(commandSender, Open_vault_type_error);
                        return true;

                    default:
                        sendMessages(commandSender, Unknown_number);
                        return true;
                }

            case 3:
            case 4:
            case 5:
            case 6:
            case 7:

                switch (args[0].toLowerCase()) {

                    case "alter-name":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        if(!vaultNameChange){
                            sendMessages(commandSender,LocaleEnums.Vault_attribute_change_disabled);
                            return true;
                        }
                        int type1;
                        int vault_num1;
                        switch(args[1].toLowerCase()){
                            case "large":
                                type1 = 1;
                                break;
                            case "medium":
                                type1 = 2;
                                break;
                            default:
                                sendMessages(commandSender, Open_vault_type_error);
                                return true;
                        }
                        String num1 = args[2];
                        if(!NodeUtil.isNum(num1)){
                            sendMessages(commandSender, Open_vault_num_error);
                            return true;
                        }
                        vault_num1 = Integer.parseInt(num1);
                        if(vault_num1 <= 0){
                            sendMessages(commandSender, Open_vault_num_error);
                            return true;
                        }
                        if (!hasVaultPermission((Player) commandSender,type1,vault_num1)) {
                            sendMessages(commandSender, No_permission_vault);
                            return true;
                        }
                        typeAttributeMap.put((Player) commandSender,new TypeAttribute((Player) commandSender,type1,vault_num1,null));
                        sendMessages((Player) commandSender,LocaleEnums.Vault_attribute_change_notice);
                        soundManager.playSoundEffect((Player) commandSender,soundManager.typeChatBar);
                        return true;

                    case "open":
                        if (isConsole(commandSender)) {
                            sendMessages(commandSender, Not_console_command);
                            return true;
                        }
                        if (!hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        Player player1 = (Player) commandSender;
                        if(args.length == 3){
                            if(!NodeUtil.isNum(args[2])){
                                sendMessages(commandSender, Unknown_number);
                                return true;
                            }
                            if(args[1].equalsIgnoreCase("large")){
                                vaultManager.openStorageVault(player1,1,Integer.parseInt(args[2]),null,true);
                                return true;
                            } else if(args[1].equalsIgnoreCase("medium")){
                                vaultManager.openStorageVault(player1,2,Integer.parseInt(args[2]),null,true);
                                return true;
                            } else {
                                sendMessages(commandSender, Unknown_number);
                                return true;
                            }
                        } else {
                            sendMessages(commandSender, Unknown_number);
                            return true;
                        }

                    case "open-vault":
                        if (!hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        if(args.length == 3){
                            sendMessages(commandSender, Open_vault_num_error);
                            return true;
                        }
                        if(args.length == 4){
                            Player tm = Bukkit.getPlayerExact(args[1]);
                            if(tm == null){
                                sendMessages(commandSender,Open_vault_player_error);
                                return true;
                            }
                            if(!NodeUtil.isNum(args[3])){
                                sendMessages(commandSender, Open_vault_num_error);
                                return true;
                            }
                            if(args[2].equalsIgnoreCase("large")){
                                vaultManager.openVaultIgnorePerms(tm,1,Integer.parseInt(args[3]),commandSender);
                                return true;
                            } else if(args[2].equalsIgnoreCase("medium")){
                                vaultManager.openVaultIgnorePerms(tm,2,Integer.parseInt(args[3]),commandSender);
                                return true;
                            } else {
                                sendMessages(commandSender, Open_vault_type_error);
                                return true;
                            }
                        } else {
                            sendMessages(commandSender, Unknown_number);
                            return true;
                        }

                    case "give":
                        if (!hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        if(args.length >= 3){
                            int amount = 1;
                            Player target = Bukkit.getServer().getPlayerExact(args[1]);
                            if(target == null){
                                sendMessages(commandSender,Console_give_voucher_player_error);
                                return true;
                            }
                            if(args.length == 4 && !args[3].isEmpty() && isNum(args[3])){
                                amount = Integer.parseInt(args[3]);
                            }
                            if(args[2].equalsIgnoreCase("large")){
                                NodeUtil.sendLargeVoucher(target,amount);
                            } else if(args[2].equalsIgnoreCase("medium")){
                                NodeUtil.sendMediumVoucher(target,amount);
                            } else {
                                sendMessages(commandSender, Unknown_number);
                                return true;
                            }
                            sendMessages(commandSender,Voucher_give,"%player%",target.getName());
                            return true;
                        } else {
                            sendMessages(commandSender, Unknown_number);
                        }
                        return true;

                    case "vault":
                        if (!hasPermission(commandSender, PermissionEnums.VAULT_GIVE) && !hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                            sendMessages(commandSender, No_permission_command);
                            return true;
                        }
                        if(args.length == 4){
                            sendMessages(commandSender,Check_player_error);
                            return true;
                        }
                        switch (args[1].toLowerCase()){

                            case "give":
                                if(args.length == 3 || !isNum(args[3])){
                                    sendMessages(commandSender,Vault_command_give_num_null);
                                    return true;
                                }
                                if(Bukkit.getPlayerExact(args[4]) == null){
                                    sendMessages(commandSender,Console_give_voucher_player_error);
                                    return true;
                                }
                                long day = 0;
                                if(args.length == 6 && isNum(args[5])){
                                    day = Long.parseLong(args[5]);
                                }
                                long result = day;
                                if(args[2].equalsIgnoreCase("large")){
                                    scheduler.scheduling().asyncScheduler().runDelayed(task -> {
                                        permissionManager.giveCertainPermission(Bukkit.getPlayerExact(args[4]),1,Integer.parseInt(args[3]),result,commandSender);
                                    },Duration.ofMillis(500));
                                    return true;
                                } else if(args[2].equalsIgnoreCase("medium")){
                                    scheduler.scheduling().asyncScheduler().runDelayed(task -> {
                                        permissionManager.giveCertainPermission(Bukkit.getPlayerExact(args[4]),2,Integer.parseInt(args[3]),result,commandSender);
                                    },Duration.ofMillis(500));
                                    return true;
                                } else {
                                    sendMessages(commandSender, Unknown_number);
                                    return true;
                                }


                            case "append":
                                if(args.length == 3 || !isNum(args[3])){
                                    sendMessages(commandSender,Vault_command_append_num_null);
                                    return true;
                                }
                                if(Bukkit.getPlayerExact(args[4]) == null){
                                    sendMessages(commandSender,Console_give_voucher_player_error);
                                    return true;
                                }
                                long day2 = 0;
                                boolean ignore = false;
                                if(args.length >= 6 && isNum(args[5])){
                                    day2 = Long.parseLong(args[5]);
                                }
                                if(args.length == 7){
                                    if(args[6].equalsIgnoreCase("true")){
                                        ignore = true;
                                    }
                                }
                                long re1 = day2;
                                boolean i1 = ignore;
                                if(args[2].equalsIgnoreCase("large")){
                                    scheduler.scheduling().asyncScheduler().runDelayed(() -> {
                                        permissionManager.appendPermissions(Bukkit.getPlayerExact(args[4]),1,Integer.parseInt(args[3]),re1,commandSender,i1);
                                    }, Duration.ofMillis(500));
                                    return true;
                                } else if(args[2].equalsIgnoreCase("medium")){
                                    scheduler.scheduling().asyncScheduler().runDelayed(() -> {
                                        permissionManager.appendPermissions(Bukkit.getPlayerExact(args[4]),2,Integer.parseInt(args[3]),re1,commandSender,i1);
                                    }, Duration.ofMillis(500));
                                    return true;
                                } else {
                                    sendMessages(commandSender, Unknown_number);
                                    return true;
                                }

                            default:
                                sendMessages(commandSender, Unknown_number);
                                return true;

                        }

                    default:
                        sendMessages(commandSender, Unknown_number);
                        return true;
                }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> Commands = new ArrayList<>(Arrays.asList("reload","check","give"));
        List<String> Admin = new ArrayList<>(Arrays.asList("reload","check","give","vault","open-main","open-vault","pickup","help","alter-name"));

        List<String> list = new ArrayList<>();
        switch(args.length){

            case 1:
                if(!commandSender.isOp()){
                    for (String cmd : Commands) {
                        if (!commandSender.hasPermission("playerinv." + cmd)) continue;

                        list.add(cmd);
                    }

                    if (commandSender.hasPermission("playerinv.check")) list.add("check-share");

                    if (commandSender.hasPermission("playerinv.pickup.toggle")) list.add("pickup");

                    if (commandSender.hasPermission("playerinv.invopen")) list.add("open");

                    if (commandSender.hasPermission("playerinv.command.main.open")) list.add("open-main");

                    if (commandSender.hasPermission("playerinv.command.vault.open")) list.add("open-vault");

                    if (commandSender.hasPermission("playerinv.vault.give") || commandSender.hasPermission("playerinv.vault.append")) list.add("vault");

                    if (commandSender.hasPermission("playerinv.alter.name")) list.add("alter-name");

                    list.add("help");
                } else {
                    list = Admin;
                }
                return StringUtil.copyPartialMatches(args[0].toLowerCase(), list, new ArrayList<String>());

            case 2:
                List<String> players = new ArrayList<>();
                switch(args[0].toLowerCase()){

                    case "check":
                        if (!commandSender.hasPermission("playerinv.check")) return Collections.emptyList();

                        Bukkit.getOnlinePlayers().forEach(target -> {players.add(target.getName());});
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), players, new ArrayList<String>());

                    case "give":

                        if (!commandSender.hasPermission("playerinv.give")) return Collections.emptyList();

                        Bukkit.getOnlinePlayers().forEach(target -> {players.add(target.getName());});
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), players, new ArrayList<String>());

                    case "open-main":

                        if (!commandSender.hasPermission("playerinv.command.main.open")) return Collections.emptyList();

                        Bukkit.getOnlinePlayers().forEach(target -> {players.add(target.getName());});
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), players, new ArrayList<String>());

                    case "open-vault":

                        if (!commandSender.hasPermission("playerinv.command.vault.open")) return Collections.emptyList();

                        Bukkit.getOnlinePlayers().forEach(target -> {players.add(target.getName());});
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), players, new ArrayList<String>());

                    case "pickup":

                        if (!commandSender.hasPermission("playerinv.pickup.toggle")) return Collections.emptyList();

                        list.add("toggle");
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), list, new ArrayList<String>());

                    case "open":

                        if (!commandSender.hasPermission("playerinv.invopen")) return Collections.emptyList();

                        list.add("large");
                        list.add("medium");
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), list, new ArrayList<String>());

                    case "vault":

                        if (!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append")) return Collections.emptyList();

                        if (commandSender.hasPermission("playerinv.vault.give")) list.add("give");

                        if (commandSender.hasPermission("playerinv.vault.append")) list.add("append");

                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), list, new ArrayList<String>());

                    case "alter-name":

                        if (!commandSender.hasPermission("playerinv.alter.name")) return Collections.emptyList();

                        list.add("large");
                        list.add("medium");
                        return StringUtil.copyPartialMatches(args[1].toLowerCase(), list, new ArrayList<String>());
                }

            case 3:
                List<String> players3 = new ArrayList<>();
                switch(args[0].toLowerCase()){

                    case "alter-name":

                        if (!commandSender.hasPermission("playerinv.alter.name")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        if(args[1].equalsIgnoreCase("large")) {
                            list.addAll(this.getLargeAmounts());
                            return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());
                        } else if(args[1].equalsIgnoreCase("medium")) {
                            list.addAll(this.getMediumAmounts());
                            return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());
                        } else {
                            return Collections.emptyList();
                        }


                    case "give":

                        if (!commandSender.hasPermission("playerinv.give")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        list.add("large");
                        list.add("medium");
                        return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());

                    case "open":

                        if (!commandSender.hasPermission("playerinv.invopen")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        if(args[1].equalsIgnoreCase("large")) {
                            list.addAll(this.getLargeAmounts());
                            return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());
                        } else if(args[1].equalsIgnoreCase("medium")) {
                            list.addAll(this.getMediumAmounts());
                            return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());
                        } else {
                            return Collections.emptyList();
                        }

                    case "open-vault":

                        if (!commandSender.hasPermission("playerinv.command.vault.open")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        list.add("large");
                        list.add("medium");
                        return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());

                    case "vault":

                        if (!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        if(args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("append")){
                            list.add("large");
                            list.add("medium");
                            return StringUtil.copyPartialMatches(args[2].toLowerCase(), list, new ArrayList<String>());
                        } else {
                            return Collections.emptyList();
                        }
                }

            case 4:

                switch(args[0].toLowerCase()){

                    case "vault":

                        if (!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        if(args[2].equalsIgnoreCase("large")) {
                            list.addAll(this.getLargeAmounts());
                            return StringUtil.copyPartialMatches(args[3].toLowerCase(), list, new ArrayList<String>());
                        } else if(args[2].equalsIgnoreCase("medium")) {
                            list.addAll(this.getMediumAmounts());
                            return StringUtil.copyPartialMatches(args[3].toLowerCase(), list, new ArrayList<String>());
                        } else {
                            return Collections.emptyList();
                        }

                    case "open-vault":

                        if (!commandSender.hasPermission("playerinv.command.vault.open")) return Collections.emptyList();

                        if(args[1].isEmpty()) return Collections.emptyList();

                        if(args[2].equalsIgnoreCase("large")) {
                            list.addAll(this.getLargeAmounts());
                            return StringUtil.copyPartialMatches(args[3].toLowerCase(), list, new ArrayList<String>());
                        } else if(args[2].equalsIgnoreCase("medium")) {
                            list.addAll(this.getMediumAmounts());
                            return StringUtil.copyPartialMatches(args[3].toLowerCase(), list, new ArrayList<String>());
                        } else {
                            return Collections.emptyList();
                        }
                }

            case 5:

                List<String> players4 = new ArrayList<>();
                switch(args[0].toLowerCase()) {

                    case "vault":

                        if (!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append"))
                            return Collections.emptyList();

                        if (args[1].isEmpty()) return Collections.emptyList();

                        if (args[2].isEmpty()) return Collections.emptyList();

                        if (args[3].isEmpty()) return Collections.emptyList();

                        Bukkit.getOnlinePlayers().forEach(target -> {
                            players4.add(target.getName());
                        });
                        return StringUtil.copyPartialMatches(args[4].toLowerCase(), players4, new ArrayList<String>());

                }

            case 6:

                switch(args[0].toLowerCase()) {

                    case "vault":

                        if (!commandSender.hasPermission("playerinv.vault.give") && !commandSender.hasPermission("playerinv.vault.append"))
                            return Collections.emptyList();

                        if (args[1].isEmpty()) return Collections.emptyList();

                        if (args[2].isEmpty()) return Collections.emptyList();

                        if (args[3].isEmpty()) return Collections.emptyList();

                        if (args[4].isEmpty()) return Collections.emptyList();

                        list.add("(Days)");
                        return StringUtil.copyPartialMatches(args[5].toLowerCase(), list, new ArrayList<String>());
                }
        }
        return Collections.emptyList();
    }


    private LinkedList<String> getLargeAmounts(){
        LinkedList<String> list = new LinkedList<>();
        for(int i=1;i<=largeVaultAmount;i++){
            list.add(String.valueOf(i));
        }
        return list;
    }

    private LinkedList<String> getMediumAmounts(){
        LinkedList<String> list = new LinkedList<>();
        for(int i=1;i<=mediumVaultAmount;i++){
            list.add(String.valueOf(i));
        }
        return list;
    }
}
