package com.wildbeeslabs.jentle.collections.set;

import java.util.Set;

/**
 * A ConcurrentSet
 *
 * @param <E> The generic class
 */
public interface ConcurrentSet<E> extends Set<E> {

   boolean addIfAbsent(E o);
}
