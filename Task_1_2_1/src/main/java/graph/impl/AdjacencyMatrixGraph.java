package graph.impl;

import graph.core.AbstractGraph;
import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;

import java.util.*;

public class AdjacencyMatrixGraph extends AbstractGraph {
    private boolean[][] adjacencyMatrix;
    private double[][] weightMatrix;

    public AdjacencyMatrixGraph() {
        super();
        adjacencyMatrix = new boolean[10][10];
        weightMatrix = new double[10][10];
    }

    @Override
    public boolean addVertex(Vertex<?> vertex) throws VertexException {
        int currentSize = getVertexCount();
        if (super.addVertex(vertex)) {
            ensureCapacity(currentSize + 1);
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
        int lastIndex = getVertexCount() - 1;

        for (int i = 0; i < getVertexCount(); i++) {
            if (adjacencyMatrix[indexToRemove][i]) edgeCount--;
            if (i != indexToRemove && adjacencyMatrix[i][indexToRemove]) edgeCount--;
        }

        for (int i = 0; i < getVertexCount(); i++) {
            for (int j = indexToRemove; j < lastIndex; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i][j + 1];
                weightMatrix[i][j] = weightMatrix[i][j + 1];
            }
        }

        for (int i = indexToRemove; i < lastIndex; i++) {
            for (int j = 0; j < getVertexCount(); j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
                weightMatrix[i][j] = weightMatrix[i + 1][j];
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

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);

        if (!adjacencyMatrix[startIndex][endIndex]) {
            adjacencyMatrix[startIndex][endIndex] = true;
            weightMatrix[startIndex][endIndex] = weight;
            edgeCount++;
            return true;
        }

        return false;
    }

    @Override
    public boolean removeEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }

        if (!containsVertex(start) || !containsVertex(end)) {
            return false;
        }

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);

        if (adjacencyMatrix[startIndex][endIndex]) {
            adjacencyMatrix[startIndex][endIndex] = false;
            weightMatrix[startIndex][endIndex] = 0;
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

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);
        return new Edge(start, end, weightMatrix[startIndex][endIndex]);
    }

    @Override
    public List<Vertex<?>> getNeighbors(Vertex<?> vertex) throws VertexException {
        if (vertex == null) {
            throw new VertexException("Vertex cannot be null");
        }
        if (!containsVertex(vertex)) {
            throw new VertexException("Vertex isn't found in graph");
        }

        List<Vertex<?>> neighbors = new ArrayList<>();
        int vertexIndex = vertexToIndex.get(vertex);

        for (int i = 0; i < getVertexCount(); i++) {
            if (adjacencyMatrix[vertexIndex][i]) {
                neighbors.add(indexToVertex.get(i));
            }
        }

        return neighbors;
    }

    @Override
    public boolean containsEdge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (!containsVertex(start) || !containsVertex(end)) {
            throw new VertexException("Start and end vertex must be in graph");
        }

        int startIndex = vertexToIndex.get(start);
        int endIndex = vertexToIndex.get(end);

        return adjacencyMatrix[startIndex][endIndex];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency matrix (").append(getVertexCount())
                .append(" vertexes, ").append(getEdgeCount()).append(" edges):\n");

        for (int i = 0; i < getVertexCount(); i++) {
            for (int j = 0; j < getVertexCount(); j++) {
                sb.append(adjacencyMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity > adjacencyMatrix.length) {
            int newCapacity = Math.max(adjacencyMatrix.length * 2, requiredCapacity);
            boolean[][] newMatrix = new boolean [newCapacity][newCapacity];
            double[][] newWeights = new double[newCapacity][newCapacity];

            for (int i = 0; i < adjacencyMatrix.length; i++) {
                System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix.length);
                System.arraycopy(weightMatrix[i], 0, newWeights[i], 0, weightMatrix.length);
            }

            adjacencyMatrix = newMatrix;
            weightMatrix = newWeights;
        }
    }
}
