package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Custom priority queue empty {@link RuntimeException} implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class PriorityQueueEmptyException extends RuntimeException {

    public PriorityQueueEmptyException(final String message) {
        super(message);
    }

    public PriorityQueueEmptyException(final Throwable cause) {
        super(cause);
    }

    public PriorityQueueEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
