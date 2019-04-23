package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalTreeContainer;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

/**
 * Custom {@link IPositionalTreeContainer} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public class PositionalTree<T> implements IPositionalTreeContainer<T, CPositionalTreeNode<T>> {

    @Override
    public <S extends Position<T>> void swap(S positionFirst, S positionLast) {

    }

    @Override
    public <S extends Position<T>> T replace(S position, T value) {
        return null;
    }

    @Override
    public <S extends TreePosition<T, CPositionalTreeNode<T>>> S root() {
        return null;
    }

    @Override
    public <S extends TreePosition<T, CPositionalTreeNode<T>>> S getParent(S position) {
        return null;
    }

    @Override
    public @NonNull PositionIterator<CPositionalTreeNode<T>> children(CPositionalTreeNode<T> position) {
        return null;
    }

    @Override
    public boolean isInternal(CPositionalTreeNode<T> position) {
        return false;
    }

    @Override
    public boolean isExternal(CPositionalTreeNode<T> position) {
        return false;
    }

    @Override
    public <S extends TreePosition<T, CPositionalTreeNode<T>>> boolean isRoot(S position) {
        return false;
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
}
