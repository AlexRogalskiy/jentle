package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Search {@link RuntimeException} implementation
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchException extends RuntimeException {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -7092886778226268686L;

    /**
     * Search exception constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public SearchException(final String message) {
        super(message);
    }

    /**
     * Search exception constructor with initial input target {@link Throwable}
     *
     * @param cause - initial input target {@link Throwable}
     */
    public SearchException(final Throwable cause) {
        super(cause);
    }

    /**
     * Search exception constructor with initial input message and target {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input target {@link Throwable}
     */
    public SearchException(final String message, final Throwable cause) {
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
