package com.wildbeeslabs.jentle.algorithms.io;

/**
 * Exception indicating that a deadlock has been detected while a thread was attempting to acquire a lock. This
 * typically happens when a Thread attempts to acquire a lock that is owned by a Thread that is in turn waiting for a
 * lock held by the current thread.
 * <p/>
 * It is typically safe to retry the operation when this exception occurs.
 *
 * @author Allard Buijze
 * @since 2.0
 */
public class DeadlockException extends LockAcquisitionFailedException {

    private static final long serialVersionUID = 3135654948624383571L;

    /**
     * Initializes the exception with given {@code message}.
     *
     * @param message The message describing the exception
     */
    public DeadlockException(final String message) {
        super(message);
    }
}
