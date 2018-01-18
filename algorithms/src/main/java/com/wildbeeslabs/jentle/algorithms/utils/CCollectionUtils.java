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

import com.wildbeeslabs.jentle.collections.set.CCheckedSet;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex
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

    /**
     * Creates an Object {@link Set} from the supplied objects.
     *
     * @param objects The objects to be added to the set.
     * @param <T>
     * @return The {@link Set}.
     */
    public static <T> Set<T> toSet(final T... objects) {
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

    public static <T> Set<T> toSet(final Set rawSet, final Class<? extends T> type, boolean strict) {
        return new CCheckedSet<>(rawSet, type, strict);
    }
}
