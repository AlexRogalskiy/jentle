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
package com.wildbeeslabs.jentle.collections.tree.iface.trie;

import com.wildbeeslabs.jentle.collections.iface.collection.CollectionLike;
import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTrieNode;

/**
 * Custom trie {@link CollectionLike} interface declaration
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ITrie<T, U extends ACBaseTrieNode<T, U>> extends CollectionLike<T> {

    /**
     * Returns the size of the tree.
     *
     * @return size of the tree.
     */
    int size();

    /**
     * Checks if trie is empty
     *
     * @return true - if tree is empty, false - otherwise
     */
    boolean isEmpty();

    /**
     * Returns current root node
     *
     * @return - current root node
     */
    U getRoot();

    /**
     * Checks if trie contains character sequence
     *
     * @param value - character sequence
     * @return true - if trie contains character sequence, false - otherwise
     */
    boolean contains(final CharSequence value);
}
