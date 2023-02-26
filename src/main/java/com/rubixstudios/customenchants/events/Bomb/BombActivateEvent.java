package com.rubixstudios.customenchants.events.Bomb;

import lombok.Getter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BombActivateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final @Getter Player dropper;
    private final @Getter Item bomb;

    public BombActivateEvent(Player dropper, Item bomb) {
        this.bomb = bomb;
        this.dropper = dropper;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
