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
package com.wildbeeslabs.jentle.algorithms.list;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.stack.CStack;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom list algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CList {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CList.class);

    private CList() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, U extends ACListNode<T, U>> U merge(final U first, final U last, final Comparator<? super T> cmp) {
        U mergedList = null;
        if (Objects.isNull(first)) {
            return last;
        }
        if (Objects.isNull(last)) {
            return first;
        }
        if (Objects.compare(first.getData(), last.getData(), cmp) < 0) {
            mergedList = first;
            mergedList.setNext(merge(first.getNext(), last, cmp));
        } else {
            mergedList = last;
            mergedList.setNext(merge(first, last.getNext(), cmp));
        }
        return mergedList;
    }

    public static <T, U extends ACListNode<T, U>> U intersect(final U first, final U last) {
        int length1 = length(first);
        int length2 = length(last);

        U largerList = null;
        U smallerList = null;
        if (length1 > length2) {
            largerList = first;
            smallerList = last;
        } else {
            largerList = last;
            smallerList = first;
        }
        return getIntersetPoint(largerList, smallerList, Math.abs(length1 - length2));
    }

    private static <T, U extends ACListNode<T, U>> U getIntersetPoint(U largerList, U smallerList, int lengthDifference) {
        int count = 0;
        while (count++ < lengthDifference) {
            largerList = largerList.getNext();
        }
        while (Objects.nonNull(largerList) && Objects.nonNull(smallerList)) {
            if (largerList == smallerList) {
                return largerList;
            }
            largerList = largerList.getNext();
            smallerList = smallerList.getNext();
        }
        return null;
    }

    public static <T, U extends ACListNode<T, U>> int length(U root) {
        int length = 0;
        while (Objects.nonNull(root)) {
            root = root.getNext();
            length++;
        }
        return length;
    }

    public static <T, U extends ACListNode<T, U>> U findNthNodeFromLast(final U root, int n) {
        if (Objects.isNull(root)) {
            return null;
        }
        U slow = root;
        U fast = root;
        int index = 0;
        while (index++ < n) {
            if (Objects.isNull(fast)) {
                return null;
            }
            fast = fast.getNext();
        }
        while (Objects.nonNull(fast)) {
            slow = slow.getNext();
            fast = fast.getNext();
        }
        return slow;
    }

    public static <T, U extends ACListNode<T, U>> U findMiddleNode(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        U slow = root;
        U fast = root;
        while (Objects.nonNull(fast) && Objects.nonNull(fast.getNext())) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        return slow;
    }

    public static <T, U extends ACListNode<T, U>> U reverseList(U root) {
        U next = null, prev = null;
        while (Objects.nonNull(root)) {
            next = root.getNext();
            root.setNext(prev);
            prev = root;
            root = next;
        }
        return prev;
    }

    public static <T, U extends ACListNode<T, U>> boolean isPalindrome(final U node, final Comparator<? super T> cmp) throws OverflowStackException {
        U fast = node;
        U slow = node;
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
                if (Objects.compare(stack.pop(), slow.getData(), cmp) != 0) {
                    return false;
                }
                slow = slow.getNext();
            }
        } catch (EmptyStackException ex) {
            LOGGER.error(String.format("ERROR: empty stack, message=%s", ex.getMessage()));
        }
        return true;
    }

    //Floyd’s cycle detection algorithm
    public static <T, U extends ACListNode<T, U>> U findLoop(final U node) {
        U slow = node;
        U fast = node;
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

    public static <T, U extends ACListNode<T, U>> U findIntersection(final U first, final U last) {
        if (Objects.isNull(first) || Objects.isNull(last)) {
            return null;
        }
        ResultCNode<U> res1 = getTailAndSize(first);
        ResultCNode<U> res2 = getTailAndSize(last);
        if (res1.tail != res2.tail) {
            return null;
        }
        U shorter = res1.size < res2.size ? first : last;
        U longer = res2.size < res2.size ? last : first;
        longer = getKthToFirst(longer, Math.abs(res1.size - res2.size));
        while (shorter != longer) {
            shorter = shorter.getNext();
            longer = longer.getNext();
        }
        return longer;
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

    private static <T, U extends ACListNode<T, U>> ResultCNode<U> getTailAndSize(final U node) {
        if (Objects.isNull(node)) {
            return null;
        }
        int size = 1;
        U current = node;
        while (Objects.nonNull(current.getNext())) {
            size++;
            current = current.getNext();
        }
        return new ResultCNode(null, current, size);
    }

    public static <T, U extends ACListNode<T, U>> U getKthToFirst(final U node, int k) {
        U current = node;
        while (k > 0 && Objects.nonNull(current)) {
            current = current.getNext();
            k--;
        }
        return current;
    }

    public static <T, U extends ACListNode<T, U>> boolean areListsEqual(final U first, final U last, final Comparator<? super T> cmp) {
        U headFirst = first;
        U headLast = last;
        while (Objects.nonNull(headFirst) && Objects.nonNull(headLast)) {
            if (Objects.compare(headFirst.getData(), headLast.getData(), cmp) != 0) {
                return false;
            }
            headFirst = headFirst.getNext();
            headLast = headLast.getNext();
        }
        return (Objects.nonNull(headFirst) && Objects.nonNull(headLast));
    }

    public static <T, U extends ACListNode<T, U>> U partition(final U node, final T value, final Comparator<? super T> cmp) {
        if (Objects.isNull(node)) {
            return null;
        }
        U beforeStart = null;
        U beforeEnd = null;
        U afterStart = null;
        U afterEnd = null;
        U current = node;
        while (Objects.nonNull(current)) {
            final U next = current.getNext();
            current.setNext(null);
            if (Objects.compare(current.getData(), value, cmp) < 0) {
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

    public static <T, U extends ACListNode<T, U>> U partition2(U node, final T value, final Comparator<? super T> cmp) {
        U head = node;
        U tail = node;
        while (Objects.nonNull(node)) {
            U next = node.getNext();
            if (Objects.compare(node.getData(), value, cmp) < 0) {
                node.setNext(head);
                head = node;
            } else {
                tail.setNext(node);
                tail = node;
            }
            node = next;
        }
        tail.setNext(null);
        return head;
    }

    public static <T, U extends ACListNode<T, U>> T getKthToLast(final U node, int k) {
        int[] idx = new int[]{0};
        final U current = getKthToLast(node, k, idx);
        return Objects.nonNull(current) ? current.getData() : null;
    }

    private static <T, U extends ACListNode<T, U>> U getKthToLast(final U node, int k, int[] index) {
        if (Objects.isNull(node)) {
            return null;
        }
        U current = getKthToLast(node.getNext(), k, index);
        index[0] += 1;
        if (index[0] == k) {
            return node;
        }
        return current;
    }

    public static <T, U extends ACListNode<T, U>> T getKthToLast2(final U node, int k) {
        U p1 = node, p2 = node;
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

    public static <T, U extends ACListNode<T, U>> U reverse(U root) {
        U previous = root;
        U newHead = root.getNext();
        U temp = root.getNext();
        while (Objects.nonNull(root) && Objects.nonNull(root.getNext())) {
            previous.setNext(root.getNext());
            root.setNext(temp.getNext());
            temp.setNext(root);
            if (Objects.nonNull(root.getNext())) {
                previous = root;
                root = root.getNext();
                temp = root.getNext();
            }
        }
        return newHead;
    }

    public static <T, U extends ACListNode<T, U>> void deleteDuplicates(final U node) {
        final Set<T> set = new HashSet<>();
        U previous = node, current = node;
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

    public static <T, U extends ACListNode<T, U>> void deleteDuplicates2(final U node, final Comparator<? super T> cmp) {
        U current = node;
        while (Objects.nonNull(current)) {
            U runner = current;
            while (Objects.nonNull(runner.getNext())) {
                if (Objects.compare(runner.getNext().getData(), current.getData(), cmp) == 0) {
                    runner.setNext(runner.getNext().getNext());
                } else {
                    runner = runner.getNext();
                }
            }
            current = current.getNext();
        }
    }

    public static <T, U extends ACListNode<T, U>> int countTimes(final U root, final T value, final Comparator<? super T> cmp) {
        int count = 0;
        U current = root;
        while (Objects.nonNull(current)) {
            if (Objects.compare(current.getData(), value, cmp) == 0) {
                count++;
            }
            current = current.getNext();
        }
        return count;
    }
}
