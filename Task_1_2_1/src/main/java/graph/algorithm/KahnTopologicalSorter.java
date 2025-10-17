package graph.algorithm;

import graph.api.Graph;
import graph.api.SortingStrategy;

import java.util.*;

public class KahnTopologicalSorter<V> implements SortingStrategy<V> {

    @Override
    public List<V> sort(Graph<V> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        Map<V, Integer> inDegree = new HashMap<>();
        for (V vertex : graph.getVertices()) {
            inDegree.put(vertex, 0);
        }

        for (V start : graph.getVertices()) {
            for (V end : graph.getNeighbors(start)) {
                inDegree.put(end, inDegree.get(end) + 1);
            }
        }

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<V> result = new ArrayList<>();
        int visited = 0;

        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            result.add(vertex);
            visited++;

            for (V neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (visited != graph.getVertexCount()) {
            throw new IllegalStateException("Graph has a circle, topological sort isn't impossible");
        }

        return result;
    }
}
