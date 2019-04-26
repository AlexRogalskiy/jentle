package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertyHolder {

    private static PropertyHolder ourInstance = new PropertyHolder();
    private static final String DEFAULT_PROPERTIES_PATH = "/default.properties";

    private Properties properties;

    /**
     * Default private constructor
     */
    private PropertyHolder() {
        properties = new Properties();
    }

    /**
     * Initialized {@link Properties} by input source path
     *
     * @param path - initial input source path
     */
    public void init(final String path) {
        final String propertiesPath = Objects.isNull(System.getProperty(path)) ? DEFAULT_PROPERTIES_PATH : System.getProperty(path);
        final InputStream input = PropertyHolder.class.getResourceAsStream(propertiesPath);
        try {
            this.properties.load(input);
        } catch (IOException e) {
            log.error("ERROR: cannot load input source: ", path);
        }
    }

    public static PropertyHolder getInstance() {
        return ourInstance;
    }

    public String getProperty(final String key) {
        return StringUtils.isNotBlank(key) ? this.properties.getProperty(key) : null;
    }
}
