package com.playerinv.enums;

public enum LocaleEnums {

    No_permission_command("Messages.No_permission_command"),

    No_permission_vault("Messages.No_permission_vault"),

    Vault_blacklist_items("Messages.Vault_blacklist_items"),

    Voucher_full("Messages.Voucher_full"),

    Voucher_give("Messages.Voucher_give"),

    Voucher_use_large("Messages.Voucher_use_large"),

    Voucher_use_medium("Messages.Voucher_use_medium"),

    Voucher_cannot_use("Messages.Voucher_cannot_use"),

    Check_player_error("Messages.Check_player_error"),

    Check_unknown_player("Messages.Check_unknown_player"),

    Not_console_command("Messages.Not_console_command"),

    Console_give_voucher_player_error("Messages.Console_give_voucher_player_error"),

    Command_reload("Messages.Command_reload"),

    Unknown_number("Messages.Unknown_number"),

    Cache_broken("Messages.Cache_broken"),

    Vault_command_give_large_success("Messages.Vault_command_give_large_success"),

    Vault_command_give_medium_success("Messages.Vault_command_give_medium_success"),

    Vault_command_give_already_has("Messages.Vault_command_give_already_has"),

    Vault_command_give_num_null("Messages.Vault_command_give_num_null"),

    Vault_command_append_large_success("Messages.Vault_command_append_large_success"),

    Vault_command_append_medium_success("Messages.Vault_command_append_medium_success"),

    Vault_command_append_num_null("Messages.Vault_command_append_num_null"),

    Vault_player_give_large_notice("Messages.Vault_player_give_large_notice"),

    Vault_player_give_medium_notice("Messages.Vault_player_give_medium_notice"),

    Vault_player_append_large_notice("Messages.Vault_player_append_large_notice"),

    Vault_player_append_medium_notice("Messages.Vault_player_append_medium_notice"),

    Vault_command_append_full("Messages.Vault_command_append_full"),

    Player_death_store_full("Messages.Player_death_store_full"),

    Player_death_store_notice("Messages.Player_death_store_notice"),

    Console_open_main("Messages.Console_open_main"),

    Vault_already_open("Messages.Vault_already_open"),

    Open_vault_player_error("Messages.Open-vault_player_error"),

    Open_vault_type_error("Messages.Open-vault_type_error"),

    Open_vault_num_error("Messages.Open-vault_num_error"),

    Open_vault_large_success("Messages.Open-vault_large_success"),

    Open_vault_medium_success("Messages.Open-vault_medium_success"),

    Auto_pickup_large_notice("Messages.Auto_pickup_large_notice"),

    Auto_pickup_medium_notice("Messages.Auto_pickup_medium_notice"),

    Auto_pickup_enabled("Messages.Auto_pickup_enabled"),

    Auto_pickup_disabled("Messages.Auto_pickup_disabled"),

    Auto_pickup_vault_null("Messages.Auto_pickup_vault_null"),

    Auto_pickup_vault_full("Messages.Auto_pickup_vault_full"),

    Vault_string_destroy("Messages.Vault_string_destroy"),

    Vault_item_destroy("Messages.Vault_item_destroy"),

    Translator_not_support_language("Messages.Translator_not_support_language"),

    Vault_attribute_change_disabled("Messages.Vault_attribute_change_disabled"),

    Vault_attribute_change_notice("Messages.Vault_attribute_change_notice"),

    Vault_attribute_change_large_success("Messages.Vault_attribute_change_large_success"),

    Vault_attribute_change_medium_success("Messages.Vault_attribute_change_medium_success"),

    Vault_item_destroy_cancel_open("Messages.Vault_item_destroy_cancel_open"),

    Check_vault_error_exist("Messages.Check_vault_error_exist"),

    Check_vault_set_save("Messages.Check_vault_set_save"),

    Check_vault_no_save("Messages.Check_vault_no_save"),

    Cache_not_ready("Messages.Cache_not_ready"),

    Connection_valid("Messages.Connection_valid"),

    Connection_not_valid("Messages.Connection_not_valid"),

    Connection_invalid_refuse("Messages.Connection_invalid_refuse"),

    Vault_item_broken_fix("Messages.Vault_item_broken_fix"),

    Vault_item_broken_remove("Messages.Vault_item_broken_remove"),

    Vault_item_broken_deny("Messages.Vault_item_broken_deny"),

    Vault_item_retry("Messages.Vault_item_retry"),

    Vault_item_retry_cooldown("Messages.Vault_item_retry_cooldown");

    private String desc;

    private LocaleEnums(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
