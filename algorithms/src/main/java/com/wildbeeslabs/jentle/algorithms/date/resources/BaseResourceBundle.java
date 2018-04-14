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
package com.wildbeeslabs.jentle.algorithms.date.resources;

import com.wildbeeslabs.jentle.algorithms.utils.CConverterUtils;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Default base resource bundle implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class BaseResourceBundle extends ListResourceBundle {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
    /**
     * Default bundle source
     */
    protected static final String DEFAULT_BUNDLE_SOURCE = "/src/main/resources/i18n";
    /**
     * Default bundle locale
     */
    protected static final Locale DEFAULT_BUNDLE_LOCALE = Locale.US;
    /**
     * Default resource properties
     */
    protected final Properties properties = new Properties();
    /**
     * Default bundle source
     */
    protected String source;
    /**
     * Default bundle locale
     */
    protected Locale locale;
    /**
     * Default resource properties
     */
    protected ResourceBundle resources = null;

    public BaseResourceBundle() {
        this(BaseResourceBundle.DEFAULT_BUNDLE_LOCALE);
    }

    public BaseResourceBundle(final Locale locale) {
        this(BaseResourceBundle.DEFAULT_BUNDLE_SOURCE, locale);
    }

    public BaseResourceBundle(final String source, final Locale locale) {
        this.source = source;
        this.locale = locale;
    }

    public <R extends BaseResource> Collection<R> getProperties() {
        return CConverterUtils.convertToList(this.properties.values().stream(), item -> (R) item);
    }

    public <R extends BaseResource> void setProperties(final Map<Object, R> properties) {
        this.properties.clear();
        if (Objects.nonNull(properties)) {
            this.properties.putAll(properties);
        }
    }

    public <R extends BaseResource> void addProperty(final Object name, final R property) {
        if (Objects.nonNull(name)) {
            this.properties.put(name, property);
        }
    }

    public void removeProperty(final Object name) {
        if (Objects.nonNull(name)) {
            this.properties.remove(name);
        }
    }

    public void loadProperties(final String source) {
        try (final InputStream in = this.getClass().getClassLoader().getResourceAsStream(source)) {
            this.properties.load(in);
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot load data from source=(%s), message=(%s)", source, ex.getMessage()));
        }
    }

    protected void loadResources() {
        this.resources = ResourceBundle.getBundle(this.source, this.locale);
    }

    public Object getResourceByKey(final String key) {
        if (Objects.isNull(this.resources)) {
            this.loadResources();
        }
        return this.resources.getObject(key);
    }

    @Override
    protected Object[][] getContents() {
        return this.getProperties().stream().map((item) -> item.toArray()).collect(CConverterUtils.toArray(Object[][]::new));
    }
}
