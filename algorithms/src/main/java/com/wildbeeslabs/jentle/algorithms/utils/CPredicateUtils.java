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
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Custom predicate utilities implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@UtilityClass
public class CPredicateUtils {

    /**
     * Default {@link Predicate} operators
     */
    public final static Predicate<Object> randBoolean = predicate -> new Random().nextBoolean();
    public static final Predicate<CharSequence> notEmpty = sequence -> StringUtils.isNotEmpty(sequence);
    public static final Predicate<CharSequence> notBlank = sequence -> StringUtils.isNotBlank(sequence);

    /**
     * Negates a {@link Predicate}.  Exists primarily to enable negation of method references that are {@link Predicate}s.
     *
     * @param t   the predicate to negate
     * @param <T> the type of element being tested
     * @return a negated predicate
     * @throws NullPointerException if {@code t} is {@code null}
     * @see Predicate#negate()
     */
    public static <T> Predicate<T> not(final Predicate<T> t) {
        Objects.requireNonNull(t, "t must not be null");
        return t.negate();
    }

    /**
     * Logical OR a collection of {@link Predicate}s.  Exists primarily to enable the logical OR of method references that are {@link Predicate}s.
     *
     * @param ts  the predicates to logical OR
     * @param <T> the type of element being tested
     * @return a local ORd collection of predicates
     * @throws NullPointerException if {@code ts} is {@code null}
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Predicate<T> or(final Predicate<T>... ts) {
        Objects.requireNonNull(ts, "ts must not be null");
        return Arrays.stream(ts).reduce(Predicate::or).orElseThrow(() -> new IllegalStateException("Unable to combine predicates together via logical OR"));
    }
}
