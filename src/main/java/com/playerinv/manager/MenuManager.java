package com.playerinv.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.playerinv.PlayerInv;
import com.playerinv.constants.ConfigConstants;
import com.playerinv.enums.LocaleEnums;
import com.playerinv.gui.*;
import com.playerinv.gui.icon.MenuIcon;
import com.playerinv.util.VersionUtil;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import static com.playerinv.util.LocaleUtil.sendMessages;
import static org.bukkit.Bukkit.getServer;

public class MenuManager {

    public void reload(){
        CheckMenuStruct.getInstance().loadCheckMenu();
        VaultMenuStruct.getInstance().loadVaultMenuConfig();
    }

    private boolean isConnected(){
        return JDBCUtil.isConnected();
    }

    public void openCheckMenu(Player checker, OfflinePlayer target){
        if(!this.isConnected()){
            sendMessages(checker,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        CheckMenuStruct.getInstance().openCheckMenuToPlayer(checker,target);
    }

    public void openVaultMenu(Player target, String file_name){
        if(!this.isConnected()){
            sendMessages(target,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        PlayerInv.getInstance().getSoundManager().menuOpen(target);
        if(file_name != null){
            VaultMenuStruct.getInstance().openVaultMenuForPlayer(target, file_name);
            return;
        }
        VaultMenuStruct.getInstance().openVaultMenuForPlayer(target, ConfigConstants.INDEX_MENU);
    }

    public void openVaultMenu(Player target, String file_name,boolean sound){
        if(!this.isConnected()){
            sendMessages(target,LocaleEnums.Connection_invalid_refuse);
            return;
        }
        if(file_name != null){
            VaultMenuStruct.getInstance().openVaultMenuForPlayer(target, file_name);
            if(sound){
                PlayerInv.getInstance().getSoundManager().menuSwitch(target);
            }
            return;
        }
        VaultMenuStruct.getInstance().openVaultMenuForPlayer(target, ConfigConstants.INDEX_MENU);
        if(sound){
            PlayerInv.getInstance().getSoundManager().menuSwitch(target);
        }
    }

    public static ItemStack setCustomSkull(ItemStack head, String base64) {
        if (base64.isEmpty()) return head;
        if(VersionUtil.isUpToVer1_21()){
            try {
                SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                PlayerProfile profile = getServer().createPlayerProfile(UUID.randomUUID(),"playerinv");
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(getUrlFromBase64(base64));
                profile.setTextures(textures);
                skullMeta.setOwnerProfile(profile);
                head.setItemMeta(skullMeta);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return head;
        } else if(VersionUtil.isBelowVer1_13()){
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), "playerinv");
            profile.getProperties().put("textures", new Property("textures", base64));

            try {
                Field mtd = skullMeta.getClass().getDeclaredField("profile");
                mtd.setAccessible(true);
                mtd.set(skullMeta, profile);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                ex.printStackTrace();
            }
            head.setItemMeta(skullMeta);
            return head;
        } else {
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), "playerinv");
            profile.getProperties().put("textures", new Property("textures", base64));
            try {
                Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                mtd.setAccessible(true);
                mtd.invoke(skullMeta, profile);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                try {
                    Field mtd = skullMeta.getClass().getDeclaredField("profile");
                    mtd.setAccessible(true);
                    mtd.set(skullMeta, profile);
                } catch (IllegalAccessException | NoSuchFieldException ex2) {
                    ex2.printStackTrace();
                }
            }
            head.setItemMeta(skullMeta);
            return head;
        }
    }

    private static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }
}
