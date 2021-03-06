package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Versions of TLS that can be offered when negotiating a secure socket. See {@link
 * javax.net.ssl.SSLSocket#setEnabledProtocols}.
 */
@Getter
@RequiredArgsConstructor
public enum TlsVersion {
    TLS_1_3("TLSv1.3"), // 2016.
    TLS_1_2("TLSv1.2"), // 2008.
    TLS_1_1("TLSv1.1"), // 2006.
    TLS_1_0("TLSv1"),   // 1999.
    SSL_3_0("SSLv3");   // 1996.

    private final String javaName;

    public static TlsVersion forJavaName(final String javaName) {
        switch (javaName) {
            case "TLSv1.3":
                return TLS_1_3;
            case "TLSv1.2":
                return TLS_1_2;
            case "TLSv1.1":
                return TLS_1_1;
            case "TLSv1":
                return TLS_1_0;
            case "SSLv3":
                return SSL_3_0;
        }
        throw new IllegalArgumentException("Unexpected TLS version: " + javaName);
    }

    static List<TlsVersion> forJavaNames(final String... tlsVersions) {
        final List<TlsVersion> result = new ArrayList<>(tlsVersions.length);
        for (final String tlsVersion : tlsVersions) {
            result.add(forJavaName(tlsVersion));
        }
        return Collections.unmodifiableList(result);
    }
}
