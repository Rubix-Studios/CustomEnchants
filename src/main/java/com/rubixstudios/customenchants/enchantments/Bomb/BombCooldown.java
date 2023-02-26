package com.rubixstudios.customenchants.enchantments.Bomb;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.BombEnchantment;
import com.rubixstudios.customenchants.events.Bomb.BombCooldownTimerEvent;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.uhcf.timer.TimerManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BombCooldown implements Listener {

    @EventHandler
    private void setCooldown(BombCooldownTimerEvent event){
        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();

        if (CustomEnchants.softDepend){
            if (!setCooldownByEnchant(player)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean setCooldownByEnchant(final Player player){
        final TimerManager timerManager = TimerManager.getInstance();
        // TODO check of dit beter kan
        boolean succeeded = false;
        for (Enchantment ench : player.getItemInHand().getEnchantments().keySet()){
            if (!(ench instanceof BombEnchantment)) continue;
            if (EnchantUtils.enchantComparer(ench, CustomEnchants.getInstance().getBleedBombEnchantment())){
                if (!timerManager.getBleedBombCooldownTimer().isActive(player)) {
                    timerManager.getBleedBombCooldownTimer().activate(player, 30);
                    succeeded = true;
                }
            }
            if (EnchantUtils.enchantComparer(ench, CustomEnchants.getInstance().getSmokeBombEnch())){
                if (!timerManager.getSmokeBombCooldownTimer().isActive(player)) {
                    timerManager.getSmokeBombCooldownTimer().activate(player, 25);
                    succeeded = true;
                }
            }
        }
        return succeeded;
    }

}
