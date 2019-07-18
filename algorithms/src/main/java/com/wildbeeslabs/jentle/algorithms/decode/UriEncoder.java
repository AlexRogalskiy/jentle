package com.wildbeeslabs.jentle.algorithms.decode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public abstract class UriEncoder {
    private static final CharsetDecoder UTF8Decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPORT);
    private static final CharsetEncoder UTF8Encoder = Charset.forName("UTF-8").newEncoder().onMalformedInput(CodingErrorAction.REPORT);

    /**
     * Escape special characters with '%'
     *
     * @param uri URI to be escaped
     * @return encoded URI
     */
    public static String encode(final String uri) {
        return URLEncoder.encode(uri, StandardCharsets.UTF_8);
    }

    /**
     * Decode '%'-escaped characters. Decoding fails in case of invalid UTF-8
     *
     * @param buff data to decode
     * @return decoded data
     * @throws CharacterCodingException if cannot be decoded
     */
    public static String decode(final ByteBuffer buff) throws CharacterCodingException {
        final CharBuffer chars = UTF8Decoder.decode(buff);
        return chars.toString();
    }

    public static String decode(final String buff) {
        try {
            return URLDecoder.decode(buff, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String encode(final CharBuffer buff) throws CharacterCodingException {
        final ByteBuffer chars = UTF8Encoder.encode(buff);
        return chars.toString();
    }
}
