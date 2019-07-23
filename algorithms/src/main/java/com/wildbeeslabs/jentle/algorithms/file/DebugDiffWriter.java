package com.wildbeeslabs.jentle.algorithms.file;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
public class DebugDiffWriter implements DiffWriter {

    private final byte buf[] = new byte[256];
    private int buflen = 0;

    public void addCopy(int offset, int length) {
        if (buflen > 0)
            writeBuf();
        System.err.println("COPY off: " + offset + ", len: " + length);
    }

    public void addData(byte b) {
        if (buflen < 256)
            buf[buflen++] = b;
        else
            writeBuf();
    }

    private void writeBuf() {
        System.err.print("DATA: ");
        for (int ix = 0; ix < buflen; ix++) {
            if (buf[ix] == '\n')
                System.err.print("\\n");
            else
                System.err.print(String.valueOf((char) ((char) buf[ix])));
            //System.err.print("0x" + Integer.toHexString(buf[ix]) + " "); // hex output
        }
        System.err.println(StringUtils.EMPTY);
        buflen = 0;
    }

    public void flush() {
    }

    public void close() {
    }
}
