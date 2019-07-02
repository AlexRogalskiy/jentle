package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayTools {

    public static int findFirst(char c, int start, int offset, final char[] array) {
        int end = start + offset;
        for (int i = start; i < end; i++) {
            if (array[i] == c) return i;
        }
        return -1;
    }

    public static int findLast(char c, int start, int offset, final char[] array) {
        for (int i = start + offset - 1; i >= 0; i--) {
            if (array[i] == c) return i;
        }
        return -1;
    }
}
