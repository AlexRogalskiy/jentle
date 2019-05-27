/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.logger;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.CRC32;

/**
 * Log archiver utilities implementation
 */
@Slf4j
@UtilityClass
public class CLogArchiverUtils {

    private static final String ZIP_ENCODING = "CP866";
    public static final long LOG_TAIL_FOR_PACKING_SIZE = 20 * 1024 * 1024; // 20 MB

    public static void writeArchivedLogTailToStream(File logFile, OutputStream outputStream) throws IOException {
        if (!logFile.exists()) {
            throw new FileNotFoundException();
        }

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.setEncoding(ZIP_ENCODING);

        byte[] content = getTailBytes(logFile);

        ArchiveEntry archiveEntry = newTailArchive(logFile.getName(), content);
        zipOutputStream.putArchiveEntry(archiveEntry);
        zipOutputStream.write(content);

        zipOutputStream.closeArchiveEntry();
        zipOutputStream.close();
    }

    public static void writeArchivedLogToStream(File logFile, OutputStream outputStream) throws IOException {
        if (!logFile.exists()) {
            throw new FileNotFoundException();
        }

        File tempFile = File.createTempFile(FilenameUtils.getBaseName(logFile.getName()) + "_log_", ".zip");

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(tempFile);
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.setEncoding(ZIP_ENCODING);

        ArchiveEntry archiveEntry = newArchive(logFile);
        zipOutputStream.putArchiveEntry(archiveEntry);

        FileInputStream logFileInput = new FileInputStream(logFile);
        IOUtils.copyLarge(logFileInput, zipOutputStream);

        logFileInput.close();

        zipOutputStream.closeArchiveEntry();
        zipOutputStream.close();

        FileInputStream tempFileInput = new FileInputStream(tempFile);
        IOUtils.copyLarge(tempFileInput, outputStream);

        tempFileInput.close();

        FileUtils.forceDelete(tempFile);
    }

    private static byte[] getTailBytes(File logFile) throws FileNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buf = null;
        int len;
        int size = 1024;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r");
            long lengthFile = randomAccessFile.length();
            if (lengthFile >= LOG_TAIL_FOR_PACKING_SIZE) {
                randomAccessFile.seek(lengthFile - LOG_TAIL_FOR_PACKING_SIZE);
                skipFirstLine(randomAccessFile);
            }
            buf = new byte[size];
            while ((len = randomAccessFile.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        } catch (IOException e) {
            log.error("Unable to get tail for log file " + logFile.getName(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
        return buf;
    }

    private static ArchiveEntry newTailArchive(String name, byte[] tail) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(tail.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(tail);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    private static ArchiveEntry newArchive(File file) throws IOException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(file.getName());
        zipEntry.setSize(file.length());
        zipEntry.setCompressedSize(zipEntry.getSize());
        zipEntry.setCrc(FileUtils.checksumCRC32(file));
        return zipEntry;
    }

    protected static void skipFirstLine(RandomAccessFile logFile) throws IOException {
        boolean eol = false;
        while (!eol) {
            switch (logFile.read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    long cur = logFile.getFilePointer();
                    if ((logFile.read()) != '\n') {
                        logFile.seek(cur);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
