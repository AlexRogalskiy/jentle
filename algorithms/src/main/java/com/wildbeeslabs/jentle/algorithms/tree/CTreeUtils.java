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
package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.algorithms.list.CListUtils;
import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended2;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * Custom tree algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CTreeUtils {

    private CTreeUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<LinkedList<U>> createLevelNodeList(final U root) {
        final List<LinkedList<U>> levelList = new ArrayList<>();
        CTreeUtils.createLevelNodeList(root, levelList, 0);
        return levelList;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void createLevelNodeList(final U root, final List<LinkedList<U>> levelList, int level) {
        if (Objects.isNull(root)) {
            return;
        }
        LinkedList<U> list = new LinkedList<>();
        if (levelList.size() == level) {
            levelList.add(list);
        } else {
            list = levelList.get(level);
        }
        list.add(root);
        CTreeUtils.createLevelNodeList(root.getLeft(), levelList, level + 1);
        CTreeUtils.createLevelNodeList(root.getRight(), levelList, level + 1);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<LinkedList<U>> createLevelNodeList2(final U root) {
        final List<LinkedList<U>> levelList = new ArrayList<>();
        LinkedList<U> current = new LinkedList<>();
        if (Objects.nonNull(root)) {
            current.add(root);
        }
        while (current.size() > 0) {
            levelList.add(current);
            final LinkedList<U> parents = current;
            current = new LinkedList<>();
            for (final ACBaseTreeNode<T, U> parent : parents) {
                if (Objects.nonNull(parent.getLeft())) {
                    current.add(parent.getLeft());
                }
                if (Objects.nonNull(parent.getRight())) {
                    current.add(parent.getRight());
                }
            }
        }
        return levelList;
    }

    //breadth first search recursive algorithm
    public static <T, U extends ACBaseTreeNode<T, U>> boolean isLeavesAtSameLevel(final U root) {
        if (Objects.isNull(root)) {
            return false;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int level = 0;
        boolean bLeafFound = false;
        int leafLevel = -1;
        while (!queue.isEmpty()) {
            final U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                level++;
            } else {
                if (Objects.nonNull(node.getLeft()) && Objects.nonNull(node.getRight())) {
                    if (!bLeafFound) {
                        bLeafFound = true;
                        leafLevel = level;
                    } else {
                        if (leafLevel != level) {
                            return false;
                        }
                    }
                }
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        return true;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U ceilInBST(final U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return root;
        }
        if (Objects.compare(root.getData(), data, cmp) > 0) {
            final U left = CTreeUtils.ceilInBST(root.getLeft(), data, cmp);
            if (Objects.isNull(left)) {
                return root;
            }
            return left;
        }
        return CTreeUtils.ceilInBST(root.getRight(), data, cmp);
    }

    //depth first search recursive algorithm
    public static <T, U extends ACBaseTreeNode<T, U>> U floor(final U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return root;
        }
        if (Objects.compare(root.getData(), data, cmp) > 0) {
            return floor(root.getLeft(), data, cmp);
        }
        final U right = floor(root.getRight(), data, cmp);
        if (Objects.isNull(right)) {
            return root;
        }
        return right;
    }

    public static <T, U extends ACTreeNodeExtended2<T, U>> void nextSiblingPointer(final U node) {
        if (Objects.isNull(node)) {
            return;
        }
        if (Objects.nonNull(node.getLeft())) {
            node.getLeft().setNextSibling(node.getRight());
        }
        if (Objects.nonNull(node.getRight())) {
            if (Objects.nonNull(node.getNextSibling())) {
                node.getRight().setNextSibling(node.getNextSibling().getLeft());
            } else {
                node.getRight().setNextSibling(null);
            }
        }
        CTreeUtils.nextSiblingPointer(node.getLeft());
        CTreeUtils.nextSiblingPointer(node.getRight());
    }

    public static <T, U extends ACTreeNodeExtended2<T, U>> List<T> nextSiblingTraversal(U root) {
        final List<T> result = new ArrayList<>();
        U node = null;
        int level = 0;
        while (Objects.nonNull(root)) {
            node = root;
            level++;
            while (Objects.nonNull(node)) {
                result.add(node.getData());
                node = node.getNextSibling();
            }
            root = root.getLeft();
        }
        return result;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U kSmallestElement(final U root, int k) {
        return CTreeUtils.kSmallestElement(root, k, new int[]{0});
    }

    private static <T, U extends ACBaseTreeNode<T, U>> U kSmallestElement(final U root, int k, int[] iElement) {
        if (Objects.isNull(root)) {
            return null;
        }
        U left = CTreeUtils.kSmallestElement(root.getLeft(), k, iElement);
        if (Objects.nonNull(left)) {
            return left;
        }
        iElement[0]++;
        if (iElement[0] == k) {
            return root;
        }
        return CTreeUtils.kSmallestElement(root.getRight(), k, iElement);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean quasiIsomorphicBinaryTree(final U node1, final U node2) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (CTreeUtils.quasiIsomorphicBinaryTree(node1.getLeft(), node2.getLeft()) && CTreeUtils.quasiIsomorphicBinaryTree(node1.getRight(), node2.getRight())) {
            return true;
        }
        if (CTreeUtils.quasiIsomorphicBinaryTree(node1.getLeft(), node2.getRight()) && CTreeUtils.quasiIsomorphicBinaryTree(node1.getRight(), node2.getLeft())) {
            return true;
        }
        return false;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean isIsomorphicBinaryTree(final U node1, final U node2) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (!CTreeUtils.isIsomorphicBinaryTree(node1.getLeft(), node2.getLeft())) {
            return false;
        }

        if (!CTreeUtils.isIsomorphicBinaryTree(node1.getRight(), node2.getRight())) {
            return false;
        }
        return true;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> Integer levelOfNodeInBTree(final U root, final U inputNode, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int level = 0;
        boolean found = false;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                level++;
            } else {
                if (Objects.compare(inputNode.getData(), node.getData(), cmp) == 0) {
                    found = true;
                    break;
                }
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        if (found) {
            return level;
        }
        return null;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int[] maxWidthOfBTree(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int maxWidth = 0;
        int level = 0;
        int localLevel = 0;
        int localWidth = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                if (localWidth > maxWidth) {
                    maxWidth = localWidth;
                    level = localLevel;
                }
                localWidth = 0;
                localLevel++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
                localWidth++;
            }
        }
        return new int[]{maxWidth, level};
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void getNodesKDistFromLeaf(final U root, int distance, int index, final List<T> data, final List<Boolean> isVisitedBefore) {
        if (Objects.isNull(root)) {
            return;
        }
        data.set(index, root.getData());
        isVisitedBefore.set(index, Boolean.FALSE);
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight()) && (index - distance >= 0) && !isVisitedBefore.get(index - distance)) {
            isVisitedBefore.set(index - distance, Boolean.TRUE);
        } else if (distance > 0) {
            CTreeUtils.getNodesKDistFromLeaf(root.getLeft(), distance, index + 1, data, isVisitedBefore);
            CTreeUtils.getNodesKDistFromLeaf(root.getRight(), distance, index + 1, data, isVisitedBefore);
        }
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> getNodesKDistFromLeaf(final U root, int distance) {
        final List<T> result = new ArrayList<>();
        final List<Boolean> visit = new ArrayList<>();
        CTreeUtils.getNodesKDistFromLeaf(root, distance, 0, result, visit);
        return result;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void getNodesKDistFromRoot(final U root, int distance, final List<T> data) {
        if (Objects.isNull(root)) {
            return;
        }
        if (distance == 0) {
            data.add(root.getData());
        } else if (distance > 0) {
            CTreeUtils.getNodesKDistFromRoot(root.getLeft(), distance - 1, data);
            CTreeUtils.getNodesKDistFromRoot(root.getRight(), distance - 1, data);
        }
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> getNodesKDistFromRoot(final U root, int distance) {
        final List<T> result = new ArrayList<>();
        CTreeUtils.getNodesKDistFromRoot(root, distance, result);
        return result;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void getNodesByRange(final U root, final T data1, final T data2, final List<T> result, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return;
        }
        if (Objects.compare(root.getData(), data1, cmp) >= 0 && Objects.compare(root.getData(), data2, cmp) <= 0) {
            result.add(root.getData());
        }
        if (Objects.compare(root.getData(), data1, cmp) > 0) {
            CTreeUtils.getNodesByRange(root.getLeft(), data1, data2, result, cmp);
        }
        if (Objects.compare(root.getData(), data2, cmp) < 0) {
            CTreeUtils.getNodesByRange(root.getRight(), data1, data2, result, cmp);
        }
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> getNodesByRange(final U root, final T data1, final T data2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        CTreeUtils.getNodesByRange(root, data1, data2, result, cmp);
        return result;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> U min(final U node) {
        if (Objects.isNull(node.getLeft())) {
            return node;
        }
        return CTreeUtils.min(node.getLeft());
    }

    private static <T, U extends ACBaseTreeNode<T, U>> U max(final U root) {
        if (Objects.isNull(root.getRight())) {
            return root;
        }
        return CTreeUtils.max(root.getRight());
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U deleteNodeInBST(U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(data, root.getData(), cmp) < 0) {
            root.setLeft(deleteNodeInBST(root.getLeft(), data, cmp));
        } else if (Objects.compare(data, root.getData(), cmp) > 0) {
            root.setRight(deleteNodeInBST(root.getRight(), data, cmp));
        } else {
            if (Objects.nonNull(root.getLeft()) && Objects.nonNull(root.getRight())) {
                final T minInRightSubTree = min(root.getRight()).getData();
                root.setData(minInRightSubTree);
                root.setRight(deleteNodeInBST(root.getRight(), minInRightSubTree, cmp));
            } else {
                if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
                    root = null;
                } else {
                    U deleteNode = root;
                    root = (Objects.nonNull(root.getLeft())) ? root.getLeft() : root.getRight();
                    deleteNode = null;
                }
            }
        }
        return root;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U predecessor(U root, final U node, final Comparator<? super T> cmp) {
        if (Objects.nonNull(node.getLeft())) {
            return max(node.getLeft());
        }
        U predecessor = null;
        while (Objects.nonNull(root)) {
            if (Objects.compare(node.getData(), root.getData(), cmp) == 0) {
                break;
            } else if (Objects.compare(node.getData(), root.getData(), cmp) < 0) {
                root = root.getLeft();
            } else if (Objects.compare(node.getData(), root.getData(), cmp) > 0) {
                predecessor = root;
                root = root.getRight();
            }
        }
        return predecessor;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U successor(U root, final U node, final Comparator<? super T> cmp) {
        if (Objects.nonNull(node.getRight())) {
            return min(node.getRight());
        }
        U successor = null;
        while (Objects.nonNull(root)) {
            if (Objects.compare(node.getData(), root.getData(), cmp) == 0) {
                break;
            } else if (Objects.compare(node.getData(), root.getData(), cmp) < 0) {
                successor = root;
                root = root.getLeft();
            } else if (Objects.compare(node.getData(), root.getData(), cmp) > 0) {
                root = root.getRight();
            }
        }
        return successor;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U leastCommonAncestor(final U root, final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), node1.getData(), cmp) > 0 && Objects.compare(root.getData(), node2.getData(), cmp) > 0) {
            return CTreeUtils.leastCommonAncestor(root.getLeft(), node1, node2, cmp);
        }
        if (Objects.compare(root.getData(), node1.getData(), cmp) < 0 && Objects.compare(root.getData(), node2.getData(), cmp) < 0) {
            return CTreeUtils.leastCommonAncestor(root.getRight(), node1, node2, cmp);
        }
        return root;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean isBST(final U node, final T min, final T max, final Comparator<? super T> cmp) {
        if (Objects.isNull(node)) {
            return true;
        }
        if (Objects.compare(node.getData(), min, cmp) < 0 || Objects.compare(node.getData(), max, cmp) > 0) {
            return false;
        }
        boolean isLeft = CTreeUtils.isBST(node.getLeft(), min, node.getData(), cmp);
        if (!isLeft) {
            return isLeft;
        }
        boolean isRight = CTreeUtils.isBST(node.getRight(), node.getData(), max, cmp);
        if (!isRight) {
            return isRight;
        }
        return true;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U findNodeInBST(final U node, final T value, final Comparator<? super T> cmp) {
        if (Objects.isNull(node)) {
            return null;
        }
        if (Objects.compare(node.getData(), value, cmp) == 0) {
            return node;
        } else if (Objects.compare(value, node.getData(), cmp) < 0) {
            return CTreeUtils.findNodeInBST(node.getLeft(), value, cmp);
        }
        return CTreeUtils.findNodeInBST(node.getRight(), value, cmp);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean isIdentical(final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (Objects.compare(node1.getData(), node2.getData(), cmp) != 0) {
            return false;
        }
        if (!CTreeUtils.isIdentical(node1.getLeft(), node2.getLeft(), cmp)) {
            return false;
        }
        if (!CTreeUtils.isIdentical(node1.getRight(), node2.getRight(), cmp)) {
            return false;
        }
        return true;
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> List<T> sumInRoot2LeafPath(final U root, int sum) {
        final List<T> path = new ArrayList<>();
        boolean sumExist = CTreeUtils.sumInRoot2LeafPath(root, path, sum);
        if (sumExist) {
            return path;
        }
        return null;
    }

    private static <T extends Number, U extends ACBaseTreeNode<T, U>> boolean sumInRoot2LeafPath(final U root, final List<T> path, int sum) {
        if (Objects.isNull(root)) {
            return false;
        }
        path.add(root.getData());
        if (Objects.nonNull(root.getData())) {
            sum -= root.getData().intValue();
        }
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            if (sum == 0) {
                return true;
            }
            return false;
        }
        return CTreeUtils.sumInRoot2LeafPath(root.getLeft(), path, sum) || CTreeUtils.sumInRoot2LeafPath(root.getRight(), path, sum);
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> List<T> maxSumPath(final U root) {
        final List<T> path = new ArrayList<>();
        CTreeUtils.maxSumPathRoot2Leaf(root, path, 0, Integer.MIN_VALUE);
        return path;
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> int maxSumPathRoot2Leaf(final U root, final List<T> path, int sum, int maxSum) {
        if (Objects.isNull(root)) {
            return Integer.MIN_VALUE;
        }
        path.add(root.getData());
        if (Objects.nonNull(root.getData())) {
            sum += root.getData().intValue();
        }
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            if (sum > maxSum) {
                maxSum = sum;
            }
            return maxSum;
        }
        return Math.max(CTreeUtils.maxSumPathRoot2Leaf(root.getLeft(), path, sum, maxSum), CTreeUtils.maxSumPathRoot2Leaf(root.getRight(), path, sum, maxSum));
    }

    private static <T extends Number, U extends ACBaseTreeNode<T, U>> void verticalOrder(final U root, int distance, final Map<Integer, Integer> map) {
        if (Objects.isNull(root)) {
            return;
        }
        int existingValue = 0;
        if (map.containsKey(distance)) {
            existingValue = map.get(distance);
        }
        map.put(distance, root.getData().intValue() + existingValue);
        CTreeUtils.verticalOrder(root.getLeft(), distance - 1, map);
        CTreeUtils.verticalOrder(root.getRight(), distance + 1, map);
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> Map<Integer, Integer> verticalOrderSumOfBTree(final U root) {
        final Map<Integer, Integer> map = new HashMap<>();
        CTreeUtils.verticalOrder(root, 0, map);
        return map;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void verticalOrder2(final U root, int distance, final Map<Integer, List<T>> map) {
        if (Objects.isNull(root)) {
            return;
        }
        List<T> list = null;
        if (map.containsKey(distance)) {
            list = map.get(distance);
        } else {
            list = new ArrayList<>();
        }
        list.add(root.getData());
        map.put(distance, list);
        CTreeUtils.verticalOrder2(root.getLeft(), distance - 1, map);
        CTreeUtils.verticalOrder2(root.getRight(), distance + 1, map);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> Map<Integer, List<T>> verticalOrderOfBTree(final U root) {
        final Map<Integer, List<T>> map = new HashMap<>();
        CTreeUtils.verticalOrder2(root, 0, map);
        return map;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> int diameterOfBTree(final U root, int[] diameter) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int left = CTreeUtils.diameterOfBTree(root.getLeft(), diameter);
        int right = CTreeUtils.diameterOfBTree(root.getRight(), diameter);
        diameter[0] = Math.max(diameter[0], left + right + 1);
        return Math.max(left, right) + 1;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int getDiameter(final U root) {
        int[] diameter = new int[]{0};
        CTreeUtils.diameterOfBTree(root, diameter);
        return diameter[0];
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int nodesInBTree(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int nLeftSubtree = CTreeUtils.nodesInBTree(root.getLeft());
        int nRightSubtree = CTreeUtils.nodesInBTree(root.getRight());
        return nLeftSubtree + nRightSubtree + 1;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> T minMaxElement(final U root, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        T data = root.getData();
        final T left = CTreeUtils.minMaxElement(root.getLeft(), cmp);
        final T right = CTreeUtils.minMaxElement(root.getRight(), cmp);
        return Collections.max(Arrays.asList(left, right, data), cmp);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int heightOfTree(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int left = CTreeUtils.heightOfTree(root.getLeft());
        int right = CTreeUtils.heightOfTree(root.getRight());
        return Math.max(left, right) + 1;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean isBalanced(final U root) {
        if (Objects.isNull(root)) {
            return true;
        }
        int heightDiff = CTreeUtils.heightOfTree(root.getLeft()) - CTreeUtils.heightOfTree(root.getRight());
        if (Math.abs(heightDiff) > 1) {
            return false;
        } else {
            return CTreeUtils.isBalanced(root.getLeft()) && CTreeUtils.isBalanced(root.getRight());
        }
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean isMirrorTree(final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (Objects.compare(node1.getData(), node2.getData(), cmp) != 0) {
            return false;
        }
        if (!CTreeUtils.isMirrorTree(node1.getLeft(), node2.getRight(), cmp)) {
            return false;
        }

        if (!CTreeUtils.isMirrorTree(node1.getRight(), node2.getLeft(), cmp)) {
            return false;
        }
        return true;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> spiralTraversal(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Stack<U> stackOne = new Stack<>();
        final Stack<U> stackTwo = new Stack<>();
        stackOne.push(root);
        while (!stackOne.isEmpty() || !stackTwo.isEmpty()) {
            while (!stackOne.isEmpty()) {
                U node = stackOne.pop();
                result.add(node.getData());
                if (Objects.nonNull(node.getRight())) {
                    stackTwo.push(node.getRight());
                }
                if (Objects.nonNull(node.getLeft())) {
                    stackTwo.push(node.getLeft());
                }
            }
            while (!stackTwo.isEmpty()) {
                U node = stackTwo.pop();
                result.add(node.getData());
                if (Objects.nonNull(node.getLeft())) {
                    stackOne.push(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    stackOne.push(node.getRight());
                }
            }
        }
        return result;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> reverseLevelOrder(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        final Stack<U> stack = new Stack<>();
        while (!queue.isEmpty()) {
            U node = queue.poll();
            stack.push(node);
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop().getData());
        }
        return result;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> getAncestorsOfNode(final U root, final T data, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        CTreeUtils.getAncestorsOfNode(root, data, result, cmp);
        return result;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> boolean getAncestorsOfNode(final U root, final T data, final List<T> result, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return true;
        }
        boolean bFoundOnLeft = CTreeUtils.getAncestorsOfNode(root.getLeft(), data, result, cmp);
        if (bFoundOnLeft) {
            result.add(root.getData());
            return bFoundOnLeft;
        }
        boolean bFoundOnRight = CTreeUtils.getAncestorsOfNode(root.getRight(), data, result, cmp);
        if (bFoundOnRight) {
            result.add(root.getData());
            return bFoundOnRight;
        }
        return false;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> List<T> root2LeafPath(final U root, int[] path) {
        final List<T> result = new ArrayList<>();
        CTreeUtils.root2LeafPath(root, result);
        return result;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void root2LeafPath(final U root, final List<T> path) {
        if (Objects.isNull(root)) {
            return;
        }
        path.add(root.getData());
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            return;
        }
        CTreeUtils.root2LeafPath(root.getLeft(), path);
        CTreeUtils.root2LeafPath(root.getRight(), path);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> void mirrorTree(final U root) {
        if (Objects.isNull(root)) {
            return;
        }
        CTreeUtils.mirrorTree(root.getLeft());
        CTreeUtils.mirrorTree(root.getRight());
        U swapNode = root.getLeft();
        root.setLeft(root.getRight());
        root.setRight(swapNode);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int countNodes(final U root) {
        return CTreeUtils.countNodes(root, (node) -> Boolean.TRUE);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int countFullNodes(final U root) {
        return CTreeUtils.countNodes(root, (node) -> Objects.nonNull(node.getLeft()) && Objects.nonNull(node.getRight()));
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int countNonLeafNodes(final U root) {
        return CTreeUtils.countNodes(root, (node) -> Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int countNonLeafOneChild(final U root) {
        final Function<U, Boolean> function = (node) -> (Objects.nonNull(node.getLeft()) && Objects.isNull(node.getRight())) || (Objects.isNull(node.getLeft()) && Objects.nonNull(node.getRight()));
        return CTreeUtils.countNodes(root, function);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int countLeaves(final U root) {
        return CTreeUtils.countNodes(root, (node) -> Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    private static <T, U extends ACBaseTreeNode<T, U>> int countNodes(final U root, final Function<U, Boolean> function) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int nNodes = 0;
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (function.apply(node)) {
                nNodes++;
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return nNodes;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> T minMaxElement2(final U root, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        T max = root.getData();
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.compare(max, node.getData(), cmp) < 0) {
                max = node.getData();
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return max;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U lowestCommonAncestor(final U root, final U node1, final U node2) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (root == node1 || root == node2) {
            return root;
        }
        U left = CTreeUtils.lowestCommonAncestor(root.getLeft(), node1, node2);
        U right = CTreeUtils.lowestCommonAncestor(root.getRight(), node1, node2);
        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            return root;
        }
        if (Objects.nonNull(left)) {
            return left;
        }
        return right;
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> int[] maxSumLevel(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int maxSum = 0;
        int level = 0;
        int localLevel = 0;
        int localSum = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                if (localSum > maxSum) {
                    maxSum = localSum;
                    level = localLevel;
                }
                localSum = 0;
                localLevel++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
                if (Objects.nonNull(node.getData())) {
                    localSum += node.getData().intValue();
                }
            }
        }
        return new int[]{maxSum, level};
    }

    public static <T extends Number, U extends ACBaseTreeNode<T, U>> Integer sumOfNodes(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        int totalSum = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.nonNull(node.getData())) {
                totalSum += node.getData().intValue();
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return totalSum;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> int heightOfTree2(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int height = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                height++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        return height;
    }

    //level order traversal
    public static <T, U extends ACBaseTreeNode<T, U>> List<T> traverseBinaryTree(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            result.add(node.getData());
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return result;
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U deleteTree(U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        root.setLeft(CTreeUtils.deleteTree(root.getLeft()));
        root.setRight(CTreeUtils.deleteTree(root.getRight()));
        root = null;
        return root;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> U commonAncestor(final U firstNode, final U secondNode) {
        int delta = depth(firstNode) - depth(secondNode);
        U first = delta > 0 ? secondNode : firstNode;
        U second = delta > 0 ? firstNode : secondNode;
        second = CTreeUtils.goUpBy(second, Math.abs(delta));
        while (first != second && Objects.nonNull(first) && Objects.nonNull(second)) {
            first = first.getParent();
            second = second.getParent();
        }
        return Objects.isNull(first) || Objects.isNull(second) ? null : first;
    }

    private static <T, U extends ACTreeNodeExtended<T, U>> U goUpBy(U node, int delta) {
        while (delta > 0 && Objects.nonNull(node)) {
            node = node.getParent();
            delta--;
        }
        return node;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> int depth(U node) {
        int depth = 0;
        while (Objects.nonNull(node)) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> U commonAncestor2(final U root, final U firstNode, final U secondNode) {
        if (!covers(root, firstNode) || !covers(root, secondNode)) {
            return null;
        } else if (CTreeUtils.covers(firstNode, secondNode)) {
            return firstNode;
        } else if (CTreeUtils.covers(secondNode, firstNode)) {
            return secondNode;
        }
        U sibling = CTreeUtils.getSibling(firstNode);
        U parent = firstNode.getParent();
        while (!CTreeUtils.covers(sibling, secondNode)) {
            sibling = CTreeUtils.getSibling(parent);
            parent = parent.getParent();
        }
        return parent;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> boolean covers(final U root, final U node) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (root == node) {
            return true;
        }
        return CTreeUtils.covers(root.getLeft(), node) || CTreeUtils.covers(root.getRight(), node);
    }

    private static <T, U extends ACTreeNodeExtended<T, U>> U getSibling(final U node) {
        if (Objects.isNull(node) || Objects.isNull(node.getParent())) {
            return null;
        }
        final U parent = node.getParent();
        return parent.getLeft() == node ? parent.getRight() : parent.getLeft();
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U commonAncestor3(final U root, final U firstNode, final U secondNode) {
        if (!CTreeUtils.covers(root, firstNode) || !CTreeUtils.covers(root, secondNode)) {
            return null;
        }
        return CTreeUtils.ancestorHelper(root, firstNode, secondNode);
    }

    private static <T, U extends ACBaseTreeNode<T, U>> U ancestorHelper(final U root, final U firstNode, final U secondNode) {
        if (Objects.isNull(root) || root == firstNode || root == secondNode) {
            return root;
        }
        boolean firstIsOnLeft = CTreeUtils.covers(root.getLeft(), firstNode);
        boolean secondIsOnLeft = CTreeUtils.covers(root.getLeft(), secondNode);
        if (!Objects.equals(firstIsOnLeft, secondIsOnLeft)) {
            return root;
        }
        final U childSide = firstIsOnLeft ? root.getLeft() : root.getRight();
        return CTreeUtils.ancestorHelper(childSide, firstNode, secondNode);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> U commonAncestor4(final U root, final U firstNode, final U secondNode) {
        final Result<T, U> result = CTreeUtils.commonAncestorHelper(root, firstNode, secondNode);
        if (result.isAncestor) {
            return result.node;
        }
        return null;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> Result<T, U> commonAncestorHelper(final U root, final U firstNode, final U secondNode) {
        if (Objects.isNull(root)) {
            return new Result<>(null, false);
        }
        if (root == firstNode && root == secondNode) {
            return new Result(root, true);
        }
        final Result<T, U> temp = CTreeUtils.commonAncestorHelper(root.getLeft(), firstNode, secondNode);
        if (temp.isAncestor) {
            return temp;
        }
        final Result<T, U> temp2 = CTreeUtils.commonAncestorHelper(root.getRight(), firstNode, secondNode);
        if (temp2.isAncestor) {
            return temp2;
        }
        if (Objects.nonNull(temp.node) && Objects.nonNull(temp2.node)) {
            return new Result<>(root, true);
        } else if (root == firstNode || root == secondNode) {
            boolean isAncestor = Objects.nonNull(temp.node) || Objects.nonNull(temp2.node);
            return new Result<>(root, isAncestor);
        }
        return new Result<>(Objects.nonNull(temp.node) ? temp.node : temp2.node, false);
    }

    @Data
    @EqualsAndHashCode
    @ToString
    private static class Result<T, U extends ACBaseTreeNode<T, U>> {

        public U node;
        public boolean isAncestor;

        public Result(final U node, boolean isAncestor) {
            this.node = node;
            this.isAncestor = isAncestor;
        }
    }

    public static <T extends Serializable, U extends ACBaseTreeNode<T, U>> List<LinkedList<T>> allSequences(final U node) {
        final List<LinkedList<T>> result = new ArrayList<>();
        if (Objects.isNull(node)) {
            result.add(new LinkedList<>());
            return result;
        }
        final LinkedList<T> prefix = new LinkedList<>();
        prefix.add(node.getData());
        final List<LinkedList<T>> leftSequence = allSequences(node.getLeft());
        final List<LinkedList<T>> rightSequence = allSequences(node.getRight());
        leftSequence.stream().forEach((left) -> {
            rightSequence.stream().map((right) -> {
                final List<LinkedList<T>> weaved = new ArrayList<>();
                weaveLists(left, right, weaved, prefix);
                return weaved;
            }).forEach((weaved) -> {
                result.addAll(weaved);
            });
        });
        return result;
    }

    private static <T extends Serializable> void weaveLists(final LinkedList<T> first, final LinkedList<T> second, final List<LinkedList<T>> results, final LinkedList<T> prefix) {
        if (first.isEmpty() || second.isEmpty()) {
            final List<T> result = CListUtils.cloneList(prefix);
            result.addAll(first);
            result.addAll(second);
            results.add((LinkedList<T>) result);
            return;
        }
        final T headFirst = first.removeFirst();
        prefix.addLast(headFirst);
        CTreeUtils.weaveLists(first, second, results, prefix);
        prefix.removeLast();
        first.addFirst(headFirst);

        final T headSecond = second.removeFirst();
        prefix.addLast(headSecond);
        CTreeUtils.weaveLists(first, second, results, prefix);
        prefix.removeLast();
        second.addFirst(headSecond);
    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean containsTree(final U firstNode, final U secondNode) {
        final StringBuffer firstStr = new StringBuffer();
        final StringBuffer secondStr = new StringBuffer();
        CTreeUtils.getOrderString(firstNode, firstStr);
        CTreeUtils.getOrderString(secondNode, secondStr);
        return firstStr.indexOf(secondStr.toString()) != -1;
    }

    public static <T, S extends TreePosition<T, S>> Iterable<T> postOrder(@NonNull final IPositionalTree<T, S> tree, final S position) {
        return preOrder(tree, position, new ArrayList<>());
    }

    private static <T, S extends TreePosition<T, S>> Iterable<T> postOrder(@NonNull final IPositionalTree<T, S> tree, final S position, final List<T> result) {
        final PositionIterator<T> positionIterator = (PositionIterator<T>) tree.children(position);
        while (positionIterator.hasNext()) {
            postOrder(tree, positionIterator.nextPosition(), result);
        }
        result.add(position.element());
        return result;
    }

    public static <T, S extends TreePosition<T, S>> Iterable<T> preOrder(@NonNull final IPositionalTree<T, S> tree, final S position) {
        return preOrder(tree, position, new ArrayList<>());
    }

    private static <T, S extends TreePosition<T, S>> Iterable<T> preOrder(@NonNull final IPositionalTree<T, S> tree, final S position, final List<T> result) {
        result.add(position.element());
        final PositionIterator<T> positionIterator = (PositionIterator<T>) tree.children(position);
        while (positionIterator.hasNext()) {
            preOrder(tree, positionIterator.nextPosition(), result);
        }
        return result;
    }

    public static <T, S extends TreePosition<T, S>> int depth(@NonNull final IPositionalTree<T, S> tree, final S position) {
        if (tree.isRoot(position)) {
            return 0;
        }
        return 1 + depth(tree, tree.getParent(position));
    }

    public static <T, S extends TreePosition<T, S>> String parenthetic(@NonNull final IPositionalTree<T, S> tree, final S position) {
        final StringBuilder sb = new StringBuilder();
        if (tree.isExternal(position)) {
            final PositionIterator<T> positionIterator = (PositionIterator<T>) tree.children(position);
            sb.append("(").append(parenthetic(tree, positionIterator.nextPosition()));
            while (positionIterator.hasNext()) {
                sb.append(",").append(parenthetic(tree, positionIterator.nextPosition()));
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public static <T, S extends TreePosition<T, S>> int height(@NonNull final IPositionalTree<T, S> tree, final S position) {
        if (tree.isExternal(position)) {
            return 0;
        }
        int h = 0;
        final PositionIterator<S> children = tree.children(position);
        while (children.hasNext()) {
            h = Math.max(h, height(tree, (S) children.nextPosition()));
        }
        return 1 + h;
    }

    private static <T, U extends ACBaseTreeNode<T, U>> void getOrderString(final U node, final StringBuffer sBuffer) {
        if (Objects.isNull(node)) {
            sBuffer.append("-");
            return;
        }
        sBuffer.append(node.getData()).append(" ");
        CTreeUtils.getOrderString(node.getLeft(), sBuffer);
        CTreeUtils.getOrderString(node.getRight(), sBuffer);

    }

    public static <T, U extends ACBaseTreeNode<T, U>> boolean containsTree2(final U firstNode, final U secondNode) {
        if (Objects.isNull(secondNode)) {
            return true;
        }
        return CTreeUtils.subTree(firstNode, secondNode);
    }

    private static <T, U extends ACBaseTreeNode<T, U>> boolean subTree(final U firstNode, final U secondNode) {
        if (Objects.isNull(firstNode)) {
            return false;
        } else if (Objects.equals(firstNode.getData(), secondNode.getData()) && matchTree(firstNode, secondNode)) {
            return true;
        }
        return CTreeUtils.subTree(firstNode.getLeft(), secondNode) || CTreeUtils.subTree(firstNode.getRight(), secondNode);
    }

    private static <T, U extends ACBaseTreeNode<T, U>> boolean matchTree(final U firstNode, final U secondNode) {
        if (Objects.isNull(firstNode) && Objects.isNull(secondNode)) {
            return true;
        } else if (Objects.isNull(firstNode) || Objects.isNull(secondNode)) {
            return false;
        } else if (!Objects.equals(firstNode.getData(), secondNode.getData())) {
            return false;
        }
        return CTreeUtils.matchTree(firstNode.getLeft(), secondNode.getLeft()) && matchTree(firstNode.getRight(), secondNode.getRight());
    }

    public static <U extends ACBaseTreeNode<Double, U>> Integer countPathsWithSum(final U root, double targetSum) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int pathsFromRoot = CTreeUtils.countPathWithSumFromNode(root, targetSum, 0.0);
        int pathsOnLeft = CTreeUtils.countPathsWithSum(root.getLeft(), targetSum);
        int pathsOnRight = CTreeUtils.countPathsWithSum(root.getRight(), targetSum);
        return (pathsFromRoot + pathsOnLeft + pathsOnRight);
    }

    private static <U extends ACBaseTreeNode<Double, U>> int countPathWithSumFromNode(final U node, double targetSum, double currentSum) {
        if (Objects.isNull(node)) {
            return 0;
        }
        currentSum += node.getData();
        int totalPaths = 0;
        if (Double.compare(currentSum, targetSum) == 0) {
            totalPaths++;
        }
        totalPaths += CTreeUtils.countPathWithSumFromNode(node.getLeft(), targetSum, currentSum);
        totalPaths += CTreeUtils.countPathWithSumFromNode(node.getRight(), targetSum, currentSum);
        return totalPaths;
    }

    public static <U extends ACBaseTreeNode<Double, U>> int countPathsWithSum2(final U root, final double targetSum) {
        return CTreeUtils.countPathsWithSum(root, targetSum, 0.0, new HashMap<>());
    }

    private static <U extends ACBaseTreeNode<Double, U>> int countPathsWithSum(final U node, double targetSum, double currentSum, final Map<Double, Integer> pathCount) {
        if (Objects.isNull(node)) {
            return 0;
        }
        currentSum += node.getData();
        double sum = currentSum - targetSum;
        int totalPaths = pathCount.getOrDefault(sum, 0);
        if (Double.compare(currentSum, targetSum) == 0) {
            totalPaths++;
        }
        CTreeUtils.incrementHashTable(pathCount, currentSum, 1);
        totalPaths += CTreeUtils.countPathsWithSum(node.getLeft(), targetSum, currentSum, pathCount);
        totalPaths += CTreeUtils.countPathsWithSum(node.getRight(), targetSum, currentSum, pathCount);
        CTreeUtils.incrementHashTable(pathCount, currentSum, -1);
        return totalPaths;
    }

    private static void incrementHashTable(final Map<Double, Integer> hashTable, final Double key, final Integer delta) {
        if (0 == delta) {
            hashTable.remove(key);
        } else {
            hashTable.put(key, delta);
        }
    }

    @SuppressWarnings("UnusedAssignment")
    private static <T> void track(CRankTreeNode<T> root, final T value) {
        if (Objects.isNull(root)) {
            root = new CRankTreeNode<>(value);
        } else {
            root.insert(value);
        }
    }

    public static <T, U extends CRankTreeNode<T>> int getRank(final U root, final T value) {
        return root.getRank(value);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CRankTreeNode<T> extends ACBaseTreeNode<T, CRankTreeNode<T>> {

        private int leftSize;
        private final Comparator<? super T> cmp;

        public CRankTreeNode() {
            this(null);
        }

        public CRankTreeNode(final T data) {
            this(data, CUtils.DEFAULT_SORT_COMPARATOR);
        }

        public CRankTreeNode(final T data, final Comparator<? super T> cmp) {
            this(data, null, null, cmp);
        }

        public CRankTreeNode(final T data, final CRankTreeNode<T> left, final CRankTreeNode<T> right, final Comparator<? super T> cmp) {
            super(data, left, right);
            this.leftSize = 0;
            this.cmp = cmp;
        }

        public void insert(final T value) {
            if (Objects.compare(this.data, value, this.cmp) >= 0) {
                if (Objects.nonNull(this.left)) {
                    this.left.insert(value);
                } else {
                    this.left = new CRankTreeNode<>(value, this.cmp);
                }
                leftSize++;
            } else {
                if (Objects.nonNull(this.right)) {
                    this.right.insert(value);
                } else {
                    this.right = new CRankTreeNode<>(value, this.cmp);
                }
            }
        }

        public int getRank(final T value) {
            if (Objects.compare(this.data, value, this.cmp) == 0) {
                return this.leftSize;
            } else if (Objects.compare(this.data, value, this.cmp) > 0) {
                if (Objects.isNull(this.left)) {
                    return -1;
                }
                return this.left.getRank(value);
            } else {
                int rightRank = Objects.isNull(this.right) ? -1 : this.right.getRank(value);
                if (-1 == rightRank) {
                    return -1;
                }
                return this.leftSize + 1 + rightRank;
            }
        }
    }

    private static <U extends ACBaseTreeNode<Double, U>> U convertToCircular(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final U left = convertToCircular(root.getLeft());
        final U right = convertToCircular(root.getRight());
        if (Objects.isNull(left) && Objects.isNull(right)) {
            root.setLeft(root);
            root.setRight(root);
            return root;
        }
        final U tailRight = (Objects.isNull(right) ? null : right.getLeft());
        if (Objects.isNull(left)) {
            CTreeUtils.concat(right.getLeft(), root);
        } else {
            CTreeUtils.concat(left.getLeft(), root);
        }
        if (Objects.isNull(right)) {
            CTreeUtils.concat(root, left);
        } else {
            CTreeUtils.concat(root, right);
        }

        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            CTreeUtils.concat(tailRight, left);
        }
        return Objects.isNull(left) ? root : left;
    }

    private static <U extends ACBaseTreeNode<Double, U>> void concat(final U left, final U right) {
        left.setRight(right);
        right.setLeft(left);
    }

    public static <U extends ACBaseTreeNode<Double, U>> U convert(final U root) {
        final U head = CTreeUtils.convertToCircular(root);
        head.getLeft().setRight(null);
        head.setLeft(null);
        return head;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Operator {

        private char sign;
        private ExpressionTree operand1;
        private ExpressionTree operand2;

        public Operator(char sign, final ExpressionTree operand1, final ExpressionTree operand2) {
            this.sign = sign;
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        public String toFormatString() {
            return "(" + this.operand1 + this.sign + this.operand2 + ")";
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class ExpressionTree {

        private ExpressionTree root = null;

        public static ExpressionTree parse(final String source) {
            return null;
        }

        public String toFormatString() {
            return this.root.toString();
        }
    }
}
