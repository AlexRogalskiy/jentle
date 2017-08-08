package com.wildbeeslabs.jentle.collections.exception;

/**
 *
 * Custom EmptyQueueException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class EmptyQueueException extends Exception {

    public EmptyQueueException() {
    }

    public EmptyQueueException(String message) {
        super(message);
    }

    public EmptyQueueException(Throwable cause) {
        super(cause);
    }

    public EmptyQueueException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
