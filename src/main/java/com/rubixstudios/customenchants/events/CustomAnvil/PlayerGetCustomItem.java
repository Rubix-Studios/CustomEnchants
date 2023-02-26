package com.rubixstudios.customenchants.events.CustomAnvil;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGetCustomItem extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerGetCustomItem(Player player) {
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
