package com.rubixstudios.customenchants.enchantments.handlers;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import com.rubixstudios.customenchants.value.IEnchantmentUsageValues;
import com.rubixstudios.uhcf.timer.TimerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

public abstract class MeleeEnchantment extends CustomEnchantment implements Listener {
    private final PotionEffect potionEffect;
    private final ParticleEffect particleEffect;

    private final Random random = new Random();

    private final IEnchantmentUsageValues usageValues = EnchantmentValue.usageValues;

    //TODO make that the enchantment itself can modify those values
    private @Getter @Setter int min;
    private @Getter @Setter int max;

    private @Getter @Setter float offsetX = .3f;
    private @Getter @Setter float offsetY = 1f;
    private @Getter @Setter float offsetZ = .3f;

    private @Getter @Setter float speed = .2f;
    private @Getter @Setter int amount = 100;
    private @Getter @Setter float range = 16f;



    private final EventExecutor damageExecutor = (listener, event) -> {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
            onHit(entityDamageByEntityEvent);
        }
    };

    public MeleeEnchantment(int id, PotionEffect potionEffect, ParticleEffect particleEffect) {
        super(id);
        this.potionEffect = potionEffect;
        this.particleEffect = particleEffect;
        this.registerListeners();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, this, EventPriority.NORMAL, damageExecutor, CustomEnchants.getInstance());
    }

    /**
     * On the player hit event
     * @param event damage event
     */
    private void onHit(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        final Player victim = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();

        final ItemStack itemInHand = damager.getItemInHand();

        if (!EnchantUtils.hasItemStackEnchantment(itemInHand, this)) return;
        if (damager.getItemInHand().getType() == Material.ENCHANTED_BOOK) return;
        final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

        if (CustomEnchants.softDepend){
            if (LazarusUtil.arePlayersTeamMates(victim, damager)) return;
            if (EnchantUtils.hasItemStackEnchantment(itemInHand, CustomEnchants.getInstance().getPoisonEnch())) {
                final TimerManager timerManager = TimerManager.getInstance();
                if (!timerManager.getThelepCooldownTimer().isActive(damager)) {
                    timerManager.getThelepCooldownTimer().activate(damager, 15);

                } else {
                    return;
                }
            }

        }

        if (!hasChance()){
            if (EnchantUtils.hasItemStackEnchantment(itemInHand, CustomEnchants.getInstance().getUsageEnch())){
                eventManager.callCustomItemUseEvent(damager);
            }

            addEffect(victim);
            return;
        }

        int chance = random.ints(this.min, (max+1)).findFirst().getAsInt();

        if (chance != 1) return;

        if (EnchantUtils.hasItemStackEnchantment(itemInHand, CustomEnchants.getInstance().getUsageEnch())){
            eventManager.callCustomItemUseEvent(damager);
        }

        addEffect(victim);
    }

    private void addEffect(Player victim){
        victim.addPotionEffect(this.potionEffect);
        this.particleEffect.display(this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount, victim.getLocation(), this.range);
    }

    private boolean hasChance(){
        if (this.min < 0 || this.max <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
