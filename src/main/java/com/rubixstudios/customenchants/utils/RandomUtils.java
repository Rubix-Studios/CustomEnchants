package com.rubixstudios.customenchants.utils;

import java.util.Random;

public class RandomUtils {

    public static int getRandomBetween(int value) {
        return new Random().nextInt(value);
    }
}
