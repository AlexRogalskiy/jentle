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

import java.util.Random;

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
        //new RandomDataGenerator().getRandomGenerator().nextLong();
    }

    public static int generateRandomInt() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextInt();
        //new RandomDataGenerator().getRandomGenerator().nextInt();
    }

    public static float generateRandomFloat() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat();
        //new RandomDataGenerator().getRandomGenerator().nextFloat();
    }

    public static double generateRandomDouble() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble();
        //new RandomDataGenerator().getRandomGenerator().nextDouble();
    }

    public static long generateRandomLong(long bottomLimit, long upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (long) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextLong(bottomLimit, upLimit);
    }

    public static int generateRandomInt(int bottomLimit, int upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (int) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextInt(bottomLimit, upLimit);
    }

    public static float generateRandomFloat(float bottomLimit, float upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat() * (upLimit - bottomLimit));
        //float randomFloat = new RandomDataGenerator().getRandomGenerator().nextFloat();
        //bottomLimit + randomFloat * (upLimit - bottomLimit);
    }

    public static double generateRandomDouble(double bottomLimit, double upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (upLimit - bottomLimit));
        //new RandomDataGenerator().nextUniform(bottomLimit, upLimit);
    }

    public static boolean generateRandomBoolean() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextBoolean();
        //new RandomDataGenerator().getRandomGenerator().nextBoolean();
    }
}
