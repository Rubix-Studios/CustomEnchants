package com.rubixstudios.customenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.KillEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;

public class KillOnItemEnchantment extends KillEnchantment {
    public KillOnItemEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return false;
    }

    public String getName() {
        return "KillLog";
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getStartLevel() {
        return 1;
    }

    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    public boolean canEnchantItem(ItemStack itemStack) {
        return ItemUtils.isWeapon(itemStack.getType()) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}