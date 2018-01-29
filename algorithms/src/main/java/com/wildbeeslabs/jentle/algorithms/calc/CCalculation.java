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
package com.wildbeeslabs.jentle.algorithms.calc;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom calculation implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CCalculation {

    private CCalculation() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    // downward dynamic
    public static int fibonacci1(int n) {
        return fibonacci2(n, new int[n + 1]);
    }

    public static int fibonacci2(int i, int[] memo) {
        assert (i >= 0);
        if (0 == i || 1 == i) {
            return i;
        }
        if (0 == memo[i]) {
            memo[i] = fibonacci2(i - 1, memo) + fibonacci2(i - 2, memo);
        }
        return memo[i];
    }

    //upward dynamic
    public static int fibonacci3(int n) {
        assert (n >= 0);
        if (0 == n) {
            return 0;
        } else if (1 == n) {
            return 1;
        }

        int[] memo = new int[n];
        memo[0] = 0;
        memo[1] = 1;
        for (int i = 2; i < n; i++) {
            memo[i] = memo[i - 1] + memo[i - 2];
        }
        return memo[n - 1] + memo[n - 2];
    }

    public static int fibonacci4(int n) {
        assert (n >= 0);
        if (0 == n) {
            return 1;
        }
        int a = 0, b = 1;
        for (int i = 2; i < n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return (a + b);
    }

    public static int finonacci5(int n) {
        assert (n >= 0);
        final int[] fibs = {0, 1};
        final Stream<Integer> fibonacci = Stream.generate(() -> {
            int result = fibs[1];
            int fib3 = fibs[0] + fibs[1];
            fibs[0] = fibs[1];
            fibs[1] = fib3;
            return result;
        });
        return fibonacci.skip(n - 1).findFirst().get();
        //return fibonacci.limit(n).collect(Collectors.toList());
    }

    public static int factorial(int n) {
        assert (n >= 0);
        if (0 == n) {
            return 1;
        }
        int facNumber = 1;
        for (int i = 1; i < n; i++) {
            facNumber *= (i + 1);
        }
        return facNumber;
    }

    public static int gorner(final int[] array, int x) {
        Objects.requireNonNull(array);
        int k = 0, n = array.length - 1, y = array[n];
        while (k++ <= n) {
            y = y * x + array[n - k];
        }
        return y;
    }

    /**
     * Simple Moving Average algorithm implementation
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class CSimpleMovingAverage {

        private final LinkedList<Double> values = new LinkedList<>();

        private int length;
        private double sum = 0;
        private double average = 0;

        /**
         *
         * @param length the maximum length
         */
        public CSimpleMovingAverage(int length) {
            if (length <= 0) {
                throw new IllegalArgumentException("length must be greater than zero");
            }
            this.length = length;
        }

        public double currentAverage() {
            return this.average;
        }

        /**
         * Computes the moving average.
         *
         * @param value The value
         * @return The average
         */
        public synchronized double compute(final double value) {
            if (this.values.size() == this.length && this.length > 0) {
                this.sum -= this.values.removeFirst();
            }
            this.sum += value;
            this.values.addLast(value);
            this.average = this.sum / this.values.size();
            return this.average;
        }
    }
}
