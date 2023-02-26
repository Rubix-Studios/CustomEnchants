package com.rubixstudios.customenchants.events.CustomItem;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomItemUseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public CustomItemUseEvent(Player player) {
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
