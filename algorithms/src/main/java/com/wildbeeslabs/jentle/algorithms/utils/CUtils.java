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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Custom utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CUtils {

    private CUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> Collection<T> join(final Collection<T> first, final Collection<T> second, final Predicate<? super T> filter) {
        return Stream.concat(first.stream(), second.stream()).filter(filter).collect(Collectors.toList());
    }

    public static String join(final Collection<String> collection, final String delimiter) {
        return collection.stream().collect(Collectors.joining(delimiter));
    }

    public static <K, V> String join(final Map<K, V> map, final String keyValueDelimiter, final String delimiter) {
        return map.entrySet().stream().map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(Collectors.joining(delimiter));
    }

    public static String[] join(final String[] first, final String[] second) {
        return Stream.concat(Arrays.stream(first), Arrays.stream(second)).toArray(String[]::new);
    }

    public static String join(final String[] array, final String delimiter) {
        return Arrays.stream(array).collect(Collectors.joining(delimiter));
    }

    public static Map<Integer, List<String>> splitByLength(final String[] array) {
        return Arrays.stream(array).filter(Objects::nonNull).collect(Collectors.groupingBy(String::length));
    }

    public static Collection<String> split(final String value, final String delimiter) {
        return Arrays.stream(value.split(delimiter))
                .map(String::trim)
                .filter(next -> !next.isEmpty())
                .collect(Collectors.toList());
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
}