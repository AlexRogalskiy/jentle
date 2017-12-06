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
package com.wildbeeslabs.jentle.collections.utils;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.graph.CLGraph;
import com.wildbeeslabs.jentle.collections.graph.node.CGraphNode;
import com.wildbeeslabs.jentle.collections.interfaces.IStack;
import com.wildbeeslabs.jentle.collections.stack.CStack;

import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import static org.apache.commons.lang3.ArrayUtils.toArray;

/**
 *
 * Collection utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CUtils {

    private CUtils() {
        // PRIVATE CONSTRUCTOR
    }

    public static Object[] merge(final Object o1, final Object o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);
        if (!o1.getClass().isArray() && !o2.getClass().isArray()) {
            return new Object[]{o1, o2};
        }
        Object[] a1 = toArray(o1);
        Object[] a2 = toArray(o2);
        return ArrayUtils.addAll(a1, a2);
    }

    public static <T> T safeCast(final Object o, final Class<T> clazz) {
        Objects.requireNonNull(o);
        return (clazz.isInstance(o)) ? clazz.cast(o) : null;
    }

    public static <T> Collection<? extends T> union(final Iterable<? extends T> first, final Iterable<? extends T> second) {
        return CollectionUtils.union(first, second);
        //IterableUtils.chainedIterable(collectionA, collectionB);
        //Stream<? extends T> combinedStream = Stream.of(first, second).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> IStack<T> sort(final IStack<T> stack, final Comparator<? super T> cmp) throws EmptyStackException {
        final IStack<T> result = new CStack<>();
        while (!stack.isEmpty()) {
            final T temp = stack.pop();
            while (!result.isEmpty() && Objects.compare(result.peek(), temp, cmp) > 1) {
                stack.push(result.pop());
            }
            result.push(temp);
        }
        return result;
    }

    public static <T> boolean search(final CLGraph<T> graph, final CGraphNode<T> start, final CGraphNode<T> end) {//IGraph<T>
        if (start == end) {
            return true;
        }
        final Deque<CGraphNode<T>> listNode = new LinkedList<>();
        for (final CGraphNode<T> node : graph.getNodes()) {
            node.setState(CGraphNode.State.UNVISITED);
        }
        start.setState(CGraphNode.State.VISITING);
        listNode.add(start);
        while (!listNode.isEmpty()) {
            CGraphNode<T> temp = listNode.removeFirst();
            if (Objects.nonNull(temp)) {
                for (final CGraphNode<T> v : temp.getAdjacent()) {
                    if (Objects.equals(v.getState(), CGraphNode.State.UNVISITED)) {
                        if (v == end) {
                            return true;
                        } else {
                            v.setState(CGraphNode.State.VISITING);
                            listNode.add(v);
                        }
                    }
                }
                temp.setState(CGraphNode.State.VISITED);
            }
        }
        return false;
    }
}
