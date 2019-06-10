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

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.*;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.stream;

/**
 * Custom stream utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@UtilityClass
public class CStreamUtils {

    /**
     * Returns a {@link Stream} backed by the given {@link Iterator}
     *
     * @param iterator must not be {@literal null}.
     * @return
     */
    public static <T> Stream<T> from(final Iterator<T> iterator) {
        final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false);
    }

    /**
     * Returns a {@link Collector} to create an unmodifiable {@link List}.
     *
     * @return will never be {@literal null}.
     */
    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return collectingAndThen(toList(), Collections::unmodifiableList);
    }

    /**
     * Returns a {@link Collector} to create an unmodifiable {@link Set}.
     *
     * @return will never be {@literal null}.
     */
    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return collectingAndThen(toSet(), Collections::unmodifiableSet);
    }

    /**
     * Creates a new {@link Stream} for the given value returning an empty {@link Stream} if the value is {@literal null}.
     *
     * @param source can be {@literal null}.
     * @return a new {@link Stream} for the given value returning an empty {@link Stream} if the value is {@literal null}.
     * @since 2.0.6
     */
    public static <T> Stream<T> fromNullable(final T source) {
        return Objects.isNull(source) ? Stream.empty() : Stream.of(source);
    }

    /**
     * Zips the given {@link Stream}s using the given {@link BiFunction}. The resulting {@link Stream} will have the
     * length of the shorter of the two, abbreviating the zipping when the shorter of the two {@link Stream}s is
     * exhausted.
     *
     * @param left     must not be {@literal null}.
     * @param right    must not be {@literal null}.
     * @param combiner must not be {@literal null}.
     * @return
     * @since 2.1
     */
    public static <L, R, T> Stream<T> zip(final Stream<L> left, final Stream<R> right, final BiFunction<L, R, T> combiner) {
        Objects.requireNonNull(left, "Left stream must not be null!");
        Objects.requireNonNull(right, "Right must not be null!");
        Objects.requireNonNull(combiner, "Combiner must not be null!");

        final Spliterator<L> lefts = left.spliterator();
        final Spliterator<R> rights = right.spliterator();

        long size = Long.min(lefts.estimateSize(), rights.estimateSize());
        int characteristics = lefts.characteristics() & rights.characteristics();
        boolean parallel = left.isParallel() || right.isParallel();

        return StreamSupport.stream(new AbstractSpliterator<>(size, characteristics) {
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                return lefts.tryAdvance(left -> rights.tryAdvance(right -> action.accept(combiner.apply(left, right))));
            }
        }, parallel);
    }

    public static <T> Stream<T> enumerationAsStream(final Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration, "Enumeration should not be null");
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    public T next() {
                        return enumeration.nextElement();
                    }

                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }
                },
                Spliterator.ORDERED), false);
    }

    public static <T> Stream<T> enumerationAsStream2(final Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration, "Enumeration should not be null");
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    public T next() {
                        return enumeration.nextElement();
                    }

                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }

                    public void forEachRemaining(final Consumer<? super T> action) {
                        while (enumeration.hasMoreElements()) action.accept(enumeration.nextElement());
                    }
                },
                Spliterator.ORDERED), false);
    }

    public static <T> Stream<T> enumerationAsStream3(final Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration, "Enumeration should not be null");
        return StreamSupport.stream(
            new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
                public boolean tryAdvance(final Consumer<? super T> action) {
                    if (enumeration.hasMoreElements()) {
                        action.accept(enumeration.nextElement());
                        return true;
                    }
                    return false;
                }

                public void forEachRemaining(final Consumer<? super T> action) {
                    while (enumeration.hasMoreElements()) action.accept(enumeration.nextElement());
                }
            }, false);
    }

    public static Stream<?> toStream(final Object object) {
        Objects.requireNonNull(object, "Object must not be null");
        if (object instanceof Stream) {
            return (Stream<?>) object;
        }
        if (object instanceof DoubleStream) {
            return ((DoubleStream) object).boxed();
        }
        if (object instanceof IntStream) {
            return ((IntStream) object).boxed();
        }
        if (object instanceof LongStream) {
            return ((LongStream) object).boxed();
        }
        if (object instanceof Collection) {
            return ((Collection<?>) object).stream();
        }
        if (object instanceof Iterable) {
            return stream(((Iterable<?>) object).spliterator(), false);
        }
        if (object instanceof Iterator) {
            return stream(spliteratorUnknownSize((Iterator<?>) object, ORDERED), false);
        }
        if (object instanceof Object[]) {
            return java.util.Arrays.stream((Object[]) object);
        }
        if (object instanceof double[]) {
            return DoubleStream.of((double[]) object).boxed();
        }
        if (object instanceof int[]) {
            return IntStream.of((int[]) object).boxed();
        }
        if (object instanceof long[]) {
            return LongStream.of((long[]) object).boxed();
        }
        if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
            return IntStream.range(0, Array.getLength(object)).mapToObj(i -> Array.get(object, i));
        }
        throw new IllegalArgumentException("Cannot convert instance of " + object.getClass().getName() + " into a Stream: " + object);
    }

    public static <T> T getOnlyElement(final Collection<T> collection) {
        Objects.requireNonNull(collection, "collection must not be null");
        assert collection.size() == 1 : "collection must contain exactly one element";
        return collection.iterator().next();
    }

    //String[] result = input.stream().collect( CustomCollectors.**toArray**(String[]::new));
    public static <T> Collector<T, ?, T[]> toArray(final IntFunction<T[]> converter) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> list.toArray(converter.apply(list.size())));
    }
}
