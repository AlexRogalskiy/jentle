package com.wildbeeslabs.jentle.collections.list;

import java.util.Iterator;

/**
 * A LinkedListIterator
 *
 * This iterator allows the last element to be repeated in the next call to hasNext or next
 */
public interface LinkedListIterator<E> extends Iterator<E>, AutoCloseable {

   void repeat();

   @Override
   void close();
}
