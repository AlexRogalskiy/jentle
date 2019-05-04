package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.dictionary.Dictionary;
import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 * Custom binary search tree {@link Dictionary} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CBinarySearchTree<K, V> implements Dictionary<K, V> {

    private final CPositionalTree<CKeyValueNode<K, V>> binaryTree;
    private int size;
    private Comparator<? super K> comparator;
    private Position<CKeyValueNode<K, V>> actionPosition;

    public CBinarySearchTree(final Comparator<? super K> comparator) {
        this.comparator = comparator;
        this.binaryTree = new CPositionalTree<>();
    }

    protected K key(final Position<CKeyValueNode<K, V>> position) {
        return position.element().getKey();
    }

    protected V element(final Position<CKeyValueNode<K, V>> position) {
        return position.element().getValue();
    }

    protected void swap(final Position<CKeyValueNode<K, V>> first, final Position<CKeyValueNode<K, V>> last) {
        this.binaryTree.replace(first, last.element());
    }

    protected <S extends CPositionalTreeNode<CKeyValueNode<K, V>>> S findPosition(final K key, final S position) {
        if (this.binaryTree.isExternal(position)) {
            return position;
        }
        K currentKey = this.key(position);
        final int c = Objects.compare(key, currentKey, this.comparator);
        if (c < 0) {
            return findPosition(key, this.binaryTree.getLeftChild(position));
        } else if (c > 0) {
            return findPosition(key, this.binaryTree.getRightChild(position));
        }
        return position;
    }

    @Override
    public int size() {
        return (this.binaryTree.size() - 1) / 2;
    }

    @Override
    public boolean isEmpty() {
        return (1 == this.size());
    }

    @Override
    public V findElement(final K key) {
        final CPositionalTreeNode<CKeyValueNode<K, V>> currentPosition = this.findPosition(key, this.binaryTree.root());
        this.actionPosition = currentPosition;
        if (this.binaryTree.isInternal(currentPosition)) {
            return this.element(currentPosition);
        }
        return null;
    }

    @Override
    public Enumeration<K> keys() {
        return null;
    }

    @Override
    public Enumeration<V> elements() {
        return null;
    }

    @Override
    public void insertElement(final K key, final V value) {
        CPositionalTreeNode<CKeyValueNode<K, V>> currentPosition = this.binaryTree.root();
        do {
            currentPosition = this.findPosition(key, currentPosition);
            if (this.binaryTree.isExternal(currentPosition)) {
                break;
            } else {
                currentPosition = this.binaryTree.getRightChild(currentPosition);
            }
        } while (true);
//        this.binaryTree.expandExternal(currentPosition);
        final CKeyValueNode<K, V> item = new CKeyValueNode<>();
        item.setKey(key);
        item.setValue(value);
        this.binaryTree.replace(currentPosition, item);
        this.actionPosition = currentPosition;
    }

    @Override
    public V removeElement(final K key) {
        CPositionalTreeNode<CKeyValueNode<K, V>> currentPosition = this.findPosition(key, this.binaryTree.root());
        if (this.binaryTree.isExternal(currentPosition)) {
            this.actionPosition = currentPosition;
            return null;
        }
        final V value = this.element(currentPosition);
        if (this.binaryTree.isExternal(this.binaryTree.getLeftChild(currentPosition))) {
            currentPosition = this.binaryTree.getLeftChild(currentPosition);
        } else if (this.binaryTree.isExternal(this.binaryTree.getRightChild(currentPosition))) {
            currentPosition = this.binaryTree.getRightChild(currentPosition);
        } else {
            Position<CKeyValueNode<K, V>> swapPosition = currentPosition;
            currentPosition = this.binaryTree.getRightChild(currentPosition);
            do {
                currentPosition = this.binaryTree.getLeftChild(currentPosition);
            } while (this.binaryTree.isInternal(currentPosition));
            this.swap(swapPosition, this.binaryTree.getParent(currentPosition));
        }
//        this.actionPosition = this.binaryTree.sibling(currentPosition);
//        this.binaryTree.removeAboveExternal(currentPosition);
        return value;
    }

    @Override
    public Iterator<KeyValueNode<K, V>> iterator() {
        return null;
    }
}
