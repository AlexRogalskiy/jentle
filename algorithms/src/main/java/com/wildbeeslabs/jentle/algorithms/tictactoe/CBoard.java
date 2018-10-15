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
package com.wildbeeslabs.jentle.algorithms.tictactoe;

import com.wildbeeslabs.jentle.algorithms.maze.CBFSMazeAlgorithm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom tic-tac-toe board implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CBoard {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CBoard.class);

    private int[][] boardValues;
    private int totalMoves;

    public static final int DEFAULT_BOARD_SIZE = 3;

    public static enum CBoardStatus {

        IN_PROGRESS(-1), DRAW(0), P1(1), P2(2);

        private final int value;

        CBoardStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static CBoardStatus forCode(int code) {
            for (final CBoardStatus status : CBoardStatus.values()) {
                if (status.getValue() == code) {
                    return status;
                }
            }
            return null;
        }
    }

    public CBoard() {
        this.boardValues = new int[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
    }

    public CBoard(int boardSize) {
        this.boardValues = new int[boardSize][boardSize];
    }

    public CBoard(int[][] boardValues) {
        this.boardValues = boardValues;
    }

    public CBoard(int[][] boardValues, int totalMoves) {
        this.boardValues = boardValues;
        this.totalMoves = totalMoves;
    }

    public CBoard(final CBoard board) {
        int boardLength = board.getBoardValues().length;
        this.boardValues = new int[boardLength][boardLength];
        int[][] inputBoardValues = board.getBoardValues();
        int n = inputBoardValues.length;
        for (int i = 0; i < n; i++) {
            System.arraycopy(inputBoardValues[i], 0, this.boardValues[i], 0, inputBoardValues[i].length);
        }
    }

    public void performMove(int player, final CPosition p) {
        this.totalMoves++;
        this.boardValues[p.getX()][p.getY()] = player;
    }

    public int[][] getBoardValues() {
        return this.boardValues;
    }

    public void setBoardValues(int[][] boardValues) {
        this.boardValues = boardValues;
    }

    public CBoardStatus checkStatus() {
        int boardSize = this.boardValues.length;
        int maxIndex = boardSize - 1;
        int[] diag1 = new int[boardSize];
        int[] diag2 = new int[boardSize];

        for (int i = 0; i < boardSize; i++) {
            int[] row = this.boardValues[i];
            int[] col = new int[boardSize];
            for (int j = 0; j < boardSize; j++) {
                col[j] = this.boardValues[j][i];
            }

            int checkRowForWin = checkForWin(row);
            if (checkRowForWin != 0) {
                return CBoardStatus.forCode(checkRowForWin);
            }

            int checkColForWin = checkForWin(col);
            if (checkColForWin != 0) {
                return CBoardStatus.forCode(checkRowForWin);
            }

            diag1[i] = this.boardValues[i][i];
            diag2[i] = this.boardValues[maxIndex - i][i];
        }

        int checkDia1gForWin = checkForWin(diag1);
        if (checkDia1gForWin != 0) {
            return CBoardStatus.forCode(checkDia1gForWin);
        }

        int checkDiag2ForWin = checkForWin(diag2);
        if (checkDiag2ForWin != 0) {
            return CBoardStatus.forCode(checkDiag2ForWin);
        }

        if (getEmptyPositions().size() > 0) {
            return CBoardStatus.IN_PROGRESS;
        }
        return CBoardStatus.DRAW;
    }

    private int checkForWin(int[] row) {
        boolean isEqual = true;
        int size = row.length;
        int previous = row[0];
        for (int i = 0; i < size; i++) {
            if (previous != row[i]) {
                isEqual = false;
                break;
            }
            previous = row[i];
        }
        if (isEqual) {
            return previous;
        }
        return 0;
    }

    public void printBoard() {
        int size = this.boardValues.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(this.boardValues[i][j] + " ");
            }
            LOGGER.debug(StringUtils.LF);
        }
    }

    public List<CPosition> getEmptyPositions() {
        int size = this.boardValues.length;
        final List<CPosition> emptyPositions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.boardValues[i][j] == 0) {
                    emptyPositions.add(CPosition.of(i, j));
                }
            }
        }
        return emptyPositions;
    }

    public void printStatus() {
        switch (this.checkStatus()) {
            case P1:
                LOGGER.debug("Player 1 wins");
                break;
            case P2:
                LOGGER.debug("Player 2 wins");
                break;
            case DRAW:
                LOGGER.debug("Game Draw");
                break;
            case IN_PROGRESS:
                LOGGER.debug("Game In Progress");
                break;
        }
    }
}
