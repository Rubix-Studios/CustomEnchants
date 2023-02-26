package com.rubixstudios.customenchants.enchantments.handlers;

import org.bukkit.inventory.ItemStack;

public abstract class DataEnchantment extends CustomEnchantment {

    public DataEnchantment(int id) {
        super(id);
    }

    public abstract String getCurrentKey(); // TODO maak support voor arrays
    public abstract int getIntOfKey(ItemStack itemStack);

}
