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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

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
    public static final CSort.CSortComparator DEFAULT_SORT_COMPARATOR = new CSort.CSortComparator();

    protected static class CSortComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(final T first, final T last) {
            return CComparator.compareTo(first, last);
        }
    }

    public static <T extends Comparable<? super T>> CSort.CSortComparator<T> getDefaultSortComparator() {
        return new CSort.CSortComparator<>();
    }

    public static <T extends Comparable<? super T>> int binarySearch(final T[] array, T value) {
        return CSort.binarySearch(array, value, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int binarySearch(final T[] array, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
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

    public static <T extends Comparable<? super T>> int binarySearchRecursive(final T[] array, final T value, int low, int high) {
        return CSort.binarySearchRecursive(array, value, low, high, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int binarySearchRecursive(final T[] array, final T value, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (low >= 0 && high >= 0);
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
    public static <T extends Comparable<? super T>> void quickSort(final T[] array, int left, int right) {
        CSort.quickSort(array, left, right, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void quickSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (left >= 0 && right >= 0 && left <= right);
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
    public static <T extends Comparable<? super T>> void mergeSort(final T[] array) {
        CSort.mergeSort(array, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void mergeSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        final T[] temp = ArrayUtils.clone(array);//CSort.newArray((Class<? extends T[]>) array.getClass(), array.length);
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

//    private static <T> T[] newArray(final Class<? extends T> type, int size) {
//        Objects.requireNonNull(type);
//        assert (size >= 0);
//        return (T[]) Array.newInstance(type, size);
//    }
//    private static <T> T[] newArray(final Class<? extends T[]> type, int size) {
//        Objects.requireNonNull(type);
//        assert (size >= 0);
//        return type.cast(Array.newInstance(type.getComponentType(), size));
//    }
    public static <T extends Comparable<? super T>> void sort(final T[] array, int low, int high) {
        sort(array, low, high, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void sort(final T[] array, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (low >= 0 && high >= 0 && low <= high);
        Arrays.parallelSort(array, low, high, cmp);
    }

    public static <T extends Comparable<? super T>> void sort(final List<? extends T> list) {
        sort(list, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void sort(final List<? extends T> list, final Comparator<? super T> cmp) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(cmp);
        Collections.sort(list, cmp);
    }

    public static <T extends Comparable<? super T>> Set<? extends T> sort(final Set<? extends T> set, final Comparator<? super T> cmp) {
        Objects.requireNonNull(set);
        Objects.requireNonNull(cmp);
        final List<? extends T> list = new ArrayList<>(set);
        sort(list, cmp);
        return new LinkedHashSet<>(list);
    }

    public static <T extends Comparable<? super T>> Set<? extends T> sort(final Set<? extends T> set) {
        return sort(set, CSort.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>, U> Map<? extends T, ? extends U> sortByKeys(final Map<T, U> map, final Comparator<? super T> cmp) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(cmp);
        final List<Map.Entry<T, U>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, Map.Entry.<T, U>comparingByKey(cmp));
        final Map<T, U> sortedMap = new LinkedHashMap<>();
        entries.stream().forEach((entry) -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });
        return sortedMap;
    }

    public static <T extends Comparable<? super T>, U> Map<? extends T, ? extends U> sortByKeys(final Map<? extends T, ? extends U> map) {
        return sortByKeys(map, CSort.<T>getDefaultSortComparator());
    }

    public static <T, U extends Comparable<? super U>> Map<? extends T, ? extends U> sortByValues(final Map<T, U> map, final Comparator<? super U> cmp) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(cmp);
        final List<Map.Entry<T, U>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, Map.Entry.<T, U>comparingByValue(cmp));
        final Map<T, U> sortedMap = new LinkedHashMap<>();
        entries.stream().forEach((entry) -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });
        return sortedMap;
    }

    public static <T, U extends Comparable<? super U>> Map<? extends T, ? extends U> sortByValues(final Map<T, U> map) {
        return sortByValues(map, CSort.<U>getDefaultSortComparator());
    }
}
