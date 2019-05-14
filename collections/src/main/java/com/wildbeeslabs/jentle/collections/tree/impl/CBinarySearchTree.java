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
public class CBinarySearchTree<K, V, T extends CKeyValueNode<K, V>> implements Dictionary<K, V> {

    /**
     * Default empty node {@code V}
     */
    public final V DEFAULT_EMPTY_VALUE = (V) new Object();

    protected final CPositionalTree<T> binaryTree;
    protected int size;
    protected Comparator<? super K> comparator;
    protected Position<T> actionPosition;

    public CBinarySearchTree(final Comparator<? super K> comparator) {
        this.comparator = comparator;
        this.binaryTree = new CPositionalTree<>();
    }

    protected K key(final Position<T> position) {
        return position.element().getKey();
    }

    protected V element(final Position<T> position) {
        return position.element().getValue();
    }

    protected void swap(final Position<T> first, final Position<T> last) {
        this.getBinaryTree().replace(first, last.element());
    }

    protected <S extends CPositionalTreeNode<T>> S findPosition(final K key, final S position) {
        if (this.getBinaryTree().isExternal(position)) {
            return position;
        }
        final K currentKey = this.key(position);
        final int c = Objects.compare(key, currentKey, this.getComparator());
        if (c < 0) {
            return findPosition(key, this.getBinaryTree().getLeftChild(position));
        } else if (c > 0) {
            return findPosition(key, this.getBinaryTree().getRightChild(position));
        }
        return position;
    }

    @Override
    public int size() {
        return ((this.binaryTree.size() - 1) / 2);
    }

    @Override
    public boolean isEmpty() {
        return (this.size() == 1);
    }

    @Override
    public V findElement(final K key) {
        final CPositionalTreeNode<T> currentPosition = this.findPosition(key, this.getBinaryTree().root());
        this.setActionPosition(currentPosition);
        if (this.getBinaryTree().isInternal(currentPosition)) {
            return this.element(currentPosition);
        }
        return DEFAULT_EMPTY_VALUE;
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
        CPositionalTreeNode<T> currentPosition = this.getBinaryTree().root();
        do {
            currentPosition = this.findPosition(key, currentPosition);
            if (this.getBinaryTree().isExternal(currentPosition)) {
                break;
            } else {
                currentPosition = this.getBinaryTree().getRightChild(currentPosition);
            }
        } while (true);
//        this.getBinaryTree().expandExternal(currentPosition);
        final T item = this.createNewNode(key, value);
        this.getBinaryTree().replace(currentPosition, item);
        this.setActionPosition(currentPosition);
    }

    @Override
    public V removeElement(final K key) {
        CPositionalTreeNode<T> currentPosition = this.findPosition(key, this.getBinaryTree().root());
        if (this.getBinaryTree().isExternal(currentPosition)) {
            this.setActionPosition(currentPosition);
            return DEFAULT_EMPTY_VALUE;
        }
        final V value = this.element(currentPosition);
        if (this.getBinaryTree().isExternal(this.getBinaryTree().getLeftChild(currentPosition))) {
            currentPosition = this.getBinaryTree().getLeftChild(currentPosition);
        } else if (this.getBinaryTree().isExternal(this.getBinaryTree().getRightChild(currentPosition))) {
            currentPosition = this.getBinaryTree().getRightChild(currentPosition);
        } else {
            Position<T> swapPosition = currentPosition;
            currentPosition = this.getBinaryTree().getRightChild(currentPosition);
            do {
                currentPosition = this.getBinaryTree().getLeftChild(currentPosition);
            } while (this.getBinaryTree().isInternal(currentPosition));
            this.swap(swapPosition, this.getBinaryTree().getParent(currentPosition));
        }
//        this.setActionPosition(this.binaryTree.sibling(currentPosition));
//        this.getBinaryTree().removeAboveExternal(currentPosition);
        return value;
    }

    @Override
    public Iterator<KeyValueNode<K, V>> iterator() {
        return null;
    }

    protected T createNewNode(final K key, final V value) {
        return (T) CKeyValueNode.of(key, value);
    }
}
