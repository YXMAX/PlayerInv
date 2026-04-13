package com.playerinv.Util;

import com.playerinv.Enums.LocaleEnums;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.playerinv.PlayerInv.localeConfig;
import static com.playerinv.PlayerInv.vaultAttributesManager;
import static com.playerinv.Util.LoadUtil.prefix;
import static com.playerinv.Util.NodeUtil.*;

public class LocaleUtil {

    public static String getLocaleMessages(LocaleEnums enums){
        return localeConfig.getString(enums.getDesc());
    }

    public static void sendMessages(CommandSender commandSender, LocaleEnums enums, String ...replace){
        if(commandSender == null){
            return;
        }
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i< count;i++){
            msg = msg.replaceAll(replace[2 * i],replace[(2*i)+1]);
        }
        if(isConsole(commandSender)){
            sendConsole(color(msg));
        } else {
            commandSender.sendMessage(color(prefix + msg));
        }
    }

    public static void sendMessages(Player player, LocaleEnums enums, String ...replace){
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i< count;i++){
            msg = msg.replaceAll(replace[2 * i],replace[(2*i)+1]);
        }
        player.sendMessage(color(prefix + msg));
    }

    public static void sendActionBar(Player player, LocaleEnums enums, String ...replace){
        String msg = LocaleUtil.getLocaleMessages(enums);
        int count = replace.length / 2;
        for(int i=0;i< count;i++){
            msg = msg.replaceAll(replace[2 * i],replace[(2*i)+1]);
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color(msg)));
    }

    public static String Locale_Voucher_Large_DisplayName(){
        return localeConfig.getString("Voucher.large.display-name");
    }

    public static List<String> Locale_Voucher_Large_Lore(){
        return localeConfig.getStringList("Voucher.large.lore");
    }

    public static String Locale_Voucher_Large_Title(int num){
        return localeConfig.getString("Voucher.large.title").replaceAll("%large_vault_number%", String.valueOf(num));
    }

    public static String Locale_Voucher_Large_Subtitle(int num){
        return localeConfig.getString("Voucher.large.subtitle").replaceAll("%large_vault_number%", String.valueOf(num));
    }

    public static String Locale_Voucher_Medium_DisplayName(){
        return localeConfig.getString("Voucher.medium.display-name");
    }

    public static List<String> Locale_Voucher_Medium_Lore(){
        return localeConfig.getStringList("Voucher.medium.lore");
    }

    public static String Locale_Voucher_Medium_Title(int num){
        return localeConfig.getString("Voucher.medium.title").replaceAll("%medium_vault_number%", String.valueOf(num));
    }

    public static String Locale_Voucher_Medium_Subtitle(int num) {
        return localeConfig.getString("Voucher.medium.subtitle").replaceAll("%medium_vault_number%", String.valueOf(num));
    }

    public static String Sharing_vault_permissions(int type){
        switch(type){
            case 1:
                return localeConfig.getString("Sharing_vault_permission.Low");
            case 2:
                return localeConfig.getString("Sharing_vault_permission.Medium");
            case 3:
                return localeConfig.getString("Sharing_vault_permission.High");
            case 4:
                return localeConfig.getString("Sharing_vault_permission.Max");
            default:
                return localeConfig.getString("Sharing_vault_permission.Max");
        }
    }

    public static List<String> invite_display_messages(){
        return localeConfig.getStringList("Invite_display.Messages");
    }

    public static String invite_display_accept_button(){
        return localeConfig.getString("Invite_display.Accept_button");
    }

    public static String invite_display_accept_show_text(){
        return localeConfig.getString("Invite_display.Accept_show_text");
    }

    public static String invite_display_deny_button(){
        return localeConfig.getString("Invite_display.Deny_button");
    }

    public static String invite_display_deny_show_text(){
        return localeConfig.getString("Invite_display.Deny_show_text");
    }

    /**
     *
     * @param num
     * 1: 永久
     * 2: 限时
     * 3: 未拥有
     * 4: 不足一天
     * @return
     */
    public static String placeholder_expiry(int num){
        switch (num){
            case 1:
                return localeConfig.getString("Placeholder_expiry.Permanent");
            case 2:
                return localeConfig.getString("Placeholder_expiry.Expiry");
            case 3:
                return localeConfig.getString("Placeholder_expiry.Not_exist");
            case 4:
                return localeConfig.getString("Placeholder_expiry.Day_less");
        }
        return localeConfig.getString("Placeholder_expiry.Permanent");
    }

    public static String Share_vault_title(){
        return localeConfig.getString("Vault.Share_title");
    }

    public static String expiry_adjust(long day){
        String expiry = null;
        if(day == 0){
            expiry = localeConfig.getString("Expiry.Permanent");
        } else {
            expiry = localeConfig.getString("Expiry.Temporary").replaceAll("%day_value%", String.valueOf(day));
        }
        return expiry;
    }

    public static void sendAutoPickupNotice(int type, Player target, ItemStack item, int total, int vault_num,boolean addPrefix){
        if(type == 1){
            autoPickupLargeNoticeComponents(target, item, total, vault_num, addPrefix);
        } else {
            autoPickupMediumNoticeComponents(target, item, total, vault_num, addPrefix);
        }
    }

    public static void autoPickupLargeNoticeComponents(Player target, ItemStack item, int total, int vault_num,boolean addPrefix){
        String v = localeConfig.getString("Messages.Auto_pickup_large_notice")
                .replaceAll("%vault_num%", String.valueOf(vault_num))
                .replaceAll("%item_amount%", String.valueOf(total))
                .replaceAll("%vault_name%", vaultAttributesManager.replaceVaultName(target,1,vault_num));
        if(addPrefix){
            v = prefix + v;
        }
        TranslatableComponent component = new TranslatableComponent(checkItemName(target,item));
        String[] split = v.split("%item%");
        BaseComponent[] baseComponent2 = TextComponent.fromLegacyText(color(split[1]));
        BaseComponent[] baseComponent1 = TextComponent.fromLegacyText(color(split[0]));
        LinkedList<BaseComponent> list = new LinkedList<>(Arrays.asList(baseComponent1));
        list.add(component);
        list.addAll(Arrays.asList(baseComponent2));
        if(addPrefix){
            target.spigot().sendMessage(component);
        } else {
            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, list.toArray(new BaseComponent[0]));
        }
    }

    public static void autoPickupMediumNoticeComponents(Player target, ItemStack item, int total, int vault_num,boolean addPrefix){
        String v = localeConfig.getString("Messages.Auto_pickup_medium_notice")
                .replaceAll("%vault_num%", String.valueOf(vault_num))
                .replaceAll("%item_amount%", String.valueOf(total))
                .replaceAll("%vault_name%", vaultAttributesManager.replaceVaultName(target,2,vault_num));
        if(addPrefix){
            v = prefix + v;
        }
        TranslatableComponent component = new TranslatableComponent(checkItemName(target,item));
        String[] split = v.split("%item%");
        BaseComponent[] baseComponent2 = TextComponent.fromLegacyText(color(split[1]));
        BaseComponent[] baseComponent1 = TextComponent.fromLegacyText(color(split[0]));
        LinkedList<BaseComponent> list = new LinkedList<>(Arrays.asList(baseComponent1));
        list.add(component);
        list.addAll(Arrays.asList(baseComponent2));
        if(addPrefix){
            target.spigot().sendMessage(component);
        } else {
            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, list.toArray(new BaseComponent[0]));
        }
    }
}
