package com.playerinv.constants;

import com.playerinv.PlayerInv;
import com.playerinv.gui.action.IconActionCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.playerinv.PlayerInv.getInstance;

public class ConfigConstants {

    public static boolean ONLINE_MODE;

    public static boolean DEBUG_MODE;

    public static boolean DETAIL_MODE;

    public static boolean FUNCTION_ENDERCHEST;

    public static boolean FUNCTION_AUTO_PICKUP;

    public static boolean FUNCTION_DEATH_STORE;

    public static boolean FUNCTION_AUTO_RETURN;

    public static boolean FUNCTION_CHANGE_VAULT_ATTRIBUTES;

    public static boolean MATERIAL_PICKUP_ENABLED;

    public static boolean DEATH_STORE_IGNORE;

    public static boolean ACTIONBAR_AUTO_PICKUP;

    public static boolean RETURN_MENU_BY_COMMAND;

    public static String RETURN_MENU_BY_COMMAND_VALUE;

    public static boolean RETURN_CMD_BY_COMMAND;

    public static IconActionCommand RETURN_CMD_BY_COMMAND_VALUE;

    public static String INDEX_MENU;

    public static boolean ALLOW_CLICK_INVENTORY_IN_MENU;

    public static boolean ALLOW_PICKUP_WHEN_NO_VAULT_PERMISSION;

    private static List<IconActionCommand> SEND_LARGE_VAULT_COMMAND_LIST;

    private static List<IconActionCommand> SEND_MEDIUM_VAULT_COMMAND_LIST;

    public static void reloadConfigConstants(){
        DEBUG_MODE = PlayerInv.getInstance().getConfig().getBoolean("Debug-mode");
        DETAIL_MODE = PlayerInv.getInstance().getConfig().getBoolean("Player_operation_detail_console");
        FUNCTION_ENDERCHEST = PlayerInv.getInstance().getConfig().getBoolean("Function.Enderchest_open");
        FUNCTION_AUTO_PICKUP = PlayerInv.getInstance().getConfig().getBoolean("Inventory.Auto_pickup.enabled");
        FUNCTION_DEATH_STORE = PlayerInv.getInstance().getConfig().getBoolean("Player-death-store.Enabled");
        FUNCTION_CHANGE_VAULT_ATTRIBUTES = PlayerInv.getInstance().getConfig().getBoolean("Vault.Name_change");
        FUNCTION_AUTO_RETURN = PlayerInv.getInstance().getConfig().getBoolean("Function.Auto_return");
        MATERIAL_PICKUP_ENABLED = PlayerInv.getInstance().getConfig().getBoolean("Inventory.Auto_pickup.material_pickup");
        DEATH_STORE_IGNORE = PlayerInv.getInstance().getConfig().getBoolean("Player-death-store.ignore-keepInventory");
        ACTIONBAR_AUTO_PICKUP = PlayerInv.getInstance().getConfig().getBoolean("Inventory.Auto_pickup.action_bar");
        RETURN_MENU_BY_COMMAND = PlayerInv.getInstance().getConfig().getBoolean("Command.Return_menu.Enabled");
        RETURN_MENU_BY_COMMAND_VALUE = PlayerInv.getInstance().getConfig().getString("Command.Return_menu.Menu");
        RETURN_CMD_BY_COMMAND = PlayerInv.getInstance().getConfig().getBoolean("Command.Return_command.Enabled");
        RETURN_CMD_BY_COMMAND_VALUE = new IconActionCommand(PlayerInv.getInstance().getConfig().getString("Command.Return_command.Command"));
        INDEX_MENU = PlayerInv.getInstance().getConfig().getString("Menu.Open_main");
        ALLOW_CLICK_INVENTORY_IN_MENU = PlayerInv.getInstance().getConfig().getBoolean("Allow_click_in_menu");
        ALLOW_PICKUP_WHEN_NO_VAULT_PERMISSION = PlayerInv.getInstance().getConfig().getBoolean("Vault_can_pickup_no_permission");
        boolean bool = PlayerInv.getInstance().getConfig().getBoolean("Send-vault.Enabled");
        if(bool){
            List<String> large_list = getInstance().getConfig().getStringList("Send-vault.Command.Large-vault");
            if(!large_list.isEmpty()){
                SEND_LARGE_VAULT_COMMAND_LIST = new ArrayList<>();
                for(String cmd : large_list){
                    SEND_LARGE_VAULT_COMMAND_LIST.add(new IconActionCommand(cmd,true));
                }
            }
            List<String> medium_list = getInstance().getConfig().getStringList("Send-vault.Command.Medium-vault");
            if(!medium_list.isEmpty()){
                SEND_MEDIUM_VAULT_COMMAND_LIST = new ArrayList<>();
                for(String cmd : medium_list){
                    SEND_MEDIUM_VAULT_COMMAND_LIST.add(new IconActionCommand(cmd,true));
                }
            }
        }
    }

    public static void runCommandBySendVault(int type,Player player,int num){
        if(type == 1){
            if(SEND_LARGE_VAULT_COMMAND_LIST != null){
                for(IconActionCommand command : SEND_LARGE_VAULT_COMMAND_LIST){
                    command.runCommandToPlayer(player,"%vault_num%",String.valueOf(num));
                }
            }
        } else {
            if(SEND_MEDIUM_VAULT_COMMAND_LIST != null){
                for(IconActionCommand command : SEND_MEDIUM_VAULT_COMMAND_LIST){
                    command.runCommandToPlayer(player,"%vault_num%",String.valueOf(num));
                }
            }
        }
    }
}
