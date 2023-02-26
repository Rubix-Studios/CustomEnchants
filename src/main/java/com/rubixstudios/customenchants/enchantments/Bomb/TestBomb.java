package com.rubixstudios.customenchants.enchantments.Bomb;

import com.rubixstudios.customenchants.enchantments.handlers.BombEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.value.BombValue;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class TestBomb extends BombEnchantment {
    public TestBomb(int id) {
        super(id);
        this.setMaterial(Material.DIAMOND);
        this.setMetadata(BombValue.xdbomb);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Testbomb";
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
        return itemStack.getType().equals(Material.DIAMOND) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
