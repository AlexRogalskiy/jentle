package com.wildbeeslabs.jentle.algorithms.writer;

import java.io.StringWriter;

public class YamlStringWriterStream implements StreamDataWriter {
    final StringWriter writer = new StringWriter();

    @Override
    public void write(final String str) {
        this.writer.write(str);
    }

    @Override
    public void write(final String str, int off, int len) {
        this.writer.write(str, off, len);
    }

    public String getString() {
        return this.writer.toString();
    }
}
