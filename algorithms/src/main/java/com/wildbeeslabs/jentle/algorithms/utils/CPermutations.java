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
 * List<String> items = Arrays.asList("A", "B", "C");
 * long permutations = Permutations.factorial(items.size());
 * 
 * LongStream.range(0, permutations).forEachOrdered(i -> {
        System.out.println(i + ": " + Permutations.permutation(i, items));
    });
 * 
 * Permutations.of("A", "B", "C")
        .map(s -> s.collect(toList()))
        .forEachOrdered(System.out::println);
 * 
 * Permutations.of("A", "B", "C")
        .flatMap(Function.identity())
        .forEachOrdered(System.out::print);
 * 
 * Permutations.of("A", "B", "C")
        .parallel()
        .flatMap(Function.identity())
        .forEachOrdered(System.out::print);
 *
 * Permutations.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        .findFirst()
        .get()                
        .collect(toList())
 *
 */
public class CPermutations {

    private CPermutations() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static long factorial(int n) {
        if (n > 20 || n < 0) {
            throw new IllegalArgumentException(n + " is out of range");
        }
        return LongStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
    }

    public static <T> List<T> permutation(long no, List<T> items) {
        return permutationHelper(no, new LinkedList<>(Objects.requireNonNull(items)), new ArrayList<>());
    }

    private static <T> List<T> permutationHelper(long no, LinkedList<T> in, List<T> out) {
        if (in.isEmpty()) {
            return out;
        }
        long subFactorial = factorial(in.size() - 1);
        out.add(in.remove((int) (no / subFactorial)));
        return permutationHelper((int) (no % subFactorial), in, out);
    }

    public static <T> Stream<Stream<T>> of(T... items) {
        List<T> itemList = Arrays.asList(items);
        return LongStream.range(0, factorial(items.length)).mapToObj(no -> permutation(no, itemList).stream());
    }
}
