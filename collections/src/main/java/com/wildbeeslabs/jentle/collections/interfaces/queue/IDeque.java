package com.wildbeeslabs.jentle.collections.interfaces.queue;

import java.util.Deque;

/**
 * Custom deque interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IDeque<T> extends IQueue<T>, Deque<T> {
}
