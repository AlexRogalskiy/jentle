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
package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;

import java.util.Objects;

/**
 * Custom {@link ITreeCollection} extended interface declaration
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ITreeCollectionExtended<T, U extends ACTreeNodeExtended<T, U>> extends ITreeCollection<T, U> {

    /**
     * Checks if current node is left child
     *
     * @param node - current node
     * @return true - if current node is left child node, false - otherwise
     */
    default boolean isLeftChild(final U node) {
        Objects.requireNonNull(node);
        if (Objects.isNull(node.getParent())) {
            return false;
        }
        return (node.getParent().getLeft() == node);
    }

    /**
     * Checks if current node is right child
     *
     * @param node - current node
     * @return true - if current node is right child node, false - otherwise
     */
    default boolean isRightChild(final U node) {
        Objects.requireNonNull(node);
        if (Objects.isNull(node.getParent())) {
            return false;
        }
        return (node.getParent().getRight() == node);
    }

    /**
     * Checks if current node has parent node
     *
     * @param node - current node
     * @return true - if current node has parent node, false - otherwise
     */
    default boolean hasParent(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getParent()));
    }

    /**
     * Returns parent node of the current node
     *
     * @param node - current node
     * @return parent node
     */
    default U getParent(final U node) {
        if (this.hasParent(node)) {
            return node.getParent();
        }
        return null;
    }

    /**
     * Returns depth of the current node
     *
     * @param node - current node
     * @return number of nodes in parent hierarchy
     */
    default int depth(final U node) {
        if (this.isRoot(node)) {
            return 0;
        }
        return 1 + this.depth(node.getParent());
    }
}
