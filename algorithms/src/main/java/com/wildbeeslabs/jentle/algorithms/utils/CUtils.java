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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CUtils {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CUtils.class);

    private CUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> List<T> union(final T[] array1, final T[] array2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        int length1 = array1.length;
        int length2 = array2.length;

        int index1 = 0, index2 = 0;
        while (index1 < length1 && index2 < length2) {
            if (Objects.compare(array1[index1], array2[index2], cmp) < 0) {
                result.add(array1[index1]);
                index1++;

            } else if (Objects.compare(array1[index1], array2[index2], cmp) > 0) {
                result.add(array2[index2]);
                index2++;
            } else {
                result.add(array1[index1]);
                index1++;
                index2++;
            }
        }
        while (index1 < length1) {
            result.add(array1[index1++]);
        }
        while (index2 < length2) {
            result.add(array2[index2++]);
        }
        return result;
    }

    public static <T> void merge(final T[] first, final T[] second, int lastFirst, int lastSecond, final Comparator<? super T> cmp) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        assert (lastFirst >= 0 && lastSecond >= 0 && lastFirst < first.length && lastSecond < second.length);
        int indexF = lastFirst - 1;
        int indexS = lastSecond - 1;
        int indexMerged = lastSecond + lastFirst - 1;

        while (indexS >= 0) {
            if (indexF >= 0 && Objects.compare(first[indexF], second[indexS], cmp) > 0) {
                first[indexMerged] = first[indexF];
                indexF--;
            } else {
                first[indexMerged] = second[indexS];
                indexS--;
            }
            indexMerged--;
        }
    }

    public static <T> List<T> intersect(final T[] array1, final T[] array2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        int length1 = array1.length;
        int length2 = array2.length;
        int index1 = 0, index2 = 0;
        while (index1 < length1 && index2 < length2) {
            if (Objects.compare(array1[index1], array2[index2], cmp) < 0) {
                index1++;
            } else if (Objects.compare(array1[index1], array2[index2], cmp) > 0) {
                index2++;
            } else {
                result.add(array1[index1]);
                index1++;
                index2++;
            }
        }
        return result;
    }

    public static <T> T getInstance(final Class<? extends T> clazz) {
        try {
            return (T) clazz.getComponentType().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error("ERROR: cannot initialize class instance=" + clazz + ", message=" + ex.getMessage());
        }
        return null;
    }
}
