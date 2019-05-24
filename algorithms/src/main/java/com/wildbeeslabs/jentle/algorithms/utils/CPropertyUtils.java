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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Custom property utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public class CPropertyUtils {

    private static CPropertyUtils ourInstance = new CPropertyUtils();
    private static final String DEFAULT_PROPERTIES_PATH = "/default.properties";

    private Properties properties;

    /**
     * Default private constructor
     */
    private CPropertyUtils() {
        this.properties = new Properties();
    }

    /**
     * Initialized {@link Properties} by input source path
     *
     * @param path - initial input source path
     */
    public void init(final String path) {
        final String propertiesPath = Objects.isNull(System.getProperty(path)) ? DEFAULT_PROPERTIES_PATH : System.getProperty(path);
        final InputStream input = CPropertyUtils.class.getResourceAsStream(propertiesPath);
        try {
            this.properties.load(input);
        } catch (IOException e) {
            log.error("ERROR: cannot load input source: ", path);
        }
    }

    public static CPropertyUtils getInstance() {
        return ourInstance;
    }

    public String getProperty(final String key) {
        return StringUtils.isNotBlank(key) ? this.properties.getProperty(key) : null;
    }
}
