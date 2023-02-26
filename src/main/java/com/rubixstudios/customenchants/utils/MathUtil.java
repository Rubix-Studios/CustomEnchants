package com.rubixstudios.customenchants.utils;

public class MathUtil {
    public static double clamp(final double value, final double min, final double max) {
        return Math.min(Math.max(value, min), max);
    }
}