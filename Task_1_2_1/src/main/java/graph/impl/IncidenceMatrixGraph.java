package graph.impl;

import graph.core.AbstractGraph;
import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;

import java.util.*;

public class IncidenceMatrixGraph extends AbstractGraph {
    private final List<Edge> edges;
    private int[][] incidenceMatrix;

    public IncidenceMatrixGraph() {
        super();
        edges = new ArrayList<>();
        incidenceMatrix = new int[10][0];
    }

    @Override
    public boolean addVertex(Vertex<?> vertex) throws VertexException {
        int currentVertexCount = getVertexCount();
        if (super.addVertex(vertex)) {
            ensureCapacity(currentVertexCount + 1, edges.size());
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

        int indexToRemove = vertexToIndex.get(vertex);

        List<Edge> edgesToRemove = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getStart().equals(vertex) || edge.getEnd().equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }

        for (Edge edge : edgesToRemove) {
            removeEdge((Vertex<?>) edge.getStart(), (Vertex<?>) edge.getEnd());
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

        if (containsEdge(start, end)) {
            return false;
        }

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);
        int edgeIndex = edges.size();

        ensureCapacity(getVertexCount(), edgeIndex + 1);

        edges.add(new Edge(start, end, weight));
        incidenceMatrix[startIndex][edgeIndex] = -1;
        incidenceMatrix[endIndex][edgeIndex] = 1;
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

        int edgeIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
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
    public Edge getEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }

        for (Edge edge : edges) {
            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public List<Vertex<?>> getNeighbors(Vertex<?> vertex) throws VertexException {
        if (vertex == null) {
            throw new VertexException("Vertex cannot be null");
        }
        if (!containsVertex(vertex)) {
            throw new VertexException("Vertex isn't found in graph");
        }

        Set<Vertex<?>> neighbors = new HashSet<>();
        int vertexIndex = vertexToIndex.get(vertex);

        for (int i = 0; i < edges.size(); i++) {
            if (incidenceMatrix[vertexIndex][i] == -1) {
                Edge edge = edges.get(i);
                neighbors.add((Vertex<?>) edge.getEnd());
            }
        }

        return new ArrayList<>(neighbors);
    }

    @Override
    public boolean containsEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (!containsVertex(start) || !containsVertex(end)) {
            throw new VertexException("Start and end vertex must be in graph");
        }

        for (Edge edge : edges) {
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
                sb.append(String.format("%2d ", incidenceMatrix[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
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

            int[][] newMatrix = new int[newVertexCapacity][newEdgeCapacity];

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
}
