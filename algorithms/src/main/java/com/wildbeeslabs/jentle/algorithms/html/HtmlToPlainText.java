package com.wildbeeslabs.jentle.algorithms.html;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;
import java.util.Objects;

/**
 * HTML to plain-text. This example program demonstrates the use of jsoup to convert HTML input to lightly-formatted
 * <p>
 * plain-text. That is divergent from the general goal of jsoup's .text() methods, which is to get clean data from a
 * <p>
 * scrape.
 *
 * <p>
 * <p>
 * Note that this is a fairly simplistic formatter -- for real world use you'll want to embrace and extend.
 *
 * </p>
 *
 * <p>
 * <p>
 * To invoke from the command line, assuming you've downloaded the jsoup jar to your current directory:</p>
 *
 * <p><code>java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]</code></p>
 * <p>
 * where <i>url</i> is the URL to fetch, and <i>selector</i> is an optional CSS selector.
 *
 * @author Jonathan Hedley, jonathan@hedley.net
 */
public class HtmlToPlainText {
    private static final String userAgent = "Mozilla/5.0 (jsoup)";
    private static final int timeout = 5 * 1000;

    public static void main(final String... args) throws IOException {
        Validate.isTrue(args.length == 1 || args.length == 2, "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
        final String url = args[0];
        final String selector = args.length == 2 ? args[1] : null;
        final Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
        final HtmlToPlainText formatter = new HtmlToPlainText();

        if (Objects.nonNull(selector)) {
            final Elements elements = doc.select(selector);
            for (final Element element : elements) {
                final String plainText = formatter.getPlainText(element); // format that element to plain text
                System.out.println(plainText);
            }
        } else {
            System.out.println(formatter.getPlainText(doc));
        }
    }

    /**
     * Format an Element to plain-text
     *
     * @param element the root element to format
     * @return formatted text
     */
    public String getPlainText(final Element element) {
        final FormattingVisitor formatter = new FormattingVisitor();
        NodeTraversor.traverse(formatter, element);
        return formatter.toString();
    }

    /**
     * Formatting {@link NodeVisitor}
     */
    private static class FormattingVisitor implements NodeVisitor {

        private static final int maxWidth = 80;
        private int width = 0;
        private final StringBuilder accum = new StringBuilder();

        public void head(final Node node, int depth) {
            final String name = node.nodeName();
            if (node instanceof TextNode) {
                append(((TextNode) node).text());
            } else if (name.equals("li")) {
                append("\n * ");
            } else if (name.equals("dt")) {
                append(" ");
            } else if (StringUtils.containsAny(name, "p", "h1", "h2", "h3", "h4", "h5", "tr")) {
                append("\n");
            }
        }

        public void tail(final Node node, int depth) {
            final String name = node.nodeName();
            if (StringUtils.containsAny(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
                append("\n");
            } else if (name.equals("a")) {
                append(String.format(" <%s>", node.absUrl("href")));
            }
        }

        private void append(final String text) {
            if (text.startsWith("\n")) {
                this.width = 0;
            }
            if (text.equals(" ") && (this.accum.length() == 0 || StringUtils.containsAny(this.accum.substring(this.accum.length() - 1), " ", "\n"))) {
                return;
            }
            if (text.length() + this.width > this.maxWidth) {
                final String[] words = text.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    boolean last = i == words.length - 1;
                    if (!last)
                        word = word + " ";
                    if (word.length() + this.width > this.maxWidth) { // wrap and reset counter
                        this.accum.append("\n").append(word);
                        this.width = word.length();
                    } else {
                        this.accum.append(word);
                        this.width += word.length();
                    }
                }
            } else { // fits as is, without need to wrap text
                this.accum.append(text);
                this.width += text.length();
            }
        }

        @Override
        public String toString() {
            return this.accum.toString();
        }
    }
}
