package com.playerinv.util.message;

import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.constants.DependencyConstants;
import com.playerinv.util.VersionUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MessageUtil {

    private static String prefix;

    private static final Pattern MINIMESSAGE_PATTERN = Pattern.compile("<[a-zA-Z0-9#:/]+>");

    private static MiniMessage msgAdapter;

    private static LegacyComponentSerializer sectionAdapter;

    public static String getPrefix(){
        return prefix;
    }

    public static void initMiniMessageSupport(){
        msgAdapter = MiniMessage.miniMessage();
        if(!VersionUtil.isHexColorSupport()){
            sectionAdapter = LegacyComponentSerializer.legacySection();
        } else {
            sectionAdapter = LegacyComponentSerializer.builder().character(LegacyComponentSerializer.SECTION_CHAR).useUnusualXRepeatedCharacterHexFormat().hexColors().build();
        }
    }

    public static void loadPrefix(){
        prefix = PlayerInv.getInstance().getConfig().getString("Prefix");
    }

    public static void sendMsgToChatBar(Player player,String msg){
        player.sendMessage(parseString(prefix + msg));
    }

    public static void sendMsgToActionBar(Player player,String msg){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(parseString(msg,player)));
    }

    public static void sendMsgToConsole(String msg){
        Bukkit.getConsoleSender().sendMessage(parseString(prefix + msg));
    }

    public static void sendTitle(Player player,String title,String subtitle,String... replace){
        player.sendTitle(parseString(title,player,replace),parseString(subtitle,player,replace),10,60,10);
    }

    public static void sendDebug(String message){
        if(ConfigConstants.DEBUG_MODE){
            Bukkit.getConsoleSender().sendMessage("[PlayerInv/DEBUG-INFO] " + message);
        }
    }

    public static void sendDetail(String msg){
        if(ConfigConstants.DETAIL_MODE){
            Bukkit.getConsoleSender().sendMessage("[PlayerInv/PLAYER-INFO] " + msg);
        }
    }

    private static String parse(String s){
        if(MINIMESSAGE_PATTERN.matcher(s).find()){
            Component mm = msgAdapter.deserialize(s);
            s = sectionAdapter.serialize(mm);
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String parseString(String s,String... replace){
        s = replaceMessageVariable(s,replace);
        return parse(s);
    }

    public static String parseString(String s,Player player,String... replace){
        s = replaceMessageVariable(s,replace);
        if(DependencyConstants.PAPI_ENABLED && player != null){
            s = PlaceholderAPI.setPlaceholders(player,s);
        }
        return parse(s);
    }

    public static List<String> parseLore(List<String> lore,String... replace){
        List<String> s = new ArrayList<>();
        for(String l : lore){
            if(l == null) continue;
            s.add(MessageUtil.parseString(l,replace));
        }
        return s;
    }

    public static List<String> parseLore(List<String> lore,Player player,String... replace){
        List<String> s = new ArrayList<>();
        for(String l : lore){
            if(l == null) continue;
            s.add(MessageUtil.parseString(l,player,replace));
        }
        return s;
    }

    public static String replaceMessageVariable(String msg,String... replace){
        if(replace.length == 0) return msg;
        int count = replace.length / 2;
        for(int i=0;i<count;i++){
            msg = msg.replace(replace[2 * i],replace[(2*i)+1]);
        }
        return msg;
    }

    public static String parseToLegacyColor(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
