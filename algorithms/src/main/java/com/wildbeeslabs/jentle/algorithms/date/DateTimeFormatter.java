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

import com.wildbeeslabs.jentle.collections.utils.CComparatorUtils;
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
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * Default date/time formatter implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class DateTimeFormatter {

    private volatile Date reference;
    private volatile Locale locale = Locale.getDefault();
    private volatile Map<ITimeUnit, ITimeFormat> units = new LinkedHashMap<>();

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
    public IDuration approximateDuration(@NonNull final Date date) {
        Date ref = this.reference;
        if (Objects.isNull(ref)) {
            ref = GregorianCalendar.getInstance().getTime();
        }
        long difference = date.getTime() - ref.getTime();
        return this.calculateDuration(difference);
    }

    private void initTimeUnits() {
        this.addUnit(TimeUnit.WORKING_DAY);
        this.addUnit(TimeUnit.MILLISECOND);
        this.addUnit(TimeUnit.SECOND);
        this.addUnit(TimeUnit.MINUTE);
        this.addUnit(TimeUnit.HOUR);
        this.addUnit(TimeUnit.DAY);
        this.addUnit(TimeUnit.WEEK);
        this.addUnit(TimeUnit.MONTH);
        this.addUnit(TimeUnit.YEAR);
        this.addUnit(TimeUnit.NONE);
    }

    private void addUnit(final ResourcesTimeUnit unit) {
        this.registerUnit(unit, new ResourcesTimeFormat(unit));
    }

    private IDuration calculateDuration(final long difference) {
        long absoluteDifference = Math.abs(difference);
        final List<ITimeUnit> timeUnits = new ArrayList<>(this.getUnits().size());
        timeUnits.addAll(this.getUnits());
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
                } else {
                    result.setQuantity(difference / millisPerUnit);
                }
                result.setDelta(difference - result.getQuantity() * millisPerUnit);
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
     * @param then The date to be compared against the reference timestamp, or
     * <i>now</i> if no reference timestamp was provided
     * @return A sorted {@link List} of {@link Duration} objects, from largest
     * to smallest. Each element in the list represents the approximate duration
     * (number of times) that {@link TimeUnit} to fit into the previous
     * element's delta. The first element is the largest {@link TimeUnit} to fit
     * within the total difference between compared dates.
     */
    public List<IDuration> calculatePreciseDuration(final Date then) {
        if (Objects.isNull(then)) {
            throw new IllegalArgumentException("Date to calculate must not be null.");
        }
        if (Objects.isNull(this.reference)) {
            this.reference = new Date();
        }
        final List<IDuration> result = new ArrayList<>();
        long difference = then.getTime() - this.reference.getTime();
        IDuration duration = calculateDuration(difference);
        result.add(duration);
        while (0 != duration.getDelta()) {
            duration = calculateDuration(duration.getDelta());
            result.add(duration);
        }
        return result;
    }

    /**
     * Format the given {@link Date} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense.
     *
     * @param then the {@link Date} to be formatted
     * @return A formatted string representing {@code then}
     */
    public String format(final Date then) {
        if (Objects.isNull(then)) {
            throw new IllegalArgumentException("Date to format must not be null.");
        }
        final IDuration duration = approximateDuration(then);
        return format(duration);
    }

    /**
     * Format the given {@link Calendar} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense.
     *
     * @param then the {@link Calendar} whose date is to be formatted
     * @return A formatted string representing {@code then}
     */
    public String format(final Calendar then) {
        if (Objects.isNull(then)) {
            throw new IllegalArgumentException("Provided Calendar must not be null.");
        }
        return format(then.getTime());
    }

    /**
     * Format the given {@link Date} object. This method applies the
     * {@code PrettyTime.approximateDuration(date)} method to perform its
     * calculation. If {@code then} is null, it will default to
     * {@code new Date()}; also decorate for past/future tense. Rounding rules
     * are ignored.
     *
     * @param then the {@link Date} to be formatted
     * @return A formatted string representing {@code then}
     */
    public String formatUnrounded(final Date then) {
        if (Objects.isNull(then)) {
            throw new IllegalArgumentException("Date to format must not be null.");
        }
        final IDuration d = approximateDuration(then);
        return formatUnrounded(d);
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
            throw new IllegalArgumentException("Duration to format must not be null.");
        }
        ITimeFormat format = getFormat(duration.getUnit());
        String time = format.format(duration);
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
            throw new IllegalArgumentException("Duration to format must not be null.");
        }
        ITimeFormat format = getFormat(duration.getUnit());
        String time = format.formatUnrounded(duration);
        return format.decorateUnrounded(duration, time);
    }

    /**
     * Format the given {@link IDuration} objects, using the {@link ITimeFormat}
     * specified by the {@link ITimeFormat} contained within. Rounds only the last
     * {@link Duration} object.
     *
     * @param durations the {@link IDuration}s to be formatted
     * @return A list of formatted strings representing {@code durations}
     */
    public String format(final List<IDuration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException("Duration list must not be null.");
        }
        StringBuilder builder = new StringBuilder();
        IDuration duration = null;
        ITimeFormat format = null;
        for (int i = 0; i < durations.size(); i++) {
            duration = durations.get(i);
            format = getFormat(duration.getUnit());
            boolean isLast = (i == durations.size() - 1);
            if (!isLast) {
                builder.append(format.formatUnrounded(duration));
                builder.append(" ");
            } else {
                builder.append(format.format(duration));
            }
        }
        return format.decorateUnrounded(duration, builder.toString());
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
            throw new IllegalArgumentException("TimeUnit must not be null.");
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
        final List<ITimeUnit> result = new ArrayList<>(this.units.keySet());
        Collections.sort(result, new TimeUnitComparator());
        return Collections.unmodifiableList(result);
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
            throw new IllegalArgumentException("Unit to register must not be null.");
        }
        if (Objects.isNull(format)) {
            throw new IllegalArgumentException("Format to register must not be null.");
        }
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
    public <U extends TimeUnit> ITimeFormat removeUnit(final Class<U> unitType) {
        if (Objects.isNull(unitType)) {
            throw new IllegalArgumentException("Unit type to remove must not be null.");
        }
        for (final ITimeUnit unit : this.units.keySet()) {
            if (unitType.isAssignableFrom(unit.getClass())) {
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
        this.units.clear();
        return result;
    }

    /**
     * Default TimeUnit comparator.
     */
    public static class TimeUnitComparator extends CUtils.CSortComparator<TimeUnit> {

        @Override
        public int compare(final TimeUnit first, final TimeUnit last) {
            return CComparatorUtils.compareTo(first, last);
        }
    }
}
