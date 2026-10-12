package com.playerinv.api.unit;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface VaultUnitMap {

    VaultUnit getVaultUnit(int num);

    Map<Integer, ItemStack> sendItemToVault(int num, ItemStack itemStack);

    Map<Integer, ItemStack> sendItemToVault(int num, Collection<ItemStack> collection);

    Map<Integer, ItemStack> sendItemToVaults(ItemStack itemStack);

    Map<Integer, ItemStack> sendItemToVaults(Collection<ItemStack> collection);
}
