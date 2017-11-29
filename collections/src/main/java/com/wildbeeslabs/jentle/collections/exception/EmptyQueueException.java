package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * Custom EmptyQueueException implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EmptyQueueException extends Exception {

    public EmptyQueueException(final String message) {
        super(message);
    }

    public EmptyQueueException(final Throwable cause) {
        super(cause);
    }

    public EmptyQueueException(final String message, final Throwable cause) {
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
