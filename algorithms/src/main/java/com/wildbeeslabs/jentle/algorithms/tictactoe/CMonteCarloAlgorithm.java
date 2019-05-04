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

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;

import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * Custom Monte-Carlo tree search implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CMonteCarloAlgorithm {

    private static final int WIN_SCORE = 10;
    private int level;
    @Setter(AccessLevel.NONE)
    private int opponent;

    public CMonteCarloAlgorithm() {
        this.level = 3;
    }

    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    public <T extends CState, U extends ACTreeNode<T, U>> CBoard findNextMove(final U rootNode, final CBoard board, int playerNo) {
        long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel();
        this.opponent = 3 - playerNo;

        rootNode.getData().setBoard(board);
        rootNode.getData().setPlayerNo(opponent);

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            U promisingNode = selectPromisingNode(rootNode);
            // Phase 2 - Expansion
            if (promisingNode.getData().getBoard().checkStatus() == CBoard.CBoardStatus.IN_PROGRESS) {
                expandNode(promisingNode);
            }
            // Phase 3 - Simulation
            U nodeToExplore = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChild();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            // Phase 4 - Update
            backPropogation(nodeToExplore, playoutResult);
        }

        U winnerNode = rootNode.getMaxChild();
        return winnerNode.getData().getBoard();
    }

    private <T extends CState, U extends ACTreeNode<T, U>> U selectPromisingNode(final U rootNode) {
        U node = rootNode;
        while (!node.getChildren().isEmpty()) {
            node = CUct.findBestNodeWithUCT(node);
        }
        return node;
    }

    private <T extends CState, U extends ACTreeNode<T, U>> void expandNode(final U node) {
        final List<CState> possibleStates = node.getData().getAllPossibleStates();
        possibleStates.forEach(state -> {
            U newNode = (U) new CTreeSearch.CTreeSearchNode<>(state);
            newNode.setParent(node);
            newNode.getData().setPlayerNo(node.getData().getOpponent());
            node.getChildren().add(newNode);
        });
    }

    private <T extends CState, U extends ACTreeNode<T, U>> void backPropogation(final U nodeToExplore, int playerNo) {
        U tempNode = nodeToExplore;
        while (Objects.nonNull(tempNode)) {
            tempNode.getData().incrementVisit();
            if (tempNode.getData().getPlayerNo() == playerNo) {
                tempNode.getData().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    private <T extends CState, U extends ACTreeNode<T, U>> int simulateRandomPlayout(final U node) {
        U tempNode = SerializationUtils.clone(node);
        T tempState = tempNode.getData();
        CBoard.CBoardStatus boardStatus = tempState.getBoard().checkStatus();

        if (boardStatus.getValue() == this.opponent) {
            tempNode.getParent().getData().setWinScore(Integer.MIN_VALUE);
            return boardStatus.getValue();
        }
        while (boardStatus == CBoard.CBoardStatus.IN_PROGRESS) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().checkStatus();
        }
        return boardStatus.getValue();
    }
}
