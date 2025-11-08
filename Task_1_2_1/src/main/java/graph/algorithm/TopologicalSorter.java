package graph.algorithm;

import graph.api.Graph;
import graph.api.SortingStrategy;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.model.Vertex;

import java.util.*;

public class TopologicalSorter implements SortingStrategy {

    @Override
    public List<Vertex<?>> sort(Graph graph) throws SortException, VertexException {
        if (graph == null) {
            throw new SortException("Graph cannot be null");
        }

        List<Vertex<?>> result = new LinkedList<>();
        Set<Vertex<?>> visited = new HashSet<>();
        Set<Vertex<?>> temp = new HashSet<>();

        for (Vertex<?> vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (!visitVertex(vertex, graph, visited, temp, result)) {
                    throw new SortException("Graph has a cycle, topological sort is impossible");
                }
            }
        }

        return result;
    }

    private boolean visitVertex(Vertex<?> vertex, Graph graph, Set<Vertex<?>> visited,
                                Set<Vertex<?>> temp, List<Vertex<?>> result) throws VertexException {
        if (temp.contains(vertex)) {
            return false;
        }

        if (visited.contains(vertex)) {
            return true;
        }

        temp.add(vertex);

        for (Vertex<?> neighbor : graph.getNeighbors(vertex)) {
            if (!visitVertex(neighbor, graph, visited, temp, result)) {
                return false;
            }
        }

        temp.remove(vertex);
        visited.add(vertex);
        result.add(0, vertex);
        return true;
    }
}
