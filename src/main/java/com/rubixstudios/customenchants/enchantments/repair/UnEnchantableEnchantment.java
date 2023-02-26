package com.rubixstudios.customenchants.enchantments.repair;

import com.rubixstudios.customenchants.utils.EnchantUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;

public class UnEnchantableEnchantment extends CustomEnchantment {

    public UnEnchantableEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return false;
    }


    @Override
    public String getName() {
        return "Unrepairable";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
