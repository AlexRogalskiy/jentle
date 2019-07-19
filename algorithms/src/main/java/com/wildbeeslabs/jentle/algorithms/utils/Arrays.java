package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

/**
 * Utility methods related to arrays.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Florent Biville
 */
@UtilityClass
public class Arrays {

    /**
     * Indicates whether the given object is not {@code null} and is an ArrayUtils.
     *
     * @param o the given object.
     * @return {@code true} if the given object is not {@code null} and is an ArrayUtils, otherwise {@code false}.
     */
    public static boolean isArray(final Object o) {
        return Objects.nonNull(o) && o.getClass().isArray();
    }

    /**
     * Get the values of any ArrayUtils (primitive or not) into a {@code Object[]}.
     *
     * @param array ArrayUtils passed as an object to support both primitive and Object ArrayUtils
     * @return the values of the given Object as a {@code Object[]}.
     * @throws IllegalArgumentException it the given Object is not an ArrayUtils.
     */
    public static Object[] asObjectArray(final Object array) {
        Objects.requireNonNull(array, "Array should not be null");
        if (Objects.isNull(array)) {
            return null;
        }
        int length = Array.getLength(array);
        final Object[] objectArray = new Object[length];
        for (int i = 0; i < length; i++) {
            objectArray[i] = Array.get(array, i);
        }
        return objectArray;
    }

    /**
     * Get the values of any ArrayUtils (primitive or not) into a {@code List<Object>}.
     *
     * @param array ArrayUtils passed as an object to support both primitive and Object ArrayUtils
     * @return the values of the given Object as a {@code List<Object>}.
     * @throws IllegalArgumentException it the given Object is not an ArrayUtils.
     */
    public static List<Object> asList(Object array) {
        return newArrayList(asObjectArray(array));
    }

    /**
     * Indicates whether the given ArrayUtils is {@code null} or empty.
     *
     * @param <T>   the type of elements of the ArrayUtils.
     * @param array the ArrayUtils to check.
     * @return {@code true} if the given ArrayUtils is {@code null} or empty, otherwise {@code false}.
     */
    public static <T> boolean isNullOrEmpty(final T[] array) {
        return array == null || isEmpty(array);
    }

    /**
     * Returns an ArrayUtils containing the given arguments.
     *
     * @param <T>    the type of the ArrayUtils to return.
     * @param values the values to store in the ArrayUtils.
     * @return an ArrayUtils containing the given arguments.
     */
    @SafeVarargs
    public static <T> T[] array(final T... values) {
        return values;
    }

    /**
     * Returns an int[] from the {@link AtomicIntegerArray}, null if the given atomic ArrayUtils is null.
     *
     * @param atomicIntegerArray the {@link AtomicIntegerArray} to convert to int[].
     * @return an int[].
     */
    public static int[] array(final AtomicIntegerArray atomicIntegerArray) {
        if (Objects.isNull(atomicIntegerArray)) {
            return null;
        }
        final int[] array = new int[atomicIntegerArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = atomicIntegerArray.get(i);
        }
        return array;
    }

    /**
     * Returns an long[] from the {@link AtomicLongArray}, null if the given atomic ArrayUtils is null.
     *
     * @param atomicLongArray the {@link AtomicLongArray} to convert to long[].
     * @return an long[].
     */
    public static long[] array(final AtomicLongArray atomicLongArray) {
        if (Objects.isNull(atomicLongArray)) {
            return null;
        }
        final long[] array = new long[atomicLongArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = atomicLongArray.get(i);
        }
        return array;
    }

    /**
     * Returns an T[] from the {@link AtomicReferenceArray}, null if the given atomic ArrayUtils is null.
     *
     * @param <T>                  the type of elements of the ArrayUtils.
     * @param atomicReferenceArray the {@link AtomicReferenceArray} to convert to T[].
     * @return an T[].
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] array(final AtomicReferenceArray<T> atomicReferenceArray) {
        if (Objects.isNull(atomicReferenceArray)) {
            return null;
        }
        int length = atomicReferenceArray.length();
        if (length == 0) return array();
        final List<T> list = newArrayList();
        for (int i = 0; i < length; i++) {
            list.add(atomicReferenceArray.get(i));
        }
        return list.toArray((T[]) Array.newInstance(Object.class, length));
    }

    /**
     * Returns all the non-{@code null} elements in the given ArrayUtils.
     *
     * @param <T>   the type of elements of the ArrayUtils.
     * @param array the given ArrayUtils.
     * @return all the non-{@code null} elements in the given ArrayUtils. An empty list is returned if the given ArrayUtils is
     * {@code null}.
     */
    public static <T> List<T> nonNullElementsIn(final T[] array) {
        if (Objects.isNull(array)) return emptyList();
        final List<T> nonNullElements = new ArrayList<>();
        for (final T o : array) {
            if (Objects.nonNull(o)) nonNullElements.add(o);
        }
        return nonNullElements;
    }

    /**
     * Returns {@code true} if the given ArrayUtils has only {@code null} elements, {@code false} otherwise. If given ArrayUtils is
     * empty, this method returns {@code true}.
     *
     * @param <T>   the type of elements of the ArrayUtils.
     * @param array the given ArrayUtils. <b>It must not be null</b>.
     * @return {@code true} if the given ArrayUtils has only {@code null} elements or is empty, {@code false} otherwise.
     * @throws NullPointerException if the given ArrayUtils is {@code null}.
     */
    public static <T> boolean hasOnlyNullElements(final T[] array) {
        Objects.requireNonNull(array, "Array should not be null");
        if (isEmpty(array)) return false;
        for (final T o : array) {
            if (Objects.nonNull(o)) return false;
        }
        return true;
    }

    private static <T> boolean isEmpty(final T[] array) {
        return array.length == 0;
    }

    public static boolean isObjectArray(final Object o) {
        return isArray(o) && !isArrayTypePrimitive(o);
    }

    public static boolean isArrayTypePrimitive(final Object o) {
        return Objects.nonNull(o) && o.getClass().getComponentType().isPrimitive();
    }

    public static IllegalArgumentException notAnArrayOfPrimitives(final Object o) {
        return new IllegalArgumentException(String.format("<%s> is not an ArrayUtils of primitives", o));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] prepend(final T first, final T... rest) {
        final T[] result = (T[]) new Object[1 + rest.length];
        result[0] = first;
        System.arraycopy(rest, 0, result, 1, rest.length);
        return result;
    }
}
