package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * Custom string utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CStringUtils {

    private CStringUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Check whether string consists of unique set of characters
     *
     * @param value - input string
     * @return true - if string is unique, false - otherwise
     */
    public static boolean isUnique(final String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return false;
        }
        return value.codePoints().allMatch(new HashSet<>()::add);
    }

    public static boolean permutation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || first.length() != last.length()) {
            return false;
        }
        Map<Character, Long> firstMap = first.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Character, Long> secondMap = last.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return firstMap.equals(secondMap);
    }
}
