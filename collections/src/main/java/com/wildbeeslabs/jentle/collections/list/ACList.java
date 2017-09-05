package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.interfaces.ResultVisitor;
import com.wildbeeslabs.jentle.collections.list.ACList.ACListNode;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Abstract Custom list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
abstract public class ACList<T, E extends ACListNode<T, E>> implements IList<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    protected abstract static class ACListNode<T, E extends ACListNode<T, E>> {

        protected T data;
        protected E next;
        protected final Comparator<? super T> cmp;

        public ACListNode() {
            this(null);
        }

        public ACListNode(final T data) {
            this(data, null);
        }

        public ACListNode(final T data, final E next) {
            this(data, next, CSort.DEFAULT_SORT_COMPARATOR);
        }

        public ACListNode(final T data, final E next, final Comparator<? super T> cmp) {
            this.data = data;
            this.next = next;
            this.cmp = cmp;
        }

        @Override
        public String toString() {
            return String.format("ACListNode {data: %s, next: %s}", this.data, this.next);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final ACListNode<T, E> other = (ACListNode<T, E>) obj;
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            if (!Objects.equals(this.next, other.next)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + Objects.hashCode(this.data);
            hash = 11 * hash + Objects.hashCode(this.next);
            return hash;
        }
    }

    protected final Comparator<? super T> cmp;

    public ACList(final Comparator<? super T> cmp) {
        this.cmp = cmp;
    }

    protected void deleteDuplicates(final ACListNode<T, E> node) {
        final Set<T> set = new HashSet<>();
        ACListNode<T, E> previous = node, current = node;
        while (Objects.nonNull(current)) {
            if (set.contains(current.data)) {
                previous.next = current.next;
            } else {
                set.add(current.data);
                previous = current;
            }
            current = current.next;
        }
    }

    protected void deleteDuplicates2(final ACListNode<T, E> node) {
        ACListNode<T, E> current = node;
        while (Objects.nonNull(current)) {
            ACListNode<T, E> runner = current;
            while (Objects.nonNull(runner.next)) {
                if (Objects.compare(runner.next.data, current.data, this.cmp) == 0) {
                    runner.next = runner.next.next;
                } else {
                    runner = runner.next;
                }
            }
            current = current.next;
        }
    }

    protected T getKthToLast(final ACListNode<T, E> node, int k) {
        Integer idx = new Integer(0);
        ACListNode<T, E> current = this.getKthToLast(node, k, idx);
        return Objects.nonNull(current) ? current.data : null;
    }

    private ACListNode<T, E> getKthToLast(final ACListNode<T, E> node, int k, Integer idx) {
        if (this.isEmpty()) {
            return null;
        }
        ACListNode<T, E> current = getKthToLast(node.next, k, idx);
        idx = idx + 1;
        if (idx.intValue() == k) {
            return node;
        }
        return current;
    }

    protected T getKthToLast2(final ACListNode<T, E> node, int k) {
        ACListNode<T, E> p1 = node, p2 = node;
        for (int i = 0; i < k; i++) {
            if (Objects.isNull(p1)) {
                return null;
            }
            p1 = p1.next;
        }
        while (Objects.nonNull(p1)) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p2.data;
    }

    protected boolean delete(final ACListNode<T, E> node) {
        if (this.isEmpty() || Objects.isNull(node) || Objects.isNull(node.next)) {
            return false;
        }
        ACListNode<T, E> current = node.next;
        node.data = current.data;
        node.next = current.next;
        return true;
    }

    protected ACListNode<T, E> partition(final ACListNode<T, E> node, final T value) {
        if (this.isEmpty()) {
            return null;
        }
        ACListNode<T, E> beforeStart = null;
        ACListNode<T, E> beforeEnd = null;
        ACListNode<T, E> afterStart = null;
        ACListNode<T, E> afterEnd = null;
        ACListNode<T, E> current = node;
        while (Objects.nonNull(current)) {
            ACListNode<T, E> next = current.next;
            current.next = null;
            if (Objects.compare(current.data, value, this.cmp) < 0) {
                if (Objects.isNull(beforeStart)) {
                    beforeStart = current;
                    beforeEnd = beforeStart;
                } else {
                    beforeEnd.next = (E) current;
                    beforeEnd = current;
                }
            } else {
                if (Objects.nonNull(afterStart)) {
                    afterStart = current;
                    afterEnd = afterStart;
                } else {
                    afterEnd.next = (E) current;
                    afterEnd = current;
                }
            }
            current = next;
        }
        if (Objects.isNull(beforeStart)) {
            return afterStart;
        }
        beforeStart.next = (E) afterStart;
        return beforeStart;
    }

    public void addLists(final ACListNode<T, E> first, final ACListNode<T, E> last, final ACList<T, E> result, final ResultVisitor<T, T> visitor) {
        if (Objects.isNull(first) && Objects.isNull(last) || Objects.isNull(visitor)) {
            return;
        }
        if (Objects.nonNull(first)) {
            visitor.visit(first.data);
        }
        if (Objects.nonNull(last)) {
            visitor.visit(last.data);
        }
        result.addLast(visitor.getResult());
        if (Objects.nonNull(first) || Objects.nonNull(last)) {
            addLists(Objects.isNull(first) ? null : first.next, Objects.isNull(last) ? null : last.next, result, visitor);
        }
    }
}
