package com.wildbeeslabs.jentle.collections.set;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Immutable sets.
 *
 * @author Martin Kouba
 */
public final class ImmutableSet {

    /**
     * @param set
     * @return an immutable copy of the given set
     */
    public static <T> Set<T> copyOf(Set<T> set) {
        if (set.isEmpty()) {
            return Collections.emptySet();
        }
        if (set.size() == 1) {
            return Collections.singleton(set.iterator().next());
        }
        return Set.copyOf(set);
    }

    /**
     * @param elements
     * @return an immutable set of the given elements
     */
    @SafeVarargs
    public static <T> Set<T> of(T... elements) {
        if (elements.length == 0) {
            return Collections.emptySet();
        }
        if (elements.length == 1) {
            return Collections.singleton(elements[0]);
        }
        Set<T> set = new HashSet<>();
        Collections.addAll(set, elements);
        return Collections.unmodifiableSet(set);
    }

    /**
     * @return a builder
     */
    public static <T> ImmutableSetBuilder<T> builder() {
        return new ImmutableSetBuilder<>();
    }

    public static final class ImmutableSetBuilder<T> {

        private Set<T> elements;

        private ImmutableSetBuilder() {
            this.elements = new HashSet<>();
        }

        public ImmutableSetBuilder<T> add(T element) {
            elements.add(element);
            return this;
        }

        public Set<T> build() {
            return copyOf(elements);
        }
    }
}
