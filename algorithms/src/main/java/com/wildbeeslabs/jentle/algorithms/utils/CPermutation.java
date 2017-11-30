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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 *
 * Custom permutation utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-09-01
 *
 * @see
 *
 * List<String> items = Arrays.asList("A", "B", "C"); long permutations =
 * Permutations.factorial(items.size());
 *
 * LongStream.range(0, permutations).forEachOrdered(i -> { System.out.println(i
 * + ": " + Permutations.permutation(i, items)); });
 *
 * Permutations.of("A", "B", "C") .map(s -> s.collect(toList()))
 * .forEachOrdered(System.out::println);
 *
 * Permutations.of("A", "B", "C") .flatMap(Function.identity())
 * .forEachOrdered(System.out::print);
 *
 * Permutations.of("A", "B", "C") .parallel() .flatMap(Function.identity())
 * .forEachOrdered(System.out::print);
 *
 * Permutations.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
 * .findFirst() .get() .collect(toList())
 *
 */
public final class CPermutation {

    private CPermutation() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static long factorial(int n) {
        if (n > 20 || n < 0) {
            throw new IllegalArgumentException(n + " is out of range");
        }
        return LongStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
    }

    public static <T> List<? extends T> permutation(long no, List<? extends T> items) {
        return permutationHelper(no, new LinkedList<>(Objects.requireNonNull(items)), new ArrayList<>());
    }

    private static <T> List<? extends T> permutationHelper(long no, List<? extends T> in, List<T> out) {
        if (in.isEmpty()) {
            return out;
        }
        long subFactorial = factorial(in.size() - 1);
        out.add(in.remove((int) (no / subFactorial)));
        return permutationHelper((no % subFactorial), in, out);
    }

    public static <T> Stream<Stream<? extends T>> of(T... items) {
        List<? extends T> itemList = Arrays.asList(items);
        return LongStream.range(0, factorial(items.length)).mapToObj(no -> permutation(no, itemList).stream());
    }
}
