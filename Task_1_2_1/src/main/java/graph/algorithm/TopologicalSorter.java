package graph.algorithm;

import graph.api.Graph;
import graph.api.SortingStrategy;

import java.util.*;

public class TopologicalSorter<V> implements SortingStrategy<V> {

    @Override
    public List<V> sort(Graph<V> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        List<V> result = new LinkedList<>();
        Set<V> visited = new HashSet<>();
        Set<V> temp = new HashSet<>();

        for (V vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (!visitVertex(vertex, graph, visited, temp, result)) {
                    return null;
                }
            }
        }

        if (result.isEmpty() || result.size() < graph.getVertexCount()) {
            throw new IllegalStateException("Graph has a circle, topological sort isn't impossible");
        }

        return result;
    }

    private boolean visitVertex(V vertex, Graph<V> graph, Set<V> visited,
                                Set<V> temp, List<V> result) {
        if (temp.contains(vertex)) {
            return false;
        }

        if (visited.contains(vertex)) {
            return true;
        }

        temp.add(vertex);

        for (V neighbor : graph.getNeighbors(vertex)) {
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
