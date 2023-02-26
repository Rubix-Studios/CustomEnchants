package com.rubixstudios.customenchants.manager;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.objects.BleedBombObject;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.particles.ParticleUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.enchantments.bleed.BleedDamage;

import java.util.*;

public class BleedBombManager {
    private final @Getter BukkitRunnable setupTask;
    private final @Getter EventManager eventManager = CustomEnchants.getInstance().getEventManager();
    private final @Getter List<BleedBombObject> bleedBombs;
    private final @Getter List<BleedBombObject> removalCache;

    public BleedBombManager(){
        this.removalCache = new ArrayList<>();
        this.bleedBombs = new ArrayList<>();
        this.setupTask = setupTasks();
        this.setupTask.runTaskTimer(CustomEnchants.getInstance(), 0L, 5L);
    }

    private BukkitRunnable setupTasks() {
        final int timeAfterRemoval = 5; // 5 sec

        return new BukkitRunnable() {
            @Override
            public void run() {

                if (!removalCache.isEmpty()) {
                    removalCache.forEach(bleedBombs::remove);
                }

                if (bleedBombs.isEmpty()) return; // Check if bleedbomb cache is empty then return.

                bleedBombs.forEach((b) -> {
                    b.setActive(true);
                    spawnParticlesAtEachBleedBomb(b);

                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (!isPlayerInRange(b, player)) continue;
                        final UUID uuid = player.getUniqueId();

                        if (BleedDamage.getBleedMap().containsKey(uuid)) continue;

                        BleedDamage.getBleedMap().put(uuid, System.currentTimeMillis());
                        eventManager.callBleedActivateEvent(player);
                    }

                    final boolean afterTime = b.getTimeThrown() + 1000 * timeAfterRemoval < System.currentTimeMillis(); // If the thrown time is higher then timeAfterRemoval
                    if (afterTime) {  // If the thrown time is higher then timeAfterRemoval
                        b.setActive(false);
                        removeBleedBomb(b);
                    }
                });
            }
        };
    }

    /**
     * Removes the bleed Bomb
     * @param bleedBombObject the Smoke Bomb
     */
    private void removeBleedBomb(final BleedBombObject bleedBombObject) {
        bleedBombObject.getItem().remove();
        removalCache.add(bleedBombObject);
    }

    private void spawnParticlesAtEachBleedBomb(final BleedBombObject bleedBombObject) {
        final int range = bleedBombObject.getBleedRange(); // Range of spawning smokes from the point the smokebomb lands
        this.spawnRandomParticlesInsideBleedArea(bleedBombObject, range);

        this.spawnParticlesOnBleedBombDroppedLocation(bleedBombObject);
    }

    /**
     * Spawn random particles inside the smoke area
     * @param bleedBombObject the Smoke Bomb
     * @param range the range
     */
    private void spawnRandomParticlesInsideBleedArea(final BleedBombObject bleedBombObject, int range) {
        final org.bukkit.util.Vector pointVector = this.getRandomOffsetWithinRangeAndHeight(range, true);
        final Location randomPointInRangeFromImpactPosition = bleedBombObject.getLandLoc().clone().add(pointVector); // Calculate random point for particles
        ParticleUtil.runColoredParticles(randomPointInRangeFromImpactPosition, 5, 255, 0, 0, Effect.COLOURED_DUST); // spawn random red particles inside range
    }


    /**
     * Get random offset within range an height
     * @param range the range
     * @param positiveY true or false
     * @return returns vector
     */
    private Vector getRandomOffsetWithinRangeAndHeight(final double range, boolean positiveY) {
        final double yModifier = positiveY ? 1 : Math.random() > 5 ? 1 : -1;
        final double xOffset = (Math.random() > 5 ? 1 : -1) * Math.random() * range;
        final double yOffset = yModifier * Math.random() * range;
        final double zOffset = (Math.random() > 5 ? 1 : -1) * Math.random() * range;

        return new Vector(xOffset, yOffset, zOffset);
    }

    /**
     * Spawn particles around the Smoke Bomb
     * @param bleedBombObject the smoke bomb
     */
    private void spawnParticlesOnBleedBombDroppedLocation(final BleedBombObject bleedBombObject) {
        final double range = 0.5;

        final Vector bleedBombParticleVector = this.getRandomOffsetWithinRangeAndHeight(range, true);
        final Location bleedBombLoc = bleedBombObject.getLandLoc().clone().add(bleedBombParticleVector); // The point where the smokeball has been thrown at.
        ParticleUtil.runColoredParticles(bleedBombLoc, 5, 255, 0, 0, Effect.COLOURED_DUST); // Spawn random red dust particles at bomb loc
    }

    private boolean isPlayerInRange (final BleedBombObject bleedBombObject, final Player player){
        // Check if player is in range the smoke point

        if (CustomEnchants.softDepend) {
            return !LazarusUtil.arePlayersTeamMates(bleedBombObject.getShooter(), player) && bleedBombObject.getLandLoc().distance(player.getLocation()) < bleedBombObject.getBleedRange();
        }

        return bleedBombObject.getLandLoc().distance(player.getLocation()) < bleedBombObject.getBleedRange();
    }
}
