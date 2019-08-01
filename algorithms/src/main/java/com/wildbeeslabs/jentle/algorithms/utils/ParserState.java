package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

// Taken from http://icecube.wisc.edu/~dglo/software/calparse/index.html
// Copyright Dave Glowacki. Released under the BSD license.

/**
 * Date parser state.
 */
public class ParserState {

    /**
     * bit indicating that the year comes before the month.
     */
    static final int YEAR_BEFORE_MONTH = 0x4;
    /**
     * bit indicating that the year comes before the day.
     */
    static final int YEAR_BEFORE_DAY = 0x2;
    /**
     * bit indicating that the month comes before the day.
     */
    static final int MONTH_BEFORE_DAY = 0x1;

    /**
     * bit indicating that the year comes after the month.
     */
    static final int YEAR_AFTER_MONTH = 0x0;
    /**
     * bit indicating that the year comes after the day.
     */
    static final int YEAR_AFTER_DAY = 0x0;
    /**
     * bit indicating that the month comes after the day.
     */
    static final int MONTH_AFTER_DAY = 0x0;

    /**
     * value indicating an unset variable.
     */
    static final int UNSET = Integer.MIN_VALUE;

    /**
     * <tt>true</tt> if year should appear before month.
     */
    private boolean yearBeforeMonth;
    /**
     * <tt>true</tt> if year should appear before day.
     */
    private boolean yearBeforeDay;
    /**
     * <tt>true</tt> if month should appear before day.
     */
    private boolean monthBeforeDay;

    /**
     * year.
     */
    private int year;
    /**
     * month (0-11).
     */
    private int month;
    /**
     * day of month.
     */
    private int day;
    /**
     * hour (0-23).
     */
    private int hour;
    /**
     * minute (0-59).
     */
    private int minute;
    /**
     * second (0-59).
     */
    private int second;
    /**
     * millisecond (0-999).
     */
    private int milli;

    /**
     * <tt>true</tt> if time is after noon.
     */
    private boolean timePostMeridian;

    /**
     * time zone (use default time zone if this is <tt>null</tt>).
     */
    private TimeZone timeZone;

    /**
     * Create parser state for the specified order.
     *
     * @param order <tt>YY_MM_DD</tt>, <tt>MM_DD_YY</tt>, etc.
     */
    ParserState(int order) {
        yearBeforeMonth = (order & YEAR_BEFORE_MONTH) == YEAR_BEFORE_MONTH;
        yearBeforeDay = (order & YEAR_BEFORE_DAY) == YEAR_BEFORE_DAY;
        monthBeforeDay = (order & MONTH_BEFORE_DAY) == MONTH_BEFORE_DAY;

        year = UNSET;
        month = UNSET;
        day = UNSET;
        hour = UNSET;
        minute = UNSET;
        second = UNSET;
        timePostMeridian = false;
    }

    /**
     * Get day of month.
     *
     * @return day of month
     */
    int getDate() {
        return day;
    }

    /**
     * Get hour.
     *
     * @return hour
     */
    int getHour() {
        return hour;
    }

    /**
     * Get millisecond.
     *
     * @return millisecond
     */
    int getMillisecond() {
        return milli;
    }

    /**
     * Get minute.
     *
     * @return minute
     */
    int getMinute() {
        return minute;
    }

    /**
     * Get month.
     *
     * @return month
     */
    int getMonth() {
        return month;
    }

    /**
     * Get second.
     *
     * @return second
     */
    int getSecond() {
        return second;
    }

    /**
     * Get time zone.
     *
     * @return time zone (<tt>null</tt> if none was specified)
     */
    TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Get year.
     *
     * @return year
     */
    int getYear() {
        return year;
    }

    /**
     * Is day of month value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isDateSet() {
        return (day != UNSET);
    }

    /**
     * Is hour value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isHourSet() {
        return (hour != UNSET);
    }

    /**
     * Is millisecond value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isMillisecondSet() {
        return (milli != UNSET);
    }

    /**
     * Is minute value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isMinuteSet() {
        return (minute != UNSET);
    }

    /**
     * Is a numeric month placed before a numeric day of month?
     *
     * @return <tt>true</tt> if month is before day of month
     */
    boolean isMonthBeforeDay() {
        return monthBeforeDay;
    }

    /**
     * Is month value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isMonthSet() {
        return (month != UNSET);
    }

    /**
     * Is second value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isSecondSet() {
        return (second != UNSET);
    }

    /**
     * Is the time post-meridian (i.e. afternoon)?
     *
     * @return <tt>true</tt> if time is P.M.
     */
    boolean isTimePostMeridian() {
        return (timePostMeridian || hour > 12);
    }

    /**
     * Is a numeric year placed before a numeric day of month?
     *
     * @return <tt>true</tt> if year is before day of month
     */
    boolean isYearBeforeDay() {
        return yearBeforeDay;
    }

    /**
     * Is a numeric year placed before a numeric month?
     *
     * @return <tt>true</tt> if year is before month
     */
    boolean isYearBeforeMonth() {
        return yearBeforeMonth;
    }

    /**
     * Is year value set?
     *
     * @return <tt>true</tt> if a value has been assigned
     */
    boolean isYearSet() {
        return (year != UNSET);
    }

    /**
     * Fill the calendar with the parsed date.
     *
     * @param cal           calendar to fill
     * @param ignoreChanges if <tt>true</tt>, throw an exception when a date like
     *                      <tt>Sept 31</tt> is changed to <tt>Oct 1</tt>
     * @throws IllegalArgumentException if the date cannot be set for some reason
     */
    void setCalendar(GregorianCalendar cal, boolean ignoreChanges)
        throws IllegalArgumentException {
        cal.clear();
        if (year != UNSET && month != UNSET && day != UNSET) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, day);

            if (!ignoreChanges) {
                final int calYear = cal.get(Calendar.YEAR);
                final int calMonth = cal.get(Calendar.MONTH);
                final int calDay = cal.get(Calendar.DATE);

                if (calYear != year || (calMonth + 1) != month || calDay != day) {
                    throw new IllegalArgumentException("Date was set to "
                        + calYear + "/" + (calMonth + 1) + "/" + calDay
                        + " not requested " + year + "/" + month + "/"
                        + day);
                }
            }
        }

        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        if (hour != UNSET && minute != UNSET) {
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, minute);
            if (second != UNSET) {
                cal.set(Calendar.SECOND, second);
                if (milli != UNSET) {
                    cal.set(Calendar.MILLISECOND, milli);
                }
            }

            if (timeZone != null) {
                cal.setTimeZone(timeZone);
            }
        }
    }

    /**
     * Set the day of month value.
     *
     * @param val day of month value
     * @throws IllegalArgumentException if the value is not a valid day of month
     */
    void setDate(int val) throws IllegalArgumentException {
        if (val < 1 || val > 31) {
            throw new IllegalArgumentException("Bad day " + val);
        }

        day = val;
    }

    /**
     * Set the hour value.
     *
     * @param val hour value
     * @throws IllegalArgumentException if the value is not a valid hour
     */
    void setHour(int val) throws IllegalArgumentException {
        final int tmpHour;
        if (timePostMeridian) {
            tmpHour = val + 12;
            timePostMeridian = false;
        } else {
            tmpHour = val;
        }

        if (tmpHour < 0 || tmpHour > 23) {
            throw new IllegalArgumentException("Bad hour " + val);
        }

        hour = tmpHour;
    }

    /**
     * Set the millisecond value.
     *
     * @param val millisecond value
     * @throws IllegalArgumentException if the value is not a valid millisecond
     */
    void setMillisecond(int val) throws IllegalArgumentException {
        if (val < 0 || val > 999) {
            throw new IllegalArgumentException("Bad millisecond " + val);
        }

        milli = val;
    }

    /**
     * Set the minute value.
     *
     * @param val minute value
     * @throws IllegalArgumentException if the value is not a valid minute
     */
    void setMinute(int val) throws IllegalArgumentException {
        if (val < 0 || val > 59) {
            throw new IllegalArgumentException("Bad minute " + val);
        }

        minute = val;
    }

    /**
     * Set the month value.
     *
     * @param val month value
     * @throws IllegalArgumentException if the value is not a valid month
     */
    void setMonth(int val) throws IllegalArgumentException {
        if (val < 1 || val > 12) {
            throw new IllegalArgumentException("Bad month " + val);
        }

        month = val;
    }

    /**
     * Set the second value.
     *
     * @param val second value
     * @throws IllegalArgumentException if the value is not a valid second
     */
    void setSecond(int val) throws IllegalArgumentException {
        if (val < 0 || val > 59) {
            throw new IllegalArgumentException("Bad second " + val);
        }

        second = val;
    }

    /**
     * Set the AM/PM indicator value.
     *
     * @param val <tt>true</tt> if time represented is after noon
     */
    void setTimePostMeridian(boolean val) {
        timePostMeridian = val;
    }

    /**
     * Set the time zone.
     *
     * @param tz time zone
     */
    void setTimeZone(TimeZone tz) {
        timeZone = tz;
    }

    /**
     * Set the year value.
     *
     * @param val year value
     * @throws IllegalArgumentException if the value is not a valid year
     */
    void setYear(int val) throws IllegalArgumentException {
        if (val < 0) {
            throw new IllegalArgumentException("Bad year " + val);
        }

        year = val;
    }
}
