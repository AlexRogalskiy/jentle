package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Defines the supported mail protocols and default ports.
 */
@Getter
@RequiredArgsConstructor
public enum Protocol {
    /**
     * SMTP
     */
    SMTP(25),
    /**
     * Secure SMTP
     */
    SMTPS(465),
    /**
     * POP3
     */
    POP3(110),
    /**
     * Secure POP3
     */
    POP3S(995),
    /**
     * IMAP
     */
    IMAP(143),
    /**
     * Secure IMAP
     */
    IMAPS(993);

    /**
     * The default port.
     */
    private final int port;

    /**
     * Finds the protocol by its default port.
     *
     * @param pPort the default port.
     * @return the protocol.
     */
    static Protocol findByPort(final int pPort) {
        for (final Protocol p : values()) {
            if (pPort == p.port) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown port " + pPort + ", supported ports are " + Arrays.toString(values()));
    }

    @Override
    public String toString() {
        return name() + '(' + Integer.toString(port) + ')';
    }
}
