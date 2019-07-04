package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Enumeration of possible values of property {@link org.glassfish.jersey.server.ServerProperties#TRACING}.
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
     * Tracing support is in stand-by mode. Waiting for a request header
     * {@link org.glassfish.jersey.message.internal.TracingLogger#HEADER_ACCEPT} existence.
     */
    ON_DEMAND,
    /**
     * Tracing support is enabled for every request.
     */
    ALL
}
