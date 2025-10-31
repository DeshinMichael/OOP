package graph.algorithm;

import graph.api.Graph;
import graph.api.SortingStrategy;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.model.Vertex;

import java.util.*;

public class KahnTopologicalSorter implements SortingStrategy {

    @Override
    public List<Vertex<?>> sort(Graph graph) throws SortException, VertexException {
        if (graph == null) {
            throw new SortException("Graph cannot be null");
        }

        Map<Vertex<?>, Integer> inDegree = new HashMap<>();
        for (Vertex<?> vertex : graph.getVertices()) {
            inDegree.put(vertex, 0);
        }

        for (Vertex<?> start : graph.getVertices()) {
            for (Vertex<?> end : graph.getNeighbors(start)) {
                inDegree.put(end, inDegree.get(end) + 1);
            }
        }

        Queue<Vertex<?>> queue = new LinkedList<>();
        for (Map.Entry<Vertex<?>, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<Vertex<?>> result = new ArrayList<>();
        int visited = 0;

        while (!queue.isEmpty()) {
            Vertex<?> vertex = queue.poll();
            result.add(vertex);
            visited++;

            for (Vertex<?> neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (visited != graph.getVertexCount()) {
            throw new SortException("Graph has a circle, topological sort is impossible");
        }

        return result;
    }
}
