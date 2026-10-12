package com.playerinv.api;

import com.playerinv.api.manager.VaultUnitManager;
import com.playerinv.api.unit.VaultUnit;
import com.playerinv.api.unit.VaultUnitMap;
import org.bukkit.OfflinePlayer;

public final class PlayerInvAPI {

    // 私有静态变量，持有主插件注入的实现
    private static VaultUnitManager manager;

    // 私有构造器，防止被实例化
    private PlayerInvAPI() {}

    /**
     * 【内部使用】供主插件在 onEnable 时注入实现类
     */
    public static void register(VaultUnitManager managerImpl) {
        if (manager == null) {
            manager = managerImpl;
        }
    }

    public static void unregister(){
        manager = null;
    }

    /**
     * 获取玩家指定类型、指定编号的仓库。
     *
     * @param player    目标玩家, 可离线
     * @param vaultType 仓库类型
     * @param vaultNum  仓库编号
     * @return 对应仓库
     */
    public static VaultUnit getVault(OfflinePlayer player, VaultType vaultType, int vaultNum) {
        if (manager == null) {
            throw new IllegalStateException("PlayerInvAPI 尚未初始化！请确保主插件已正确加载并启用。");
        }
        return manager.getVault(player, vaultType, vaultNum);
    }

    /**
     * 获取玩家指定类型下的全部仓库。
     *
     * @param player    目标玩家, 可离线
     * @param vaultType 仓库类型
     * @return 仓库集合
     */
    public static VaultUnitMap getVaults(OfflinePlayer player, VaultType vaultType) {
        if (manager == null) {
            throw new IllegalStateException("PlayerInvAPI 尚未初始化！请确保主插件已正确加载并启用。");
        }
        return manager.getVaults(player, vaultType);
    }
}