package com.rubixstudios.customenchants.uhcf;

import com.rubixstudios.customenchants.damage.GankDamage;
import com.rubixstudios.customenchants.damage.GankType;
import com.rubixstudios.uhcf.games.envoy.event.EnvoyStartEvent;
import com.rubixstudios.uhcf.games.envoy.event.EnvoyStopEvent;
import com.rubixstudios.uhcf.games.koth.event.KothStartEvent;
import com.rubixstudios.uhcf.games.koth.event.KothStopEvent;
import com.rubixstudios.uhcf.world.map.eotw.event.EotwStartEvent;
import com.rubixstudios.uhcf.world.map.eotw.event.EotwStopEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Djorr
 * @created 06/01/2022 - 7:08 AM
 * @project custom-enchants
 */
public class GankToggleEvent implements Listener {

    @EventHandler
    public void onEnvoyStart(EnvoyStartEvent event) {
        GankDamage.getInstance().enableGankMode(GankType.ENVOY);
    }

    @EventHandler
    public void onEnvoyStop(EnvoyStopEvent event) {
        GankDamage.getInstance().disableGankMode(GankType.ENVOY);
    }

    @EventHandler
    public void onKoTHStart(KothStartEvent event) {
        GankDamage.getInstance().enableGankMode(GankType.KOTH);
    }

    @EventHandler
    public void onKoTHStop(KothStopEvent event) {
        GankDamage.getInstance().disableGankMode(GankType.KOTH);
    }

    @EventHandler
    public void onEotwStart(EotwStartEvent event) {
        GankDamage.getInstance().enableGankMode(GankType.OVERALL);
    }

    @EventHandler
    public void onEotwStop(EotwStopEvent event) {
        GankDamage.getInstance().disableGankMode(GankType.OVERALL);
    }
}
