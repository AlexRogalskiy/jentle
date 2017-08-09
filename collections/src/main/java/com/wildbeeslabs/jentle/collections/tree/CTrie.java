package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;

import java.util.Comparator;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom trie implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CTrie<T> {
    /**
     * Default Logger instance
     */
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected static class CTrieNode<T> {

        protected T data;
        protected CTrieNode<T> left;
        protected CTrieNode<T> right;

        public CTrieNode(final T data) {
            this(data, null, null);
        }

        public CTrieNode(final T data, final CTrieNode<T> left, final CTrieNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.format("CTrieNode {data: %s, left: %s, right: %s}", this.data, this.left, this.right);
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
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            if (!Objects.equals(this.left, other.left)) {
                return false;
            }
            if (!Objects.equals(this.right, other.right)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.data);
            hash = 47 * hash + Objects.hashCode(this.left);
            hash = 47 * hash + Objects.hashCode(this.right);
            return hash;
        }
    }

    private CTrieNode<T> root;
    private int size;
    private final Comparator<? super T> cmp;

    public CTrie() {
        this(CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CTrie(final Comparator<? super T> cmp) {
        this.root = null;
        this.size = 0;
        this.cmp = cmp;
    }
}
