package com.playerinv.service.translator;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Translator {

    void init();

    String translate(ItemStack stack, String locale);

    boolean hasLang(String locale);

    void reloadCustomTranslations();

    String itemTranslate(ItemStack item, Player player);
}
