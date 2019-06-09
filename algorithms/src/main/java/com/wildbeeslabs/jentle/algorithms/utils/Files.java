package com.wildbeeslabs.jentle.algorithms.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.wildbeeslabs.jentle.algorithms.utils.Arrays.isNullOrEmpty;
import static com.wildbeeslabs.jentle.algorithms.utils.Strings.append;
import static com.wildbeeslabs.jentle.algorithms.utils.Strings.concat;
import static java.io.File.separator;
import static java.util.regex.Pattern.quote;

/**
 * Utility methods related to files.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Files {
    /**
     * Returns the names of the files inside the specified directory.
     *
     * @param dirName the name of the directory to start the search from.
     * @param recurse if {@code true}, we will look in subdirectories.
     * @return the names of the files inside the specified directory.
     * @throws IllegalArgumentException if the given directory name does not point to an existing directory.
     */
    public static List<String> fileNamesIn(String dirName, boolean recurse) {
        File dir = new File(dirName);
        assert dir.isDirectory() : "%s is not a directory" + quote(dirName);
        return fileNamesIn(dir, recurse);
    }

    /**
     * Returns the names of the files inside the specified directory.
     *
     * @param dir     the name of the directory to start the search from.
     * @param recurse if {@code true}, we will look in subdirectories.
     * @return the names of the files inside the specified directory.
     */
    private static List<String> fileNamesIn(File dir, boolean recurse) {
        List<String> scriptNames = new ArrayList<>();
        File[] existingFiles = dir.listFiles();
        if (isNullOrEmpty(existingFiles)) {
            return scriptNames;
        }
        for (File existingFile : existingFiles) {
            if (existingFile.isDirectory()) {
                if (recurse) {
                    scriptNames.addAll(fileNamesIn(existingFile, recurse));
                }
                continue;
            }
            String filename = existingFile.getAbsolutePath();
            if (!scriptNames.contains(filename)) {
                scriptNames.add(filename);
            }
        }
        return scriptNames;
    }

    /**
     * Returns the system's temporary directory.
     *
     * @return the system's temporary directory.
     * @throws RuntimeException if this method cannot find or create the system's temporary directory.
     */
    public static File temporaryFolder() {
        File temp = new File(temporaryFolderPath());
        if (!temp.isDirectory()) {
            throw new RuntimeException("Unable to find temporary directory");
        }
        return temp;
    }

    /**
     * Returns the path of the system's temporary directory. This method appends the system's file separator at the end of
     * the path.
     *
     * @return the path of the system's temporary directory.
     */
    public static String temporaryFolderPath() {
        return append(separator).to(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Creates a new file in the system's temporary directory. The name of the file will be the result of:
     * <pre><code class='java'> concat(UUID.randomUUID().toString(), &quot;.txt&quot;);</code></pre>
     *
     * @return the created file.
     */
    public static File newTemporaryFile() {
        String tempFileName = concat(UUID.randomUUID().toString(), ".txt");
        return newFile(concat(temporaryFolderPath(), tempFileName));
    }

    /**
     * Creates a new directory in the system's temporary directory. The name of the directory will be the result of:
     * <pre><code class='java'> UUID.randomUUID().toString();</code></pre>
     *
     * @return the created file.
     */
    public static File newTemporaryFolder() {
        String tempFileName = UUID.randomUUID().toString();
        return newFolder(concat(temporaryFolderPath(), tempFileName));
    }

    /**
     * Creates a new file using the given path.
     *
     * @param path the path of the new file.
     * @return the new created file.
     * @throws RuntimeException     if the path belongs to an existing non-empty directory.
     * @throws RuntimeException     if the path belongs to an existing file.
     * @throws UncheckedIOException if any I/O error is thrown when creating the new file.
     */
    public static File newFile(String path) {
        File file = createFileIfPathIsNotANonEmptyDirectory(path);
        try {
            if (!file.createNewFile()) {
                throw cannotCreateNewFile(path, "a file was found with the same path");
            }
        } catch (IOException e) {
            throw cannotCreateNewFile(path, e);
        }
        return file;
    }

    /**
     * Creates a new directory using the given path.
     *
     * @param path the path of the new directory.
     * @return the new created directory.
     * @throws RuntimeException if the path belongs to an existing non-empty directory.
     * @throws RuntimeException if the path belongs to an existing file.
     * @throws RuntimeException if any I/O error is thrown when creating the new directory.
     */
    public static File newFolder(String path) {
        File file = createFileIfPathIsNotANonEmptyDirectory(path);
        try {
            if (!file.mkdir()) {
                throw cannotCreateNewFile(path, "a file was found with the same path");
            }
        } catch (Exception e) {
            throw cannotCreateNewFile(path, e);
        }
        return file;
    }

    private static File createFileIfPathIsNotANonEmptyDirectory(String path) {
        File file = new File(path);
        if (file.isDirectory() && !isNullOrEmpty(file.list())) {
            throw cannotCreateNewFile(path, "a non-empty directory was found with the same path");
        }
        return file;
    }

    private static UncheckedIOException cannotCreateNewFile(String path, String reason) {
        throw cannotCreateNewFile(path, reason, null);
    }

    private static UncheckedIOException cannotCreateNewFile(String path, Exception cause) {
        throw cannotCreateNewFile(path, null, cause);
    }

    private static UncheckedIOException cannotCreateNewFile(String path, String reason, Exception cause) {
        String message = String.format("Unable to create the new file %s", quote(path));
        if (!Strings.isNullOrEmpty(reason)) {
            message = concat(message, ": ", reason);
        }
        if (cause == null) {
            throw new RuntimeException(message);
        }
        if (cause instanceof IOException) {
            throw new UncheckedIOException(message, (IOException) cause);
        }
        throw new RuntimeException(message, cause);
    }

    /**
     * Returns the current directory.
     *
     * @return the current directory.
     * @throws UncheckedIOException if the current directory cannot be obtained.
     */
    public static File currentFolder() {
        try {
            return new File(".").getCanonicalFile();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to get current directory", e);
        }
    }

    /**
     * Deletes the given file or directory.
     *
     * @param file the file or directory to delete.
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        File[] existingFiles = file.listFiles();
        if (!isNullOrEmpty(existingFiles)) {
            for (File f : existingFiles) {
                delete(f);
            }
        }
        file.delete();
    }

    /**
     * Loads the text content of a file into a character string.
     *
     * @param file        the file.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws IllegalArgumentException if the given character set is not supported on this platform.
     * @throws UncheckedIOException     if an I/O exception occurs.
     */
    public static String contentOf(File file, String charsetName) {
        checkArgumentCharsetIsSupported(charsetName);
        return contentOf(file, Charset.forName(charsetName));
    }

    /**
     * Loads the text content of a file into a character string.
     *
     * @param file    the file.
     * @param charset the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static String contentOf(File file, Charset charset) {
        checkNotNull(charset, "The charset should not be null");
        try {
            return loadContents(file, charset);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + file.getAbsolutePath(), e);
        }
    }

    private static String loadContents(File file, Charset charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            StringWriter writer = new StringWriter();
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            return writer.toString();
        }
    }

    /**
     * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param file    the file.
     * @param charset the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static List<String> linesOf(File file, Charset charset) {
        checkNotNull(charset, "The charset should not be null");
        try {
            return loadLines(file, charset);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param file        the file.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws UncheckedIOException if an I/O exception occurs.
     */
    public static List<String> linesOf(File file, String charsetName) {
        checkArgumentCharsetIsSupported(charsetName);
        return linesOf(file, Charset.forName(charsetName));
    }

    private static List<String> loadLines(File file, Charset charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            List<String> strings = Lists.newArrayList();

            String line = reader.readLine();
            while (line != null) {
                strings.add(line);
                line = reader.readLine();
            }

            return strings;
        }
    }

    private static void checkArgumentCharsetIsSupported(String charsetName) {
        checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
    }
}
