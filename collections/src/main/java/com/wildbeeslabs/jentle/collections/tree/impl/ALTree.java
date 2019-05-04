package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.dictionary.Dictionary;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNodeExtended;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * Custom AVL search tree {@link Dictionary} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ALTree<K, V> extends CBinarySearchTree<K, V, CKeyValueNodeExtended<K, V>> {

    public ALTree(final Comparator<? super K> comparator) {
        super(comparator);
        //this.binaryTree = new ResctructurableNodeBinaryTree<>();
    }

    private <S extends CPositionalTreeNode<CKeyValueNodeExtended<K, V>>> int height(final S position) {
        if (this.getBinaryTree().isExternal(position)) {
            return 0;
        }
        return position.element().getHeight();
    }

    private <S extends CPositionalTreeNode<CKeyValueNodeExtended<K, V>>> void setHeight(final S position) {
        position.element().setHeight(1 + Math.max(this.height(this.getBinaryTree().getLeftChild(position)), this.height(this.getBinaryTree().getRightChild(position))));
    }

    private <S extends CPositionalTreeNode<CKeyValueNodeExtended<K, V>>> boolean isBalanced(final S position) {
        final int balance = this.height(this.binaryTree.getLeftChild(position)) - this.height(this.binaryTree.getRightChild(position));
        return -1 <= balance && balance <= 1;
    }

    private <S extends CPositionalTreeNode<CKeyValueNodeExtended<K, V>>> S tallerChild(final S position) {
        if (this.height(this.getBinaryTree().getLeftChild(position)) >= this.height(this.getBinaryTree().getRightChild(position))) {
            return this.getBinaryTree().getLeftChild(position);
        }
        return this.getBinaryTree().getRightChild(position);
    }

    private <S extends CPositionalTreeNode<CKeyValueNodeExtended<K, V>>> void rebalance(final S position) {
        S currentPosition = position;
        while (!this.getBinaryTree().isRoot(currentPosition)) {
            currentPosition = this.getBinaryTree().getParent(currentPosition);
            setHeight(currentPosition);
            if (!this.isBalanced(currentPosition)) {
                S xPosition = this.tallerChild(this.tallerChild(currentPosition));
//                currentPosition = this.getBinaryTree().restructure(xPosition);
                this.setHeight(this.getBinaryTree().getLeftChild(currentPosition));
                this.setHeight(this.getBinaryTree().getRightChild(currentPosition));
                this.setHeight(currentPosition);
            }
        }
    }

    @Override
    public void insertElement(final K key, final V value) {
        super.insertElement(key, value);
        CPositionalTreeNode<CKeyValueNodeExtended<K, V>> currentPosition = (CPositionalTreeNode<CKeyValueNodeExtended<K, V>>) this.getActionPosition();
        final CKeyValueNodeExtended<K, V> node = this.createNewNode(key, value);
        node.setHeight(1);
        this.getBinaryTree().replace(currentPosition, node);
        this.rebalance(currentPosition);
    }

    @Override
    public V removeElement(final K key) {
        final V node = super.removeElement(key);
        if (node != DEFAULT_EMPTY_VALUE) {
            final CPositionalTreeNode<CKeyValueNodeExtended<K, V>> currentPosition = (CPositionalTreeNode<CKeyValueNodeExtended<K, V>>) this.getActionPosition();
            this.rebalance(currentPosition);
        }
        return node;
    }

    protected CKeyValueNodeExtended<K, V> createNewNode(final K key, final V value) {
        return new CKeyValueNodeExtended<>(key, value);
    }
}
