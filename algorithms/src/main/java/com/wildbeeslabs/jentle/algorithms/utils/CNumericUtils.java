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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
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

    public static int negate(int num) {
        int neg = 0;
        int newSign = num < 0 ? 1 : -1;
        while (num != 0) {
            neg += newSign;
            num += newSign;
        }
        return neg;
    }

    public static int negate2(int num) {
        int neg = 0;
        int newSign = num < 0 ? 1 : -1;
        int delta = newSign;
        while (num != 0) {
            boolean diffSigns = (num + delta > 0) != (num > 0);
            if (num + delta != 0 && diffSigns) {
                delta = newSign;
            }
            neg += delta;
            num += delta;
            delta += delta;
        }
        return neg;
    }

    public static int minus(int a, int b) {
        return a + negate(b);
    }

    public static int multiply(int a, int b) {
        if (a < b) {
            return multiply(b, a);
        }
        int sum = 0;
        for (int i = abs(b); i > 0; i = minus(i, 1)) {
            sum += a;
        }
        if (b < 0) {
            sum = negate(sum);
        }
        return sum;
    }

    private static int abs(int a) {
        if (a < 0) {
            return negate(a);
        }
        return a;
    }

    public static int divide(int a, int b) throws java.lang.ArithmeticException {
        if (0 == b) {
            throw new java.lang.ArithmeticException("ERROR: divider cannot be null");
        }
        int absa = abs(a);
        int absb = abs(b);
        int product = 0;
        int x = 0;
        while (product + absb <= absa) {
            product += absb;
            x++;
        }
        if ((a < 0 && b < 0) || (a > 0 && b > 0)) {
            return x;
        }
        return negate(x);
    }

    public static int rand7() {
        while (true) {
            int r1 = 2 * rand5();
            int r2 = rand5();
            if (r2 != 4) {
                int rand1 = r2 % 2;
                int num = r1 + rand1;
                if (num < 7) {
                    return num;
                }
            }
        }
    }

    public static int rand72() {
        while (true) {
            int num = 5 * rand5() + rand5();
            if (num < 21) {
                return num % 7;
            }
        }
    }

    public static int rand5() {
        return CNumericUtils.generateRandomInt(0, 5);
    }

    public static int add(int a, int b) {
        while (b != 0) {
            int sum = a ^ b;
            int carry = (a & b) << 1;
            a = sum;
            b = carry;
        }
        return a;
    }

    private static int count2sInRangeDigit(int number, int d) {
        int powerOf10 = (int) Math.pow(10, d);
        int nextPowerOf10 = powerOf10 * 10;
        int right = number % powerOf10;
        int roundDown = number - number % nextPowerOf10;
        int roundUp = roundDown + nextPowerOf10;

        int digit = (number / powerOf10) % 10;
        if (digit < 2) {
            return roundDown / 10;
        } else if (digit == 2) {
            return roundDown / 10 + right + 1;
        }
        return roundUp / 10;
    }

    public static int count2sInRange(int number) {
        int count = 0;
        int len = String.valueOf(number).length();
        for (int digit = 0; digit < len; digit++) {
            count += count2sInRangeDigit(number, digit);
        }
        return count;
    }

    public static int getKthMagicNumber(int k) {
        if (k < 0) {
            return 0;
        }
        int val = 0;
        final Queue<Integer> queue3 = new LinkedList<>();
        final Queue<Integer> queue5 = new LinkedList<>();
        final Queue<Integer> queue7 = new LinkedList<>();
        queue3.add(1);
        for (int i = 0; i <= k; i++) {
            int v3 = queue3.size() > 0 ? queue3.peek() : Integer.MAX_VALUE;
            int v5 = queue5.size() > 0 ? queue5.peek() : Integer.MAX_VALUE;
            int v7 = queue7.size() > 0 ? queue7.peek() : Integer.MAX_VALUE;
            val = Collections.min(Arrays.asList(v3, v5, v7));
            if (val == v3) {
                queue3.remove();
                queue3.add(3 * val);
                queue5.add(5 * val);
            } else if (val == v5) {
                queue5.remove();
                queue5.add(5 * val);
            } else if (val == v7) {
                queue7.remove();
            }
            queue7.add(7 * val);
        }
        return val;
    }
}
