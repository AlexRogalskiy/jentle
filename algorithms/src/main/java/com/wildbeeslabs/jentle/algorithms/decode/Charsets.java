package com.wildbeeslabs.jentle.algorithms.decode;

import java.nio.charset.Charset;

public final class Charsets {

    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final Charset DEFAULT_CHARSET = US_ASCII;
}
