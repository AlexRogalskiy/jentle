package com.wildbeeslabs.jentle.collections.tree.iface; /**
 * @author Ionesio Junior
 */

import com.wildbeeslabs.jentle.collections.tree.node.TreeNode;

/**
 * Binary Search Tree Interface
 */
public interface BSTInterface<T> {
    /**
     * Search an element in a binary search tree structure
     *
     * @return TreeNode with element
     */
    TreeNode<T> search(T element);

    /**
     * Insert an element in a binary search tree
     *
     * @param element
     */
    void insert(T element);

    /**
     * Remove an element in a binary search tree
     */
    void remove(T element);

    /**
     * Get node with the predecessor of data element
     *
     * @return TreeNode with predecessor
     */
    TreeNode<T> predecessor(T element);

    /**
     * Get node with the sucessor of data element
     *
     * @return TreeNode with sucessor
     */
    TreeNode<T> sucessor(T element);

    /**
     * Return Root TreeNode
     *
     * @return TreeNode root
     */
    TreeNode<T> getRoot();

    /**
     * Return True if binary search tree is empty or False if not
     */
    boolean isEmpty();

    /**
     * Return node with maximum data value in binary search tree
     *
     * @return TreeNode maximum
     */
    TreeNode<T> maximum();

    /**
     * Return node with minimum data value in binary search tree
     *
     * @return minimum
     */
    TreeNode<T> minimum();

    /**
     * Return array with bst elements in order(left , root, right)
     *
     * @return Ordered array
     */
    T[] toArrayOrder();

    /**
     * Return array with bst elements in pre order(root,left,right)
     *
     * @return Pre Order array
     */
    T[] toArrayPreOrder();

    /**
     * Return array with bst elements in post order(left,right,root)
     *
     * @return Post Order array
     */
    T[] toArrayPostOrder();

    /**
     * Return number of elements in Binary Search Tree
     *
     * @return size
     */
    int size();

    /**
     * Return max height of Binary Search Tree
     *
     * @return height
     */
    int height();
}
