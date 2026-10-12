package com.playerinv.constants;

import com.playerinv.PlayerInv;
import com.playerinv.service.papi.PlaceholderExpansion;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DependencyConstants {

    public static boolean PAPI_ENABLED;

    public static boolean LUCKPERMS_ENABLED;

    public static void initPlaceholderAPI(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderExpansion().register();
            DependencyConstants.PAPI_ENABLED = true;
        }
    }

    public static void initLuckpermsService(){
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                if(rsp != null){
                    PlayerInv.getInstance().getPermissionManager().initLuckpermService();
                    PlayerInv.getInstance().getPermissionManager().getLuckpermsService().setAPI(rsp.getProvider());
                    DependencyConstants.LUCKPERMS_ENABLED = true;
                }
            } catch (NoClassDefFoundError e) {
                return;
            }
        }
    }
}
