package com.wildbeeslabs.jentle.collections.iface.position;

import com.wildbeeslabs.jentle.collections.iface.collection.IContainer;
import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;

/**
 * Custom positional {@link IContainer} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionContainer<T> extends IContainer<T> {

    PositionIterator<T> positionIterator();
}
