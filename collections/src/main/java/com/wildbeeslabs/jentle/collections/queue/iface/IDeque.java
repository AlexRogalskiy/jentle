package com.wildbeeslabs.jentle.collections.queue.iface;

import java.util.Deque;

/**
 * Custom {@link IQueue} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IDeque<T> extends IQueue<T>, Deque<T> {
}
