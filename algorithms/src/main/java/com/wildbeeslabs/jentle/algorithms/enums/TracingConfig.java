package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Enumeration of possible values of property.
 *
 * @author Libor Kramolis (libor.kramolis at oracle.com)
 * @since 2.3
 */
public enum TracingConfig {
    /**
     * Tracing support is completely disabled.
     */
    OFF,
    /**
     * Tracing support is in stand-by mode. Waiting for a request header existence.
     */
    ON_DEMAND,
    /**
     * Tracing support is enabled for every request.
     */
    ALL
}
