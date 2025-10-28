package graph.impl;

import graph.core.AbstractGraph;
import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;

import java.util.*;

public class AdjacencyListGraph extends AbstractGraph {
    private Map<Vertex<?>, Map<Vertex<?>, Double>> adjacencyMap;

    public AdjacencyListGraph() {
        super();
        adjacencyMap = new HashMap<>();
    }

    @Override
    public boolean addVertex(Vertex<?> vertex) throws VertexException {
        if (super.addVertex(vertex)) {
            adjacencyMap.put(vertex, new HashMap<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(Vertex<?> vertex) throws VertexException {
        if (vertex == null) {
            throw new VertexException("Vertex cannot be empty");
        }

        if (!containsVertex(vertex)) {
            return false;
        }

        for (Vertex<?> v : adjacencyMap.keySet()) {
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
    public boolean addEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        return addEdge(start, end, 0);
    }

    @Override
    public boolean addEdge(Vertex<?> start, Vertex<?> end, double weight) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
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
    public boolean removeEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
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
    public Edge getEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }

        if (!containsEdge(start, end)) {
            return null;
        }

        return new Edge(start, end, adjacencyMap.get(start).get(end));
    }

    @Override
    public List<Vertex<?>> getNeighbors(Vertex<?> vertex) throws VertexException {
        if (vertex == null) {
            throw new VertexException("Vertex cannot be null");
        }
        if (!containsVertex(vertex)) {
            throw new VertexException("Vertex isn't found in graph");
        }

        return new ArrayList<>(adjacencyMap.get(vertex).keySet());
    }

    @Override
    public boolean containsEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (!containsVertex(start) || !containsVertex(end)) {
            throw new VertexException("Start and end vertex must be in graph");
        }

        return adjacencyMap.get(start).containsKey(end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency list (").append(getVertexCount())
                .append(" vertexes, ").append(getEdgeCount()).append(" edges):\n");

        for (Vertex<?> vertex : getVertices()) {
            sb.append(vertex).append(" -> ");
            Map<Vertex<?>, Double> neighbors = adjacencyMap.get(vertex);
            if (!neighbors.isEmpty()) {
                sb.append("[");
                for (Map.Entry<Vertex<?>, Double> entry : neighbors.entrySet()) {
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
