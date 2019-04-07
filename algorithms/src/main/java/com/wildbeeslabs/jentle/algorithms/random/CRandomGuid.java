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
package com.wildbeeslabs.jentle.algorithms.random;

import com.wildbeeslabs.jentle.algorithms.utils.CDigestUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Custom random GUID utility implementation.
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public class CRandomGuid {

    @Setter(AccessLevel.NONE)
    public String valueBeforeMD5 = StringUtils.EMPTY;
    @Setter(AccessLevel.NONE)
    public String valueAfterMD5 = StringUtils.EMPTY;

    /**
     * Default constructor.
     */
    public CRandomGuid() {
        getRandomGUID(Boolean.FALSE);
    }

    /**
     * Constructor with security option of instantiating random generator.
     *
     * @param secure - seed flag (true - secure random generator, false -
     *               standard random generator)
     */
    public CRandomGuid(boolean secure) {
        getRandomGUID(secure);
    }

    /**
     * Generates the random GUID.
     */
    private void getRandomGUID(boolean secure) {
        try {
            long rand = (secure) ? new SecureRandom().nextLong() : new Random().nextLong();

            final StringBuffer sbBeforeMD5 = new StringBuffer();
            sbBeforeMD5.append(System.currentTimeMillis());
            sbBeforeMD5.append(":");
            sbBeforeMD5.append(Long.toString(System.currentTimeMillis()));
            sbBeforeMD5.append(":");
            sbBeforeMD5.append(Long.toString(rand));
            this.valueBeforeMD5 = sbBeforeMD5.toString();

            final byte[] array = CDigestUtils.md5(this.valueBeforeMD5);
            final StringBuffer sbAfterMD5 = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) {
                    sbAfterMD5.append('0');
                }
                sbAfterMD5.append(Integer.toHexString(b));
            }
            this.valueAfterMD5 = sbAfterMD5.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.error(String.format("ERROR: cannot create random GUID, message=%s", ex.getMessage()));
        }
    }

    /**
     * Converts to the standard string format of GUID.
     *
     * @return standard string format of GUID
     */
    public String toFormatString() {
        final String raw = this.valueAfterMD5.toUpperCase();
        final StringBuffer sb = new StringBuffer();
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));
        return sb.toString();
    }
}
