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
import com.wildbeeslabs.jentle.algorithms.date.time.ITimeFormatProvider;
import com.wildbeeslabs.jentle.algorithms.date.time.ITimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.DecadeTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.MillenniumTimeUnit;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Default resources bundle [JA]
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Resources_JA extends Resources implements ITimeFormatProvider {

    private static final Object[][] OBJECTS = new Object[][]{
        {"CenturyPattern", "%n%u"},
        {"CenturyFuturePrefix", "今から"},
        {"CenturyFutureSuffix", "後"},
        {"CenturyPastPrefix", ""},
        {"CenturyPastSuffix", "前"},
        {"CenturySingularName", "世紀"},
        {"CenturyPluralName", "世紀"},
        {"DayPattern", "%n%u"},
        {"DayFuturePrefix", "今から"},
        {"DayFutureSuffix", "後"},
        {"DayPastPrefix", ""},
        {"DayPastSuffix", "前"},
        {"DaySingularName", "日"},
        {"DayPluralName", "日"},
        {"DecadePattern", "%n%u"},
        {"DecadeFuturePrefix", "今から"},
        {"DecadeFutureSuffix", "後"},
        {"DecadePastPrefix", ""},
        {"DecadePastSuffix", "前"},
        {"DecadeSingularName", "年"},
        {"DecadePluralName", "年"},
        {"HourPattern", "%n%u"},
        {"HourFuturePrefix", "今から"},
        {"HourFutureSuffix", "後"},
        {"HourPastPrefix", ""},
        {"HourPastSuffix", "前"},
        {"HourSingularName", "時間"},
        {"HourPluralName", "時間"},
        {"JustNowPattern", "%u"},
        {"JustNowFuturePrefix", "今から"},
        {"JustNowFutureSuffix", "すぐ"},
        {"JustNowPastPrefix", ""},
        {"JustNowPastSuffix", "たった今"},
        {"JustNowSingularName", ""},
        {"JustNowPluralName", ""},
        {"MillenniumPattern", "%n%u"},
        {"MillenniumFuturePrefix", "今から"},
        {"MillenniumFutureSuffix", "後"},
        {"MillenniumPastPrefix", ""},
        {"MillenniumPastSuffix", "前"},
        {"MillenniumSingularName", "年"},
        {"MillenniumPluralName", "年"},
        {"MillisecondPattern", "%n%u"},
        {"MillisecondFuturePrefix", "今から"},
        {"MillisecondFutureSuffix", "後"},
        {"MillisecondPastPrefix", ""},
        {"MillisecondPastSuffix", "前"},
        {"MillisecondSingularName", "ミリ秒"},
        {"MillisecondPluralName", "ミリ秒"},
        {"MinutePattern", "%n%u"},
        {"MinuteFuturePrefix", "今から"},
        {"MinuteFutureSuffix", "後"},
        {"MinutePastPrefix", ""},
        {"MinutePastSuffix", "前"},
        {"MinuteSingularName", "分"},
        {"MinutePluralName", "分"},
        {"MonthPattern", "%n%u"},
        {"MonthFuturePrefix", "今から"},
        {"MonthFutureSuffix", "後"},
        {"MonthPastPrefix", ""},
        {"MonthPastSuffix", "前"},
        {"MonthSingularName", "ヶ月"},
        {"MonthPluralName", "ヶ月"},
        {"SecondPattern", "%n%u"},
        {"SecondFuturePrefix", "今から"},
        {"SecondFutureSuffix", "後"},
        {"SecondPastPrefix", ""},
        {"SecondPastSuffix", "前"},
        {"SecondSingularName", "秒"},
        {"SecondPluralName", "秒"},
        {"WeekPattern", "%n%u"},
        {"WeekFuturePrefix", "今から"},
        {"WeekFutureSuffix", "後"},
        {"WeekPastPrefix", ""},
        {"WeekPastSuffix", "前"},
        {"WeekSingularName", "週間"},
        {"WeekPluralName", "週間"},
        {"YearPattern", "%n%u"},
        {"YearFuturePrefix", "今から"},
        {"YearFutureSuffix", "後"},
        {"YearPastPrefix", ""},
        {"YearPastSuffix", "前"},
        {"YearSingularName", "年"},
        {"YearPluralName", "年"},
        {"AbstractTimeUnitPattern", ""},
        {"AbstractTimeUnitFuturePrefix", ""},
        {"AbstractTimeUnitFutureSuffix", ""},
        {"AbstractTimeUnitPastPrefix", ""},
        {"AbstractTimeUnitPastSuffix", ""},
        {"AbstractTimeUnitSingularName", ""},
        {"AbstractTimeUnitPluralName", ""}};

    @Override
    public Object[][] getResources() {
        return Resources_JA.OBJECTS;
    }

    private volatile ConcurrentMap<ITimeUnit, ITimeFormat> formatMap = new ConcurrentHashMap<>();

    @Override
    public ITimeFormat getFormat(final ITimeUnit timeUnit) {
        if (!this.formatMap.containsKey(timeUnit)) {
            this.formatMap.putIfAbsent(timeUnit, new JaTimeFormat(this, timeUnit));
        }
        return this.formatMap.get(timeUnit);
    }

    @Data
    @EqualsAndHashCode
    @ToString
    private static class JaTimeFormat implements ITimeFormat {

        private static final String NEGATIVE = "-";
        public static final String SIGN = "%s";
        public static final String QUANTITY = "%n";
        public static final String UNIT = "%u";

        @Getter(AccessLevel.NONE)
        @Setter(AccessLevel.NONE)
        private final ResourceBundle bundle;
        private String singularName = null;
        private String pluralName = null;
        private String futureSingularName = null;
        private String futurePluralName = null;
        private String pastSingularName = null;
        private String pastPluralName = null;

        private String pattern = null;
        private String futurePrefix = null;
        private String futureSuffix = null;
        private String pastPrefix = null;
        private String pastSuffix = null;
        private int roundingTolerance = 50;

        public JaTimeFormat(final ResourceBundle bundle, final ITimeUnit unit) {
            this.bundle = bundle;
            setPattern(bundle.getString(getUnitName(unit) + "Pattern"));
            setFuturePrefix(bundle.getString(getUnitName(unit) + "FuturePrefix"));
            setFutureSuffix(bundle.getString(getUnitName(unit) + "FutureSuffix"));
            setPastPrefix(bundle.getString(getUnitName(unit) + "PastPrefix"));
            setPastSuffix(bundle.getString(getUnitName(unit) + "PastSuffix"));
            setSingularName(bundle.getString(getUnitName(unit) + "SingularName"));
            setPluralName(bundle.getString(getUnitName(unit) + "PluralName"));

            try {
                setFuturePluralName(bundle.getString(this.getUnitName(unit) + "FuturePluralName"));
            } catch (Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future plural name by key=%s", this.getUnitName(unit) + "FuturePluralName"), ex);
            }
            try {
                setFutureSingularName((bundle.getString(this.getUnitName(unit) + "FutureSingularName")));
            } catch (Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future singular name by key=%s", this.getUnitName(unit) + "FutureSingularName"), ex);
            }
            try {
                setPastPluralName((bundle.getString(this.getUnitName(unit) + "PastPluralName")));
            } catch (Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set past plural name by key=%s", this.getUnitName(unit) + "PastPluralName"), ex);
            }
            try {
                setPastSingularName((bundle.getString(this.getUnitName(unit) + "PastSingularName")));
            } catch (Exception ex) {
                LOGGER.error(String.format("ERROR: cannot set future plural name by key=%s", this.getUnitName(unit) + "PastSingularName"), ex);
            }
        }

        private String getUnitName(final ITimeUnit timeUnit) {
            return timeUnit.getClass().getSimpleName();
        }

        @Override
        public String format(final IDuration duration) {
            return this.format(duration, true);
        }

        @Override
        public String formatUnrounded(final IDuration duration) {
            return this.format(duration, false);
        }

        private String format(final IDuration duration, boolean round) {
            String sign = this.getSign(duration);
            String unit = this.getGramaticallyCorrectName(duration, round);
            long quantity = this.getQuantity(duration, round);
            if (duration.getUnit() instanceof DecadeTimeUnit) {
                quantity *= 10;
            }
            if (duration.getUnit() instanceof MillenniumTimeUnit) {
                quantity *= 1000;
            }
            return applyPattern(sign, unit, quantity);
        }

        private String applyPattern(final String sign, final String unit, long quantity) {
            String result = this.getPattern(quantity).replaceAll(SIGN, sign);
            result = result.replaceAll(QUANTITY, String.valueOf(quantity));
            result = result.replaceAll(UNIT, unit);
            return result;
        }

        protected String getPattern(long quantity) {
            return this.pattern;
        }

        public String getPattern() {
            return this.pattern;
        }

        protected long getQuantity(final IDuration duration, boolean round) {
            return Math.abs(round ? duration.getQuantityRounded(roundingTolerance) : duration.getQuantity());
        }

        protected String getGramaticallyCorrectName(final IDuration duration, boolean round) {
            String result = getSingularName(duration);
            if ((Math.abs(getQuantity(duration, round)) == 0) || (Math.abs(getQuantity(duration, round)) > 1)) {
                result = getPluralName(duration);
            }
            return result;
        }

        private String getSign(final IDuration duration) {
            if (duration.getQuantity() < 0) {
                return NEGATIVE;
            }
            return StringUtils.EMPTY;
        }

        private String getSingularName(final IDuration duration) {
            if (duration.isInFuture() && Objects.nonNull(this.futureSingularName) && this.futureSingularName.length() > 0) {
                return this.futureSingularName;
            } else if (duration.isInPast() && Objects.nonNull(this.pastSingularName) && this.pastSingularName.length() > 0) {
                return this.pastSingularName;
            }
            return this.singularName;
        }

        private String getPluralName(final IDuration duration) {
            if (duration.isInFuture() && Objects.nonNull(this.futurePluralName) && this.futureSingularName.length() > 0) {
                return this.futurePluralName;
            } else if (duration.isInPast() && Objects.nonNull(this.pastPluralName) && this.pastSingularName.length() > 0) {
                return this.pastPluralName;
            }
            return this.pluralName;
        }

        @Override
        public String decorate(final IDuration duration, final String time) {
            final StringBuilder result = new StringBuilder();
            if (duration.isInPast()) {
                result.append(this.pastPrefix).append(time).append(this.pastSuffix);
            } else {
                result.append(this.futurePrefix).append(time).append(this.futureSuffix);
            }
            return result.toString().replaceAll("\\s+", StringUtils.SPACE).trim();
        }

        @Override
        public String decorateUnrounded(final IDuration duration, final String time) {
            return decorate(duration, time);
        }

        public JaTimeFormat setPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }

        public JaTimeFormat setFuturePrefix(final String futurePrefix) {
            this.futurePrefix = futurePrefix.trim();
            return this;
        }

        public JaTimeFormat setFutureSuffix(final String futureSuffix) {
            this.futureSuffix = futureSuffix.trim();
            return this;
        }

        public JaTimeFormat setPastPrefix(final String pastPrefix) {
            this.pastPrefix = pastPrefix.trim();
            return this;
        }

        public JaTimeFormat setPastSuffix(final String pastSuffix) {
            this.pastSuffix = pastSuffix.trim();
            return this;
        }

        /**
         * The percentage of the current tolerance {@link ITimeUnit}.getMillisPerUnit() for
         * which the quantity may be rounded up by one.
         *
         * @param roundingTolerance
         * @return
         */
        public JaTimeFormat setRoundingTolerance(final int roundingTolerance) {
            this.roundingTolerance = roundingTolerance;
            return this;
        }

        public JaTimeFormat setSingularName(final String name) {
            this.singularName = name;
            return this;
        }

        public JaTimeFormat setPluralName(final String pluralName) {
            this.pluralName = pluralName;
            return this;
        }

        public JaTimeFormat setFutureSingularName(final String futureSingularName) {
            this.futureSingularName = futureSingularName;
            return this;
        }

        public JaTimeFormat setFuturePluralName(final String futurePluralName) {
            this.futurePluralName = futurePluralName;
            return this;
        }

        public JaTimeFormat setPastSingularName(final String pastSingularName) {
            this.pastSingularName = pastSingularName;
            return this;
        }

        public JaTimeFormat setPastPluralName(final String pastPluralName) {
            this.pastPluralName = pastPluralName;
            return this;
        }
    }
}
