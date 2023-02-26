package com.rubixstudios.customenchants.uhcf;

import com.rubixstudios.uhcf.classes.event.ArcherTagShootEvent;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.rubixstudios.customenchants.particletrailbuilder.ParticleColor;
import com.rubixstudios.customenchants.particletrailbuilder.ParticleTrailBuilder;

public class ArcherTagEvent implements Listener {

    @EventHandler
    private void onArcherTag(ArcherTagShootEvent event){
        final Arrow arrow = event.getArrow();
        final ParticleTrailBuilder particleTrailBuilder = new ParticleTrailBuilder(arrow);
        particleTrailBuilder.setColor(ParticleColor.RED).setEffect(Effect.COLOURED_DUST).build();
        arrow.setCritical(false);
    }
}
