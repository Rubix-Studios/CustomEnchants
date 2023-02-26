package com.rubixstudios.customenchants.enchantments.handlers;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.CallableEventListener;
import com.rubixstudios.customenchants.potion.PotionDuration;
import com.rubixstudios.customenchants.utils.CallableFunctionUtil;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.PlayerUtil;
import com.rubixstudios.customenchants.utils.PotionUtil;
import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public abstract class PotionEnchantment extends CustomEnchantment implements Listener {
    private final PotionEffect potionEffect;



    public PotionEnchantment(int id, PotionEffect potionEffect) {
        super(id);
        this.potionEffect = potionEffect;
        this.registerListeners();
    }

    private EventExecutor getJoinListenerExecutor() {
        return CallableFunctionUtil.getEventExecutor(new CallableEventListener<PlayerJoinEvent>() {
            @Override
            public void call(PlayerJoinEvent eventListener) {
                onJoin(eventListener);
            }
        });
    }

    private EventExecutor getQuitListenerExecutor() {
        return CallableFunctionUtil.getEventExecutor(new CallableEventListener<PlayerQuitEvent>() {
            @Override
            public void call(PlayerQuitEvent eventListener) {
                onQuit(eventListener);
            }
        });
    }

    private EventExecutor getEquipListenerExecutor() {
        return CallableFunctionUtil.getEventExecutor(new CallableEventListener<EquipmentSetEvent>() {
            @Override
            public void call(EquipmentSetEvent eventListener) {
                onEquip(eventListener);
            }
        });
    }

    /**
     * Registers listeners
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, this, EventPriority.NORMAL, getJoinListenerExecutor(), CustomEnchants.getInstance());
        Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, this, EventPriority.NORMAL, getQuitListenerExecutor(), CustomEnchants.getInstance());
        Bukkit.getPluginManager().registerEvent(EquipmentSetEvent.class, this, EventPriority.NORMAL, getEquipListenerExecutor(), CustomEnchants.getInstance());
    }

    /**
     * Will trigger when player equips armor
     * @param event that's being called on equip
     */
    private void onEquip(EquipmentSetEvent event){ // TODO fix buffs
        final Player player = (Player) event.getHumanEntity();

        if (!PlayerUtil.hasConnection(player)) return; // On join will handle this

        // Remove enchant buff from player
        if (EnchantUtils.hasItemStackEnchantment(event.getPreviousItem(), this)){
            player.removePotionEffect(this.potionEffect.getType());
            PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());

            // Add cached buff
            if (potionDuration.isPotionDurationInCache(this.potionEffect.getType())) {
                PotionDuration.PotionInfo potionInfo = potionDuration.getPotionDurationFromCache(this.potionEffect.getType());

                player.addPotionEffect(new PotionEffect(this.potionEffect.getType(), potionInfo.getDuration(), potionInfo.getAmplifier()));

                potionDuration.removePotionDurationFromCache(this.potionEffect.getType());
            }
        }

        // Add enchant buff to player
        if (EnchantUtils.hasItemStackEnchantment(event.getNewItem(), this)) {
            if (player.hasPotionEffect(this.potionEffect.getType()) && !EnchantUtils.hasArmorEnchant(this, player.getInventory().getArmorContents(), event.getNewItem())){
                final MobEffect currentMobEffect = PotionUtil.getMobEffect(this.potionEffect.getType(), player);
                if (currentMobEffect != null) {
                    final int currentDuration = currentMobEffect.getDuration();
                  //  Bukkit.broadcastMessage("Saving current duration: " + currentDuration);

                 //   Bukkit.broadcastMessage("Get Current duration: " + currentDuration);
                 //   Bukkit.broadcastMessage("Get Current cached duration: " + currentDuration);

                    PotionDuration potionDuration = CustomEnchants.getInstance().getPotionDuration(player.getUniqueId());
                    potionDuration.addPotionDurationToCache(this.potionEffect.getType(), currentDuration, currentMobEffect.getAmplifier());
                } else {
                  //  Bukkit.broadcastMessage("MOB EFFECT NULL - NOT GOOD");
                }
            }

            player.removePotionEffect(this.potionEffect.getType());
            player.addPotionEffect(this.potionEffect);

        }
    }

    /**
     * Will trigger when player joins
     * @param event that's being called on join
     */
    private void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        if (getArmorPotionEnchantCount(player) > 0) {
            event.getPlayer().addPotionEffect(this.potionEffect);
        }

    }

    private void onQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        if (getArmorPotionEnchantCount(player) > 0) {
            event.getPlayer().removePotionEffect(this.potionEffect.getType());

        }
    }

    private long getArmorPotionEnchantCount(Player player){
        return Arrays.stream(player.getInventory().getArmorContents()).filter(armorEnchant -> EnchantUtils.hasItemStackEnchantment(armorEnchant, this)).count();
    }
}
