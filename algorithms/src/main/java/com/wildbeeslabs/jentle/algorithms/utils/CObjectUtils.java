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

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Custom object utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CObjectUtils {

    /**
     * Checks that the specified value is of a specific type.
     *
     * @param value   the value to check
     * @param type    the type to require
     * @param message the message to use in exception if type is not as required
     * @param <T>     the type being required
     * @return the value casted to the required type
     * @throws IllegalArgumentException if {@code value} is not of the required type
     * @throws NullPointerException     if {@code value}, {@code type}, or {@code message} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T requireType(final Object value, final Class<T> type, String message) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (!type.isInstance(value)) {
            throw new IllegalArgumentException(message);
        }
        return (T) value;
    }

    public static void freezeProcessing(final int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> wrapWithList(final Supplier<T> supplier) {
        return Lists.newArrayList(supplier.get());
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object object) {
        return (T) object;
    }

    public static boolean areEqual(final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return o1 == null && o2 == null;
        } else if (isArray(o1)) {
            return isArray(o2) && areArraysEqual(o1, o2);
        } else {
            return o1.equals(o2);
        }
    }

    public static boolean areArraysEqual(final Object o1, final Object o2) {
        return areArrayLengthsEqual(o1, o2)
            && areArrayElementsEqual(o1, o2);
    }

    public static boolean areArrayLengthsEqual(final Object o1, final Object o2) {
        return Array.getLength(o1) == Array.getLength(o2);
    }

    public static boolean areArrayElementsEqual(final Object o1, final Object o2) {
        for (int i = 0; i < Array.getLength(o1); i++) {
            if (!areEqual(Array.get(o1, i), Array.get(o2, i))) return false;
        }
        return true;
    }

    public static boolean isArray(final Object o) {
        return o.getClass().isArray();
    }
}
