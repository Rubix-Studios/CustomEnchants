package com.rubixstudios.customenchants.damage;

import com.rubixstudios.customenchants.utils.particles.ParticleUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.rubixstudios.customenchants.events.Gank.Stage1;
import com.rubixstudios.customenchants.events.Gank.Stage2;
import com.rubixstudios.customenchants.events.Gank.Stage3;

public class GankParticles implements Listener {

    private @Getter @Setter float speed = 1f;
    private @Getter @Setter int amount = 1;
    private @Getter @Setter float range = 16f;



    // YELLOW PARTICLES
    //  float colorX = 246f;
    //  float colorY = 255f;
    //   float colorZ = 76f;

    // ORANGE PARTICLES
    //  float colorX = 255f;
    //  float colorY = 174f;
    //  float colorZ = 0;

    // RED PARTICLES
    //  float colorX = 255f;
    //  float colorY = 0f;
    //  float colorZ = 0f;

    // TODO refactor this with using enums (ParticleColor)


    @EventHandler
    private void Stage1(Stage1 event){
        final Player player = event.getPlayer();

        final float red = 246f;
        final float green = 255f;
        final float blue = 76f;


        ParticleUtil.runColoredParticlesCubeAtPlayer(player, 10, red, green, blue, Effect.COLOURED_DUST);
    }

    @EventHandler
    private void Stage2(Stage2 event){
        final Player player = event.getPlayer();

        final float red = 255f;
        final float green = 174f;
        final float blue = 0f;


        ParticleUtil.runColoredParticlesCubeAtPlayer(player, 10, red, green, blue, Effect.COLOURED_DUST);

    }

    @EventHandler
    private void Stage3(Stage3 event){
        final Player player = event.getPlayer();

        final float red = 255f;
        final float green = 0f;
        final float blue = 0f;


        ParticleUtil.runColoredParticlesCubeAtPlayer(player, 10, red, green, blue, Effect.COLOURED_DUST);

    }
}
