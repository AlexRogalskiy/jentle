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
package com.wildbeeslas.jentle.algorithms.sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom dancing links implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CDancingLinks {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CBackTrackAlgorithm.class);

    private static final int DEFAULT_SIZE = 9;

    private final CColumnNode header;
    private List<CDancingNode> answer;

    private void search(int k) {
        if (this.header.R == this.header) {
            handleSolution(answer);
        } else {
            CColumnNode c = selectColumnNodeHeuristic();
            c.cover();
            for (CDancingNode r = c.D; r != c; r = r.D) {
                answer.add(r);
                for (CDancingNode j = r.R; j != r; j = j.R) {
                    j.C.cover();
                }
                search(k + 1);
                r = answer.remove(answer.size() - 1);
                c = r.C;
                for (CDancingNode j = r.L; j != r; j = j.L) {
                    j.C.uncover();
                }
            }
            c.uncover();
        }
    }

    private CColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        CColumnNode ret = null;
        for (CColumnNode c = (CColumnNode) header.R; c != header; c = (CColumnNode) c.R) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private CColumnNode makeDLXBoard(boolean[][] grid) {
        final int COLS = grid[0].length;

        CColumnNode headerNode = new CColumnNode("header");
        final List<CColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            CColumnNode n = new CColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (CColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.R.C;

        for (boolean[] aGrid : grid) {
            CDancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (aGrid[j]) {
                    CColumnNode col = columnNodes.get(j);
                    CDancingNode newNode = new CDancingNode(col);
                    if (prev == null) {
                        prev = newNode;
                    }
                    col.U.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }
        headerNode.size = COLS;
        return headerNode;
    }

    CDancingLinks(boolean[][] cover) {
        this.header = makeDLXBoard(cover);
    }

    public void runSolver() {
        this.answer = new LinkedList<>();
        search(0);
    }

    private void handleSolution(final List<CDancingNode> answer) {
        int[][] result = parseBoard(answer);
        printSolution(result);
    }

    private int[][] parseBoard(final List<CDancingNode> answer) {
        int[][] result = new int[DEFAULT_SIZE][DEFAULT_SIZE];
        for (final CDancingNode n : answer) {
            CDancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.C.name);
            for (CDancingNode tmp = n.R; tmp != n; tmp = tmp.R) {
                int val = Integer.parseInt(tmp.C.name);
                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = Integer.parseInt(rcNode.C.name);
            int ans2 = Integer.parseInt(rcNode.R.C.name);
            int r = ans1 / DEFAULT_SIZE;
            int c = ans1 % DEFAULT_SIZE;
            int num = (ans2 % DEFAULT_SIZE) + 1;
            result[r][c] = num;
        }
        return result;
    }

    private static void printSolution(int[][] result) {
        int size = result.length;
        for (int[] aResult : result) {
            StringBuilder ret = new StringBuilder();
            for (int j = 0; j < size; j++) {
                ret.append(aResult[j]).append(" ");
            }
            LOGGER.debug(ret);
        }
        LOGGER.debug(StringUtils.LF);
    }
}
