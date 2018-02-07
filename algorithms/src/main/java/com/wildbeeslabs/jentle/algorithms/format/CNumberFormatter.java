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
package com.wildbeeslabs.jentle.algorithms.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Helper class to handle number format operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CNumberFormatter {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CNumberFormatter.class);
    /**
     * Default number format pattern
     */
    public static final String DEFAULT_NUMBER_FORMAT_PATTERN = "#.##";
    /**
     * Default number format instance
     */
    private static final DecimalFormat numberFormat = new DecimalFormat(CNumberFormatter.DEFAULT_NUMBER_FORMAT_PATTERN);

    private CNumberFormatter() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static String format(final Double value) {
        return numberFormat.format(value);
    }

    public static String formatByPattern(final Double value, final String pattern) {
        final DecimalFormat formatter = new DecimalFormat(pattern);
        return formatter.format(value);
    }

    /**
     * Round the given value to the specified number of decimal places. The
     * value is rounded using the {@link BigDecimal#ROUND_HALF_UP} method.
     *
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @return the rounded value.
     * @since 1.1
     */
    public static double round(double x, int scale) {
        return CNumberFormatter.round(x, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Round the given value to the specified number of decimal places. The
     * value is rounded using the given method which is any method defined in
     * {@link BigDecimal}.
     *
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @param roundingMethod the rounding method as defined in
     * {@link BigDecimal}.
     * @return the rounded value.
     * @since 1.1
     */
    public static double round(double x, int scale, int roundingMethod) {
        try {
            return (new BigDecimal(Double.toString(x))
                    .setScale(scale, roundingMethod))
                    .doubleValue();
        } catch (NumberFormatException ex) {
            if (Double.isInfinite(x)) {
                return x;
            } else {
                return Double.NaN;
            }
        }
    }
}