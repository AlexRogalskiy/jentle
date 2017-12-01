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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 *
 * Collection converter utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CConverterUtils {

    private CConverterUtils() {
        // PRIVATE CONSTRUCTOR
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
    public static <T, U> List<? extends U> convertList(final List<? extends T> list, final Function<T, U> func) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(func);
        return list.stream().map(func).collect(Collectors.toList());
    }

    /**
     * Convert Array by function converter
     *
     * @param <T>
     * @param <U>
     * @param array
     * @param func
     * @param generator
     * @return converted array
     *
     * @see
     *
     * String[] stringArr = {"1","2","3"}; Double[] doubleArr =
     * convertArray(stringArr, Double::parseDouble, Double[]::new);
     */
    public static <T, U> U[] convertArray(final T[] array, final Function<T, U> func, final IntFunction<U[]> generator) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(func);
        Objects.requireNonNull(generator);
        return Arrays.stream(array).map(func).toArray(generator);
    }

    public static <T, U> U[] convertMapToArray(final Map<? extends T, ? extends U> map, final Class<? extends U[]> type) {
        Objects.requireNonNull(map);
        final Collection<? extends U> values = map.values();
        //MapsUtils.getObjectInstanceOf(entry.getValue())
        return values.toArray(CConverterUtils.newArray(type, values.size()));
    }

    public static <T, U> List<? extends U> convertMapToList(final Map<? extends T, ? extends U> map) {
        Objects.requireNonNull(map);
        return new ArrayList<>(map.values());
    }

    public static <T, U> Set<? extends U> convertMapToSet(final Map<? extends T, ? extends U> map) {
        Objects.requireNonNull(map);
        return new HashSet<>(map.values());
    }

    public static <T> Set<? extends T> convertListToSet(final List<? extends T> list) {
        Objects.requireNonNull(list);
        return new HashSet<>(list);
        //Set<? extends T> targetSet = new HashSet<>(list.size());
        //CollectionUtils.addAll(targetSet, list);
    }

    public static <T> List<? extends T> convertSetToList(final Set<? extends T> set) {
        Objects.requireNonNull(set);
        return new ArrayList<>(set);
        //List<? extends T> targetList = new ArrayList<>(set.size());
        //CollectionUtils.addAll(targetList, set);
    }

    public static <T> Set<? extends T> convertArrayToSet(final T[] array) {
        Objects.requireNonNull(array);
        return new HashSet<>(Arrays.asList(array));
        //Set<? extends T> targetSet = new HashSet<>(array.length);
        //CollectionUtils.addAll(targetSet, array);
    }

    public static <T> List<? extends T> convertArrayToList(final T[] array) {
        Objects.requireNonNull(array);
        return new ArrayList<>(Arrays.asList(array));
        ////return Arrays.stream(a).collect(Collectors.toList());
        //List<? extends T> targetList = new ArrayList<>(array.length); 
        //CollectionUtils.addAll(targetList, array);
    }

    public static <T, U> List<? extends U> convertArrayToList(final T[] array, final Function<T, U> func) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(func);
        return Arrays.stream(array).map(func).collect(Collectors.toList());
    }

    public static <T> T[] convertSetToArray(final Set<? extends T> set, final Class<? extends T[]> type) {
        Objects.requireNonNull(set);
        return set.toArray(CConverterUtils.newArray(type, set.size()));
    }

    public static <T> T[] convertListToArray(final List<? extends T> list, final Class<? extends T[]> type) {
        Objects.requireNonNull(list);
        return list.toArray(CConverterUtils.newArray(type, list.size()));
    }

    private static <T> T[] newArray(final Class<? extends T[]> type, int size) {
        Objects.requireNonNull(type);
        assert (size >= 0);
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
}
