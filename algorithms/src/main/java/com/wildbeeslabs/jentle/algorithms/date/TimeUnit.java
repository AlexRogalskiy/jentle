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
package com.wildbeeslabs.jentle.algorithms.date;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import lombok.ToString;

/**
 *
 * Time unit enumeration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@ToString
public enum TimeUnit {

    MILLISECOND {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofMillis(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.empty();
                }
            },
    SECOND {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofSeconds(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.empty();
                }
            },
    MINUTE {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofMinutes(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.empty();
                }
            },
    HOUR {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofHours(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.empty();
                }
            },
    DAY {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofDays(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.of(Period.ofDays(Long.valueOf(value).intValue()));
                }
            },
    WORKING_DAY {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofDays(value));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.of(Period.ofDays(Long.valueOf(value).intValue()));
                }
            },
    WEEK {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.of(Duration.ofDays(value * 7));
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.of(Period.ofWeeks(Long.valueOf(value).intValue()));
                }
            },
    MONTH {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.empty();
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.of(Period.ofMonths(Long.valueOf(value).intValue()));
                }
            },
    YEAR {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.empty();
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.of(Period.ofYears(Long.valueOf(value).intValue()));
                }
            },
    NONE {
                @Override
                public Optional<Duration> getDuration(long value) {
                    return Optional.empty();
                }

                @Override
                public Optional<TemporalAmount> getPeriod(long value) {
                    return Optional.empty();
                }
            };

    /**
     * Get type of time unit by label
     *
     * @param code - time unit label
     * @return time unit type
     */
    public static TimeUnit getByCode(final String code) {
        if (!StringUtils.isEmpty(code)) {
            for (final TimeUnit unit : values()) {
                if (unit.name().equalsIgnoreCase(code)) {
                    return unit;
                }
            }
        }
        return TimeUnit.NONE;
    }

    /**
     * Get duration of datetime period
     *
     * @param value - datetime period
     * @return duration of datetime period
     */
    public abstract Optional<Duration> getDuration(long value);

    /**
     * Get amount of datetime by period
     *
     * @param value - datetime period
     * @return amount of time by period
     */
    public abstract Optional<TemporalAmount> getPeriod(long value);

}
