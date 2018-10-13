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

import com.wildbeeslabs.jentle.algorithms.list.CList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import lombok.NonNull;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom collection utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CCollectionUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CCollectionUtils.class);

    private CCollectionUtils() {
        LOGGER.debug("Initializing collection utilities...");
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> Map<Integer, IntSummaryStatistics> getMapStatisticsBy(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends Integer> groupingBy, final ToIntFunction<? super T> mapper) {
        return stream.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.summarizingInt(mapper)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMaxBy(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.maxBy(cmp)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMinBy(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.minBy(cmp)));
    }

    public static <T> Optional<T> getMaxBy(final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return CCollectionUtils.getMinMaxBy(stream, Collectors.maxBy(cmp));
    }

    public static <T> Optional<T> getMinBy(final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return CCollectionUtils.getMinMaxBy(stream, Collectors.minBy(cmp));
    }

    protected static <T> Optional<T> getMinMaxBy(@NonNull final Stream<? extends T> stream, final Collector<T, ?, Optional<T>> collector) {
        return stream.collect(collector);
    }

    public static <T, U> Stream<U> getStreamBy(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends U> mapper, final Predicate<? super U> predicate) {
        return stream.map(mapper).filter(predicate);
    }

    public static <T> Stream<? extends T> getStreamSortedBy(@NonNull final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return stream.sorted(cmp);
    }

    public static <T, K> Map<K, Integer> getMapSumBy(@NonNull final Stream<? extends T> stream, final Function<T, K> keys, final Function<T, Integer> values) {
        return stream.collect(Collectors.toMap(keys, values, Integer::sum));
    }

    public static <T extends Number> T reduceStreamBy(@NonNull final Stream<T> stream, final T identity, final BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    public static <A, B, C> Function<A, C> compose(@NonNull final Function<A, B> function1, @NonNull final Function<B, C> function2) {
        return function1.andThen(function2);
    }

    public static <T> Collection<T> join(final Stream<? extends T> first, final Stream<? extends T> second, final Predicate<? super T> predicate) {
        return Stream.concat(first, second).filter(predicate).collect(Collectors.toList());
    }

    public static <T> String join(@NonNull final Stream<? extends T> collection, final String delimiter) {
        return collection.map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> String[] join(final T[] first, final T[] second) {
        return Stream.concat(Arrays.stream(first), Arrays.stream(second)).toArray(String[]::new);
    }

    public static <T> String join(final T[] array, final String delimiter) {
        return Arrays.stream(array).map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> String joinWithPrefixPostfix(final T[] array, final String delimiter, final String prefix, final String postfix) {
        return CCollectionUtils.joinWithPrefixPostfix(Arrays.asList(array), delimiter, prefix, postfix);
    }

    public static <T> String joinWithPrefixPostfix(@NonNull final Collection<T> list, final String delimiter, final String prefix, final String postfix) {
        return list.stream().map(Objects::toString).collect(Collectors.joining(delimiter, prefix, postfix));
    }

    public static <T extends CharSequence> Map<Integer, List<T>> getMapByLength(final T[] array) {
        return Arrays.stream(array).filter(Objects::nonNull).collect(Collectors.groupingByConcurrent(s -> s.length()));
    }

    public static <T extends CharSequence> CharSequence[] getArrayBy(final T[] array, final Predicate<? super T> predicate) {
        return CList.getListBy(Arrays.asList(array), predicate);
    }

    public static List<String> split(final String value, final String delimiter) {
        return CCollectionUtils.split(value, delimiter, (str) -> Boolean.TRUE);
    }

    public static <T extends CharSequence> List<String> split(final T value, final String delimiter, final Predicate<? super String> predicate) {
        return Arrays.stream(String.valueOf(value).split(delimiter))
                .map(String::trim)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Character> splitToListOfChars(final CharSequence value) {
        return String.valueOf(value).chars()
                .mapToObj(item -> (char) item)
                .collect(Collectors.toList());
    }

    public static <T, K> Map<K, Long> countBy(@NonNull final Stream<? extends T> list, final Function<? super T, ? extends K> groupingBy) {
        return list.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.counting()));
    }

    public static <T> Map<T, Long> countBy(final Stream<? extends T> list) {
        return CCollectionUtils.countBy(list, Function.identity());
    }

    public static <T, K, M> Map<K, Optional<T>> reduce(@NonNull final Stream<? extends T> list, final Function<? super T, ? extends K> groupingBy, final BinaryOperator<T> operator) {
        return list.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.reducing(operator)));
    }

    public static <T> Collector<T, ?, List<T>> lastN(int n) {
        assert (n > 0);
        return Collector.<T, Deque<T>, List<T>>of(ArrayDeque::new, (acc, t) -> {
            if (acc.size() == n) {
                acc.pollFirst();
            }
            acc.add(t);
        }, (acc1, acc2) -> {
            while (acc2.size() < n && !acc1.isEmpty()) {
                acc2.addFirst(acc1.pollLast());
            }
            return acc2;
        }, ArrayList::new);
    }

    public static <T> Stream<T> concatFlat(final Stream<? extends T>... args) {
        return Stream.of(args).flatMap(Function.identity());
    }

    public static <T> Stream<T> concat(final Stream<? extends T> stream1, final Stream<? extends T> stream2) {
        return Stream.concat(stream1, stream2);
    }

    public static <T> Stream<T> concat(final Stream<? extends T> stream, final T item) {
        return Stream.concat(stream, Stream.of(item));
    }

    public static Stream<Double> concat(final DoubleStream stream1, final DoubleStream stream2) {
        return DoubleStream.concat(stream1, stream2).boxed();
    }

    public static <T> T max(@NonNull final Stream<? extends T> stream, final Comparator<? super T> comparator) {
        return stream.max(comparator).orElseThrow(NoSuchElementException::new);
    }

    public static <T> T min(@NonNull final Stream<? extends T> stream, final Comparator<? super T> comparator) {
        return stream.min(comparator).orElseThrow(NoSuchElementException::new);
    }

    public static <T> List<T> generate(final Supplier<T> supplier, int skip, int limit) {
        final Stream<T> infiniteStreamOfRandomUUID = Stream.generate(supplier);
        return infiniteStreamOfRandomUUID
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<UUID> generateUUID(int skip, int limit) {
        final Supplier<UUID> randomUUIDSupplier = UUID::randomUUID;
        return generate(randomUUIDSupplier, skip, limit);
    }

    //TriFunction <Integer, String, Integer, Computer> c6Function = Computer::new;
    //Computer c3 = c6Function.apply(2008, "black", 90);
    @FunctionalInterface
    interface TriFunction<A, B, C, R> {

        R apply(A a, B b, C c);

        default <V> TriFunction<A, B, C, V> andThen(@NonNull Function<? super R, ? extends V> after) {
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }
    }

    public static <T, M extends Collection<T>> Map<Boolean, M> partitionBy(@NonNull final Stream<T> stream, final Predicate<? super T> predicate, final Collector<T, ?, M> collector) {
        return stream.collect(Collectors.partitioningBy(predicate, collector));
    }

    public static <T> Map<Boolean, List<T>> partitionBy(@NonNull final Stream<T> stream, final Predicate<? super T> predicate) {
        return CCollectionUtils.partitionBy(stream, predicate, Collectors.toList());
    }
}
