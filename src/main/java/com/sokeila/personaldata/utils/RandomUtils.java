package com.sokeila.personaldata.utils;

public class RandomUtils {

    protected RandomUtils() {
    }

    public static int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }
}
