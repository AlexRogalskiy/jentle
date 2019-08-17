package com.wildbeeslabs.jentle.algorithms.sort;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CountingSort {

    public static Integer[] sort(final Integer[] unsorted) {
        int maxValue = findMax(unsorted);
        int[] counts = new int[maxValue + 1];
        updateCounts(unsorted, counts);
        populateCounts(unsorted, counts);
        return unsorted;
    }

    private static int findMax(final Integer[] unsorted) {
        int max = Integer.MIN_VALUE;
        for (int i : unsorted) {
            if (i > max)
                max = i;
        }
        return max;
    }

    private static void updateCounts(final Integer[] unsorted, final int[] counts) {
        for (int e : unsorted)
            counts[e]++;
    }

    private static void populateCounts(final Integer[] unsorted, final int[] counts) {
        int index = 0;
        for (int i = 0; i < counts.length; i++) {
            int e = counts[i];
            while (e > 0) {
                unsorted[index++] = i;
                e--;
            }
        }
    }
}
