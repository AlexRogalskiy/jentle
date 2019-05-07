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

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;
import com.wildbeeslabs.jentle.algorithms.list.CList;
import com.wildbeeslabs.jentle.algorithms.list.CListUtils;
import com.wildbeeslabs.jentle.algorithms.set.utils.CSetUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Custom collection utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CCollectionUtils {

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

    //(currentList, nextInt) -> currentList.stream().mapToInt(Integer::intValue).sum() + nextInt <= 5
    public static <T> Stream<List<T>> aggregateBy(@NonNull final Stream<T> stream, final BiPredicate<List<T>, T> predicate) {
        return StreamUtils.aggregateOnListCondition(stream, predicate);
    }

    // i -> (i < 100) ? Optional.of(i * i) : Optional.empty()
    public static <T> Stream<T> unfold(@NonNull final T start, final Function<T, Optional<T>> generator) {
        return StreamUtils.unfold(start, generator);
    }

    public static <T> Collection<T> skipUntil(@NonNull final Stream<T> stream, final Predicate<T> predicate) {
        return StreamUtils.skipUntil(stream, predicate).collect(Collectors.toList());
    }

    public static <T> Collection<T> skipWhile(@NonNull final Stream<T> stream, final Predicate<T> predicate) {
        return StreamUtils.skipWhile(stream, predicate).collect(Collectors.toList());
    }

    public static <T> Collection<T> takeWhile(@NonNull final Stream<T> stream, final Predicate<T> predicate) {
        return StreamUtils.takeWhile(stream, predicate).collect(Collectors.toList());
    }

    public static <T> Collection<T> takeUntil(@NonNull final Stream<T> stream, final Predicate<T> predicate) {
        return StreamUtils.takeUntil(stream, predicate).collect(Collectors.toList());
    }

    public static <T> Collection<Collection<T>> merge(@NonNull final Stream<T>... streams) {
        return StreamUtils.mergeToList(streams).collect(Collectors.toList());
    }

    public static <T> Set<Indexed<T>> zip(@NonNull final Stream<T> stream) {
        return StreamUtils.zipWithIndex(stream).collect(Collectors.toSet());
    }

    public static <T> boolean removeBy(@NonNull final Collection<T> collection, final Predicate<T> predicate) {
        final Collection<T> operatedList = new ArrayList<>();
        collection.stream().filter(predicate).forEach(item -> {
            operatedList.add(item);
        });
        return collection.removeAll(operatedList);
    }

    public static <T, U> Stream<U> getStreamBy(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends U> mapper, final Predicate<? super U> predicate) {
        return (Stream<U>) stream.map(mapper).filter(predicate);
    }

    public static <T> Stream<? extends T> getStreamSortedBy(@NonNull final Stream<? extends T> stream, final Comparator<? super T> cmp) {
        return stream.sorted(cmp);
    }

    public static <K, V> Map<K, V> sortByValue(final Map<K, V> map, Comparator<? super V> comparator) {
        return map.entrySet()
            .stream()
            .sorted(Comparator.comparing(entry -> entry.getValue(), comparator))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <K, V> Map<K, V> sortByKey(final Map<K, V> map, Comparator<? super K> comparator) {
        return map.entrySet()
            .stream()
            .sorted(Comparator.comparing(entry -> entry.getKey(), comparator))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
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

    public static Integer findKthElement(final Integer[] arr, int left, int right, int k) {
        if (k >= 0 && k <= right - left + 1) {
            int pos = randomPartition(arr, left, right);
            if (pos - left == k) {
                return arr[pos];
            }
            if (pos - left > k) {
                return findKthElement(arr, left, pos - 1, k);
            }
            return findKthElement(arr, pos + 1, right, k - pos + left - 1);
        }
        return null;
    }

    /**
     * average time complexity: O(n) worst time complexity: O(n^2)
     *
     * @param <T>
     * @param arr
     * @param left
     * @param right
     * @param cmp
     * @return
     */
    public static <T> int iterativePartition(final T[] arr, int left, int right, final Comparator<? super T> cmp) {
        final T pivot = arr[right];
        int i = left;
        for (int j = left; j <= right - 1; j++) {
            if (Objects.compare(arr[j], pivot, cmp) <= 0) {
                CArrayUtils.swap(arr, i, j);
                i++;
            }
        }
        CArrayUtils.swap(arr, i, right);
        return i;
    }

    /**
     * average time complexity: O(n) worst time complexity: O(n^2)
     *
     * @param arr
     * @param left
     * @param right
     * @return
     */
    private static int randomPartition(final Integer arr[], int left, int right) {
        int n = right - left + 1;
        int pivot = (int) (Math.random()) * n;
        CArrayUtils.swap(arr, left + pivot, right);
        return partition(arr, left, right);
    }

    private static int partition(final Integer[] arr, int left, int right) {
        int pivot = arr[right];
        final Integer[] leftArr = IntStream.range(left, right)
            .filter(i -> arr[i] < pivot)
            .map(i -> arr[i])
            .boxed()
            .toArray(Integer[]::new);

        final Integer[] rightArr = IntStream.range(left, right)
            .filter(i -> arr[i] > pivot)
            .map(i -> arr[i])
            .boxed()
            .toArray(Integer[]::new);

        int leftArraySize = leftArr.length;
        System.arraycopy(leftArr, 0, arr, left, leftArraySize);
        arr[leftArraySize + left] = pivot;
        System.arraycopy(rightArr, 0, arr, left + leftArraySize + 1, rightArr.length);
        return left + leftArraySize;
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

    public static <E> Collection<E> difference(final Collection<E> first, final Collection<E> second) {
        if (first instanceof List) {
            return CListUtils.difference((List) first, (List) second);
        } else if (first instanceof Set) {
            return CSetUtils.difference((Set) first, (Set) second);
        } else {
            throw new IllegalArgumentException("At this moment Javers don't support " + first.getClass().getSimpleName());
        }
    }

    public static <T, M extends Collection<T>> Map<Boolean, M> partitionBy(@NonNull final Stream<T> stream, final Predicate<? super T> predicate, final Collector<T, ?, M> collector) {
        return stream.collect(Collectors.partitioningBy(predicate, collector));
    }

    public static <T> Map<Boolean, List<T>> partitionBy(@NonNull final Stream<T> stream, final Predicate<? super T> predicate) {
        return CCollectionUtils.partitionBy(stream, predicate, Collectors.toList());
    }

    public static <E> Iterable<E> concat(final Iterable<? extends E> i1, final Iterable<? extends E> i2) {
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return new Iterator<E>() {
                    Iterator<? extends E> listIterator = i1.iterator();
                    Boolean checkedHasNext;
                    E nextValue;
                    private boolean startTheSecond;

                    void theNext() {
                        if (this.listIterator.hasNext()) {
                            this.checkedHasNext = true;
                            this.nextValue = this.listIterator.next();
                        } else if (this.startTheSecond) {
                            this.checkedHasNext = false;
                        } else {
                            this.startTheSecond = true;
                            this.listIterator = i2.iterator();
                            theNext();
                        }
                    }

                    public boolean hasNext() {
                        if (this.checkedHasNext == null) {
                            theNext();
                        }
                        return this.checkedHasNext;
                    }

                    public E next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        this.checkedHasNext = null;
                        return this.nextValue;
                    }

                    public void remove() {
                        this.listIterator.remove();
                    }
                };
            }
        };
    }

    public static <E extends Comparable<E>> E max(final Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("Pusta kolekcja");

        E result = null;
        for (final E e : c)
            if (Objects.isNull(result) || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        return result;
    }

    public static <T extends Comparable<? super T>> T max(final List<? extends T> list) {
        final Iterator<? extends T> i = list.iterator();
        T result = i.next();
        while (i.hasNext()) {
            T t = i.next();
            if (t.compareTo(result) > 0)
                result = t;
        }
        return result;
    }

    public static <E> E reduce(final List<? extends E> list, final com.wildbeeslabs.jentle.algorithms.utils.CFunctionUtils.Function<E> f, E initVal) {
        List<E> snapshot;
        synchronized (list) {
            snapshot = new ArrayList<>(list);
        }
        E result = initVal;
        for (final E e : snapshot)
            result = f.apply(result, e);
        return result;
    }

    public static void swap(final List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private static <E> void swapHelper(final List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    /**
     * Returns binary flag by input {@link Collection} comparison
     *
     * @param set1 - initial input first {@link Collection}
     * @param set2 - initial input last {@link Collection}
     * @return true - if collections are equal, false - otherwise
     */
    public static boolean isEqualSet(final Collection<?> set1, final Collection<?> set2) {
        if (set1 == set2) {
            return true;
        }
        if (Objects.isNull(set1) || Objects.isNull(set2) || set1.size() != set2.size()) {
            return false;
        }
        return set1.containsAll(set2);
    }
}
