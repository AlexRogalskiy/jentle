package com.wildbeeslabs.jentle.collections.queue.iface;

import java.io.Closeable;
import java.io.IOException;

/**
 * Implemented by closeable objects which might be able to report
 * an error when closed due to exceptional conditions.
 */
public interface ExceptionallyCloseable extends Closeable {

    /**
     * Called when an instance of {@code ExceptionallyCloseable} is closed
     * due to some exceptional condition revealed by {@code cause}.
     *
     * @param cause The reason for which the object is closed.
     * @throws IOException if {@link #close()} fails.
     * @implSpec The default implementation of this method simply calls
     * {@link #close()}. Implementation of this interface are
     * suppose to override this method in order to ensure that
     * the cause is properly reported.
     */
    public default void closeExceptionally(Throwable cause) throws IOException {
        close();
    }

    public static void close(Throwable t, Closeable c) throws IOException {
        if (c instanceof ExceptionallyCloseable) {
            ((ExceptionallyCloseable) c).closeExceptionally(t);
        } else {
            c.close();
        }
    }
}
