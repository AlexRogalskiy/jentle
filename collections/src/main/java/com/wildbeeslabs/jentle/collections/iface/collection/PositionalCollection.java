package com.wildbeeslabs.jentle.collections.iface.collection;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterable;

/**
 * Custom positino {@link CollectionLike} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionalCollection<T> extends CollectionLike<T>, PositionIterable<T> {
}
