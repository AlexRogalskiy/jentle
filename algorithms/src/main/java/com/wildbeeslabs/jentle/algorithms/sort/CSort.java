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
package com.wildbeeslabs.jentle.algorithms.sort;

import com.wildbeeslabs.jentle.algorithms.utils.CComparator;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * Custom sorting implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSort {

    private CSort() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Default sort comparator
     */
    public static final CSortComparator DEFAULT_SORT_COMPARATOR = new CSortComparator();

    protected static class CSortComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(final T first, final T last) {
            return CComparator.compareTo(first, last);
        }
    }

    public static <T> int binarySearch(final T[] array, T value) {
        return CSort.binarySearch(array, value, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> int binarySearch(T[] array, T value, Comparator<? super T> cmp) {
        assert (Objects.nonNull(array));
        int low = 0, high = array.length - 1, middle;
        while (low <= high) {
            middle = (int) Math.floor((low + high) / 2);
            if (Objects.compare(array[middle], value, cmp) < 0) {
                low = middle + 1;
            } else if (Objects.compare(array[middle], value, cmp) > 0) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    public static <T> int binarySearchRecursive(final T[] array, final T value, int low, int high) {
        return CSort.binarySearchRecursive(array, value, low, high, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> int binarySearchRecursive(final T[] array, final T value, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        if (low > high) {
            return -1;
        }
        int middle = (int) Math.floor((low + high) / 2);
        if (Objects.compare(array[middle], value, cmp) < 0) {
            return binarySearchRecursive(array, value, middle + 1, high, cmp);
        } else if (Objects.compare(array[middle], value, cmp) > 0) {
            return binarySearchRecursive(array, value, low, middle - 1, cmp);
        } else {
            return middle;
        }
    }

    /**
     *
     * @param <T>
     * @param array
     * @param left
     * @param right
     * @see
     *
     * average - O(n*log(n)) /worst - O(n*n) / memory - O(log(n))
     */
    public static <T> void quickSort(final T[] array, int left, int right) {
        CSort.quickSort(array, left, right, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> void quickSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int index = partition(array, left, right, cmp);
        if (left < index - 1) {
            quickSort(array, left, index - 1, cmp);
        }
        if (index < right) {
            quickSort(array, index, right, cmp);
        }
    }

    private static <T> int partition(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        T pivot = array[(int) Math.floor((left + right) / 2)];
        while (left <= right) {
            while (Objects.compare(array[left], pivot, cmp) < 0) {
                left++;
            }
            while (Objects.compare(array[right], pivot, cmp) > 0) {
                right--;
            }

            if (left <= right) {
                swap(array, left, right);
                left++;
                right--;
            }
        }
        return left;
    }

    private static <T> void swap(final T[] array, int left, int right) {
        final T temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }

    /**
     *
     * @param <T>
     * @param array
     * @see
     *
     * worst - O(n*log(n)) / average - O(n*log(n)) / memory - O(n)
     */
    public static <T> void mergeSort(final T[] array) {
        CSort.mergeSort(array, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> void mergeSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        T[] temp = CSort.newArray((Class<? extends T>) array.getClass(), array.length);
        mergeSort(array, temp, 0, array.length - 1, cmp);
    }

    private static <T> void mergeSort(final T[] array, final T[] temp, int low, int high, final Comparator<? super T> cmp) {
        if (low < high) {
            int middle = (int) Math.floor((low + high) / 2);
            mergeSort(array, temp, low, middle, cmp);
            mergeSort(array, temp, middle + 1, high, cmp);
            merge(array, temp, low, middle, high, cmp);
        }
    }

    private static <T> void merge(final T[] array, final T[] temp, int low, int middle, int high, final Comparator<? super T> cmp) {
        for (int i = low; i <= high; i++) {
            temp[i] = array[i];
        }

        int tempLeft = low;
        int tempRight = middle + 1;
        int current = low;

        while (tempLeft <= middle && tempRight <= high) {
            if (Objects.compare(temp[tempLeft], temp[tempRight], cmp) <= 0) {
                array[current] = temp[tempLeft];
                tempLeft++;
            } else {
                array[current] = temp[tempRight];
                tempRight++;
            }
            current++;
        }

        int residue = middle - tempLeft;
        for (int i = 0; i <= residue; i++) {
            array[current + i] = temp[tempLeft + i];
        }
    }

    private static <T> T[] newArray(final Class<? extends T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }
}
