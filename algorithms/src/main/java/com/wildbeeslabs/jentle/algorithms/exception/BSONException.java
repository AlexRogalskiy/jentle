package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Bson {@link RuntimeException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BSONException extends RuntimeException {

    private static final long serialVersionUID = -315953634851345999L;

    private final Integer errorCode;

    /**
     * @param msg The error message.
     */
    public BSONException(final String msg) {
        super(msg);
        this.errorCode = null;
    }

    /**
     * @param errorCode The error code.
     * @param msg       The error message.
     */
    public BSONException(final Integer errorCode, final String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    /**
     * @param msg The error message.
     * @param t   The throwable cause.
     */
    public BSONException(final String msg, final Throwable t) {
        super(msg, t);
        this.errorCode = null;
    }

    /**
     * @param errorCode The error code.
     * @param msg       The error message.
     * @param t         The throwable cause.
     */
    public BSONException(final Integer errorCode, final String msg, final Throwable t) {
        super(msg, t);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code.
     *
     * @return The error code.
     */
    public Integer getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns if the error code is set (i.e., not null).
     *
     * @return true if the error code is not null.
     */
    public boolean hasErrorCode() {
        return Objects.nonNull(this.errorCode);
    }
}
