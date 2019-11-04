//package com.wildbeeslabs.jentle.collections.tree.common;
//
//public class BinaryNode<E extends Comparable<E>> {
//    final E key;
//    BinaryNode<E> left;
//    BinaryNode<E> right;
//
//    public BinaryNode(E k) {
//        this.key = k;
//    }
//
//    public int size() {
//        return 1 + size(left) + size(right);
//    }
//
//    int size(BinaryNode<E> n) {
//        if (n == null) {
//            return 0;
//        }
//        return n.size();
//    }
//
//    void add(E k) {
//        int rc = k.compareTo(key);
//        if (rc <= 0) {
//            left = add(left, k);
//        } else {
//            right = add(right, k);
//        }
//    }
//
//    BinaryNode<E> add(BinaryNode<E> parent, E k) {
//        if (parent == null) {
//            return new BinaryNode<E>(k);
//        }
//
//        parent.add(k);
//        return parent;
//    }
//
//    public BinaryNode<E> updateNodes() {
//        if (left == null && right == null) {
//            return null;
//        }
//        if (left == null) {
//            return right;
//        }
//        if (right == null) {
//            return left;
//        }
//
//        BinaryNode<E> child = left;
//        BinaryNode<E> grandChild = child.right;
//        if (grandChild == null) {
//            left = child.left;
//            key = child.key;
//        } else {
//            while (grandChild.right != null) {
//                child = grandChild;
//                grandChild = grandChild.right;
//            }
//            key = grandChild.key;
//            child.right = grandChild.left;
//        }
//
//        return this;
//    }
//}
