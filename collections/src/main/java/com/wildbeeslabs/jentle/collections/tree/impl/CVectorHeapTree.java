package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.IHeapTree;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Iterator;

/**
 * Custom {@link IHeapTree} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@EqualsAndHashCode
@ToString
public class CVectorHeapTree<T> implements IHeapTree<T, CPositionalTreeNode<T>> {

    @Override
    public <S extends CPositionalTreeNode<T>> S add(T value) {
        return null;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public <S extends Position<T>> void swap(S positionFirst, S positionLast) {

    }

    @Override
    public <S extends Position<T>> T replace(S position, T value) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getLeft(S value) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getRight(S value) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getSibling(S value) {
        return null;
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
    public @NonNull <S extends Position<T>> PositionIterator<S> positionIterator() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S root() {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getParent(S position) {
        return null;
    }

    @Override
    public @NonNull <S extends CPositionalTreeNode<T>> PositionIterator<CPositionalTreeNode<T>> children(S position) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isInternal(S position) {
        return false;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isExternal(S position) {
        return false;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isRoot(S position) {
        return false;
    }
}
