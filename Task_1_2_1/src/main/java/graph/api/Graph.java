package graph.api;

import graph.model.Edge;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Graph<V> {
    boolean addVertex(V vertex);
    boolean removeVertex(V vertex);
    boolean containsVertex(V vertex);
    Set<V> getVertices();
    int getVertexCount();

    boolean addEdge(V start, V end);
    boolean addEdge(V start, V end, double weight);
    boolean removeEdge(V start, V end);
    boolean containsEdge(V start, V end);
    Edge<V> getEdge(V start, V end);
    int getEdgeCount();

    List<V> getNeighbors(V vertex);

    void readFromFile(String filename) throws IOException;
    void writeToFile(String filename) throws IOException;

    List<V> topologicalSort(SortingStrategy<V> strategy);
}
