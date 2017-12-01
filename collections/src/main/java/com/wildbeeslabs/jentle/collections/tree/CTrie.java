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

package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.ITree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom Trie implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CTrie implements ITree<String> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    protected static class CTrieNode<T> {

        protected final Map<T, CTrieNode<T>> children;
        protected boolean isTerminated;
        protected T data;

        public CTrieNode() {
            this(null);
        }

        public CTrieNode(final T data) {
            this.children = new LinkedHashMap<>();
            this.isTerminated = false;
            this.data = data;
        }

        public CTrieNode<T> getChild(final T data) {
            return this.children.get(data);
        }
    }

    protected CTrieNode root;
    protected int size;
    protected final Comparator<?> cmp;

    public CTrie() {
        this(null, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie(final List<String> list) {
        this(list, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie(final String[] array) {
        this(Arrays.asList(array), CSort.DEFAULT_SORT_COMPARATOR);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public CTrie(final List<String> list, final Comparator<?> cmp) {
        this.root = new CTrieNode<>();
        this.size = 0;
        this.cmp = cmp;
        this.init(list);
    }

    protected void init(final List<String> list) {
        if (Objects.nonNull(list)) {
            list.stream().filter((item) -> (Objects.nonNull(item) && !item.isEmpty())).map((String item) -> {
                CTrieNode current = CTrie.this.root;
                for (int i = 0; i < item.length(); i++) {
                    Character letter = item.charAt(i);
                    CTrieNode child = current.getChild(letter.toString());
                    if (Objects.isNull(child)) {
                        child = new CTrieNode<>(letter.toString());
                        current.children.put(letter.toString(), child);
                        CTrie.this.size++;
                    }
                    current = child;
                }
                return current;
            }).forEach((current) -> {
                current.isTerminated = true;
            });
        }
    }

    public boolean contains(final String prefix, boolean isExact) {
        CTrieNode current = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            Character letter = prefix.charAt(i);
            current = current.getChild(letter.toString());
            if (Objects.isNull(current)) {
                return false;
            }
        }
        return !isExact || current.isTerminated;
    }

    public boolean contains(final String prefix) {
        return this.contains(prefix, false);
    }

    public CTrieNode<?> getRoot() {
        return this.root;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }
}
