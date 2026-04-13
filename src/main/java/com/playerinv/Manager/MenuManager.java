package com.playerinv.Manager;

import com.playerinv.Enums.LocaleEnums;
import com.playerinv.GUI.*;
import com.playerinv.Util.JDBCUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static com.playerinv.PlayerInv.soundManager;
import static com.playerinv.Util.LoadUtil.openMainMenu;
import static com.playerinv.Util.LocaleUtil.sendMessages;

public class MenuManager {

    private CheckMenuStruct checkMenuStruct;
    public CheckMenuStruct getCheckMenu() {
        return checkMenuStruct;
    }
    public int getCheckMenuMaxPage(){
        return checkMenuStruct.getMax_page();
    }

    private VaultMenuStruct vaultMenuStruct;

    public void structAll(){
        checkMenuStruct = new CheckMenuStruct();
        checkMenuStruct.structCheckMenu();
        vaultMenuStruct = new VaultMenuStruct();
        vaultMenuStruct.structVaultMenu();
    }

    private boolean isConnected(){
        return JDBCUtil.isConnected();
    }

    public void openCheckMenu(Player checker, OfflinePlayer target){
        if(!this.isConnected()){
            sendMessages(checker,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        checker.openInventory(checkMenuStruct.buildCheckMenu(target));
    }

    public void openVaultMenu(Player target, String file_name){
        if(!this.isConnected()){
            sendMessages(target,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        this.openVaultMenu(target, file_name,true);
    }
    public void openVaultMenu(Player target, String file_name,boolean sound){
        if(file_name != null){
            vaultMenuStruct.buildVaultMenu(target, file_name);
            if(sound){
                soundManager.menuSwitch(target);
            }
            return;
        }
        vaultMenuStruct.buildVaultMenu(target,openMainMenu);
        if(sound){
            soundManager.menuOpen(target);
        }
    }
}
