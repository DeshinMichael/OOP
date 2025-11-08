package graph.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VertexTest {

    @Test
    void testVertexEquality() {
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("A");
        Vertex<String> vertex3 = new Vertex<>("B");

        assertEquals(vertex1, vertex2);
        assertNotEquals(vertex1, vertex3);
        assertEquals(vertex1, vertex1);
        assertNotEquals(vertex1, null);
        assertNotEquals(vertex1, "A");
    }

    @Test
    void testVertexEqualityWithNulls() {
        Vertex<String> vertex1 = new Vertex<>(null);
        Vertex<String> vertex2 = new Vertex<>(null);
        Vertex<String> vertex3 = new Vertex<>("A");

        assertEquals(vertex1, vertex2);
        assertNotEquals(vertex1, vertex3);
        assertNotEquals(vertex3, vertex1);
    }

    @Test
    void testVertexEqualityWithDifferentTypes() {
        Vertex<String> stringVertex = new Vertex<>("1");
        Vertex<Integer> intVertex = new Vertex<>(1);

        assertNotEquals(stringVertex, intVertex);
    }

    @Test
    void testHashCode() {
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("A");
        Vertex<String> vertex3 = new Vertex<>("B");

        assertEquals(vertex1.hashCode(), vertex2.hashCode());
        assertNotEquals(vertex1.hashCode(), vertex3.hashCode());
    }

    @Test
    void testHashCodeWithNull() {
        Vertex<String> vertex1 = new Vertex<>(null);
        Vertex<String> vertex2 = new Vertex<>(null);
        Vertex<String> vertex3 = new Vertex<>("A");

        assertEquals(vertex1.hashCode(), vertex2.hashCode());
        assertNotEquals(vertex1.hashCode(), vertex3.hashCode());
    }

    @Test
    void testToString() {
        Vertex<String> stringVertex = new Vertex<>("A");
        assertEquals("A", stringVertex.toString());

        Vertex<Integer> intVertex = new Vertex<>(42);
        assertEquals("42", intVertex.toString());
    }

    @Test
    void testVertexImmutability() {
        String data = "A";
        Vertex<String> vertex = new Vertex<>(data);
        assertEquals("A", vertex.getData());

        data = "B";
        assertEquals("A", vertex.getData());
    }
}
