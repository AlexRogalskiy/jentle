package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class SearchException extends RuntimeException {

    private static final long serialVersionUID = -7092886778226268686L;

    public SearchException(final String message) {
        super(message);
    }

    public SearchException(final Throwable cause) {
        super(cause);
    }

    public SearchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
