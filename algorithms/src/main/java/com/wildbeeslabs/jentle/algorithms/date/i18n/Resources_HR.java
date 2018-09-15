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

import com.wildbeeslabs.jentle.algorithms.date.IDuration;
import com.wildbeeslabs.jentle.algorithms.date.ITimeFormat;
import com.wildbeeslabs.jentle.algorithms.date.ITimeFormatProvider;
import com.wildbeeslabs.jentle.algorithms.date.ITimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.SimpleTimeFormat;
import static com.wildbeeslabs.jentle.algorithms.date.i18n.Resources.LOGGER;
import com.wildbeeslabs.jentle.algorithms.date.units.DayTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.HourTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MillenniumTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MinuteTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MonthTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.WeekTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.YearTimeUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Default resources bundle [HR]
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Resources_HR extends Resources implements ITimeFormatProvider {

    private static final Object[][] OBJECTS = new Object[][]{
        {"CenturyPattern", "%n %u"},
        {"CenturyFuturePrefix", "za "},
        {"CenturyFutureSuffix", ""},
        {"CenturyPastPrefix", ""},
        {"CenturyPastSuffix", " unatrag"},
        {"CenturySingularName", "stoljeće"},
        {"CenturyPluralName", "stoljeća"},
        {"DayPattern", "%n %u"},
        {"DayFuturePrefix", "za "},
        {"DayFutureSuffix", ""},
        {"DayPastPrefix", "prije "},
        {"DayPastSuffix", ""},
        {"DaySingularName", "dan"},
        {"DayPluralName", "dana"},
        {"DecadePattern", "%n %u"},
        {"DecadeFuturePrefix", "za "},
        {"DecadeFutureSuffix", ""},
        {"DecadePastPrefix", "prije "},
        {"DecadePastSuffix", ""},
        {"DecadeSingularName", "desetljeće"},
        {"DecadePluralName", "desetljeća"},
        {"HourPattern", "%n %u"},
        {"HourFuturePrefix", "za "},
        {"HourFutureSuffix", ""},
        {"HourPastPrefix", "prije "},
        {"HourPastSuffix", ""},
        {"HourSingularName", "sat"},
        {"HourPluralName", "sati"},
        {"JustNowPattern", "%u"},
        {"JustNowFuturePrefix", "za nekoliko trenutaka"},
        {"JustNowFutureSuffix", ""},
        {"JustNowPastPrefix", "prije nekoliko trenutaka"},
        {"JustNowPastSuffix", ""},
        {"JustNowSingularName", ""},
        {"JustNowPluralName", ""},
        {"MillenniumPattern", "%n %u"},
        {"MillenniumFuturePrefix", "za "},
        {"MillenniumFutureSuffix", ""},
        {"MillenniumPastPrefix", "prije "},
        {"MillenniumPastSuffix", ""},
        {"MillenniumSingularName", "tisućljeće"},
        {"MillenniumPluralName", "tisućljeća"},
        {"MillisecondPattern", "%n %u"},
        {"MillisecondFuturePrefix", "za "},
        {"MillisecondFutureSuffix", ""},
        {"MillisecondPastPrefix", "prije "},
        {"MillisecondPastSuffix", ""},
        {"MillisecondSingularName", "milisekunda"},
        {"MillisecondPluralName", "milisekunda"},
        {"MinutePattern", "%n %u"},
        {"MinuteFuturePrefix", "za "},
        {"MinuteFutureSuffix", ""},
        {"MinutePastPrefix", "prije "},
        {"MinutePastSuffix", ""},
        {"MinuteSingularName", "minuta"},
        {"MinutePluralName", "minuta"},
        {"MonthPattern", "%n %u"},
        {"MonthFuturePrefix", "za "},
        {"MonthFutureSuffix", ""},
        {"MonthPastPrefix", "prije "},
        {"MonthPastSuffix", ""},
        {"MonthSingularName", "mjesec"},
        {"MonthPluralName", "mjeseca"},
        {"SecondPattern", "%n %u"},
        {"SecondFuturePrefix", "za "},
        {"SecondFutureSuffix", ""},
        {"SecondPastPrefix", "prije "},
        {"SecondPastSuffix", ""},
        {"SecondSingularName", "sekunda"},
        {"SecondPluralName", "sekundi"},
        {"WeekPattern", "%n %u"},
        {"WeekFuturePrefix", "za "},
        {"WeekFutureSuffix", ""},
        {"WeekPastPrefix", "prije "},
        {"WeekPastSuffix", ""},
        {"WeekSingularName", "tjedan"},
        {"WeekPluralName", "tjedna"},
        {"YearPattern", "%n %u"},
        {"YearFuturePrefix", "za "},
        {"YearFutureSuffix", ""},
        {"YearPastPrefix", "prije "},
        {"YearPastSuffix", ""},
        {"YearSingularName", "godina"},
        {"YearPluralName", "godina"},
        {"AbstractTimeUnitPattern", ""},
        {"AbstractTimeUnitFuturePrefix", ""},
        {"AbstractTimeUnitFutureSuffix", ""},
        {"AbstractTimeUnitPastPrefix", ""},
        {"AbstractTimeUnitPastSuffix", ""},
        {"AbstractTimeUnitSingularName", ""},
        {"AbstractTimeUnitPluralName", ""}};

    @Override
    public Object[][] getResources() {
        return Resources_HR.OBJECTS;
    }

    @Override
    public ITimeFormat getFormat(final ITimeUnit timeUnit) {
        if (timeUnit instanceof MinuteTimeUnit) {
            return new HrTimeFormatBuilder("Minute").addNames("minutu", 1)
                    .addNames("minute", 4).addNames("minuta", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof HourTimeUnit) {
            return new HrTimeFormatBuilder("Hour").addNames("sat", 1)
                    .addNames("sata", 4).addNames("sati", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof DayTimeUnit) {
            return new HrTimeFormatBuilder("Day").addNames("dan", 1)
                    .addNames("dana", 4).addNames("dana", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof WeekTimeUnit) {
            return new HrTimeFormatBuilder("Week").addNames("tjedan", 1)
                    .addNames("tjedna", 4).addNames("tjedana", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof MonthTimeUnit) {
            return new HrTimeFormatBuilder("Month").addNames("mjesec", 1)
                    .addNames("mjeseca", 4).addNames("mjeseci", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof YearTimeUnit) {
            return new HrTimeFormatBuilder("Year").addNames("godinu", 1)
                    .addNames("godine", 4).addNames("godina", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof MillenniumTimeUnit) {
            return new HrTimeFormatBuilder("Millennium")
                    .addNames("tisućljeće", 1).addNames("tisućljeća", Long.MAX_VALUE)
                    .build(this);
        }
        return null;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    private static class HrName implements Comparable<Resources_HR.HrName> {

        private final boolean isFuture;
        private final String value;
        private final Long threshold;

        @Override
        public int compareTo(final Resources_HR.HrName obj) {
            return this.threshold.compareTo(obj.getThreshold());
        }
    }

    private static class HrTimeFormat extends SimpleTimeFormat implements ITimeFormat {

        private final List<Resources_HR.HrName> futureNames = new ArrayList<>();
        private final List<Resources_HR.HrName> pastNames = new ArrayList<>();

        public HrTimeFormat(final String resourceKeyPrefix, final ResourceBundle bundle, final Collection<Resources_HR.HrName> names) {
            setPattern(bundle.getString(resourceKeyPrefix + "Pattern"));
            setFuturePrefix(bundle.getString(resourceKeyPrefix + "FuturePrefix"));
            setFutureSuffix(bundle.getString(resourceKeyPrefix + "FutureSuffix"));
            setPastPrefix(bundle.getString(resourceKeyPrefix + "PastPrefix"));
            setPastSuffix(bundle.getString(resourceKeyPrefix + "PastSuffix"));
            setSingularName(bundle.getString(resourceKeyPrefix + "SingularName"));
            setPluralName(bundle.getString(resourceKeyPrefix + "PluralName"));

            try {
                setFuturePluralName(bundle.getString(resourceKeyPrefix + "FuturePluralName"));
            } catch (final Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future plural name by key=%s", resourceKeyPrefix + "FuturePluralName"), ex);
            }
            try {
                setFutureSingularName(bundle.getString(resourceKeyPrefix + "FutureSingularName"));
            } catch (final Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future singular name by key=%s", resourceKeyPrefix + "FutureSingularName"), ex);
            }
            try {
                setPastPluralName(bundle.getString(resourceKeyPrefix + "PastPluralName"));
            } catch (final Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set past plural name by key=%s", resourceKeyPrefix + "PastPluralName"), ex);
            }
            try {
                setPastSingularName(bundle.getString(resourceKeyPrefix + "PastSingularName"));
            } catch (final Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future plural name by key=%s", resourceKeyPrefix + "PastSingularName"), ex);
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

        private String getGramaticallyCorrectName(long quantity, final List<Resources_HR.HrName> names) {
            for (final Resources_HR.HrName name : names) {
                if (name.getThreshold() >= quantity) {
                    return name.getValue();
                }
            }
            throw new IllegalStateException("Invalid resource bundle configuration");
        }

        @Override
        protected String getGramaticallyCorrectName(final IDuration duration, boolean round) {
            final long quantity = Math.abs(this.getQuantity(duration, round));
            if (duration.isInFuture()) {
                return this.getGramaticallyCorrectName(quantity, this.futureNames);
            }
            return this.getGramaticallyCorrectName(quantity, this.pastNames);
        }
    }

    private static class HrTimeFormatBuilder {

        private final List<Resources_HR.HrName> names = new ArrayList<>();
        private final String resourceKeyPrefix;

        public HrTimeFormatBuilder(final String resourceKeyPrefix) {
            this.resourceKeyPrefix = resourceKeyPrefix;
        }

        private HrTimeFormatBuilder addName(boolean isFuture, final String name, long limit) {
            if (Objects.isNull(name)) {
                throw new IllegalArgumentException("ERROR: name is not provided");
            }
            this.names.add(new Resources_HR.HrName(isFuture, name, limit));
            return this;
        }

        public HrTimeFormatBuilder addNames(final String name, long limit) {
            return addName(true, name, limit).addName(false, name, limit);
        }

        public HrTimeFormat build(final ResourceBundle bundle) {
            return new HrTimeFormat(this.resourceKeyPrefix, bundle, this.names);
        }
    }
}
