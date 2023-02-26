package com.rubixstudios.customenchants.enchantments.bleed;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.Bleed.BleedStopEvent;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.TimeUtil;
import com.rubixstudios.customenchants.value.BleedValue;
import com.rubixstudios.uhcf.timer.TimerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BleedDamage implements Listener {

    public final static @Getter Map<UUID, Long> bleedMap = new HashMap<UUID, Long>();

    private final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

    private final int bloodEffectSeconds = CustomEnchants.getInstance().getConfig().getInt("blood-effect.seconds");

    @EventHandler (priority = EventPriority.HIGHEST)
    private void OnHit(EntityDamageByEntityEvent event){
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;


        final Player victim = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();
        final UUID victimUUID = victim.getUniqueId();
        double damage = event.getDamage();

        final long currentTime = System.currentTimeMillis();

        // TODO, IF ON COOLDOWN CANCEL. Could be easily done with check if difference is lower then 10 then cancel
        if (bleedMap.containsKey(victimUUID)){

            final long prevTime = bleedMap.get(victimUUID);
            final long diffInSeconds = TimeUtil.secondDifference(prevTime, currentTime);

            if (diffInSeconds > bloodEffectSeconds) {
                bleedMap.remove(victimUUID);
                final BleedStopEvent bleedStopEvent = new BleedStopEvent(victim);
                Bukkit.getPluginManager().callEvent(bleedStopEvent);
                return;
            }

            final double newDamage = calculateExtraDamage(event.getDamage(), BleedValue.getBleedPercentage());

            event.setDamage(newDamage);

        } else if (EnchantUtils.hasItemStackEnchantment(damager.getItemInHand(), CustomEnchants.getInstance().getBleedEnch())) {

            if (CustomEnchants.softDepend){
                if (LazarusUtil.isAtKoth(victim)) return;
                if (LazarusUtil.arePlayersTeamMates(victim, damager)) return;
                final TimerManager timerManager = TimerManager.getInstance();
                if (!timerManager.getTearOfBloodTimer().isActive(damager)) {
                    timerManager.getTearOfBloodTimer().activate(damager, 25);
                }else {
                    return;
                }
            }
            eventManager.callCustomItemUseEvent(damager);
            eventManager.callBleedActivateEvent(victim);
            bleedMap.put(victimUUID, currentTime);

            final double newDamage = calculateExtraDamage(damage, BleedValue.getBleedPercentage());

            event.setDamage(newDamage);
        }
    }

    @EventHandler
    private void onPlayerEat(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        if (!itemInHand.getType().equals(Material.SPIDER_EYE)) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (EnchantUtils.hasItemStackEnchantment(itemInHand, CustomEnchants.getInstance().getBleedEnch())) {
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private double calculateExtraDamage(final double damage, final int percentage){
        return damage * percentage;
    }
}
