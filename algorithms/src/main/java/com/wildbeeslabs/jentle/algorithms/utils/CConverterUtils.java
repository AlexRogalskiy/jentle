/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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

import com.wildbeeslabs.jentle.collections.map.CCheckedMap;
import com.wildbeeslabs.jentle.collections.set.CCheckedSet;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import lombok.NonNull;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom collection converter utilities implementation
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
        LOGGER.debug("Initializing converter utilities...");
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Creates an Object {@link Set} from the supplied objects.
     *
     * @param objects The objects to be added to the set.
     * @param <T>
     * @return The {@link Set}.
     */
    public static <T> Set<T> toSet(final T... objects) {
        Objects.requireNonNull(objects);
        final Set<T> resultSet = new HashSet<>();
        addToCollection(resultSet, objects);
        return resultSet;
    }

    /**
     * Creates an Object {@link List} from the supplied objects.
     *
     * @param objects The objects to be added to the list.
     * @param <T>
     * @return The {@link List}.
     */
    public static <T> List<T> toList(final T... objects) {
        Objects.requireNonNull(objects);
        final List<T> resultList = new ArrayList<>();
        addToCollection(resultList, objects);
        return resultList;
    }

    /**
     * Adds supplied objects to collection {@link Collection}
     *
     * @param objects The objects to be added to the list.
     */
    private static <T> void addToCollection(final Collection<T> theCollection, final T... objects) {
        for (final T object : objects) {
            theCollection.add(object);
        }
    }

    public static <T> Set<T> toImmutableSet(final T... args) {
        return Stream.of(args).collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::<T>unmodifiableSet));
    }

    public static <T> List<T> toImmutableList(final T... args) {
        return Stream.of(args).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::<T>unmodifiableList));
    }

    public static <T, U> U[] toArray(final Map<? extends T, ? extends U> map, final Class<? extends U[]> type) {
        Objects.requireNonNull(map);
        final Collection<? extends U> values = map.values();
        //MapsUtils.getObjectInstanceOf(entry.getValue())
        return values.toArray(CUtils.newArray(type, values.size()));
    }

    public static <T> T[] toArray(final Set<? extends T> set, final Class<? extends T[]> type) {
        Objects.requireNonNull(set);
        return set.toArray(CUtils.newArray(type, set.size()));
    }

    public static <T> T[] toArray(final List<? extends T> list, final Class<? extends T[]> type) {
        Objects.requireNonNull(list);
        return list.toArray(CUtils.newArray(type, list.size()));
    }

    public static <T, U> List<? extends U> toList(final Map<? extends T, ? extends U> map) {
        Objects.requireNonNull(map);
        return new ArrayList<>(map.values());
    }

    public static <T, U> Set<? extends U> toSet(final Map<? extends T, ? extends U> map) {
        Objects.requireNonNull(map);
        return new HashSet<>(map.values());
    }

    public static <T> Set<? extends T> toSet(final List<? extends T> list) {
        Objects.requireNonNull(list);
        return new HashSet<>(list);
        //Set<? extends T> targetSet = new HashSet<>(list.size());
        //CollectionUtils.addAll(targetSet, list);
    }

    public static <T> List<? extends T> toList(final Set<? extends T> set) {
        Objects.requireNonNull(set);
        return new ArrayList<>(set);
        //List<? extends T> targetList = new ArrayList<>(set.size());
        //CollectionUtils.addAll(targetList, set);
    }

    public static <T> Set<T> toCheckedSet(final Set rawSet, final Class<? extends T> type, boolean strict) {
        return new CCheckedSet<>(rawSet, type, strict);
    }

    public static <K, V> Map<K, V> toCheckedMap(final Map rawMap, final Class<? extends K> keyType, final Class<? extends V> valueType, boolean strict) {
        return new CCheckedMap<>(rawMap, keyType, valueType, strict);
    }

    public static <T> void fill(final Collection<T> list, final T value, int count) {
        Objects.requireNonNull(list);
        for (int i = 0; i < count; i++) {
            list.add(value);
        }
    }

    public static <T, E extends T> void append(final Collection<T> list1, final Collection<E> list2, int count) {
        Objects.requireNonNull(list1);
        Objects.requireNonNull(list2);
        final Iterator<E> it2 = list2.iterator();
        for (int i = 0; i < count && it2.hasNext(); i++) {
            list1.add(it2.next());
        }
    }

    public static <T> void fillWithDefault(final Collection<T> list, final Class<? extends T> clazz, int count) {
        Objects.requireNonNull(list);
        for (int i = 0; i < count; i++) {
            list.add(CUtils.getInstance(clazz));
        }
    }

    public static <K, V> Map<K, V> toCheckedMapCopy(final Map rawMap, final Class<? extends K> keyType, final Class<? extends V> valueType, boolean strict) throws ClassCastException {
        Objects.requireNonNull(rawMap);
        final Map<K, V> m2 = new HashMap<>(rawMap.size() * 4 / 3 + 1);
        final Iterator it = rawMap.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry e = (Map.Entry) it.next();
            try {
                m2.put(keyType.cast(e.getKey()), valueType.cast(e.getValue()));
            } catch (ClassCastException ex) {
                LOGGER.error(String.format("ERROR: cannot convert map key=%s, value=%s to key type=%s, key value=%s, message=%s", e.getKey(), e.getValue(), keyType, valueType, ex.getMessage()));
                if (strict) {
                    throw ex;
                }
            }
        }
        return m2;
    }

    public static <E> List<E> toCheckedList(final List rawList, final Class<? extends E> type, boolean strict) throws ClassCastException {
        Objects.requireNonNull(rawList);
        final List<E> l = (rawList instanceof RandomAccess) ? new ArrayList<>(rawList.size()) : new LinkedList<>();
        final Iterator it = rawList.iterator();
        while (it.hasNext()) {
            final Object e = it.next();
            try {
                l.add(type.cast(e));
            } catch (ClassCastException ex) {
                LOGGER.error(String.format("ERROR: cannot convert list value=%s to type=%s, message=%s", e, type, ex.getMessage()));
                if (strict) {
                    throw ex;
                }
            }
        }
        return l;
    }

    public static <T, K, V> Map<K, V> toMap(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends V> valueMapper) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T, K, V> Map<K, V> toMap(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends V> valueMapper, final BinaryOperator<V> mergeFunction) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    public static <K, V> Map<K, V> toMap(@NonNull final Stream<Map.Entry<K, V>> stream) {
        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> toMap2(@NonNull final Stream<AbstractMap.SimpleEntry<K, V>> stream) {
        return stream.collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    public static <K, V> Map<K, V> toUnmodifiableMap2(@NonNull final Stream<AbstractMap.SimpleImmutableEntry<K, V>> stream) {
        return stream.collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
    }

    public static <K, V> Map<K, V> toUnmodifiableMap(@NonNull final Stream<Map.Entry<K, V>> stream) {
        return CConverterUtils.toUnmodifiableMap(stream, Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <T, K, V> Map<K, V> toTreeMap(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends V> valueMapper, final BinaryOperator<V> mergeFunction) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, TreeMap::new));
    }

    public static <T, K, U> Map<K, U> toUnmodifiableMap(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> valueMapper) {
        return stream.collect(Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper), Collections::<K, U>unmodifiableMap));
    }

    public static <T, K, U> LinkedHashMap<K, U> toLinkedMap(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> valueMapper, final BinaryOperator<U> mergeFunction) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new));
    }

    public static <T> Map<T, T> toMap(final Set<T> set) {
        final Map<T, T> map = new ConcurrentHashMap<>();
        set.forEach(t -> map.put(t, t));
        return map;
    }

    public static <K> Set<K> mapToSet(final Map<K, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    public static <T, K, U> Map<K, List<U>> toMapList(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Function<? super T, ? extends U> mapper) {
        return stream.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.mapping(mapper, Collectors.toList())));
    }

    public static <T, K, U> Map<K, Set<U>> toMapSet(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends K> groupingBy, final Function<? super T, ? extends U> mapper) {
        return stream.collect(Collectors.groupingByConcurrent(groupingBy, Collectors.mapping(mapper, Collectors.toSet())));
    }

    public static <T, U> List<U> toList(@NonNull final Stream<? extends T> stream, final Function<? super T, ? extends U> mapper) {
        return stream.collect(Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <T> List<? extends T> toList(final T[] array, final IntPredicate indexPredicate) {
        return CConverterUtils.convertTo(array, indexPredicate, Collectors.toList());
    }

    public static <T, M> M convertTo(final T[] array, final IntPredicate indexPredicate, final Collector<T, ?, M> collector) {
        return IntStream
                .range(0, array.length)
                .filter(indexPredicate)
                .mapToObj(i -> array[i])
                .collect(collector);
    }

    public static <T, M> M convert(@NonNull final Stream<Optional<T>> stream, final Collector<T, ?, M> collector) {
        return stream.flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty()).collect(collector);
    }

    /**
     * Converts supplied objects to array by function converter
     *
     * @param <T>
     * @param <U>
     * @param func
     * @param generator
     * @param objects
     * @return
     *
     * @see String[] stringArr = {"1","2","3"}; Double[] doubleArr =
     * toArray(Double::parseDouble, Double[]::new, stringArr);
     */
    public static <T, U> U[] toArray(final Function<? super T, ? extends U> func, final IntFunction<U[]> generator, final T... objects) {
        return Arrays.stream(objects).map(func).toArray(generator);
    }

    public static <T> Collector<T, ?, T[]> toArray(final IntFunction<T[]> converter) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> list.toArray(converter.apply(list.size())));
    }

    public static String[] toArray(@NonNull final String value, @NonNull final String delimeter) {
        return Arrays.stream(value.split(delimeter)).map(String::trim).toArray(String[]::new);
    }

    public static <T> Collection<T> toCollection(@NonNull final Stream<? extends T> stream) {
        return CConverterUtils.toCollection(stream, TreeSet::new);
    }

    public static <T, C extends Collection<T>> C toCollection(@NonNull final Stream<? extends T> stream, final Supplier<C> supplier) {
        return stream.collect(Collectors.toCollection(supplier));
    }

    public static List<Double> toCollection(@NonNull final DoubleStream stream) {
        return stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static <T, S> Collection<T> toCollection(@NonNull final Stream<? extends CharSequence> stream, final Function<? super CharSequence, ? extends Stream<T>> mapper) {
        return stream.flatMap(mapper).collect(Collectors.toList());
    }

    public static List<String> toBinaryString(@NonNull final IntStream stream) {
        return stream.mapToObj(n -> Integer.toBinaryString(n)).collect(Collectors.toList());
    }

    public static Collection<Double> toDouble(@NonNull final Stream<String> stream) {
        return stream.flatMapToDouble(n -> DoubleStream.of(Double.parseDouble(n))).boxed().collect(Collectors.toList());
    }

    public static Collection<Integer> toInt(@NonNull final Stream<String> stream) {
        return stream.flatMapToInt(n -> IntStream.of(Integer.parseInt(n))).boxed().collect(Collectors.toList());
    }

    public static Collection<Long> toLong(@NonNull final Stream<String> stream) {
        return stream.flatMapToLong(n -> LongStream.of(Long.parseLong(n))).boxed().collect(Collectors.toList());
    }

    public static <T> Collector<T, ?, LinkedList<T>> toLinkedList() {
        return Collector.of(LinkedList::new, LinkedList::add,
                (first, second) -> {
                    first.addAll(second);
                    return first;
                });
    }
}
