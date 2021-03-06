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
package com.wildbeeslabs.jentle.collections.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Custom comparators utilities implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CComparatorUtils<T> {

    /**
     * Compare null-safe integer arguments for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int intCompareTo(final T n1, final T n2) {
        return compareTo(Objects.nonNull(n1) ? n1.intValue() : null, Objects.nonNull(n2) ? n2.intValue() : null);
    }

    /**
     * Compare null-safe long arguments for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int longCompareTo(final T n1, final T n2) {
        return compareTo(Objects.nonNull(n1) ? n1.longValue() : null, Objects.nonNull(n2) ? n2.longValue() : null);
    }

    /**
     * Compare null-safe float arguments for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int floatCompareTo(final T n1, final T n2) {
        return compareTo(Objects.nonNull(n1) ? n1.floatValue() : null, Objects.nonNull(n2) ? n2.floatValue() : null);
    }

    /**
     * Compare null-safe double arguments for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int doubleCompareTo(final T n1, final T n2) {
        return compareTo(Objects.nonNull(n1) ? n1.doubleValue() : null, Objects.nonNull(n2) ? n2.doubleValue() : null);
    }

    /**
     * Compare null-safe object arguments as bigDecimal for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int bigDecimalCompareTo(final T n1, final T n2) {
        return bigDecimalCompareTo(Objects.nonNull(n1) ? new BigDecimal(n1.toString()) : null, Objects.nonNull(n2) ? new BigDecimal(n2.toString()) : null);
    }

    /**
     * Compare null-safe bigDecimal arguments for order
     *
     * @param <T>
     * @param n1
     * @param n2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int bigDecimalCompareTo(final BigDecimal n1, final BigDecimal n2) {
        return compareTo(n1, n2);
    }

    /**
     * Compare null-safe object arguments as string for order
     *
     * @param <T>
     * @param obj1
     * @param obj2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T> int stringCompareTo(final T obj1, final T obj2) {
        return stringCompareTo(String.valueOf(obj1), String.valueOf(obj2));
    }

    /**
     * Compare null-safe string arguments for order
     *
     * @param str1
     * @param str2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static int stringCompareTo(final String str1, final String str2) {
        final boolean f1, f2;
        return (f1 = Objects.isNull(str1)) ^ (f2 = Objects.isNull(str2)) ? (f1 ? -1 : 1) : (f1 && f2 ? 0 : str1.compareToIgnoreCase(str2));
    }

    /**
     * Compare null-safe object arguments for order
     *
     * @param <T>
     * @param c1
     * @param c2
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Comparable<? super T>> int compareTo(final T c1, final T c2) {
        final boolean f1, f2;
        return (f1 = Objects.isNull(c1)) ^ (f2 = Objects.isNull(c2)) ? f1 ? -1 : 1 : f1 && f2 ? 0 : c1.compareTo(c2);
    }
}
