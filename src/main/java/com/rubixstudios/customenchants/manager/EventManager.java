package com.rubixstudios.customenchants.manager;

import com.rubixstudios.customenchants.events.Bleed.BleedActivateEvent;
import com.rubixstudios.customenchants.events.Bomb.BombActivateEvent;
import com.rubixstudios.customenchants.events.CustomAnvil.CantCreateCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.CreateCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.PlayerGetCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.PlayerOpensCustomAnvil;
import com.rubixstudios.customenchants.events.CustomItem.CustomItemUseEvent;
import com.rubixstudios.customenchants.events.Gank.Stage1;
import com.rubixstudios.customenchants.events.Gank.Stage2;
import com.rubixstudios.customenchants.events.Gank.Stage3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class EventManager {

    public void callCustomItemUseEvent(Player player){
        final CustomItemUseEvent customItemUseEvent = new CustomItemUseEvent(player);
        Bukkit.getPluginManager().callEvent(customItemUseEvent);
    }

    public void callCantCreateCustomItem(Player player){
        final CantCreateCustomItem cantCreateCustomItem = new CantCreateCustomItem(player);
        Bukkit.getPluginManager().callEvent(cantCreateCustomItem);
    }

    public void callCreateCustomItem(Player player){
        final CreateCustomItem createCustomItem = new CreateCustomItem(player);
        Bukkit.getPluginManager().callEvent(createCustomItem);
    }

    public void callPlayerGetCustomItem(Player player){
        final PlayerGetCustomItem playerGetCustomItem = new PlayerGetCustomItem(player);
        Bukkit.getPluginManager().callEvent(playerGetCustomItem);
    }

    public void callPlayerOpensCustomAnvil(Player player){
        final PlayerOpensCustomAnvil playerOpensCustomAnvil = new PlayerOpensCustomAnvil(player);
        Bukkit.getPluginManager().callEvent(playerOpensCustomAnvil);
    }

    public void callBleedActivateEvent(Player damagedPlayer){
        final BleedActivateEvent bleedActivateEvent = new BleedActivateEvent(damagedPlayer);
        Bukkit.getPluginManager().callEvent(bleedActivateEvent);
    }

    public void callBombActivateEvent(Player dropper, Item bomb){
        final BombActivateEvent bombActivateEvent = new BombActivateEvent(dropper, bomb);
        Bukkit.getPluginManager().callEvent(bombActivateEvent);

    }

    public void callGankStage1(Player player){
        final Stage1 stage1 = new Stage1(player);
        Bukkit.getPluginManager().callEvent(stage1);
    }

    public void callGankStage2(Player player){
        final Stage2 stage2 = new Stage2(player);
        Bukkit.getPluginManager().callEvent(stage2);
    }

    public void callGankStage3(Player player){
        final Stage3 stage3 = new Stage3(player);
        Bukkit.getPluginManager().callEvent(stage3);
    }
}
