package com.rubixstudios.customenchants.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LocationUtil {

    public static boolean equalPositionVector(Location a, Location b) {
        return a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ();
    }

    public static boolean positionVectorInRange(Location a, Location b, int range) {
        return false;
    }

}
