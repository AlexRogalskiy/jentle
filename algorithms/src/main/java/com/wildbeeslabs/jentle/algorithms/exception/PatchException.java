package com.wildbeeslabs.jentle.algorithms.exception;

/**
 * @author Heiko Klein
 */
public class PatchException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>PatchException</code> without detail message.
     */
    public PatchException() {
    }


    /**
     * Constructs an instance of <code>PatchException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PatchException(String msg) {
        super(msg);
    }
}
