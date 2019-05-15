package com.wildbeeslabs.jentle.collections.iface.iterator;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Position iterator interface declaration
 *
 * @param <T> type of element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionIterator<T> {

    boolean hasNext();

    <S extends Position<T>> S nextPosition();

    default void remove() {
        throw new UnsupportedOperationException("ERROR: unsupported operation remove");
    }

    default <S extends Position<T>> void forEachRemaining(final Consumer<? super S> action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextPosition());
        }
    }
}
