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
package com.wildbeeslabs.jentle.algorithms.date.i18n;

import com.wildbeeslabs.jentle.algorithms.date.time.IDuration;
import com.wildbeeslabs.jentle.algorithms.date.time.ITimeFormat;
import com.wildbeeslabs.jentle.algorithms.date.time.SimpleTimeFormat;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Default resources bundle
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class Resources extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return getResources();
    }

    /**
     * Returns an {@code Object} array containing the resources of this
     * {@code ListResourceBundle}. Each element in the array is an array of two
     * elements, the first is the resource key string and the second is the
     * resource.
     *
     * @return a {@code Object} array containing the resources.
     */
    protected abstract Object[][] getResources();

    @Data
    @EqualsAndHashCode
    @ToString
    protected static class CsName implements Comparable<CsName> {

        protected boolean isFuture;
        protected String value;
        protected Long threshold;

        @Override
        public int compareTo(final CsName obj) {
            return this.threshold.compareTo(obj.getThreshold());
        }
    }

    protected static class CsTimeFormat<T extends CsName> extends SimpleTimeFormat implements ITimeFormat {

        private final List<T> futureNames = new ArrayList<>();
        private final List<T> pastNames = new ArrayList<>();

        public CsTimeFormat(final String resourceKeyPrefix, final ResourceBundle bundle, final Collection<T> names) {
            setPattern(bundle.getString(resourceKeyPrefix + "Pattern"));
            setFuturePrefix(bundle.getString(resourceKeyPrefix + "FuturePrefix"));
            setFutureSuffix(bundle.getString(resourceKeyPrefix + "FutureSuffix"));
            setPastPrefix(bundle.getString(resourceKeyPrefix + "PastPrefix"));
            setPastSuffix(bundle.getString(resourceKeyPrefix + "PastSuffix"));
            setSingularName(bundle.getString(resourceKeyPrefix + "SingularName"));
            setPluralName(bundle.getString(resourceKeyPrefix + "PluralName"));

            try {
                setFuturePluralName(bundle.getString(resourceKeyPrefix + "FuturePluralName"));
            } catch (Exception ex) {
                log.error(String.format("ERROR: cannot set future plural name by key=%s", resourceKeyPrefix + "FuturePluralName"), ex);
            }
            try {
                setFutureSingularName((bundle.getString(resourceKeyPrefix + "FutureSingularName")));
            } catch (Exception ex) {
                log.error(String.format("ERROR: cannot set future singular name by key=%s", resourceKeyPrefix + "FutureSingularName"), ex);
            }
            try {
                setPastPluralName((bundle.getString(resourceKeyPrefix + "PastPluralName")));
            } catch (Exception ex) {
                log.error(String.format("ERROR: cannot set past plural name by key=%s", resourceKeyPrefix + "PastPluralName"), ex);
            }
            try {
                setPastSingularName((bundle.getString(resourceKeyPrefix + "PastSingularName")));
            } catch (Exception ex) {
                log.error(String.format("ERROR: cannot set future plural name by key=%s", resourceKeyPrefix + "PastSingularName"), ex);
            }
            names.stream().forEach((name) -> {
                if (name.isFuture()) {
                    this.futureNames.add(name);
                } else {
                    this.pastNames.add(name);
                }
            });
            Collections.sort(this.futureNames);
            Collections.sort(this.pastNames);
        }

        @Override
        protected String getGramaticallyCorrectName(final IDuration duration, boolean round) {
            long quantity = Math.abs(getQuantity(duration, round));
            if (duration.isInFuture()) {
                return getGramaticallyCorrectName(quantity, this.futureNames);
            }
            return getGramaticallyCorrectName(quantity, this.pastNames);
        }

        private String getGramaticallyCorrectName(long quantity, final List<T> names) {
            for (final T name : names) {
                if (name.getThreshold() >= quantity) {
                    return name.getValue();
                }
            }
            throw new IllegalStateException("Invalid resource bundle configuration");
        }
    }

    protected static class CsTimeFormatBuilder<T extends CsName> {

        private final List<T> names = new ArrayList<>();
        private final String resourceKeyPrefix;
        private final Class<? extends T> clazz;

        public CsTimeFormatBuilder(final String resourceKeyPrefix) {
            this(resourceKeyPrefix, (Class<? extends T>) CsName.class);
        }

        public CsTimeFormatBuilder(final String resourceKeyPrefix, final Class<? extends T> clazz) {
            this.resourceKeyPrefix = resourceKeyPrefix;
            this.clazz = clazz;
        }

        public CsTimeFormatBuilder addFutureName(final String name, long limit) {
            return addName(true, name, limit);
        }

        public CsTimeFormatBuilder addPastName(final String name, long limit) {
            return addName(false, name, limit);
        }

        public CsTimeFormatBuilder addNames(final String name, long limit) {
            return this.addFutureName(name, limit).addPastName(name, limit);
        }

        private CsTimeFormatBuilder addName(boolean isFuture, final String name, long limit) {
            if (Objects.isNull(name)) {
                throw new IllegalArgumentException("ERROR: name is not provided");
            }
            final T csName = this.createCsName();
            csName.setFuture(isFuture);
            csName.setValue(name);
            csName.setThreshold(limit);
            //this.names.add((T) new CsName(isFuture, name, limit));
            this.names.add(csName);
            return this;
        }

        protected T createCsName() {
            return CUtils.getInstance(this.clazz);
        }

        public CsTimeFormat build(final ResourceBundle bundle) {
            return new CsTimeFormat(this.resourceKeyPrefix, bundle, this.names);
        }
    }
}
