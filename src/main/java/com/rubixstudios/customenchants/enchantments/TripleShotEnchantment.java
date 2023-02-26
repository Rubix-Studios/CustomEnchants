package com.rubixstudios.customenchants.enchantments;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.value.PotionBowValue;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.particles.ParticleUtil;

public class TripleShotEnchantment extends CustomEnchantment implements Listener {

    private final double distanceFromPlayerHead = 1;

    private final double chanceOnTripleShot = 0.2;

    public TripleShotEnchantment(int id) {
        super(id);
    }

    @EventHandler
    public void onProjectileLaunch(EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();

        if (!(arrow.getShooter() instanceof Player)) return;
        final Player shooter = (Player) arrow.getShooter();

        if (!EnchantUtils.hasItemStackEnchantment(shooter.getItemInHand(), this)) return;

        final Location eyeLocation = shooter.getEyeLocation();
        final Vector direction = eyeLocation.getDirection();

        final float force = event.getForce();

        if (Math.random() > chanceOnTripleShot) return;

        // Getting spawn location for arrows and adding an offset from the players head

        eyeLocation.add(direction.multiply(distanceFromPlayerHead));

        final int fireTicks = arrow.getFireTicks();
        // Spawning 2 extra arrows
        final Arrow arrow2 = shooter.getWorld().spawnArrow(eyeLocation, rotateVector(direction.clone(), 0.25), force * 2, 0f); // left
        final Arrow arrow3 = shooter.getWorld().spawnArrow(eyeLocation, rotateVector(direction.clone(), -0.25), force * 2, 0f); // right

        final Arrow[] arrows = new Arrow[]{
                arrow2,
                arrow3
        };

        for (final String metadata : PotionBowValue.potionBowValues) {
            final boolean hasMeta = arrow.hasMetadata(metadata) && arrow.getMetadata(metadata).get(0).asBoolean();
            if (hasMeta){
                for (final Arrow extraArrow : arrows) {
                    extraArrow.setMetadata(metadata, new FixedMetadataValue(CustomEnchants.getInstance(), true));
                }

                ParticleUtil.runColoredParticleTrailByMetadata(metadata, arrows, Effect.POTION_SWIRL);
            }
        }

        for (final Arrow extraArrow : arrows) {
            extraArrow.setFireTicks(fireTicks);
            extraArrow.setShooter(shooter);

            if (force == 1){
                extraArrow.setCritical(true);
            }
        }
    }


    public Vector rotateVector(Vector vector, double whatAngle) {
        final double sin = Math.sin(whatAngle);
        final double cos = Math.cos(whatAngle);
        final double x = vector.getX() * cos + vector.getZ() * sin;
        final double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "TripleShot";
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
