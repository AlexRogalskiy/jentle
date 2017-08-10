package com.wildbeeslabs.jentle.collections.interfaces;

/**
 *
 * Custom graph interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IGraph<T> {

    public void add(int from, int to, final T data);

}
