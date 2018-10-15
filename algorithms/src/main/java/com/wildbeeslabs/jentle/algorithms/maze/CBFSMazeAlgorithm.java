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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom BFS maze algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CBFSMazeAlgorithm {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CBFSMazeAlgorithm.class);

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private CBFSMazeAlgorithm() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public List<CCoordinate> solve(final CMaze maze) {
        final LinkedList<CCoordinate> nextToVisit = new LinkedList<>();
        final CCoordinate start = maze.getEntry();
        nextToVisit.add(start);

        while (!nextToVisit.isEmpty()) {
            CCoordinate cur = nextToVisit.remove();
            if (!maze.isValidLocation(cur.getX(), cur.getY()) || maze.isExplored(cur.getX(), cur.getY())) {
                continue;
            }
            if (maze.isWall(cur.getX(), cur.getY())) {
                maze.setVisited(cur.getX(), cur.getY(), true);
                continue;
            }
            if (maze.isExit(cur.getX(), cur.getY())) {
                return backtrackPath(cur);
            }
            for (int[] direction : DIRECTIONS) {
                final CCoordinate coordinate = new CCoordinate(cur.getX() + direction[0], cur.getY() + direction[1], cur);
                nextToVisit.add(coordinate);
                maze.setVisited(cur.getX(), cur.getY(), true);
            }
        }
        return Collections.EMPTY_LIST;
    }

    private List<CCoordinate> backtrackPath(final CCoordinate cur) {
        final List<CCoordinate> path = new ArrayList<>();
        CCoordinate iter = cur;

        while (Objects.nonNull(iter)) {
            path.add(iter);
            iter = iter.getParent();
        }

        return path;
    }
}
