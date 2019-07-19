package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * General utilities for SSLContext.
 *
 * @since 3.0
 */
@UtilityClass
public class SSLContextUtils {

    /**
     * Create and initialise an SSLContext.
     *
     * @param protocol     the protocol used to instatiate the context
     * @param keyManager   the key manager, may be {@code null}
     * @param trustManager the trust manager, may be {@code null}
     * @return the initialised context.
     * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
     */
    public static SSLContext createSSLContext(final String protocol, final KeyManager keyManager, final TrustManager trustManager) throws IOException {
        return createSSLContext(protocol,
            keyManager == null ? null : new KeyManager[]{keyManager},
            trustManager == null ? null : new TrustManager[]{trustManager});
    }

    /**
     * Create and initialise an SSLContext.
     *
     * @param protocol      the protocol used to instatiate the context
     * @param keyManagers   the ArrayUtils of key managers, may be {@code null} but ArrayUtils entries must not be {@code null}
     * @param trustManagers the ArrayUtils of trust managers, may be {@code null} but ArrayUtils entries must not be {@code null}
     * @return the initialised context.
     * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
     */
    public static SSLContext createSSLContext(final String protocol, final KeyManager[] keyManagers, final TrustManager[] trustManagers)
        throws IOException {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance(protocol);
            ctx.init(keyManagers, trustManagers, /*SecureRandom*/ null);
        } catch (GeneralSecurityException e) {
            IOException ioe = new IOException("Could not initialize SSL context");
            ioe.initCause(e);
            throw ioe;
        }
        return ctx;
    }
}
