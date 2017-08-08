package com.wildbeeslabs.jentle.algorithms.sort;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 *
 * Custom sorting implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSort {

    /**
     * Default sort comparator
     */
    private static final CSortComparator DEFAULT_SORT_COMPARATOR = new CSortComparator();

    protected static class CSortComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(T first, T last) {
            return first.compareTo(last);
        }
    }

    public static <T> int binarySearch(T[] array, T value) {
        return CSort.binarySearch(array, value, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> int binarySearch(T[] array, T value, Comparator<? super T> cmp) {
        int low = 0, high = array.length - 1, middle;
        while (low <= high) {
            middle = (int) Math.floor((low + high) / 2);
            if (cmp.compare(array[middle], value) < 0) {
                low = middle + 1;
            } else if (cmp.compare(array[middle], value) > 0) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    public static <T> int binarySearchRecursive(T[] array, T value, int low, int high) {
        return CSort.binarySearchRecursive(array, value, low, high, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> int binarySearchRecursive(T[] array, T value, int low, int high, Comparator<? super T> cmp) {
        if (low > high) {
            return -1;
        }
        int middle = (int) Math.floor((low + high) / 2);
        if (cmp.compare(array[middle], value) < 0) {
            return binarySearchRecursive(array, value, middle + 1, high, cmp);
        } else if (cmp.compare(array[middle], value) > 0) {
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
    public static <T> void quickSort(T[] array, int left, int right) {
        CSort.quickSort(array, left, right, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> void quickSort(T[] array, int left, int right, Comparator<? super T> cmp) {
        int index = partition(array, left, right, cmp);
        if (left < index - 1) {
            quickSort(array, left, index - 1, cmp);
        }
        if (index < right) {
            quickSort(array, index, right, cmp);
        }
    }

    private static <T> int partition(T[] array, int left, int right, Comparator<? super T> cmp) {
        T pivot = array[(int) Math.floor((left + right) / 2)];
        while (left <= right) {
            while (cmp.compare(array[left], pivot) < 0) {
                left++;
            }
            while (cmp.compare(array[right], pivot) > 0) {
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

    private static <T> void swap(T[] array, int left, int right) {
        T temp = array[left];
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
    public static <T> void mergeSort(T[] array) {
        CSort.mergeSort(array, DEFAULT_SORT_COMPARATOR);
    }

    public static <T> void mergeSort(T[] array, Comparator<? super T> cmp) {
        T[] temp = newArray((Class<T[]>) array.getClass(), array.length);
        mergeSort(array, temp, 0, array.length - 1, cmp);
    }

    private static <T> void mergeSort(T[] array, T[] temp, int low, int high, Comparator<? super T> cmp) {
        if (low < high) {
            int middle = (int) Math.floor((low + high) / 2);
            mergeSort(array, temp, low, middle, cmp);
            mergeSort(array, temp, middle + 1, high, cmp);
            merge(array, temp, low, middle, high, cmp);
        }
    }

    private static <T> void merge(T[] array, T[] temp, int low, int middle, int high, Comparator<? super T> cmp) {
        for (int i = low; i <= high; i++) {
            temp[i] = array[i];
        }

        int tempLeft = low;
        int tempRight = middle + 1;
        int current = low;

        while (tempLeft <= middle && tempRight <= high) {
            if (cmp.compare(temp[tempLeft], temp[tempRight]) <= 0) {
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

    private static <T> T[] newArray(Class<T[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
}
