package com.wildbeeslabs.jentle.collections.iface.position;

import com.wildbeeslabs.jentle.collections.iface.collection.ICollection;
import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterable;

/**
 * Custom positino {@link ICollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalCollection<T> extends ICollection<T>, PositionIterable<T> {
}
