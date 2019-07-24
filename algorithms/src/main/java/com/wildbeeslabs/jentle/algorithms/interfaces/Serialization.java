package com.wildbeeslabs.jentle.algorithms.interfaces;

import com.wildbeeslabs.jentle.algorithms.exception.SerializationException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Defines the contract for platform serialization implementations.
 */
public interface Serialization {
    /**
     * Serialize object {@code object} to stream {@code os}.
     * Stream isn't closed automatically after serialization.
     * User code should be close stream correctly
     *
     * @param object object
     * @param os     output stream
     * @throws SerializationException in case of serialization problems
     */
    void serialize(Object object, OutputStream os);

    /**
     * Deserialize object from stream
     *
     * @throws SerializationException in case of serialization problems
     */
    Object deserialize(InputStream is);

    /**
     * Serialize object to byte array
     *
     * @throws SerializationException in case of serialization problems
     */
    byte[] serialize(Object object);

    /**
     * Deserialize object from byte array
     *
     * @throws SerializationException in case of serialization problems
     */
    Object deserialize(byte[] bytes);
}
