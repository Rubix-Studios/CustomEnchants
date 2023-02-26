package com.rubixstudios.customenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.MeleeEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;

public class BlindnessEnchantment extends MeleeEnchantment {

    public BlindnessEnchantment(int id) {
        super(id, new PotionEffect(PotionEffectType.BLINDNESS, 20 * CustomEnchants.getInstance().getConfig().getInt("blindness-effect.seconds"), CustomEnchants.getInstance().getConfig().getInt("blindness-effect.amplifier")), ParticleEffect.PORTAL);

        final int minimalChance = CustomEnchants.getInstance().getConfig().getInt("blindness-effect-minimal.chance");
        final int maximalChance = CustomEnchants.getInstance().getConfig().getInt("blindness-effect-maximal.chance");

        // 2,5 percent chance
        this.setMin(minimalChance);
        this.setMax(maximalChance);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Blindness";
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
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return ItemUtils.isWeapon(itemStack.getType()) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
