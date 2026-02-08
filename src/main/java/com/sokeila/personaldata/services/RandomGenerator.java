package com.sokeila.personaldata.services;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class RandomGenerator {

    private final Random randomGenerator;

    public RandomGenerator() {
        this.randomGenerator = new Random();
    }

    protected <T> T getRandomValue(List<T> list) {
        Objects.requireNonNull(list, "List can't be null");
        if(list.isEmpty()) {
            throw new IllegalArgumentException("List size is 0");
        }

        return list.get(randomGenerator.nextInt(list.size()));
    }

    protected Random getRandomGenerator() {
        return randomGenerator;
    }
}
