package graph.impl;

import graph.model.Edge;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AdjacencyMatrixGraphTest {

    @Test
    void testAddVertex() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();

        assertTrue(graph.addVertex("A"));
        assertEquals(1, graph.getVertexCount());

        assertFalse(graph.addVertex("A"));
        assertEquals(1, graph.getVertexCount());

        for (int i = 0; i < 15; i++) {
            graph.addVertex("V" + i);
        }
        assertEquals(16, graph.getVertexCount());
    }

    @Test
    void testRemoveVertex() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        assertTrue(graph.removeVertex("B"));
        assertEquals(2, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "C"));

        assertFalse(graph.removeVertex("D"));

        assertTrue(graph.removeVertex("A"));
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddEdge() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");

        assertTrue(graph.addEdge("A", "B"));
        assertEquals(1, graph.getEdgeCount());

        assertFalse(graph.addEdge("A", "B"));
        assertEquals(1, graph.getEdgeCount());

        assertFalse(graph.addEdge("A", "C"));
        assertFalse(graph.addEdge("C", "B"));
    }

    @Test
    void testAddEdgeWithWeight() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
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
    void testRemoveEdge() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.removeEdge("A", "B"));
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.containsEdge("A", "B"));

        assertFalse(graph.removeEdge("A", "B"));

        assertFalse(graph.removeEdge("A", "C"));
    }

    @Test
    void testGetEdge() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
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
    void testGetNeighbors() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");

        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("B"));
        assertTrue(neighbors.contains("C"));

        neighbors = graph.getNeighbors("B");
        assertEquals(0, neighbors.size());

        assertThrows(NoSuchElementException.class, () -> graph.getNeighbors("D"));
    }

    @Test
    void testContainsEdge() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertTrue(graph.containsEdge("A", "B"));

        assertFalse(graph.containsEdge("B", "A"));

        assertFalse(graph.containsEdge("A", "C"));
    }

    @Test
    void testToString() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        String representation = graph.toString();
        assertNotNull(representation);
        assertTrue(representation.contains("2 vertexes, 1 edges"));
        assertTrue(representation.contains("1 "));
        assertTrue(representation.contains("0 "));
    }

    @Test
    void testEnsureCapacityWithLargeGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>();

        for (int i = 0; i < 20; i++) {
            graph.addVertex(i);
        }

        assertEquals(20, graph.getVertexCount());

        for (int i = 0; i < 19; i++) {
            graph.addEdge(i, i + 1);
        }

        assertEquals(19, graph.getEdgeCount());

        for (int i = 0; i < 19; i++) {
            assertTrue(graph.containsEdge(i, i + 1));
        }
    }

    @Test
    void testComplexScenario() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");

        graph.addEdge("A", "B", 1.0);
        graph.addEdge("A", "C", 2.0);
        graph.addEdge("B", "D", 3.0);
        graph.addEdge("C", "D", 4.0);
        graph.addEdge("D", "E", 5.0);

        graph.removeVertex("C");

        assertEquals(4, graph.getVertexCount());
        assertEquals(3, graph.getEdgeCount());

        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("A", "C"));
        assertTrue(graph.containsEdge("B", "D"));
        assertTrue(graph.containsEdge("D", "E"));

        assertEquals(1.0, graph.getEdge("A", "B").getWeight());
        assertEquals(3.0, graph.getEdge("B", "D").getWeight());
        assertEquals(5.0, graph.getEdge("D", "E").getWeight());

        graph.addVertex("F");
        graph.addEdge("A", "F", 6.0);
        graph.addEdge("F", "E", 7.0);

        assertEquals(5, graph.getVertexCount());
        assertEquals(5, graph.getEdgeCount());

        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("B"));
        assertTrue(neighbors.contains("F"));
    }

    @Test
    void testExceptionHandlingWithAddVertex() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(null));
    }

    @Test
    void testExceptionHandlingWithRemoveVertex() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(null));
    }

    @Test
    void testExceptionHandlingWithEdges() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();

        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(null, null));

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(null, null));

        assertThrows(IllegalArgumentException.class, () -> graph.getEdge(null, "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.getEdge("A", null));
        assertThrows(IllegalArgumentException.class, () -> graph.getEdge(null, null));
    }
}
