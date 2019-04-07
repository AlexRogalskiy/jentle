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
package com.wildbeeslabs.jentle.algorithms.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom parser utility implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@JacksonXmlRootElement(localName = "registry", namespace = "io.jentle")
public class CXkbConfigRegistry {

    /**
     * Default bundle source
     */
    protected static final String DEFAULT_CONFIG_SOURCE = "/usr/share/X11/xkb/rules/base.xml";

    public static class Model {

        @Data
        @EqualsAndHashCode
        @ToString
        public static class ModelConfigItem {

            @JacksonXmlProperty
            public String name;

            @JacksonXmlProperty
            public String description;

            @JacksonXmlProperty
            public String vendor;
        }

        @JacksonXmlProperty
        ModelConfigItem configItem;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Layout {

        @Data
        @EqualsAndHashCode
        @ToString
        public static class LayoutConfigItem {

            @JacksonXmlProperty
            public String name;

            @JacksonXmlProperty
            public String description;
        }

        @JacksonXmlProperty
        public LayoutConfigItem configItem;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty
    private final List<Model> modelList = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty
    private final List<Layout> layoutList = new ArrayList<>();

    private static CXkbConfigRegistry instance = null;

    public static CXkbConfigRegistry getXkbConfigRegistry() {
        //return CXkbConfigRegistry.getXkbConfigRegistry(CXkbConfigRegistry.DEFAULT_CONFIG_SOURCE);
        return null;
    }

//    public static CXkbConfigRegistry getXkbConfigRegistry(final String source) {
//        if (Objects.isNull(instance)) {
//            try {
//                JAXBContext jc = JAXBContext.newInstance(CXkbConfigRegistry.class);
//                Unmarshaller unmarshaller = jc.createUnmarshaller();
//                final File xml = new File(source);
//                if (xml.exists()) {
//                    instance = (CXkbConfigRegistry) unmarshaller.unmarshal(xml);
//                }
//            } catch (JAXBException ex) {
//                log.error(String.format("ERROR: cannot load configuration from source=(%s), message=(%s)", source, ex.getMessage()));
//            }
//        }
//        return instance;
//    }
}
