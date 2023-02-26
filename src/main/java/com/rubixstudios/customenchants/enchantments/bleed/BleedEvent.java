package com.rubixstudios.customenchants.enchantments.bleed;

import com.rubixstudios.customenchants.events.Bleed.BleedActivateEvent;
import com.rubixstudios.customenchants.events.Bleed.BleedStopEvent;
import com.rubixstudios.customenchants.utils.particles.ParticleUtil;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.rubixstudios.customenchants.particletrailbuilder.ParticleColor;

public class BleedEvent implements Listener {

    @EventHandler
    private void onActivate(final BleedActivateEvent event){

        final Player damagedPlayer = event.getDamagedPlayer();

        ParticleUtil.runColoredParticlesCubeAtPlayer(damagedPlayer, 10, 255, 0, 0, Effect.COLOURED_DUST);

        // location, sound, volume, pitch
        damagedPlayer.playSound(damagedPlayer.getLocation(), Sound.CAT_HISS, 1f, 1f);
    }

    @EventHandler
    private void onStop(final BleedStopEvent event){
        final Player damagedPlayer = event.getDamagedPlayer();
        ParticleUtil.runColoredParticlesCubeAtPlayer(damagedPlayer, 10, ParticleColor.LIMEGREEN.getRed(), ParticleColor.LIMEGREEN.getGreen(), ParticleColor.LIMEGREEN.getBlue(), Effect.COLOURED_DUST);
    }

}
