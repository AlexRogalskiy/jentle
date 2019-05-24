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
