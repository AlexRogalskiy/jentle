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
public final class CSort {

    private CSort() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

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
     * @param <T>
     * @param array
     * @param left
     * @param right
     * @see
     *
     * average - O(n*log(n)) /worst - O(n*n) / memory - O(log(n))
     */
    public static <T extends Comparable<? super T>> void quickSort(final T[] array, int left, int right) {
        CSort.quickSort(array, left, right, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void quickSort(final T[] array, int left, int right, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        assert (left >= 0 && right >= 0 && left <= right && left < array.length && right <= array.length);
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
        CSort.mergeSort(array, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> void mergeSort(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(cmp);
        final T[] temp = ArrayUtils.clone(array);//CUtils.newArray((Class<? extends T[]>) array.getClass(), array.length);
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

    public static void sortBlocks(final String[] array) {
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
        while (Objects.compare(list.getAt(index), value, cmp) < 0) {//list.getAt(index) != -1
            index *= 2;
        }
        return binarySearch(list, value, index / 2, index, cmp);
    }

    private static <T, E extends ACListNode<T, E>> int binarySearch(final ACList<T, E> list, final T value, int low, int high, final Comparator<? super T> cmp) {
        while (low <= high) {
            int middle = (low + high) / 2;
            final T midValue = list.getAt(middle);
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
