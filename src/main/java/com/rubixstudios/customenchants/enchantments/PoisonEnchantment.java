package com.rubixstudios.customenchants.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.MeleeEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;

public class PoisonEnchantment extends MeleeEnchantment {

    public PoisonEnchantment(int id) {
        super(id, new PotionEffect(PotionEffectType.POISON, 20* CustomEnchants.getInstance().getConfig().getInt("poisonStick-effect.seconds"), CustomEnchants.getInstance().getConfig().getInt("poisonStick-effect.amplifier")), ParticleEffect.PORTAL);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Poison";
    }
//test
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
        return itemStack.getType().equals(Material.STICK) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}