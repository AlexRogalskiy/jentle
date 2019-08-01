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

import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.BiPredicate;
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
        return Base64.encodeBase64String(fileContent);
    }

    public static void convertFromBase64(final String fileName, final String encodedString) throws IOException {
        byte[] decodedBytes = Base64.decodeBase64(encodedString);
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

    public static File getTestFile(final String payloadResourceFile) {
        try {
            return new File(Thread.currentThread().getContextClassLoader().getResource("data/" + payloadResourceFile).toURI());
        } catch (final URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
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
        return readAllBytes(loadFile(path).toPath());
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

    public static void writeToFile(Path path, byte[] content, StandardOpenOption... openOption) {
        try {
            Files.write(path, content, openOption);
        } catch (Exception e) {
            log.error("Problem occured while writing {}", path, e);
        }
    }

    private static Charset detectCharset(File f, String[] charsets, byte[] bytes) {
        for (final String charsetName : charsets) {
            if (Objects.nonNull(charsetName)) {
                Charset charset = detectCharset(f, Charset.forName(charsetName), bytes);
                if (charset != null) {
                    return charset;
                }
            }
        }

        throw new RuntimeException("Charset not detected, can't open this file ");
    }

    private static Charset detectCharset(File f, Charset charset, byte[] bytes) {
        try {
            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();
            boolean identified = true;

            try {
                decoder.decode(ByteBuffer.wrap(bytes));
            } catch (CharacterCodingException e) {
                identified = false;
            }
            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public static void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            log.error("Problem occured while creating directories {}", path, e);
        }

    }

    public static Path createTempFile2(final String suffix) {
        try {
            return Files.createTempFile("asciidoc-temp", suffix);
        } catch (Exception e) {
            log.error("Problem occured while creating temp file", e);
        }
        return null;
    }

    public static Path createTempFile(Path path, String prefix, String suffix) {
        if (Objects.isNull(path)) {
            return createTempFile2(suffix);
        }
        try {
            return Files.createTempFile(path, prefix, suffix);
        } catch (Exception e) {
            log.error("Problem occured while creating temp file {}", path, e);
        }
        return null;
    }

    public static Path createTempFile(Path path, String suffix) {
        if (Objects.isNull(path)) {
            return createTempFile2(suffix);
        }

        return createTempFile(path, "asciidoc-temp", suffix);
    }

    public static void copy(Path source, Path target, CopyOption... copyOptions) {
        try {
            Files.copy(source, target, copyOptions);
        } catch (Exception e) {
            log.error("Problem occured while copying {} to {}", source, target, e);
        }
    }

    public static String pathToUrl(Path path) {
        try {
            return path.toUri().toURL().toString();
        } catch (Exception e) {
            log.error("Problem occured while getting URL of {}", path, e);
        }
        return null;
    }

    public static Stream<Path> list(Path path) {
        try {
            return Files.list(path);
        } catch (Exception e) {
            log.error("Problem occured while listing {}", path, e);
        }
        return Stream.empty();
    }

    public static void imageWrite(BufferedImage bufferedImage, String format, File output) {
        try {
            ImageIO.write(bufferedImage, format, output);
        } catch (Exception e) {
            log.error("Problem occured while writing buff image to {}", output, e);
        }
    }

    public static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.error("Problem occured while reading {}", path, e);
        }
        return new byte[]{};
    }

    public static void move(Path source, Path target, StandardCopyOption... option) {
        try {
            Files.move(source, target, option);
        } catch (Exception e) {
            log.error("Problem occured while moving {} to {}", source, target, e);
        }
    }

    public static void transform(Transformer transformer, StreamSource xmlSource, StreamResult streamResult) {
        try {
            transformer.transform(xmlSource, streamResult);
        } catch (TransformerException e) {
            log.error("Problem occured while transforming XML Source to Stream result", e);

        }
    }

    public static void copyDirectoryToDirectory(File source, File target) {
        try {
            FileUtils.copyDirectoryToDirectory(source, target);
        } catch (Exception e) {
            log.error("Problem occured while copying {} to {}", source, target, e);
        }
    }

    public static Optional<Exception> deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            log.error("Problem occured while deleting {}", path, e);
            return Optional.ofNullable(e);
        }

        return Optional.empty();
    }

    public static void copyDirectory(Path sourceDir, Path targetDir) {
        try {
            FileUtils.copyDirectory(sourceDir.toFile(), targetDir.toFile());
        } catch (Exception e) {
            log.error("Problem occured while copying {} to {}", sourceDir, targetDir, e);
        }
    }

    public static Stream<Path> find(Path start, Integer maxDepth, BiPredicate<Path, BasicFileAttributes> matcher, FileVisitOption... options) {
        if (Objects.isNull(maxDepth)) {
            maxDepth = Integer.MAX_VALUE;
        }

        try {
            return Files.find(start, maxDepth, matcher, options);
        } catch (Exception e) {
            log.error("Problem occured while finding in path {}", start, e);
        }
        return Stream.empty();
    }

    public static boolean isHidden(Path path) {
        try {
            return Files.exists(path) && (Files.isHidden(path) || path.getFileName().toString().startsWith("."));
        } catch (Exception e) {
//            logger.error("Problem occured while detecting hidden path {}", path, e);
        }
        return false;
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

    public String encodeFileToBase64Binary(final String fileName) throws IOException {
        Objects.requireNonNull(fileName, "File name should not be null");
        final File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static byte[] loadFile(final File file) throws IOException {
        Objects.requireNonNull(file, "File should not be null");
        final InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            return null;
        }
        final byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public static String inputStreamString(final InputStream inputStream) throws IOException {
        try (inputStream) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static void copyFileToDirectory(File file, File directory) {
        try {
            FileUtils.copyFileToDirectory(file, directory);
        } catch (Exception e) {
            log.error("Problem occured while copying {} to {}", file, directory, e);
        }
    }

    public static void copyFile2(File file, File dest) {
        try {
            FileUtils.copyFile(file, dest);
        } catch (Exception e) {
            log.error("Problem occured while copying {} to {}", file, dest, e);
        }
    }

    public static void createDirectory(Path path) {
        try {
            Files.createDirectory(path);
        } catch (Exception e) {
            log.error("Problem occured while creating {} path", path, e);
        }
    }

    private static Optional<Exception> forceDelete(Path path) {

        try {
            Objects.requireNonNull(path);

            if (Files.notExists(path)) {
                return Optional.empty();
            }

            FileUtils.forceDelete(path.toFile());
        } catch (FileNotFoundException fnx) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Problem occured while deleting {}", path, e);
            return Optional.ofNullable(e);
        }
        return Optional.empty();
    }

    public static List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            log.error("Problem occured while reading {} path", path, e);
        }
        return new ArrayList<>();
    }

    public static Reader fileReader(Path path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            return new InputStreamReader(fileInputStream, "UTF-8");
        } catch (Exception e) {
            log.error("Problem occured while creating FileReader for {} path", path, e);
        }
        return null;
    }

    public static FileTime getLastModifiedTime(Path path) {
        try {
            return Files.getLastModifiedTime(path);
        } catch (Exception e) {
        }
        return null;
    }

    public static String decode(String uri, String encoding) {
        try {
            return URLDecoder.decode(uri, encoding);
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        }
        return uri;
    }

    public static Path createTempDirectory(final Path path, final String prefix, final FileAttribute<?>... attrs) {
        try {
            return Files.createTempDirectory(path, prefix, attrs);
        } catch (Exception e) {
            log.error("Problem occured while creating temp directory");
        }

        return null;
    }

    public static String getPathCleanName(Path object) {
        return object.getFileName().toString().replaceAll("\\..*", "");
    }

    public static <T> T readFile(Path path, Class<T> clazz) throws IOException {

        if (clazz.isAssignableFrom(byte[].class)) {
            return clazz.cast(readAllBytes(path));
        } else {
            return clazz.cast(readFile((InputStream) path));
        }

    }

    public static boolean isEmptyDir(Path path) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
            return !dirStream.iterator().hasNext();
        } catch (Exception e) {
//            logger.warn("Problem occured while checking is directory empty {}", path);
        }
        return false;
    }

    public static WatchService newWatchService() {
        try {
            return FileSystems.getDefault().newWatchService();
        } catch (Exception e) {
            log.warn("Problem occured while creating new watch service");
        }
        return null;
    }

    public static Optional<Long> size(Path path) {
        try {
            long size = Files.size(path);
            return Optional.of(size);
        } catch (Exception e) {
            log.warn("Problem occured while getting size info of {}", path);
        }
        return Optional.empty();
    }

    public static boolean contains(Path root, Path subPath) {

        if (root == null || subPath == null)
            return false;

        Iterator<Path> realPathIterator = root.normalize().iterator();
        Iterator<Path> subPathIterator = subPath.normalize().iterator();

        while (realPathIterator.hasNext()) {
            Path realPathSegment = realPathIterator.next();
            if (subPathIterator.hasNext()) {
                Path subPathSegment = subPathIterator.next();
                if (!Objects.equals(realPathSegment, subPathSegment)) {
                    subPathIterator = subPath.normalize().iterator();
                }
            } else {
                break;
            }
        }
        return !subPathIterator.hasNext();
    }

    public static Stream<Path> walk(Path path) {
        try {
            return Files.walk(path);
        } catch (Exception e) {
            log.warn("Problem occured while walking path {}", path);
        }
        return Stream.empty();
    }

    public static Stream<Path> walk(Path path, int depth) {
        try {
            return Files.walk(path, depth);
        } catch (Exception e) {
            log.warn("Problem occured while walking path {}", path);
        }
        return Stream.empty();
    }

    public static boolean isSameImage(BufferedImage firstImage, BufferedImage secondImage) {
        if (Objects.isNull(firstImage)) {
            return false;
        }
        if (Objects.isNull(secondImage)) {
            return false;
        }

        // The images must be the same size.
        if (firstImage.getWidth() == secondImage.getWidth() && firstImage.getHeight() == secondImage.getHeight()) {
            int width = firstImage.getWidth();
            int height = firstImage.getHeight();

            // Loop over every pixel.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Compare the pixels for equality.
                    if (firstImage.getRGB(x, y) != secondImage.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static File getOrCreateFile(final String path) {
        final File file = new File(path);
        if (!file.exists() && file.canWrite()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void findFiles(final String extension, final File baseDir, final Collection<File> found) {
        baseDir.listFiles(pathname -> {
            if (pathname.isDirectory()) {
                findFiles(extension, pathname, found);
            } else if (pathname.getName().endsWith(extension)) {
                found.add(pathname);
            }
            return false;
        });
    }
}
