package com.rubixstudios.customenchants.enchantments;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.MathUtil;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;
import com.rubixstudios.uhcf.timer.TimerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;

public class GrappleHarpoonEnchantment extends CustomEnchantment implements Listener {

    public GrappleHarpoonEnchantment(int id) {
        super(id);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;
        final Arrow arrow = (Arrow) event.getProjectile();
        if (!(EnchantUtils.hasItemStackEnchantment(event.getBow(), this))) return;
        final Player player = (Player) arrow.getShooter();

        if (CustomEnchants.softDepend) {
            if (TimerManager.getInstance().getGrappleCooldownTimer().isActive(player)) {
                event.setCancelled(true);
                return;
            }
        }
        arrow.setMetadata("grappleHarpoon", new FixedMetadataValue(CustomEnchants.getInstance(), true));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        final Arrow arrow = (Arrow) event.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;

        final Player shooter = (Player) arrow.getShooter();

        final boolean isGrappleHarpoon = arrow.hasMetadata("grappleHarpoon") && arrow.getMetadata("grappleHarpoon").get(0).asBoolean();
        if (!isGrappleHarpoon) return;
        final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

        if (CustomEnchants.softDepend){
            final TimerManager timerManager = TimerManager.getInstance();
            if (!timerManager.getGrappleCooldownTimer().isActive(shooter)) {
                timerManager.getGrappleCooldownTimer().activate(shooter, 30);

            }else {

                return;
            }
        }

        eventManager.callCustomItemUseEvent(shooter);
        fireGrapple(shooter, arrow);
        this.spawnParticles(shooter);

    }

    private void fireGrapple(Player shooter, Arrow arrow){
        final double minYVel = .5;
        final double maxYVel = 2.5;


        final Location playerLocation = shooter.getLocation();
        final Location arrowLocation = arrow.getLocation();

        // Multipliers
        final double horizontalMultiplier = 0.1;
        final double verticalMultiplier = 0.09;

        final double horizontalDistanceToArrow = new Vector(playerLocation.getX(), 0, playerLocation.getZ()).distance(new Vector(arrowLocation.getX(), 0, arrowLocation.getZ()));
        final double distanceToArrow = playerLocation.toVector().distance(arrowLocation.toVector());

        final double finalHorizontalMultiplier = horizontalMultiplier * horizontalDistanceToArrow;
        final double finalVerticalMultiplier = arrowLocation.getY() < shooter.getLocation().getY() ? verticalMultiplier : verticalMultiplier * distanceToArrow;

        final Vector directionToArrow = arrow.getLocation().toVector().subtract(playerLocation.toVector()).normalize();
        Vector velocity = directionToArrow.clone();

        // Horizontal multiplier
        velocity = new Vector(velocity.getX() * finalHorizontalMultiplier, velocity.getY(), velocity.getZ() * finalHorizontalMultiplier);

        // Vertical multiplier
        velocity = new Vector(velocity.getX(), velocity.getY() * finalVerticalMultiplier, velocity.getZ());
        // Set minimal-Y if Y-velocity is below minimal-Y
        velocity.setY(MathUtil.clamp(velocity.getY(), minYVel, maxYVel));

        shooter.setVelocity(correctVelocity(velocity));


    }

    private BukkitTask spawnParticles(Player shooter) {
        return new BukkitRunnable() {
            final ParticleEffect particleEffect = ParticleEffect.CLOUD;
            @Override
            public void run() {
                shooter.setFallDistance(0.1f);
                if (shooter.isDead() || !shooter.isOnline() ||  !shooter.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) cancel();
                particleEffect.display(0f, 0f, 0f, .1f, 1, shooter.getLocation(), 16);
            }
        }.runTaskTimer(CustomEnchants.getInstance(), 0L, 0L);
    }

    // TODO bandage code, fix this. I did this because if velocity is higher then 4 or lower then -4 the console throws a stacktrace for "excessive velocity detected"
    private Vector correctVelocity(Vector vector){
        if (vector.getX() >= 4){
            vector.setX(3.99);
        }
        if (vector.getY() >= 4){
            vector.setY(3.99);
        }
        if (vector.getZ() >= 4) {
            vector.setZ(3.99);
        }

        if (vector.getX() <= -4){
            vector.setX(-3.99);
        }
        if (vector.getY() <= -4){
            vector.setY(-3.99);
        }
        if (vector.getZ() <= -4) {
            vector.setZ(-3.99);
        }

        return vector;
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Grapple";
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
        return itemStack.getType() == Material.BOW && !itemStack.containsEnchantment(this);
    }
}