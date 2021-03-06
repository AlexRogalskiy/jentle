package com.wildbeeslabs.jentle.collections.graph2;

import java.util.List;

public class GraphMlTreeAdapter implements GraphAdapter<Node<DexClass>> {

    @Override
    public List<? extends Node<DexClass>> getNeighbors(Node<DexClass> node) {
        return node.getChildren();
    }

    @Override
    public String getId(Node<DexClass> node) {
        return "".equals(node.getPath()) ? "[root]" : node.getPath();
    }

    @Override
    public Vertex toVertex(Node<DexClass> node) {
        return new Vertex(getId(node));
    }

    @Override
    public Edge toEdge(Node<DexClass> node1, Node<DexClass> node2) {
        return new Edge(getId(node1), getId(node2));

    }
}
