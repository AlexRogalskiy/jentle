/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 *
 * Custom numeric utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CNumericUtils {

    private CNumericUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static long generateRandomLong() {
        return new Random().nextLong();
        //new RandomDataGenerator().getRandomGenerator().nextLong();
    }

    public static int generateRandomInt() {
        return new Random().nextInt();
        //new RandomDataGenerator().getRandomGenerator().nextInt();
    }

    public static float generateRandomFloat() {
        return new Random().nextFloat();
        //new RandomDataGenerator().getRandomGenerator().nextFloat();
    }

    public static double generateRandomDouble() {
        return new Random().nextDouble();
        //new RandomDataGenerator().getRandomGenerator().nextDouble();
    }

    public static long generateRandomLong(long bottomLimit, long upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (long) (Math.random() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextLong(bottomLimit, upLimit);
    }

    public static int generateRandomInt(int bottomLimit, int upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (int) (new Random().nextFloat() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextInt(bottomLimit, upLimit);
    }

    public static float generateRandomFloat(float bottomLimit, float upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + new Random().nextFloat() * (upLimit - bottomLimit));
        //float randomFloat = new RandomDataGenerator().getRandomGenerator().nextFloat();
        //bottomLimit + randomFloat * (upLimit - bottomLimit);
    }

    public static double generateRandomDouble(double bottomLimit, double upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + new Random().nextDouble() * (upLimit - bottomLimit));
        //new RandomDataGenerator().nextUniform(bottomLimit, upLimit);
    }

    public static boolean generateRandomBoolean() {
        return new Random().nextBoolean();
        //new RandomDataGenerator().getRandomGenerator().nextBoolean();
    }

    public static double round(double value, int places) {
        assert (places > 0);
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
        //Precision.round(PI, 3);
    }

    public static int numOfDigits(int value) {
        assert (value > 0);
        return (int) (Math.log10(value) + 1);
    }

    public static boolean hasPrimeNumbers(int n) {
        int sqrt = (int) Math.sqrt(n);
        for (int i = 2; i <= sqrt; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
