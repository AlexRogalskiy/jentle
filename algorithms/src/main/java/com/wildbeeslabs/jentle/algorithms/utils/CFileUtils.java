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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom file utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CFileUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CFileUtils.class);
    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    private CFileUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static List<String> readAllLines(final File inputFile) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.EMPTY_LIST;
        try {
            resultList = Files.readAllLines(inputFile.toPath(), CFileUtils.DEFAULT_FILE_CHARACTER_ENCODING);
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }

    public static List<String> readFileByFilter(final File inputFile, final Predicate<String> predicate) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.EMPTY_LIST;
        try (final BufferedReader br = Files.newBufferedReader(inputFile.toPath(), CFileUtils.DEFAULT_FILE_CHARACTER_ENCODING)) {
            resultList = br.lines().filter(predicate).collect(Collectors.toList());
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }

    public static <E> void writeFile(final File outputFile, final List<? extends E> output) {
        Objects.requireNonNull(outputFile);
        Objects.requireNonNull(output);
        try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile.toPath(), CFileUtils.DEFAULT_FILE_CHARACTER_ENCODING))) {
            output.stream().forEach(writer::println);//String newLine = System.getProperty("line.separator");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            LOGGER.error(String.format("ERROR: cannot create output file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read / writer operations on file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }

    public static void writeZipFile(final List<File> listFiles, final File outputZip) throws IOException {
        outputZip.getParentFile().mkdirs();
        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZip))) {
            for (final File file : listFiles) {
                final String filePath = file.getCanonicalPath();
                LOGGER.debug(String.format("Processing zip file: %s", filePath));

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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile2(final String filename) {
        Objects.requireNonNull(filename);
        final StringBuilder sb = new StringBuilder();
        try {
            final List<String> lines = Files.readAllLines(Paths.get(filename), DEFAULT_FILE_CHARACTER_ENCODING);
            for (final String line : lines) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
        return sb.toString();
    }

    public static String readFile6(final String filename) {
        Objects.requireNonNull(filename);
        try (final FileInputStream inputStream = new FileInputStream(filename)) {
            return IOUtils.toString(inputStream, DEFAULT_FILE_CHARACTER_ENCODING.name());
            //return FileUtils.readFileToString(new File(filename), DEFAULT_FILE_CHARACTER_ENCODING.name());
        } catch (FileNotFoundException ex) {
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
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
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
    }

    public static void writeCsvFile(final String filename, final List<String[]> data) {
        Objects.requireNonNull(filename);
        try (final FileOutputStream fos = new FileOutputStream(filename);
                final OutputStreamWriter osw = new OutputStreamWriter(fos, DEFAULT_FILE_CHARACTER_ENCODING);
                final CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            writer.writeAll(data);
        } catch (FileNotFoundException ex) {
            LOGGER.error(String.format("ERROR: cannot found file=%s, message=%s", filename, ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read operations on file=%s, message=%s", filename, ex.getMessage()));
        }
    }
}
