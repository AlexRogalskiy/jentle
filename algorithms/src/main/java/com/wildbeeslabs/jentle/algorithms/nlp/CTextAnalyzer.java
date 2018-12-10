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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CTextAnalyzer implements Runnable {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CTextAnalyzer.class);

    static TokenizerModel tm = null;
    static TokenNameFinderModel locModel = null;
    String doc;
    NameFinderME myNameFinder;
    TokenizerME wordBreaker;
    SentenceDetector sd;

    public CTextAnalyzer() {
    }

    public CTextAnalyzer(final String document, final SentenceDetector sd, final NameFinderME mf, final TokenizerME wordBreaker) {
        System.out.println("got doc");
        this.sd = sd;
        this.myNameFinder = mf;
        this.wordBreaker = wordBreaker;
        doc = document;
    }

    private static List<String> getMyDocsFromSomewhere() {
        //this should return an object that has all the info about the doc you want 
        return new ArrayList<>();
    }

    public static void main(final String[] args) {
        try {
            String modelPath = "c:\\temp\\opennlpmodels\\";

            if (Objects.isNull(tm)) {
                //user does normal namefinder instantiations... 
                final InputStream stream = new FileInputStream(new File(modelPath + "en-token.zip"));
                try {
                    // new SentenceDetectorME(new SentenceModel(new FileInputStream(new File(modelPath + "en-sent.zip"))));
                    tm = new TokenizerModel(stream);
                    // new TokenizerME(tm); 
                    locModel = new TokenNameFinderModel(new FileInputStream(new File(modelPath + "en-ner-location.bin")));
                    // new NameFinderME(locModel); 
                } catch (IOException ex) {
                    LOGGER.error(String.format("ERROR: invalid process stream, message={%s}", ex.getMessage()));
                }
            }

            LOGGER.debug("getting data");
            List<String> docs = getMyDocsFromSomewhere();
            LOGGER.debug("done getting data");
            // FileWriter fw = new FileWriter("C:\\apache\\modelbuilder\\sentences.txt"); 

            try {
                for (final String docu : docs) {
                    //you could also use the runnable here and launch in a diff thread
                    new CTextAnalyzer(docu,
                            new SentenceDetectorME(new SentenceModel(new FileInputStream(new File(modelPath + "en-sent.zip")))),
                            new NameFinderME(locModel), new TokenizerME(tm)).run();
                }
            } catch (IOException ex) {
                LOGGER.error(String.format("ERROR: invalid process stream, message={%s}", ex.getMessage()));
            }
        } catch (FileNotFoundException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input stream, message={%s}", ex.getMessage()));
        }
    }

    @Override
    public void run() {
        try {
            process(doc);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void process(final String document) throws Exception {
        // System.out.println(document); 
        //user instantiates the non static entitylinkerproperty object and constructs is with a pointer to the prop file they need to use 
        String modelPath = "C:\\apache\\entitylinker\\";
        //input document 
        myNameFinder.clearAdaptiveData();
        //user splits doc to sentences 
        String[] sentences = sd.sentDetect(document);
        //get the sentence spans 
        Span[] sentenceSpans = sd.sentPosDetect(document);
        Span[][] allnamesInDoc = new Span[sentenceSpans.length][];
        String[][] allTokensInDoc = new String[sentenceSpans.length][];

        for (int sentenceIndex = 0; sentenceIndex < sentences.length; sentenceIndex++) {
            String[] stringTokens = wordBreaker.tokenize(sentences[sentenceIndex]);
            Span[] tokenSpans = wordBreaker.tokenizePos(sentences[sentenceIndex]);
            Span[] spans = myNameFinder.find(stringTokens);
            allnamesInDoc[sentenceIndex] = spans;
            allTokensInDoc[sentenceIndex] = stringTokens;
        }

        //now access the data like this... 
        for (int s = 0; s < sentenceSpans.length; s++) {
            Span[] namesInSentence = allnamesInDoc[s];
            String[] tokensInSentence = allTokensInDoc[s];
            String[] entities = Span.spansToStrings(namesInSentence, tokensInSentence);
            for (String entity : entities) {
                //start building up the XML here.... 
                System.out.println(entity + " Was in setnence " + s + " @ " + namesInSentence[s].toString());
            }
        }
    }
}
