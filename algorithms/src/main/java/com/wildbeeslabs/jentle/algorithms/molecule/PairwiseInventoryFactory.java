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
package com.wildbeeslabs.jentle.algorithms.molecule;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class PairwiseInventoryFactory {
    private static Logger log = LoggerFactory.getLogger(PairwiseInventoryFactory.class);

    /**
     * Go through the parameter sets to populate the list of ParameterSets we're going to use. These are the raw materials
     * from which the test cases will be generated
     *
     * @param contents The contents of the Scenario you're testing
     * @return The Scenario, fully populated
     */
    public static Scenario generateScenario(String contents) {
        Scenario scenario = new Scenario();
        for (String line : StringUtils.split(contents, System.getProperty("line.separator"))) {
            scenario.addParameterSet(processOneLine(line));
        }
        return scenario;
    }

    /**
     * Parses a String representing the contents of the Scenario, and returns the Scenario
     *
     * @param contents The contents of the Scenario you're testing
     * @return the Scenario
     */
    public static IInventory generateParameterInventory(String contents) {
        IInventory inventory = new PairwiseInventory();
        Scenario scenario = generateScenario(contents);
        inventory.setScenario(scenario);
        inventory.buildMolecules();
        return inventory;
    }

    public static IInventory generateParameterInventory(InputStream stream) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(isr);

        Scenario scenario = new Scenario();
        String line;
        while ((line = br.readLine()) != null) {
            scenario.addParameterSet(processOneLine(line));
        }

        IInventory inventory = new PairwiseInventory();
        inventory.setScenario(scenario);
        inventory.buildMolecules();
        return inventory;
    }

    /**
     * Processes a single line of inputs
     *
     * @param line One line, containing one parameter space (e.g. "Title: Value1, Value2, Value3")
     * @return The ParameterSet representing the line
     */
    public static ParameterSet<String> processOneLine(String line) {
        log.debug("Processing line: {}", line);
        String[] lineTokens = line.split(":", 2);
        List<String> strValues = splitAndTrim(",", lineTokens[1]);
        ParameterSet<String> parameterSet = new ParameterSet<String>(strValues);
        parameterSet.setName(lineTokens[0]);
        return parameterSet;
    }

    private static List<String> splitAndTrim(String regex, String lineTokens) {
        String[] rawTokens = lineTokens.split(regex);
        String[] processedTokens = new String[rawTokens.length];
        for (int i = 0; i < rawTokens.length; i++) {
            processedTokens[i] = StringUtils.trim(rawTokens[i]);
        }
        return Arrays.asList(processedTokens);
    }
}
