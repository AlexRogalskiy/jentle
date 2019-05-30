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
package com.wildbeeslabs.jentle.algorithms.url;

import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Ping implementation if you want to do a "health check" kind of Ping. This
 * will be a "real" ping. As in a real http/s call is made to this url e.g.
 * http://ec2-75-101-231-85.compute-1.amazonaws.com:7101/cs/hostRunning
 * <p>
 * Some services/clients choose PingDiscovery - which is quick but is not a real
 * ping. i.e It just asks discovery (eureka) in-memory cache if the server is present
 * in its Roster PingUrl on the other hand, makes an actual call. This is more
 * expensive - but its the "standard" way most VIPs and other services perform
 * HealthChecks.
 * <p>
 * Choose your Ping based on your needs.
 *
 * @author stonse
 */
@NoArgsConstructor
public class PingUrl implements IPing {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingUrl.class);

    String pingAppendString = "";
    boolean isSecure = false;

    String expectedContent = null;

    public PingUrl(boolean isSecure, String pingAppendString) {
        this.isSecure = isSecure;
        this.pingAppendString = (pingAppendString != null) ? pingAppendString : "";
    }

    public void setPingAppendString(String pingAppendString) {
        this.pingAppendString = (pingAppendString != null) ? pingAppendString : "";
    }

    public String getPingAppendString() {
        return pingAppendString;
    }

    public boolean isSecure() {
        return isSecure;
    }

    /**
     * Should the Secure protocol be used to Ping
     *
     * @param isSecure
     */
    public void setSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }


    public String getExpectedContent() {
        return expectedContent;
    }

    /**
     * Is there a particular content you are hoping to see?
     * If so -set this here.
     * for e.g. the WCS server sets the content body to be 'true'
     * Please be advised that this content should match the actual
     * content exactly for this to work. Else yo may get false status.
     *
     * @param expectedContent
     */
    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }

    public boolean isAlive(final Server server) {
        String urlStr = "";
        if (isSecure) {
            urlStr = "https://";
        } else {
            urlStr = "http://";
        }
        urlStr += server.getId();
        urlStr += getPingAppendString();

        boolean isAlive = false;

        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest getRequest = new HttpGet(urlStr);
        String content = null;
        try {
            HttpResponse response = httpClient.execute(getRequest);
            content = EntityUtils.toString(response.getEntity());
            isAlive = (response.getStatusLine().getStatusCode() == 200);
            if (getExpectedContent() != null) {
                LOGGER.debug("content:" + content);
                if (content == null) {
                    isAlive = false;
                } else {
                    if (content.equals(getExpectedContent())) {
                        isAlive = true;
                    } else {
                        isAlive = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Release the connection.
            getRequest.abort();
        }

        return isAlive;
    }

    public static void main(String[] args) {
        PingUrl p = new PingUrl(false, "/cs/hostRunning");
        p.setExpectedContent("true");
        Server s = new Server("ec2-75-101-231-85.compute-1.amazonaws.com", 7101);

        boolean isAlive = p.isAlive(s);
        System.out.println("isAlive:" + isAlive);
    }
}