package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods related to URLs.
 *
 * @author Turbo87
 * @author dorzey
 */
@UtilityClass
public class URLs {

    /**
     * Loads the text content of a URL into a character string.
     *
     * @param url         the URL.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws IllegalArgumentException if the given character set is not supported on this platform.
     * @throws UncheckedIOException     if an I/O exception occurs.
     */
    public static String contentOf(URL url, String charsetName) {
        checkArgumentCharsetIsSupported(charsetName);
        return contentOf(url, Charset.forName(charsetName));
    }

    /**
     * Loads the text content of a URL into a character string.
     *
     * @param url     the URL.
     * @param charset the character set to use.
     * @return the content of the URL.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static String contentOf(URL url, Charset charset) {
        checkNotNull(charset, "The charset should not be null");
        try {
            return loadContents(url.openStream(), charset);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + url, e);
        }
    }

    /**
     * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param url     the URL.
     * @param charset the character set to use.
     * @return the content of the URL.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static List<String> linesOf(URL url, Charset charset) {
        checkNotNull(charset, "The charset should not be null");
        try {
            return loadLines(url.openStream(), charset);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + url, e);
        }
    }

    /**
     * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param url         the URL.
     * @param charsetName the name of the character set to use.
     * @return the content of the URL.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static List<String> linesOf(URL url, String charsetName) {
        checkArgumentCharsetIsSupported(charsetName);
        return linesOf(url, Charset.forName(charsetName));
    }

    private static String loadContents(InputStream stream, Charset charset) throws IOException {
        try (StringWriter writer = new StringWriter();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset))) {
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            return writer.toString();
        }
    }

    private static List<String> loadLines(InputStream stream, Charset charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset))) {
            List<String> strings = Lists.newArrayList();
            String line = reader.readLine();
            while (line != null) {
                strings.add(line);
                line = reader.readLine();
            }
            return strings;
        }
    }

    private static void checkArgumentCharsetIsSupported(String charsetName) {
        checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
    }
}
