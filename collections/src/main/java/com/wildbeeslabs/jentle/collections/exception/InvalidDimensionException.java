package com.wildbeeslabs.jentle.collections.exception;

/**
 *
 * Custom InvalidDimensionException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class InvalidDimensionException extends Exception {

    public InvalidDimensionException() {
    }

    public InvalidDimensionException(String message) {
        super(message);
    }

    public InvalidDimensionException(Throwable cause) {
        super(cause);
    }

    public InvalidDimensionException(String message, Throwable cause) {
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