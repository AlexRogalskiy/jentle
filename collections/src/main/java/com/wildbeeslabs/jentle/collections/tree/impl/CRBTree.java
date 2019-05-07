package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.dictionary.Dictionary;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import com.wildbeeslabs.jentle.collections.tree.node.CRedBlackTreeNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * Custom red-black {@link CBinarySearchTree} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CRBTree<K, V> extends CBinarySearchTree<K, V, CRedBlackTreeNode<K, V>> implements Dictionary<K, V> {

    /**
     * Default node colors
     */
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public CRBTree(final Comparator<? super K> comparator) {
        super(comparator);
        //this.binaryTree = new ResctructurableNodeBinaryTree<>();
    }

    @Override
    public void insertElement(final K key, final V value) {
        super.insertElement(key, value);
        final CPositionalTreeNode<CRedBlackTreeNode<K, V>> currentPosition = (CPositionalTreeNode<CRedBlackTreeNode<K, V>>) this.getActionPosition();
        this.getBinaryTree().replace(currentPosition, this.createRedNode(key, value));
        if (this.getBinaryTree().isRoot(currentPosition)) {
            this.setBlack(currentPosition);
        } else {
            this.remedyDoubleRed(currentPosition);
        }
    }

    private void remedyDoubleRed(final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position) {
        CPositionalTreeNode<CRedBlackTreeNode<K, V>> currentPosition = this.getBinaryTree().getParent(position);
        if (this.getBinaryTree().isRoot(currentPosition)) {
            return;
        }
        if (!isPositionRed(position)) {
            return;
        }
        if (!isPositionRed(this.getBinaryTree().getSibling(currentPosition))) {
//            currentPosition = this.getBinaryTree().restructure(position);
            this.setBlack(currentPosition);
            this.setRed(this.getBinaryTree().getLeftChild(currentPosition));
            this.setRed(this.getBinaryTree().getRightChild(currentPosition));
        } else {
            this.setBlack(this.getBinaryTree().getSibling(currentPosition));
            final CPositionalTreeNode<CRedBlackTreeNode<K, V>> positionU = this.getBinaryTree().getParent(currentPosition);
            if (this.getBinaryTree().isRoot(positionU)) {
                return;
            }
            this.setRed(positionU);
            this.remedyDoubleRed(positionU);
        }
    }

    @Override
    public V removeElement(final K key) {
        final V result = super.removeElement(key);
        final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position = (CPositionalTreeNode<CRedBlackTreeNode<K, V>>) this.getActionPosition();
        if (position != DEFAULT_EMPTY_VALUE) {
            if (this.isParentRed(position) || this.getBinaryTree().isRoot(position) || isPositionRed(position)) {
                this.setBlack(position);
            } else {
                this.remedyDoubleBlack(position);
            }
        }
        return result;
    }

    private boolean isParentRed(final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position) {
        return position.getParent().element().isRed();
    }

    private void remedyDoubleBlack(final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position) {
        CPositionalTreeNode<CRedBlackTreeNode<K, V>> positionX = this.getBinaryTree().getParent(position);
        CPositionalTreeNode<CRedBlackTreeNode<K, V>> positionY = this.getBinaryTree().getSibling(position);
        CPositionalTreeNode<CRedBlackTreeNode<K, V>> positionZ = null;
        boolean prevColor = false;
        if (!this.isPositionRed(positionY)) {
            positionZ = this.redChild(positionY);
            if (this.hasRedChild(positionY)) {
                prevColor = this.isPositionRed(positionX);
//                positionZ = this.getBinaryTree().restructure(positionZ);
                this.setColor(positionZ, prevColor);
                this.setBlack(position);
                this.setBlack(this.getBinaryTree().getLeftChild(positionZ));
                this.setBlack(this.getBinaryTree().getRightChild(positionZ));
                return;
            }
            this.setBlack(position);
            this.setRed(positionY);
            ;
            if (!this.isPositionRed(positionX)) {
                if (!this.getBinaryTree().isRoot(positionX)) {
                    this.remedyDoubleBlack(positionX);
                }
                return;
            }
            this.setBlack(positionX);
            return;
        }
        if (positionY == this.getBinaryTree().getRightChild(positionX)) {
            positionZ = this.getBinaryTree().getRightChild(positionY);
        } else {
            positionZ = this.getBinaryTree().getLeftChild(positionY);
        }
//        this.getBinaryTree().restructure(positionZ);
        this.setBlack(positionY);
        this.setRed(positionX);
        this.remedyDoubleBlack(position);
    }

    private CPositionalTreeNode<CRedBlackTreeNode<K, V>> redChild(final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position) {
        if (position.getRight().element().isRed()) {
            return position.getRight();
        } else if (position.getLeft().element().isRed()) {
            return position.getLeft();
        }
        return null;
    }

    private void setColor(final Position<CRedBlackTreeNode<K, V>> position, final boolean isRed) {
        position.element().setRed(isRed);
    }

    private boolean hasRedChild(final CPositionalTreeNode<CRedBlackTreeNode<K, V>> position) {
        return position.getLeft().element().isRed() || position.getRight().element().isRed();
    }

    private boolean isPositionRed(final Position<CRedBlackTreeNode<K, V>> position) {
        return position.element().isRed();
    }

    private void setRed(final Position<CRedBlackTreeNode<K, V>> position) {
        position.element().setRed(true);
    }

    private void setBlack(final Position<CRedBlackTreeNode<K, V>> position) {
        position.element().setRed(false);
    }

    private CRedBlackTreeNode<K, V> createRedNode(final K key, final V value) {
        final CRedBlackTreeNode<K, V> node = this.createNewNode(key, value);
        node.setRed(true);
        return node;
    }

    @Override
    protected CRedBlackTreeNode<K, V> createNewNode(final K key, final V value) {
        return new CRedBlackTreeNode<>(key, value);
    }
}
