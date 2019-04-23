package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalTreeContainer;
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
public class PositionalTree<T> implements IPositionalTreeContainer<T> {

    @Override
    public void swap(final Position<T> positionFirst, final Position<T> positionLast) {

    }

    @Override
    public T replace(final Position<T> position, final T value) {
        return null;
    }

    @Override
    public Position<T> root() {
        return null;
    }

    @Override
    public Position<T> parent(final Position<T> position) {
        return null;
    }

    @Override
    public @NonNull <S extends Position<T>> PositionIterator<S> children(final S position) {
        return null;
    }

    @Override
    public boolean isInternal(final Position<T> position) {
        return false;
    }

    @Override
    public boolean isExternal(final Position<T> position) {
        return false;
    }

    @Override
    public boolean isRoot(final Position<T> position) {
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
