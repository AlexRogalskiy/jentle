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
package com.wildbeeslabs.jentle.algorithms.math;

import java.util.LinkedList;

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

    /**
     * Simple Moving Average algorithm implementation
     */
    @Data
    @EqualsAndHashCode
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
