package com.playerinv.api.manager;

import com.playerinv.api.PlayerInvAPI;
import com.playerinv.api.VaultType;
import com.playerinv.api.unit.VaultUnit;
import com.playerinv.api.unit.VaultUnitMap;
import org.bukkit.OfflinePlayer;

/**
 * PlayerInv 功能实现接口, 由 PlayerInv 插件内部实现并注册。
 * <p>
 * 第三方插件请直接调用 {@link PlayerInvAPI} 的静态方法, 不需要实现本接口。
 */
public interface VaultUnitManager {

    VaultUnit getVault(OfflinePlayer player, VaultType vaultType, int vaultNum);

    VaultUnitMap getVaults(OfflinePlayer player, VaultType vaultType);
}