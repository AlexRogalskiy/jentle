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
package com.wildbeeslabs.jentle.algorithms.map;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.NonNull;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom mp algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CMap {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CMap.class);

    private CMap() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, K> Map<K, List<T>> getSortedMapByKey(@NonNull final Map<K, List<T>> map, final Comparator<? super K> comparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public <K, V extends Comparable<V>> Entry<K, V> getMaxValue(final Map<K, V> map, final Comparator<? super V> comparator) {
        Optional<Entry<K, V>> maxValue = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue(comparator));
        return maxValue.orElse(null);
    }

    public <K, V extends Comparable<V>> Entry<K, V> getMinValue(final Map<K, V> map, final Comparator<? super V> comparator) {
        Optional<Entry<K, V>> minValue = map.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue(comparator));
        return minValue.orElse(null);
    }

    public <K extends Comparable<K>, V> Entry<K, V> getMaxKey(final Map<K, V> map, final Comparator<? super K> comparator) {
        Optional<Entry<K, V>> maxKey = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey(comparator));
        return maxKey.orElse(null);
    }

    public <K extends Comparable<K>, V> Entry<K, V> getMinKey(final Map<K, V> map, final Comparator<? super K> comparator) {
        Optional<Entry<K, V>> minKey = map.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey(comparator));
        return minKey.orElse(null);
    }

    public static <K, V> String join(@NonNull final Map<K, V> map, final String keyValueDelimiter, final String delimiter) {
        return map.entrySet().stream().map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(Collectors.joining(delimiter));
    }

    public static <K, V> Map<K, V> filterByKey(@NonNull final Map<K, V> map, final Predicate<? super K> predicate) {
        return map.entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> filterByValue(@NonNull final Map<K, V> map, final Predicate<? super V> predicate) {
        return map.entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> List<V> getMapValues(final Map<K, V> map, final Comparator<? super K> comparator) {
        final Map<K, V> reverse = new TreeMap<>(comparator);
        reverse.putAll(map);
        return reverse.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public static <K, V> List<K> getMapKeys(final Map<K, V> map, final Comparator<? super K> comparator) {
        final Map<K, V> reverse = new TreeMap<>(comparator);
        reverse.putAll(map);
        return reverse.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    public <K, V> List<K> keys(final Map<K, V> map, final V value, final Comparator<? super V> comparator) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> Objects.compare(value, entry.getValue(), comparator) == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
