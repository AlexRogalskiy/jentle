package com.wildbeeslabs.jentle.collections.graph;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mutable; implements a directed graph; null node is allowed.
 *
 * <p> Note: it uses n1==n2 for comparing nodes rather than using n1.equals(n2)
 *
 * @param <N> - the type of node
 */

public final class DirectedGraph<N> {

    /**
     * This substitutes for null nodes. This allows hasPath() method to use put() to both insert and test membership in one step.
     */
    private static final Object NULL = new Object();

    /**
     * This field maps each node X to a list of "neighbor nodes" that X can reach by following directed edges zero or more times.
     */
    private final Map<Object, List<Object>> nodeToTargets = new IdentityHashMap<Object, List<Object>>();

    /**
     * Constructs an empty graph.
     */
    public DirectedGraph() {
    }

    /**
     * Add a directed edge from start node to end node (if there wasn't such an edge already).
     */
    public void addEdge(N start, N end) {
        if (start == end) return;
        Object a = (start == null ? NULL : start);
        Object b = (end == null ? NULL : end);
        List<Object> targets = nodeToTargets.get(a);
        if (targets == null) {
            targets = new ArrayList<Object>();
            targets.add(b);
            nodeToTargets.put(a, targets);
        } else {
            for (int i = targets.size() - 1; i >= 0; i--) if (targets.get(i) == b) return;
            targets.add(b);
        }
    }

    /**
     * Returns whether there is a directed path from start node to end node by following directed edges 0 or more times (breath-first).
     */
    public boolean hasPath(N start, N end) {
        if (start == end) return true;
        Object a = (start == null ? NULL : start);
        Object b = (end == null ? NULL : end);
        List<Object> todo = new ArrayList<Object>();
        Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
        // The correctness and guaranteed termination relies on following three invariants:
        // (1) Every time we add X to "visited", we also simultaneously add X to "todo".
        // (2) Every time we add X to "todo", we also simultaneously add X to "visited".
        // (3) Nothing is ever removed.
        visited.put(a, a);
        todo.add(a);
        for (int k = 0; k < todo.size(); k++) { // use an integer loop since we will be adding to the "todo" list as we iterate
            List<Object> targets = nodeToTargets.get(todo.get(k));
            if (targets != null) for (int i = targets.size() - 1; i >= 0; i--) {
                Object next = targets.get(i);
                if (next == b) {
                    addEdge(start, end);
                    return true;
                } // Cache so that later hasPath(start,end) returns true immediately
                if (visited.put(next, next) == null) todo.add(next);
            }
        }
        return false;
    }
}
