package com.wildbeeslabs.jentle.algorithms.math;

import java.util.PriorityQueue;

/**
 * Created by sasakiumi on 3/31/14.
 */
public class Dijikstra {
    public static int INF = 1 << 10;

    public static int[] decode(int v) {
        int[] ret = new int[2];
        ret[0] = v / 1000;
        ret[1] = v % 1000;
        return ret;
    }

    public static int shortestPath(int[][] adj, int s, int N, int g) {
        int d[] = new int[N];
        for (int i = 0; i < N; i++) {
            d[i] = INF;
        }
        d[s] = 0;
        PriorityQueue<Integer> q = new PriorityQueue<Integer>();
        q.add(1000 * 0 + s);
        while (!q.isEmpty()) {
            int[] val = decode(q.poll());
            int dist = val[0];
            int vertex = val[1];
            if (dist > d[vertex]) continue;
            for (int i = 0; i < N; i++) {
                if (d[i] > d[vertex] + adj[vertex][i]) {
                    d[i] = d[vertex] + adj[vertex][i];
                    q.add(1000 * d[i] + i);
                }
            }

        }
        return d[g];

    }
}

/*
public class Main {
    public static void main(String[] args) {
        int N = 5;
        int[][] graph = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                graph[i][j] = Dijikstra.INF;
            }
        }
        graph[0][1] = graph[1][0] = 2;
        graph[0][2] = graph[2][0] = 5;
        graph[1][2] = graph[2][1] = 4;
        graph[1][3] = graph[3][1] = 6;
        graph[1][4] = graph[4][1] = 10;
        graph[2][3] = graph[3][2] = 2;
        graph[3][5] = graph[5][3] = 1;
        graph[4][5] = graph[5][4] = 3;
        graph[4][6] = graph[6][4] = 5;
        graph[5][6] = graph[6][5] = 9;

        for (int i = 1; i < 7; i++) {
            System.out.println(Dijikstra.shortestPath(graph, 0, 7, i));
        }
    }
}
 */
