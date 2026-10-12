package com.playerinv.gui;

import com.playerinv.PlayerInv;
import com.playerinv.object.container.MenuContainer;
import com.playerinv.util.LocaleUtil;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.NodeUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class VaultMenuStruct {

    private static final VaultMenuStruct instance = new VaultMenuStruct();

    private final HashSet<String> customKeySet = new HashSet<>(Arrays.asList("close-menu","open-share","open-search","pickup-switch","open-menu"));

    private final HashMap<String, MenuContainer> menuContainers = new HashMap<>();

    private void structVaultMenu(){
        menuContainers.clear();
        File file = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(), "vault_menu").normalize().toFile();
        File[] menu_array = file.listFiles();
        if(menu_array == null){
            if(System.getProperty("os.name").toLowerCase().contains("windows")){
                menu_array = file.listFiles();
            }
            if(System.getProperty("os.name").toLowerCase().contains("linux")){
                try {
                    Stream<Path> stream = Files.list(file.toPath());
                    menu_array = (File[]) stream.toArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
                    } else {
                        continue;
                    }
                    MenuContainer menuContainer = new MenuContainer(current_fileConfig, new_name, true, this.customKeySet);
                    menuContainer.loadSpecialVaultMenuIcon();
                    menuContainers.put(new_name,menuContainer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void openVaultMenuForPlayer(Player player, String fileName){
        MenuContainer menuContainer = menuContainers.get(fileName);
        if(menuContainer == null){
            NodeUtil.sendMenuError("Cannot find vault menu: " + fileName + ".yml");
            return;
        }
        MessageUtil.sendDetail("玩家 " + player.getName() + " 打开菜单: " + fileName);
        PlayerInv.getScheduler().entitySpecificScheduler(player).runDelayed(task -> {
           player.openInventory(menuContainer.generateVaultMenu(player));
        },null,1);
    }

    public void loadVaultMenuConfig(){
        File menu_folder = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(), "vault_menu").normalize().toFile();
        if(!menu_folder.exists()){
            menu_folder.mkdirs();
            File main = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(), "vault_menu","Main.yml").normalize().toFile();
            File other = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(), "vault_menu","vault_page_2.yml").normalize().toFile();
            File other_3 = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(), "vault_menu","vault_page_3.yml").normalize().toFile();
            try {
                if(!main.exists()) {
                    FileUtils.copyInputStreamToFile(PlayerInv.getInstance().getResource("vault_menu/" + LocaleUtil.getLocale() + "/Main.yml"),main);
                }
                if(!other.exists()) {
                    FileUtils.copyInputStreamToFile(PlayerInv.getInstance().getResource("vault_menu/" + LocaleUtil.getLocale() + "/vault_page_2.yml"),other);
                }
                if(!other_3.exists()) {
                    FileUtils.copyInputStreamToFile(PlayerInv.getInstance().getResource("vault_menu/" + LocaleUtil.getLocale() + "/vault_page_3.yml"),other_3);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.structVaultMenu();
    }

    public static VaultMenuStruct getInstance(){
        return instance;
    }
}
