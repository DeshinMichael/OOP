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

        // Изменено: ожида��м исключение при попытке получить соседей несуществующей вершины
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
}
