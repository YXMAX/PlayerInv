package com.playerinv.Util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.playerinv.API.VaultType;
import com.playerinv.Enums.PermissionEnums;
import com.playerinv.Util.InvHolder.*;
import com.playerinv.Util.Object.Cache.InventoryContainer;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.jpountz.lz4.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.playerinv.Util.InitUtil.*;
import static com.playerinv.Util.LoadUtil.*;
import static com.playerinv.Util.LocaleUtil.*;
import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.LocaleUtil.sendMessages;
import static org.bukkit.Bukkit.getServer;

public class NodeUtil {

    public static String inventoryToBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            String encoded = compress(outputStream.toByteArray());
            sendLog("Final base64 Length: " + encoded.length());
            return encoded;
            //Converts the inventory and its contents to base64, This also saves item meta-data and inventory type
        } catch (Exception e) {
            throw new IllegalStateException("Could not convert inventory to base64.", e);
        }
    }

    public static InventoryContainer inventoryFromBase64_LargeCache(String data, int vault_num, String player) {
        InventoryContainer container = new InventoryContainer();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decompress(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(null, dataInput.readInt(), "CLONE");
            container.setInventory(inventory);
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = null;
                try {
                    item = (ItemStack) dataInput.readObject();
                } catch (Throwable e){
                    sendError("Large:" + player + ":" + vault_num + " slot:" + (i+1) + " item deserialize failed! Skip this item! Cause: " + e.getMessage(),
                            "玩家 " + player + " 的大型仓库[" + vault_num + "]内槽位为 " + (i+1) + " 的物品解析失败, 系统将跳过该物品的生成, 失败原因: " + e.getMessage());
                    container.setError();
                    continue;
                }
                if(item == null){
                    continue;
                }
                if(item.getType() == Material.AIR){
                    continue;
                }
                inventory.setItem(i, item);
            }
            dataInput.close();
            return container;
        } catch( IOException | IllegalArgumentException e) {
            sendError("Large:" + player + ":" + vault_num + " -- Unable to convert inventory from base64: " + e.getMessage(),
                    "玩家 " + player + " 的大型仓库[" + vault_num + "]解析失败 失败原因: " + e.getMessage());
            container.setError();
            return null;
        }
    }

    public static InventoryContainer inventoryFromBase64_MediumCache(String data, int vault_num,String player) {
        InventoryContainer container = new InventoryContainer();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decompress(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(null, dataInput.readInt(), "CLONE");
            container.setInventory(inventory);
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = null;
                try {
                    item = (ItemStack) dataInput.readObject();
                } catch (Throwable e){
                    sendError("Medium:" + player + ":" + vault_num + " slot:" + (i+1) + " item deserialize failed! Skip this item! Cause: " + e.getMessage(),
                            "玩家 " + player+ " 的中型仓库[" + vault_num + "]内槽位为 " + (i+1) + " 的物品解析失败, 系统将跳过该物品的生成, 失败原因: " + e.getMessage());
                    container.setError();
                    continue;
                }
                if(item == null){
                    continue;
                }
                if(item.getType() == Material.AIR){
                    continue;
                }
                inventory.setItem(i, item);
            }
            dataInput.close();
            return container;
        } catch( IOException | IllegalArgumentException e) {
            sendError("Medium:" + player + ":" + vault_num + " -- Unable to convert inventory from base64: " + e.getMessage(),
                    "玩家 " + player + " 的中型仓库[" + vault_num + "]解析失败 失败原因: " + e.getMessage());
            container.setError();
            return null;
        }
    }

    public static CheckVaultHolder inventoryFromBase64_Check(OfflinePlayer target,String data,int type,int vault_num,Inventory back_inventory) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decompress(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            CheckVaultHolder checkVaultHolder = new CheckVaultHolder(target,vault_num, type, back_inventory);
            Inventory inventory = getServer().createInventory(checkVaultHolder, dataInput.readInt(), target.getUniqueId() + " [" + vault_num + "]");
            checkVaultHolder.setVault(inventory);
            boolean isError = false;
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = null;
                try {
                    item = (ItemStack) dataInput.readObject();
                } catch (Throwable e){
                    if(!isError){
                        checkVaultHolder.setError();
                        checkVaultHolder.notSave();
                        isError = true;
                    }
                    if(type == 1){
                        sendError("Large:" + target.getName() + ":" + vault_num + " slot:" + (i+1) + " item deserialize failed! Skip this item! Cause: " + e.getMessage(),
                                "玩家 " + target.getName() + " 的大型仓库[" + vault_num + "]内槽位为 " + (i+1) + " 的物品解析失败, 系统将跳过该物品的生成, 失败原因: " + e.getMessage());
                    } else {
                        sendError("Medium:" + target.getName() + ":" + vault_num + " slot:" + (i+1) + " item deserialize failed! Skip this item! Cause: " + e.getMessage(),
                                "玩家 " + target.getName() + " 的中型仓库[" + vault_num + "]内槽位为 " + (i+1) + " 的物品解析失败, 系统将跳过该物品的生成, 失败原因: " + e.getMessage());
                    }
                    continue;
                }
                if(item == null){
                    continue;
                }
                if(item.getType() == Material.AIR){
                    continue;
                }
                inventory.setItem(i, item);
            }
            dataInput.close();
            return checkVaultHolder;
        } catch(IOException | IllegalArgumentException e) {
            if(type == 1){
                sendError("Large:" + target.getName() + ":" + vault_num + " -- Unable to convert inventory from base64: " + e.getMessage(),
                        "玩家 " + target.getName() + " 的大型仓库[" + vault_num + "]解析失败 失败原因: " + e.getMessage());
            } else {
                sendError("Medium:" + target.getName() + ":" + vault_num + " -- Unable to convert inventory from base64: " + e.getMessage(),
                        "玩家 " + target.getName() + " 的中型仓库[" + vault_num + "]解析失败 失败原因: " + e.getMessage());
            }
            return null;
        }
    }

    public static Inventory inventoryFromBase64_Basic(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decompress(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(null, dataInput.readInt(), "CLONE");
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = null;
                try {
                    item = (ItemStack) dataInput.readObject();
                } catch (Throwable e){
                    continue;
                }
                if(item == null){
                    continue;
                }
                if(item.getType() == Material.AIR){
                    continue;
                }
                inventory.setItem(i, item);
            }
            dataInput.close();
            return inventory;
        } catch( IOException | IllegalArgumentException e) {
            return null;
        }
    }

    public static String inventoryToBase64_Basic(ItemStack[] items,int size) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(size);
            for (int i = 0; i < size; i++) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            String encoded = compress(outputStream.toByteArray());
            sendLog("Final base64 Length: " + encoded.length());
            return encoded;
            //Converts the inventory and its contents to base64, This also saves item meta-data and inventory type
        } catch (Exception e) {
            throw new IllegalStateException("Could not convert inventory to base64.", e);
        }
    }

    public static String compress(byte[] str){
        return Base64.getEncoder().encodeToString(str);
    }

    public static byte[] decompress(String data) throws IOException {
        return Base64.getDecoder().decode(data.replace("\r\n",""));
    }

    public static String color(String message) {
        if(!Below116 || !isBelow113 || !is113) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String hexCode = message.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');

                char[] ch = replaceSharp.toCharArray();
                StringBuilder builder = new StringBuilder("");
                for (char c : ch) {
                    builder.append("&" + c);
                }

                message = message.replace(hexCode, builder.toString());
                matcher = pattern.matcher(message);
            }
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isNum(String s) {
        if (s != null) {
            s.equals(s.trim());
            return s.matches("^[0-9]*$");
        } else {
            return false;
        }
    }

    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static void sendLog(String message){
        if(isDebug){
            Bukkit.getConsoleSender().sendMessage("[PlayerInv/DEBUG-INFO] " + message);
        }
    }

    public static ItemStack setCustomSkull(ItemStack head, String base64) {
        if(is121){
            if (base64.isEmpty()) return head;

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

        }
        if(!isBelow113) {

            if (base64.isEmpty()) return head;

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
        } else if(isBelow113){

            if (base64.isEmpty()) return head;

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
        }
        return head;
    }

    public static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

    public static String getHeadValue(String name){
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"","");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = g.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = g.fromJson(decoded,JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored){ }
        return null;
    }

    private static String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        if(str != null && !str.replace(" ", "").equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    public static String checkItemName(Player player,ItemStack itemStack){
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }
        if(isUp1205){
            StringBuilder s = new StringBuilder();
            NBT.getComponents(itemStack,nbt -> {
                if(nbt.hasTag("minecraft:item_name")){
                    ReadableNBT compound = nbt.getCompound("minecraft:item_name");
                    if(compound.hasTag("translate")){
                        String translate = compound.getString("translate");
                        s.append(translate);
                    } else if(compound.hasTag("text")){
                        String text = compound.getString("text");
                        s.append(text);
                    }
                }
            });
            if(!s.toString().isEmpty()){
                return s.toString();
            }
        }
        String name = itemTranslator.translate(itemStack,player.getLocale());
        if(name == null){
            return itemStack.getType().toString();
        }
        return name;
    }

    public static void runCommand_Large(String player,int vault_num){
        boolean cmd_bool = plugin.getConfig().getBoolean("Send-vault.Enabled");
        if(!cmd_bool){
            return;
        }
        List<String> cmd_list = plugin.getConfig().getStringList("Send-vault.Command.Large-vault");
        if(cmd_list.isEmpty()){
            return;
        }
        for(String cmd : cmd_list){
            String rcmd = cmd.replaceAll("%target%",player).replaceAll("%vault_num%", String.valueOf(vault_num));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rcmd);
        }
    }

    public static void runCommand_Medium(String player,int vault_num){
        boolean cmd_bool = plugin.getConfig().getBoolean("Send-vault.Enabled");
        if(!cmd_bool){
            return;
        }
        List<String> cmd_list = plugin.getConfig().getStringList("Send-vault.Command.Medium-vault");
        if(cmd_list.isEmpty()){
            return;
        }
        for(String cmd : cmd_list){
            String rcmd = cmd.replaceAll("%target%",player).replaceAll("%vault_num%", String.valueOf(vault_num));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rcmd);
        }
    }

    public static void sendConsole(String msg) {
        getServer().getConsoleSender().sendMessage(color(prefix + msg));
    }

    public static boolean isConsole(CommandSender commandSender){
        return !(commandSender instanceof Player);
    }

    public static void sendHelp(CommandSender commandSender, String msg){
        if(isConsole(commandSender)){
            sendConsole(msg);
        } else {
            commandSender.sendMessage(color(msg));
        }
    }

    public static boolean hasPermission(CommandSender commandSender, PermissionEnums permission){
        if(isConsole(commandSender)){
            return true;
        }
        if(commandSender.isOp()){
            return true;
        }
        return commandSender.hasPermission(permission.getDesc());
    }

    public static boolean hasPermission(Player player, PermissionEnums permission){
        if(player.isOp()){
            return true;
        }
        return player.hasPermission(permission.getDesc());
    }

    public static void sendWarn(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[PlayerInv] " + msg);
    }

    public static void sendError(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] " + msg);
    }

    public static void sendSQLError(String method,String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] " + method + " THROW-SQLException: " + msg);
    }

    public static void sendSQLConnectionError(String method,String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] " + method + " THROW-SQLTransientConnectionException: " + msg);
    }

    public static void sendError(String enUS,String zhCN){
        if(locale.equals("zh-CN")){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] " + zhCN);
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerInv] " + enUS);
        }
    }

    public static void printHelp(CommandSender commandSender){
        sendHelp(commandSender," ");
        sendHelp(commandSender, "&e&l&m------&6&l&m=====&a&l PlayerInv Remastered &6&l&m=====&e&l&m------");
        sendHelp(commandSender," ");
        if(locale.equals("zh-CN")) {
            if (hasPermission(commandSender, PermissionEnums.GUI_OPEN)) {
                sendHelp(commandSender, "&e/PlayerInv &f或 &e/Inv &f或 &e/Pinv &f或 &e/Pi &8▶ &8&o打开仓库主菜单");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-main &6[玩家] &8▶ &8&o为玩家打开仓库主菜单 (无视权限)");
            }
            if (hasPermission(commandSender, PermissionEnums.PICKUP_TOGGLE)) {
                sendHelp(commandSender, "&e/inv &fpickup toggle &8▶ &8&o切换 自动收纳掉落物 开关");
            }
            if (hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                sendHelp(commandSender, "&e/inv &falter-name &6[仓库类型] [仓库编号] &8▶ &8&o更改对应仓库的名称");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                sendHelp(commandSender, "&e/inv &fcheck &6[玩家] &8▶ &8&o查看某玩家仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                sendHelp(commandSender, "&e/inv &fgive &6[玩家] [类型] &8▶ &8&o给予玩家某一类型的仓库兑换券");
            }
            if (hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen &6[类型] [编号] &8▶ &8&o打开某一类型和编号的仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-vault &6[玩家] [large/medium] [编号] &8▶ &8&o为玩家打开相应类型和编号的仓库 (无视权限)");
            }
            if (hasPermission(commandSender, PermissionEnums.VAULT_GIVE) || hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                sendHelp(commandSender, "&e/inv &fvault &6[给予类型] [仓库类型] [数值] [玩家] &8▶ &8&o给予或追加给予玩家相应数值的仓库");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_RELOAD)) {
                sendHelp(commandSender, "&e/inv &freload &8▶ &8&o重载插件");
            }
            sendHelp(commandSender, " ");
            if (isConsole(commandSender) || commandSender.isOp()) {
                sendHelp(commandSender, "&e更多关于插件的信息 查看: https://gitee.com/yxmax_w/player-inv-reloaded-wiki/wikis");
                sendHelp(commandSender, " ");
            }
        } else {
            if (hasPermission(commandSender, PermissionEnums.GUI_OPEN)) {
                sendHelp(commandSender, "&e/PlayerInv &fOR &e/Inv &fOR &e/Pinv &fOR &e/Pi &8▶ &8&oOpen main GUI");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_MAIN_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-main &6[PLAYER] &8▶ &8&oOpen the main menu for players (Ignore Permission)");
            }
            if (hasPermission(commandSender, PermissionEnums.ALTER_NAME)) {
                sendHelp(commandSender, "&e/inv &falter-name &6[Vault-type] [Vault-num] &8▶ &8&oAlter the vault name");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_CHECK)) {
                sendHelp(commandSender, "&e/inv &fcheck &6[PLAYER] &8▶ &8&oChceck the player vault contents");
            }
            if (hasPermission(commandSender, PermissionEnums.VOUCHER_GIVE)) {
                sendHelp(commandSender, "&e/inv &fgive &6[PLAYER] [TYPE] &8▶ &8&oGive player a type of vault-voucher");
            }
            if (hasPermission(commandSender, PermissionEnums.INV_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen &6[TYPE] [NUM] &8▶ &8&oOpen a vault of a certain type and number");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_VAULT_OPEN)) {
                sendHelp(commandSender, "&e/inv &fopen-vault &6[PLAYER] [VAULT_TYPE] [VAULT_NUM] &8▶ &8&oOpen a vault for players (Ignore Permission)");
            }
            if (hasPermission(commandSender, PermissionEnums.VAULT_GIVE) || hasPermission(commandSender, PermissionEnums.VAULT_APPEND)) {
                sendHelp(commandSender, "&e/inv &fvault &6[GIVEN_TYPE] [VAULT_TYPE] [NUM] [PLAYER] &8▶ &8&oAppend or add vault that give players corresponding values");
            }
            if (hasPermission(commandSender, PermissionEnums.COMMAND_RELOAD)) {
                sendHelp(commandSender, "&e/inv &freload &8▶ &8&oReload the plugin");
            }
            sendHelp(commandSender, " ");
        }
        sendHelp(commandSender, "&e&l&m------&6&l&m=====&a&l PlayerInv Remastered &6&l&m=====&e&l&m------");
    }

    public static boolean hasVaultPermission(Player player,int type,int vault_number){
        if(player.isOp() || player.hasPermission("playerinv.admin") || player.hasPermission("playerinv.inv.*")){
            return true;
        }
        if(type == 1){
            if(player.hasPermission("playerinv.large.inv.*")){
                return true;
            }
            if(luckperms_givePermissions){
                return permissionManager.getLuckpermsService().hasVaultPermission(player,"large",vault_number);
            }
            if(vault_number <= 10){
                return player.hasPermission("playerinv.large.inv." + vault_number) || player.hasPermission("playerinv.inv." + vault_number);
            } else {
                return player.hasPermission("playerinv.large.inv." + vault_number);
            }
        } else {
            if(player.hasPermission("playerinv.medium.inv.*")){
                return true;
            }
            if(luckperms_givePermissions){
                return permissionManager.getLuckpermsService().hasVaultPermission(player,"medium",vault_number);
            }
            if(vault_number <= 15){
                return player.hasPermission("playerinv.medium.inv." + vault_number) || player.hasPermission("playerinv.inv." + (vault_number+10));
            } else {
                return player.hasPermission("playerinv.medium.inv." + vault_number);
            }
        }
    }

    public static String resetMaterial_WoolGlass(String material){
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

    public static int isMaterialColorful(String material){
        if(material == null){
            return -1;
        }
        if(!isBelow113){
            return -1;
        }
        String new_material = null;
        if(material.toLowerCase().contains("_wool")){
            new_material = material.toUpperCase().replaceAll("_WOOL","");
        } else if(material.toLowerCase().contains("_stained_glass_pane")){
            new_material = material.toUpperCase().replaceAll("_STAINED_GLASS_PANE","");
        } else if(material.toLowerCase().contains("_stained_glass")){
            new_material = material.toUpperCase().replaceAll("_STAINED_GLASS","");
        } else {
            return -1;
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
                return -1;
        }
    }

    public static void sendConsole(String zhCN,String enUS){
        if (locale.equals("zh-CN")) {
            getServer().getConsoleSender().sendMessage(color(prefix + zhCN));
        } else {
            getServer().getConsoleSender().sendMessage(color(prefix + enUS));
        }
    }

    public static void runReturnCommand(Player player){
            String run = command_returnCmdString.replaceAll("%player%", player.getName());
            if(hasPAPI){
                run = PlaceholderAPI.setPlaceholders(player, run);
            }
            switch (command_returnCmdType){
                case 2:
                    if(player.isOp()){
                        player.performCommand(run);
                        return;
                    }
                    try {
                        player.setOp(true);
                        player.performCommand(run);
                    } finally {
                        player.setOp(false);
                    }
                    return;
                case 3:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),run);
                    return;
                default:
                    player.performCommand(run);
                    return;
            }
    }

    public static String objectToString(Serializable Demo_Object){
        ByteArrayOutputStream Byte_Array_Output_Stream = new ByteArrayOutputStream();
        ObjectOutputStream Object_Output_Stream = null;
        try {
            Object_Output_Stream = new ObjectOutputStream(Byte_Array_Output_Stream);
            Object_Output_Stream.writeObject(Demo_Object);
            Object_Output_Stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZ4Compressor compressor = factory.fastCompressor();
        LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(out,2048,compressor);
        try {
            compressedOutput.write(Byte_Array_Output_Stream.toByteArray());
            compressedOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = out.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Object objectFromString(String s){
        if(s == null){
            return null;
        }
        byte[] decoded = Base64.getDecoder().decode(s);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LZ4BlockInputStream lz4Input = new LZ4BlockInputStream(inputStream, factory.fastDecompressor());
        byte[] buffer = new byte[1024];
        int count;
        try {
            while ((count = lz4Input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        byte[] Byte_Data = outputStream.toByteArray();
        Object Demo_Object = null;
        ObjectInputStream Object_Input_Stream = null;
        try {
            Object_Input_Stream = new ObjectInputStream(new ByteArrayInputStream(Byte_Data));
            Demo_Object = Object_Input_Stream.readObject();
            Object_Input_Stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Demo_Object;
    }

    public static VaultType parseVaultType(int type){
        if(type == 1){
            return VaultType.LARGE;
        } else {
            return VaultType.MEDIUM;
        }
    }

    public static boolean isChinese(){
        return locale.equalsIgnoreCase("zh-cn");
    }

    public static void sendLargeVoucher(Player player,int amount){
        String material = plugin.getConfig().getString("Voucher.large.material");
        if(material == null){
            material = "PAPER";
        }
        int custom_model_data = plugin.getConfig().getInt("Voucher.large.custom-model-data");
        boolean enchant_glow = plugin.getConfig().getBoolean("Voucher.large.enchant-glow");
        ItemStack litem = new ItemStack(Material.getMaterial(material),amount);
        ItemMeta lim = litem.getItemMeta();
        if(enchant_glow){
            lim.addEnchant(Enchantment.KNOCKBACK, 1, false);
            lim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(custom_model_data > 0){
            if(!is113 && !isBelow113){
                lim.setCustomModelData(custom_model_data);
            }
        }
        lim.setDisplayName(color(Locale_Voucher_Large_DisplayName()));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = Locale_Voucher_Large_Lore();
        for(String s : list){
            lorelist.add(color(s.replaceAll("%voucher_player%", player.getName())));
        }
        lim.setLore(lorelist);
        litem.setItemMeta(lim);
        litem = NBTEditor.set(litem, player.getName(), NBTEditor.CUSTOM_DATA, "playerinv:large");
        ItemStack item = litem.clone();
        scheduler.scheduling().entitySpecificScheduler(player).run(scheduledTask -> {
            if(natural_drop){
                player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0),item);
                return;
            }
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(item);
            if(!map.isEmpty()){
                player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0),item);
            }
            return;
        },null);
    }

    public static void sendMediumVoucher(Player player,int amount){
        String material = plugin.getConfig().getString("Voucher.medium.material");
        if(material == null){
            material = "PAPER";
        }
        int custom_model_data = plugin.getConfig().getInt("Voucher.medium.custom-model-data");
        boolean enchant_glow = plugin.getConfig().getBoolean("Voucher.medium.enchant-glow");
        ItemStack sitem = new ItemStack(Material.getMaterial(material),amount);
        ItemMeta sim = sitem.getItemMeta();
        if(enchant_glow){
            sim.addEnchant(Enchantment.KNOCKBACK, 1, false);
            sim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(custom_model_data > 0){
            if(!is113 && !isBelow113){
                sim.setCustomModelData(custom_model_data);
            }
        }
        sim.setDisplayName(color(Locale_Voucher_Medium_DisplayName()));
        ArrayList<String> lorelist = new ArrayList<String>();
        List<String> list = Locale_Voucher_Medium_Lore();
        for(String s : list){
            lorelist.add(color(s.replaceAll("%voucher_player%", player.getName())));
        }
        sim.setLore(lorelist);
        sitem.setItemMeta(sim);
        sitem = NBTEditor.set(sitem, player.getName(), NBTEditor.CUSTOM_DATA, "playerinv:medium");
        ItemStack item = sitem.clone();
        scheduler.scheduling().entitySpecificScheduler(player).run(scheduledTask -> {
            if(natural_drop){
                player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0),item);
                return;
            }
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(item);
            if(!map.isEmpty()){
                player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0),item);
            }
            return;
        },null);
    }
}
