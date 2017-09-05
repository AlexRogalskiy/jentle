package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.interfaces.ResultVisitor;
import com.wildbeeslabs.jentle.collections.list.ACList.ACListNode;
import com.wildbeeslabs.jentle.collections.stack.CStack;

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

    abstract protected static class ACListNode<T, E extends ACListNode<T, E>> {

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

    protected void deleteDuplicates(final E node) {
        final Set<T> set = new HashSet<>();
        E previous = node, current = node;
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

    protected void deleteDuplicates2(final E node) {
        E current = node;
        while (Objects.nonNull(current)) {
            E runner = current;
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

    protected T getKthToLast(final E node, int k) {
        Integer idx = new Integer(0);
        E current = this.getKthToLast(node, k, idx);
        return Objects.nonNull(current) ? current.data : null;
    }

    private E getKthToLast(final E node, int k, Integer index) {
        if (this.isEmpty()) {
            return null;
        }
        E current = getKthToLast(node.next, k, index);
        index = index + 1;
        if (index.intValue() == k) {
            return node;
        }
        return current;
    }

    protected T getKthToLast2(final E node, int k) {
        E p1 = node, p2 = node;
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

    protected boolean delete(final E node) {
        if (this.isEmpty() || Objects.isNull(node) || Objects.isNull(node.next)) {
            return false;
        }
        E current = node.next;
        node.data = current.data;
        node.next = current.next;
        return true;
    }

    protected E insertBefore(final E list, final T data, final Class<E> clazz) {
        try {
            E node = clazz.newInstance();
            node.data = data;
            return this.insertBefore(list, node);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error("ERROR: cannot instantiate list node", ex);
        }
        return null;
    }

    protected E insertBefore(final E list, final E data) {
        if (Objects.nonNull(list)) {
            data.next = list;
        }
        return data;
    }

    protected E partition(final E node, final T value) {
        if (this.isEmpty()) {
            return null;
        }
        E beforeStart = null;
        E beforeEnd = null;
        E afterStart = null;
        E afterEnd = null;
        E current = node;
        while (Objects.nonNull(current)) {
            E next = current.next;
            current.next = null;
            if (Objects.compare(current.data, value, this.cmp) < 0) {
                if (Objects.isNull(beforeStart)) {
                    beforeStart = current;
                    beforeEnd = beforeStart;
                } else {
                    beforeEnd.next = current;
                    beforeEnd = current;
                }
            } else {
                if (Objects.nonNull(afterStart)) {
                    afterStart = current;
                    afterEnd = afterStart;
                } else {
                    afterEnd.next = current;
                    afterEnd = current;
                }
            }
            current = next;
        }
        if (Objects.isNull(beforeStart)) {
            return afterStart;
        }
        beforeStart.next = afterStart;
        return beforeStart;
    }

    public void addLists(final E first, final E last, final ACList<T, E> result, final ResultVisitor<T, T> visitor) {
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

    protected boolean isEqual(final E first, final E last) {
        E headFirst = first;
        E headLast = last;
        while (Objects.nonNull(headFirst) && Objects.nonNull(headLast)) {
            if (Objects.compare(headFirst.data, headLast.data, this.cmp) != 0) {
                return false;
            }
            headFirst = headFirst.next;
            headLast = headLast.next;
        }
        return (Objects.nonNull(headFirst) && Objects.nonNull(headLast));
    }

    protected boolean isPalindrome(final E node) {
        E fast = node;
        E slow = node;
        CStack<T> stack = new CStack<>();
        while (Objects.nonNull(fast) && Objects.nonNull(fast.next)) {
            stack.push(slow.data);
            slow = slow.next;
            fast = fast.next.next;
        }
        if (Objects.nonNull(fast)) {
            slow = slow.next;
        }
        try {
            while (Objects.nonNull(slow)) {
                if (Objects.compare(stack.pop(), slow.data, this.cmp) != 0) {
                    return false;
                }
                slow = slow.next;
            }
        } catch (EmptyStackException ex) {
            LOGGER.error("ERROR: empty stack", ex);
        }
        return true;
    }

    protected int length(final E node) {
        int size = 0;
        E current = node;
        while (Objects.nonNull(current)) {
            size++;
            current = current.next;
        }
        return size;
    }

    protected E getKthToFirst(final E node, int k) {
        E current = node;
        while (k > 0 && Objects.nonNull(current)) {
            current = current.next;
            k--;
        }
        return current;
    }

    private static final class ResultCNode<E> {

        public E head;
        public E tail;
        public int size;

        public ResultCNode() {
            this(null, null, 0);
        }

        public ResultCNode(final E head, int size) {
            this(head, null, size);
        }

        public ResultCNode(final E head, final E tail) {
            this(head, tail, 0);
        }

        public ResultCNode(final E head, final E tail, int size) {
            this.head = head;
            this.tail = tail;
            this.size = size;
        }
    }

    private ResultCNode<E> getTailAndSize(final E node) {
        if (Objects.isNull(node)) {
            return null;
        }
        int size = 1;
        E current = node;
        while (Objects.nonNull(current.next)) {
            size++;
            current = current.next;
        }
        return new ResultCNode(null, current, size);
    }

    protected E findIntersection(final E first, final E last) {
        if (Objects.isNull(first) || Objects.isNull(last)) {
            return null;
        }
        ResultCNode<E> res1 = this.getTailAndSize(first);
        ResultCNode<E> res2 = this.getTailAndSize(last);
        if (res1.tail != res2.tail) {
            return null;
        }
        E shorter = res1.size < res2.size ? first : last;
        E longer = res2.size < res2.size ? last : first;
        longer = this.getKthToFirst(longer, Math.abs(res1.size - res2.size));
        while (shorter != longer) {
            shorter = shorter.next;
            longer = longer.next;
        }
        return longer;
    }

    protected E findLoop(final E node) {
        E slow = node;
        E fast = node;
        while (Objects.nonNull(fast) && Objects.nonNull(fast.next)) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                break;
            }
        }
        if (Objects.isNull(fast) || Objects.isNull(fast.next)) {
            return null;
        }
        slow = node;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return fast;
    }
}
