package graph.impl;

import graph.model.Edge;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AdjacencyListGraphTest {

    @Test
    void testAddVertex() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex("A"));
    }

    @Test
    void testAddEdge() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");

        assertTrue(graph.addEdge("A", "B"));
        assertFalse(graph.addEdge("A", "B"));
    }

    @Test
    void testRemoveVertex() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.removeVertex("A"));
        assertFalse(graph.containsEdge("A", "B"));
    }

    @Test
    void testRemoveEdge() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.removeEdge("A", "B"));
        assertFalse(graph.containsEdge("A", "B"));
    }

    @Test
    void testGetNeighbors() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains("B"));
    }

    @Test
    void testGetEdge() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 3.5);

        Edge<String> edge = graph.getEdge("A", "B");
        assertNotNull(edge);
        assertEquals("A", edge.getStart());
        assertEquals("B", edge.getEnd());
        assertEquals(3.5, edge.getWeight());

        assertNull(graph.getEdge("B", "A"));
        assertNull(graph.getEdge("A", "C"));
    }

    @Test
    void testAddEdgeWithWeight() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");

        assertTrue(graph.addEdge("A", "B", 5.0));
        assertEquals(1, graph.getEdgeCount());

        Edge<String> edge = graph.getEdge("A", "B");
        assertNotNull(edge);
        assertEquals(5.0, edge.getWeight());

        assertFalse(graph.addEdge("A", "B", 10.0));
        assertEquals(1, graph.getEdgeCount());
        assertEquals(5.0, graph.getEdge("A", "B").getWeight());
    }

    @Test
    void testContainsEdge() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");

        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"));
        assertFalse(graph.containsEdge("A", "C"));
        assertFalse(graph.containsEdge("X", "Y"));
    }

    @Test
    void testToString() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        String representation = graph.toString();
        assertNotNull(representation);
        assertTrue(representation.contains("2 vertexes, 1 edges"));
        assertTrue(representation.contains("A ->"));
        assertTrue(representation.contains("B ->"));
    }

    @Test
    void testComplexOperations() {
        AdjacencyListGraph<Integer> graph = new AdjacencyListGraph<>();

        for (int i = 0; i < 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        assertEquals(5, graph.getVertexCount());
        assertEquals(5, graph.getEdgeCount());

        graph.removeVertex(2);

        assertEquals(4, graph.getVertexCount());
        assertEquals(3, graph.getEdgeCount());

        assertTrue(graph.containsEdge(0, 1));
        assertFalse(graph.containsEdge(0, 2));
        assertTrue(graph.containsEdge(1, 3));
        assertTrue(graph.containsEdge(3, 4));
    }

    @Test
    void testExceptionHandling() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();

        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(null));
        assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(null));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.getEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.getEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors(null));

        assertThrows(NoSuchElementException.class, () -> graph.getNeighbors("A"));
    }
}
