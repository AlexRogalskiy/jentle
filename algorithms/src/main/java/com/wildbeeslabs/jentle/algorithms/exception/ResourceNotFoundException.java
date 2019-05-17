package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Resource not found {@link RuntimeException} implementation
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 649598609369331140L;

    /**
     * Resource not found exception constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Resource not found exception constructor with initial input target {@link Throwable}
     *
     * @param cause - initial input target {@link Throwable}
     */
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * Resource not found exception constructor with initial input message and target {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input target {@link Throwable}
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
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
}
