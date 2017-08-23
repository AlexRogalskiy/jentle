package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.ITree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public class CTrie implements ITree<String> {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CTrie.class);

    protected static class CTrieNode<T> {

        protected final Map<T, CTrieNode<T>> children;
        protected boolean isTerminated;
        protected T data;

        public CTrieNode() {
            this(null);
        }

        public CTrieNode(final T data) {
            this.children = new HashMap<>();
            this.isTerminated = false;
            this.data = data;
        }

        public CTrieNode<T> getChild(final T data) {
            return this.children.get(data);
        }

        @Override
        public String toString() {
            return String.format("CTrieNode {data: %s, isTerminated: %s, children: %s}", this.data, this.isTerminated, this.children);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CTrieNode<T> other = (CTrieNode<T>) obj;
            if (this.isTerminated != other.isTerminated) {
                return false;
            }
            if (!Objects.equals(this.children, other.children)) {
                return false;
            }
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + Objects.hashCode(this.children);
            hash = 71 * hash + (this.isTerminated ? 1 : 0);
            hash = 71 * hash + Objects.hashCode(this.data);
            return hash;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CTrie other = (CTrie) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.root, other.root)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.root);
        hash = 23 * hash + this.size;
        return hash;
    }
}
