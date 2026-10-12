package com.playerinv.api.impl;

import com.playerinv.api.unit.VaultUnit;
import com.playerinv.api.unit.VaultUnitMap;
import com.playerinv.object.cache.VaultStatistics;
import com.playerinv.object.container.InventoryContainer;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class VaultUnitMapImpl implements VaultUnitMap {

    private final HashMap<Integer, VaultUnit> vaultUnitHashMap = new HashMap<>();

    public VaultUnitMapImpl(OfflinePlayer player, LinkedHashMap<Integer, InventoryContainer> map) {
        for (Map.Entry<Integer, InventoryContainer> entry : map.entrySet()) {
            this.vaultUnitHashMap.put(entry.getKey(), new VaultUnitImpl(player, entry.getKey(), entry.getValue()));
        }
    }

    public VaultUnitMapImpl(OfflinePlayer player, LinkedHashMap<Integer, VaultStatistics> map, boolean stat) {
        for (Map.Entry<Integer, VaultStatistics> entry : map.entrySet()) {
            InventoryContainer inventoryContainer = entry.getValue().getInventoryContainer();
            if (inventoryContainer != null) {
                this.vaultUnitHashMap.put(entry.getKey(), new VaultUnitImpl(player, entry.getKey(), inventoryContainer));
            }
        }
    }

    @Override
    public VaultUnit getVaultUnit(int num) {
        return this.vaultUnitHashMap.get(num);
    }

    @Override
    public HashMap<Integer, ItemStack> sendItemToVault(int num, ItemStack itemStack) {
        ItemStack item = itemStack.clone();
        VaultUnit vaultUnit = this.vaultUnitHashMap.get(num);
        if (vaultUnit == null) return null;
        HashMap<Integer, ItemStack> map = vaultUnit.getInventory().addItem(item);
        vaultUnit.updateInventory();
        return map;
    }

    @Override
    public HashMap<Integer, ItemStack> sendItemToVault(int num, Collection<ItemStack> collection) {
        VaultUnit vaultUnit = this.vaultUnitHashMap.get(num);
        if (vaultUnit == null) return null;
        HashMap<Integer, ItemStack> map = vaultUnit.getInventory().addItem(collection.toArray(new ItemStack[0]));
        vaultUnit.updateInventory();
        return map;
    }

    @Override
    public HashMap<Integer, ItemStack> sendItemToVaults(ItemStack itemStack) {
        if (this.vaultUnitHashMap.isEmpty()) return null;
        ItemStack item = itemStack.clone();
        HashMap<Integer, ItemStack> map = new HashMap<>();
        for (VaultUnit unit : this.vaultUnitHashMap.values()) {
            map = unit.getInventory().addItem(item);
            unit.updateInventory();
            if (map.isEmpty()) {
                return map;
            }
        }
        return map;
    }

    @Override
    public HashMap<Integer, ItemStack> sendItemToVaults(Collection<ItemStack> collection) {
        if (this.vaultUnitHashMap.isEmpty()) return null;
        HashMap<Integer, ItemStack> map = new HashMap<>();
        for (VaultUnit unit : this.vaultUnitHashMap.values()) {
            map = unit.getInventory().addItem(collection.toArray(new ItemStack[0]));
            unit.updateInventory();
            if (map.isEmpty()) {
                return map;
            }
        }
        return map;
    }
}
