package com.wildbeeslabs.jentle.collections.list;

public interface LinkedList<E> {

   void addHead(E e);

   void addTail(E e);

   E poll();

   LinkedListIterator<E> iterator();

   void clear();

   int size();
}
