package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * Utility methods related to {@link Set}s.
 *
 * @author alruiz
 */
@UtilityClass
public class Sets {
    /**
     * Creates a <em>mutable</em> {@code HashSet}.
     *
     * @param <T> the generic type of the {@code HashSet} to create.
     * @return the created {@code HashSet}.
     */
    public static <T> HashSet<T> newHashSet() {
        return new HashSet<>();
    }

    /**
     * Creates a <em>mutable</em> {@code HashSet} containing the given elements.
     *
     * @param <T>      the generic type of the {@code HashSet} to create.
     * @param elements the elements to store in the {@code HashSet}.
     * @return the created {@code HashSet}, or {@code null} if the given ArrayUtils of elements is {@code null}.
     */
    public static <T> HashSet<T> newHashSet(final Iterable<? extends T> elements) {
        if (Objects.isNull(elements)) {
            return null;
        }
        final HashSet<T> set = newHashSet();
        for (final T e : elements) {
            set.add(e);
        }
        return set;
    }

    /**
     * Creates a <em>mutable</em> {@code LinkedHashSet}.
     *
     * @param <T> the generic type of the {@code LinkedHashSet} to create.
     * @return the created {@code LinkedHashSet}.
     */
    public static <T> LinkedHashSet<T> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    /**
     * Creates a <em>mutable</em> {@link LinkedHashSet} containing the given elements.
     *
     * @param <T>      the generic type of the {@code LinkedHashSet} to create.
     * @param elements the elements to store in the {@code LinkedHashSet}.
     * @return the created {@code LinkedHashSet}, or {@code null} if the given ArrayUtils of elements is {@code null}.
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(final T... elements) {
        if (Objects.isNull(elements)) {
            return null;
        }
        final LinkedHashSet<T> set = newLinkedHashSet();
        java.util.Collections.addAll(set, elements);
        return set;
    }

    /**
     * Creates a <em>mutable</em> {@link TreeSet}.
     *
     * @param <T> the generic type of the {@link TreeSet} to create.
     * @return the created {@link TreeSet}.
     */
    public static <T> TreeSet<T> newTreeSet() {
        return new TreeSet<>();
    }

    /**
     * Creates a <em>mutable</em> {@link TreeSet} containing the given elements.
     *
     * @param <T>      the generic type of the {@link TreeSet} to create.
     * @param elements the elements to store in the {@link TreeSet}.
     * @return the created {@link TreeSet}, or {@code null} if the given ArrayUtils of elements is {@code null}.
     */
    @SafeVarargs
    public static <T> TreeSet<T> newTreeSet(final T... elements) {
        if (Objects.isNull(elements)) {
            return null;
        }
        final TreeSet<T> set = newTreeSet();
        java.util.Collections.addAll(set, elements);
        return set;
    }
}
