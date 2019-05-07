package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.tree.ITreeCollection;
import com.wildbeeslabs.jentle.collections.tree.iface.tree.position.IPositionalTreeContainer;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Optional;

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
public class CPositionalTree<T> implements IPositionalTreeContainer<T, CPositionalTreeNode<T>>, ITreeCollection<T, CPositionalTreeNode<T>> {

    @Override
    public <S extends Position<T>> void swap(final S positionFirst, final S positionLast) {
    }

    @Override
    public <S extends Position<T>> T replace(final S position, final T value) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S root() {
        return null;
    }

    @Override
    public @NonNull <S extends CPositionalTreeNode<T>> PositionIterator<CPositionalTreeNode<T>> children(final S position) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void setRoot(final Optional<? extends T> value) {
    }

    @Override
    public CPositionalTreeNode<T> getRoot() {
        return null;
    }

    @Override
    public @NonNull <S extends Position<T>> PositionIterator<S> positionIterator() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public <S extends CPositionalTreeNode<T>> S getSibling(final S position) {
        return null;
    }
}
