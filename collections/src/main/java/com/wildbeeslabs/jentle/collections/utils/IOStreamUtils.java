package com.wildbeeslabs.jentle.collections.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@UtilityClass
public class IOStreamUtils {

    public static String toString(final Reader in)
        throws IOException {
        StringBuilder sbuf = new StringBuilder();
        char[] buffer = new char[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            sbuf.append(buffer, 0, len);
        }

        return sbuf.toString();
    }

    public static void copy(final Reader in, final Writer out)
        throws IOException {
        char[] buffer = new char[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
    }

    /**
     * Creates a reader that will return -1 after <code>len</code>
     * chars have been read.
     */
    public static Reader limit(final Reader in, long len) {
        return new LimitedReader(in, len);
    }

    public static Reader splice(final Reader one, Reader two) {
        return new SpliceReader(one, two);
    }

    private static class SpliceReader extends Reader {
        private Reader one;
        private Reader two;
        private boolean oneFinished;

        public SpliceReader(final Reader one, final Reader two) {
            this.one = one;
            this.two = two;
        }

        @Override
        public void close()
            throws IOException {
            this.one.close();
            this.two.close();
        }

        @Override
        public int read()
            throws IOException {
            while (true) {
                if (oneFinished) {
                    return two.read();
                } else {
                    int value = one.read();
                    if (value == -1) {
                        oneFinished = true;
                    } else {
                        return value;
                    }
                }
            }
        }

        @Override
        public int read(final char[] buf, int start, int len)
            throws IOException {
            while (true) {
                if (this.oneFinished) {
                    return two.read(buf, start, len);
                } else {
                    int value = one.read(buf, start, len);
                    if (value == -1) {
                        this.oneFinished = true;
                    } else {
                        return value;
                    }

                }
            }
        }

        @Override
        public int read(final char[] buf)
            throws IOException {
            while (true) {
                if (oneFinished) {
                    return two.read(buf);
                } else {
                    int value = one.read(buf);
                    if (value == -1) {
                        oneFinished = true;
                    } else {
                        return value;
                    }
                }
            }
        }
    }

    private static class LimitedReader extends Reader {
        private Reader in;
        private final long maxLen;
        private long lenread;

        public LimitedReader(final Reader in, long len) {
            this.in = in;
            this.maxLen = len;
        }

        @Override
        public void close() {
        }

        @Override
        public int read()
            throws IOException {
            if (this.lenread < this.maxLen) {
                this.lenread++;
                return this.in.read();
            }
            return -1;
        }

        @Override
        public int read(final char[] buf, int start, int len)
            throws IOException {
            if (this.lenread < this.maxLen) {
                int numAllowedToRead = (int) Math.min(this.maxLen - this.lenread, len);
                int count = this.in.read(buf, start, numAllowedToRead);
                this.lenread += count;
                return count;
            }
            return -1;
        }

        @Override
        public int read(final char[] buf) throws IOException {
            return read(buf, 0, buf.length);
        }
    }
}
