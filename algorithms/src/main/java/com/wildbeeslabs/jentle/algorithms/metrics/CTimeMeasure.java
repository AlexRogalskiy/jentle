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
package com.wildbeeslabs.jentle.algorithms.metrics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom time measure implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CTimeMeasure {

    /**
     * Default time measure resolution
     */
    protected final static double DEFAULT_RESOLUTION = 100.0;

    /**
     * List of measurements.
     */
    private final List measures;
    /**
     * Message formatter.
     */
    private final MessageFormat formatter;
    /**
     * Current time measurement.
     */
    private CMeasure current;

    /**
     * Simple TimeMesaure with default format.
     */
    public CTimeMeasure() {
        this(new MessageFormat("{0}\t: {1}\t {2}x\n"));
    }

    /**
     * A new TimeMeasure which reports in a specific format. The format is a
     * standard MessageFormat with the following variables:
     * <ul>
     * <li><code>{0}</code> the measurement comment</li>
     * <li><code>{1}</code> the time it took</li>
     * <li><code>{2}</code> how many times this is faster than the slowest
     * measurement</li>
     * </ul>
     *
     * @param formatter message formatter instance
     */
    public CTimeMeasure(final MessageFormat formatter) {
        this.measures = new ArrayList();
        this.formatter = formatter;
    }

    /**
     * Reset of measurements.
     */
    public void reset() {
        this.measures.clear();
    }

    /**
     * Starts new time measurement.
     *
     * @param comment - measure comment info
     */
    public void start(final String comment) {
        this.current = new CMeasure(comment);
    }

    /**
     * Stops current time measurement and store it.
     */
    public void stop() {
        if (Objects.nonNull(this.current)) {
            this.current.stop();
            this.measures.add(current);
            this.current = null;
        }
    }

    /**
     * Finds the time duration of the longest / shortest time interval.
     *
     * @param findShortest flag of time interval (true - for the shortest time
     * interval, false - for the longest).
     */
    private long findReferenceValue(boolean findShortest) {
        long result = findShortest ? Long.MAX_VALUE : -1;
        final Iterator<CMeasure> it = this.measures.iterator();
        while (it.hasNext()) {
            final CMeasure m = it.next();
            result = (findShortest
                    ? Math.min(result, m.getDuration())
                    : Math.max(result, m.getDuration()));
        }
        return result;
    }

    /**
     * Returns formatted measurement summary (for the longest time interval) of
     * all results.
     *
     * @return formatted measurement summary
     */
    public String print() {
        return this.print(Boolean.FALSE);
    }

    /**
     * Creates formatted output (using the MessageFormat) of all results.
     *
     * @param shortestIsReference flag of time interval (true - for the shortest
     * time interval, false - for the longest).
     * @return formatted measurement info
     */
    public String print(boolean shortestIsReference) {
        final StringBuilder result = new StringBuilder();
        long reference = this.findReferenceValue(shortestIsReference);
        final Iterator<CMeasure> it = this.measures.iterator();
        while (it.hasNext()) {
            final CMeasure m = it.next();
            String factor = " -- ";
            long duration = m.getDuration();
            if (reference > 0) {
                long tmp = (long) ((duration * CTimeMeasure.DEFAULT_RESOLUTION) / reference);
                factor = String.valueOf(tmp / CTimeMeasure.DEFAULT_RESOLUTION);
            }
            final Object[] args = {m.getComment(), (duration + "ms"), factor};
            result.append(this.formatter.format(args));
        }
        return result.toString();
    }

    /**
     * A class to store measurement info
     */
    @Data
    @EqualsAndHashCode
    @ToString
    private final static class CMeasure {

        /**
         * Start time of time.
         */
        private final long start;
        /**
         * End time of time.
         */
        private long stop;
        /**
         * Duration of time.
         */
        private long duration;
        /**
         * Measure description.
         */
        private String comment;

        public CMeasure() {
            this(StringUtils.EMPTY);
        }

        public CMeasure(final String comment) {
            this.start = System.currentTimeMillis();
            this.comment = comment;
        }

        public void stop() {
            this.stop = System.currentTimeMillis();
            this.duration = this.stop - this.start;
        }
    }
}
