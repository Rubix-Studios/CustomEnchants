package com.rubixstudios.customenchants.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static long secondDifference(long millisFirst, long millisSecond) {
        return TimeUnit.MILLISECONDS.toSeconds(millisSecond - millisFirst);
    }
}
