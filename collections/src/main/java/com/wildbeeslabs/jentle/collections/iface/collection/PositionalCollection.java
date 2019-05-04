package com.wildbeeslabs.jentle.collections.iface.collection;

import com.wildbeeslabs.jentle.collections.iface.collection.Collection;
import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterable;

/**
 * Custom positino {@link Collection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionalCollection<T> extends Collection<T>, PositionIterable<T> {
}
