package graph.impl;

import graph.core.AbstractGraph;
import graph.model.Edge;

import java.util.*;

public class AdjacencyListGraph<V> extends AbstractGraph<V> {
    private Map<V, Map<V, Double>> adjacencyMap;

    public AdjacencyListGraph() {
        super();
        adjacencyMap = new HashMap<>();
    }

    @Override
    public boolean addVertex(V vertex) {
        if (super.addVertex(vertex)) {
            adjacencyMap.put(vertex, new HashMap<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be empty");
        }

        if (!containsVertex(vertex)) {
            return false;
        }

        for (V v : adjacencyMap.keySet()) {
            if (adjacencyMap.get(v).containsKey(vertex)) {
                adjacencyMap.get(v).remove(vertex);
                edgeCount--;
            }
        }

        edgeCount -= adjacencyMap.get(vertex).size();
        adjacencyMap.remove(vertex);

        int indexToRemove = vertexToIndex.get(vertex);
        indexToVertex.remove(indexToRemove);
        vertexToIndex.remove(vertex);

        for (int i = indexToRemove; i < indexToVertex.size(); i++) {
            vertexToIndex.put(indexToVertex.get(i), i);
        }

        return true;
    }

    @Override
    public boolean addEdge(V start, V end) {
        return addEdge(start, end, 0);
    }

    @Override
    public boolean addEdge(V start, V end, double weight) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }

        if (!containsVertex(start) || !containsVertex(end)) {
            return false;
        }

        if (adjacencyMap.get(start).containsKey(end)) {
            return false;
        }

        adjacencyMap.get(start).put(end, weight);
        edgeCount++;
        return true;
    }

    @Override
    public boolean removeEdge(V start, V end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }

        if (!containsVertex(start) || !containsVertex(end)) {
            return false;
        }

        if (adjacencyMap.get(start).remove(end) != null) {
            edgeCount--;
            return true;
        }

        return false;
    }

    @Override
    public Edge<V> getEdge(V start, V end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }

        if (!containsEdge(start, end)) {
            return null;
        }

        return new Edge<>(start, end, adjacencyMap.get(start).get(end));
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (!containsVertex(vertex)) {
            throw new NoSuchElementException("Vertex isn't found in graph");
        }

        return new ArrayList<>(adjacencyMap.get(vertex).keySet());
    }

    @Override
    public boolean containsEdge(V start, V end) {
        if (!containsVertex(start) || !containsVertex(end)) {
            return false;
        }

        return adjacencyMap.get(start).containsKey(end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency list (").append(getVertexCount())
                .append(" vertexes, ").append(getEdgeCount()).append(" edges):\n");

        for (V vertex : getVertices()) {
            sb.append(vertex).append(" -> ");
            Map<V, Double> neighbors = adjacencyMap.get(vertex);
            if (!neighbors.isEmpty()) {
                sb.append("[");
                for (Map.Entry<V, Double> entry : neighbors.entrySet()) {
                    sb.append(entry.getKey());
                    if (entry.getValue() != 0) {
                        sb.append("(").append(entry.getValue()).append(")");
                    }
                    sb.append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                sb.append("]");
            } else {
                sb.append("[]");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
