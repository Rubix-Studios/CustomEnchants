package com.rubixstudios.customenchants.utils;

import org.bukkit.Location;

public class RangeUtil {

    public static Location getRangeLocation(Location blockLocation, int range) {
        final int x = blockLocation.getBlockX();
        final int y = blockLocation.getBlockY();
        final int z = blockLocation.getBlockZ();

        return blockLocation.add(x, y, z).add(
                (Math.random() > x ? 1 : -1) * Math.random() * range,
                y,
                (Math.random() > z ? 1 : -1) * Math.random() * range);
    }
}
