package com.rubixstudios.customenchants.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static boolean hasConnection(Player player) {
        if (player == null) return false;
        return ((CraftPlayer)player).getHandle().playerConnection != null;
    }

}
