package com.wildbeeslabs.jentle.collections.exception;

/**
 *
 * Custom EmptyListException implementation
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class EmptyListException extends Exception {
    public EmptyListException() {}

    public EmptyListException (String message) {
        super(message);
    }

    public EmptyListException (Throwable cause) {
        super(cause);
    }

    public EmptyListException (String message, Throwable cause) {
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
