package com.wildbeeslabs.jentle.collections.exception;

/**
 *
 * Custom EmptyStackException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class EmptyStackException extends Exception {

    public EmptyStackException() {
    }

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(Throwable cause) {
        super(cause);
    }

    public EmptyStackException(String message, Throwable cause) {
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
