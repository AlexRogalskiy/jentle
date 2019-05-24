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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Custom permutation utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @see List<String> items = Arrays.asList("A", "B", "C"); long permutations =
 * Permutations.factorial(items.size());
 * <p>
 * LongStream.range(0, permutations).forEachOrdered(i -> { System.out.println(i
 * + ": " + Permutations.permutation(i, items)); });
 * <p>
 * Permutations.of("A", "B", "C") .map(s -> s.collect(toList()))
 * .forEachOrdered(System.out::println);
 * <p>
 * Permutations.of("A", "B", "C") .flatMap(Function.identity())
 * .forEachOrdered(System.out::print);
 * <p>
 * Permutations.of("A", "B", "C") .parallel() .flatMap(Function.identity())
 * .forEachOrdered(System.out::print);
 * <p>
 * Permutations.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
 * .findFirst() .get() .collect(toList())
 * @since 2017-09-01
 */
@Slf4j
@UtilityClass
public class CPermutationUtils {

    public static long factorial(int n) {
        if (n > 20 || n < 0) {
            throw new IllegalArgumentException(n + " is out of range");
        }
        return LongStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
    }

    public static <T> List<? extends T> permutation(long no, final List<? extends T> items) {
        return permutationHelper(no, new LinkedList<>(Objects.requireNonNull(items)), new ArrayList<>());
    }

    private static <T> List<? extends T> permutationHelper(long no, final List<? extends T> in, final List<T> out) {
        if (in.isEmpty()) {
            return out;
        }
        long subFactorial = factorial(in.size() - 1);
        out.add(in.remove((int) (no / subFactorial)));
        return permutationHelper((no % subFactorial), in, out);
    }

    public static <T> Stream<Stream<? extends T>> of(final T... items) {
        Objects.requireNonNull(items);
        final List<? extends T> itemList = Arrays.asList(items);
        return LongStream.range(0, factorial(items.length)).mapToObj(no -> permutation(no, itemList).stream());
    }

    public static List<String> getPermutations(final String value) {
        Objects.requireNonNull(value);
        final List<String> result = new ArrayList<>();
        getPermutations(StringUtils.EMPTY, value, result);
        return result;
    }

    private static void getPermutations(final String prefix, final String remainder, final List<String> result) {
        int len = remainder.length();
        if (0 == len) {
            result.add(prefix);
        }
        for (int i = 0; i < len; i++) {
            String before = remainder.substring(0, i);
            String after = remainder.substring(i + 1, len);
            String c = Character.toChars(remainder.codePointAt(i)).toString();
            getPermutations(prefix + c, before + after, result);
        }
    }

    public static List<String> getPermutations2(final String value) {
        Objects.requireNonNull(value);
        final List<String> result = new ArrayList<>();
        final Map<Integer, Integer> map = buildFrequencyTable(value);
        getPermutations2(map, StringUtils.EMPTY, value.length(), result);
        return result;
    }

    private static Map<Integer, Integer> buildFrequencyTable(final String value) {
        final Map<Integer, Integer> map = new HashMap<>();
        for (final Integer c : value.codePoints().toArray()) {
            if (!map.containsKey(c)) {
                map.put(c, 0);
            }
            map.put(c, map.get(c) + 1);
        }
        return map;
    }

    private static void getPermutations2(final Map<Integer, Integer> map, final String prefix, int remaining, final List<String> result) {
        if (0 == remaining) {
            result.add(prefix);
            return;
        }
        for (final Integer c : map.keySet()) {
            int count = map.get(c);
            if (count > 0) {
                map.put(c, count - 1);
                getPermutations2(map, prefix + c, remaining - 1, result);
                map.put(c, count);
            }
        }
    }

    public static int makeChange(int num) {
        final int[] denoms = {25, 10, 5, 1};
        final int[][] map = new int[num + 1][denoms.length];
        return makeChange(num, denoms, 0, map);
    }

    private static int makeChange(int amount, int[] denoms, int index, int[][] map) {
        if (map[amount][index] > 0) {
            return map[amount][index];
        }
        if (index >= denoms.length - 1) {
            return 1;
        }
        int denomAmount = denoms[index];
        int ways = 0;
        for (int i = 0; i * denomAmount <= amount; i++) {
            int amountReamining = amount - i * denomAmount;
            ways += makeChange(amountReamining, denoms, index + 1, map);
        }
        map[amount][index] = ways;
        return ways;
    }

    public static void placeMembers(int row, final Integer[] columns, final List<Integer[]> results) {
        if (8 == row) {
            results.add(columns.clone());
        } else {
            for (int col = 0; col < 8; col++) {
                if (isLocationValid(columns, row, col)) {
                    columns[row] = col;
                    placeMembers(row + 1, columns, results);
                }
            }
        }
    }

    private static boolean isLocationValid(final Integer[] columns, int row, int column) {
        for (int r = 0; r < row; r++) {
            int c = columns[r];
            if (column == c) {
                return false;
            }
            int columnDistance = Math.abs(c - column);
            int rowDistance = row - r;
            if (columnDistance == rowDistance) {
                return false;
            }
        }
        return true;
    }

    public static <T> int createStack(final List<T> list, final Comparator<? super T> cmp) {
        Objects.requireNonNull(list);
        Collections.sort(list, cmp);
        int maxHeight = 0;
        int[] stackMap = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            int height = createStack(list, i, stackMap);
            maxHeight = Math.max(maxHeight, height);
        }
        return maxHeight;
    }

    private static <T> int createStack(final List<T> list, int bottomIndex, final int[] stackMap) {
        if (bottomIndex < list.size() && stackMap[bottomIndex] > 0) {
            return stackMap[bottomIndex];
        }
        final T bottom = list.get(bottomIndex);
        int maxHeight = 0;
        for (int i = bottomIndex + 1; i < list.size(); i++) {
//            if (list.get(i).isAbove(bottom)) {
//                int height = createStack(list, i, stackMap);
//                maxHeight = Math.max(height, maxHeight);
//            }
        }
//        maxHeight += bottom.height;
        stackMap[bottomIndex] = maxHeight;
        return maxHeight;
    }

    public static <T> int createStack2(final List<T> list, final Comparator<? super T> cmp) {
        Collections.sort(list, cmp);
        final int[] stackMap = new int[list.size()];
        return createStack(list, null, 0, stackMap);
    }

    private static <T> int createStack(final List<T> list, final T bottom, int offset, int[] stackMap) {
        if (offset >= list.size()) {
            return 0;
        }
        final T newBottom = list.get(offset);
        int heightWithBottom = 0;
//        if (Objects.isNull(bottom) || newBottom.canBeAbove(bottom)) {
//            if (0 == stackMap[offset]) {
//                stackMap[offset] = createStack(list, newBottom, offset + 1, stackMap);
//                stackMap[offset] += newBottom.height;
//            }
//            heightWithBottom = stackMap[offset];
//        }
        int heightWithoutBottom = createStack(list, bottom, offset + 1, stackMap);
        return Math.max(heightWithBottom, heightWithoutBottom);
    }
}
