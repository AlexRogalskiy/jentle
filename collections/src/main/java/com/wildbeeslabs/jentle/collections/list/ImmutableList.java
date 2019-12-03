package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.array.ImmutableArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable lists.
 *
 * @author Martin Kouba
 */
public final class ImmutableList {

    /**
     * @param list
     * @return an immutable copy of the given list
     */
    public static <T> List<T> copyOf(List<T> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        if (list.size() == 1) {
            return Collections.singletonList(list.get(0));
        }
        return new ImmutableArrayList<>(list.toArray());
    }

    /**
     * @param elements
     * @return an immutable list of the given elements
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        if (elements.length == 0) {
            return Collections.emptyList();
        }
        if (elements.length == 1) {
            return Collections.singletonList(elements[0]);
        }
        return new ImmutableArrayList<>(elements);
    }

    /**
     * @return a builder
     */
    public static <T> ImmutableListBuilder<T> builder() {
        return new ImmutableListBuilder<>();
    }

    public static final class ImmutableListBuilder<T> {

        private List<T> elements;

        private ImmutableListBuilder() {
            this.elements = new ArrayList<>();
        }

        public ImmutableListBuilder<T> add(T element) {
            elements.add(element);
            return this;
        }

        public List<T> build() {
            return copyOf(elements);
        }

    }

}
