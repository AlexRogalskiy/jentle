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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom converter utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CConverterUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CConverterUtils.class);

    private CConverterUtils() {
        LOGGER.debug("Initializing converter utils...");
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, K, V> Map<K, V> convertToMap(final Stream<? extends T> stream, final Function<? super T, ? extends K> keys, final Function<? super T, ? extends V> values) {
        return stream.collect(Collectors.toMap(keys, values));
    }

    public static <K, T> Map<K, List<T>> getSortedMapByKey(final Map<K, List<T>> map, final Comparator<? super K> comparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <T> Map<Integer, IntSummaryStatistics> getMapStatisticsBy(final Stream<? extends T> stream, final Function<? super T, ? extends Integer> groupingBy, final ToIntFunction<? super T> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.summarizingInt(mapper)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMaxBy(final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.maxBy(cmp)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMinBy(final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.minBy(cmp)));
    }

    public static <T> Optional<T> getMaxBy(final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return getMinMaxBy(stream, Collectors.maxBy(cmp));
    }

    public static <T> Optional<T> getMinBy(final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return getMinMaxBy(stream, Collectors.minBy(cmp));
    }

    protected static <T> Optional<T> getMinMaxBy(final Stream<? extends T> stream, final Collector<T, ?, Optional<T>> collector) {
        return stream.collect(collector);
    }

    public static <E> Map<Integer, Long> getMapCountBy(final Stream<? extends E> stream, final Function<? super E, ? extends Integer> groupingBy) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.counting()));
    }

    public static <E, K, U> Map<K, List<U>> convertToMapList(final Stream<? extends E> stream, final Function<? super E, ? extends K> groupingBy, final Function<? super E, ? extends U> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.mapping(mapper, Collectors.toList())));
    }

    public static <E, K, U> Map<K, Set<U>> convertToMapSet(final Stream<? extends E> stream, final Function<? super E, ? extends K> groupingBy, final Function<? super E, ? extends U> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.mapping(mapper, Collectors.toSet())));
    }

    public static <E, U> List<U> convertToList(final Stream<? extends E> stream, final Function<? super E, ? extends U> mapper) {
        return stream.collect(Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <E, U> Stream<U> getStreamBy(final Stream<? extends E> stream, final Function<? super E, ? extends U> mapper, final Predicate<? super U> predicate) {
        return stream.map(mapper).filter(predicate);
    }

    public static <E> Stream<? extends E> getStreamSortedBy(final Stream<? extends E> stream, final Comparator<? super E> cmp) {
        return stream.sorted(cmp);
    }

    public static <T, K> Map<K, Integer> getMapSumBy(final Stream<? extends T> stream, final Function<T, K> keys, final Function<T, Integer> values) {
        return stream.collect(Collectors.toMap(keys, values, Integer::sum));
    }

    public static <T extends Number> T reduceStreamBy(final Stream<T> stream, final T identity, final BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    public static <A, B, C> Function<A, C> compose(final Function<A, B> function1, final Function<B, C> function2) {
        return function1.andThen(function2);
    }

    public static <T> Collection<T> join(final Collection<T> first, final Collection<T> second, final Predicate<? super T> predicate) {
        return Stream.concat(first.stream(), second.stream()).filter(predicate).collect(Collectors.toList());
    }

    public static <T> String join(final Collection<T> collection, final String delimiter) {
        return collection.stream().map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    public static <K, V> String join(final Map<K, V> map, final String keyValueDelimiter, final String delimiter) {
        return map.entrySet().stream().map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(Collectors.joining(delimiter));
    }

    public static <T> String[] join(final T[] first, final T[] second) {
        return Stream.concat(Arrays.stream(first), Arrays.stream(second)).toArray(String[]::new);
    }

    public static <T> String join(final T[] array, final String delimiter) {
        return Arrays.stream(array).map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> String joinWithPrefixPostfix(final T[] array, final String delimiter, final String prefix, final String postfix) {
        return joinWithPrefixPostfix(Arrays.asList(array), delimiter, prefix, postfix);
    }

    public static <T> String joinWithPrefixPostfix(final Collection<T> list, final String delimiter, final String prefix, final String postfix) {
        return list.stream().map(Objects::toString).collect(Collectors.joining(delimiter, prefix, postfix));
    }

    public static Map<Integer, List<String>> getMapByLength(final String[] array) {
        return Arrays.stream(array).filter(Objects::nonNull).collect(Collectors.groupingBy(String::length));
    }

    public static String[] getArrayBy(final String[] array, final Predicate<? super String> predicate) {
        return getArrayBy(Arrays.asList(array), predicate);
    }

    public static String[] getArrayBy(final List<String> list, final Predicate<? super String> predicate) {
        return list.stream().filter(predicate).toArray(size -> new String[size]);
    }

    public static List<String> split(final String value, final String delimiter) {
        return split(value, delimiter, (str) -> Boolean.TRUE);
    }

    public static List<String> split(final String value, final String delimiter, final Predicate<? super String> predicate) {
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

    public static <T> List<? extends T> convertToList(final T[] array, final IntPredicate indexPredicate) {
        return convertTo(array, indexPredicate, Collectors.toList());
    }

    public static <T, M> M convertTo(final T[] array, final IntPredicate indexPredicate, final Collector<T, ?, M> collector) {
        return IntStream
                .range(0, array.length)
                .filter(indexPredicate)
                .mapToObj(i -> array[i])
                .collect(collector);
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

    public static <T> Collector<T, ?, LinkedList<T>> toLinkedList() {
        return Collector.of(LinkedList::new, LinkedList::add,
                (first, second) -> {
                    first.addAll(second);
                    return first;
                });
    }

//    List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
//    integers.forEach(lambdaWrapper(i -> System.out.println(50 / i), ArithmeticException.class));
    public static <T, E extends Exception> Consumer<T> consumerWrapper(final Consumer<T> consumer, final Class<E> clazz) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    final E exCast = clazz.cast(ex);
                    LOGGER.error("Exception occured: message=" + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }

    @FunctionalInterface
    public static interface ThrowingConsumer<T, E extends Exception> {

        void accept(final T t) throws E;
    }

//    List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
//    integers.forEach(throwingConsumerWrapper(i -> writeToFile(i)));
    public static <T> Consumer<T> throwingConsumerWrapper(final ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

//    List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
//    integers.forEach(handlingConsumerWrapper(i -> writeToFile(i), IOException.class));
    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(final ThrowingConsumer<T, E> throwingConsumer, final Class<E> exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    final E exCast = exceptionClass.cast(ex);
                    LOGGER.error("Exception occured: message=" + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    public static <T> Set<T> toImmutableSet(final T... args) {
        return Stream.of(args).collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::<T>unmodifiableSet));
    }

    public static <T> List<T> toImmutableList(final T... args) {
        return Stream.of(args).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::<T>unmodifiableList));
    }

    public static <T, M> M convert(final Stream<Optional<T>> stream, final Collector<T, ?, M> collector) {
        return stream.flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty()).collect(collector);
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

    public static <T> T max(final Stream<? extends T> stream, final Comparator<? super T> comparator) {
        return stream.max(comparator).orElseThrow(NoSuchElementException::new);
    }

    public static <T> T min(final Stream<? extends T> stream, final Comparator<? super T> comparator) {
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
        Supplier<UUID> randomUUIDSupplier = UUID::randomUUID;
        return generate(randomUUIDSupplier, skip, limit);
    }

    //TriFunction <Integer, String, Integer, Computer> c6Function = Computer::new;
    //Computer c3 = c6Function.apply(2008, "black", 90);
    @FunctionalInterface
    interface TriFunction<A, B, C, R> {

        R apply(A a, B b, C c);

        default <V> TriFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }
    }

    public static <T> Map<Boolean, List<T>> partitionBy(final List<T> list, final Predicate<? super T> predicate) {
        return list.stream().collect(Collectors.partitioningBy(predicate));
    }

}
