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

public class SlownessEnchantment extends MeleeEnchantment {

    public SlownessEnchantment(int id) {
        super(id, new PotionEffect(PotionEffectType.SLOW, 20 * CustomEnchants.getInstance().getConfig().getInt("slowness-effect.seconds"), CustomEnchants.getInstance().getConfig().getInt("slowness-effect.amplifier")), ParticleEffect.REDSTONE);

        final int minimalChance = CustomEnchants.getInstance().getConfig().getInt("slowness-effect-minimal.chance");
        final int maximalChance = CustomEnchants.getInstance().getConfig().getInt("slowness-effect-maximal.chance");
        // 3 percent chance
        this.setMin(minimalChance);
        this.setMax(maximalChance);

        this.setOffsetX(.2f);
        this.setOffsetY(1);
        this.setOffsetZ(.2f);

        this.setSpeed(.2f);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Slowness";
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
