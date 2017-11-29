package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * Custom EmptyStackException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EmptyStackException extends Exception {

    public EmptyStackException(final String message) {
        super(message);
    }

    public EmptyStackException(final Throwable cause) {
        super(cause);
    }

    public EmptyStackException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
