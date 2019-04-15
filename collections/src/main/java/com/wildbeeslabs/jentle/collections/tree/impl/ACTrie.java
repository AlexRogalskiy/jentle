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
package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.tree.iface.ITrie;
import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTrieNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

/**
 * Custom abstract trie implementation
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public abstract class ACTrie<T, U extends ACBaseTrieNode<T, U>> implements ITrie<T, U> {

    @Setter(AccessLevel.NONE)
    protected U root;
    protected int size;
    protected Comparator<? super T> cmp;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ACTrie(final Comparator<? super T> cmp) {
        this.root = this.createTrieNode(Optional.empty());
        this.cmp = cmp;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public boolean contains(final CharSequence value) {
        return this.contains(value, this.root, false);
    }

    @Override
    public U getRoot() {
        return this.root;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract U createTrieNode(final Optional<? extends T> value);

    protected abstract boolean contains(final CharSequence value, final U root, boolean isExact);
}
