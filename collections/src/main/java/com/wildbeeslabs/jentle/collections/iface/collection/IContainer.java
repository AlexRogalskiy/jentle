package com.wildbeeslabs.jentle.collections.iface.collection;

/**
 * Custom container {@link ICollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IContainer<T> extends ICollection<T> {

    int size();

    boolean isEmpty();
}
