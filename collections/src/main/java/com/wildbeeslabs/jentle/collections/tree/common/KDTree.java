//package com.wildbeeslabs.jentle.collections.tree.common;
//
//import java.awt.*;
//
//public class KDTree {
//    KDNode root;
//    int deletedCount;
//    int totalCount;
//    float loadFactor;
//    static float DEFAULT_LOAD_FACTOR = 0.5f;
//
//    public KDTree() {
//        this(DEFAULT_LOAD_FACTOR);
//    }
//
//    public KDTree(float factor) {
//        root = null;
//        loadFactor = factor;
//    }
//
//    public boolean void add(Point value) {
//        if (root == null) {
//            root = new KDNode(value, KDNode.VERTICAL);
//            totalCount = 1;
//            return true;
//        } else {
//            if ( root.add(value);){
//                totalCount++;
//                return true;
//            }
//            return false;
//        }
//    }
//
//    void recreate() {
//        KDNode oldRoot = root;
//        root = null;
//
//        int remaining = totalCount - deletedCount;
//        totalCount = deletedCount = 0;
//        if (remaining == 0) {
//            return;
//        }
//
//        fill(oldRoot);
//    }
//
//    void fill(KDNode n) {
//        if (n == null) {
//            return;
//        }
//
//        if (!n.deleted) {
//            add(n.point);
//        }
//
//        fill(n.below);
//        fill(n.above);
//    }
//
//    public boolean remove(Point p) {
//        KDNode exist = find(p);
//        if (exist != null && !exist.deleted) {
//            exist.deleted = true;
//            deletedCount++;
//
//            if (deletedCount * 1.0 / totalCount >= loadFactor) {
//                recreate();
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public KDNode find(Point p) {
//        return find(root, p);
//    }
//
//    KDNode find(KDNode node, Point p) {
//        if (node == null) {
//            return null;
//        }
//        if (node.point.distance(p) < 5) {
//            return node;
//        }
//
//        if (node.isBelow(p)) {
//            return find(node.below, p);
//        } else {
//            return find(node.above, p);
//        }
//    }
//}
