package graph.algorithm;

import graph.impl.AdjacencyListGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSorterTest {

    @Test
    void testSortValidGraph() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        TopologicalSorter<String> sorter = new TopologicalSorter<>();
        List<String> result = sorter.sort(graph);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.indexOf("A") < result.indexOf("B"));
        assertTrue(result.indexOf("B") < result.indexOf("C"));
    }

    @Test
    void testSortCyclicGraph() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.addEdge("B", "A");

        TopologicalSorter<String> sorter = new TopologicalSorter<>();
        assertNull(sorter.sort(graph));
    }

    @Test
    void testSortEmptyGraph() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        TopologicalSorter<String> sorter = new TopologicalSorter<>();

        Exception exception = assertThrows(IllegalStateException.class, () -> sorter.sort(graph));
        assertTrue(exception.getMessage().contains("Graph has a circle"));
    }
}
