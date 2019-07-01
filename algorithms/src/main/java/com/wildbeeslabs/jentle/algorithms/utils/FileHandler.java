package com.wildbeeslabs.jentle.algorithms.utils;

import org.openqa.selenium.Platform;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utility methods for common filesystem activities
 */
public class FileHandler {

    private static InputStream locateResource(Class<?> forClassLoader, String name)
        throws IOException {
        String arch = Objects.requireNonNull(System.getProperty("os.arch")).toLowerCase() + "/";
        List<String> alternatives =
            Arrays.asList(name, "/" + name, arch + name, "/" + arch + name);
        if (Platform.getCurrent().is(Platform.MAC)) {
            alternatives.add("mac/" + name);
            alternatives.add("/mac/" + name);
        }

        // First look using our own classloader
        for (String possibility : alternatives) {
            InputStream stream = FileHandler.class.getResourceAsStream(possibility);
            if (stream != null) {
                return stream;
            }
            stream = forClassLoader.getResourceAsStream(possibility);
            if (stream != null) {
                return stream;
            }
        }

        throw new IOException("Unable to locate: " + name);
    }


    public static boolean createDir(File dir) throws IOException {
        if ((dir.exists() || dir.mkdirs()) && dir.canWrite())
            return true;

        if (dir.exists()) {
            FileHandler.makeWritable(dir);
            return dir.canWrite();
        }

        // Iterate through the parent directories until we find that exists,
        // then sink down.
        return createDir(dir.getParentFile());
    }

    public static boolean makeWritable(File file) throws IOException {
        return file.canWrite() || file.setWritable(true);
    }

    public static boolean isZipped(String fileName) {
        return fileName.endsWith(".zip") || fileName.endsWith(".xpi");
    }

    public static boolean delete(File toDelete) {
        boolean deleted = true;

        if (toDelete.isDirectory()) {
            File[] children = toDelete.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleted &= child.canWrite() && delete(child);
                }
            }
        }

        return deleted && toDelete.canWrite() && toDelete.delete();
    }

    public static void copy(File from, File to) throws IOException {
        if (!from.exists()) {
            return;
        }

        if (from.isDirectory()) {
            copyDir(from, to);
        } else {
            copyFile(from, to);
        }
    }

    private static void copyDir(File from, File to) throws IOException {
        // Create the target directory.
        createDir(to);

        // List children.
        String[] children = from.list();
        if (children == null) {
            throw new IOException("Could not copy directory " + from.getPath());
        }
        for (String child : children) {
            if (!".parentlock".equals(child) && !"parent.lock".equals(child)) {
                copy(new File(from, child), new File(to, child));
            }
        }
    }

    private static void copyFile(File from, File to) throws IOException {
        try (OutputStream out = new FileOutputStream(to)) {
            final long copied = Files.copy(from.toPath(), out);
            final long length = from.length();
            if (copied != length) {
                throw new IOException("Could not transfer all bytes from " + from + " to " + to);
            }
        }
    }
}
