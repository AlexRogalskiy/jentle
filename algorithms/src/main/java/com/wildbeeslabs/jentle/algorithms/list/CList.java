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

import com.wildbeeslabs.jentle.collections.list.node.ACListNode;

import java.util.Comparator;
import java.util.Objects;

/**
 *
 * Custom list algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CList {

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

    public static <T, U extends ACListNode<T, U>> U findNthNodeFromLast(U root, int n) {
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

    public static <T, U extends ACListNode<T, U>> U findMiddleNode(U root) {
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
}
