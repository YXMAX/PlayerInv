package com.playerinv.Enums.Lang.Legacy;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemEntry {

    private Material material;
    private int metadata;
    private static ItemEntry inst = new ItemEntry();

    private ItemEntry() {
    }

    public ItemEntry(Material material, int meta) {
        this.material = material;
        this.metadata = meta;
    }

    public ItemEntry(Material material) {
        this(material, 0);
    }


    public ItemEntry(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.metadata = itemStack.getDurability();
    }

    public static ItemEntry from(ItemStack itemStack) {
        try {
            ItemEntry result = (ItemEntry) inst.clone();
            result.material = itemStack.getType();
            result.metadata = itemStack.getDurability();
            return result;
        } catch (CloneNotSupportedException e) {
            return new ItemEntry(itemStack);
        }
    }

    public static ItemEntry from(Material material, int meta) {
        try {
            ItemEntry result = (ItemEntry) inst.clone();
            result.material = material;
            result.metadata = meta;
            return result;
        } catch (CloneNotSupportedException e) {
            return new ItemEntry(material, meta);
        }
    }

    public static ItemEntry from(Material material) {
        return ItemEntry.from(material, 0);
    }

    public Material getMaterial() {
        return material;
    }

    public int getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemEntry)) return false;

        ItemEntry itemEntry = (ItemEntry) o;

        return metadata == itemEntry.metadata && material == itemEntry.material;
    }

    @Override
    public int hashCode() {
        int result = material.hashCode();
        result = 31 * result + metadata;
        return result;
    }

    @Override
    public String toString() {
        return this.material.toString() + " " + this.metadata;
    }
}
