package com.rubixstudios.customenchants.enchantments.bleed;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;

public class BleedEnchantment extends CustomEnchantment implements Listener {

    public BleedEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Bleed";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }


}
