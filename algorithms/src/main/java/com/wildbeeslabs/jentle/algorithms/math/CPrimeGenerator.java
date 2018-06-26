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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom prime generator implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CPrimeGenerator {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CPrimeGenerator.class);

    private static boolean[] crossOut;
    private static int[] result;

    public static int[] generatePrimeNumbers(final int maxVal) {
        if (maxVal < 2) {
            return new int[0];
        }
        init(maxVal);
        crossOut();
        save();
        return result;
    }

    private static void init(int maxVal) {
        crossOut = new boolean[maxVal + 1];
        for (int i = 2; i < crossOut.length; i++) {
            crossOut[i] = false;
        }
    }

    private static void crossOut() {
        int limit = getLimit();
        for (int i = 2; i <= limit; i++) {
            if (notCrossed(i)) {
                crossOutOf(i);
            }
        }
    }

    private static int getLimit() {
        return Double.valueOf(Math.sqrt(crossOut.length)).intValue();
    }

    private static void crossOutOf(int i) {
        for (int mult = 2 * i; mult < crossOut.length; mult += i) {
            crossOut[mult] = true;
        }
    }

    private static boolean notCrossed(int i) {
        return (false == crossOut[i]);
    }

    private static void save() {
        result = new int[getResultSet()];
        for (int j = 0, i = 2; i < crossOut.length; i++) {
            if (notCrossed(i)) {
                result[j++] = i;
            }
        }
    }

    private static int getResultSet() {
        int count = 0;
        for (int i = 2; i < crossOut.length; i++) {
            if (notCrossed(i)) {
                count++;
            }
        }
        return count;
    }
}
