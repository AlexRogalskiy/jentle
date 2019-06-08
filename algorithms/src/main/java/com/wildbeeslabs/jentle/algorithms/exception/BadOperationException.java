package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Bad operation {@link RuntimeException} implementation
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BadOperationException extends RuntimeException {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -1529056019925871290L;

    /**
     * File upload exception constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public BadOperationException(final String message) {
        super(message);
    }

    /**
     * File upload exception constructor with initial input {@link Throwable}
     *
     * @param cause - initial input {@link Throwable}
     */
    public BadOperationException(final Throwable cause) {
        super(cause);
    }

    /**
     * File upload exception constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input {@link Throwable}
     */
    public BadOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns description message {@link String}
     *
     * @return description message {@link String}
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Returns {@link BadOperationException} by input parameters
     *
     * @param message - initial input message
     * @return {@link BadOperationException}
     */
    public static final BadOperationException throwBadOperation(final String message) {
        throw new BadOperationException(message);
    }

    /**
     * Returns {@link BadOperationException} by input parameters
     *
     * @param message - initial input message {@link String}
     * @param message - initial input source target {@link Throwable}
     * @return {@link BadOperationException}
     */
    public static final BadOperationException throwBadOperation(final String message, final Throwable throwable) {
        throw new BadOperationException(message, throwable);
    }
}
