package com.wildbeeslabs.jentle.collections.iface.iterator;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Custom position {@link Iterator} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionIterator<T> {

    boolean hasNext();

    <S extends Position<T>> S nextPosition();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default <S extends Position<T>> void forEachRemaining(final Consumer<? super S> action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextPosition());
        }
    }
}
