package com.rubixstudios.customenchants.enchantments.Bomb;

import com.rubixstudios.customenchants.enchantments.handlers.BombEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.value.BombValue;
import com.rubixstudios.customenchants.utils.EnchantUtils;

public class BleedBombEnchantment extends BombEnchantment implements Listener {
    public BleedBombEnchantment(int id){
        super(id);
        this.setMaterial(Material.BLAZE_POWDER);
        this.setMetadata(BombValue.bleedbomb);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Bleed bomb";
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
        return itemStack.getType().equals(Material.BLAZE_POWDER) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
