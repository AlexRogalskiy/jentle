package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.collections.interfaces.ITree;
import com.wildbeeslabs.jentle.collections.interfaces.Visitor;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CTree<T> implements ITree<T> {

    /**
     * Default Logger instance
     */
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected static class CTreeNode<T> {

        private T data;
        private CTreeNode<T> left;
        private CTreeNode<T> right;

        public CTreeNode(final T data) {
            this(data, null, null);
        }

        public CTreeNode(final T data, final CTreeNode<T> left, final CTreeNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.format("CTreeNode {data: %s, left: %s, right: %s}", this.data, this.left, this.right);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CTreeNode<T> other = (CTreeNode<T>) obj;
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

    private CTreeNode<T> root;

    public CTree() {
        this.root = null;
    }

    public int height() {
        return this.height(this.root);
    }

    private int height(CTreeNode<T> node) {
        if (null == node) {
            return 0;
        } else {
            return Math.max(this.height(node.left), this.height(node.right)) + 1;
        }
    }

    public int nodesOnLevel(int level) {
        return this.nodesOnLevel(this.root, level);
    }

    private int nodesOnLevel(CTreeNode<T> node, int level) {
        if (null == node) {
            return 0;
        }
        if (1 == level) {
            return 1;
        }
        return this.nodesOnLevel(node.left, level - 1) + this.nodesOnLevel(node.right, level - 1);
    }

    //@Override
    public int size() {
        //return this.size;
        return 0;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    public void inOrderTraversal(final CTreeNode<T> node, final Visitor<T> visitor) {
        if (null != node) {
            inOrderTraversal(node.left, visitor);
            visitor.visit(node.data);
            inOrderTraversal(node.right, visitor);
        }
    }

    public void preOrderTraversal(final CTreeNode<T> node, final Visitor<T> visitor) {
        if (null != node) {
            visitor.visit(node.data);
            preOrderTraversal(node.left, visitor);
            preOrderTraversal(node.right, visitor);
        }
    }

    public void postOrderTraversal(final CTreeNode<T> node, final Visitor<T> visitor) {
        if (null != node) {
            postOrderTraversal(node.left, visitor);
            postOrderTraversal(node.right, visitor);
            visitor.visit(node.data);
        }
    }

}
