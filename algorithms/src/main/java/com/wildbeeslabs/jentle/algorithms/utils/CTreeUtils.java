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
package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import java.util.Objects;

/**
 *
 * Custom tree utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CTreeUtils {

    private CTreeUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> ACTreeNodeExtended<T, U> commonAncestor(final ACTreeNodeExtended<T, U> firstNode, final ACTreeNodeExtended<T, U> secondNode) {
        Objects.requireNonNull(firstNode);
        Objects.requireNonNull(secondNode);
        int delta = depth(firstNode) - depth(secondNode);
        ACTreeNodeExtended<T, U> first = delta > 0 ? secondNode : firstNode;
        ACTreeNodeExtended<T, U> second = delta > 0 ? firstNode : secondNode;
        second = goUpBy(second, Math.abs(delta));
        while (first != second && Objects.nonNull(first) && Objects.nonNull(second)) {
            first = first.getParent();
            second = second.getParent();
        }
        return Objects.isNull(first) || Objects.isNull(second) ? null : first;
    }

    private static <T, U extends ACTreeNodeExtended<T, U>> ACTreeNodeExtended<T, U> goUpBy(ACTreeNodeExtended<T, U> node, int delta) {
        while (delta > 0 && Objects.nonNull(node)) {
            node = node.getParent();
            delta--;
        }
        return node;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> int depth(ACTreeNodeExtended<T, U> node) {
        int depth = 0;
        while (Objects.nonNull(node)) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }
}
