/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.random;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 *
 * Custom random utilities implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CRandom {

    /**
     * Default Random instance
     */
    private static final Random DEFAULT_RANDOM_INSTANCE = new Random();

    public static long generateRandomLong() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextLong();
        //ThreadLocalRandom.current().nextLong();
        //new RandomDataGenerator().getRandomGenerator().nextLong();
    }

    public static int generateRandomInt() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextInt();
        //ThreadLocalRandom.current().nextInt();
        //new RandomDataGenerator().getRandomGenerator().nextInt();
    }

    public static float generateRandomFloat() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat();
        //ThreadLocalRandom.current().nextFloat();
        //new RandomDataGenerator().getRandomGenerator().nextFloat();
    }

    public static double generateRandomDouble() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble();
        //ThreadLocalRandom.current().nextDouble();
        //new RandomDataGenerator().getRandomGenerator().nextDouble();
    }

    public static long generateRandomLong(long bottomLimit, long topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + (long) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit)));
        //ThreadLocalRandom.current().nextLong(bottomLimit, topLimit);
        //new RandomDataGenerator().nextLong(bottomLimit, topLimit);
    }

    public static int generateRandomInt(int bottomLimit, int topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + (int) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit)));
        //ThreadLocalRandom.current().nextInt(bottomLimit, topLimit);
        //new RandomDataGenerator().nextInt(bottomLimit, topLimit);
    }

    public static float generateRandomFloat(float bottomLimit, float topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat() * (topLimit - bottomLimit));
        //float randomFloat = new RandomDataGenerator().getRandomGenerator().nextFloat();
        //bottomLimit + randomFloat * (upLimit - bottomLimit);
    }

    public static double generateRandomDouble(double bottomLimit, double topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit));
        //ThreadLocalRandom.current().nextDouble(upLimit, topLimit);
        //new RandomDataGenerator().nextUniform(bottomLimit, upLimit);
    }

    public static boolean generateRandomBoolean() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextBoolean();
        //ThreadLocalRandom.current().nextBoolean();
        //new RandomDataGenerator().getRandomGenerator().nextBoolean();
    }

    public static double generateRandomGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }

    public static List<Integer> generateRandomIntsInRange(int bottomLimit, int topLimit) {
        return ThreadLocalRandom.current().ints(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }

    public static List<Double> generateRandomDoublesInRange(double bottomLimit, double topLimit) {
        return ThreadLocalRandom.current().doubles(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }

    public static List<Long> generateRandomLongsInRange(long bottomLimit, long topLimit) {
        return ThreadLocalRandom.current().longs(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }
}
