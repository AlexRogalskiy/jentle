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
package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.interfaces.IResultVisitor;
import com.wildbeeslabs.jentle.collections.interfaces.IVisitor;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.stack.CStack;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom abstract list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class ACList<T, E extends ACListNode<T, E>> implements IList<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected E first;
    protected E last;
    protected int size;
    protected final Comparator<? super T> cmp;

    public ACList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public ACList(final IList<T> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ACList(final IList<T> source, final Comparator<? super T> cmp) {
        this.first = this.last = null;
        this.size = 0;
        this.cmp = cmp;
        this.addList(source);
    }

    public T head() throws EmptyListException {
        if (Objects.isNull(this.first)) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.first.getData();
    }

    public T tail() throws EmptyListException {
        if (Objects.isNull(this.last)) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.last.getData();
    }

    protected void addList(final IList<T> source) {
        if (Objects.nonNull(source)) {
            for (Iterator<? extends T> iterator = source.iterator(); iterator.hasNext();) {
                this.addLast(iterator.next());
            }
        }
    }

    protected E addToFirst(final T item) {
        final E temp = this.createNode(item);
        temp.setNext(this.first);
        if (Objects.isNull(this.first)) {
            this.last = temp;
        }
        this.first = temp;
        this.size++;
        return this.first;
    }

    protected E addToLast(final T item) {
        final E temp = this.createNode(item);
        temp.setNext(null);
        if (Objects.isNull(this.last)) {
            this.first = temp;
        } else {
            this.last.setNext(temp);
        }
        this.last = temp;
        this.size++;
        return this.last;
    }

    public T removeFirst() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        final T removed = this.first.getData();
        this.first = this.first.getNext();
        this.size--;
        return removed;
    }

    public T removeLast() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        E previous = this.first, next = this.first;
        while (Objects.nonNull(next.getNext())) {
            previous = next;
            next = next.getNext();
        }
        if (Objects.isNull(previous)) {
            this.first = null;
        } else {
            previous.setNext(null);
        }
        this.last = previous;
        this.size--;
        return next.getData();
    }

    @Override
    public boolean remove(final T item) throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        E previous = this.first, next = this.first;
        boolean removed = false;
        while (Objects.nonNull(next)) {
            if (Objects.compare(item, next.getData(), this.cmp) == 0) {
                removed = true;
                this.size--;
                if (Objects.nonNull(previous)) {
                    previous.setNext(next.getNext());
                }
            } else {
                previous = next;
            }
            next = next.getNext();
        }
        this.last = previous;
        return removed;
    }

    protected E insertToAt(final T item, int index) {
        this.checkRange(index);
        E previous = this.first, next = this.first;
        while (Objects.nonNull(next) && --index > 0) {
            previous = next;
            next = next.getNext();
        }
        final E temp = this.createNode(item);
        temp.setNext(next);
        if (Objects.isNull(next)) {
            this.last = temp;
        }
        if (Objects.isNull(previous)) {
            this.first = temp;
        } else {
            previous.setNext(temp);
        }
        this.size++;
        return temp;
    }

    protected E getToAt(int index) {
        this.checkRange(index);
        E current = this.first;
        while (Objects.nonNull(current) && --index > 0) {
            current = current.getNext();
        }
        if (Objects.nonNull(current)) {
            return current;
        }
        return null;
    }

    @Override
    public T getAt(int index) {
        final E current = this.getToAt(index);
        if (Objects.nonNull(current)) {
            return current.getData();
        }
        return null;
    }

    @Override
    public boolean contains(final T item) {
        for (Iterator<? extends T> i = this.iterator(); i.hasNext();) {
            if (Objects.compare(i.next(), item, this.cmp) == 0) {
                return true;
            }
        }
        return false;
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (index=%d is out of bounds [1, %d])", this.getClass().getName(), index, this.size));
        }
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public void clear() {
        this.first = this.last = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public boolean isDistinct() {
        for (Iterator<? extends T> i = this.iterator(); i.hasNext();) {
            T item = i.next();
            for (Iterator<? extends T> j = this.iterator(); j.hasNext();) {
                if (Objects.compare(j.next(), item, this.cmp) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void deleteDuplicates(final E node) {
        final Set<T> set = new HashSet<>();
        E previous = node, current = node;
        while (Objects.nonNull(current)) {
            if (set.contains(current.getData())) {
                previous.setNext(current.getNext());
            } else {
                set.add(current.getData());
                previous = current;
            }
            current = current.getNext();
        }
    }

    protected void deleteDuplicates2(final E node) {
        E current = node;
        while (Objects.nonNull(current)) {
            E runner = current;
            while (Objects.nonNull(runner.getNext())) {
                if (Objects.compare(runner.getNext().getData(), current.getData(), this.cmp) == 0) {
                    runner.setNext(runner.getNext().getNext());
                } else {
                    runner = runner.getNext();
                }
            }
            current = current.getNext();
        }
    }

    protected T getKthToLast(final E node, int k) {
        Integer idx = new Integer(0);
        E current = this.getKthToLast(node, k, idx);
        return Objects.nonNull(current) ? current.getData() : null;
    }

    private E getKthToLast(final E node, int k, Integer index) {
        if (this.isEmpty()) {
            return null;
        }
        E current = getKthToLast(node.getNext(), k, index);
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
            p1 = p1.getNext();
        }
        while (Objects.nonNull(p1)) {
            p1 = p1.getNext();
            p2 = p2.getNext();
        }
        return p2.getData();
    }

    protected boolean delete(final E node) {
        if (this.isEmpty() || Objects.isNull(node) || Objects.isNull(node.getNext())) {
            return false;
        }
        E current = node.getNext();
        node.setData(current.getData());
        node.setNext(current.getNext());
        return true;
    }

    protected E insertBefore(final E list, final T data) {
        return this.insertBefore(list, this.createNode(data));
    }

    protected E insertBefore(final E first, final E second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        second.setNext(first);
        E current = this.first, previous = null;
        while (Objects.nonNull(current) && current != first) {
            previous = current;
            current = current.getNext();
        }
        if (Objects.nonNull(previous)) {
            previous.setNext(second);
        }
        return second;
    }

    protected E insertAfter(final E list, final T data) {
        return this.insertAfter(list, this.createNode(data));
    }

    protected E insertAfter(final E first, final E second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        second.setNext(first.getNext());
        first.setNext(second);
        return second;
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
            final E next = current.getNext();
            current.setNext(null);
            if (Objects.compare(current.getData(), value, this.cmp) < 0) {
                if (Objects.isNull(beforeStart)) {
                    beforeStart = current;
                    beforeEnd = beforeStart;
                } else {
                    beforeEnd.setNext(current);
                    beforeEnd = current;
                }
            } else {
                if (Objects.nonNull(afterStart)) {
                    afterStart = current;
                    afterEnd = afterStart;
                } else {
                    afterEnd.setNext(current);
                    afterEnd = current;
                }
            }
            current = next;
        }
        if (Objects.isNull(beforeStart)) {
            return afterStart;
        }
        beforeStart.setNext(afterStart);
        return beforeStart;
    }

    public void addLists(final E first, final E last, final ACList<T, E> result, final IResultVisitor<T, T> visitor) {
        if (Objects.isNull(first) && Objects.isNull(last) || Objects.isNull(visitor)) {
            return;
        }
        if (Objects.nonNull(first)) {
            visitor.visit(first.getData());
        }
        if (Objects.nonNull(last)) {
            visitor.visit(last.getData());
        }
        result.addLast(visitor.getResult());
        if (Objects.nonNull(first) || Objects.nonNull(last)) {
            addLists(Objects.isNull(first) ? null : first.getNext(), Objects.isNull(last) ? null : last.getNext(), result, visitor);
        }
    }

    public void each(final IVisitor visitor) {
        for (Iterator<? extends T> current = this.iterator(); current.hasNext();) {
            visitor.visit(current.next());
        }
    }

    protected boolean isEqual(final E first, final E last) {
        E headFirst = first;
        E headLast = last;
        while (Objects.nonNull(headFirst) && Objects.nonNull(headLast)) {
            if (Objects.compare(headFirst.getData(), headLast.getData(), this.cmp) != 0) {
                return false;
            }
            headFirst = headFirst.getNext();
            headLast = headLast.getNext();
        }
        return (Objects.nonNull(headFirst) && Objects.nonNull(headLast));
    }

    protected boolean isPalindrome(final E node) throws OverflowStackException {
        E fast = node;
        E slow = node;
        final CStack<T> stack = new CStack<>();
        while (Objects.nonNull(fast) && Objects.nonNull(fast.getNext())) {
            stack.push(slow.getData());
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        if (Objects.nonNull(fast)) {
            slow = slow.getNext();
        }
        try {
            while (Objects.nonNull(slow)) {
                if (Objects.compare(stack.pop(), slow.getData(), this.cmp) != 0) {
                    return false;
                }
                slow = slow.getNext();
            }
        } catch (EmptyStackException ex) {
            LOGGER.error(String.format("ERROR: empty stack, message=%s", ex.getMessage()));
        }
        return true;
    }

    protected int length(final E node) {
        int size = 0;
        E current = node;
        while (Objects.nonNull(current)) {
            size++;
            current = current.getNext();
        }
        return size;
    }

    protected E getKthToFirst(final E node, int k) {
        E current = node;
        while (k > 0 && Objects.nonNull(current)) {
            current = current.getNext();
            k--;
        }
        return current;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
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
        while (Objects.nonNull(current.getNext())) {
            size++;
            current = current.getNext();
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
            shorter = shorter.getNext();
            longer = longer.getNext();
        }
        return longer;
    }

    protected E findLoop(final E node) {
        E slow = node;
        E fast = node;
        while (Objects.nonNull(fast) && Objects.nonNull(fast.getNext())) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
            if (slow == fast) {
                break;
            }
        }
        if (Objects.isNull(fast) || Objects.isNull(fast.getNext())) {
            return null;
        }
        slow = node;
        while (slow != fast) {
            slow = slow.getNext();
            fast = fast.getNext();
        }
        return fast;
    }

    public void deleteDuplicates() {
        this.deleteDuplicates(this.first);
    }

    public T getKthToLast2(int k) {
        return this.getKthToLast2(this.first, k);
    }

    public E partition(final T value) {
        return this.partition(this.first, value);
    }

    public boolean isPalindrome() throws OverflowStackException {
        return this.isPalindrome(this.first);
    }

    @Override
    public Queue<T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract E createNode(final T value);
}
