package com.wildbeeslabs.jentle.algorithms.toolset;

/**
 * Exception indicating that a predefined property is not accessible. Generally, this means that the property does not
 * conform to the accessibility requirements of the accessor.
 *
 * @author Maxim Fedorov
 * @author Allard Buijze
 * @since 2.0
 */
public class PropertyAccessException extends RuntimeException {

    private static final long serialVersionUID = 8870783603420157710L;

    /**
     * Initializes the PropertyAccessException with given {@code message} and {@code cause}.
     *
     * @param message The message describing the cause
     * @param cause   The underlying cause of the exception
     */
    public PropertyAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
