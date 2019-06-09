package com.wildbeeslabs.jentle.algorithms.file;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author Yvonne Wang
 * @author Olivier Michallat
 */
@UtilityClass
public class TextFileWriter {

    private static final TextFileWriter INSTANCE = new TextFileWriter();

    public static TextFileWriter instance() {
        return INSTANCE;
    }

    public void write(final File file, final String... content) throws IOException {
        write(file, Charset.defaultCharset(), content);
    }

    public void write(final File file, final Charset charset, final String... content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
            for (final String line : content) {
                writer.println(line);
            }
        }
    }
}
