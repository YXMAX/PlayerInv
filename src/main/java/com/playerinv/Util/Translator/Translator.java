package com.playerinv.Util.Translator;

import org.bukkit.inventory.ItemStack;

public interface Translator {

    void init();

    String translate(ItemStack stack, String locale);

    boolean hasLang(String locale);

    void reloadCustomTranslations();
}
