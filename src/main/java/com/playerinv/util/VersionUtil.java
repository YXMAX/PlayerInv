package com.playerinv.util;

import org.bukkit.Bukkit;

public class VersionUtil {

    private static boolean Ver1_7;

    private static boolean Ver1_12_2;

    private static boolean Ver1_13;

    private static boolean Ver1_14;

    private static boolean Ver1_16;

    private static boolean Ver1_18;

    private static boolean Ver1_20_5;

    private static boolean Ver1_21;

    public static void checkServerVersion(){
        String v = Bukkit.getBukkitVersion().split("-")[0];
        String[] split = v.split("\\.");
        if(!split[0].equals("1")){
            Ver1_21 = true;
            return;
        }
        switch(split[1]){
            case "6":
            case "7":
                Ver1_7 = true;
                return;
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
                Ver1_12_2 = true;
                return;
            case "13":
                Ver1_13 = true;
                return;
            case "14":
            case "15":
                Ver1_14 = true;
                return;
            case "16":
            case "17":
                Ver1_16 = true;
                return;
            case "18":
            case "19":
                Ver1_18 = true;
                return;
            case "20":
                if(Integer.parseInt(split[2]) >= 5){
                    Ver1_20_5 = true;
                    return;
                }
                Ver1_18 = true;
                return;
            case "21":
                Ver1_21 = true;
                return;
        }
    }

    public static boolean isVer1_7(){
        return Ver1_7;
    }

    public static boolean isVer1_12_2(){
        return Ver1_12_2;
    }

    public static boolean isVer1_13(){
        return Ver1_13;
    }

    public static boolean isCustomModelDataSupport(){
        return Ver1_18 || Ver1_21 || Ver1_20_5 || Ver1_16 || Ver1_14;
    }

    public static boolean isHexColorSupport(){
        return Ver1_18 || Ver1_21 || Ver1_20_5 || Ver1_16;
    }

    public static boolean isUpToVer1_18(){
        return Ver1_18 || Ver1_21 || Ver1_20_5;
    }

    public static boolean isUpToVer1_20_5(){
        return Ver1_20_5 || Ver1_21;
    }

    public static boolean isUpToVer1_21(){
        return Ver1_21;
    }

    public static boolean isBelowVer1_13(){
        return Ver1_12_2 || Ver1_7;
    }
}
