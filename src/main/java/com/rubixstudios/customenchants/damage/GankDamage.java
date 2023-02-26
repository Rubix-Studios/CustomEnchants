package com.rubixstudios.customenchants.damage;

import com.rubixstudios.uhcf.UHCF;
import com.rubixstudios.uhcf.factions.Faction;
import com.rubixstudios.uhcf.factions.claim.ClaimManager;
import com.rubixstudios.uhcf.factions.type.EnvoyFaction;
import com.rubixstudios.uhcf.factions.type.KothFaction;
import com.rubixstudios.uhcf.games.koth.KothManager;
import com.rubixstudios.uhcf.games.koth.RunningKoth;
import com.rubixstudios.uhcf.utils.Color;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.value.GankValue;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GankDamage implements Listener {
    private static @Getter GankDamage instance;

    public GankDamage(){
        instance = this;
    }

    private @Getter @Setter int maxDiff = CustomEnchants.getInstance().getConfig().getInt("gank-damage.ThresholdInSeconds");;

    private final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event){
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        final Player victim = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();

        // if (!enabled) return;
        if (CustomEnchants.softDepend){
            if (LazarusUtil.arePlayersTeamMates(victim, damager)) return;
        }

        final GankType gankType = this.getGankType(victim);
        if (gankType == null) return;

        final UUID victimUUID = victim.getUniqueId();
        final UUID damagerUUID = damager.getUniqueId();

        final long currentTime = System.currentTimeMillis();
        final double damage = event.getDamage();

        if (!gankType.getGankMap().containsKey(victimUUID)){

            final GPlayer gPlayer = new GPlayer();

            gankType.getGankMap().put(victimUUID, gPlayer);
            gPlayer.addNewDamager(damager, currentTime);

            return;
        }

        final GPlayer gPlayer = gankType.getGankMap().get(victimUUID);

        // TODO make the variable names more readable
        final long lastTimeStamp = gPlayer.getLastTimeStamp();
        final long lowestTimeStamp = gPlayer.getLowestTimeStamp();

        final long diffInSeconds = TimeUtil.secondDifference(lastTimeStamp, currentTime);
        final long diffBetweenHitsInSecs = TimeUtil.secondDifference(lowestTimeStamp, currentTime);


        if (diffInSeconds > maxDiff){
            gankType.getGankMap().remove(victimUUID);
            gPlayer.clearMap();
            return;
        }

        if (diffBetweenHitsInSecs > maxDiff){
           final Player player = Bukkit.getPlayer(gPlayer.getLateHitter());

           gPlayer.removeDamagerByPlayer(player);
        }

        final int damagerCount = gPlayer.getDamagerCount();
        final double extraDamage = calculateExtraDamage(damagerCount, damage, victim);

        event.setDamage(damage + extraDamage);

        gPlayer.addNewDamager(damager, currentTime);
    }

    public void toggleGankmode(CommandSender sender, GankType gankType){
        boolean enabled = gankType.isEnabled();

        String color;
        if (enabled) {
            this.disableGankMode(gankType);
            color = "&a";
        } else {
            this.enableGankMode(gankType);
            color = "&c";
        }

        sender.sendMessage(Color.translate("&aCE &8» &aGankMode for <name> = <status>"
                .replace("<name>", gankType.getName())
                .replace("<status>", Color.translate(color + enabled))
        ));

        if(!enabled) {
            gankType.getGankMap().clear();
        }
    }

    public void enableGankMode(GankType gankType) {
        gankType.setEnabled(true);
    }

    public void disableGankMode(GankType gankType) {
        gankType.setEnabled(false);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event){
        removePlayerFromGankMap(event.getEntity());
    }

    @EventHandler
    private void onLogout(PlayerQuitEvent event){
        removePlayerFromGankMap(event.getPlayer());
    }

    private void removePlayerFromGankMap(Player player){
        final GankType gankType = this.getGankType(player);
        if (gankType == null) return;

        if (!gankType.getGankMap().containsKey(player.getUniqueId())) return;
        final GPlayer gPlayer = gankType.getGankMap().get(player.getUniqueId());

        gPlayer.clearMap();
        gankType.getGankMap().remove(player.getUniqueId());
    }

    private double calculateExtraDamage(int damagerCount, double damage, Player victim){

        double extraDamage;
        if (damagerCount >= 6){
            final GankType gankType = this.getGankType(victim);

            assert gankType != null;
            final GPlayer gPlayer = gankType.getGankMap().get(victim.getUniqueId());
            final Player player = Bukkit.getPlayer(gPlayer.getLateHitter());

            gPlayer.removeDamagerByPlayer(player);
            damagerCount = 5;
        }
        switch (damagerCount){
            case 2:
                eventManager.callGankStage1(victim);
                extraDamage = damage * GankValue.getStage1();
                break;
            case 3:
                eventManager.callGankStage2(victim);
                extraDamage = damage * GankValue.getStage2();
                break;
            case 5:
                eventManager.callGankStage3(victim);
                extraDamage = damage * GankValue.getStage3();
                break;
            default:
                extraDamage = 0;
        }
        return extraDamage;
    }

    private GankType getGankType(Player victim) {
        final Faction factionAt = ClaimManager.getInstance().getFactionAt(victim.getLocation());

        if (GankType.OVERALL.isEnabled()) return GankType.OVERALL;
        if (GankType.KOTH.isEnabled() && factionAt instanceof KothFaction && LazarusUtil.isPlayerAtActiveKoth(victim)) return GankType.KOTH;
        if (GankType.ENVOY.isEnabled() && factionAt instanceof EnvoyFaction && LazarusUtil.isPlayerAtActiveEnvoy(victim)) return GankType.ENVOY;

        return null;
    }
}
