package graph.impl;

import graph.exceptions.VertexException;
import graph.model.Edge;
import graph.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {
    private AdjacencyListGraph graph;
    private Vertex<String> vertexA;
    private Vertex<String> vertexB;
    private Vertex<String> vertexC;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph();
        vertexA = new Vertex<>("A");
        vertexB = new Vertex<>("B");
        vertexC = new Vertex<>("C");
    }

    @Test
    void testAddVertex() throws VertexException {
        assertTrue(graph.addVertex(vertexA));
        assertEquals(1, graph.getVertexCount());
        assertTrue(graph.containsVertex(vertexA));
    }

    @Test
    void testAddDuplicateVertex() throws VertexException {
        graph.addVertex(vertexA);
        assertFalse(graph.addVertex(vertexA));
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testRemoveVertex() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        assertTrue(graph.removeVertex(vertexA));
        assertEquals(1, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.containsVertex(vertexA));
    }

    @Test
    void testRemoveVertexNull() {
        assertThrows(VertexException.class, () -> graph.removeVertex(null));
    }

    @Test
    void testRemoveNonExistentVertex() throws VertexException {
        assertFalse(graph.removeVertex(vertexA));
    }

    @Test
    void testRemoveVertexWithIncomingEdges() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexC, vertexB);

        assertTrue(graph.removeVertex(vertexB));
        assertEquals(2, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    void testAddEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        assertTrue(graph.addEdge(vertexA, vertexB));
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.containsEdge(vertexA, vertexB));
    }

    @Test
    void testAddEdgeWithWeight() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        assertTrue(graph.addEdge(vertexA, vertexB, 2.5));
        assertEquals(1, graph.getEdgeCount());

        Edge edge = graph.getEdge(vertexA, vertexB);
        assertNotNull(edge);
        assertEquals(2.5, edge.getWeight());
    }

    @Test
    void testAddEdgeNullVertices() throws VertexException {
        graph.addVertex(vertexA);

        assertThrows(VertexException.class, () -> graph.addEdge(null, vertexA));
        assertThrows(VertexException.class, () -> graph.addEdge(vertexA, null));
        assertThrows(VertexException.class, () -> graph.addEdge(null, null));
    }

    @Test
    void testAddEdgeNonExistentVertex() throws VertexException {
        graph.addVertex(vertexA);

        assertFalse(graph.addEdge(vertexA, vertexB));
        assertFalse(graph.addEdge(vertexB, vertexA));
    }

    @Test
    void testAddDuplicateEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        assertTrue(graph.addEdge(vertexA, vertexB));
        assertFalse(graph.addEdge(vertexA, vertexB));
        assertEquals(1, graph.getEdgeCount());
    }

    @Test
    void testRemoveEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        assertTrue(graph.removeEdge(vertexA, vertexB));
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.containsEdge(vertexA, vertexB));
    }

    @Test
    void testRemoveEdgeNullVertices() throws VertexException {
        graph.addVertex(vertexA);

        assertThrows(VertexException.class, () -> graph.removeEdge(null, vertexA));
        assertThrows(VertexException.class, () -> graph.removeEdge(vertexA, null));
    }

    @Test
    void testRemoveEdgeNonExistentVertex() throws VertexException {
        graph.addVertex(vertexA);

        assertFalse(graph.removeEdge(vertexA, vertexB));
        assertFalse(graph.removeEdge(vertexB, vertexA));
    }

    @Test
    void testRemoveNonExistentEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        assertFalse(graph.removeEdge(vertexA, vertexB));
    }

    @Test
    void testGetEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB, 3.0);

        Edge edge = graph.getEdge(vertexA, vertexB);
        assertNotNull(edge);
        assertEquals(vertexA, edge.getStart());
        assertEquals(vertexB, edge.getEnd());
        assertEquals(3.0, edge.getWeight());
    }

    @Test
    void testGetEdgeNullVertices() {
        assertThrows(VertexException.class, () -> graph.getEdge(null, vertexA));
    }

    @Test
    void testGetNonExistentEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        assertNull(graph.getEdge(vertexA, vertexB));
    }

    @Test
    void testGetNeighbors() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexA, vertexC);

        List<Vertex<?>> neighbors = graph.getNeighbors(vertexA);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(vertexB));
        assertTrue(neighbors.contains(vertexC));
    }

    @Test
    void testGetNeighborsNull() {
        assertThrows(VertexException.class, () -> graph.getNeighbors(null));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        assertThrows(VertexException.class, () -> graph.getNeighbors(vertexA));
    }

    @Test
    void testGetNeighborsNoNeighbors() throws VertexException {
        graph.addVertex(vertexA);

        List<Vertex<?>> neighbors = graph.getNeighbors(vertexA);
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testContainsEdge() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        assertTrue(graph.containsEdge(vertexA, vertexB));
        assertFalse(graph.containsEdge(vertexB, vertexA));
    }

    @Test
    void testContainsEdgeNonExistentVertices() throws VertexException {
        graph.addVertex(vertexA);

        assertThrows(VertexException.class, () -> graph.containsEdge(vertexA, vertexB));
        assertThrows(VertexException.class, () -> graph.containsEdge(vertexB, vertexA));
    }

    @Test
    void testToString() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB, 2.5);

        String result = graph.toString();
        assertNotNull(result);
        assertTrue(result.contains("Adjacency list"));
        assertTrue(result.contains("2 vertexes"));
        assertTrue(result.contains("1 edges"));
    }

    @Test
    void testToStringEmptyGraph() {
        String result = graph.toString();
        assertNotNull(result);
        assertTrue(result.contains("0 vertexes"));
        assertTrue(result.contains("0 edges"));
    }

    @Test
    void testToStringWithZeroWeight() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB, 0);

        String result = graph.toString();
        assertNotNull(result);
        assertFalse(result.contains("(0"));
    }

    @Test
    void testComplexOperations() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);

        graph.addEdge(vertexA, vertexB, 1.0);
        graph.addEdge(vertexB, vertexC, 2.0);
        graph.addEdge(vertexA, vertexC, 3.0);

        assertEquals(3, graph.getVertexCount());
        assertEquals(3, graph.getEdgeCount());

        assertTrue(graph.removeVertex(vertexB));
        assertEquals(2, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.containsEdge(vertexA, vertexC));
    }

    @Test
    void testRemoveVertexWithSelfLoop() throws VertexException {
        graph.addVertex(vertexA);
        graph.addEdge(vertexA, vertexA);

        assertEquals(1, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.containsEdge(vertexA, vertexA));

        assertTrue(graph.removeVertex(vertexA));
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
    }
}
