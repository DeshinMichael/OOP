package graph.impl;

import graph.core.AbstractGraph;
import graph.model.Edge;

import java.util.*;

public class IncidenceMatrixGraph<V> extends AbstractGraph<V> {
    private List<Edge<V>> edges;
    private boolean[][] incidenceMatrix;

    public IncidenceMatrixGraph() {
        super();
        edges = new ArrayList<>();
        incidenceMatrix = new boolean[10][0];
    }

    private void ensureCapacity(int vertexCount, int edgeCount) {
        if (vertexCount > incidenceMatrix.length ||
                (edgeCount > 0 && (incidenceMatrix.length == 0 || edgeCount > incidenceMatrix[0].length))) {

            int newVertexCapacity = Math.max(incidenceMatrix.length * 2, vertexCount);
            int newEdgeCapacity = Math.max(
                    (incidenceMatrix.length > 0 && incidenceMatrix[0].length > 0 ?
                    incidenceMatrix[0].length * 2 : 1),
                    edgeCount
            );

            boolean[][] newMatrix = new boolean[newVertexCapacity][newEdgeCapacity];

            for (int i = 0; i < Math.min(getVertexCount(), newVertexCapacity); i++) {
                for (int j = 0; j < Math.min(edges.size(), newEdgeCapacity); j++) {
                    if (i < incidenceMatrix.length && j < incidenceMatrix[0].length) {
                        newMatrix[i][j] = incidenceMatrix[i][j];
                    }
                }
            }

            incidenceMatrix = newMatrix;
        }
    }

    @Override
    public boolean addVertex(V vertex) {
        int currentVertexCount = getVertexCount();
        if (super.addVertex(vertex)) {
            ensureCapacity(currentVertexCount + 1, edges.size());
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

        int indexToRemove = vertexToIndex.get(vertex);

        List<Edge<V>> edgesToRemove = new ArrayList<>();
        for (Edge<V> edge : edges) {
            if (edge.getStart().equals(vertex) || edge.getEnd().equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }

        for (Edge<V> edge : edgesToRemove) {
            removeEdge(edge.getStart(), edge.getEnd());
        }

        for (int i = indexToRemove; i < getVertexCount() - 1; i++) {
            for (int j = 0; j < edges.size(); j++) {
                incidenceMatrix[i][j] = incidenceMatrix[i + 1][j];
            }
        }

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

        if (containsEdge(start, end)) {
            return false;
        }

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);
        int edgeIndex = edges.size();

        ensureCapacity(getVertexCount(), edgeIndex + 1);

        edges.add(new Edge<>(start, end, weight));
        incidenceMatrix[startIndex][edgeIndex] = true;
        incidenceMatrix[endIndex][edgeIndex] = true;
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

        int edgeIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge<V> edge = edges.get(i);
            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            return false;
        }

        edges.remove(edgeIndex);
        edgeCount--;

        for (int i = 0; i < getVertexCount(); i++) {
            for (int j = edgeIndex; j < edges.size(); j++) {
                incidenceMatrix[i][j] = incidenceMatrix[i][j + 1];
            }
        }

        return true;
    }

    @Override
    public Edge<V> getEdge(V start, V end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }

        for (Edge<V> edge : edges) {
            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (!containsVertex(vertex)) {
            throw new NoSuchElementException("Vertex isn't found in graph");
        }

        Set<V> neighbors = new HashSet<>();
        int vertexIndex = vertexToIndex.get(vertex);

        for (int i = 0; i < edges.size(); i++) {
            if (incidenceMatrix[vertexIndex][i]) {
                Edge<V> edge = edges.get(i);
                if (edge.getStart().equals(vertex)) {
                    neighbors.add(edge.getEnd());
                }
            }
        }

        return new ArrayList<>(neighbors);
    }

    @Override
    public boolean containsEdge(V start, V end) {
        if (!containsVertex(start) || !containsVertex(end)) {
            return false;
        }

        for (Edge<V> edge : edges) {
            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Incidence matrix (").append(getVertexCount())
                .append(" vertexes, ").append(getEdgeCount()).append(" edges):\n");

        for (int i = 0; i < getVertexCount(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                sb.append(incidenceMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
