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
package com.wildbeeslabs.jentle.algorithms.nlp;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Custom text summarizer algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public class CTextSummarizer {

    private Map<String, Integer> getWordCounts(final String text) {
        if (Objects.isNull(text)) {
            return Collections.EMPTY_MAP;
        }
        final Map<String, Integer> allWords = new HashMap<>();
        int count;
        int singleIncrement = 0;
        /* start with raw frequencies
         * scan entire text and record all words and word counts
         * so if a word appears multiple times, increment the word count for that particular word
         * if a word appears only once, add the new word to the Map
         */
        text.trim();
        final String[] words = text.split("\\s+");//split with white space delimiters
        for (final String word : words) {
            count = 0;
            if (allWords.containsKey(word)) {
                allWords.put(word, singleIncrement += 1);
            } else {
                allWords.put(word, count++);
            }
        }
        return allWords;
    }

    private Map<String, Integer> filterStopWords(final Map<String, Integer> d) {
        //filter any stop words here, so remove from the dictionary collection
        //return a dictionary, use the dictionary to store the frequency of a word and the word itself
        String[] stop_words = {"a", "able", "about", "after", "all", "also", "am",
            "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "did",
            "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "I",
            "if", "in", "into", "is", "it", "its", "just", "let", "like", "likely", "may", "me",
            "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off",
            "often", "on", "only", "or", "other", "our", "own", "said", "say", "says", "she",
            "should", "so", "some", "than", "that", "the", "their", "them", "then", "there",
            "these", "they", "this", "they're", "to", "too", "that's", "us", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with",
            "would", "yet", "you", "your", "you're"};
        for (final String stop_word : stop_words) {
            if (d.containsKey(stop_word)) {
                d.remove(stop_word);
            }
        }
        return d;
    }

    private List<String> sortByFreqThenDropFreq(final Map<String, Integer> wordFrequencies) {
        //sort the dictionary, sort by frequency and drop counts ['code', language']
        //return a List<string>
        final List<String> sortedCollection = new ArrayList<>(wordFrequencies.keySet());
        Collections.sort(sortedCollection);
        Collections.reverse(sortedCollection);    //largest to smallest
        return sortedCollection;
    }

    private String[] getSentences(String text) {
        text = text.replace("Mr.", "Mr").replace("Ms.", "Ms").replace("Dr.", "Dr").replace("Jan.", "Jan").replace("Feb.", "Feb")
            .replace("Mar.", "Mar").replace("Apr.", "Apr").replace("Jun.", "Jun").replace("Jul.", "Jul").replace("Aug.", "Aug")
            .replace("Sep.", "Sep").replace("Spet.", "Sept").replace("Oct.", "Oct").replace("Nov.", "Nov").replace("Dec.", "Dec")
            .replace("St.", "St").replace("Prof.", "Prof").replace("Mrs.", "Mrs").replace("Gen.", "Gen")
            .replace("Corp.", "Corp").replace("Mrs.", "Mrs").replace("Sr.", "Sr").replace("Jr.", "Jr").replace("cm.", "cm")
            .replace("Ltd.", "Ltd").replace("Col.", "Col").replace("vs.", "vs").replace("Capt.", "Capt")
            .replace("Univ.", "University").replace("Sgt.", "Sgt").replace("ft.", "ft").replace("in.", "in")
            .replace("Ave.", "Ave").replace("Univ.", "University").replace("Lt.", "Lt").replace("etc.", "etc").replace("mm.", "mm")
            .replace("\n\n", "").replace("\n", "").replace("\r", "");
        //solved! now fix alphabet letters like A. B. etc...use a regex
        text = text.replaceAll("([A-Z])\\.", "$1");

        //split using ., !, ?, and omit decimal numbers
        String pattern = "(?<!\\d)\\.(?!\\d)|(?<=\\d)\\.(?!\\d)|(?<!\\d)\\.(?=\\d)";
        Pattern pt = Pattern.compile(pattern);

        String[] sentences = pt.split(text);
        return sentences;
    }

    private String search(final String[] sentences, final String word) {
        //search for a particular sentence containing a particular word
        //this function will return the first matching sentence that has a value word
        String first_matching_sentence = null;
        for (final String sentence : sentences) {
            if (sentence.contains(word)) {
                first_matching_sentence = sentence;
            }
        }
        return first_matching_sentence;
    }

    public String Summarize(final String text, int maxSummarySize) {
        if (text.equals("") || text.equals(" ") || text.equals("\n")) {
            String msg = "Nothing to summarize...";
            return msg;
        }
        //start with raw freqs
        final Map<String, Integer> wordFrequencies = getWordCounts(text);

        //filter
        final Map<String, Integer> filtered = filterStopWords(wordFrequencies);

        //sort
        final List<String> sorted = sortByFreqThenDropFreq(filtered);

        //split the sentences
        final String[] sentences = getSentences(text);

        //we should have the first sentence be part of the summary
        String firstSentence = sentences[0];
        String datePatternString = "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)\\s\\d{1,2}\\s(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{4}\\s\\d{1,2}\\.\\d{2}(\\sEST|\\sPST)";

        firstSentence = firstSentence.replace("Last modified on", "");
        firstSentence = firstSentence.replaceAll(datePatternString, "");

        //select up to maxSummarySize sentences, so create a List<String>
        final List<String> setSummarySentences = new ArrayList<>();
        for (final String word : sorted)//foreach string in the sorted list
        {
            String first_matching_sentence = search(sentences, word);
            setSummarySentences.add(first_matching_sentence);//add to summary list
            if (setSummarySentences.size() == maxSummarySize) {
                break;
            }
        }
        //construct the summary size out of select sentences
        String summary = "";
        summary = summary + "• " + firstSentence + System.getProperty("line.separator") + System.getProperty("line.separator");

        for (final String sentence : sentences)//foreach string sentence in sentences list
        {
            if (setSummarySentences.contains(sentence)) {
                //produce each sentence with a bullet point and good amounts of spacing
                summary = summary + "• " + sentence + System.getProperty("line.separator") + System.getProperty("line.separator");
            }
        }
        return summary;
    }
}
