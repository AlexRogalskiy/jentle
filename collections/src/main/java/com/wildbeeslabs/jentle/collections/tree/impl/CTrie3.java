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

import java.util.ArrayList;
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
 * Custom trie3 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CTrie3 extends ACTrie<Character, CTrie3.CTrieNode> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CTrieNode extends ACTrieNode<Character, CTrieNode> {

        public static final char DEFAULT_NODE_VALUE_TERMINATOR = '\0';
        protected List<Integer> indexes;

        public CTrieNode() {
            this(null);
        }

        public CTrieNode(final Character data) {
            super(data);
            this.indexes = new ArrayList<>();
        }

        public void insertString(final CharSequence value, int index) {
            this.indexes.add(index);
            if (Objects.nonNull(value) && value.length() > 0) {
                char val = value.charAt(0);
                this.setData(val);
                @SuppressWarnings("UnusedAssignment")
                CTrieNode child = null;
                if (this.hasChild(val)) {
                    child = this.getChild(val);
                } else {
                    child = new CTrieNode();
                    this.childs.put(val, child);
                }
                child.insertString(value.toString().substring(1), index + 1);
            } else {
                this.childs.put(CTrieNode.DEFAULT_NODE_VALUE_TERMINATOR, null);
            }
        }

        public List<Integer> search(final CharSequence value) {
            if (Objects.isNull(value) || value.length() == 0) {
                return this.indexes;
            } else {
                char first = value.charAt(0);
                if (this.hasChild(first)) {
                    return this.getChild(first).search(value.toString().substring(1));
                }
            }
            return null;
        }

        @Override
        public boolean isTerminated() {
            return this.hasChild(CTrieNode.DEFAULT_NODE_VALUE_TERMINATOR);
        }
    }

    public CTrie3() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie3(final Comparator<? super Character> cmp) {
        this(null, cmp);
    }

    public CTrie3(final List<? extends CharSequence> list) {
        this(list, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie3(final CharSequence[] array) {
        this(Arrays.asList(array), CUtils.DEFAULT_SORT_COMPARATOR);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public CTrie3(final List<? extends CharSequence> list, final Comparator<? super Character> cmp) {
        super(cmp);
        this.init(list);
    }

    protected void init(final List<? extends CharSequence> list) {
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).map((item) -> {
                CTrie3.CTrieNode current = this.root;
                for (int i = 0; i < item.length(); i++) {
                    CTrie3.CTrieNode child = current.getChild(item.charAt(i));
                    if (Objects.isNull(child)) {
                        child = new CTrie3.CTrieNode(item.charAt(i));
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

    public List<Integer> search(final CharSequence value) {
        return this.root.search(value);
    }

    public void setRoot(final CharSequence value) {
        this.insert(value, 0);
    }

    public void insert(final CharSequence value, int location) {
        this.root.insertString(value, location);
    }

    @Override
    protected boolean contains(final CharSequence value, final CTrie3.CTrieNode node, boolean isExact) {
        CTrie3.CTrieNode current = node;
        for (int i = 0; i < value.length(); i++) {
            current = current.getChild(value.charAt(i));
            if (Objects.isNull(current)) {
                return false;
            }
        }
        return current.isTerminated() || !isExact;
    }

    @Override
    protected CTrie3.CTrieNode createTrieNode(final Optional<? extends Character> value) {
        if (value.isPresent()) {
            return new CTrie3.CTrieNode(value.get());
        }
        return new CTrie3.CTrieNode();
    }
}
