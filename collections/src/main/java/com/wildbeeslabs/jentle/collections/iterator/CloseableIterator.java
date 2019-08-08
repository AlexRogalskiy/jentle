package com.wildbeeslabs.jentle.collections.iterator;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Iterators that need to be closed in order to release resources should implement this interface.
 * <p>
 * Warning: before implementing this interface, consider if there are better options. The chance of misuse is
 * a bit high since people are used to iterating without closing.
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {

    void close();
}
