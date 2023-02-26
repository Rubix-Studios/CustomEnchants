package com.rubixstudios.customenchants.enchantments.Bomb;

import com.rubixstudios.customenchants.enchantments.handlers.BombEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.value.BombValue;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SmokeBombEnchantment extends BombEnchantment implements Listener {

    public SmokeBombEnchantment(int id) {
        super(id);
        this.setMaterial(Material.SNOW_BALL);
        this.setMetadata(BombValue.smokebomb);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
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
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().equals(Material.FIREBALL) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
