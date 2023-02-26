package com.rubixstudios.customenchants.events.Gank;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class Stage2 extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public Stage2(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer(){ return this.player; }
}
