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

import com.wildbeeslabs.jentle.algorithms.date.units.CenturyTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.DayTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.DecadeTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.HourTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MillenniumTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MillisecondTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MinuteTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MonthTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.NowTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.SecondTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.WeekTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.YearTimeUnit;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Default date/time formatter implementation by creating social-networking
 * style timestamps (e.g. "just now", "moments ago", "3 days ago", "within 2
 * months")
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class DateTimeFormatter {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(DateTimeFormatter.class);

    private volatile Date reference;
    private volatile Locale locale = Locale.getDefault();
    private volatile Map<ITimeUnit, ITimeFormat> units = new LinkedHashMap<>();
    private volatile List<ITimeUnit> cachedUnits = new ArrayList<>();

    /**
     * Default constructor
     */
    public DateTimeFormatter() {
        this(null, null);
    }

    /**
     * Accept a {@link Date} timestamp to represent the point of reference for
     * comparison. This may be changed by the user, after construction.
     * <p>
     * See {@code PrettyTime.setReference(Date timestamp)}.
     *
     * @param reference
     */
    public DateTimeFormatter(final Date reference) {
        this(reference, null);
    }

    /**
     * Construct a new instance using the given {@link Locale} instead of the
     * system default.
     *
     * @param locale
     */
    public DateTimeFormatter(final Locale locale) {
        this(null, locale);
    }

    /**
     * Accept a {@link Date} timestamp to represent the point of reference for
     * comparison. This may be changed by the user, after construction. Use the
     * given {@link Locale} instead of the system default.
     *
     * @param reference
     * @param locale
     */
    public DateTimeFormatter(final Date reference, final Locale locale) {
        this.initTimeUnits();
        this.setReference(reference);
        this.setLocale(locale);
    }

    /**
     * Calculate the approximate duration between the referenceDate and date
     *
     * @param date
     * @return
     */
    public IDuration approximateDuration(final Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("ERROR: date provided must not be null.");
        }
        Date ref = this.reference;
        if (Objects.isNull(ref)) {
            ref = this.now();
        }
        long difference = date.getTime() - ref.getTime();
        return this.calculateDuration(difference);
    }

    private void initTimeUnits() {
        this.addUnit(new NowTimeUnit());
        this.addUnit(new MillisecondTimeUnit());
        this.addUnit(new SecondTimeUnit());
        this.addUnit(new MinuteTimeUnit());
        this.addUnit(new HourTimeUnit());
        this.addUnit(new DayTimeUnit());
        this.addUnit(new WeekTimeUnit());
        this.addUnit(new MonthTimeUnit());
        this.addUnit(new YearTimeUnit());
        this.addUnit(new DecadeTimeUnit());
        this.addUnit(new CenturyTimeUnit());
        this.addUnit(new MillenniumTimeUnit());
    }

    private void addUnit(final ResourcesTimeUnit unit) {
        this.registerUnit(unit, new ResourcesTimeFormat(unit));
    }

    private IDuration calculateDuration(final long difference) {
        long absoluteDifference = Math.abs(difference);
        final List<ITimeUnit> timeUnits = this.getUnits();
        final Duration result = new Duration();
        for (int i = 0; i < timeUnits.size(); i++) {
            ITimeUnit unit = timeUnits.get(i);
            long millisPerUnit = Math.abs(unit.getMillisPerUnit());
            long quantity = Math.abs(unit.getMaxQuantity());
            boolean isLastUnit = (i == timeUnits.size() - 1);
            if ((0 == quantity) && !isLastUnit) {
                quantity = timeUnits.get(i + 1).getMillisPerUnit() / unit.getMillisPerUnit();
            }
            if ((millisPerUnit * quantity > absoluteDifference) || isLastUnit) {
                result.setUnit(unit);
                if (millisPerUnit > absoluteDifference) {
                    result.setQuantity(getSign(difference));
                    result.setDelta(0);
                } else {
                    result.setQuantity(difference / millisPerUnit);
                    result.setDelta(difference - result.getQuantity() * millisPerUnit);
                }
                break;
            }
        }
        return result;
    }

    private long getSign(final long difference) {
        if (0 > difference) {
            return -1;
        }
        return 1;
    }

    /**
     * Calculate to the precision of the smallest provided {@link TimeUnit}, the
     * exact duration represented by the difference between the reference
     * timestamp, and {@code then}
     * <p>
     * <b>Note</b>: Precision may be lost if no supplied {@link TimeUnit} is
     * granular enough to represent one millisecond
     *
     * @param date The date to be compared against the reference timestamp, or
     * <i>now</i> if no reference timestamp was provided
     * @return A sorted {@link List} of {@link Duration} objects, from largest
     * to smallest. Each element in the list represents the approximate duration
     * (number of times) that {@link TimeUnit} to fit into the previous
     * element's delta. The first element is the largest {@link TimeUnit} to fit
     * within the total difference between compared dates.
     */
    public List<IDuration> calculatePreciseDuration(final Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("ERROR: date provided must not be null.");
        }
        if (Objects.isNull(this.reference)) {
            this.reference = this.now();
        }
        final List<IDuration> result = new ArrayList<>();
        long difference = date.getTime() - this.reference.getTime();
        IDuration duration = calculateDuration(difference);
        result.add(duration);
        while (0 != duration.getDelta()) {
            duration = calculateDuration(duration.getDelta());
            if (result.size() > 0) {
                final IDuration last = result.get(result.size() - 1);
                if (last.getUnit().equals(duration.getUnit())) {
                    break;
                }
            }
            if (duration.getUnit().isPrecise()) {
                result.add(duration);
            }
        }
        return result;
    }

    /**
     * Format the given {@link Date} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense.
     *
     * @param date the {@link Date} to be formatted
     * @return A formatted string representing {@code then}
     */
    public String format(final Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("ERROR: date provided must not be null.");
        }
        final IDuration duration = approximateDuration(date);
        return format(duration);
    }

    /**
     * Format the given {@link Calendar} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense.
     *
     * @param calendar the {@link Calendar} whose date is to be formatted
     * @return A formatted string representing {@code then}
     */
    public String format(final Calendar calendar) {
        if (Objects.isNull(calendar)) {
            throw new IllegalArgumentException("ERROR: calendar provided must not be null.");
        }
        return format(calendar.getTime());
    }

    /**
     * Format the given {@link Date} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense. Rounding rules
     * are ignored.
     *
     * @param date the {@link Date} to be formatted
     * @return A formatted string representing {@code then}
     */
    public String formatUnrounded(final Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("ERROR: date provided must not be null.");
        }
        final IDuration d = approximateDuration(date);
        return formatUnrounded(d);
    }

    /**
     * Format the given {@link Calendar} object. This method applies the
     * {@link PrettyTime#approximateDuration(Date)} method to perform its
     * calculation. Rounding rules are ignored. If the given {@link Calendar} is
     * <code>null</code>, the current value of
     * {@link System#currentTimeMillis()} will be used instead.
     *
     * @param calendar the {@link Calendar} whose date is to be formatted
     * @return A formatted string representing {@code then}
     */
    public String formatUnrounded(final Calendar calendar) {
        if (Objects.isNull(calendar)) {
            throw new IllegalArgumentException("ERROR: calendar provided must not be null.");
        }
        return formatUnrounded(calendar.getTime());
    }

    /**
     * Format the given {@link IDuration} object, using the {@link ITimeFormat}
     * specified by the {@link ITimeUnit} contained within; also decorate for
     * past/future tense.
     *
     * @param duration the {@link IDuration} to be formatted
     * @return A formatted string representing {@code duration}
     */
    public String format(final IDuration duration) {
        if (Objects.isNull(duration)) {
            throw new IllegalArgumentException("ERROR: duration provided must not be null.");
        }
        final ITimeFormat format = getFormat(duration.getUnit());
        final String time = format.format(duration);
        return format.decorate(duration, time);
    }

    /**
     * Format the given {@link IDuration} object, using the {@link ITimeFormat}
     * specified by the {@link ITimeUnit} contained within; also decorate for
     * past/future tense. Rounding rules are ignored.
     *
     * @param duration the {@link IDuration} to be formatted
     * @return A formatted string representing {@code duration}
     */
    public String formatUnrounded(final IDuration duration) {
        if (Objects.isNull(duration)) {
            throw new IllegalArgumentException("ERROR: duration provided must not be null.");
        }
        final ITimeFormat format = getFormat(duration.getUnit());
        final String time = format.formatUnrounded(duration);
        return format.decorateUnrounded(duration, time);
    }

    /**
     * Format the given {@link IDuration} objects, using the {@link ITimeFormat}
     * specified by the {@link ITimeFormat} contained within. Rounds only the
     * last {@link Duration} object.
     *
     * @param durations the {@link IDuration}s to be formatted
     * @return A list of formatted strings representing {@code durations}
     */
    @SuppressWarnings("null")
    public String format(final List<IDuration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException("ERROR: list of durations provided must not be null.");
        }
        final StringBuilder builder = new StringBuilder();
        IDuration duration = null;
        ITimeFormat format = null;
        for (int i = 0; i < durations.size(); i++) {
            duration = durations.get(i);
            format = getFormat(duration.getUnit());
            // Round only the last element 
            if (i < durations.size() - 1) {
                builder.append(format.formatUnrounded(duration));
                builder.append(StringUtils.SPACE);
            } else {
                builder.append(format.format(duration));
            }
        }
        return format.decorateUnrounded(duration, builder.toString());
    }

    /**
     * Format the given {@link Date} and return a non-relative (not decorated
     * with past or future tense) {@link String} for the approximate duration of
     * its difference between the reference {@link Date}. If the given
     * {@link Date} is <code>null</code>, the current value of
     * {@link System#currentTimeMillis()} will be used instead.
     * <p>
     *
     * @param date the date to be formatted
     * @return A formatted string of the given {@link Date}
     */
    public String formatDuration(final Date date) {
        final IDuration duration = approximateDuration(date);
        return formatDuration(duration);
    }

    /**
     * Format the given {@link Calendar} and return a non-relative (not
     * decorated with past or future tense) {@link String} for the approximate
     * duration of its difference between the reference {@link Date}. If the
     * given {@link Calendar} is <code>null</code>, the current value of
     * {@link System#currentTimeMillis()} will be used instead.
     * <p>
     *
     * @param calendar the date to be formatted
     * @return A formatted string of the given {@link Date}
     */
    public String formatDuration(final Calendar calendar) {
        if (Objects.isNull(calendar)) {
            throw new IllegalArgumentException("ERROR: calendar provided must not be null.");
        }
        return formatDuration(calendar.getTime());
    }

    /**
     * Format the given {@link IDuration} and return a non-relative (not
     * decorated with past or future tense) {@link String} for the approximate
     * duration of the difference between the reference {@link Date} and the
     * given {@link Duration}. If the given {@link IDuration} is
     * <code>null</code>, the current value of
     * {@link System#currentTimeMillis()} will be used instead.
     *
     * @param duration the duration to be formatted
     * @return A formatted string of the given {@link IDuration}
     */
    public String formatDuration(final IDuration duration) {
        if (Objects.isNull(duration)) {
            throw new IllegalArgumentException("ERROR: duration provided must not be null.");
        }
        final ITimeFormat timeFormat = getFormat(duration.getUnit());
        return timeFormat.format(duration);
    }

    /**
     * Format the given {@link Duration} objects, using the {@link TimeFormat}
     * specified by the {@link TimeUnit} contained within. Rounding rules are
     * ignored. If the given {@link Duration} {@link List} is <code>null</code>
     * or empty, the current value of {@link System#currentTimeMillis()} will be
     * used instead.
     *
     * @param durations the {@link IDuration}s to be formatted
     * @return A list of formatted strings representing {@code durations}
     */
    @SuppressWarnings("null")
    public String formatUnrounded(final List<IDuration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException("ERROR: list of durations provided must not be null.");
        }
        final StringBuilder result = new StringBuilder();
        IDuration duration = null;
        ITimeFormat format = null;
        for (int i = 0; i < durations.size(); i++) {
            duration = durations.get(i);
            format = getFormat(duration.getUnit());
            result.append(format.formatUnrounded(duration));
            // Round only the last element 
            if (i < durations.size() - 1) {
                result.append(StringUtils.SPACE);
            }
        }
        return format.decorateUnrounded(duration, result.toString());
    }

    /**
     * Format the given {@link IDuration} {@link List} and return a non-relative
     * (not decorated with past or future tense) {@link String} for the
     * approximate duration of its difference between the reference
     * {@link Date}. If the given {@link IDuration} is <code>null</code>, the
     * current value of {@link System#currentTimeMillis()} will be used instead.
     *
     * @param durations the duration to be formatted
     * @return A formatted string of the given {@link IDuration}
     */
    @SuppressWarnings("UnusedAssignment")
    public String formatDuration(final List<IDuration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException("ERROR: list of durations provided must not be null.");
        }
        final StringBuilder result = new StringBuilder();
        IDuration duration = null;
        ITimeFormat format = null;
        for (int i = 0; i < durations.size(); i++) {
            duration = durations.get(i);
            format = getFormat(duration.getUnit());
            // Round only the last element 
            if (i < durations.size() - 1) {
                result.append(format.formatUnrounded(duration));
                result.append(StringUtils.SPACE);
            } else {
                result.append(format.format(duration));
            }
        }
        return result.toString();
    }

    /**
     * Format the given {@link Date} and return a non-relative (not decorated
     * with past or future tense) {@link String} for the approximate duration of
     * its difference between the reference {@link Date}. Rounding rules are
     * ignored. If the given {@link Date} is <code>null</code>, the current
     * value of {@link System#currentTimeMillis()} will be used instead.
     * <p>
     *
     * @param date the date to be formatted
     * @return A formatted string of the given {@link Date}
     */
    public String formatDurationUnrounded(final Date date) {
        final IDuration duration = approximateDuration(date);
        return formatDurationUnrounded(duration);
    }

    /**
     * Format the given {@link Calendar} and return a non-relative (not
     * decorated with past or future tense) {@link String} for the approximate
     * duration of its difference between the reference {@link Date}. Rounding
     * rules are ignored. If the given {@link Calendar} is <code>null</code>,
     * the current value of {@link System#currentTimeMillis()} will be used
     * instead.
     * <p>
     *
     * @param calendar the date to be formatted
     * @return A formatted string of the given {@link Date}
     */
    public String formatDurationUnrounded(final Calendar calendar) {
        if (Objects.isNull(calendar)) {
            throw new IllegalArgumentException("ERROR: calendar provided must not be null.");
        }
        return formatDurationUnrounded(calendar.getTime());
    }

    /**
     * Format the given {@link IDuration} and return a non-relative (not
     * decorated with past or future tense) {@link String} for the approximate
     * duration of its difference between the reference {@link Date}. Rounding
     * rules are ignored. If the given {@link IDuration} is <code>null</code>,
     * the current value of {@link System#currentTimeMillis()} will be used
     * instead.
     *
     * @param duration the duration to be formatted
     * @return A formatted string of the given {@link IDuration}
     */
    public String formatDurationUnrounded(final IDuration duration) {
        if (Objects.isNull(duration)) {
            throw new IllegalArgumentException("ERROR: duration provided must not be null.");
        }
        final ITimeFormat timeFormat = getFormat(duration.getUnit());
        return timeFormat.formatUnrounded(duration);
    }

    /**
     * Format the given {@link IDuration} {@link List} and return a non-relative
     * (not decorated with past or future tense) {@link String} for the
     * approximate duration of its difference between the reference
     * {@link Date}. Rounding rules are ignored. If the given {@link IDuration}
     * is <code>null</code>, the current value of
     * {@link System#currentTimeMillis()} will be used instead.
     *
     * @param durations the duration to be formatted
     * @return A formatted string of the given {@link IDuration}
     */
    @SuppressWarnings("UnusedAssignment")
    public String formatDurationUnrounded(final List<IDuration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException("ERROR: list of durations provided must not be null.");
        }
        final StringBuilder result = new StringBuilder();
        IDuration duration = null;
        ITimeFormat format = null;
        for (int i = 0; i < durations.size(); i++) {
            duration = durations.get(i);
            format = getFormat(duration.getUnit());
            result.append(format.formatUnrounded(duration));
            // Round only the last element 
            if (i < durations.size() - 1) {
                result.append(StringUtils.SPACE);
            }
        }
        return result.toString();
    }

    /**
     * Get the registered {@link TimeFormat} for the given {@link TimeUnit} or
     * null if none exists.
     *
     * @param unit
     * @return
     */
    public ITimeFormat getFormat(final ITimeUnit unit) {
        if (Objects.isNull(unit)) {
            throw new IllegalArgumentException("ERROR: time unit provided must not be null.");
        }
        if (Objects.nonNull(this.units.get(unit))) {
            return this.units.get(unit);
        }
        return null;
    }

    /**
     * Get the current reference timestamp.
     * <p>
     * See {@code PrettyTime.setReference(Date timestamp)}
     *
     * @return
     */
    public Date getReference() {
        return this.reference;
    }

    /**
     * Set the reference timestamp.
     * <p>
     * If the Date formatted is before the reference timestamp, the format
     * command will produce a String that is in the past tense. If the Date
     * formatted is after the reference timestamp, the format command will
     * produce a string that is in the future tense.
     *
     * @param timestamp
     * @return
     */
    public DateTimeFormatter setReference(final Date timestamp) {
        this.reference = timestamp;
        return this;
    }

    /**
     * Get a {@link List} of the current configured {@link TimeUnit} instances
     * in calculations.
     *
     * @return
     */
    public List<ITimeUnit> getUnits() {
        if (CollectionUtils.isEmpty(this.cachedUnits)) {
            final List<ITimeUnit> result = new ArrayList<>(this.units.keySet());
            Collections.sort(result, new CUtils.CSortComparator<ITimeUnit>());
            this.cachedUnits.addAll(Collections.unmodifiableList(result));
        }
        return this.cachedUnits;
    }

    /**
     * Get the registered {@link TimeUnit} for the given {@link TimeUnit} type
     * or <code>null</code> if none exists.
     *
     * @param <U>
     * @param unitType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <U extends ITimeUnit> U getUnit(final Class<? extends U> unitType) {
        if (Objects.isNull(unitType)) {
            return null;
        }
        for (final ITimeUnit unit : this.units.keySet()) {
            if (unitType.isAssignableFrom(unit.getClass())) {
                return (U) unit;
            }
        }
        return null;
    }

    /**
     * Register the given {@link TimeUnit} and corresponding {@link TimeFormat}
     * instance to be used in calculations. If an entry already exists for the
     * given {@link TimeUnit}, its format will be overwritten with the given
     * {@link TimeFormat}.
     *
     * @param unit
     * @param format
     * @return
     */
    public DateTimeFormatter registerUnit(final ITimeUnit unit, final ITimeFormat format) {
        if (Objects.isNull(unit)) {
            throw new IllegalArgumentException("ERROR: time unit provided must not be null.");
        }
        if (Objects.isNull(format)) {
            throw new IllegalArgumentException("ERROR: time format provided must not be null.");
        }
        this.cachedUnits.clear();
        this.units.put(unit, format);
        if (unit instanceof ILocaleAware) {
            ((ILocaleAware<?>) unit).setLocale(this.locale);
        }
        if (format instanceof ILocaleAware) {
            ((ILocaleAware<?>) format).setLocale(this.locale);
        }
        return this;
    }

    /**
     * Removes the mapping for the given {@link TimeUnit} type. This effectively
     * de-registers the unit so it will not be used in formatting. Returns the
     * {@link TimeFormat} that was registered for the given {@link TimeUnit}
     * type, or null if no unit of the given type was registered.
     *
     * @param <U>
     * @param unitType
     * @return
     */
    public <U extends ITimeUnit> ITimeFormat removeUnit(final Class<? extends U> unitType) {
        if (Objects.isNull(unitType)) {
            throw new IllegalArgumentException("Unit type to remove must not be null.");
        }
        for (final ITimeUnit unit : this.units.keySet()) {
            if (unitType.isAssignableFrom(unit.getClass())) {
                this.cachedUnits.clear();
                return this.units.remove(unit);
            }
        }
        return null;
    }

    /**
     * Removes the mapping for the given {@link TimeUnit}. This effectively
     * de-registers the unit so it will not be used in formatting. Returns the
     * {@link TimeFormat} that was registered for the given {@link TimeUnit}, or
     * null if no such unit was registered.
     *
     * @param unit
     * @return
     */
    public ITimeFormat removeUnit(final ITimeUnit unit) {
        if (Objects.isNull(unit)) {
            throw new IllegalArgumentException("Unit to remove must not be null.");
        }
        this.cachedUnits.clear();
        return this.units.remove(unit);
    }

    /**
     * Get the currently configured {@link Locale} for this {@link PrettyTime}
     * object.
     *
     * @return
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Set the the {@link Locale} for this {@link PrettyTime} object. This may
     * be an expensive operation, since this operation calls
     * {@link TimeUnit#setLocale(Locale)} for each {@link TimeUnit} in
     * {@link #getUnits()}.
     *
     * @param locale
     * @return
     */
    public DateTimeFormatter setLocale(final Locale locale) {
        if (Objects.isNull(locale)) {
            this.locale = Locale.getDefault();
        }
        this.locale = locale;
        this.units.keySet().stream().filter((unit) -> (unit instanceof ILocaleAware)).forEach((unit) -> {
            ((ILocaleAware<?>) unit).setLocale(locale);
        });
        this.units.values().stream().filter((format) -> (format instanceof ILocaleAware)).forEach((format) -> {
            ((ILocaleAware<?>) format).setLocale(locale);
        });
        return this;
    }

    @Override
    public String toString() {
        return "DateTimeFormatter [reference=" + this.reference + ", locale=" + this.locale + "]";
    }

    /**
     * Remove all registered {@link ITimeUnit} instances.
     *
     * @return The removed {@link ITimeUnit} instances.
     */
    public List<ITimeUnit> clearUnits() {
        final List<ITimeUnit> result = getUnits();
        this.cachedUnits.clear();
        this.units.clear();
        return result;
    }

    private Date now() {
        return GregorianCalendar.getInstance().getTime();
    }
}
