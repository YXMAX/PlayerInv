package com.playerinv;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static com.playerinv.PlayerInv.LocaleConfig;
import static com.playerinv.PluginSet.locale;

public class LocaleUtil {

    public static String Locale_Voucher_Large_DisplayName(){
        String v = LocaleConfig.getString("Voucher.large.display-name");
        return v;
    }

    public static List<String> Locale_Voucher_Large_Lore(){
        List<String> v = LocaleConfig.getStringList("Voucher.large.lore");
        return v;
    }

    public static String Locale_Voucher_Large_Title(int num){
        String v = LocaleConfig.getString("Voucher.large.title").replaceAll("%large_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Locale_Voucher_Large_Subtitle(int num){
        String v = LocaleConfig.getString("Voucher.large.subtitle").replaceAll("%large_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Locale_Voucher_Medium_DisplayName(){
        String v = LocaleConfig.getString("Voucher.medium.display-name");
        return v;
    }

    public static List<String> Locale_Voucher_Medium_Lore(){
        List<String> v = LocaleConfig.getStringList("Voucher.medium.lore");
        return v;
    }

    public static String Voucher_cannot_use(){
        String v = LocaleConfig.getString("Messages.Voucher_cannot_use");
        return v;
    }

    public static String Locale_Voucher_Medium_Title(int num){
        String v = LocaleConfig.getString("Voucher.medium.title").replaceAll("%medium_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Locale_Voucher_Medium_Subtitle(int num) {
        String v = LocaleConfig.getString("Voucher.medium.subtitle").replaceAll("%medium_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Check_Large_Title_Online(Player num){
        String v = LocaleConfig.getString("Check.Player_online.Large_title").replaceAll("%player%", String.valueOf(num.getName()));
        return v;
    }

    public static String Check_Medium_Title_Online(Player num) {
        String v = LocaleConfig.getString("Check.Player_online.Medium_title").replaceAll("%player%", String.valueOf(num.getName()));
        return v;
    }

    public static String Check_Large_Title_Offline(OfflinePlayer num){
        String v = LocaleConfig.getString("Check.Player_offline.Large_title").replaceAll("%player%", String.valueOf(num.getName()));
        return v;
    }

    public static String Check_Medium_Title_Offline(OfflinePlayer num) {
        String v = LocaleConfig.getString("Check.Player_offline.Medium_title").replaceAll("%player%", String.valueOf(num.getName()));
        return v;
    }

    public static String Messages_Open_main_gui(){
        String v = LocaleConfig.getString("Messages.Open_main_gui");
        return v;
    }

    public static String Messages_Toggle_enabled(){
        String v = LocaleConfig.getString("Messages.Toggle_enabled");
        return v;
    }

    public static String Messages_Toggle_disabled(){
        String v = LocaleConfig.getString("Messages.Toggle_disabled");
        return v;
    }

    public static String Messages_Toggle_unable_command(){
        String v = LocaleConfig.getString("Messages.Toggle_unable_command");
        return v;
    }

    public static String Messages_Toggle_unable_keys(){
        String v = LocaleConfig.getString("Messages.Toggle_unable_keys");
        return v;
    }

    public static String Messages_No_permission_command(){
        String v = LocaleConfig.getString("Messages.No_permission_command");
        return v;
    }

    public static String Messages_No_permission_vault(){
        String v = LocaleConfig.getString("Messages.No_permission_vault");
        return v;
    }

    public static String Messages_Vault_blacklist_items(){
        String v = LocaleConfig.getString("Messages.Vault_blacklist_items");
        return v;
    }

    public static String Messages_Voucher_full(){
        String v = LocaleConfig.getString("Messages.Voucher_full");
        return v;
    }

    public static String Messages_Voucher_give(Player player){
        String v = LocaleConfig.getString("Messages.Voucher_give").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Messages_Voucher_use_large(int num){
        String v = LocaleConfig.getString("Messages.Voucher_use_large").replaceAll("%large_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Messages_Voucher_use_medium(int num){
        String v = LocaleConfig.getString("Messages.Voucher_use_medium").replaceAll("%medium_vault_number%", String.valueOf(num));
        return v;
    }

    public static String Command_reload(){
        String v = LocaleConfig.getString("Messages.Command_reload");
        return v;
    }

    public static String Messages_Check_unknown_player(){
        String v = LocaleConfig.getString("Messages.Check_unknown_player");
        return v;
    }

    public static String Messages_Check_Player_Error(){
        String v = LocaleConfig.getString("Messages.Check_player_error");
        return v;
    }


    public static String Messages_Not_Console_Command(){
        String v = LocaleConfig.getString("Messages.Not_console_command");
        return v;
    }

    public static String Messages_Console_give_voucher_player_error(){
        String v = LocaleConfig.getString("Messages.Console_give_voucher_player_error");
        return v;
    }

    public static String Messages_Unknown_number(){
        String v = LocaleConfig.getString("Messages.Unknown_number");
        return v;
    }

    public static String Messages_Console_give_voucher_large(Player player){
        String v = LocaleConfig.getString("Messages.Console_give_voucher_large").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Messages_Console_give_voucher_medium(Player player){
        String v = LocaleConfig.getString("Messages.Console_give_voucher_medium").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Messages_Console_give_voucher_large_notice(Player player){
        String v = LocaleConfig.getString("Messages.Console_give_voucher_large_notice").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Messages_Console_give_voucher_medium_notice(Player player){
        String v = LocaleConfig.getString("Messages.Console_give_voucher_medium_notice").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Vault_command_give_large_success(Player player, String num){
        String v = LocaleConfig.getString("Messages.Vault_command_give_large_success").replaceAll("%player%", player.getName());
        return v.replaceAll("%vault_num%", String.valueOf(num));
    }

    public static String Vault_command_give_medium_success(Player player, String num){
        String v = LocaleConfig.getString("Messages.Vault_command_give_medium_success").replaceAll("%player%", player.getName());
        return v.replaceAll("%vault_num%", String.valueOf(num));
    }

    public static String Vault_command_give_already_has(Player player){
        String v = LocaleConfig.getString("Messages.Vault_command_give_already_has").replaceAll("%player%", player.getName());
        return v;
    }

    public static String Vault_command_append_large_success(Player player, int before ,String num){
        String v = LocaleConfig.getString("Messages.Vault_command_append_large_success").replaceAll("%player%", player.getName());
        return v.replaceAll("%vault_amount%", String.valueOf(num)).replaceAll("%before_append%", String.valueOf(before));
    }

    public static String Vault_command_append_medium_success(Player player, int before,String num){
        String v = LocaleConfig.getString("Messages.Vault_command_append_medium_success").replaceAll("%player%", player.getName());
        return v.replaceAll("%vault_amount%", String.valueOf(num)).replaceAll("%before_append%", String.valueOf(before));
    }

}
