package com.rubixstudios.customenchants.enchantments.Bomb;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.Bomb.BombActivateEvent;
import com.rubixstudios.customenchants.manager.SmokeBombManager;
import com.rubixstudios.customenchants.objects.BleedBombObject;
import com.rubixstudios.customenchants.objects.SmokeBombObject;
import com.rubixstudios.customenchants.value.BombValue;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnActivate implements Listener {

    @EventHandler
    private void onActivate(BombActivateEvent event){
        final Player player = event.getDropper();
        final Item bomb = event.getBomb();
        // TODO check if this can be cleaner

        if (bomb.hasMetadata(BombValue.smokebomb)){
            final SmokeBombObject smokeBombObject = new SmokeBombObject(player, bomb);
            SmokeBombManager.getInstance().getSmokeBombs().add(smokeBombObject);
        }
        if (bomb.hasMetadata(BombValue.bleedbomb)){
            final BleedBombObject bleedBombObject = new BleedBombObject(player, bomb);
            CustomEnchants.getInstance().getBleedBombManager().getBleedBombs().add(bleedBombObject);
        }
    }
}
