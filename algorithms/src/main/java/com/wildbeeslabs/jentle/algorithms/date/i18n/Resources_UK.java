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
import com.wildbeeslabs.jentle.algorithms.date.time.unit.CenturyTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.DayTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.DecadeTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.HourTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.MillenniumTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.MillisecondTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.MinuteTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.MonthTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.NowTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.SecondTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.WeekTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.time.unit.YearTimeUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Default resources bundle [UK]
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Resources_UK extends Resources implements ITimeFormatProvider {

    private static final Object[][] OBJECTS = new Object[0][0];
    private static final int tolerance = 50;
    // see http://translate.sourceforge.net/wiki/l10n/pluralforms
    private static final int slavicPluralForms = 3;

    private static class TimeFormatAided implements ITimeFormat {

        private final String[] plurals;

        public TimeFormatAided(final String... plurals) {
            if (plurals.length != Resources_UK.slavicPluralForms) {
                throw new IllegalArgumentException(String.format("ERROR: Wrong plural forms number for slavic language = %s, expected = %s", plurals.length, Resources_UK.slavicPluralForms));
            }
            this.plurals = plurals;
        }

        @Override
        public String format(final IDuration duration) {
            long quantity = duration.getQuantityRounded(tolerance);
            final StringBuilder result = new StringBuilder();
            result.append(quantity);
            return result.toString();
        }

        @Override
        public String formatUnrounded(final IDuration duration) {
            long quantity = duration.getQuantity();
            final StringBuilder result = new StringBuilder();
            result.append(quantity);
            return result.toString();
        }

        @Override
        public String decorate(final IDuration duration, String time) {
            return performDecoration(
                    duration.isInPast(),
                    duration.isInFuture(),
                    duration.getQuantityRounded(Resources_UK.tolerance),
                    time);
        }

        @Override
        public String decorateUnrounded(final IDuration duration, String time) {
            return performDecoration(
                    duration.isInPast(),
                    duration.isInFuture(),
                    duration.getQuantity(),
                    time);
        }

        private String performDecoration(boolean past, boolean future, long quantity, String time) {
            // consider http://translate.sourceforge.net/wiki/l10n/pluralforms
            int pluralIdx = (quantity % 10 == 1 && quantity % 100 != 11 ? 0 : (quantity % 10 >= 2 && quantity % 10 <= 4 && (quantity % 100 < 10 || quantity % 100 >= 20)) ? 1 : 2);
            if (pluralIdx > Resources_UK.slavicPluralForms) {
                throw new IllegalArgumentException(String.format("ERROR: Wrong plural forms number for slavic language = %s, expected = %s", pluralIdx, Resources_UK.slavicPluralForms));
            }
            final StringBuilder result = new StringBuilder();
            if (future) {
                result.append("через ");
            }
            result.append(time);
            result.append(StringUtils.SPACE);
            result.append(this.plurals[pluralIdx]);
            if (past) {
                result.append(" тому");
            }
            return result.toString();
        }
    }

    @Override
    public Object[][] getResources() {
        return Resources_UK.OBJECTS;
    }

    @Override
    public ITimeFormat getFormat(final ITimeUnit timeUnit) {
        if (timeUnit instanceof NowTimeUnit) {
            return new ITimeFormat() {
                @Override
                public String format(final IDuration duration) {
                    return this.performFormat(duration);
                }

                @Override
                public String formatUnrounded(final IDuration duration) {
                    return this.performFormat(duration);
                }

                private String performFormat(final IDuration duration) {
                    if (duration.isInFuture()) {
                        return "зараз";
                    }
                    if (duration.isInPast()) {
                        return "щойно";
                    }
                    return null;
                }

                @Override
                public String decorate(final IDuration duration, final String time) {
                    return time;
                }

                @Override
                public String decorateUnrounded(final IDuration duration, final String time) {
                    return time;
                }
            };
        } else if (timeUnit instanceof CenturyTimeUnit) {
            return new TimeFormatAided("століття", "століття", "столітть");
        } else if (timeUnit instanceof DayTimeUnit) {
            return new TimeFormatAided("день", "дні", "днів");
        } else if (timeUnit instanceof DecadeTimeUnit) {
            return new TimeFormatAided("десятиліття", "десятиліття", "десятиліть");
        } else if (timeUnit instanceof HourTimeUnit) {
            return new TimeFormatAided("годину", "години", "годин");
        } else if (timeUnit instanceof MillenniumTimeUnit) {
            return new TimeFormatAided("тисячоліття", "тисячоліття", "тисячоліть");
        } else if (timeUnit instanceof MillisecondTimeUnit) {
            return new TimeFormatAided("мілісекунду", "мілісекунди", "мілісекунд");
        } else if (timeUnit instanceof MinuteTimeUnit) {
            return new TimeFormatAided("хвилину", "хвилини", "хвилин");
        } else if (timeUnit instanceof MonthTimeUnit) {
            return new TimeFormatAided("місяць", "місяці", "місяців");
        } else if (timeUnit instanceof SecondTimeUnit) {
            return new TimeFormatAided("секунду", "секунди", "секунд");
        } else if (timeUnit instanceof WeekTimeUnit) {
            return new TimeFormatAided("тиждень", "тижні", "тижнів");
        } else if (timeUnit instanceof YearTimeUnit) {
            return new TimeFormatAided("рік", "роки", "років");
        }
        return null;
    }
}
