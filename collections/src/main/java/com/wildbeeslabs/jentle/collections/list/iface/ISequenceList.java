package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.iface.IPosition;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom sequence {@link IPositionList} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ISequenceList<T, E extends ACPositionalListNode<T, E>> extends IPositionList<T, E> {

    IPosition<T> atRank(int rank);

    T elemAtRank(int rank);

    int rankOf(final IPosition<T> position);

    void insertAtRank(int rank, final T value);

    T removeAtRank(int rank);

    T replaceAtRank(int rank, final T value);
}
