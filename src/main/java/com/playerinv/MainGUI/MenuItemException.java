package com.playerinv.MainGUI;


import org.bukkit.Bukkit;

import java.io.File;

public class MenuItemException {
    public static void invalidMaterial(File file, String k){
        String file_path = file.getName();
        Bukkit.getServer().getConsoleSender().sendMessage(file_path + " contains an invalid item material:" + k);
    }

    public static void invalidMaterial_Other(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + " contains an invalid item material! Disabled this items...");
    }

    public static void invalidMix(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + " contains close-menu and open-menu! Disabled this items...");
    }

    public static void invalidVaultAmount(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + " is an invalid vault id! Disabled this items...");
    }

    public static void invalidSlot(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + "'s slot is not exist! Disabled this items...");
    }

    public static void invalidMenu(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + "menu is not exist! Disabled this items...");
    }

    public static void overloadAmount(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + "'s amount is over 64! Disabled this items...");
    }

    public static void unknownVaultNum(String path){
        Bukkit.getServer().getConsoleSender().sendMessage(path + " detected a unknown vault number! Disabled this items...");
    }
}
