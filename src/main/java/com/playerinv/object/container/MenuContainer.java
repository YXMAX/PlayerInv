package com.playerinv.object.container;

import com.playerinv.PlayerInv;
import com.playerinv.gui.adapter.MenuValueAdapter;
import com.playerinv.gui.icon.impl.ItemIcon;
import com.playerinv.gui.icon.impl.VaultIcon;
import com.playerinv.gui.icon.MenuIcon;
import com.playerinv.manager.PermissionManager;
import com.playerinv.gui.holder.impl.menu.*;
import com.playerinv.util.message.MessageUtil;
import com.playerinv.util.NodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class MenuContainer {

    // 点击的原始槽位:MenuIcon接口
    private final HashMap<Integer, MenuIcon> iconMap = new HashMap<>();

    // 自定义的功能键 为ItemIcon服务
    private HashSet<String> customKeySet;

    private final YamlConfiguration config;

    private final String configName;

    // 标题的key:标题名称
    private final HashMap<String, String> menuTitles = new HashMap<>();

    // 仓库按钮的TopKey:仓库Icon对象
    private HashMap<String, VaultIcon> vaultIcons;

    // 仓库按钮Set的TopKey : Map<槽位:编号>
    private HashMap<String, LinkedHashMap<Integer, Integer>> vaultIconSlots;

    private final int menuSize;

    public MenuContainer(YamlConfiguration config, String configName, boolean one_title, HashSet<String> set) {
        this.config = config;
        this.configName = configName;
        this.vaultIcons = null;
        if (one_title) {
            this.menuTitles.put("menu_title", this.config.getString("menu_title", "Menu"));
        }
        this.menuSize = MenuValueAdapter.parseMenuSize(configName, this.config.getInt("size", 54));
        this.customKeySet = set;
        this.loadItemIcon(false);
        NodeUtil.sendConsole("&a成功加载菜单: " + configName + ".yml", "Menu loaded: " + configName + ".yml");
    }

    public MenuContainer(YamlConfiguration config, String configName, boolean one_title, HashSet<String> set, boolean barSlot) {
        this.config = config;
        this.configName = configName;
        this.vaultIcons = null;
        if (one_title) {
            this.menuTitles.put("menu_title", this.config.getString("menu_title", "Menu"));
        }
        this.menuSize = MenuValueAdapter.parseMenuSize(configName, this.config.getInt("size", 54));
        this.customKeySet = set;
        this.loadItemIcon(barSlot);
        NodeUtil.sendConsole("&a成功加载菜单: " + configName + ".yml", "Menu loaded: " + configName + ".yml");
    }

    public HashMap<Integer, MenuIcon> getIcons() {
        return iconMap;
    }

    public void addMenuTitle(String key) {
        this.menuTitles.put(key, this.config.getString(key, "Menu"));
    }

    private String parseMenuTitle(String key, Player player) {
        return MessageUtil.parseString(this.menuTitles.get(key), player);
    }

    private String parseMenuTitle(String key) {
        return MessageUtil.parseString(this.menuTitles.get(key));
    }

    public void loadItemIcon(boolean barSlot) {
        ConfigurationSection section = this.config.getConfigurationSection("items");
        if (section == null) return;
        for (String topKey : section.getKeys(false)) {
            try {
                ItemIcon icon = ItemIcon.build(section, topKey, this.menuSize, this.configName, barSlot);
                icon.importCustomKey(this.customKeySet);
                this.iconMap.put(icon.getRawSlot(), icon);
            } catch (Exception e) {
                continue;
            }
        }
    }

    public Inventory generateVaultMenu(Player player) {
        HashMap<Integer, MenuIcon> icons = new HashMap<>(this.iconMap);
        VaultMenuHolder holder = new VaultMenuHolder(player, this.configName, icons);
        Inventory inventory = Bukkit.createInventory(holder, this.menuSize, this.parseMenuTitle("menu_title", player));
        holder.loadInventory(inventory);
        holder.lock();
        PlayerInv.getScheduler().asyncScheduler().run(() -> {
            HashMap<Integer, Integer> large = this.vaultIconSlots.get("large_vault_set");
            for (Map.Entry<Integer, Integer> entry : large.entrySet()) {
                int num = entry.getValue();
                int slot = entry.getKey();
                if (PermissionManager.hasVaultPermission(player, 1, num)) {
                    VaultIcon unlocked = this.vaultIcons.get("large_unlocked").clone();
                    icons.put(slot, unlocked);
                    holder.setItemToInventory(slot, unlocked.generatePersonalVaultIcon(player, 1, num));
                } else {
                    VaultIcon locked = this.vaultIcons.get("large_locked").clone();
                    icons.put(slot, locked);
                    holder.setItemToInventory(slot, locked.generatePersonalVaultIcon(player, 1, num));
                }
            }
            HashMap<Integer, Integer> medium = this.vaultIconSlots.get("medium_vault_set");
            for (Map.Entry<Integer, Integer> entry : medium.entrySet()) {
                int num = entry.getValue();
                int slot = entry.getKey();
                if (PermissionManager.hasVaultPermission(player, 2, num)) {
                    VaultIcon unlocked = this.vaultIcons.get("medium_unlocked").clone();
                    icons.put(slot, unlocked);
                    holder.setItemToInventory(slot, unlocked.generatePersonalVaultIcon(player, 2, num));
                } else {
                    VaultIcon locked = this.vaultIcons.get("medium_locked").clone();
                    icons.put(slot, locked);
                    holder.setItemToInventory(slot, locked.generatePersonalVaultIcon(player, 2, num));
                }
            }
            holder.unlock();
        });
        return inventory;
    }

    public void loadSpecialVaultMenuIcon() {
        ConfigurationSection section = this.config.getConfigurationSection("vault_items");
        if (section == null) return;
        this.vaultIcons = new HashMap<>();
        this.vaultIconSlots = new HashMap<>();
        for (String topKey : section.getKeys(false)) {
            if (topKey.contains("locked")) {
                try {
                    VaultIcon vaultIcon = VaultIcon.build(section, topKey, this.configName);
                    this.vaultIcons.put(topKey, vaultIcon);
                } catch (Exception e) {
                    continue;
                }
            } else if (topKey.contains("_set")) {
                List<String> stringList = section.getStringList(topKey);
                for (String s : stringList) {
                    if (!s.contains(":")) {
                        continue;
                    }
                    String[] split = s.split(":");
                    int rawSlot = Integer.parseInt(split[0]) - 1;
                    int num = Integer.parseInt(split[1]);
                    this.vaultIconSlots.compute(topKey, (k, v) -> {
                        if (v == null) {
                            LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
                            map.put(rawSlot, num);
                            return map;
                        }
                        v.put(rawSlot, num);
                        return v;
                    });
                }
            }
            continue;
        }
    }
}
