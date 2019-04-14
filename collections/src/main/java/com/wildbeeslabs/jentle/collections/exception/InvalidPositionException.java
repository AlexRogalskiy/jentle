package com.wildbeeslabs.jentle.collections.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Custom invalid position {@link RuntimeException} implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class InvalidPositionException extends RuntimeException {

    public InvalidPositionException(final String message) {
        super(message);
    }

    public InvalidPositionException(final Throwable cause) {
        super(cause);
    }

    public InvalidPositionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
