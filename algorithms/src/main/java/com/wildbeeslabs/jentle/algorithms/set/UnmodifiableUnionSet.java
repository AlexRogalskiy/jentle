package com.wildbeeslabs.jentle.algorithms.set;

import java.io.Serializable;
import java.util.*;

/**
 * An unmodifiable live view of the union of two sets.
 *
 * @param <E> the element type
 * @author Dimitrios Michail
 */
public class UnmodifiableUnionSet<E> extends AbstractSet<E> implements Serializable {
    private static final long serialVersionUID = -1937327799873331354L;

    private final Set<E> first;
    private final Set<E> second;

    /**
     * Constructs a new set.
     *
     * @param first  the first set
     * @param second the second set
     */
    public UnmodifiableUnionSet(final Set<E> first, final Set<E> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        this.first = first;
        this.second = second;
    }

    @Override
    public Iterator<E> iterator() {
        return new UnionIterator(this.orderSetsBySize());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Since the view is live, this operation is no longer a constant time operation.
     */
    @Override
    public int size() {
        SetSizeOrdering ordering = orderSetsBySize();
        Set<E> bigger = ordering.bigger;
        int count = ordering.biggerSize;
        for (E e : ordering.smaller) {
            if (!bigger.contains(e)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean contains(Object o) {
        return first.contains(o) || second.contains(o);
    }

    private SetSizeOrdering orderSetsBySize() {
        int firstSize = first.size();
        int secondSize = second.size();
        if (secondSize > firstSize) {
            return new SetSizeOrdering(second, first, secondSize, firstSize);
        } else {
            return new SetSizeOrdering(first, second, firstSize, secondSize);
        }
    }

    // note that these inner classes could be static, but we
    // declare them as non-static to avoid the clutter from
    // duplicating the generic type parameter

    private class SetSizeOrdering {
        final Set<E> bigger;
        final Set<E> smaller;
        final int biggerSize;
        final int smallerSize;

        SetSizeOrdering(Set<E> bigger, Set<E> smaller, int biggerSize, int smallerSize) {
            this.bigger = bigger;
            this.smaller = smaller;
            this.biggerSize = biggerSize;
            this.smallerSize = smallerSize;
        }
    }

    private class UnionIterator
        implements
        Iterator<E> {
        private SetSizeOrdering ordering;
        private boolean inBiggerSet;
        private Iterator<E> iterator;
        private E cur;

        UnionIterator(SetSizeOrdering ordering) {
            this.ordering = ordering;
            this.inBiggerSet = true;
            this.iterator = ordering.bigger.iterator();
            this.cur = prefetch();
        }

        @Override
        public boolean hasNext() {
            if (cur != null) {
                return true;
            }
            return (cur = prefetch()) != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E result = cur;
            cur = null;
            return result;
        }

        private E prefetch() {
            while (true) {
                if (inBiggerSet) {
                    if (iterator.hasNext()) {
                        return iterator.next();
                    } else {
                        inBiggerSet = false;
                        iterator = ordering.smaller.iterator();
                    }
                } else {
                    if (iterator.hasNext()) {
                        E elem = iterator.next();
                        if (!ordering.bigger.contains(elem)) {
                            return elem;
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
