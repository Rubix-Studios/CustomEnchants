package com.rubixstudios.customenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.enchantments.handlers.PotionEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;

public class FireResEnchantment extends PotionEnchantment {


    public FireResEnchantment(int id) {
        super(id, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Fire Res";
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
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return ItemUtils.isArmor(itemStack.getType()) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
