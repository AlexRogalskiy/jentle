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
package com.wildbeeslabs.jentle.algorithms.math;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom math algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CMath {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CMath.class);

    private CMath() {
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

    public static double factorialLog(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input argument n=%i (n > 0 for n!)", n));
        }
        double logSum = 0;
        for (int i = 2; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        return logSum;
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
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicCosine.html">
     * hyperbolic cosine</a> of x.
     *
     * @param x double value for which to find the hyperbolic cosine
     * @return hyperbolic cosine of x
     */
    public static double cosh(double x) {
        return (Math.exp(x) + Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicSine.html">
     * hyperbolic sine</a> of x.
     *
     * @param x double value for which to find the hyperbolic sine
     * @return hyperbolic sine of x
     */
    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the natural <code>log</code> of the <a
     * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
     * Coefficient</a>, "<code>n choose k</code>", the number of
     * <code>k</code>-element subsets that can be selected from an
     * <code>n</code>-element set.
     *
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>0 <= k <= n </code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * </ul>
     *
     * @param n the size of the set
     * @param k the size of the subsets to be counted
     * @return <code>n choose k</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
    public static double binomialCoefficientLog(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i, k=%i (n >= k for binomial coefficient (n,k))", n, k));
        }
        if (n < 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i, k=%i (n >= 0 for binomial coefficient (n,k))", n, k));
        }
        if ((n == k) || (k == 0)) {
            return 0;
        }
        if ((k == 1) || (k == n - 1)) {
            return Math.log((double) n);
        }

        double logSum = 0;
        // n!/k!
        for (int i = k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        // divide by (n-k)!
        for (int i = 2; i <= n - k; i++) {
            logSum -= Math.log((double) i);
        }
        return logSum;
    }

    public static double hypot(double a, double b) {
        double r = 0.0;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        }
        return r;
    }

    public static int log2(int x) {
        int y, v;
        if (x <= 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i (n > 0)", x));
        }
        v = x;
        y = -1;
        while (v > 0) {
            v >>= 1;
            y++;
        }
        return y;
    }
}
