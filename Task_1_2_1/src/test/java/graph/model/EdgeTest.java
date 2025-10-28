package graph.model;

import graph.exceptions.VertexException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void testEdgeEquality() throws VertexException {
        Edge edge1 = new Edge("A", "B", 5.0);
        Edge edge2 = new Edge("A", "B", 10.0);
        Edge edge3 = new Edge("A", "C", 5.0);
        Edge edge4 = new Edge("A", "B", 5.0);

        assertEquals(edge1, edge2);
        assertEquals(edge1, edge4);
        assertNotEquals(edge1, edge3);
        assertNotEquals(edge1, null);
        assertNotEquals(edge1, "string");
        assertEquals(edge1, edge1);
    }

    @Test
    void testEdgeEqualityWithNulls() {
        assertThrows(VertexException.class, () -> new Edge(null, "B"));
        assertThrows(VertexException.class, () -> new Edge("A", null));
        assertThrows(VertexException.class, () -> new Edge(null, null));
    }

    @Test
    void testHashCode() throws VertexException {
        Edge edge1 = new Edge("A", "B", 5.0);
        Edge edge2 = new Edge("A", "B", 10.0);
        Edge edge3 = new Edge("A", "C", 5.0);

        assertEquals(edge1.hashCode(), edge2.hashCode());
        assertNotEquals(edge1.hashCode(), edge3.hashCode());
    }

    @Test
    void testHashCodeWithNulls() {
        assertThrows(VertexException.class, () -> new Edge(null, "B"));
        assertThrows(VertexException.class, () -> new Edge("A", null));
        assertThrows(VertexException.class, () -> new Edge(null, null));
    }

    @Test
    void testToString() throws VertexException {
        Edge edgeWithWeight = new Edge("A", "B", 5.0);
        Edge edgeWithoutWeight = new Edge("A", "B", 0.0);
        Edge edgeZeroWeight = new Edge("A", "B");

        assertEquals("A -> B (5.0)", edgeWithWeight.toString());
        assertEquals("A -> B", edgeWithoutWeight.toString());
        assertEquals("A -> B", edgeZeroWeight.toString());
    }

    @Test
    void testToStringWithNulls() {
        assertThrows(VertexException.class, () -> new Edge(null, null, 2.5));
    }

    @Test
    void testToStringWithNegativeWeight() throws VertexException {
        Edge edge = new Edge("A", "B", -3.0);
        assertEquals("A -> B (-3.0)", edge.toString());
    }
}
