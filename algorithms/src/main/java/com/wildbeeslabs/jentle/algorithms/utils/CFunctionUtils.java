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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Custom function utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CFunctionUtils {

    public interface UnaryFunction<T> {
        T apply(final T arg);
    }

    public interface BinaryFunction<T> {
        T apply(final T arg1, final T arg2);
    }

    private static UnaryFunction<Object> IDENTITY_FUNCTION = arg -> arg;

    private static final BinaryFunction<Number> MAX = (n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()) > 0 ? n1 : n2;

    /**
     * Return a predicate that first applies the specified function and then
     * tests the specified predicate against the result of the function.
     *
     * @param function  the function to apply
     * @param predicate the predicate to test against the result of the function
     */
    public static <T, V> Predicate<T> where(final Function<T, V> function, final Predicate<? super V> predicate) {
        Objects.requireNonNull(function, "function must not be null");
        Objects.requireNonNull(predicate, "predicate must not be null");
        return input -> predicate.test(function.apply(input));
    }
}
