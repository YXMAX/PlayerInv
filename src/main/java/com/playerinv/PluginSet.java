package com.playerinv;

import com.playerinv.InvHolder.*;
import com.playerinv.Listener.CheckInvListener;
import com.playerinv.Listener.CheckInvOfflineListener;
import com.playerinv.Listener.InvListener;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.playerinv.LocaleUtil.*;
import static com.playerinv.PlayerInv.*;
import static org.bukkit.Bukkit.getServer;

public class PluginSet {
    public static void initListeners() {
        getServer().getPluginManager().registerEvents((Listener)new InvListener(), (Plugin)plugin);
        getServer().getPluginManager().registerEvents((Listener)new CheckInvListener(), (Plugin)plugin);
        getServer().getPluginManager().registerEvents((Listener)new CheckInvOfflineListener(), (Plugin)plugin);
    }

    public static String inventoryToBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            String base64 = Base64Coder.encodeLines(outputStream.toByteArray());
            sendLog("EnCode Length: " + base64.length());
            return base64;
            //Converts the inventory and its contents to base64, This also saves item meta-data and inventory type
        } catch (Exception e) {
            throw new IllegalStateException("Could not convert inventory to base64.", e);
        }
    }

    public static Inventory inventoryFromBase64_Large(String data, int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new VaultHolder_Large(), dataInput.readInt(), color(Vault_large_title() + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type. Make sure the items of inventory can be recognized(may be some mod items).", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64. Make sure the items of inventory can be recognized(may be some mod items) or may be the base64 code was destroyed..", e);
        }
    }

    public static Inventory inventoryFromBase64_Medium(String data, int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new VaultHolder_Medium(), dataInput.readInt(), color(Vault_medium_title() + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static Inventory inventoryFromBase64_Large_Check(String data, Player target, int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new Check_VaultHolder_Large(), dataInput.readInt(), color(Check_Large_Title_Online(target) + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static Inventory inventoryFromBase64_Medium_Check(String data, Player target, int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new Check_VaultHolder_Medium(), dataInput.readInt(), color(Check_Medium_Title_Online(target) + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static Inventory inventoryFromBase64_Large_Check_Offline(String data, OfflinePlayer target,int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new Check_VaultHolder_Large_Offline(), dataInput.readInt(), color(Check_Large_Title_Offline(target) + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static Inventory inventoryFromBase64_Medium_Check_Offline(String data, OfflinePlayer target,int VaultNum) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = getServer().createInventory(new Check_VaultHolder_Medium_Offline(), dataInput.readInt(), color(Check_Medium_Title_Offline(target) + VaultNum));
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }

    public static String getValueFromLore(ItemStack item, String value) {

        String returnVal = "null";
        ItemMeta meta = item.getItemMeta();
        try {
            List<String> lore = meta.getLore();
            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains(value)) {
                        String vals = lore.get(i).split(":")[1];
                        vals = ChatColor.stripColor(vals);
                        returnVal = vals.trim();
                    }
                }
            }
        } catch (Exception e) {
            return returnVal;
        }
        return returnVal;
    }

    public static String locale() {
        String v = plugin.getConfig().getString("Language");
        return v;
    }

    public static String Vault_large_title() {
        String v = LocaleConfig.getString("Vault.Large_title");
        return v;
    }

    public static String Vault_medium_title() {
        String v = LocaleConfig.getString("Vault.Medium_title");
        return v;
    }

    public static int Vault_large_amount() {
        int v = plugin.getConfig().getInt("Vault.Large_amount");
        return v;
    }

    public static int Vault_medium_amount() {
        int v = plugin.getConfig().getInt("Vault.Medium_amount");
        return v;
    }

    public static HashMap<String, File> LocaleMap;

    public static void getFile_writeMap() {
        String path = plugin.getDataFolder().getAbsolutePath() + "/locale";
        File file = new File(path);
        File[] array = file.listFiles();
        HashMap<String,File> temp_map = new HashMap<>();
        if(array == null){
            if(isWindows()){
                array = file.listFiles();
            }
            if(isLinux()){
                try {
                    Stream<Path> stream = Files.list(Paths.get(path));
                    array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                temp_map.put(array[i].getName(),array[i].getAbsoluteFile());
            }
        }
        LocaleMap = temp_map;

        String menu_path = plugin.getDataFolder().getAbsolutePath() + "/vault_menu";
        File menu_file = new File(menu_path);
        HashMap<String, FileConfiguration> menu_map = new HashMap<>();
        File[] menu_array = menu_file.listFiles();
        if(menu_array == null){
            if(isWindows()){
                menu_array = file.listFiles();
            }
            if(isLinux()){
                try {
                    Stream<Path> stream = Files.list(Paths.get(path));
                    menu_array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        OtherMenuFileList = new ArrayList<>();
        for (int i = 0; i < menu_array.length; i++) {
            if (menu_array[i].isFile()) {
                try {
                    File current_file = menu_array[i].getAbsoluteFile();
                    YamlConfiguration current_fileConfig = new YamlConfiguration();
                    current_fileConfig.load(current_file);
                    String file_name = menu_array[i].getName();
                    String new_name = null;
                    if(file_name.endsWith(".yml")){
                        new_name = file_name.substring(0, file_name.lastIndexOf("."));
                        OtherMenuFileList.add(new_name);
                        OtherMenuFileMap.put(new_name,current_file);
                    } else {
                        continue;
                    }
                    menu_map.put(new_name,current_fileConfig);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        VaultMenuMap = menu_map;
    }

    public static void getCheckFile_writeMap() {
        String menu_path = plugin.getDataFolder().getAbsolutePath() + "/check_menu";
        File menu_file = new File(menu_path);
        HashMap<String, FileConfiguration> menu_map = new HashMap<>();
        File[] menu_array = menu_file.listFiles();
        if(menu_array == null){
            if(isWindows()){
                menu_array = menu_file.listFiles();
            }
            if(isLinux()){
                try {
                    Stream<Path> stream = Files.list(Paths.get(menu_path));
                    menu_array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Check_OtherMenuFileList = new ArrayList<>();
        for (int i = 0; i < menu_array.length; i++) {
            if (menu_array[i].isFile()) {
                try {
                    File current_file = menu_array[i].getAbsoluteFile();
                    YamlConfiguration current_fileConfig = new YamlConfiguration();
                    current_fileConfig.load(current_file);
                    String file_name = menu_array[i].getName();
                    String new_name = null;
                    if(file_name.endsWith(".yml")){
                        new_name = file_name.substring(0, file_name.lastIndexOf("."));
                        Check_OtherMenuFileList.add(new_name);
                        Check_OtherMenuFileMap.put(new_name,current_file);
                    } else {
                        continue;
                    }
                    menu_map.put(new_name,current_fileConfig);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Check_VaultMenuMap = menu_map;
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

    public static Boolean Voucher_sound_bool() {
        Boolean v = plugin.getConfig().getBoolean("Voucher.Sound");
        return v;
    }

    public static String VoucherSoundValue;

    public static float VoucherSoundVolume;

    public static float VoucherSoundPitch;

    public static String GUISoundValue;

    public static float GUISoundVolume;

    public static float GUISoundPitch;

    public static void reloadSplitSoundValue_Voucher(){
        String value = plugin.getConfig().getString("Voucher.Value");
        String[] new_value = value.split(":");
        VoucherSoundValue = new_value[0];
        VoucherSoundVolume = Float.parseFloat(new_value[1]);
        VoucherSoundPitch = Float.parseFloat(new_value[2]);

        String value1 = plugin.getConfig().getString("GUI.Sound_value");
        String[] new_value1 = value1.split(":");
        GUISoundValue = new_value1[0];
        GUISoundVolume = Float.parseFloat(new_value1[1]);
        GUISoundPitch = Float.parseFloat(new_value1[2]);
    }

    public static String prefix(){
        String v = plugin.getConfig().getString("Prefix");
        return v;
    }

    public final static boolean isNum(String s) {
        if (s != null) {
            s.equals(s.trim());
            return s.matches("^[0-9]*$");
        } else {
            return false;
        }
    }

    public static String voucher_owner_large_material(){
        String v = plugin.getConfig().getString("Voucher.large.material");
        return v;
    }

    public static Boolean need_sneaking(){
        Boolean v = plugin.getConfig().getBoolean("Need_sneaking");
        return v;
    }

    public static Boolean voucher_owner_large_enchant_glow(){
        Boolean v = plugin.getConfig().getBoolean("Voucher.large.enchant-glow");
        return v;
    }

    public static int voucher_owner_large_custom_data(){
        int v = plugin.getConfig().getInt("Voucher.large.enchant-glow");
        return v;
    }

    public static String voucher_owner_medium_material(){
        String v = plugin.getConfig().getString("Voucher.medium.material");
        return v;
    }

    public static Boolean voucher_owner_medium_enchant_glow(){
        Boolean v = plugin.getConfig().getBoolean("Voucher.medium.enchant-glow");
        return v;
    }

    public static int voucher_owner_medium_custom_data(){
        int v = plugin.getConfig().getInt("Voucher.owner.enchant-glow");
        return v;
    }

    public static void reloadOtherMenuInventoryMap(){
        HashMap<Inventory,String> map = new HashMap<>();
        OtherMenuInventoryMap = map;
        Check_OtherMenuInventoryMap = map;
    }

    public static void sendConvertDataMessage(){
        String locale = locale();
        if(locale.equals("zh-CN")){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "存在旧数据表 转换数据至新表...");
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Existing old data table , Converting data to new table..");
        }
    }

    public static void sendConvertSuccessMessage(){
        String locale = locale();
        if(locale.equals("zh-CN")){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "数据库数据转换完成!");
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[PlayerInv] " + ChatColor.YELLOW + "Database conversion completed!");
        }
    }

    public static String ServerVersion;

    public static void DetectServerVersion(){
        String version = Bukkit.getServer().getClass().getPackage().getName();
        if(version.contains("v1_12") || version.contains("v1_11") || version.contains("v1_10") || version.contains("v1_9") || version.contains("v1_8") || version.contains("v1_7") || version.contains("v1_6")){
            isBelow113 = true;
            resetSoundValue();
        } else if(version.contains("v1_13")){
            is113 = true;
            resetSoundValue();
        } else if(version.contains("v1_14") || version.contains("v1_15")){
            Below116 = true;
        }

        ServerVersion = version;
    }

    public static void resetSoundValue(){
        if(isBelow113){
            plugin.getConfig().set("Voucher.Value", "ENTITY_ENDERDRAGON_GROWL:1:1");
            plugin.saveConfig();
        }
        if(is113){
            plugin.getConfig().set("Voucher.Value", "ENTITY_ENDER_DRAGON_GROWL:1:1");
            plugin.saveConfig();
        }
    }

    public static void PluginStartUp(){
        String locale = locale();
        Boolean mysql = plugin.getConfig().getBoolean("DataBases.MySQL");
        Boolean lptoggle = plugin.getConfig().getBoolean("Luckperms-proxy-support");
        if(locale.equals("zh-CN")){
            String database = "本地数据库已启动..";
            String lp = null;
            if(lpsupport){
                lp = "&e禁用";
                if(lptoggle){
                    lp = "&a启用";
                }
            } else {
                lp = "&c不支持";
            }
            if(mysql){
                database = "成功连接MySQL数据库..";
            }
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l ____  &e&l____   "));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|    | &e&l ||    &2PlayerInv v2.6.17"));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|____| &e&l ||    &6LuckPerms 权限同步 " + lp));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|      &e&l ||    &6" + database));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|      &e&l_||_   &8插件运行于: " + ServerVersion));
            Bukkit.getServer().getConsoleSender().sendMessage(color("                      "));
        } else {
            String database = "Local SQLite databases connected..";
            String lp = null;
            if(lpsupport){
                lp = "&eDisabled";
                if(lptoggle){
                    lp = "&aEnabled";
                }
            } else {
                lp = "&cNot Supported";
            }
            if(mysql){
                database = "Successfully connected to MySQL database";
            }
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l ____  &e&l____   "));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|    | &e&l ||    &2PlayerInv v2.6.17"));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|____| &e&l ||    &6LuckPerms permission synchronization: " + lp));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|      &e&l ||    &6" + database));
            Bukkit.getServer().getConsoleSender().sendMessage(color(" &b&l|      &e&l_||_   &8Running on " + ServerVersion));
            Bukkit.getServer().getConsoleSender().sendMessage(color("                      "));
        }
    }

    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static final String LargeFullInv = "playerinv.large.inv.*";
    public static final String MediumFullInv = "playerinv.medium.inv.*";

    public static Boolean Debug = false;

    public static void isDebug(){
        Boolean debug = plugin.getConfig().getBoolean("Debug-mode");
        if(debug){
            Debug = true;
        }
    }

    public static void sendLog(String message){
        if(Debug){
            System.out.println(message);
        }
    }

    public static void reloadMainMenuConfig(){
        String language = locale();
        MainMenu = new File(plugin.getDataFolder() + "/vault_menu/", "Main.yml");
        Check_MainMenu = new File(plugin.getDataFolder() + "/check_menu/", "Main.yml");
        if(!MainMenu.exists()){
            MainMenu.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-zh-CN/Main.yml"),new File(plugin.getDataFolder() + "/vault_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(plugin.getResource("vault_menu-en-US/Main.yml"),new File(plugin.getDataFolder() + "/vault_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(!Check_MainMenu.exists()){
            Check_MainMenu.getParentFile().mkdirs();
            if(language.equals("zh-CN")){
                try {
                    FileUtils.copyInputStreamToFile(plugin.getResource("check_menu-zh-CN/Main.yml"),new File(plugin.getDataFolder() + "/check_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    FileUtils.copyInputStreamToFile(plugin.getResource("check_menu-en-US/Main.yml"),new File(plugin.getDataFolder() + "/check_menu/", "Main.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            Check_MainMenuConfig = new YamlConfiguration();
            Check_MainMenuConfig.load(Check_MainMenu);
            MainMenuConfig = new YamlConfiguration();
            MainMenuConfig.load(MainMenu);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load main menu file",e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException("Cannot read the main menu file, check the file is exist",e);
        }
    }

    public static Boolean Voucher_Set_Owner(){
        Boolean v = plugin.getConfig().getBoolean("Voucher.Set-owner");
        return v;
    }

    public static void Update_Config(){
        boolean change = false;
        Configuration defaults = plugin.getConfig().getDefaults();
        for (String defaultKey : defaults.getKeys(true)) {
            if (!plugin.getConfig().contains(defaultKey)) {
                plugin.getConfig().set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            plugin.saveConfig();
        }
    }

    public static void Update_Locale_Config(){
        boolean change = false;
        String locale = locale();
        Configuration defaults = null;
        try {
            InputStream resource = plugin.getResource("locale/" + locale + ".yml");
            File temp = File.createTempFile("stream2file", ".tmp");
            FileUtils.copyInputStreamToFile(resource,temp);
            defaults = YamlConfiguration.loadConfiguration(temp);
            temp.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String defaultKey : defaults.getKeys(true)) {
            if (!LocaleConfig.contains(defaultKey)) {
                LocaleConfig.set(defaultKey, defaults.get(defaultKey));
                change = true;
            }
        }
        if (change) {
            try {
                LocaleConfig.save(LocaleConfigFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
