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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom text summary tool implementation
 *
 * @see http://opennlp.sourceforge.net/models-1.5/
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CTextSummaryTool {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CTextSummaryTool.class);

//Text into sentences
    public static String[] splitToSentences(final String content) {
        String[] sent = sentenceDetect.sentDetect(content);
        return sent;
    }

//Text into paragraphs
    public static String[] splitToParagraphs(final String content) {
        String[] mystring = content.split("\n\r\n");
        return mystring;
    }

    public static <T> Collection<T> intersect(final Collection<? extends T> a, final Collection<? extends T> b) {
        final Collection<T> result = new ArrayList<>();
        a.stream().filter((t) -> (b.remove(t))).forEach((t) -> {
            result.add(t);
        });
        return result;
    }

//Computing the intersection(common words) between two sentences
    public static float sentenceIntersection(final String sentence1, final String sentence2) {
        final String[] sent1 = tokenizer.tokenize(sentence1);
        final String[] sent2 = tokenizer.tokenize(sentence2);

        if (sent1.length + sent2.length == 0) {
            return 0;
        }

        final List<String> intersectArray = (List<String>) intersect(new ArrayList<>(Arrays.asList(sent1)), new ArrayList<>(Arrays.asList(sent2)));
        float result = ((float) (float) intersectArray.size() / ((float) sent1.length + ((float) sent2.length) / 2));
        return result;
    }

    public static String[] intersection(final String[] sent1, final String[] sent2) {
        if (sent1 == null || sent1.length == 0 || sent2 == null || sent2.length == 0) {
            return new String[0];
        }
        final List<String> sent1List = new ArrayList<>(Arrays.asList(sent1));
        final List<String> sent2List = new ArrayList<>(Arrays.asList(sent2));
        sent1List.retainAll(sent2List);
        String[] intersect = sent1List.toArray(new String[0]);
        return intersect;
    }

    public static String formatSentence(final String sentence) {
        return sentence;
    }

    public static String getBestsentenceFromParagraph(final String paragraph) {
        String[] sentences = splitToSentences(formatSentence(paragraph));
        if (sentences == null || sentences.length <= 2) {
            return "";
        }
        float[][] intersectionMatrix = getSentenceIntersectionMatrix(sentences);
        float[] sentenceScores = getSentenceScores(sentences, intersectionMatrix);
        return getBestSentence(sentences, sentenceScores);
    }

    public static float[][] getSentenceIntersectionMatrix(final String[] sentences) {
        //Split the content in to sentences
        int n = sentences.length;
        float[][] intersectionMatrix = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                try {
                    if (i == j) {
                        continue;
                    }
                    intersectionMatrix[i][j] = sentenceIntersection(sentences[i], sentences[j]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        //Build the sentence dictionary
        //The score of a sentence is the sum of all its intersections
        return intersectionMatrix;
    }

    public static float[] getSentenceScores(final String[] sentences, float[][] scores) {
        float[] scoresReturn = new float[sentences.length];
        for (int i = 0; i < sentences.length; i++) {
            int sentenceScore = 0;
            for (int j = 0; j < scores[i].length; j++) {
                sentenceScore += scores[i][j];
            }
            scoresReturn[i] = sentenceScore;
        }
        return scoresReturn;
    }

    public static String getBestSentence(final String[] sentences, float[] scores) {
        return sentences[getMaxIndex(scores)];
    }

    public static int getMaxIndex(float[] array) {
        int maxIndex = 0;
        float max = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }

        }
        return maxIndex;
    }

    public static TokenizerME tokenizer;
    public static SentenceDetectorME sentenceDetect;
    public static CTextSummaryTool Instance;

    public CTextSummaryTool() {
        initialize();
    }

    public void initialize() {
        final InputStream sentenceModelIS = this.getClass().getResourceAsStream("src/main/resources/Data/en-sent.bin"); //new FileInputStream("src/main/resources/Data/en-sent.bin");
        SentenceModel model;
        try {
            model = new SentenceModel(sentenceModelIS);
            sentenceDetect = new SentenceDetectorME(model);
        } catch (InvalidFormatException ex) {
            LOGGER.error(String.format("ERROR: invalid format stream, message={%s}", ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: invalid process stream, message={%s}", ex.getMessage()));
        }

        final InputStream tokenizerModelIS = this.getClass().getResourceAsStream("src/main/resources/Data/en-token.bin"); //new FileInputStream("src/main/resources/Data/en-token.bin");
        TokenizerModel tokenModel;
        try {
            tokenModel = new TokenizerModel(tokenizerModelIS);
            tokenizer = new TokenizerME(tokenModel);
        } catch (InvalidFormatException ex) {
            LOGGER.error(String.format("ERROR: invalid format stream, message={%s}", ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: invalid process stream, message={%s}", ex.getMessage()));
        }
    }

    public static void main(String[] args) {
        String title = "this is a title";
        String content = "";
        Instance = new CTextSummaryTool();
        try {
            content = new Scanner(new File(args[0])).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("ERROR: cannot read from input stream, message={%s}", e.getMessage()));
        }

        String[] paragraphs = splitToParagraphs(content);
        StringBuilder summary = new StringBuilder();

        for (String p : paragraphs) {
            String bestSent = getBestsentenceFromParagraph(p);
            if (bestSent != null && bestSent.length() > 0) {
                summary.append(bestSent);
            }
        }
        LOGGER.debug(summary);
    }
}
