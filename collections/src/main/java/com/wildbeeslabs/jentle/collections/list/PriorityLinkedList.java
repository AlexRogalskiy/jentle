package com.wildbeeslabs.jentle.collections.list;

/**
 * A type of linked list which maintains items according to a priority
 * and allows adding and removing of elements at both ends, and peeking
 */
public interface PriorityLinkedList<T> {

   void addHead(T t, int priority);

   void addTail(T t, int priority);

   T poll();

   void clear();

   int size();

   LinkedListIterator<T> iterator();

   boolean isEmpty();
}
