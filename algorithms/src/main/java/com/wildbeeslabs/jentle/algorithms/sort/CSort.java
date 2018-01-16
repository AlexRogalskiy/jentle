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

import com.wildbeeslabs.jentle.collections.list.ACList;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.map.CHashMapList;
import com.wildbeeslabs.jentle.collections.utils.CComparatorUtils;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * Custom sorting algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CSort {

    /**
     * Default shell sort steps
     */
    private static final int[] DEFAULT_SHELL_SORT_STEPS = new int[]{701, 301, 132, 57, 23, 10, 4, 1};

    private CSort() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    private static <T> void swap(final T[] array, int left, int right) {
        final T temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }
    //Comparator<String> cmp = (x, y) -> x.compareToIgnoreCase(y);
    public static class CStringComparator extends CUtils.CSortComparator<String> {

        @Override
        public int compare(final String first, final String last) {
            return CComparatorUtils.stringCompareTo(CSort.sortChars(first), CSort.sortChars(last));
        }
    }

    public static String sortChars(final String value) {
        final char[] content = value.toCharArray();
        Arrays.sort(content);
        return new String(content);
    }

    public static <T extends Comparable<? super T>> int binarySearch(final T[] array, final T value) {
        return CSort.binarySearch(array, value, CUtils.<T>getDefaultSortComparator());
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
        return CSort.binarySearchRecursive(array, value, low, high, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int binarySearchRecursive(final T[] array, final T value, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (low >= 0 && high >= 0 && low <= high && low < array.length && high <= array.length);
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
     * @public @module sort
     * @param array Input array.
     * @param <T>
     *
     * @see average - O(n * log(n)) /worst - O(n * n) / memory - O(log(n))
     */
    public static <T extends Comparable<? super T>> void quickSort(final T[] array) {
        Objects.requireNonNull(array);
        CSort.quickSort(array, 0, array.length);
    }

    /**
     *
     * @public @module sort
     * @param array Input array.
     * @param left Left border.
     * @param right Right border.
     * @param <T>
     *
     * @see average - O(n * log(n)) /worst - O(n * n) / memory - O(log(n))
     */
    public static <T extends Comparable<? super T>> void quickSort(final T[] array, int left, int right) {
        CSort.quickSort(array, left, right, CUtils.<T>getDefaultSortComparator());
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param left Left border.
     * @param right Right border.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see average - O(n * log(n)) /worst - O(n * n) / memory - O(log(n))
     */
    public static <T> void quickSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (left >= 0 && right >= 0 && left <= right && left < array.length && right < array.length);
        int index = partition(array, left, right, cmp);
        if (left < index - 1) {
            quickSort(array, left, index - 1, cmp);
        }
        if (index < right) {
            quickSort(array, index, right, cmp);
        }
    }

    private static <T> int partition(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        final T pivot = array[(left + right) / 2];
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

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n * log(n)) average time: O(n * log(n)) worst time: O(n
     * * n) memory: in-place stable: false
     */
    public static <T> void quickSort2(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int sp = 0;
        int[] stackl = new int[array.length];
        int[] stackr = new int[array.length];
        int left, right, i, j;
        stackl[sp] = 1;
        stackr[sp] = array.length - 1;
        while (sp >= 0) {
            left = stackl[sp];
            right = stackr[sp];
            sp--;
            while (left < right) {
                i = left;
                j = right;
                T middle = array[(left + right) / 2];
                while (i < j) {
                    while (Objects.compare(array[i], middle, cmp) < 0) {
                        i++;
                    }
                    while (Objects.compare(array[j], middle, cmp) > 0) {
                        j--;
                    }
                    if (i <= j) {
                        swap(array, i, j);
                        i++;
                        j--;
                    }
                }
                if (i < right) {
                    sp++;
                    stackl[sp] = i;
                    stackr[sp] = right;
                }
                right = j;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param left start index.
     * @param right end index.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void heapSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        left = (left > 0 && left < array.length) ? left : 0;
        right = (right > 0 && right < array.length) ? right : array.length - 1;
        assert (left <= right);
        final PriorityQueue<T> heap = new PriorityQueue<>(right - left, cmp);
        for (int i = left; i <= right; i++) {
            heap.add(array[i]);
        }
        int j = left;
        while (!heap.isEmpty()) {
            array[j++] = heap.poll();
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void heapSort2(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int n = array.length;
        for (int j = n / 2; j > 0; j--) {
            adjust(array, j, n, cmp);
        }
        for (int j = n - 1; j > 0; j--) {
            swap(array, 0, j);
            adjust(array, 1, j, cmp);
        }
    }

    private static <T> void adjust(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        int j = left, k = 2 * left;
        while (k <= right) {
            if (k < right && Objects.compare(array[k - 1], array[k], cmp) < 0) {
                ++k;
            }
            if (Objects.compare(array[j - 1], array[k - 1], cmp) < 0) {
                swap(array, j - 1, k - 1);
            }
            j = k;
            k *= 2;
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void heapSort3(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int n = array.length;
        for (int i = n - 1; i >= 0; i--) {
            siftup(array, i, n, cmp);
        }
        for (int i = n - 1; i > 0;) {
            swap(array, 0, i);
            siftup(array, 0, i--, cmp);
        }
        //for(var i=array.length-1; i>=1; i--) {
        //	swap(array, 0, i, cmp);
        //	siftdown(array, i-1, cmp);
        //}
    }

    private static <T> void siftdown(final T[] array, int value, final Comparator<? super T> cmp) {
        for (int c, order, i = value; (c = 2 * i) <= array.length; i = c) {
            if ((c + 1) <= array.length && Objects.compare(array[c + 1], array[c], cmp) > 0) {
                c++;
            }
            order = Objects.compare(array[i], array[c], cmp);
            if (order >= 0) {
                return;
            }
            swap(array, c, i);
        }
    }

    private static <T> void siftup(final T[] array, int pos, int n, final Comparator<? super T> cmp) {
        int temp, order;
        while ((temp = 2 * pos + 1) < n) {
            order = Objects.compare(array[2 * pos + 2], array[temp], cmp);
            if (2 * pos + 2 < n && order >= 0) {
                temp = 2 * pos + 2;
            }
            if (Objects.compare(array[pos], array[temp], cmp) < 0) {
                swap(array, pos, temp);
                pos = temp;
            } else {
                break;
            }
        }
        //for(var p, i=pos; i>=1 && Objects.compare(array[p=(i/2)], array[i], cmp) < 0; i=p) {
        //	swap(array, p, i);
        //}
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param left Left border.
     * @param right Right border.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n * log(n)) average time: O(n * log(n)) worst time: O(n
     * * n) memory: in-place stable: false
     */
    public static <T> void quickSort3(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (left >= 0 && right >= 0 && left <= right && left < array.length && right < array.length);
        final T pivotValue = array[(left + right) / 2];
        int i = left, j = right;
        while (i <= j) {
            while (Objects.compare(array[i], pivotValue, cmp) < 0) {
                i++;
            }
            while (Objects.compare(array[j], pivotValue, cmp) > 0) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }
        if (left < j) {
            quickSort3(array, left, j, cmp);
        }
        if (i < right) {
            quickSort3(array, i, right, cmp);
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n * log(n)) average time: O(n * log(n)) worst time: O(n
     * * n) memory: in-place stable: false
     */
    public static <T> void quickSort3(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        CSort.quickSort3(array, 0, array.length - 1, cmp);
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param <T>
     *
     * @see best time: O(n * log(n)) average time: O(n * log(n)) worst time: O(n
     * * log(n)) memory: O(n) stable: false
     */
    public static <T extends Comparable<? super T>> void mergeSort(final T[] array) {
        CSort.mergeSort(array, CUtils.<T>getDefaultSortComparator());
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n * log(n)) average time: O(n * log(n)) worst time: O(n
     * * log(n)) memory: O(n) stable: false
     */
    public static <T extends Comparable<? super T>> void mergeSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        final T[] temp = SerializationUtils.clone(array);//CUtils.newArray((Class<? extends T[]>) array.getClass(), array.length);
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

    public static <T extends Comparable<? super T>> void sort(final T[] array, int low, int high) {
        sort(array, low, high, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void sort(final T[] array, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (low >= 0 && high >= 0 && low <= high && low < array.length && high < array.length);
        Arrays.parallelSort(array, low, high, cmp);
    }

    public static <T extends Comparable<? super T>> void sort(final List<? extends T> list) {
        sort(list, CUtils.<T>getDefaultSortComparator());
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
        return sort(set, CUtils.<T>getDefaultSortComparator());
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
        return sortByKeys(map, CUtils.<T>getDefaultSortComparator());
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
        return sortByValues(map, CUtils.<U>getDefaultSortComparator());
    }

    public static void sortBlocks(final String[] array) {
        Objects.requireNonNull(array);
        final CHashMapList<String, String> mapList = new CHashMapList<>();
        for (final String value : array) {
            final String key = CSort.sortChars(value);
            mapList.put(key, value);
        }
        int index = 0;
        for (final String key : mapList.keySet()) {
            final List<String> list = mapList.get(key);
            for (final String value : list) {
                array[index] = value;
                index++;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n) average time: O(n * n) worst time: O(n * n) memory:
     * local stable: true
     *
     */
    public static <T> void insertionSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int length = array.length;
        int iArray = 1, iSortedArray;
        T dataToBeInserted = null;

        while (iArray < length) {
            iSortedArray = iArray - 1;
            dataToBeInserted = array[iArray];

            while (iSortedArray >= 0 && Objects.compare(dataToBeInserted, array[iSortedArray], cmp) < 0) {
                array[iSortedArray + 1] = array[iSortedArray];
                iSortedArray--;
            }
            array[iSortedArray + 1] = dataToBeInserted;
            iArray++;
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param left start index.
     * @param right end index.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void insertionSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        left = (left > 0) ? left : 0;
        right = (right > 0) ? right : array.length - 1;
        assert (left <= right);
        for (int i = left; i < right; i++) {
            for (int j = i; j > 0 && Objects.compare(array[j - 1], array[j], cmp) > 0; j--) {
                swap(array, j - 1, j);
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void gnomeSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int n = array.length;
        int i = 1;
        int j = 2;
        while (i < n) {
            if (Objects.compare(array[i - 1], array[i], cmp) < 0) {
                i = j;
                j++;
            } else {
                swap(array, i - 1, i);
                if (--i == 0) {
                    i = j;
                    j++;
                }
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void cocktailSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int j = array.length - 1;
        int i = 0;
        boolean flag = true;
        while (i < j && flag) {
            flag = false;
            for (int k = i; k < j; k++) {
                if (Objects.compare(array[k], array[k + 1], cmp) > 0) {
                    swap(array, k, k + 1);
                    flag = true;
                }
            }
            j--;
            if (flag) {
                flag = false;
                for (int k = j; k > i; k--) {
                    if (Objects.compare(array[k], array[k - 1], cmp) < 0) {
                        swap(array, k, k - 1);
                        flag = true;
                    }
                }
            }
            i++;
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @return Sorted Array
     * @param <T>
     */
    public static <T> T[] simpleCountingSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int n = array.length;
        int[] count = new int[n];
        T[] res = CUtils.newArray2((Class<? extends T>) array.getClass(), n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Objects.compare(array[i], array[j], cmp) < 0) {
                    count[j]++;
                } else {
                    count[i]++;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            res[count[i]] = array[i];
        }
        return res;
    }

    public static <T> void combSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int n = array.length;
        int gap = n;
        boolean flag;
        do {
            flag = false;
            gap = getGap(gap, 0.77);
            for (int i = 0; i < n - gap; i++) {
                if (Objects.compare(array[i], array[i + gap], cmp) > 0) {
                    flag = true;
                    swap(array, i + gap, i);
                }
            }
        } while (gap > 1 || flag);
    }

    public static <T> int getGap(int gap, double factor) {
        assert (factor >= 0.0 && factor <= 1.0);
        gap = (int) Math.floor(gap * factor);
        if (gap == 9 || gap == 10) {
            gap = 11;
        }
        if (gap < 1) {
            return 1;
        }
        return gap;
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     *
     * @see best time: O(n) average time: O(n * n) worst time: O(n * n) memory:
     * in-place stable: false
     */
    public static <T> void selectionSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int length = array.length;
        for (int outterIndex = 0; outterIndex < length - 1; outterIndex++) {
            int iCurrentMin = outterIndex;
            for (int innerIndex = iCurrentMin + 1; innerIndex < length; innerIndex++) {
                if (Objects.compare(array[iCurrentMin], array[innerIndex], cmp) > 0) {
                    iCurrentMin = innerIndex;
                }
            }
            if (iCurrentMin != outterIndex) {
                swap(array, iCurrentMin, outterIndex);
            }
        }
    }

    public static <T> Stack<T> sort(final Stack<T> s, final Comparator<? super T> cmp) {
        final Stack<T> r = new Stack<>();
        while (!s.isEmpty()) {
            final T tmp = s.pop();
            while (!r.isEmpty() && Objects.compare(r.peek(), tmp, cmp) > 0) {
                s.push(r.pop());
            }
            r.push(tmp);
        }
        return r;
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp Function that defines an alternative sort order. The function
     * should return a negative, zero, or positive value, depending on the
     * arguments.
     * @param <T>
     *
     * @see best time: O(n) average time: O(n * n) worst time: O(n * n) memory:
     * in-place stable: true
     */
    public static <T> void bubbleSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        boolean isSwapHappened = false;
        for (int outterIndex = 0; outterIndex < array.length; outterIndex++) {
            isSwapHappened = false;
            for (int innerIndex = 0; innerIndex < array.length - outterIndex - 1; innerIndex++) {
                if (Objects.compare(array[innerIndex], array[innerIndex + 1], cmp) > 0) {
                    swap(array, innerIndex, innerIndex + 1);
                    isSwapHappened = true;
                }
            }
            if (!isSwapHappened) {
                break;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     *
     */
    public static void bucketSort(final int[] array) {
        Objects.requireNonNull(array);
        if (array.length < 2) {
            return;
        }
        int minVal = array[0];
        int maxVal = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
            }
            if (array[i] < minVal) {
                minVal = array[i];
            }
        }
        final List<List<Integer>> buckets = new ArrayList<>(maxVal - minVal + 1);
        for (int i = 0; i < array.length; i++) {
            buckets.get(array[i] - minVal).add(array[i]);
        }
        int pos = 0;
        for (final List<Integer> bucket : buckets) {
            for (final Integer value : bucket) {
                array[pos++] = value;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp Function that defines an alternative sort order. The function
     * should return a negative, zero, or positive value, depending on the
     * arguments.
     * @param <T>
     */
    public static <T> void shellSort(final T[] array, final Comparator<? super T> cmp) {
        shellSort(array, CSort.DEFAULT_SHELL_SORT_STEPS, cmp);
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param steps Input array of sorting steps
     * @param cmp Function that defines an alternative sort order. The function
     * should return a negative, zero, or positive value, depending on the
     * arguments.
     * @param <T>
     */
    public static <T> void shellSort(final T[] array, int[] steps, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(steps);
        for (int k = 0; k < steps.length; k++) {
            int gap = steps[k];
            for (int i = gap; i < array.length; i += gap) {
                final T current = array[i];
                for (int j = i; j >= gap && Objects.compare(array[j - gap], current, cmp) > 0; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[i] = current;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param step Sorting step.
     * @param cmp Function that defines an alternative sort order. The function
     * should return a negative, zero, or positive value, depending on the
     * arguments.
     * @param <T>
     */
    public static <T> void shellSort(final T[] array, int step, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        step = (step > 0) ? step : array.length;
        int isSorted;
        for (int gap = step / 2; gap > 0; gap /= 2) {
            do {
                isSorted = 0;
                for (int i = 0, j = gap; j < step; i++, j++) {
                    if (Objects.compare(array[i], array[j], cmp) > 0) {
                        swap(array, i, j);
                        isSorted = 1;
                    }
                }
            } while (isSorted > 0);
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void shellSort2(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int gap = 1, n = array.length;
        do {
            gap = 3 * gap + 1;
        } while (gap <= n);
        for (gap /= 3; gap > 0; gap /= 3) {
            for (int i = gap; i < n; i++) {
                T temp = array[i];
                for (int j = i - gap; j >= 0 && Objects.compare(array[j], temp, cmp) > 0; j -= gap) {
                    array[j + gap] = array[j];
                }
                array[i + gap] = temp;
            }
        }
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void hooraSort(final T[] array, final Comparator<? super T> cmp) {
        hooraSort_(array, 0, array.length, cmp);
    }

    /**
     * @public @module sort
     * @param array Input array.
     * @param left Left border.
     * @param right Right border.
     * @param cmp A function that defines an alternative sort order. The
     * function should return a negative, zero, or positive value, depending on
     * the arguments.
     * @param <T>
     */
    public static <T> void hooraSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (left >= 0 && right >= 0 && left <= right && left < array.length && right < array.length);
        left = (left >= 0) ? left : 0;
        right = (right >= 0) ? right : array.length;
        hooraSort_(array, left, right, cmp);
    }

    private static <T> void hooraSort_(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        if (left >= right) {
            return;
        }
        swap(array, left, (left + right) / 2);
        int las = left;
        for (int i = left + 1; i <= right; i++) {
            if (Objects.compare(array[i], array[las], cmp) < 0) {
                swap(array, las, i);
                las++;
            }
        }
        swap(array, left, las);
        hooraSort_(array, left, las - 1, cmp);
        hooraSort_(array, las + 1, right, cmp);
    }

    //Dutch national flag (DNF)
    public static <T> void segregateArray(final T[] array, final T[] borders, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(borders);
        assert (borders.length == 3);
        int low = 0, mid = 0;
        int high = array.length - 1;
        while (mid <= high) {
            if (Objects.compare(array[mid], borders[0], cmp) == 0) {
                swap(array, low, mid);
                low++;
                mid++;
            } else if (Objects.compare(array[mid], borders[1], cmp) == 0) {
                mid++;
            } else if (Objects.compare(array[mid], borders[2], cmp) == 0) {
                swap(array, mid, high);
                high--;
            }
        }
    }

    public static void segregateOddEvenArray(int[] array) {
        int low = 0;
        int high = array.length - 1;
        while (low < high) {
            while (array[low] % 2 == 0 && low < high) {
                low++;
            }
            while (array[high] % 2 != 0 && low < high) {
                high--;
            }
            if (low < high) {
                int temp = array[low];
                array[low] = array[high];
                array[high] = temp;
                low++;
                high--;
            }
        }
    }

    public static void sortBinaryArray(int[] arr) {
        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            while (arr[low] == 0 && low < high) {
                low++;
            }
            while (arr[high] == 1 && low < high) {
                high--;
            }
            if (low < high) {
                int temp = arr[low];
                arr[low] = arr[high];
                arr[high] = temp;
                low++;
                high--;
            }
        }
    }

    public static <T> int search(final T[] array, int left, int right, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (left >= 0 && right >= 0 && left <= right && left < array.length && right < array.length);
        int middle = (left + right) / 2;
        if (Objects.compare(value, array[middle], cmp) == 0) {
            return middle;
        }
        if (right < left) {
            return -1;
        }
        if (Objects.compare(array[left], array[middle], cmp) < 0) {
            if (Objects.compare(value, array[left], cmp) >= 0 && Objects.compare(value, array[middle], cmp) < 0) {
                return search(array, left, middle - 1, value, cmp);
            } else {
                return search(array, middle + 1, right, value, cmp);
            }
        } else if (Objects.compare(array[middle], array[left], cmp) < 0) {
            if (Objects.compare(value, array[middle], cmp) > 0 && Objects.compare(value, array[right], cmp) <= 0) {
                return search(array, middle + 1, right, value, cmp);
            } else {
                return search(array, left, middle - 1, value, cmp);
            }
        } else if (Objects.compare(array[left], array[middle], cmp) == 0) {
            if (Objects.compare(array[middle], array[right], cmp) != 0) {
                return search(array, middle + 1, right, value, cmp);
            } else {
                int result = search(array, left, middle - 1, value, cmp);
                if (-1 == result) {
                    return search(array, middle + 1, right, value, cmp);
                } else {
                    return result;
                }
            }
        }
        return -1;
    }

    public static <T, E extends ACListNode<T, E>> int search(final ACList<T, E> list, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(list);
        int index = 1;
        while (Objects.compare(list.get(index), value, cmp) < 0) {//list.getAt(index) != -1
            index *= 2;
        }
        return binarySearch(list, value, index / 2, index, cmp);
    }

    private static <T, E extends ACListNode<T, E>> int binarySearch(final ACList<T, E> list, final T value, int low, int high, final Comparator<? super T> cmp) {
        while (low <= high) {
            int middle = (low + high) / 2;
            final T midValue = list.get(middle);
            if (Objects.compare(midValue, value, cmp) > 0) {//middle == -1
                high = middle - 1;
            } else if (Objects.compare(midValue, value, cmp) < 0) {
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    public static <T> int search(final T[] array, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        if (Objects.isNull(value)) {
            return -1;
        }
        return search(array, value, 0, array.length - 1, cmp);
    }

    private static <T> int search(final T[] array, final T value, int first, int last, final Comparator<? super T> cmp) {
        assert (first >= 0 && last >= 0 && first < array.length && last < array.length);
        if (first > last) {
            return -1;
        }
        int middle = (last + first) / 2;
        if (Objects.isNull(array[middle])) {
            int left = middle - 1;
            int right = middle + 1;
            while (true) {
                if (left < first && right > last) {
                    return -1;
                } else if (right <= last && Objects.nonNull(array[right])) {
                    middle = right;
                    break;
                } else if (left >= first && Objects.nonNull(array[left])) {
                    middle = left;
                    break;
                }
                right++;
                left--;
            }
        }
        if (Objects.compare(array[middle], value, cmp) == 0) {
            return middle;
        } else if (Objects.compare(array[middle], value, cmp) < 0) {
            return search(array, value, middle + 1, last, cmp);
        } else {
            return search(array, value, first, middle - 1, cmp);
        }
    }

    public static <T> void sortValleyPeak(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        for (int i = 1; i < array.length; i += 2) {
            int bigIndex = maxIndex(array, i - 1, i, i + 1, cmp);
            if (i != bigIndex) {
                swap(array, i, bigIndex);
            }
        }
    }

    private static <T> int maxIndex(final T[] array, int a, int b, int c, final Comparator<? super T> cmp) {
        int len = array.length;
        final T aValue = a >= 0 && a < len ? array[a] : null;
        final T bValue = b >= 0 && b < len ? array[b] : null;
        final T cValue = c >= 0 && c < len ? array[c] : null;
        final T max = Collections.max(Arrays.asList(aValue, bValue, cValue), cmp);
        if (aValue == max) {
            return a;
        } else if (bValue == max) {
            return b;
        }
        return c;
    }
}
