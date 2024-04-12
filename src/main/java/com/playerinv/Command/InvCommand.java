package com.playerinv.Command;

import com.playerinv.MainGUI.GUICheck;
import com.playerinv.MainGUI.GUICheckOffline;
import com.playerinv.MainGUI.GUIManager;
import com.playerinv.PermItem.PermItem;
import com.playerinv.PlayerInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

import static com.playerinv.PlayerInv.plugin;

public class InvCommand implements CommandExecutor , TabExecutor {

    public static Player checktarget;

    public static OfflinePlayer offlinechecktarget;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        String nocommand = plugin.getConfig().getString("NoPermissionMessage.Command");
        String nokeyscommand = plugin.getConfig().getString("UnableMessage.Command");
        String nofunction = plugin.getConfig().getString("UnableMessage.Keys");
        Boolean KeysOpen = plugin.getConfig().getBoolean("KeysOpen");
        Boolean accessToggle = plugin.getConfig().getBoolean("OpenToggle");
        if(args.length == 1 && args[0].equals("help") && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.isOp() || player.hasPermission("playerinv.admin")){
                commandSender.sendMessage("§e--------§f[§ePlayerInv§f]§e--------");
                commandSender.sendMessage("§f/PlayerInv §e打开个人仓库主GUI");
                commandSender.sendMessage("§f/PlayerInv Keys Toggle §e开/关F键打开仓库菜单");
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

        if(args.length == 1 && args[0].equals("reload") && (commandSender instanceof Player)) {
            String prefix = plugin.getConfig().getString("Prefix");
            Player player = (Player) commandSender;
            if (player.isOp() || player.hasPermission("playerinv.admin")) {
                plugin.reloadConfig();
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " &e插件重载成功"));
                return true;
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            }
        }

        if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && (commandSender instanceof Player)) {
            String prefix = plugin.getConfig().getString("Prefix");
            Player player = (Player) commandSender;
            if (player.hasPermission("playerinv.keys.toggle") && (accessToggle) && (KeysOpen)) {
                ToggleF(player);
                return true;
            } else if(player.hasPermission("playerinv.keys.toggle") && !(accessToggle)){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nokeyscommand));
            } else if(!(KeysOpen)){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nofunction));
            } else if(!(player.hasPermission("playerinv.keys.toggle"))){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            }
        } else if(args.length == 2 && args[0].equals("keys") && args[1].equals("toggle") && !(commandSender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.RED + "该命令仅玩家可用.");
        }

        if(args.length == 1 && args[0].equals("reload")) {
            if (!(commandSender instanceof Player)) {
                plugin.reloadConfig();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "插件已重载");
                return true;
            }
        }

        String prefix = plugin.getConfig().getString("Prefix");
        String openmessage = plugin.getConfig().getString("OpenMessage");
        Boolean openaccess = plugin.getConfig().getBoolean("OpenGUIMessage");
        if(args.length == 0 && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.gui.open")){
                if(openaccess) {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + openmessage));
                }
                player.openInventory(GUIManager.MainGUI(commandSender));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1,2);
            }

            return true;
        }

        if((args.length == 0 && !(commandSender instanceof Player)) || (args.length == 1 && args[0].equals("help") && !(commandSender instanceof Player))){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.RED + "该命令仅玩家可用");

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
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c无法找到该玩家."));
                    }
                    if(tp.hasPlayedBefore()){
                        offlinechecktarget = tp;
                        player.openInventory(GUICheckOffline.CheckGUIOffline(commandSender));
                    }
                } else if(Bukkit.getServer().getPlayerExact(targetp) != null){
                    Player targetonline = Bukkit.getServer().getPlayerExact(targetp);
                    checktarget = targetonline;
                    player.openInventory(GUICheck.CheckGUI(commandSender));
                }
            } else if(!player.hasPermission("playerinv.check")){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            }
        }


        if(args.length == 1 && args[0].equals("check") && args[1].isEmpty() && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(!player.hasPermission("playerinv.check")){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            }
            if(player.hasPermission("playerinv.check") || player.hasPermission("playerinv.admin") || player.isOp()){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c请输入正确的玩家名称."));;
            }
        }

        if(args.length >= 1 && args[0].equals("check") && !(commandSender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.RED + "该命令仅游戏内可用");
        }


        if(args.length == 3 && args[0].equals("give") && (commandSender instanceof Player)){
            Player player = (Player) commandSender;
            if(player.hasPermission("playerinv.give") || player.hasPermission("playerinv.admin") || player.isOp()){
                String target = args[1];
                if(Bukkit.getServer().getPlayerExact(target) == null){
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &c玩家未在线."));
                } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("large")){
                    Player targetonline = Bukkit.getServer().getPlayerExact(target);
                    PermItem.linvitem(targetonline);
                    String givemessage = plugin.getConfig().getString("ItemMessage.Give").replaceAll("%player%", targetonline.getName());
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + givemessage));
                } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("medium")){
                    Player targetonline = Bukkit.getServer().getPlayerExact(target);
                    PermItem.sinvitem(targetonline);
                    String givemessage = plugin.getConfig().getString("ItemMessage.Give").replaceAll("%player%", targetonline.getName());
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + givemessage));
                }
            } else if(!player.hasPermission("playerinv.give")){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + nocommand));
            }
        }

        if(args.length == 3 && args[0].equals("give") && !(commandSender instanceof Player)){
            String target = args[1];
            if(Bukkit.getServer().getPlayerExact(target) == null){
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.RED + "玩家未在线.");
            } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("large")){
                Player targetonline = Bukkit.getServer().getPlayerExact(target);
                PermItem.linvitem(targetonline);
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "成功给予玩家 " + targetonline.getName() + " 大型仓库兑换券.");
                String consolemessage = plugin.getConfig().getString("ConsoleMessage.Give.Large");
                targetonline.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + consolemessage));
            } else if(Bukkit.getServer().getPlayerExact(target) != null && args[2].equals("medium")){
                Player targetonline = Bukkit.getServer().getPlayerExact(target);
                PermItem.sinvitem(targetonline);
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "成功给予玩家 " + targetonline.getName() + " 小型仓库兑换券.");
                String consolemessage = plugin.getConfig().getString("ConsoleMessage.Give.Medium");
                targetonline.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + consolemessage));
            }
        }
        return false;
    }
    public static HashMap<Player, Boolean> pressToggle = new HashMap<Player, Boolean>();

    public void ToggleF(Player player){
        String prefix = plugin.getConfig().getString("Prefix");
        String enablemessage = plugin.getConfig().getString("ToggleMessage.Enabled");
        String disablemessage = plugin.getConfig().getString("ToggleMessage.Disabled");
        if(pressToggle.get(player)){
            pressToggle.put(player, false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + disablemessage));
        } else {
            pressToggle.put(player, true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + enablemessage));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        LinkedList<String> tips = new LinkedList<>();

        if(args.length == 1 && !(player.hasPermission("playerinv.reload") || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.check") || player.hasPermission("playerinv.give"))) {
            List<String> firstArgList = Arrays.asList("keys", "help");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check", "give");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help", "check");
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
            List<String> firstArgList = Arrays.asList("reload", "keys", "help");
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
            List<String> firstArgList = Arrays.asList("check", "keys", "help");
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
        if(args.length == 3 && (player.hasPermission("playerinv.give") || player.hasPermission("playerinv.admin") || player.isOp())){
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
        return tips;
    }
}
