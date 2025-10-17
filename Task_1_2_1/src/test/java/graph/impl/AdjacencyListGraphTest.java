package graph.impl;

import org.junit.jupiter.api.Test;

import java.util.List;

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
}
