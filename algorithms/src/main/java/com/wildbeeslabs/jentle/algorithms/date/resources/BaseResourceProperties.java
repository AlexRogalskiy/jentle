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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Default base resource properties implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseResourceProperties extends Properties {

    public <R extends BaseResource> Collection<R> getProperties() {
        return CConverterUtils.toList(this.defaults.values().stream(), item -> (R) item);
    }

    public <R extends BaseResource> void setProperties(final Map<Object, R> properties) {
        this.clearProperties();
        if (Objects.nonNull(properties)) {
            this.defaults.putAll(properties);
        }
    }

    public <R extends BaseResource> void addProperty(final Object key, final R property) {
        if (Objects.nonNull(key)) {
            this.defaults.put(key, property);
        }
    }

    public boolean removeProperty(final Object key) {
        if (Objects.nonNull(key)) {
            this.defaults.remove(key);
            return true;
        }
        return false;
    }

    public <R extends BaseResource> R getProperty(final Object key) {
        if (Objects.nonNull(key)) {
            return (R) this.defaults.get(key);
        }
        return null;
    }

    public void loadProperties(final String source) {
        try (final InputStream in = this.getClass().getClassLoader().getResourceAsStream(source)) {
            this.defaults.load(in);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot load data from source=(%s), message=(%s)", source, ex.getMessage()));
        }
    }

    public Object[][] getContents() {
        return this.getProperties().stream().map((item) -> item.toArray()).collect(CConverterUtils.toArray(Object[][]::new));
    }

    public Enumeration<String> getKeys() {
        return (Enumeration<String>) this.defaults.propertyNames();
    }

    public Enumeration<Object> getValues() {
        return (Enumeration<Object>) this.defaults.elements();
    }

    public void clearProperties() {
        this.defaults.clear();
    }
}
