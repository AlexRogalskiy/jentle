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

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom DFS maze algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public class CDFSMazeAlgorithm {

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private CDFSMazeAlgorithm() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public List<CCoordinate> solve(final CMaze maze) {
        final List<CCoordinate> path = new ArrayList<>();
        if (explore(maze, maze.getEntry()
                .getX(),
            maze.getEntry()
                .getY(),
            path)) {
            return path;
        }
        return Collections.EMPTY_LIST;
    }

    private boolean explore(final CMaze maze, int row, int col, final List<CCoordinate> path) {
        if (!maze.isValidLocation(row, col) || maze.isWall(row, col) || maze.isExplored(row, col)) {
            return false;
        }
        path.add(new CCoordinate(row, col));
        maze.setVisited(row, col, true);
        if (maze.isExit(row, col)) {
            return true;
        }
        for (int[] direction : DIRECTIONS) {
            final CCoordinate coordinate = getNextCoordinate(row, col, direction[0], direction[1]);
            if (explore(maze, coordinate.getX(), coordinate.getY(), path)) {
                return true;
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    private CCoordinate getNextCoordinate(int row, int col, int i, int j) {
        return new CCoordinate(row + i, col + j);
    }
}
