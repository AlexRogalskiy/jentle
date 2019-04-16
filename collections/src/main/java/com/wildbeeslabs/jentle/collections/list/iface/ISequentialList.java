package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.array.iface.IVector;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom sequence {@link IPositionalList} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ISequentialList<T, E extends ACPositionalListNode<T, E>> extends IPositionalList<T, E>, IVector<T> {

    Position<T> atRank(int rank);

    int rankOf(final Position<T> position);
}
