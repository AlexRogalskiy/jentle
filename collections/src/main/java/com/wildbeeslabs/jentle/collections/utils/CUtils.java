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
package com.wildbeeslabs.jentle.collections.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import static org.apache.commons.lang3.ArrayUtils.toArray;

/**
 *
 * Collection utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CUtils {

    public static Object[] mergeObjects(final Object o1, final Object o2) {
        assert (Objects.nonNull(o1));
        assert (Objects.nonNull(o1));
        if (!o1.getClass().isArray() && !o2.getClass().isArray()) {
            return new Object[]{o1, o2};
        }
        Object[] a1 = toArray(o1);
        Object[] a2 = toArray(o2);
        return ArrayUtils.addAll(a1, a2);
    }

    /**
     * Convert List by function converter
     *
     * @param <T>
     * @param <U>
     * @param list
     * @param func
     * @return converted list
     *
     * @see
     *
     * List<String> stringList = Arrays.asList("1","2","3"); List<Integer>
     * integerList = convertList(stringList, s -> Integer.parseInt(s));
     */
    public static <T, U> List<U> convertList(final List<T> list, final Function<T, U> func) {
        assert (Objects.nonNull(list));
        return list.stream().map(func).collect(Collectors.toList());
    }

    /**
     * Convert Array by function converter
     *
     * @param <T>
     * @param <U>
     * @param from
     * @param func
     * @param generator
     * @return converted array
     *
     * @see
     *
     * String[] stringArr = {"1","2","3"}; Double[] doubleArr =
     * convertArray(stringArr, Double::parseDouble, Double[]::new);
     */
    public static <T, U> U[] convertArray(final T[] from, final Function<T, U> func, final IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }

    public static <T> T safeCast(final Object o, final Class<T> clazz) {
        return (Objects.nonNull(clazz) && clazz.isInstance(o)) ? clazz.cast(o) : null;
    }

    public static <T> List<T> removeDuplicates(final List<T> list) {
        assert (Objects.nonNull(list));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public static <T> List<T> removeNulls(final List<T> list) {
        assert (Objects.nonNull(list));
        return list.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        //CollectionUtils.filter(list, PredicateUtils.notNullPredicate());
        //list.removeAll(Collections.singleton(null));
    }

    public static <T, U> U[] convertMapToArray(final Map<? extends T, ? extends U> map, final Class<? extends U[]> type) {
        assert (Objects.nonNull(map));
        final Collection<? extends U> values = map.values();
        //MapsUtils.getObjectInstanceOf(entry.getValue())
        return values.toArray(CUtils.newArray(type, values.size()));
    }

    public static <T, U> List<? extends U> convertMapToList(final Map<? extends T, ? extends U> map) {
        assert (Objects.nonNull(map));
        return new ArrayList<>(map.values());
    }

    public static <T, U> Set<? extends U> convertMapToSet(final Map<? extends T, ? extends U> map) {
        assert (Objects.nonNull(map));
        return new HashSet<>(map.values());
    }

    public static <T> Set<? extends T> convertListToSet(final List<? extends T> list) {
        assert (Objects.nonNull(list));
        return new HashSet<>(list);
        //Set<? extends T> targetSet = new HashSet<>(list.size());
        //CollectionUtils.addAll(targetSet, list);
    }

    public static <T> List<? extends T> convertSetToList(final Set<? extends T> set) {
        assert (Objects.nonNull(set));
        return new ArrayList<>(set);
        //List<? extends T> targetList = new ArrayList<>(set.size());
        //CollectionUtils.addAll(targetList, set);
    }

    public static <T> Set<? extends T> convertArrayToSet(final T[] array) {
        assert (Objects.nonNull(array));
        return new HashSet<>(Arrays.asList(array));
        //Set<? extends T> targetSet = new HashSet<>(array.length);
        //CollectionUtils.addAll(targetSet, array);
    }

    public static <T> List<? extends T> convertArrayToList(final T[] array) {
        assert (Objects.nonNull(array));
        return new ArrayList<>(Arrays.asList(array));
        //List<? extends T> targetList = new ArrayList<>(array.length); 
        //CollectionUtils.addAll(targetList, array);
    }

    public static <T> T[] convertSetToArray(final Set<? extends T> set, final Class<? extends T[]> type) {
        assert (Objects.nonNull(set));
        return set.toArray(CUtils.newArray(type, set.size()));
    }

    public static <T> T[] convertListToArray(final List<? extends T> list, final Class<? extends T[]> type) {
        assert (Objects.nonNull(list));
        return list.toArray(CUtils.newArray(type, list.size()));
    }

    public static <T> List<? extends T> immutableList(final List<? extends T> list) {
        assert (Objects.nonNull(list));
        return Collections.unmodifiableList(list);
        //ListUtils.unmodifiableList(list);
    }

    public static <T> List<? extends T> filter(final List<? extends T> list, final Predicate<? super T> predicate) {
        assert (Objects.nonNull(list));
        assert (Objects.nonNull(predicate));
        return list.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> List<? extends T> filter(final List<? extends T> list, final Set<? extends T> matchSet) {
        return filter(list, matchSet::contains);
    }

    public static <T extends Comparable<? super T>> int search(final List<? extends T> list, final T value) {
        assert (Objects.nonNull(list));
        Collections.sort(list);
        return Collections.binarySearch(list, value);
    }

    public static <T> T random(final List<? extends T> list) {
        assert (Objects.nonNull(list));
        int randomElementIndex = ThreadLocalRandom.current().nextInt(list.size()) % list.size();
        return list.get(randomElementIndex);
    }

    private static <T> T[] newArray(final Class<? extends T[]> type, int size) {
        assert (Objects.nonNull(type));
        assert (size >= 0);
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
}
