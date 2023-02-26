package com.rubixstudios.customenchants.events;

import org.bukkit.event.Event;

public interface CallableEventListener<T extends Event> {
    void call(T eventListener);
}



