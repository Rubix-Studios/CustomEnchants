package com.rubixstudios.customenchants.events.Bleed;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BleedActivateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player damagedPlayer;

    public BleedActivateEvent(Player damagedPlayer) {
        this.damagedPlayer = damagedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getDamagedPlayer(){ return this.damagedPlayer; }
}