package graph.core;

import graph.api.SortingStrategy;
import graph.exceptions.FileException;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.impl.AdjacencyListGraph;
import graph.impl.AdjacencyMatrixGraph;
import graph.impl.IncidenceMatrixGraph;
import graph.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbstractGraphTest {
    private AbstractGraph graph;
    private Vertex<String> vertexA;
    private Vertex<String> vertexB;
    private Vertex<String> vertexC;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph();
        vertexA = new Vertex<>("A");
        vertexB = new Vertex<>("B");
        vertexC = new Vertex<>("C");
    }

    @Test
    void testContainsVertex() throws VertexException {
        assertFalse(graph.containsVertex(vertexA));
        graph.addVertex(vertexA);
        assertTrue(graph.containsVertex(vertexA));
    }

    @Test
    void testGetVertices() throws VertexException {
        assertTrue(graph.getVertices().isEmpty());

        graph.addVertex(vertexA);
        graph.addVertex(vertexB);

        Set<Vertex<?>> vertices = graph.getVertices();
        assertEquals(2, vertices.size());
        assertTrue(vertices.contains(vertexA));
        assertTrue(vertices.contains(vertexB));
    }

    @Test
    void testAddVertexSuccess() throws VertexException {
        assertTrue(graph.addVertex(vertexA));
        assertEquals(1, graph.getVertexCount());
        assertTrue(graph.containsVertex(vertexA));
    }

    @Test
    void testAddVertexDuplicate() throws VertexException {
        graph.addVertex(vertexA);
        assertFalse(graph.addVertex(vertexA));
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddVertexNull() {
        assertThrows(VertexException.class, () -> graph.addVertex(null));
    }

    @Test
    void testTopologicalSortWithNullStrategy() throws VertexException {
        graph.addVertex(vertexA);
        assertThrows(SortException.class, () -> graph.topologicalSort(null));
    }

    @Test
    void testTopologicalSortWithValidStrategy() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        SortingStrategy mockStrategy = new SortingStrategy() {
            @Override
            public List<Vertex<?>> sort(graph.api.Graph graph) {
                return Arrays.asList(vertexA, vertexB);
            }
        };

        List<Vertex<?>> result = graph.topologicalSort(mockStrategy);
        assertEquals(2, result.size());
        assertEquals(vertexA, result.get(0));
        assertEquals(vertexB, result.get(1));
    }

    @Test
    void testReadFromFileNullFilename() {
        assertThrows(FileException.class, () -> graph.readFromFile(null));
    }

    @Test
    void testReadFromFileEmptyFilename() {
        assertThrows(FileException.class, () -> graph.readFromFile(""));
    }

    @Test
    void testReadFromFileNonExistentFile() {
        assertThrows(IOException.class, () -> graph.readFromFile("nonexistent.txt"));
    }

    @Test
    void testReadFromFileValidFormat() throws IOException, FileException, VertexException {
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A B 1.5\n");
            writer.write("B C\n");
            writer.write("C A 2.0\n");
        }

        graph.readFromFile(testFile.getAbsolutePath());

        assertEquals(3, graph.getVertexCount());
        assertEquals(3, graph.getEdgeCount());
        assertTrue(graph.containsVertex(new Vertex<>("A")));
        assertTrue(graph.containsVertex(new Vertex<>("B")));
        assertTrue(graph.containsVertex(new Vertex<>("C")));
    }

    @Test
    void testReadFromFileInvalidFormat() throws IOException {
        File testFile = tempDir.resolve("invalid.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A\n");
        }

        assertThrows(FileException.class, () -> graph.readFromFile(testFile.getAbsolutePath()));
    }

    @Test
    void testReadFromFileWithEmptyLines() throws IOException, FileException, VertexException {
        File testFile = tempDir.resolve("empty_lines.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A B\n");
            writer.write("\n");
            writer.write("   \n");
            writer.write("B C\n");
        }

        graph.readFromFile(testFile.getAbsolutePath());
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());
    }

    @Test
    void testWriteToFileNullFilename() {
        assertThrows(FileException.class, () -> graph.writeToFile(null));
    }

    @Test
    void testWriteToFileEmptyFilename() {
        assertThrows(FileException.class, () -> graph.writeToFile(""));
    }

    @Test
    void testWriteToFileSuccess() throws IOException, FileException, VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB, 2.5);

        File outputFile = tempDir.resolve("output.txt").toFile();
        graph.writeToFile(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    void testEqualsWithSameGraph() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        AbstractGraph otherGraph = new AdjacencyListGraph();
        otherGraph.addVertex(vertexA);
        otherGraph.addVertex(vertexB);
        otherGraph.addEdge(vertexA, vertexB);

        assertTrue(graph.equals(otherGraph));
    }

    @Test
    void testEqualsWithDifferentVertexCount() throws VertexException {
        graph.addVertex(vertexA);

        AbstractGraph otherGraph = new AdjacencyListGraph();
        otherGraph.addVertex(vertexA);
        otherGraph.addVertex(vertexB);

        assertFalse(graph.equals(otherGraph));
    }

    @Test
    void testEqualsWithDifferentEdgeCount() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        AbstractGraph otherGraph = new AdjacencyListGraph();
        otherGraph.addVertex(vertexA);
        otherGraph.addVertex(vertexB);

        assertFalse(graph.equals(otherGraph));
    }

    @Test
    void testEqualsWithNull() throws VertexException {
        graph.addVertex(vertexA);
        assertFalse(graph.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() throws VertexException {
        graph.addVertex(vertexA);
        assertFalse(graph.equals("not a graph"));
    }

    @Test
    void testEqualsSameReference() throws VertexException {
        graph.addVertex(vertexA);
        assertTrue(graph.equals(graph));
    }

    @Test
    void testHashCodeConsistency() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        int hash1 = graph.hashCode();
        int hash2 = graph.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeEqualGraphs() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        AbstractGraph otherGraph = new AdjacencyListGraph();
        otherGraph.addVertex(vertexA);
        otherGraph.addVertex(vertexB);
        otherGraph.addEdge(vertexA, vertexB);

        assertEquals(graph.hashCode(), otherGraph.hashCode());
    }

    @Test
    void testParseVertexWithEmptyString() throws IOException, FileException {
        File testFile = tempDir.resolve("empty_vertex.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("  B\n");
        }

        assertThrows(VertexException.class, () -> graph.readFromFile(testFile.getAbsolutePath()));
    }

    @Test
    void testEqualsAcrossDifferentGraphTypes() throws VertexException {
        AbstractGraph adjacencyListGraph = new AdjacencyListGraph();
        AbstractGraph adjacencyMatrixGraph = new AdjacencyMatrixGraph();
        AbstractGraph incidenceMatrixGraph = new IncidenceMatrixGraph();

        adjacencyListGraph.addVertex(vertexA);
        adjacencyListGraph.addVertex(vertexB);
        adjacencyListGraph.addVertex(vertexC);
        adjacencyListGraph.addEdge(vertexA, vertexB, 1.0);
        adjacencyListGraph.addEdge(vertexB, vertexC, 2.0);

        adjacencyMatrixGraph.addVertex(vertexA);
        adjacencyMatrixGraph.addVertex(vertexB);
        adjacencyMatrixGraph.addVertex(vertexC);
        adjacencyMatrixGraph.addEdge(vertexA, vertexB, 1.0);
        adjacencyMatrixGraph.addEdge(vertexB, vertexC, 2.0);

        incidenceMatrixGraph.addVertex(vertexA);
        incidenceMatrixGraph.addVertex(vertexB);
        incidenceMatrixGraph.addVertex(vertexC);
        incidenceMatrixGraph.addEdge(vertexA, vertexB, 1.0);
        incidenceMatrixGraph.addEdge(vertexB, vertexC, 2.0);

        assertTrue(adjacencyListGraph.equals(adjacencyMatrixGraph));
        assertTrue(adjacencyMatrixGraph.equals(incidenceMatrixGraph));
        assertTrue(adjacencyListGraph.equals(incidenceMatrixGraph));
    }

    @Test
    void testNotEqualsAcrossDifferentGraphTypesWithDifferentEdges() throws VertexException {
        AbstractGraph adjacencyListGraph = new AdjacencyListGraph();
        AbstractGraph adjacencyMatrixGraph = new AdjacencyMatrixGraph();

        adjacencyListGraph.addVertex(vertexA);
        adjacencyListGraph.addVertex(vertexB);
        adjacencyListGraph.addVertex(vertexC);

        adjacencyMatrixGraph.addVertex(vertexA);
        adjacencyMatrixGraph.addVertex(vertexB);
        adjacencyMatrixGraph.addVertex(vertexC);

        adjacencyListGraph.addEdge(vertexA, vertexB);
        adjacencyListGraph.addEdge(vertexB, vertexC);

        adjacencyMatrixGraph.addEdge(vertexA, vertexC);
        adjacencyMatrixGraph.addEdge(vertexB, vertexA);

        assertFalse(adjacencyListGraph.equals(adjacencyMatrixGraph));
        assertFalse(adjacencyMatrixGraph.equals(adjacencyListGraph));
    }

    @Test
    void testHashCodeConsistencyAcrossDifferentGraphTypes() throws VertexException {
        AbstractGraph adjacencyListGraph = new AdjacencyListGraph();
        AbstractGraph incidenceMatrixGraph = new IncidenceMatrixGraph();

        adjacencyListGraph.addVertex(vertexA);
        adjacencyListGraph.addVertex(vertexB);
        adjacencyListGraph.addEdge(vertexA, vertexB, 5.0);

        incidenceMatrixGraph.addVertex(vertexA);
        incidenceMatrixGraph.addVertex(vertexB);
        incidenceMatrixGraph.addEdge(vertexA, vertexB, 5.0);

        assertTrue(adjacencyListGraph.equals(incidenceMatrixGraph));
        assertEquals(adjacencyListGraph.hashCode(), incidenceMatrixGraph.hashCode());
    }
}
