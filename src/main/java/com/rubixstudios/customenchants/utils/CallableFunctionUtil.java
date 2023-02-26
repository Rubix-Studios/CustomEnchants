package com.rubixstudios.customenchants.utils;

import org.bukkit.event.Event;
import org.bukkit.plugin.EventExecutor;
import com.rubixstudios.customenchants.events.CallableEventListener;

public class CallableFunctionUtil {
    public static <T extends Event> EventExecutor getEventExecutor(CallableEventListener<T> eventListener) {

        return (listener, event) -> {
            eventListener.call((T) event);
        };
    }
}
