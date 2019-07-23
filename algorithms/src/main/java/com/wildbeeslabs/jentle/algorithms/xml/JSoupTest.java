package com.wildbeeslabs.jentle.algorithms.xml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSoupTest {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://stackoverflow.com/questions/15893655/").userAgent("Mozilla").get();
        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(doc.text());
        Set<String> emails = new HashSet<String>();
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        Set<String> links = new HashSet<String>();
        Elements elements = doc.select("a[href]");
        for (Element e : elements) {
            links.add(e.attr("href"));
        }
        System.out.println(emails);
        System.out.println(links);
    }
}
