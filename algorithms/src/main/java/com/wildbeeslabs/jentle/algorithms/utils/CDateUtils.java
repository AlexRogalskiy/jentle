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
package com.wildbeeslabs.jentle.algorithms.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom date utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CDateUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CDateUtils.class);
    
    /**
     * Default date format locale
     */
    public static final Locale DEFAULT_DATE_FORMAT_LOCALE = Locale.ENGLISH;

    /**
     * Default date format pattern
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN_EXT = "yyyy-MM-dd HH:mm:ss.SSSZ";

    /**
     * Default date format pattern
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Default time zone pattern
     */
    public static final String DEFAULT_TIMEZONE_PATTERN = Calendar.getInstance().getTimeZone().getID();

    /**
     * Custom date time formatter
     */
    public final static DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendOptional(new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendLiteral('T')
                    .append(DateTimeFormatter.ISO_TIME)
                    .toFormatter()).toFormatter();

    /**
     * Custom time formatter
     */
    public static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    private CDateUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static enum TimeZoneOffsetBase {
        GMT, UTC
    }

    public static String getDefaultTimeZone() {
        return System.getProperty("app.time.zone");
    }

    public static Date toDate(final String value) {
        return toDate(value, CDateUtils.DEFAULT_TIMEZONE_PATTERN);
    }

    public static Date toDate(final String value, final String timezone) {
        return toDate(value, timezone, CDateUtils.DEFAULT_DATE_FORMAT_PATTERN, CDateUtils.DEFAULT_DATE_FORMAT_LOCALE);
    }

    public static Date toDate(final String value, final String timezone, final String format, final Locale locale) {
        try {
            final DateFormat df = new SimpleDateFormat(format, locale);
            df.setTimeZone(TimeZone.getTimeZone(timezone));
            df.setLenient(false);
            return df.parse(value);
        } catch (ParseException ex) {
            LOGGER.error(String.format("ERROR: cannot parse input string=%s, timezone=%s, format=%s, locale=%s, message=%s", value, timezone, format, locale, ex.getMessage()));
        }
        return null;
    }

    public static String toString(final Date date) {
        return toString(date, CDateUtils.DEFAULT_TIMEZONE_PATTERN);
    }

    public static String toString(final Date date, final String timezone) {
        return toString(date, timezone, CDateUtils.DEFAULT_DATE_FORMAT_PATTERN, CDateUtils.DEFAULT_DATE_FORMAT_LOCALE);
    }

    public static String toString(final Date date, final String timezone, final Locale locale) {
        return toString(date, timezone, CDateUtils.DEFAULT_DATE_FORMAT_PATTERN, locale);
    }

    public static String toString(final Date date, final String timezone, final String format, final Locale locale) {
        final DateFormat df = new SimpleDateFormat(format, locale);
        df.setTimeZone(TimeZone.getTimeZone(timezone));
        return df.format(date);
    }

    public static List<String> getTimeZoneGMTList() {
        return getTimeZoneList(CDateUtils.TimeZoneOffsetBase.GMT);
    }

    public static List<String> getTimeZoneUTCList() {
        return getTimeZoneList(CDateUtils.TimeZoneOffsetBase.UTC);
    }

    private static List<String> getTimeZoneList(final TimeZoneOffsetBase base) {
        final LocalDateTime now = LocalDateTime.now();
        return ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .sorted(new ZoneComparator())
                .map(id -> String.format("(%s%s) %s", base, getOffset(now, id), id.getId()))
                .collect(Collectors.toList());
    }

    private static String getOffset(@NonNull final LocalDateTime dateTime, final ZoneId id) {
        return dateTime
                .atZone(id)
                .getOffset()
                .getId()
                .replace("Z", "+00:00");
    }

    private static class ZoneComparator implements Comparator<ZoneId> {

        @Override
        public int compare(final ZoneId zoneId1, final ZoneId zoneId2) {
            final LocalDateTime now = LocalDateTime.now();
            final ZoneOffset offset1 = now.atZone(zoneId1).getOffset();
            final ZoneOffset offset2 = now.atZone(zoneId2).getOffset();
            return offset1.compareTo(offset2);
        }
    }

    public static List<String> getTimeZoneList2(final TimeZoneOffsetBase base) {
        String[] availableZoneIds = TimeZone.getAvailableIDs();
        final List<String> result = new ArrayList<>(availableZoneIds.length);
        for (final String zoneId : availableZoneIds) {
            TimeZone curTimeZone = TimeZone.getTimeZone(zoneId);
            String offset = calculateOffset(curTimeZone.getRawOffset());
            result.add(String.format("(%s%s) %s", base, offset, zoneId));
        }
        Collections.sort(result);
        return result;
    }

    private static String calculateOffset(int rawOffset) {
        if (rawOffset == 0) {
            return "+00:00";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(rawOffset);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(rawOffset);
        minutes = Math.abs(minutes - TimeUnit.HOURS.toMinutes(hours));
        return String.format("%+03d:%02d", hours, Math.abs(minutes));
    }

    public static List<Date> getDatesBetween(@NonNull final Date startDate, @NonNull final Date endDate) {
        final List<Date> datesInRange = new ArrayList<>();
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        final Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static List<LocalDate> getDatesBetween2(@NonNull final LocalDate startDate, @NonNull final LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

//    public static List<LocalDate> getDatesBetween(final LocalDate startDate, final LocalDate endDate) {
//        return startDate.datesUntil(endDate).collect(Collectors.toList());
//    }
//    public LocalDate convertToLocalDate(final Date dateToConvert) {
//        return LocalDate.ofInstant(
//                dateToConvert.toInstant(), ZoneId.systemDefault());
//    }
//
//    public LocalDateTime convertToLocalDateTime(final Date dateToConvert) {
//        return LocalDateTime.ofInstant(
//                dateToConvert.toInstant(), ZoneId.systemDefault());
//    }
    public static LocalDate convertToLocalDateViaInstant(@NonNull final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(@NonNull final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertToDateViaInstant(@NonNull final LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date convertToDateViaInstant(@NonNull final LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static boolean isValidDate(final String date, final String format) {
        return Objects.nonNull(toDate(date, format));
    }

    public static long getDifference(@NonNull final Calendar first, @NonNull final Calendar last, @NonNull final TimeUnit units) {
        return units.convert(last.getTimeInMillis() - first.getTimeInMillis(), TimeUnit.MILLISECONDS);
    }

    public static LocalDateTime endOfDay(@NonNull final LocalDate date) {
        return date.atStartOfDay().plusHours(24).minus(1, ChronoUnit.MICROS);
    }

    public static LocalDateTime withoutSecond(@NonNull final LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.MINUTES);
    }

    public static String format(@NonNull final LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parseDate(final String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String format(@NonNull final LocalDateTime localTimeDate) {
        return localTimeDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parseDateTime(final String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String print(final LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.of(getDefaultTimeZone())).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static LocalDateTime parse(final String dateTime) {
        final TemporalAccessor accessor = DATE_TIME_FORMAT.parse(dateTime);
        final LocalDate date = LocalDate.from(accessor);
        final LocalTime time = accessor.query(TemporalQueries.localTime());
        if (Objects.isNull(time)) {
            return LocalDateTime.of(date, LocalTime.MIN);
        }
        return LocalDateTime.of(date, time);
    }
}
