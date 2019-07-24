package com.wildbeeslabs.jentle.algorithms.exception;

/**
 * Exception while serialize/deserialize java objects
 */
public class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
