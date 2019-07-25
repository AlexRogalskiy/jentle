package com.wildbeeslabs.jentle.algorithms.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static com.wildbeeslabs.jentle.algorithms.file.HeapMemoryMonitor.heapUtilization;
import static com.wildbeeslabs.jentle.algorithms.file.HeapMemoryMonitor.maxMemory;

public class FileMemoryMonitor implements MemoryMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(FileMemoryMonitor.class);

    private static final long UNKNOWN_SIZE = Long.MIN_VALUE;

    private static final Optional<FileStore> FILE_STORE = initFileStore();

    private static Optional<FileStore> initFileStore() {
        FileStore fileStore = null;
        try {
            Path tempFolder = File.createTempFile(UUID.randomUUID().toString(), ".tmp").toPath();
            try {
                fileStore = Files.getFileStore(tempFolder);
            } catch (IOException e) {
                LOG.error("Cannot create a file store from default temp folder", e);
            }
        } catch (IOException e) {
            LOG.error("Cannot access the default temp folder", e);
        }

        return Optional.ofNullable(fileStore);
    }

    /**
     * Returns the total space in bytes available for the temp folder.
     *
     * @return an value of available HD memory in bytes
     */
    public static long availableSpace() {
        try {
            return FILE_STORE.isPresent() ? FILE_STORE.get().getUsableSpace() : UNKNOWN_SIZE;
        } catch (IOException e) {
            LOG.error("Cannot access the amount of usable space from the file store", e);
            return UNKNOWN_SIZE;
        }
    }

    public static boolean canLoadInMemory(final File file) {
        final long fileBytes = file.length();
        return maxMemory() > fileBytes && heapUtilization() < SAFETY_THRESHOLD;
    }

    public static boolean canPersistOnDisk(final File file) {
        final long fileBytes = file.length();
        return file.getUsableSpace() > fileBytes;
    }
}
