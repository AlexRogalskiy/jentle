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

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.queue.iface.IStack;
import com.wildbeeslabs.jentle.collections.stack.CStack;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

import static org.apache.commons.lang3.ArrayUtils.toArray;

/**
 * Custom utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CUtils {

    /**
     * Default sort comparator
     */
    public static final CUtils.CSortComparator DEFAULT_SORT_COMPARATOR = CUtils.getDefaultSortComparator();

    /**
     * Default {@link Comparator} implementation
     *
     * @param <T> type of {@link Comparable} instance
     */
    public static class CSortComparator<T extends Comparable<? super T>> implements Comparator<T>, Serializable {

        private static final long serialVersionUID = -4449018458348301596L;

        @Override
        public int compare(final T first, final T last) {
            return CComparatorUtils.compareTo(first, last);
        }
    }

    public static <T extends Comparable<? super T>> CUtils.CSortComparator<T> getDefaultSortComparator() {
        return new CUtils.CSortComparator<>();
    }

    public static Object[] merge(final Object o1, final Object o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);

        if (!o1.getClass().isArray() && !o2.getClass().isArray()) {
            return new Object[]{o1, o2};
        }
        final Object[] a1 = toArray(o1);
        final Object[] a2 = toArray(o2);
        return ArrayUtils.addAll(a1, a2);
    }

    public static <T> T safeCast(final Object obj, final Class<T> clazz) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(clazz);

        return (clazz.isInstance(obj)) ? clazz.cast(obj) : null;
    }

    public static <T> Collection<? extends T> union(final Iterable<? extends T> first, final Iterable<? extends T> second) {
        return CollectionUtils.union(first, second);
        //IterableUtils.chainedIterable(collectionA, collectionB);
        //Stream<? extends T> combinedStream = Stream.of(first, second).flatMap(CollectionLike::stream).collect(Collectors.toList());
    }

    public static <T> IStack<T> sort(final IStack<T> stack, final Comparator<? super T> cmp) throws EmptyStackException, OverflowStackException {
        final IStack<T> result = new CStack<>();
        while (!stack.isEmpty()) {
            final T temp = stack.pop();
            while (!result.isEmpty() && Objects.compare(result.peek(), temp, cmp) > 1) {
                stack.push(result.pop());
            }
            result.push(temp);
        }
        return result;
    }

    public static <T> T[] newArray(@NonNull final Class<? extends T[]> type, int size) {
        assert size >= 0 : "Should be greater than or equal zero";
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    public static <T> T[] newArray2(@NonNull final Class<? extends T> type, int size) {
        assert size >= 0 : "Should be greater than or equal zero";
        return (T[]) Array.newInstance(type, size);
    }

    public static <T> T[][] newMatrix(@NonNull final Class<? extends T> type, int rowCount, int colCount) {
        assert rowCount >= 0 : "Should be greater than or equal zero";
        assert colCount >= 0 : "Should be greater than or equal zero";
        return (T[][]) Array.newInstance(type, rowCount, colCount);
    }

    public static <T> T getInstance(@NonNull final Class<? extends T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error("ERROR: cannot initialize class instance={}, message={}", clazz, ex.getMessage());
        } catch (NoSuchMethodException ex) {
            log.error("ERROR: cannot execute method of class instance={}, message={}", clazz, ex.getMessage());
        } catch (InvocationTargetException ex) {
            log.error("ERROR: cannot get class instance={}, message={}", clazz, ex.getMessage());
        }
        return null;
    }

    public static int hashCodeIgnoreCase(final String s) {
        if (s == null) {
            return 0;
        }
        int hash = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(s.charAt(i));
            hash = 31 * hash + c;
        }
        return hash;
    }
}
