package com.rubixstudios.customenchants.enchantments;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.value.PotionBowValue;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.enchantments.handlers.PotionBowEnchantment;

public class BowWitherEnchantment extends PotionBowEnchantment {
    public BowWitherEnchantment(int id) {
        super(id);

        this.setRed(128);
        this.setGreen(128);
        this.setBlue(128);

        this.setMin(CustomEnchants.getInstance().getConfig().getInt("witherbow-effect-minimal.chance"));
        this.setMax(CustomEnchants.getInstance().getConfig().getInt("witherbow-effect-maximal.chance"));


        final int seconds = CustomEnchants.getInstance().getConfig().getInt("witherbow-effect.seconds");
        final int amplifier = CustomEnchants.getInstance().getConfig().getInt("witherbow-effect.amplifier");

        this.setMetadata(PotionBowValue.WITHER);

      //  this.setEffect(Effect.POTION_SWIRL);

        this.setPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * seconds, amplifier));
        this.initialize();
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Wither";
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
        return EnchantmentTarget.BOW;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().equals(Material.BOW) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
