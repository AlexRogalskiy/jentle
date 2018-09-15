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

import com.wildbeeslabs.jentle.algorithms.date.ITimeFormat;
import com.wildbeeslabs.jentle.algorithms.date.ITimeFormatProvider;
import com.wildbeeslabs.jentle.algorithms.date.ITimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.DayTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.HourTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MillenniumTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MinuteTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.MonthTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.WeekTimeUnit;
import com.wildbeeslabs.jentle.algorithms.date.units.YearTimeUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Default resources bundle [HR]
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Resources_HR extends Resources implements ITimeFormatProvider {

    private static final Object[][] OBJECTS = new Object[][]{
        {"CenturyPattern", "%n %u"},
        {"CenturyFuturePrefix", "za "},
        {"CenturyFutureSuffix", ""},
        {"CenturyPastPrefix", ""},
        {"CenturyPastSuffix", " unatrag"},
        {"CenturySingularName", "stoljeće"},
        {"CenturyPluralName", "stoljeća"},
        {"DayPattern", "%n %u"},
        {"DayFuturePrefix", "za "},
        {"DayFutureSuffix", ""},
        {"DayPastPrefix", "prije "},
        {"DayPastSuffix", ""},
        {"DaySingularName", "dan"},
        {"DayPluralName", "dana"},
        {"DecadePattern", "%n %u"},
        {"DecadeFuturePrefix", "za "},
        {"DecadeFutureSuffix", ""},
        {"DecadePastPrefix", "prije "},
        {"DecadePastSuffix", ""},
        {"DecadeSingularName", "desetljeće"},
        {"DecadePluralName", "desetljeća"},
        {"HourPattern", "%n %u"},
        {"HourFuturePrefix", "za "},
        {"HourFutureSuffix", ""},
        {"HourPastPrefix", "prije "},
        {"HourPastSuffix", ""},
        {"HourSingularName", "sat"},
        {"HourPluralName", "sati"},
        {"JustNowPattern", "%u"},
        {"JustNowFuturePrefix", "za nekoliko trenutaka"},
        {"JustNowFutureSuffix", ""},
        {"JustNowPastPrefix", "prije nekoliko trenutaka"},
        {"JustNowPastSuffix", ""},
        {"JustNowSingularName", ""},
        {"JustNowPluralName", ""},
        {"MillenniumPattern", "%n %u"},
        {"MillenniumFuturePrefix", "za "},
        {"MillenniumFutureSuffix", ""},
        {"MillenniumPastPrefix", "prije "},
        {"MillenniumPastSuffix", ""},
        {"MillenniumSingularName", "tisućljeće"},
        {"MillenniumPluralName", "tisućljeća"},
        {"MillisecondPattern", "%n %u"},
        {"MillisecondFuturePrefix", "za "},
        {"MillisecondFutureSuffix", ""},
        {"MillisecondPastPrefix", "prije "},
        {"MillisecondPastSuffix", ""},
        {"MillisecondSingularName", "milisekunda"},
        {"MillisecondPluralName", "milisekunda"},
        {"MinutePattern", "%n %u"},
        {"MinuteFuturePrefix", "za "},
        {"MinuteFutureSuffix", ""},
        {"MinutePastPrefix", "prije "},
        {"MinutePastSuffix", ""},
        {"MinuteSingularName", "minuta"},
        {"MinutePluralName", "minuta"},
        {"MonthPattern", "%n %u"},
        {"MonthFuturePrefix", "za "},
        {"MonthFutureSuffix", ""},
        {"MonthPastPrefix", "prije "},
        {"MonthPastSuffix", ""},
        {"MonthSingularName", "mjesec"},
        {"MonthPluralName", "mjeseca"},
        {"SecondPattern", "%n %u"},
        {"SecondFuturePrefix", "za "},
        {"SecondFutureSuffix", ""},
        {"SecondPastPrefix", "prije "},
        {"SecondPastSuffix", ""},
        {"SecondSingularName", "sekunda"},
        {"SecondPluralName", "sekundi"},
        {"WeekPattern", "%n %u"},
        {"WeekFuturePrefix", "za "},
        {"WeekFutureSuffix", ""},
        {"WeekPastPrefix", "prije "},
        {"WeekPastSuffix", ""},
        {"WeekSingularName", "tjedan"},
        {"WeekPluralName", "tjedna"},
        {"YearPattern", "%n %u"},
        {"YearFuturePrefix", "za "},
        {"YearFutureSuffix", ""},
        {"YearPastPrefix", "prije "},
        {"YearPastSuffix", ""},
        {"YearSingularName", "godina"},
        {"YearPluralName", "godina"},
        {"AbstractTimeUnitPattern", ""},
        {"AbstractTimeUnitFuturePrefix", ""},
        {"AbstractTimeUnitFutureSuffix", ""},
        {"AbstractTimeUnitPastPrefix", ""},
        {"AbstractTimeUnitPastSuffix", ""},
        {"AbstractTimeUnitSingularName", ""},
        {"AbstractTimeUnitPluralName", ""}};

    @Override
    public Object[][] getResources() {
        return Resources_HR.OBJECTS;
    }

    @Override
    public ITimeFormat getFormat(final ITimeUnit timeUnit) {
        if (timeUnit instanceof MinuteTimeUnit) {
            return new CsTimeFormatBuilder<>("Minute").addNames("minutu", 1)
                    .addNames("minute", 4).addNames("minuta", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof HourTimeUnit) {
            return new CsTimeFormatBuilder<>("Hour").addNames("sat", 1)
                    .addNames("sata", 4).addNames("sati", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof DayTimeUnit) {
            return new CsTimeFormatBuilder<>("Day").addNames("dan", 1)
                    .addNames("dana", 4).addNames("dana", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof WeekTimeUnit) {
            return new CsTimeFormatBuilder<>("Week").addNames("tjedan", 1)
                    .addNames("tjedna", 4).addNames("tjedana", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof MonthTimeUnit) {
            return new CsTimeFormatBuilder<>("Month").addNames("mjesec", 1)
                    .addNames("mjeseca", 4).addNames("mjeseci", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof YearTimeUnit) {
            return new CsTimeFormatBuilder<>("Year").addNames("godinu", 1)
                    .addNames("godine", 4).addNames("godina", Long.MAX_VALUE)
                    .build(this);
        } else if (timeUnit instanceof MillenniumTimeUnit) {
            return new CsTimeFormatBuilder<>("Millennium")
                    .addNames("tisućljeće", 1).addNames("tisućljeća", Long.MAX_VALUE)
                    .build(this);
        }
        return null;
    }
}
