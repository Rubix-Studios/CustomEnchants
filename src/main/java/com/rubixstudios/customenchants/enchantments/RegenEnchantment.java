package com.rubixstudios.customenchants.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;

public class RegenEnchantment extends CustomEnchantment implements Listener {

    public RegenEnchantment(int id) {
        super(id);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        if(event.getEntity() == null) return;
        if(event.getEntity().getKiller() == null) return;

        final Player killerPlayer = event.getEntity().getKiller();

        if (killerPlayer.getInventory().getArmorContents() == null) return;
        if (killerPlayer.getInventory().getChestplate() == null) return;
        if (!EnchantUtils.hasItemStackEnchantment(killerPlayer.getInventory().getChestplate(), this)) return;

        final int seconds = CustomEnchants.getInstance().getConfig().getInt("regeneration-effect.seconds");
        final int amplifier = CustomEnchants.getInstance().getConfig().getInt("regeneration-effect.amplifier");

        final PotionEffect potionEffect = new PotionEffect(PotionEffectType.REGENERATION, 20 * seconds, amplifier);
        killerPlayer.addPotionEffect(potionEffect);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Regen";
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
        return itemStack.getType().equals(Material.DIAMOND_CHESTPLATE) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}
