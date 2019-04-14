package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Custom empty container {@link RuntimeException} implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class EmptyContainerException extends RuntimeException {

    public EmptyContainerException(final String message) {
        super(message);
    }

    public EmptyContainerException(final Throwable cause) {
        super(cause);
    }

    public EmptyContainerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
