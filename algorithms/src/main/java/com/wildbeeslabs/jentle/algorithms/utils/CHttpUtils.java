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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Objects;

import static com.wildbeeslabs.jentle.algorithms.utils.CValidationUtils.fail;
import static org.apache.http.impl.client.HttpClients.createDefault;

/**
 * Custom http utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CHttpUtils {

    private void checkImageDisplayedHttpClient(final String url) throws IOException {
        final CloseableHttpClient httpclient = createDefault();
        final HttpGet httpGet = new HttpGet(url);
        final CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.println("Actual status code is: " + response.getStatusLine().getStatusCode());
                fail("The image is broken. The url for it is: " + url);
            }
        } finally {
            response.close();
        }
    }

    public static String readUrl(final String url) {
        Objects.requireNonNull(url);
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on url=%s, message=%s", url, ex.getMessage()));
        }
        return null;
    }

    @SuppressWarnings("null")
    public static String getRequest(final String url) {
        Objects.requireNonNull(url);
        HttpGet request = null;
        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            request = new HttpGet(url);
            request.addHeader("User-Agent", "Apache HTTPClient");

            final HttpResponse response = client.execute(request);
            final HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on url=%s, message=%s", url, ex.getMessage()));
        } finally {
            if (Objects.nonNull(request)) {
                request.releaseConnection();
            }
        }
        return null;
    }

    @SuppressWarnings("null")
    public static String postRequest(final String url, final HttpEntity entity) {
        Objects.requireNonNull(url);
        HttpPost request = null;
        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            request = new HttpPost(url);
            request.addHeader("User-Agent", "Apache HTTPClient");
            request.setEntity(entity);

            final HttpResponse response = client.execute(request);
            final HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read operations on url=%s, message=%s", url, ex.getMessage()));
        } finally {
            if (Objects.nonNull(request)) {
                request.releaseConnection();
            }
        }
        return null;
    }
}
