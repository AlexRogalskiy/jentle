package com.wildbeeslabs.jentle.algorithms.file;

import java.io.*;

public class FileResource extends Resource {
    private final File file;

    public FileResource(final File file) {
        super(file.getName());
        this.file = file;
    }

    public boolean canBeOpened() {
        return file.exists();
    }

    protected InputStream openStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public String toString() {
        return "File: " + file;
    }
}
