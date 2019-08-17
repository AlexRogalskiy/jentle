package com.wildbeeslabs.jentle.algorithms.sort;

public class UnionFind {
    private static int INITIAL_CAPACITY = 10;
    private int[] parents = new int[INITIAL_CAPACITY];
    private int[] rank = new int[INITIAL_CAPACITY];

    public UnionFind() {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            parents[i] = i;
        }
    }

    public int find(int x) {
        if (parents[x] == x) {
            return x;
        } else {
            return parents[x] = find(parents[x]);
        }
    }

    public void unite(int x, int y) {
        x = find(x);
        y = find(y);

        if (x == y) {
            return;
        }

        if (rank[x] < rank[y]) {
            parents[x] = y;
        } else {
            parents[y] = x;
            if (rank[x] == rank[y]) {
                rank[x]++;
            }
        }
    }

    public boolean same(int x, int y) {
        return find(x) == find(y);
    }
}
