package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class used for common manipulations with files.
 *
 * @author attatrol
 */
@UtilityClass
public class FilesystemUtils {

    /**
     * Buffer size for buffered stream.
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Deletes the file, if it is a directory, deletes its content recursively.
     *
     * @param root path to the file.
     * @throws IOException thrown on filesystem error.
     */
    public static void delete(Path root)
        throws IOException {
        if (Files.isDirectory(root)) {
            final DirectoryStream<Path> subPaths =
                Files.newDirectoryStream(root);
            for (Path path : subPaths) {
                delete(path);
            }
            subPaths.close();
            Files.delete(root);
        } else {
            Files.delete(root);
        }
    }

    /**
     * Creates new directory or overwrites existing one.
     *
     * @param path path to the directory.
     * @throws IOException thrown on filesystem error.
     */
    public static void createOverwriteDirectory(Path path)
        throws IOException {
        if (Files.exists(path)) {
            delete(path);
        }
        Files.createDirectory(path);
    }

    /**
     * Exports a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName i.e.: "/SmartLibrary.dll".
     * @param destination  the desired path of the resource.
     * @throws IOException thrown on filesystem error.
     */
    public static void exportResource(String resourceName,
                                      Path destination) throws IOException {
        try (InputStream in = FilesystemUtils.class
            .getResourceAsStream(resourceName);
             OutputStream out = Files.newOutputStream(destination)) {
            int readBytes;
            final byte[] buffer = new byte[BUFFER_SIZE];
            while ((readBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }
        }
    }

}
