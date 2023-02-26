package com.rubixstudios.customenchants.enchantments.handlers;

import org.bukkit.enchantments.Enchantment;

public abstract class CustomEnchantment extends Enchantment {

    public CustomEnchantment(int id) {
        super(id);
    }

    public abstract boolean shouldBeDisplayedOnItemLore();

}

