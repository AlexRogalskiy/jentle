package com.wildbeeslabs.jentle.algorithms.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A simple example, used on the jsoup website.
 */
public class Wikipedia {

    public static void main(final String[] args) throws IOException {
        final Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
        log(doc.title());
        final Elements newsHeadlines = doc.select("#mp-itn b a");
        for (final Element headline : newsHeadlines) {
            log("%s\n\t%s", headline.attr("title"), headline.absUrl("href"));
        }
    }

    @SuppressWarnings("non-vargs")
    private static void log(final String msg, final String... vals) {
        System.out.println(String.format(msg, vals));
    }
}
