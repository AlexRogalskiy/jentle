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
package com.wildbeeslabs.jentel.algorithms.date;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom local date period implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class LocalDatePeriod {

    private LocalDate start;
    private LocalDate end;

    public LocalDatePeriod(final LocalDate start, final LocalDate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Check whether date periods intersect
     *
     * @param period input date period
     * @return true - if the date periods intersect, false - otherwise
     */
    public boolean isIntersect(final LocalDatePeriod period) {
        if (Objects.isNull(period)) {
            return false;
        }
        return (this.end.isAfter(period.start)) && (this.start.isBefore(period.end));
    }

    /**
     * Check whether current date period is including input date period frames
     *
     * @param period input date period
     * @return true - if the current date period includes input date period
     * borders, false - otherwise
     */
    public boolean isInclude(final LocalDatePeriod period) {
        if (Objects.isNull(period)) {
            return false;
        }
        return (this.start.isBefore(period.start) && (this.end.isAfter(period.end)));
    }

    /**
     * Check whether current date period is within input date period frames
     *
     * @param period input date period
     * @return true - if the current date period is within input date period
     * borders, false - otherwise
     */
    public boolean isWithin(final LocalDatePeriod period) {
        if (Objects.isNull(period)) {
            return false;
        }
        return (this.start.isAfter(period.start) && (this.end.isBefore(period.end)));
    }
}
