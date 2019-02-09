/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Custom JSON utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CJsonUtils {

    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    public static <K, V> String toJson(final Map<K, V> data) {
        final Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static <V> void toJson(final String filename, final List<V> data) {
        try (final FileOutputStream fos = new FileOutputStream(filename);
             final OutputStreamWriter writer = new OutputStreamWriter(fos, DEFAULT_FILE_CHARACTER_ENCODING)) {
            final Gson gson = new Gson();
            gson.toJson(data, writer);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process write operations to file=%s, message=%s", filename, ex.getMessage()));
        }
    }

    public static <V> void toJsonTreeModel(final String filename, final List<V> data) {
        final Path path = Paths.get(filename);
        try (final Writer writer = Files.newBufferedWriter(path, DEFAULT_FILE_CHARACTER_ENCODING)) {
            final Gson gson = new Gson();
            final JsonElement tree = gson.toJsonTree(data);
            gson.toJson(tree, writer);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process write operations to file=%s, message=%s", filename, ex.getMessage()));
        }
    }

    public static <V> void toJson(final OutputStream out, final V entity) {
        try (final PrintStream prs = new PrintStream(out, true, DEFAULT_FILE_CHARACTER_ENCODING.name())) {
            final Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            gson.toJson(entity, prs);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on output stream, message=%s", ex.getMessage()));
        }
    }

    public static <V> V fromJsonString(final String jsonString, final Class<? extends V> clazz) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

    public static <V> List<V> fromJsonFileToList(final String filename, final Class<? extends V> clazz) {
        final Path path = Paths.get(filename);
        try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            final Gson gson = new Gson();
            return (List<V>) gson.fromJson(reader, new TypeToken<List<V>>() {
            }.getType());
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations from file=%s, message=%s", filename, ex.getMessage()));
        }
        return null;
    }

    public static <V> V[] fromJsonFileToArray(final String filename, final Class<? extends V[]> clazz) {
        final Gson gson = new GsonBuilder().create();
        final Path path = new File(filename).toPath();
        try (final Reader reader = Files.newBufferedReader(path, DEFAULT_FILE_CHARACTER_ENCODING)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations from file=%s, message=%s", filename, ex.getMessage()));
        }
        return null;
    }

    public static <V> V fromJsonUrl(final String url, final Class<? extends V> clazz) {
        try (final InputStream is = new URL(url).openStream();
             final Reader reader = new InputStreamReader(is, DEFAULT_FILE_CHARACTER_ENCODING)) {
            final Gson gson = new Gson();
            return gson.fromJson(reader, clazz);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations from url=%s, message=%s", url, ex.getMessage()));
        }
        return null;
    }
}
