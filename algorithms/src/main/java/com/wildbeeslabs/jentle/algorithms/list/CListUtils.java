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
package com.wildbeeslabs.jentle.algorithms.list;

import com.wildbeeslabs.jentle.algorithms.random.CRandom;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom list utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CListUtils {

    public static <T> List<? extends T> removeDuplicates(final Collection<? extends T> list) {
        Objects.requireNonNull(list);
        return list.stream().distinct().collect(Collectors.toList());
    }

    public static <T> List<? extends T> removeNulls(final Collection<? extends T> list) {
        Objects.requireNonNull(list);
        return list.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        //CollectionUtils.filter(list, PredicateUtils.notNullPredicate());
        //list.removeAll(Collections.singleton(null));
    }

    public static <T> List<? extends T> filter(final Collection<? extends T> list, final Predicate<? super T> predicate) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(predicate);
        return list.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> List<? extends T> filter(final Collection<? extends T> list, final Set<? extends T> matchSet) {
        return filter(list, matchSet::contains);
    }

    public static <T extends Comparable<? super T>> int search(final List<? extends T> list, final T value) {
        Objects.requireNonNull(list);
        Collections.sort(list);
        return Collections.binarySearch(list, value);
    }

    public static <T> T random(final List<? extends T> list) {
        Objects.requireNonNull(list);
        int randomIndex = randomIndex(list.size());
        return list.get(randomIndex);
    }

    public static <T> List<? extends T> randomWithRepetitions(final List<? extends T> list, int numOfElements) {
        Objects.requireNonNull(list);
        assert (numOfElements >= 0);
        final List<T> result = new ArrayList<>(numOfElements);
        for (int i = 0; i < numOfElements; i++) {
            result.add(random(list));
        }
        return result;
    }

    public static <T extends Serializable> List<? extends T> randomWithoutRepetitions(final List<? extends T> list, int numOfElements) {
        Objects.requireNonNull(list);
        assert (numOfElements >= 0);
        final List<? extends T> clone = CListUtils.cloneList(list);
        final List<T> result = new ArrayList<>(numOfElements);
        for (int i = 0; i < numOfElements; i++) {
            int randomIndex = randomIndex(clone.size());
            result.add(clone.get(randomIndex));
            clone.remove(randomIndex);
        }
        return result;
    }

    public static <T> List<? extends T> randomSeries(final List<? extends T> list, int numOfElements) {
        //final List<? extends T> clone = CUtils.cloneList(list);
        Objects.requireNonNull(list);
        assert (numOfElements >= 0);
        Collections.shuffle(list);
        return list.subList(0, numOfElements);
    }

    public static <T> List<? extends T> toList(final Iterable<? extends T> iterable) {
        final List<T> list = new ArrayList<>();
        for (final T item : iterable) {
            list.add(item);
        }
        return list;
    }

    private static int randomIndex(int size) {
        assert (size > 0);
        return ThreadLocalRandom.current().nextInt(size) % size;
    }

    public static <T extends Serializable> List<T> cloneList(final List<? extends T> list) {
        Objects.requireNonNull(list);
        final List<T> clonedList = new ArrayList<>(list.size());
        list.stream().forEach((value) -> {
            clonedList.add(SerializationUtils.clone(value));
        });
        return clonedList;
    }

    public static <T> List<? extends T> immutableList(final List<? extends T> list) {
        Objects.requireNonNull(list);
        return Collections.unmodifiableList(list);
        //ListUtils.unmodifiableList(list);
    }

    public static <T> List<? extends T> flattenListOfListsImperatively(final List<List<? extends T>> list) {
        //final List<? extends T> ls = new ArrayList<>();
        //list.forEach(ls::addAll);
        //return ls;
        return list.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    //List<String> result = list.stream().collect(CListUtils.toImmutableList(LinkedList::new));
    public static <T, A extends List<T>> Collector<T, A, List<T>> toImmutableList(final Supplier<A> supplier) {
        return Collector.of(
            supplier,
            List::add, (left, right) -> {
                left.addAll(right);
                return left;
            }, Collections::unmodifiableList);
    }

    public static <T> List<List<? extends T>> getSubsets(final List<? extends T> list) {
        final List<List<? extends T>> result = new ArrayList<>();
        int max = 1 << list.size();
        for (int k = 0; k < max; k++) {
            final List<T> subset = convertIntToSet(k, list);
            result.add(subset);
        }
        return result;
    }

    private static <T> List<T> convertIntToSet(int x, final List<? extends T> list) {
        final List<T> subset = new ArrayList<>();
        int index = 0;
        for (int k = x; k > 0; k >>= 1) {
            if ((k & 1) == 1) {
                subset.add(list.get(index));
            }
            index++;
        }
        return subset;
    }

    public static <T> T max(final List<? extends T> list, int begin, int end, final Comparator<? super T> cmp) {//extends Comparable<? super T>
        Objects.requireNonNull(list);
        assert (begin >= 0 && end >= 0 && begin <= end && begin < list.size() && end < list.size());
        T maxElem = list.get(begin);
        for (++begin; begin < end; ++begin) {
            if (Objects.compare(maxElem, list.get(begin), cmp) < 0) {
                maxElem = list.get(begin);
            }
        }
        return maxElem;
    }

    public static <T> List<T> getRandomSubset(@NonNull final List<? extends T> list) {
        return list.stream().filter(f -> {
            return CRandom.generateRandomBoolean();
        }).collect(Collectors.<T>toList());
    }

    public static <T> List<T> copyOf(final List<? extends T> list) {
        return Optional.ofNullable(list)
            .map(List::stream)
            .orElseGet(Stream::empty)
            .collect(Collectors.toList());
    }

    public static <T> List<T> flatten(final Collection<Collection<T>> collection) {
        return collection.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public static <T> List<T> immutableListOf(final T... elements) {
        return Arrays.stream(Optional.ofNullable(elements).orElseGet(() -> (T[]) new Objects[]{})).collect(Collectors.toList());
    }

    public static <T> List<T> immutableListOf(final Collection<T> elements) {
        return Optional.ofNullable(elements).orElseGet(Collections::emptyList).stream().collect(Collectors.toList());
    }

    /**
     * returns new list with elements from input that satisfies given filter condition
     */
    public static <T> List<T> positiveFilter(final List<T> input, final Predicate<T> filter) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(filter);
        return input.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * returns new list with elements from input that don't satisfies given filter condition
     */
    public static <T> List<T> negativeFilter(final List<T> input, final Predicate<T> filter) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(filter);
        return input.stream().filter(element -> !filter.test(element)).collect(Collectors.toList());
    }

    public static <t> Collector<t, List<t>, List<t>> toImmutableList() {
        return Collector.of(ArrayList::new, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, Collections::unmodifiableList);
    }

    public static <E> List<E> difference(final List<E> first, final List<E> second) {
        if (Objects.isNull(first)) {
            return Collections.EMPTY_LIST;
        }
        if (Objects.isNull(second)) {
            return first;
        }
        final List<E> difference = new ArrayList<>(first);
        difference.removeAll(second);
        return difference;
    }


    public static <T> LinkedList<T> filterBy(final Collection<T> collection, final Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<T>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public interface Filter<T> {
        boolean isOut(final T object);
    }
}
