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
package com.wildbeeslabs.jentle.algorithms.utils;

import com.opencsv.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Custom file utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CFileUtils {
    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    public static <U extends CharSequence> List<U> readAllLines(final File inputFile) {
        Objects.requireNonNull(inputFile);
        List<U> resultList = Collections.EMPTY_LIST;
        try {
            resultList = (List<U>) Files.readAllLines(inputFile.toPath(), DEFAULT_FILE_CHARACTER_ENCODING);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }

    public static List<String> readFileByFilter(final File inputFile, final Predicate<? super String> predicate) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.EMPTY_LIST;
        try (final BufferedReader br = Files.newBufferedReader(inputFile.toPath(), DEFAULT_FILE_CHARACTER_ENCODING)) {
            resultList = br.lines().filter(predicate).collect(Collectors.toList());
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }

    public static <U> void writeFile(final File outputFile, final Collection<? extends U> output) {
        Objects.requireNonNull(outputFile);
        Objects.requireNonNull(output);
        try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile.toPath(), DEFAULT_FILE_CHARACTER_ENCODING))) {
            output.stream().forEach(writer::println);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            log.error(String.format("ERROR: cannot create output file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read / writer operations on file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }

    public static void writeZipFile(final List<File> listFiles, final File outputZip) throws IOException {
        outputZip.getParentFile().mkdirs();
        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZip))) {
            for (final File file : listFiles) {
                final String filePath = file.getCanonicalPath();
                log.debug(String.format("Processing zip file: %s", filePath));

                final String zipFilePath = FilenameUtils.getName(filePath);
                final ZipEntry zipEntry = new ZipEntry(zipFilePath);
                zipOutputStream.putNextEntry(zipEntry);

                try (final FileInputStream inputStream = new FileInputStream(file)) {
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = inputStream.read(bytes)) >= 0) {
                        zipOutputStream.write(bytes, 0, length);
                    }
                }
            }
        }
    }

    public static List<File> listFiles(final File inputDirectory) throws IOException {
        final List<File> listFiles = new ArrayList<>();
        listFiles(listFiles, inputDirectory);
        return listFiles;
    }

    private static void listFiles(final List<File> listFiles, final File inputDirectory) throws IOException {
        final File[] allFiles = inputDirectory.listFiles();
        for (final File file : allFiles) {
            if (file.isDirectory()) {
                listFiles(listFiles, file);
            } else {
                listFiles.add(file);
            }
        }
    }

    public static List<File> listDirectories(final File inputDirectory) {
        final List<File> listDirectories = new ArrayList<>();
        listDirectories(listDirectories, inputDirectory);
        return listDirectories;
    }

    private static void listDirectories(final List<File> listDirectories, final File inputDirectory) {
        final File[] directories = inputDirectory.listFiles(File::isDirectory);
        for (final File directory : directories) {
            listDirectories.add(directory);
            listDirectories(listDirectories, directory);
        }
    }

    public static List<File> listDirectories2(final File inputDirectory) {
        final List<File> listDirectories = new ArrayList<>();
        listDirectories2(listDirectories, inputDirectory);
        return listDirectories;
    }

    private static void listDirectories2(final List<File> listDirectories, final File inputFile) {
        final File[] directories = inputFile.listFiles((File current, String name) -> new File(current, name).isDirectory());
        for (final File directory : directories) {
            listDirectories.add(directory);
            listDirectories2(directory);
        }
    }

    public static String convertToBase64(final String fileName) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(fileName));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static void convertFromBase64(final String fileName, final String encodedString) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        FileUtils.writeByteArrayToFile(new File(fileName), decodedBytes);
    }

    public static Stream<String> streamOf(final String fileName) throws IOException {
        return Files.lines(Paths.get(fileName), DEFAULT_FILE_CHARACTER_ENCODING);
    }

    public static String readFile(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while (Objects.nonNull(line = br.readLine())) {
                sb.append(line);
                if (Objects.nonNull(line)) {
                    sb.append(System.lineSeparator());
                }
            }
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile2(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try {
            final List<String> lines = Files.readAllLines(Paths.get(filename), DEFAULT_FILE_CHARACTER_ENCODING);
            lines.stream().map((line) -> {
                sb.append(line);
                return line;
            }).forEach((_item) -> {
                sb.append(System.lineSeparator());
            });
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile3(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(filename)).forEachOrdered(s -> {
                sb.append(s);
                sb.append(System.lineSeparator());
            });
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    @SuppressWarnings("null")
    public static String readFile4(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try (final Scanner scan = new Scanner(new File(filename))) {
            while (scan.hasNext()) {
                String line = scan.nextLine();
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile5(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        String line = null;
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), DEFAULT_FILE_CHARACTER_ENCODING))) {
            while (Objects.nonNull(line = br.readLine())) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile6(final String filename) {
        Objects.requireNonNull(filename);
        try (final FileInputStream inputStream = new FileInputStream(filename)) {
            return IOUtils.toString(inputStream, DEFAULT_FILE_CHARACTER_ENCODING.name());
            //return FileUtils.readFileToString(new File(filename), DEFAULT_FILE_CHARACTER_ENCODING.name());
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return null;
    }

    public static String readCsvFile(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try (final CSVReader reader = new CSVReader(new FileReader(filename))) {
            String[] nextLine;
            while (Objects.nonNull(nextLine = reader.readNext())) {
                for (final String e : nextLine) {
                    sb.append(e);
                }
                sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readCsvFile(final String filename, char separator) {
        Objects.requireNonNull(filename);
        final Path path = Paths.get(filename);
        final CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
        final StringBuilder sb = new StringBuilder();
        try (final BufferedReader br = Files.newBufferedReader(path, DEFAULT_FILE_CHARACTER_ENCODING);
             final CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {
            final List<String[]> rows = reader.readAll();
            rows.stream().map((row) -> {
                for (String e : row) {
                    sb.append(e);
                }
                return row;
            }).forEach((_item) -> {
                sb.append(System.lineSeparator());
            });
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static void writeCsvFile(final String filename, final String[] data) {
        Objects.requireNonNull(filename);
        try (final FileOutputStream fos = new FileOutputStream(filename);
             final OutputStreamWriter osw = new OutputStreamWriter(fos, DEFAULT_FILE_CHARACTER_ENCODING);
             final CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            writer.writeNext(data);
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
    }

    public static void writeCsvFile(final String filename, final List<String[]> data) {
        Objects.requireNonNull(filename);
        try (final FileOutputStream fos = new FileOutputStream(filename);
             final OutputStreamWriter osw = new OutputStreamWriter(fos, DEFAULT_FILE_CHARACTER_ENCODING);
             final CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            writer.writeAll(data);
        } catch (FileNotFoundException ex) {
            log.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
    }

    public static File loadFile(final String path) {
        return new File(CFileUtils.class.getClassLoader().getResource(path).getFile());
    }

    public static byte[] loadFileIntoByte(final String path) throws IOException {
        return Files.readAllBytes(loadFile(path).toPath());
    }

    public static InputStream loadFileIntoInputStream(final String path) throws IOException {
        return new ByteArrayInputStream(loadFileIntoByte(path));
    }

    public static String readFile7(final String path) throws IOException {
        return new String(loadFileIntoByte(path), Charset.forName(StandardCharsets.UTF_8.name()));
    }

    public static URL getResource(final String resourceName) {
        URL url = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (Objects.nonNull(loader)) {
            url = loader.getResource(resourceName);
        }
        if (Objects.isNull(url)) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        if (Objects.isNull(url)) {
            try {
                url = (new File(URLDecoder.decode(resourceName, "UTF-8"))).toURI().toURL();
            } catch (Exception e) {
                log.error("Problem loading resource", e);
            }
        }
        return url;
    }

    public static void copyFile(final File from, final File to) {
        to.getParentFile().mkdirs();
        try (InputStream in = new FileInputStream(from);
             OutputStream out = new FileOutputStream(to)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static File createTempFile(final String content) {
        try {
            // Create temp file.
            File result = File.createTempFile("testng-tmp", "");

            // Delete temp file when program exits.
            result.deleteOnExit();

            // Write to temp file
            try (BufferedWriter out = new BufferedWriter(new FileWriter(result))) {
                out.write(content);
            }

            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String readFile(final File f) throws IOException {
        try (InputStream is = new FileInputStream(f)) {
            return readFile(is);
        }
    }

    public static String readFile(final InputStream is) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        while (line != null) {
            result.append(line).append("\n");
            line = br.readLine();
        }
        return result.toString();
    }

    public static void writeFile(final String string, final File f) throws IOException {
        f.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(f);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(string);
        }
    }

    public static void copyFile(final InputStream from, final File to) throws IOException {
        if (!to.getParentFile().exists()) {
            to.getParentFile().mkdirs();
        }

        try (OutputStream os = new FileOutputStream(to)) {
            byte[] buffer = new byte[65536];
            int count = from.read(buffer);
            while (count > 0) {
                os.write(buffer, 0, count);
                count = from.read(buffer);
            }
        }
    }

    public static String streamToString(final InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try (Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
