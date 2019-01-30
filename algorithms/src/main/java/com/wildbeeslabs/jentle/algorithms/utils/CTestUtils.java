/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wildbeeslabs.jentle.algorithms.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Custom test utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CTestUtils {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CStringUtils.class);

    private CTestUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static File loadFile(final String path) {
        return new File(CTestUtils.class.getClassLoader().getResource(path).getFile());
    }

    public static byte[] loadFileIntoByte(final String path) throws IOException {
        return Files.readAllBytes(loadFile(path).toPath());
    }

    public static InputStream loadFileIntoInputStream(final String path) throws IOException {
        return new ByteArrayInputStream(loadFileIntoByte(path));
    }

    public static String readFile(final String path) throws IOException {
        return new String(loadFileIntoByte(path), Charset.forName(StandardCharsets.UTF_8.name()));
    }

    public static void makeFinalStatic(final Field field, final Object newValue) throws Exception {
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}