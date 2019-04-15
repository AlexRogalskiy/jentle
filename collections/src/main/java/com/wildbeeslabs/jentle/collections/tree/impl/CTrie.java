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

import com.wildbeeslabs.jentle.collections.tree.node.ACTrieNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom trie implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CTrie extends ACTrie<Integer, CTrie.CTrieNode<Integer>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CTrieNode<T> extends ACTrieNode<T, CTrieNode<T>> {

        public CTrieNode() {
            this(null);
        }

        public CTrieNode(final T data) {
            super(data);
        }
    }

    public CTrie() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie(final Comparator<? super Integer> cmp) {
        this(null, cmp);
    }

    public CTrie(final List<? extends CharSequence> list) {
        this(list, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie(final CharSequence[] array) {
        this(Arrays.asList(array), CUtils.DEFAULT_SORT_COMPARATOR);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public CTrie(final List<? extends CharSequence> list, final Comparator<? super Integer> cmp) {
        super(cmp);
        this.init(list);
    }

    protected void init(final List<? extends CharSequence> list) {
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).map((item) -> {
                CTrie.CTrieNode<Integer> current = this.root;
                for (final Integer charCode : item.codePoints().toArray()) {
                    CTrie.CTrieNode<Integer> child = current.getChild(charCode);
                    if (Objects.isNull(child)) {
                        child = new CTrieNode<>(charCode);
                        current.addChild(child);
                        this.size++;
                    }
                    current = child;
                }
                return current;
            }).forEach((current) -> {
                current.setTerminated(Boolean.TRUE);
            });
        }
    }

    @Override
    public boolean contains(final CharSequence value, final CTrie.CTrieNode<Integer> node,  boolean isExact) {
        CTrie.CTrieNode<Integer> current = node;
        for (final Integer charCode : value.codePoints().toArray()) {
            current = current.getChild(charCode);
            if (Objects.isNull(current)) {
                return false;
            }
        }
        return current.isTerminated() || !isExact;
    }

    @Override
    protected CTrie.CTrieNode<Integer> createTrieNode(final Optional<? extends Integer> value) {
        if (value.isPresent()) {
            return new CTrie.CTrieNode<>(value.get());
        }
        return new CTrie.CTrieNode<>();
    }
}
