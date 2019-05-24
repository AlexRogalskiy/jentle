package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Custom invalid parameter {@link RuntimeException} implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class InvalidParameterException extends RuntimeException {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    public static final long serialVersionUID = -7191894726440302646L;

    /**
     * Invalid parameter exception constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public InvalidParameterException(final String message) {
        super(message);
    }

    /**
     * Invalid parameter exception constructor with initial input target {@link Throwable}
     *
     * @param cause - initial input target {@link Throwable}
     */
    public InvalidParameterException(final Throwable cause) {
        super(cause);
    }

    /**
     * Invalid parameter exception constructor with initial input message and target {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input target {@link Throwable}
     */
    public InvalidParameterException(final String message, final Throwable cause) {
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
