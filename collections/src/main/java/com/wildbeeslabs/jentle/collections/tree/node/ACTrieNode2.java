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
package com.wildbeeslabs.jentle.collections.tree.node;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract trie node2 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class ACTrieNode2<T, U extends ACTrieNode2<T, U>> extends ACBaseTrieNode<T, U> {

    protected U child;
    protected U brother;

    public ACTrieNode2() {
        this(null);
    }

    public ACTrieNode2(final T data) {
        this(null, Boolean.FALSE);
    }

    public ACTrieNode2(final T data, boolean isTerminated) {
        this(data, isTerminated, null, null);
    }

    public ACTrieNode2(final T data, boolean terminated, final U child, final U brother) {
        super(data, terminated);
        this.child = child;
        this.brother = brother;
    }

    public boolean hasChild() {
        return Objects.nonNull(this.child);
    }

    public boolean hasBrother() {
        return Objects.nonNull(this.brother);
    }
}
