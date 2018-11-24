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
package com.wildbeeslabs.jentle.algorithms.maze;

import com.wildbeeslabs.jentle.algorithms.utils.CCollectionUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CFileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom maze implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CMaze {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CMaze.class);

    private static final int ROAD = 0;
    private static final int WALL = 1;
    private static final int START = 2;
    private static final int EXIT = 3;
    private static final int PATH = 4;

    private int[][] maze;
    private boolean[][] visited;
    private CCoordinate start;
    private CCoordinate end;

    public CMaze(final File file) throws FileNotFoundException {
        final List<String> lines = CFileUtils.readAllLines(file);
        initializeMaze(CCollectionUtils.join(lines.stream(), StringUtils.EMPTY));
    }

    private void initializeMaze(final String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("empty lines data");
        }
        String[] lines = text.split("[\r]?\n");
        this.maze = new int[lines.length][lines[0].length()];
        this.visited = new boolean[lines.length][lines[0].length()];

        for (int row = 0; row < getHeight(); row++) {
            if (lines[row].length() != getWidth()) {
                throw new IllegalArgumentException("line " + (row + 1) + " wrong length (was " + lines[row].length() + " but should be " + getWidth() + ")");
            }
            for (int col = 0; col < getWidth(); col++) {
                if (lines[row].charAt(col) == '#') {
                    this.maze[row][col] = WALL;
                } else if (lines[row].charAt(col) == 'S') {
                    this.maze[row][col] = START;
                    start = new CCoordinate(row, col);
                } else if (lines[row].charAt(col) == 'E') {
                    this.maze[row][col] = EXIT;
                    end = new CCoordinate(row, col);
                } else {
                    this.maze[row][col] = ROAD;
                }
            }
        }
    }

    public int getHeight() {
        return this.maze.length;
    }

    public int getWidth() {
        return this.maze[0].length;
    }

    public CCoordinate getEntry() {
        return this.start;
    }

    public CCoordinate getExit() {
        return this.end;
    }

    public boolean isExit(int x, int y) {
        return (x == this.end.getX() && y == this.end.getY());
    }

    public boolean isStart(int x, int y) {
        return (x == this.start.getX() && y == this.start.getY());
    }

    public boolean isExplored(int row, int col) {
        return this.visited[row][col];
    }

    public boolean isWall(int row, int col) {
        return this.maze[row][col] == WALL;
    }

    public void setVisited(int row, int col, boolean value) {
        this.visited[row][col] = value;
    }

    public boolean isValidLocation(int row, int col) {
        if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) {
            return false;
        }
        return true;
    }

    public void printPath(final List<CCoordinate> path) {
        int[][] tempMaze = Arrays.stream(this.maze)
                .map(int[]::clone)
                .toArray(int[][]::new);
        path.stream().filter((coordinate) -> !(this.isStart(coordinate.getX(), coordinate.getY()) || this.isExit(coordinate.getX(), coordinate.getY()))).forEach((coordinate) -> {
            tempMaze[coordinate.getX()][coordinate.getY()] = PATH;
        });
        LOGGER.debug(toString(tempMaze));
    }

    public String toString(int[][] maze) {
        StringBuilder result = new StringBuilder(getWidth() * (getHeight() + 1));
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (maze[row][col] == ROAD) {
                    result.append(' ');
                } else if (maze[row][col] == WALL) {
                    result.append('#');
                } else if (maze[row][col] == START) {
                    result.append('S');
                } else if (maze[row][col] == EXIT) {
                    result.append('E');
                } else {
                    result.append('.');
                }
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    public void reset() {
        for (boolean[] visited1 : this.visited) {
            Arrays.fill(visited1, false);
        }
    }
}
