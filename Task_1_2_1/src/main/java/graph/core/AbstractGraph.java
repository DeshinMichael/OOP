package graph.core;

import graph.api.Graph;
import graph.api.SortingStrategy;
import graph.exceptions.FileException;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;

import java.io.*;
import java.util.*;

public abstract class AbstractGraph implements Graph {
    protected Map<Vertex<?>, Integer> vertexToIndex;
    protected List<Vertex<?>> indexToVertex;
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
    public boolean containsVertex(Vertex<?> vertex) {
        return vertexToIndex.containsKey(vertex);
    }

    @Override
    public Set<Vertex<?>> getVertices() {
        return new HashSet<>(indexToVertex);
    }

    @Override
    public boolean addVertex(Vertex<?> vertex) throws VertexException {
        if (vertex == null) {
            throw new VertexException("Vertex cannot be empty");
        }
        if (containsVertex(vertex)) {
            return false;
        }

        vertexToIndex.put(vertex, indexToVertex.size());
        indexToVertex.add(vertex);
        return true;
    }

    @Override
    public List<Vertex<?>> topologicalSort(SortingStrategy strategy) throws SortException, VertexException {
        if (strategy == null) {
            throw new SortException("Sort strategy cannot be null");
        }
        return strategy.sort(this);
    }

    @Override
    public void readFromFile(String filename) throws IOException, FileException, VertexException {
        if (filename == null || filename.isEmpty()) {
            throw new FileException("Name of file cannot be empty");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (parts.length < 2) {
                    throw new FileException("Wrong format of the file");
                }

                Vertex<?> start = parseVertex(parts[0]);
                Vertex<?> end = parseVertex(parts[1]);

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
    public void writeToFile(String filename) throws IOException, FileException, VertexException {
        if (filename == null || filename.isEmpty()) {
            throw new FileException("Name of file cannot be empty");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Vertex<?> start : getVertices()) {
                for (Vertex<?> end : getNeighbors(start)) {
                    Edge edge = getEdge(start, end);
                    if (edge != null) {
                        writer.write(start + " " + end + " " + edge.getWeight());
                        writer.newLine();
                    }
                }
            }
            writer.flush();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;

        Graph that = (Graph) o;

        if (getVertexCount() != that.getVertexCount() || getEdgeCount() != that.getEdgeCount()) {
            return false;
        }

        try {
            for (Vertex<?> start : getVertices()) {
                for (Vertex<?> end : getVertices()) {
                    if (containsEdge(start, end) != that.containsEdge(start, end)) {
                        return false;
                    }
                }
            }
        } catch (VertexException e) {
             return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getVertexCount();
        result = 31 * result + getEdgeCount();

        try {
            for (Vertex<?> start : getVertices()) {
                for (Vertex<?> end : getVertices()) {
                    result = 31 * result + (containsEdge(start, end) ? 1 : 0);
                }
            }
        } catch (VertexException e) {}

        return result;
    }

    private Vertex<?> parseVertex(String str) throws VertexException {
        if (str == null || str.trim().isEmpty()) {
            throw new VertexException("Vertex cannot be empty or null");
        }
        return new Vertex<>(str);
    }
}
