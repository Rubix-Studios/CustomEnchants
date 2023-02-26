package com.rubixstudios.customenchants.enchantments.handlers;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.CallableEventListener;
import com.rubixstudios.customenchants.utils.CallableFunctionUtil;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.LazarusUtil;
import com.rubixstudios.customenchants.utils.particles.ParticleUtil;
import com.rubixstudios.customenchants.value.PotionBowValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

@Getter @Setter
public abstract class PotionBowEnchantment extends CustomEnchantment implements Listener {

    private PotionEffect potionEffect;

    private int min;
    private int max;

    private int red;
    private int green;
    private int blue;

    private Effect effect;

    private final Random random = new Random();

    private String metadata;

    public PotionBowEnchantment(int id) {
        super(id);
        this.registerListeners();
        this.effect = Effect.POTION_SWIRL;
    }

    private EventExecutor getEntityShootBowListenerExecutor() {
        return CallableFunctionUtil.getEventExecutor(new CallableEventListener<EntityShootBowEvent>() {
            @Override
            public void call(EntityShootBowEvent eventListener) {
                onShoot(eventListener);
            }
        });
    }

    private EventExecutor getEntityDamagebyEntityListenerExecutor() {
        return CallableFunctionUtil.getEventExecutor(new CallableEventListener<EntityDamageEvent>() {
            @Override
            public void call(EntityDamageEvent eventListener) {
                // TODO bandage code, skaffe tory
                if (eventListener instanceof EntityDamageByEntityEvent) {
                    onDamageByEntity((EntityDamageByEntityEvent) eventListener);
                }
            }
        });
    }

    public void initialize(){
        PotionBowValue.potionBowValues.add(this.metadata);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, this, EventPriority.LOWEST, getEntityDamagebyEntityListenerExecutor(), CustomEnchants.getInstance());
        Bukkit.getPluginManager().registerEvent(EntityShootBowEvent.class, this, EventPriority.LOWEST, getEntityShootBowListenerExecutor(), CustomEnchants.getInstance());
    }

    public void onShoot(EntityShootBowEvent event) {
        if (!(EnchantUtils.hasItemStackEnchantment(event.getBow(), this))) return;

        final Arrow arrow = (Arrow) event.getProjectile();

        if (!hasChance()){
            arrow.setMetadata(this.metadata, new FixedMetadataValue(CustomEnchants.getInstance(), true));
            ParticleUtil.createParticleTrailForArrow(arrow, red, green, blue, effect);
            return;
        }

        int chance = random.ints(this.min, (max+1)).findFirst().getAsInt();

        if (chance != 1) return;

        arrow.setMetadata(this.metadata, new FixedMetadataValue(CustomEnchants.getInstance(), true));

        ParticleUtil.createParticleTrailForArrow(arrow, red, green, blue, effect);

    }

    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        final Arrow arrow = (Arrow) event.getDamager();

        if (!(event.getEntity() instanceof Player)) return;
        final Player victim = (Player) event.getEntity();

        final boolean hasMeta = arrow.hasMetadata(this.metadata) && arrow.getMetadata(this.metadata).get(0).asBoolean();
        if (!hasMeta) return;

        if (CustomEnchants.softDepend){
            if (arrow.getShooter() instanceof Player){
                final Player player = (Player) arrow.getShooter();
                if (LazarusUtil.arePlayersTeamMates(player, victim)) return;
            }
        }

        victim.addPotionEffect(potionEffect);
    }

    private boolean hasChance(){
        if (this.min < 0 || this.max <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
