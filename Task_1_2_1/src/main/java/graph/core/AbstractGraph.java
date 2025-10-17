package graph.core;

import graph.api.Graph;
import graph.api.SortingStrategy;
import graph.model.Edge;

import java.io.*;
import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected Map<V, Integer> vertexToIndex;
    protected List<V> indexToVertex;
    protected int edgeCount;

    public AbstractGraph() {
        vertexToIndex = new HashMap<>();
        indexToVertex = new ArrayList<>();
        edgeCount = 0;
    }

    @Override
    public int getVertexCount() {
        return indexToVertex.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @Override
    public boolean containsVertex(V vertex) {
        return vertexToIndex.containsKey(vertex);
    }

    @Override
    public Set<V> getVertices() {
        return new HashSet<>(indexToVertex);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be empty");
        }
        if (containsVertex(vertex)) {
            return false;
        }

        vertexToIndex.put(vertex, indexToVertex.size());
        indexToVertex.add(vertex);
        return true;
    }

    @Override
    public List<V> topologicalSort(SortingStrategy<V> strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Sort strategy cannot be null");
        }
        return strategy.sort(this);
    }

    @Override
    public void readFromFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Name of file cannot be empty");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Wrong format of the file");
                }

                V start = parseVertex(parts[0]);
                V end = parseVertex(parts[1]);

                if (!containsVertex(start)) {
                    addVertex(start);
                }

                if (!containsVertex(end)) {
                    addVertex(end);
                }

                if (parts.length >= 3) {
                    double weight = Double.parseDouble(parts[2]);
                    addEdge(start, end, weight);
                } else {
                    addEdge(start, end);
                }
            }
        }
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Name of file cannot be empty");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (V start : getVertices()) {
                for (V end : getNeighbors(start)) {
                    Edge<V> edge = getEdge(start, end);
                    if (edge != null) {
                        writer.write(start + " " + end + " " + edge.getWeight());
                        writer.newLine();
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;

        Graph<V> that = (Graph<V>) o;

        if (getVertexCount() != that.getVertexCount() || getEdgeCount() != that.getEdgeCount()) {
            return false;
        }

        for (V start : getVertices()) {
            for (V end : getVertices()) {
                if (containsEdge(start, end) != that.containsEdge(start, end)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getVertexCount();
        result = 31 * result + getEdgeCount();

        for (V start : getVertices()) {
            for (V end : getVertices()) {
                result = 31 * result + (containsEdge(start, end) ? 1 : 0);
            }
        }

        return result;
    }

    private V parseVertex(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Vertex cannot be empty or null");
        }
        return (V) str;
    }
}
