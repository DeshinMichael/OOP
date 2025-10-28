package graph.api;

import graph.exceptions.FileException;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Graph {
    boolean addVertex(Vertex<?> vertex) throws VertexException;
    boolean removeVertex(Vertex<?> vertex) throws VertexException;
    boolean containsVertex(Vertex<?> vertex);
    Set<Vertex<?>> getVertices();
    int getVertexCount();

    boolean addEdge(Vertex<?> start, Vertex<?> end) throws VertexException;
    boolean addEdge(Vertex<?> start, Vertex<?> end, double weight) throws VertexException;
    boolean removeEdge(Vertex<?> start, Vertex<?> end) throws VertexException;
    boolean containsEdge(Vertex<?> start, Vertex<?> end) throws VertexException;
    Edge getEdge(Vertex<?> start, Vertex<?> end) throws VertexException;
    int getEdgeCount();

    List<Vertex<?>> getNeighbors(Vertex<?> vertex) throws VertexException;

    void readFromFile(String filename) throws IOException, FileException, VertexException;
    void writeToFile(String filename) throws IOException, FileException, VertexException;

    List<Vertex<?>> topologicalSort(SortingStrategy strategy) throws SortException, VertexException;
}
