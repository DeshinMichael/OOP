package graph.api;

import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.model.Vertex;

import java.util.List;

public interface SortingStrategy {
    List<Vertex<?>> sort(Graph graph) throws SortException, VertexException;
}
