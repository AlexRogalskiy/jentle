package com.wildbeeslabs.jentle.collections.image;

/**
 * <p>Thrown when an exception occurs during Reed-Solomon decoding, such as when
 * there are too many errors to correct.</p>
 *
 * @author Sean Owen
 * @since 5.0.2
 */
public final class ReedSolomonException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2168232776886684292L;

    public ReedSolomonException(String message) {
        super(message);
    }

}
