package com.playerinv.api.impl;

import com.playerinv.api.manager.VaultUnitManager;
import com.playerinv.api.VaultType;
import com.playerinv.api.unit.VaultUnit;
import com.playerinv.api.unit.VaultUnitMap;
import com.playerinv.util.jdbc.JDBCUtil;
import org.bukkit.OfflinePlayer;

public class VaultUnitManagerImpl implements VaultUnitManager {

    @Override
    public VaultUnit getVault(OfflinePlayer player, VaultType vaultType, int vaultNum) {
        if (vaultType == VaultType.MEDIUM) {
            return JDBCUtil.getMediumVaultUnitByAPI(player, vaultType.getType(), vaultNum);
        }
        return JDBCUtil.getLargeVaultUnitByAPI(player, vaultType.getType(), vaultNum);
    }

    @Override
    public VaultUnitMap getVaults(OfflinePlayer player, VaultType vaultType) {
        if (vaultType == VaultType.MEDIUM) {
            return JDBCUtil.getMediumVaultUnitMapByAPI(player);
        }
        return JDBCUtil.getLargeVaultUnitMapByAPI(player);
    }
}
