package com.wildbeeslabs.jentle.algorithms.writer;

/**
 * Writer with the same methods as in {@link java.io.Writer} but without throwing IOExceptions
 */
public interface StreamDataWriter {

    /**
     * Flushes this stream by writing any buffered output to the underlying stream.
     */
    default void flush() {
    }

    void write(final String str);

    void write(final String str, int off, int len);
}
