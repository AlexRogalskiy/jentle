package com.wildbeeslabs.jentle.collections.tree.common;

public class BinaryTree<E extends Comparable<E>> {

    BinaryNode<E> root = null;

    public int size() {
        if (root == null) {
            return 0;
        }

        return root.size();
    }

    public int height() {
        if (root == null) {
            return 0;
        }

        return height(root);
    }

    int height(BinaryNode<E> n) {
        if (n == null) {
            return 0;
        }

        return 1 + Math.max(height(n.left), height(n.right));
    }

    public void add(E k) {
        if (root == null) {
            root = new BinaryNode<E>(k);
            return;
        }

        root = root.add(root, k);
    }

    public boolean contains(E k) {
        return contains(root, k);
    }

    boolean contains(BinaryNode<E> parent, E k) {
        if (parent == null) {
            return false;
        }

        int rc = k.compareTo(parent.key);
        if (rc == 0) {
            return true;
        } else if (rc < 0) {
            return contains(parent.left, k);
        } else {
            return contains(parent.right, k);
        }
    }

    public void remove(E k) {
        if (root == null) {
            return;
        }

        root = remove(root, k);
    }

    BinaryNode<E> remove(BinaryNode<E> parent, E k) {
        if (parent == null) {
            return null;
        }
        int rc = k.compareTo(parent.key);
        if (rc == 0) {
            return parent.updateNodes();
        } else if (rc < 0) {
            parent.left = remove(parent.left, k);
        } else {
            parent.right = remove(parent.right, k);
        }

        return parent;
    }
}
