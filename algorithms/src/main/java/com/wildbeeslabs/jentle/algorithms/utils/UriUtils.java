package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.net.URI;

/**
 * Utility methods for URI encoding and decoding based on RFC 3986.
 *
 * <p>There are two types of encode methods:
 * <ul>
 * <li>{@code "encodeXyz"} -- these encode a specific URI component (e.g. path,
 * query) by percent encoding illegal characters, which includes non-US-ASCII
 * characters, and also characters that are otherwise illegal within the given
 * URI component type, as defined in RFC 3986. The effect of this method, with
 * regards to encoding, is comparable to using the multi-argument constructor
 * of {@link URI}.
 * <li>{@code "encode"} and {@code "encodeUriVariables"} -- these can be used
 * to encode URI variable values by percent encoding all characters that are
 * either illegal, or have any reserved meaning, anywhere within a URI.
 * </ul>
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Rossen Stoyanchev
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 * @since 3.0
 */
@UtilityClass
public class UriUtils {

    /**
     * Extract the file extension from the given URI path.
     *
     * @param path the URI path (e.g. "/products/index.html")
     * @return the extracted file extension (e.g. "html")
     * @since 4.3.2
     */
    public static String extractFileExtension(String path) {
        int end = path.indexOf('?');
        int fragmentIndex = path.indexOf('#');
        if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
            end = fragmentIndex;
        }
        if (end == -1) {
            end = path.length();
        }
        int begin = path.lastIndexOf('/', end) + 1;
        int paramIndex = path.indexOf(';', begin);
        end = (paramIndex != -1 && paramIndex < end ? paramIndex : end);
        int extIndex = path.lastIndexOf('.', end);
        if (extIndex != -1 && extIndex > begin) {
            return path.substring(extIndex + 1, end);
        }
        return null;
    }

}
