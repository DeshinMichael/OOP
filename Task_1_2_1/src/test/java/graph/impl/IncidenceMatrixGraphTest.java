package graph.impl;

import graph.model.Edge;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class IncidenceMatrixGraphTest {

    @Test
    void testAddVertex() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex("A"));
    }

    @Test
    void testAddEdge() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");

        assertTrue(graph.addEdge("A", "B"));
        assertFalse(graph.addEdge("A", "B"));
    }

    @Test
    void testRemoveVertex() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.removeVertex("A"));
        assertFalse(graph.containsEdge("A", "B"));
    }

    @Test
    void testRemoveEdge() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.removeEdge("A", "B"));
        assertFalse(graph.containsEdge("A", "B"));
    }

    @Test
    void testGetNeighbors() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains("B"));
    }

    @Test
    void testGetEdge() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
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
    void testContainsEdge() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
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
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        String representation = graph.toString();
        assertNotNull(representation);
        assertTrue(representation.contains("2 vertexes, 1 edges"));
        assertTrue(representation.contains("1 "));
    }

    @Test
    void testEnsureCapacityWithManyVertices() {
        IncidenceMatrixGraph<Integer> graph = new IncidenceMatrixGraph<>();

        for (int i = 0; i < 15; i++) {
            graph.addVertex(i);
        }

        assertEquals(15, graph.getVertexCount());

        for (int i = 0; i < 14; i++) {
            graph.addEdge(i, i+1);
        }

        assertEquals(14, graph.getEdgeCount());
    }

    @Test
    void testExceptionHandling() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();

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
