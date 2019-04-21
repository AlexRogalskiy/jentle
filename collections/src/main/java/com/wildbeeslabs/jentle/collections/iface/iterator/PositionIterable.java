package com.wildbeeslabs.jentle.collections.iface.iterator;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Custom position {@link Iterable} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionIterable<T> {

    <S extends Position<T>> @NonNull PositionIterator<S> positionIterator();

    default <S extends Position<T>> void forEachPosition(@NonNull final Consumer<? super S> action) {
        final List<S> items = this.toList(this.positionIterator(), 10);
        for (final S t : items) {
            action.accept(t);
        }
    }

    default <S extends Position<T>> List<S> toList(@NonNull final PositionIterator<S> iterator, final int estimatedSize) {
        if (estimatedSize < 1) {
            throw new IllegalArgumentException("Estimated size must be greater than 0");
        }
        final List<S> list = new ArrayList<>(estimatedSize);
        while (iterator.hasNext()) {
            list.add((S) iterator.nextPosition());
        }
        return list;
    }
}
