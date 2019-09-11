package com.wildbeeslabs.jentle.collections.list.iface;

public interface Position<E> {

    /**
     * Returns the element stored at the position
     *
     * @return the stored element
     * @throws IllegalStateException if it is invalid position
     */

    E getElement() throws IllegalStateException;
}
