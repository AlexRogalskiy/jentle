package com.wildbeeslabs.jentle.algorithms.exception;

/**
 * A <code>NotSupportedException</code> is thrown to indicate that
 * callee (resource adapter
 * or application server for system contracts) cannot execute an operation
 * because the operation is not a supported feature. For example, if the
 * transaction support level for a resource adapter is
 * <code>NO_TRANSACTION</code>, an invocation of <code>getXAResource</code>
 * method on a <code>ManagedConnection</code> object should throw a
 * <code>NotSupportedException</code> exception.
 *
 * @author Rahul Sharma
 * @author Ram Jeyaraman
 * @version 1.0
 */
public class NotSupportedException extends RuntimeException {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 3793276598737045919L;

    /**
     * Constructs a new instance with null as its detail message.
     */
    public NotSupportedException() {
        super();
    }

    /**
     * Constructs a new instance with the specified detail message.
     *
     * @param message the detail message.
     */
    public NotSupportedException(String message) {
        super(message);
    }

    /**
     * Constructs a new throwable with the specified cause.
     *
     * @param cause a chained exception of type <code>Throwable</code>.
     */
    public NotSupportedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new throwable with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   a chained exception of type <code>Throwable</code>.
     */
    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
