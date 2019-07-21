package com.wildbeeslabs.jentle.algorithms.io;

/**
 * Exception indicating that a lock could not be obtained. Typically, operations failing with this exception can be
 * retried without any intervention.
 *
 * @author Allard Buijze
 * @since 2.0
 */
public class LockAcquisitionFailedException extends RuntimeException {

    private static final long serialVersionUID = 1660239819606946007L;

    /**
     * Initialize the exception with given {@code message} and {@code cause}
     *
     * @param message The message describing the exception
     * @param cause   The underlying cause of the exception
     */
    public LockAcquisitionFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Initialize the exception with given {@code message}.
     *
     * @param message The message describing the exception
     */
    public LockAcquisitionFailedException(final String message) {
        super(message);
    }
}
