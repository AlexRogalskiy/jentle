package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.algorithms.string.HttpString;

/**
 * Protocol version strings.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@Deprecated
public final class Protocols {

    private Protocols() {
    }

    /**
     * HTTP 0.9.
     */
    public static final String HTTP_0_9_STRING = "HTTP/0.9";
    /**
     * HTTP 1.0.
     */
    public static final String HTTP_1_0_STRING = "HTTP/1.0";
    /**
     * HTTP 1.1.
     */
    public static final String HTTP_1_1_STRING = "HTTP/1.1";
    /**
     * HTTP 1.1.
     */
    public static final String HTTP_2_0_STRING = "HTTP/2.0";


    public static final HttpString HTTP_0_9 = new HttpString(HTTP_0_9_STRING);
    /**
     * HTTP 1.0.
     */
    public static final HttpString HTTP_1_0 = new HttpString(HTTP_1_0_STRING);
    /**
     * HTTP 1.1.
     */
    public static final HttpString HTTP_1_1 = new HttpString(HTTP_1_1_STRING);
    /**
     * HTTP 2.0.
     */
    public static final HttpString HTTP_2_0 = new HttpString(HTTP_2_0_STRING);

}
