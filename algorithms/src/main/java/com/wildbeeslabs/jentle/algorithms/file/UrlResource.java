package com.wildbeeslabs.jentle.algorithms.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlResource extends Resource {
    private static final String CLASSPATH_PREFIX = "classpath:";

    private static final String SLASH_PREFIX = "/";

    private final URL url;

    private boolean isMissingClasspathResource = false;

    private String classpathUrl;

    public UrlResource(final String url) throws IllegalArgumentException {
        super(url);
        if (url.startsWith(CLASSPATH_PREFIX)) {
            String resource = url.substring(CLASSPATH_PREFIX.length());
            if (resource.startsWith(SLASH_PREFIX)) {
                resource = resource.substring(1);
            }

            this.url = getClass().getClassLoader().getResource(resource);
            if (this.url == null) {
                this.isMissingClasspathResource = true;
                this.classpathUrl = url;
            }
        } else {
            try {
                this.url = new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(String.format("Badly formed URL %s - %s", url, e.getMessage()));
            }
        }
    }

    public boolean canBeOpened() {
        if (this.isMissingClasspathResource) {
            return false;
        }
        try {
            this.openStream();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    protected InputStream openStream() throws IOException {
        return new BufferedInputStream(this.url.openStream());
    }

    public String toString() {
        if (!this.isMissingClasspathResource) {
            return "URL " + this.url.toString();
        }
        return this.classpathUrl;
    }
}
