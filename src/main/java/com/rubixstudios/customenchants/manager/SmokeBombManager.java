package com.rubixstudios.customenchants.manager;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.potion.PotionDuration;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.PotionUtil;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.objects.SmokeBombObject;

import java.util.*;

public class SmokeBombManager implements Listener {

    private static @Getter SmokeBombManager instance;

    private final @Getter BukkitRunnable setupTask;
    private final @Getter List<SmokeBombObject> smokeBombs;
    private final @Getter List<UUID> leftPlayers;
    private final @Getter List<SmokeBombObject> removalCache;
    private final @Getter List<UUID> currentPlayersWithEffectCache;
    private final @Getter EventManager eventManager = CustomEnchants.getInstance().getEventManager();

    public SmokeBombManager() {
        instance = this;

        this.smokeBombs = new ArrayList<>(); // Spawner cache
        this.leftPlayers = new ArrayList<>(); // Left Players cache
        this.removalCache = new ArrayList<>(); // Removal cache
        this.currentPlayersWithEffectCache = new ArrayList<>(); // current Players with invis effect

        this.setupTask = setupTasks();
        this.setupTask.runTaskTimer(CustomEnchants.getInstance(), 0L, 5L);

        Bukkit.getPluginManager().registerEvents(this, CustomEnchants.getInstance());
    }

    private BukkitRunnable setupTasks() {
        final int timeAfterRemoval = 5; // 5 sec

        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!removalCache.isEmpty()) {
                    removalCache.forEach(smokeBombs::remove);
                }

                // Run all particle spawners
                if (smokeBombs.isEmpty()) return; // Check if smokebombs cache is empty then return.

                smokeBombs.forEach((s) -> {
                    s.setActive(true);
                    spawnParticlesAtEachSmokeBomb(s); // Spawns particles for each smokebomb in the cache.
                    for (final Player player : Bukkit.getOnlinePlayers()){
                        if (CustomEnchants.softDepend) {
                            if(!LazarusUtil.arePlayersTeamMates(s.getShooter(), player)) continue;
                        }
                        if (isPlayerInRange(s, player)){
                            if (currentPlayersWithEffectCache.contains(player.getUniqueId())) continue;

                            if (!s.hasInvisPlayer(player)) {
                                s.addInvisPlayer(player);
                                hidePlayerAndArmor(player);
                            }

                        }else {
                            if (!currentPlayersWithEffectCache.contains(player.getUniqueId())) continue;
                            if (s.hasInvisPlayer(player)) {
                                s.removeInvisPlayer(player);
                                showPlayerAndArmor(player);
                            }
                        }

                    }

                    final boolean afterTime = s.getTimeThrown() + 1000 * timeAfterRemoval < System.currentTimeMillis(); // If the thrown time is higher then timeAfterRemoval
                    if (afterTime) {  // If the thrown time is higher then timeAfterRemoval
                        s.setActive(false);
                        clearSmokebombPlayers(s);
                        removeSmokeBomb(s);
                    }
                });
            }
        };
    }

    private void clearSmokebombPlayers(final SmokeBombObject s){
        for (int i = 0; i < s.getInvisPlayers().size(); i++){

            final Player player = Bukkit.getPlayer(s.getInvisPlayers().get(i));

            if (player == null) continue;
            if (!player.isOnline()) continue;

            if (hasPlayerSpecialEffect(player)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

            if (hasInvisPotionInCache(player)) {
                giveInvisPotionFromCacheToPlayer(player);
                removePotionDurationFromCache(player);
            }
            currentPlayersWithEffectCache.remove(player.getUniqueId());
        }
    }


    /**
     * Removes all playerUUID keys in currentPlayersWithEffectCache
     * @param playerUUID the player unique id
     */
    private void removeDuplicatesOfCache(UUID playerUUID){
        for (int i = 0; i < currentPlayersWithEffectCache.size(); i++) {
            final Player player1 = Bukkit.getPlayer(currentPlayersWithEffectCache.get(i));

            if (player1 == null) continue;
            if (!player1.isOnline()) continue;

            if (player1.getUniqueId().equals(playerUUID)){
                currentPlayersWithEffectCache.remove(player1.getUniqueId());
                i--; // make sure you dont skip a element
            }
        }
    }

    /**
     * Removes the Smoke Bomb
     * @param smokeBombObject the Smoke Bomb
     */
    private void removeSmokeBomb(final SmokeBombObject smokeBombObject) {
        smokeBombObject.getItem().getItemStack().setType(Material.AIR);
        smokeBombObject.getItem().remove();
        removalCache.add(smokeBombObject);
    }

    /**
     * Handles all particles on each Smoke Bomb
     * @param smokeBombObject the Smoke Bomb
     */
    private void spawnParticlesAtEachSmokeBomb(final SmokeBombObject smokeBombObject) {
        final int range = smokeBombObject.getSmokeRange(); // Range of spawning smokes from the point the smokebomb lands
        this.spawnRandomParticlesInsideSmokeArea(smokeBombObject, range);

        this.spawnParticlesOnSmokeBombDroppedLocation(smokeBombObject);
        this.spawnParticlesOnSmokeBombShooter(smokeBombObject);
    }

    private boolean isPlayerInRange(final SmokeBombObject smokeBombObject, final Player player){
        // Check if player is in range the smoke point

        return smokeBombObject.getLandLoc().distance(player.getLocation()) < smokeBombObject.getSmokeRange();
    }


    /**
     * Spawn random particles inside the smoke area
     * @param smokeBombObject the Smoke Bomb
     * @param range the range
     */
    private void spawnRandomParticlesInsideSmokeArea(final SmokeBombObject smokeBombObject, int range) {
        final Vector pointVector = this.getRandomOffsetWithinRangeAndHeight(range, true);
        final Location randomPointInRangeFromImpactPosition = smokeBombObject.getLandLoc().clone().add(pointVector); // Calculate random point for particles
        this.spawnParticles(ParticleEffect.CLOUD, null, null,  randomPointInRangeFromImpactPosition, 5); // Spawn random cloud particles inside smoke range
    }

    /**
     * Spawn particles around the Smoke Bomb
     * @param smokeBombObject the smoke bomb
     */
    private void spawnParticlesOnSmokeBombDroppedLocation(final SmokeBombObject smokeBombObject) {
        final double range = 0.5;

        final Vector smokeBombParticleVector = this.getRandomOffsetWithinRangeAndHeight(range, true);
        final Location smokeBombLoc = smokeBombObject.getLandLoc().clone().add(smokeBombParticleVector); // The point where the smokeball has been thrown at.
        this.spawnParticles(null, ParticleEffect.SNOWBALL, ParticleEffect.CLOUD, smokeBombLoc, 5); // Spawn random snowball break particles
    }

    /**
     * Spawn particles around the Smoke Bomb shooter
     * @param smokeBombObject the smokebomb
     */
    private void spawnParticlesOnSmokeBombShooter(final SmokeBombObject smokeBombObject) {
        final double range = 1;

        final Location smokeObject = smokeBombObject.getLandLoc(); // SmokeBombObject location
        final Location shooterLoc = smokeBombObject.getShooter().getLocation(); // SmokeBombObject shooter location

        final Vector playerVector = this.getRandomOffsetWithinRangeAndHeight(range, true);
        final Location shooterLocParticles = shooterLoc.clone(); // new Shooter location
        if (smokeObject.distance(shooterLoc) < smokeBombObject.getSmokeRange()) {
            this.spawnParticles(ParticleEffect.CLOUD, null, null, shooterLocParticles.add(playerVector), 1);
        }
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
     * Make player and his armor invisible
     * @param player the player
     */
    private void hidePlayerAndArmor(final Player player) {
     /*   if (hasPlayerSpecialEffect(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            return;
        } */

        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
            saveInvisPotionDuration(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        final PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
        player.addPotionEffect(effect);


        this.currentPlayersWithEffectCache.add(player.getUniqueId());
    }

    /**
     * Make player and his armor visible
     * @param player the player
     */
    private void showPlayerAndArmor(final Player player) {
        if (hasPlayerSpecialEffect(player)) {

            player.removePotionEffect(PotionEffectType.INVISIBILITY);

            if (hasInvisPotionInCache(player)) {
                giveInvisPotionFromCacheToPlayer(player);
                removePotionDurationFromCache(player);
            }

            this.currentPlayersWithEffectCache.remove(player.getUniqueId());
        }
    }

    /**
     * Spawn smoke particles on specific location
     * @param location the location
     * @param amount the amount of particles you wnat to spawn
     */
    private void spawnParticles(final ParticleEffect firstParticle, final ParticleEffect secondParticle, final ParticleEffect thirdPaticle, final Location location, final int amount) {
        if (firstParticle != null) firstParticle.display(0f, 0f, 0f, 0.1f, amount, location, 32);
        if (secondParticle != null) secondParticle.display(0f, 0f, 0f, 0.01f, amount, location, 32);
        if (thirdPaticle != null) thirdPaticle.display(0f, 0f, 0f, 0.01f, amount, location, 32);
    }

    private boolean hasPlayerSpecialEffect(Player player){
        final MobEffect currentMobEffect = PotionUtil.getMobEffect(PotionEffectType.INVISIBILITY, player);

        if (currentMobEffect == null) return false;

        final int currentDuration = currentMobEffect.getDuration();

        return currentDuration >= 100000;
    }

    private void saveInvisPotionDuration(Player player){
        final MobEffect currentMobEffect = PotionUtil.getMobEffect(PotionEffectType.INVISIBILITY, player);
        if (currentMobEffect != null) {
            final int currentDuration = currentMobEffect.getDuration();

            if (currentDuration >= 100000) {
                return;
            }

            PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());
            potionDuration.addPotionDurationToCache(PotionEffectType.INVISIBILITY, currentDuration, currentMobEffect.getAmplifier());
        }
    }

    private void giveInvisPotionFromCacheToPlayer(Player player) {
        final PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());

        if (potionDuration.isPotionDurationInCache(PotionEffectType.INVISIBILITY)) {
            final PotionDuration.PotionInfo potionInfo = potionDuration.getPotionDurationFromCache(PotionEffectType.INVISIBILITY);

            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, potionInfo.getDuration(), potionInfo.getAmplifier()));
        }
    }

    private void removePotionDurationFromCache(Player player){
        final PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());
        if(potionDuration.isPotionDurationInCache(PotionEffectType.INVISIBILITY)){
            potionDuration.removePotionDurationFromCache(PotionEffectType.INVISIBILITY);
        }
    }

    private long getCurrentPotionDurationFromCache(Player player){
        final PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());

        if (!potionDuration.isPotionDurationInCache(PotionEffectType.INVISIBILITY)) return 0;

        final PotionDuration.PotionInfo potionInfo = potionDuration.getPotionDurationFromCache(PotionEffectType.INVISIBILITY);
        return potionInfo.getDuration();
    }

    private boolean hasInvisPotionInCache(Player player){
        final PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());

        return potionDuration.isPotionDurationInCache(PotionEffectType.INVISIBILITY);
    }

    /**
     * Checks if the player that thrown an smoke bomb leaves..
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player leftPlayer = event.getPlayer();
        if (currentPlayersWithEffectCache.isEmpty()) return;

        final UUID playerUUID = leftPlayer.getUniqueId();

        if (!currentPlayersWithEffectCache.contains(playerUUID)) return;

        this.smokeBombs.forEach((s) -> {
            if (!s.getInvisPlayers().contains(playerUUID)) return; // TODO kijk welke check van deze (376) of line 377 beter is, ik dnek 376 maar moet ik even nachecken
            //   if (!LazarusUtil.arePlayersTeamMates(s.getShooter(), leftPlayer)) return;
            //  if (!s.getShooter().getUniqueId().equals(playerUUID)) return; // Check if the shooter UUID is the same as the leftPlayer UUID

            s.removeInvisPlayer(leftPlayer);
            this.showPlayerAndArmor(leftPlayer);

            this.leftPlayers.add(playerUUID); // Add player to leftplayers cache
        });

        //TODO CHECK IF COMBATLOGGER SPAWNS
    }

    /**
     * Checks if an left player joins the server.
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (this.leftPlayers.isEmpty()) return; // Checks if left players cache is empty then return

        final Player joinedPlayer = event.getPlayer();
        final UUID playerUUID = joinedPlayer.getUniqueId();
        if (!this.leftPlayers.contains(playerUUID)) return;
        this.leftPlayers.remove(playerUUID);

        this.smokeBombs.forEach((s) -> {
            if (!s.getInvisPlayers().contains(playerUUID)) return;
            //    if (!LazarusUtil.arePlayersTeamMates(s.getShooter(), joinedPlayer)) return;
            // if (!s.getShooter().getUniqueId().equals(playerUUID)) return; // Check if the shooter UUID is the same as the leftPlayer UUID
            if (!s.isActive()){
                s.removeInvisPlayer(joinedPlayer);
                this.showPlayerAndArmor(joinedPlayer);
                return;
            }

            this.hidePlayerAndArmor(joinedPlayer);
            s.addInvisPlayer(joinedPlayer);
        });
    }

    public void removePotionEffectsFromPlayersInCache(){
        for (UUID uuid : this.currentPlayersWithEffectCache){
            final Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            if (!player.isOnline()) continue;

            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    public void addPotionEffectsFromPotionCache(){
        for (UUID uuid : this.currentPlayersWithEffectCache) {
            final Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            if (!player.isOnline()) continue;

            if (hasPlayerSpecialEffect(player)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

            if (!hasInvisPotionInCache(player)) continue;

            giveInvisPotionFromCacheToPlayer(player);
            removePotionDurationFromCache(player);
        }
    }
}
