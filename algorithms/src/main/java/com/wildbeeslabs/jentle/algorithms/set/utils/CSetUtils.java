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
package com.wildbeeslabs.jentle.algorithms.set.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Custom set utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CSetUtils {

    public static <E> Set<E> xor(final Set<E> first, final Set<E> second) {
        final Set<E> xor = difference(first, second);
        xor.addAll(difference(second, first));
        return xor;
    }

    /**
     * null args are allowed
     */
    public static <E> Set<E> difference(final Set<E> first, final Set<E> second) {
        if (Objects.isNull(first)) {
            return Collections.EMPTY_SET;
        }
        if (Objects.isNull(second)) {
            return first;
        }
        final Set<E> difference = new HashSet<>(first);
        difference.removeAll(second);
        return difference;
    }

    /**
     * @return ImmutableSet
     */
    public static <F, T> Set<T> transform(final Set<F> input, final Function<F, T> transformation) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(transformation);
        return input.stream().map(transformation::apply).collect(toImmutableSet());
    }

    public static <t> Collector<t, Set<t>, Set<t>> toImmutableSet() {
        return Collector.of(HashSet::new, Set::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, Collections::unmodifiableSet);
    }
}
