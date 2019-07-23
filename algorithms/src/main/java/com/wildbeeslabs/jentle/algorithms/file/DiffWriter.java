package com.wildbeeslabs.jentle.algorithms.file;

import java.io.IOException;

/**
 * @author torgeir
 */
public interface DiffWriter {
    void addCopy(int offset, int length) throws IOException;

    void addData(byte b) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
