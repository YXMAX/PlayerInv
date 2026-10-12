package com.playerinv.gui.adapter;


import com.playerinv.PlayerInv;
import com.playerinv.util.exception.InvalidMenuSlotException;
import com.playerinv.util.exception.InvalidVaultAmountException;
import com.playerinv.util.NodeUtil;
import com.playerinv.util.VersionUtil;
import org.bukkit.Material;

public class MenuValueAdapter {

    public static Material parseMaterial(String file_name, String path, String material){
        material = resetLegacyMaterial(material);
        if(material == null || Material.getMaterial(material) == null){
            NodeUtil.sendMenuError("Invalid item material, reset to default: " + file_name + ".yml/" + path);
            return Material.STONE;
        }
        return Material.getMaterial(material);
    }

    public static int parseMenuSize(String file_name, int size){
        switch(size){
            case 9:
            case 18:
            case 27:
            case 36:
            case 45:
            case 54:
                return size;
            default:
                NodeUtil.sendMenuError("Invalid menu size, reset to default: " + file_name + ".yml");
                return 54;
        }
    }

    public static int parseVaultAmount(int num,int type,String file_name,String path) throws InvalidVaultAmountException {
        if(num <= 0 || num > PlayerInv.getInstance().getVaultManager().getVaultAmount(type)){
            throw new InvalidVaultAmountException(file_name,path);
        }
        return num;
    }

    public static int parseSlot(int num,int menu_size,String file_name,String path) throws InvalidMenuSlotException {
        if(num <= 0 || num > menu_size){
            throw new InvalidMenuSlotException(file_name,path);
        }
        return num;
    }

    public static int parseItemAmount(int num,String file_name,String path){
        if(num <= 0){
            NodeUtil.sendMenuError("Invalid item amount, reset to default: " + file_name + ".yml/" + path);
        }
        return num;
    }

    public static Short parseLegacySubId(String material){
        if(material == null){
            return null;
        }
        if(!VersionUtil.isBelowVer1_13()){
            return null;
        }
        if(material.equalsIgnoreCase("player_head")){
            return 3;
        }
        String new_material = null;
        if(material.toLowerCase().contains("_wool")){
            new_material = material.toUpperCase().replace("_WOOL","");
        } else if(material.toLowerCase().contains("_stained_glass_pane")){
            new_material = material.toUpperCase().replace("_STAINED_GLASS_PANE","");
        } else if(material.toLowerCase().contains("_stained_glass")){
            new_material = material.toUpperCase().replace("_STAINED_GLASS","");
        } else {
            return null;
        }
        switch(new_material){
            case "WHITE":
                return 0;
            case "ORANGE":
                return 1;
            case "MAGENTA":
                return 2;
            case "LIGHT_BLUE":
                return 3;
            case "YELLOW":
                return 4;
            case "LIME":
                return 5;
            case "PINK":
                return 6;
            case "GRAY":
                return 7;
            case "LIGHT_GRAY":
                return 8;
            case "CYAN":
                return 9;
            case "PURPLE":
                return 10;
            case "BLUE":
                return 11;
            case "BROWN":
                return 12;
            case "GREEN":
                return 13;
            case "RED":
                return 14;
            case "BLACK":
                return 15;
            default:
                return null;
        }
    }

    private static String resetLegacyMaterial(String material){
        if(!VersionUtil.isBelowVer1_13()){
            return material;
        }
        if(material.equalsIgnoreCase("player_head")){
            return "SKULL_ITEM";
        }
        if(material.toLowerCase().contains("_wool")){
            return "WOOL";
        } else if(material.toLowerCase().contains("_stained_glass_pane")){
            return "STAINED_GLASS_PANE";
        } else if(material.toLowerCase().contains("_stained_glass")){
            return "STAINED_GLASS";
        } else {
            return material;
        }
    }
}
