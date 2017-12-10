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

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * Custom array utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CArrayUtils {

    private CArrayUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T extends Comparable<? super T>> int getMinIndex(final T[] array) {
        return getMinIndex(array, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int getMinIndex(final T[] array, final Comparator<? super T> cmp) {
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[minIndex], cmp) < 0) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static <T extends Comparable<? super T>> int getMaxIndex(final T[] array) {
        return getMaxIndex(array, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int getMaxIndex(final T[] array, final Comparator<? super T> cmp) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[maxIndex], cmp) > 0) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static <T> void swap(final T[] array, int m, int n) {
        T temp = array[m];
        array[m] = array[n];
        array[n] = temp;
    }

    public static <T> List<? extends T> getEvenIndexedStrings(final T[] array, final IntPredicate predicate) {
        List<? extends T> result = IntStream
                .range(0, array.length)
                .filter(predicate)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());
        return result;
    }

    public static <T> T[] newArray(final Class<? extends T[]> type, int size) {
        assert (Objects.nonNull(type));
        assert (size >= 0);
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
}
