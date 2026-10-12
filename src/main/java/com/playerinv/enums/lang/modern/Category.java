package com.playerinv.enums.lang.modern;

import org.bukkit.inventory.ItemStack;

/**
 * Categories available for translations.
 */
public enum Category {

    BLOCK("block.minecraft.", ItemStack.class, true),

    ITEM("item.minecraft.", ItemStack.class, true),

    MUSIC_DISC("item.minecraft.$name.desc", ItemStack.class, true);


    private final String namespace;
    private final Class<?> clazz;
    private final boolean doAdditionalChecks;

    Category(String namespace, Class<?> clazz, boolean doAdditionalChecks){
        this.namespace = namespace;
        this.clazz = clazz;
        this.doAdditionalChecks = doAdditionalChecks;
    }

    public final String getNamespace(){
        return this.namespace;
    }

    public final boolean isDoAdditionalChecks(){
        return this.doAdditionalChecks;
    }

    public static String getNamespacedKey(ItemStack item){
        if (item.getType().isBlock() || item.getType().toString().toLowerCase().contains("skull")){
            return Category.BLOCK.getNamespace() + item.getType();
        } else {
            if (item.getType().isRecord()){
                return Category.MUSIC_DISC.getNamespace().replace("$name", item.getType().toString());
            }
            return Category.ITEM.getNamespace() + item.getType();
        }
    }

}
