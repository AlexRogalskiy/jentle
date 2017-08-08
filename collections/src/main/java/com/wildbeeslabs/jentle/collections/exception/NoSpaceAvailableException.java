package com.wildbeeslabs.jentle.collections.exception;

/**
 *
 * Custom NoSpaceAvailableException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class NoSpaceAvailableException extends Exception {

    public NoSpaceAvailableException() {
    }

    public NoSpaceAvailableException(String message) {
        super(message);
    }

    public NoSpaceAvailableException(Throwable cause) {
        super(cause);
    }

    public NoSpaceAvailableException(String message, Throwable cause) {
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
