package com.wildbeeslabs.jentle.collections.tree.utils;

import com.wildbeeslabs.jentle.collections.tree.node.TreeNode;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.util.*;

import static java.util.Arrays.asList;

@UtilityClass
public class TreeUtils {

    @Data
    public static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private Node<T> next;
    }

    public static class Codec {
        private static final String spliter = ",";
        private static final String NN = "X";

        // Encodes a tree to a single string.
        public static <T> String serialize(final TreeNode<T> root) {
            StringBuilder sb = new StringBuilder();
            buildString(root, sb);
            return sb.toString();
        }

        private static <T> void buildString(final TreeNode<T> node, final StringBuilder sb) {
            if (node == null) {
                sb.append(NN).append(spliter);
            } else {
                sb.append(node.getData()).append(spliter);
                buildString(node.getLeft(), sb);
                buildString(node.getRight(), sb);
            }
        }

        // Decodes your encoded data to tree.
        public static <T> TreeNode<T> deserialize(final String data) {
            final Deque<String> nodes = new LinkedList<>();
            nodes.addAll(asList(data.split(spliter)));
            return buildTree(nodes);
        }

        private static <T> TreeNode<T> buildTree(final Deque<String> nodes) {
            final String val = nodes.remove();
            if (val.equals(NN)) {
                return null;
            }
            final TreeNode<T> node = new TreeNode<>((T) Integer.valueOf(val));
            node.setLeft(buildTree(nodes));
            node.setRight(buildTree(nodes));
            return node;
        }
    }

    public static class BSTIterator<T> {
        private final Stack<TreeNode<T>> stack = new Stack<>();

        public BSTIterator(final TreeNode<T> root) {
            this.pushAll(root);
        }

        /**
         * @return whether we have a next smallest number
         */
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        /**
         * @return the next smallest number
         */
        public T next() {
            final TreeNode<T> tmpNode = this.stack.pop();
            this.pushAll(tmpNode.getRight());
            return tmpNode.getData();
        }

        private void pushAll(final TreeNode<T> node) {
            TreeNode<T> value = node;
            while (value != null) {
                this.stack.push(value);
                value = value.getLeft();
            }
        }
    }

    public static <T> List<T> inorderTraversal(final TreeNode<T> root) {
        TreeNode<T> node = root;
        final List<T> list = new ArrayList<>();
        final Stack<TreeNode<T>> stack = new Stack<>();
        while (node != null || !stack.empty()) {
            while (root != null) {
                stack.add(node);
                node = node.getLeft();
            }
            node = stack.pop();
            list.add(node.getData());
            node = node.getRight();
        }
        return list;
    }

    public static <T> TreeNode<T> lowestCommonAncestor(final TreeNode<T> root, final TreeNode<T> p, final TreeNode<T> q) {
        if (root == null || root == p || root == q) return root;
        final TreeNode<T> left = lowestCommonAncestor(root.getLeft(), p, q);
        final TreeNode<T> right = lowestCommonAncestor(root.getRight(), p, q);
        return left == null ? right : right == null ? left : root;
    }

    public static <T> List<T> postorderTraversal(final TreeNode<T> root) {
        final LinkedList<T> result = new LinkedList<>();
        final Stack<TreeNode<T>> stack = new Stack<>();
        TreeNode<T> node = root;
        while (!stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                // Reverse the process of preorder
                result.addFirst(node.getData());
                // Reverse the process of preorder
                node = node.getRight();
            } else {
                final TreeNode nextNode = stack.pop();
                node = nextNode.getLeft();
            }
        }
        return result;
    }

    public static <T> List<T> preorderTraversal(final TreeNode<T> root) {
        final List<T> list = new LinkedList<>();
        final Stack<TreeNode<T>> rights = new Stack<>();
        TreeNode<T> node = root;
        while (node != null) {
            list.add(node.getData());
            if (node.getRight() != null) {
                rights.push(node.getRight());
            }
            node = node.getLeft();
            if (node == null && !rights.isEmpty()) {
                node = rights.pop();
            }
        }
        return list;
    }

    public static <T> Node<T> connect(final Node<T> root) {
        if (root == null) {
            return null;
        }
        Node<T> cur = root;
        while (cur != null) {
            if (cur.left != null) {
                cur.left.next = (cur.right != null) ? cur.right : getNext(cur);
            }
            if (cur.right != null) {
                cur.right.next = getNext(cur);
            }
            cur = cur.next;
        }
        connect(root.left);
        connect(root.right);
        return root;
    }

    private static <T> Node<T> getNext(final Node<T> root) {
        Node temp = root.next;
        while (temp != null) {
            if (temp.left != null)
                return temp.left;

            if (temp.right != null)
                return temp.right;

            temp = temp.next;
        }
        return null;
    }

    public static <T> Node<T> connect2(final Node<T> root) {
        Node<T> level_start = root;
        while (level_start != null) {
            Node cur = level_start;
            while (cur != null) {
                if (cur.left != null) cur.left.next = cur.right;
                if (cur.right != null && cur.next != null) cur.right.next = cur.next.left;

                cur = cur.next;
            }
            level_start = level_start.left;
        }
        return root;
    }

    public static boolean hasPathSum(final TreeNode<Integer> root, final Integer sum) {
        if (root == null) {
            return false;
        }
        if (root.getLeft() == null && root.getRight() == null && sum - root.getData() == 0)
            return true;

        return hasPathSum(root.getLeft(), sum - root.getData()) || hasPathSum(root.getRight(), sum - root.getData());
    }

    public static <T> TreeNode<T> buildTree(final T[] inorder, final T[] postorder) {
        if (inorder.length == 0 || postorder.length == 0) return null;
        int ip = inorder.length - 1;
        int pp = postorder.length - 1;

        final Stack<TreeNode<T>> stack = new Stack<>();
        TreeNode<T> prev = null;
        TreeNode<T> root = new TreeNode<>(postorder[pp]);
        stack.push(root);
        pp--;

        while (pp >= 0) {
            while (!stack.isEmpty() && stack.peek().getData() == inorder[ip]) {
                prev = stack.pop();
                ip--;
            }
            TreeNode newNode = new TreeNode(postorder[pp]);
            if (prev != null) {
                prev.setLeft(newNode);
            } else if (!stack.isEmpty()) {
                TreeNode currTop = stack.peek();
                currTop.setRight(newNode);
            }
            stack.push(newNode);
            prev = null;
            pp--;
        }

        return root;
    }

    public static <T> TreeNode<T> buildTree2(final T[] inorder, final T[] postorder) {
        if (inorder == null || postorder == null || inorder.length != postorder.length)
            return null;
        final Map<T, Integer> hm = new HashMap<>();
        for (int i = 0; i < inorder.length; ++i)
            hm.put(inorder[i], i);
        return buildTreeHelper(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1, hm);
    }

    private static <T> TreeNode<T> buildTreeHelper(final T[] inorder, int is, int ie, final T[] postorder, int ps, int pe, final Map<T, Integer> hm) {
        if (ps > pe || is > ie) return null;
        final TreeNode root = new TreeNode(postorder[pe]);
        final int ri = hm.get(postorder[pe]);

        final TreeNode leftchild = buildTreeHelper(inorder, is, ri - 1, postorder, ps, ps + ri - is - 1, hm);
        final TreeNode rightchild = buildTreeHelper(inorder, ri + 1, ie, postorder, ps + ri - is, pe - 1, hm);
        root.setLeft(leftchild);
        root.setRight(rightchild);
        return root;
    }

    public static <T> TreeNode<T> buildTree3(final T[] preorder, final T[] inorder) {
        return buildTreeHelper(0, 0, inorder.length - 1, preorder, inorder);
    }

    private static <T> TreeNode<T> buildTreeHelper(int preStart, int inStart, int inEnd, final T[] preorder, final T[] inorder) {
        if (preStart > preorder.length - 1 || inStart > inEnd) {
            return null;
        }
        final TreeNode<T> root = new TreeNode<>(preorder[preStart]);
        int inIndex = 0; // Index of current root in inorder
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == root.getData()) {
                inIndex = i;
            }
        }
        root.setLeft(buildTreeHelper(preStart + 1, inStart, inIndex - 1, preorder, inorder));
        root.setRight(buildTreeHelper(preStart + inIndex - inStart + 1, inIndex + 1, inEnd, preorder, inorder));
        return root;
    }

    public static <T> int maxDepth(final TreeNode<T> root) {
        if (root == null) return 0;
        final int leftMax = (root.getLeft() == null) ? 0 : maxDepth(root.getLeft());
        final int rightMax = (root.getRight() == null) ? 0 : maxDepth(root.getRight());
        return 1 + Math.max(leftMax, rightMax);
    }

    public static <T> List<List<T>> levelOrder(final TreeNode<T> root) {
        final Queue<TreeNode<T>> queue = new LinkedList<>();
        final List<List<T>> wrapList = new LinkedList<>();
        if (root == null) {
            return wrapList;
        }
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelNum = queue.size();
            final List<T> subList = new LinkedList<>();
            for (int i = 0; i < levelNum; i++) {
                if (queue.peek().getLeft() != null) {
                    queue.offer(queue.peek().getLeft());
                }
                if (queue.peek().getRight() != null) {
                    queue.offer(queue.peek().getRight());
                }
                subList.add(queue.poll().getData());
            }
            wrapList.add(subList);
        }
        return wrapList;
    }

    /**
     * recursive method
     *
     * @param root pending node
     * @return true or false
     */
    public static <T> boolean isSymmetric(final TreeNode<T> root) {
        return root == null || isSymmetricHelper(root.getLeft(), root.getRight());
    }

    private static <T> boolean isSymmetricHelper(final TreeNode<T> left, final TreeNode<T> right) {
        if (left == null || right == null) {
            return left == right;
        }
        if (left.getData() != right.getData()) {
            return false;
        }
        return isSymmetricHelper(left.getLeft(), right.getRight())
            && isSymmetricHelper(left.getRight(), right.getLeft());
    }


    /**
     * Non-recursive method
     *
     * @param root pending node
     * @return true or false
     */
    public static <T> boolean isSymmetric2(final TreeNode<T> root) {
        if (root == null) {
            return true;
        }
        final Stack<TreeNode<T>> stack = new Stack<>();
        TreeNode<T> left, right;
        if (root.getLeft() != null) {
            if (root.getRight() == null) {
                return false;
            }
            stack.push(root.getLeft());
            stack.push(root.getRight());
        } else if (root.getRight() != null) {
            return false;
        }

        while (!stack.empty()) {
            if (stack.size() % 2 != 0) {
                return false;
            }

            right = stack.pop();
            left = stack.pop();

            if (right.getData() != left.getData()) {
                return false;
            }

            if (left.getLeft() != null) {
                if (right.getRight() == null)
                    return false;
                stack.push(left.getLeft());
                stack.push(right.getRight());
            } else if (right.getRight() != null) {
                return false;
            }

            if (left.getRight() != null) {
                if (right.getLeft() == null)
                    return false;
                stack.push(left.getRight());
                stack.push(right.getLeft());
            } else if (right.getLeft() != null) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isValidBST(final TreeNode<T> root, final Comparator<? super T> comparator) {
        if (root == null) {
            return true;
        }
        final Stack<TreeNode<T>> stack = new Stack<>();
        TreeNode<T> pre = null, node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
            node = stack.pop();

            if (pre != null && Objects.compare(node.getData(), pre.getData(), comparator) <= 0) {
                return false;
            }

            pre = node;
            node = node.getRight();
        }
        return true;
    }
}
