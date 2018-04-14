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
import java.time.temporal.TemporalAmount;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom class to track operational transactions by datetime units
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class BaseTransaction {

    private Long id;
    private Long paramType;
    private Long value;
    private TimeUnit unit;
    private Long version;
    private String settingType;

    public Long getValue() {
        return !TimeUnit.NONE.equals(this.unit) ? this.value : null;
    }

    public Duration getDurationValue() {
        if (TimeUnit.MILLISECOND.equals(this.unit) || TimeUnit.SECOND.equals(this.unit) || TimeUnit.MINUTE.equals(this.unit) || TimeUnit.HOUR.equals(this.unit)) {
            return Objects.nonNull(this.value) ? this.unit.getDuration(this.value).orElse(null) : null;
        }
        return null;
    }

    public TemporalAmount getPeriodValue() {
        if (TimeUnit.DAY.equals(this.unit) || TimeUnit.WORKING_DAY.equals(this.unit) || TimeUnit.WEEK.equals(this.unit) || TimeUnit.MONTH.equals(this.unit) || TimeUnit.YEAR.equals(this.unit)) {
            return Objects.nonNull(this.value) ? this.unit.getPeriod(this.value).orElse(null) : null;
        }
        return null;
    }
}
