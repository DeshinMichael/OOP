package graph.model;

import graph.exceptions.VertexException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void testEdgeEquality() throws VertexException {
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        Vertex<String> vertex3 = new Vertex<>("C");

        Edge edge1 = new Edge(vertex1, vertex2, 5.0);
        Edge edge2 = new Edge(vertex1, vertex2, 10.0);
        Edge edge3 = new Edge(vertex1, vertex3, 5.0);
        Edge edge4 = new Edge(vertex1, vertex2, 5.0);

        assertNotEquals(edge1, edge2);
        assertEquals(edge1, edge4);
        assertNotEquals(edge1, edge3);
        assertNotEquals(edge1, null);
        assertNotEquals(edge1, "string");
        assertEquals(edge1, edge1);
    }

    @Test
    void testEdgeEqualityWithNulls() {
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");

        assertThrows(VertexException.class, () -> new Edge(null, vertex2));
        assertThrows(VertexException.class, () -> new Edge(vertex1, null));
        assertThrows(VertexException.class, () -> new Edge(null, null));
    }

    @Test
    void testHashCode() throws VertexException {
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        Vertex<String> vertex3 = new Vertex<>("C");

        Edge edge1 = new Edge(vertex1, vertex2, 5.0);
        Edge edge2 = new Edge(vertex1, vertex2, 10.0);
        Edge edge3 = new Edge(vertex1, vertex3, 5.0);

        assertNotEquals(edge1.hashCode(), edge2.hashCode());
        assertNotEquals(edge1.hashCode(), edge3.hashCode());
    }
}
