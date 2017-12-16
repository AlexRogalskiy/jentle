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

import com.wildbeeslabs.jentle.collections.utils.CUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.math3.util.Pair;

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
        return getMinIndex(array, CUtils.<T>getDefaultSortComparator());
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
        return getMaxIndex(array, CUtils.<T>getDefaultSortComparator());
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

    public static int getMagic(int[] array) {
        Objects.requireNonNull(array);
        return getMagic(array, 0, array.length - 1);
    }

    public static int getMagic(int[] array, int start, int end) {
        if (end < start) {
            return -1;
        }
        int midIndex = (start + end) / 2;
        int midValue = array[midIndex];
        if (midValue == midIndex) {
            return midIndex;
        }

        int leftIndex = Math.min(midIndex - 1, midValue);
        int left = getMagic(array, start, leftIndex);
        if (left >= 0) {
            return left;
        }
        int rightIndex = Math.max(midIndex + 1, midValue);
        int right = getMagic(array, rightIndex, end);
        return right;
    }

    public static int findMinDiff(final int[] array1, final int[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        Arrays.sort(array1);
        Arrays.sort(array2);
        int a = 0;
        int b = 0;
        int difference = Integer.MAX_VALUE;
        while (a < array1.length && b < array2.length) {
            if (Math.abs(array1[a] - array2[b]) < difference) {
                difference = Math.abs(array1[a] - array2[b]);
            }
            if (array1[a] < array2[b]) {
                a++;
            } else {
                b++;
            }
        }
        return difference;
    }

    public static <T> Result findUnsortedSequence(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int endLeft = findEndOfLeftSubsequence(array, cmp);
        if (endLeft >= array.length - 1) {
            return null;
        }
        int startRight = findStartOfRightSubsequence(array, cmp);
        int maxIndex = endLeft;
        int minIndex = startRight;
        for (int i = endLeft + 1; i < startRight; i++) {
            if (Objects.compare(array[i], array[minIndex], cmp) < 0) {
                minIndex = i;
            }
            if (Objects.compare(array[i], array[maxIndex], cmp) > 0) {
                maxIndex = i;
            }
        }
        int leftIndex = shrinkLeft(array, minIndex, endLeft, cmp);
        int rightIndex = shrinkRight(array, maxIndex, startRight, cmp);
        return new Result(leftIndex, rightIndex);
    }

    private static <T> int findEndOfLeftSubsequence(final T[] array, final Comparator<? super T> cmp) {
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[i - 1], cmp) < 0) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    private static <T> int findStartOfRightSubsequence(final T[] array, final Comparator<? super T> cmp) {
        for (int i = array.length - 2; i >= 0; i--) {
            if (Objects.compare(array[i], array[i + 1], cmp) > 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private static <T> int shrinkLeft(final T[] array, int minIndex, int start, final Comparator<? super T> cmp) {
        final T comp = array[minIndex];
        for (int i = start - 1; i >= 0; i--) {
            if (Objects.compare(array[i], comp, cmp) <= 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private static <T> int shrinkRight(final T[] array, int maxIndex, int start, final Comparator<? super T> cmp) {
        final T comp = array[maxIndex];
        for (int i = start; i < array.length; i++) {
            if (Objects.compare(array[i], comp, cmp) >= 0) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Result {

        public int leftIndex = 0;
        public int rightIndex = 0;

        public String toFormatString() {
            return "( " + this.leftIndex + ", " + this.rightIndex + " )";
        }
    }

    public static int getMaxSum(int[] array) {
        int maxSum = 0;
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    public static int[] findSwapValues(int[] array1, int[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        final Integer target = getTarget(array1, array2);
        if (Objects.isNull(target)) {
            return null;
        }
        return findDifference(array1, array2, target);
    }

    private static Integer getTarget(int[] array1, int[] array2) {
        int sum1 = Arrays.stream(array1).sum();
        int sum2 = Arrays.stream(array2).sum();
        if ((sum1 - sum2) % 2 != 0) {
            return null;
        }
        return (sum1 - sum2) / 2;
    }

    private static int[] findDifference(int[] array1, int[] array2, int target) {
        final Set<Integer> content = getContents(array2);
        for (int one : array1) {
            int two = one - target;
            if (content.contains(two)) {
                return new int[]{one, two};
            }
        }
        return null;
    }

    private static int[] findDifference2(int[] array1, int[] array2, int target) {
        int a = 0;
        int b = 0;
        while (a < array1.length && b < array2.length) {
            int diff = array1[a] - array2[b];
            if (diff == target) {
                return new int[]{array1[a], array2[b]};
            } else if (diff < target) {
                a++;
            } else {
                b++;
            }
        }
        return null;
    }

    private static Set<Integer> getContents(int[] array) {
        final Set<Integer> set = new HashSet<>();
        for (int a : array) {
            set.add(a);
        }
        return set;
    }

    public static List<Pair<Integer, Integer>> getPairSum(int[] array, int sum) {
        Objects.requireNonNull(array);
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        final Set<Integer> elements = new HashSet<>();
        for (int x : array) {
            int complement = sum - x;
            if (elements.contains(complement) && !elements.contains(x)) {
                result.add(new Pair<>(x, complement));
            }
            elements.add(x);
        }
        return result;
    }

    public static List<Pair<Integer, Integer>> getPairSum2(int[] array, int sum) {
        Objects.requireNonNull(array);
        Arrays.sort(array);
        int first = 0;
        int last = array.length - 1;
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        while (first < last) {
            int s = array[first] + array[last];
            if (s == sum) {
                result.add(new Pair<>(array[first], array[last]));
                first++;
                last--;
            } else {
                if (s < sum) {
                    first++;
                } else {
                    last--;
                }
            }
        }
        return result;
    }
}
