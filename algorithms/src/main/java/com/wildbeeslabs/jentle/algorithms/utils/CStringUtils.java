package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * Custom string utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CStringUtils {

    /**
     * Check whether string consists of unique characters
     *
     * @param value - input string
     * @return true - if string is unique, false - otherwise
     */
    public static boolean isUnique(final String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return false;
        }
        Set<Integer> set = new HashSet();
        for (int i = 0; i < value.length(); i++) {
            int ch = value.charAt(i);
            if (set.contains(ch)) {
                return false;
            }
            set.add(ch);
        }
        return true;
    }
}
