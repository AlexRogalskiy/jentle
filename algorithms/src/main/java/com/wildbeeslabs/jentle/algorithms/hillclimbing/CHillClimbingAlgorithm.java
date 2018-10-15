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
package com.wildbeeslabs.jentle.algorithms.hillclimbing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom hill-climbing algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CHillClimbingAlgorithm {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CHillClimbingAlgorithm.class);

    public static void main(String[] args) {
        final CHillClimbingAlgorithm hillClimbing = new CHillClimbingAlgorithm();
        String blockArr[] = {"B", "C", "D", "A"};
        Stack<String> startState = hillClimbing.getStackWithValues(blockArr);
        String goalBlockArr[] = {"A", "B", "C", "D"};
        Stack<String> goalState = hillClimbing.getStackWithValues(goalBlockArr);
        try {
            final List<CState> solutionSequence = hillClimbing.getRouteWithHillClimbing(startState, goalState);
            solutionSequence.forEach(CHillClimbingAlgorithm::printEachStep);
        } catch (Exception e) {
            LOGGER.error("ERROR: cannot list hill-climbing solution", e);
        }
    }

    private static void printEachStep(final CState state) {
        final List<Stack<String>> stackList = state.getState();
        LOGGER.debug("----------------");
        stackList.forEach(stack -> {
            while (!stack.isEmpty()) {
                LOGGER.debug(stack.pop());
            }
            LOGGER.debug(StringUtils.SPACE);
        });
    }

    private Stack<String> getStackWithValues(final String[] blocks) {
        final Stack<String> stack = new Stack<>();
        for (final String block : blocks) {
            stack.push(block);
        }
        return stack;
    }

    /**
     * This method prepares path from init state to goal state
     *
     * @param initStateStack
     * @param goalStateStack
     * @return
     * @throws java.lang.Exception
     */
    public List<CState> getRouteWithHillClimbing(final Stack<String> initStateStack, final Stack<String> goalStateStack) throws Exception {
        final List<Stack<String>> initStateStackList = new ArrayList<>();
        initStateStackList.add(initStateStack);
        int initStateHeuristics = getHeuristicsValue(initStateStackList, goalStateStack);
        final CState initState = new CState(initStateStackList, initStateHeuristics);

        final List<CState> resultPath = new ArrayList<>();
        resultPath.add(new CState(initState));

        CState currentState = initState;
        boolean noStateFound = false;
        while (!currentState.getState()
                .get(0)
                .equals(goalStateStack) || noStateFound) {
            noStateFound = true;
            CState nextState = findNextState(currentState, goalStateStack);
            if (nextState != null) {
                noStateFound = false;
                currentState = nextState;
                resultPath.add(new CState(nextState));
            }
        }
        return resultPath;
    }

    /**
     * This method finds new state from current state based on goal and
     * heuristics
     *
     * @param currentState
     * @param goalStateStack
     * @return
     */
    public CState findNextState(final CState currentState, final Stack<String> goalStateStack) {
        final List<Stack<String>> listOfStacks = currentState.getState();
        int currentStateHeuristics = currentState.getHeuristics();

        return listOfStacks.stream()
                .map(stack -> {
                    return applyOperationsOnState(listOfStacks, stack, currentStateHeuristics, goalStateStack);
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * This method applies operations on the current state to get a new state
     *
     * @param listOfStacks
     * @param stack
     * @param goalStateStack
     * @param currentStateHeuristics
     * @return
     */
    public CState applyOperationsOnState(final List<Stack<String>> listOfStacks, final Stack<String> stack, int currentStateHeuristics, final Stack<String> goalStateStack) {
        CState tempState;
        List<Stack<String>> tempStackList = new ArrayList<>(listOfStacks);
        String block = stack.pop();
        if (stack.size() == 0) {
            tempStackList.remove(stack);
        }
        tempState = pushElementToNewStack(tempStackList, block, currentStateHeuristics, goalStateStack);
        if (tempState == null) {
            tempState = pushElementToExistingStacks(stack, tempStackList, block, currentStateHeuristics, goalStateStack);
        }
        if (tempState == null) {
            stack.push(block);
        }
        return tempState;
    }

    /**
     * Operation to be applied on a state in order to find new states. This
     * operation pushes an element into a new stack
     */
    private CState pushElementToNewStack(List<Stack<String>> currentStackList, final String block, int currentStateHeuristics, final Stack<String> goalStateStack) {
        CState newState = null;
        final Stack<String> newStack = new Stack<>();
        newStack.push(block);
        currentStackList.add(newStack);
        int newStateHeuristics = getHeuristicsValue(currentStackList, goalStateStack);
        if (newStateHeuristics > currentStateHeuristics) {
            newState = new CState(currentStackList, newStateHeuristics);
        } else {
            currentStackList.remove(newStack);
        }
        return newState;
    }

    /**
     * Operation to be applied on a state in order to find new states. This
     * operation pushes an element into one of the other stacks to explore new
     * states
     */
    private CState pushElementToExistingStacks(final Stack currentStack, final List<Stack<String>> currentStackList, final String block, int currentStateHeuristics, final Stack<String> goalStateStack) {
        Optional<CState> newState = currentStackList.stream()
                .filter(stack -> stack != currentStack)
                .map(stack -> {
                    return pushElementToStack(stack, block, currentStackList, currentStateHeuristics, goalStateStack);
                })
                .filter(Objects::nonNull)
                .findFirst();

        return newState.orElse(null);
    }

    /**
     * This method pushes a block to the stack and returns new state if its
     * closer to goal
     */
    private CState pushElementToStack(final Stack stack, final String block, final List<Stack<String>> currentStackList, int currentStateHeuristics, final Stack<String> goalStateStack) {
        stack.push(block);
        int newStateHeuristics = getHeuristicsValue(currentStackList, goalStateStack);
        if (newStateHeuristics > currentStateHeuristics) {
            return new CState(currentStackList, newStateHeuristics);
        }
        stack.pop();
        return null;
    }

    /**
     * This method returns heuristics value for given state with respect to goal
     * state
     *
     * @param currentState
     * @param goalStateStack
     * @return
     */
    public int getHeuristicsValue(final List<Stack<String>> currentState, final Stack<String> goalStateStack) {
        Integer heuristicValue = currentState.stream()
                .mapToInt(stack -> {
                    return getHeuristicsValueForStack(stack, currentState, goalStateStack);
                })
                .sum();
        return heuristicValue;
    }

    /**
     * This method returns heuristics value for a particular stack
     *
     * @param stack
     * @param currentState
     * @param goalStateStack
     * @return
     */
    public int getHeuristicsValueForStack(final Stack<String> stack, final List<Stack<String>> currentState, final Stack<String> goalStateStack) {
        int stackHeuristics = 0;
        boolean isPositioneCorrect = true;
        int goalStartIndex = 0;
        for (final String currentBlock : stack) {
            if (isPositioneCorrect && currentBlock.equals(goalStateStack.get(goalStartIndex))) {
                stackHeuristics += goalStartIndex;
            } else {
                stackHeuristics -= goalStartIndex;
                isPositioneCorrect = false;
            }
            goalStartIndex++;
        }
        return stackHeuristics;
    }
}
