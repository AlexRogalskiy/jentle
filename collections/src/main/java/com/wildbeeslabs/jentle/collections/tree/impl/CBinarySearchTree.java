package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CBinarySearchTree<K, V> extends Dictionary {

    private final CBinaryTree<CKeyValueNode<K, V>> binaryTree;
    private int size;
    private Comparator<? super K> comparator;
    private Position<CKeyValueNode<K, V>> position;

    public CBinarySearchTree(final Comparator<? super K> comparator) {
        this.comparator = comparator;
        this.binaryTree = new CBinaryTree<>();
    }

    protected K key(final Position<CKeyValueNode<K, V>> position) {
        return position.element().getKey();
    }

    protected V element(final Position<CKeyValueNode<K, V>> position) {
        return position.element().getValue();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Enumeration keys() {
        return null;
    }

    @Override
    public Enumeration elements() {
        return null;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(Object key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }
}
