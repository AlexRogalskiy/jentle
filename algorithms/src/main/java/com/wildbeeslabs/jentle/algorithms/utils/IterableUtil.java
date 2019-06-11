package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

@UtilityClass
public class IterableUtil {

    /**
     * Indicates whether the given {@link Iterable} is {@code null} or empty.
     *
     * @param iterable the given {@code Iterable} to check.
     * @return {@code true} if the given {@code Iterable} is {@code null} or empty, otherwise {@code false}.
     */
    public static boolean isNullOrEmpty(Iterable<?> iterable) {
        if (iterable == null) return true;
        if (iterable instanceof Collection && ((Collection<?>) iterable).isEmpty()) return true;
        return !iterable.iterator().hasNext();
    }

    /**
     * Returns the size of the given {@link Iterable}.
     *
     * @param iterable the {@link Iterable} to get size.
     * @return the size of the given {@link Iterable}.
     * @throws NullPointerException if given {@link Iterable} is null.
     */
    public static int sizeOf(Iterable<?> iterable) {
        checkNotNull(iterable, "Iterable must not be null");
        if (iterable instanceof Collection) return ((Collection<?>) iterable).size();
        int size = 0;
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            size++;
            iterator.next();
        }
        return size;
    }

    /**
     * Returns all the non-{@code null} elements in the given {@link Iterable}.
     *
     * @param <T> the type of elements of the {@code Iterable}.
     * @param i   the given {@code Iterable}.
     * @return all the non-{@code null} elements in the given {@code Iterable}. An empty list is returned if the given
     * {@code Iterable} is {@code null}.
     */
    public static <T> List<T> nonNullElementsIn(Iterable<? extends T> i) {
        if (isNullOrEmpty(i)) return emptyList();
        List<T> nonNull = new ArrayList<>();
        for (T element : i) {
            if (element != null) nonNull.add(element);
        }
        return nonNull;
    }

    /**
     * Create an array from an {@link Iterable}.
     * <p>
     * Note: this method will return Object[]. If you require a typed array please use {@link #toArray(Iterable, Class)}.
     * It's main usage is to keep the generic type for chaining call like in:
     * <pre><code class='java'> public S containsOnlyElementsOf(Iterable&lt;? extends T&gt; iterable) {
     *   return containsOnly(toArray(iterable));
     * }</code></pre>
     *
     * @param iterable an {@link Iterable} to translate in an array.
     * @param <T>      the type of elements of the {@code Iterable}.
     * @return all the elements from the given {@link Iterable} in an array. {@code null} if given {@link Iterable} is
     * null.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<? extends T> iterable) {
        if (iterable == null) return null;
        return (T[]) newArrayList(iterable).toArray();
    }

    /**
     * Create an typed array from an {@link Iterable}.
     *
     * @param iterable an {@link Iterable} to translate in an array.
     * @param type     the type of the resulting array.
     * @param <T>      the type of elements of the {@code Iterable}.
     * @return all the elements from the given {@link Iterable} in an array. {@code null} if given {@link Iterable} is
     * null.
     */
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
        if (iterable == null) return null;
        Collection<? extends T> collection = toCollection(iterable);
        T[] array = newArray(type, collection.size());
        return collection.toArray(array);
    }

    public static <T> Collection<T> toCollection(Iterable<T> iterable) {
        return iterable instanceof Collection ? (Collection<T>) iterable : newArrayList(iterable);
    }

    @SafeVarargs
    public static <T> Iterable<T> iterable(T... elements) {
        if (elements == null) return null;
        ArrayList<T> list = newArrayList();
        java.util.Collections.addAll(list, elements);
        return list;
    }

    @SafeVarargs
    public static <T> Iterator<T> iterator(T... elements) {
        if (elements == null) return null;
        return iterable(elements).iterator();
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }
}