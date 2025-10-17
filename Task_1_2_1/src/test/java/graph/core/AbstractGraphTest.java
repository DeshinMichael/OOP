package graph.core;

import graph.algorithm.TopologicalSorter;
import graph.api.SortingStrategy;
import graph.model.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbstractGraphTest {

    private TestGraph graph;

    @BeforeEach
    void setUp() {
        graph = new TestGraph();
    }

    @Test
    void testAddVertexBasicFunctionality() {
        assertTrue(graph.addVertex("A"));
        assertTrue(graph.containsVertex("A"));
        assertEquals(1, graph.getVertexCount());

        assertFalse(graph.addVertex("A"));
        assertEquals(1, graph.getVertexCount());

        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(null));
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testGetVertices() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        Set<String> vertices = graph.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains("A"));
        assertTrue(vertices.contains("B"));
        assertTrue(vertices.contains("C"));

        vertices.add("D");
        assertFalse(graph.containsVertex("D"));
    }

    @Test
    void testTopologicalSortDAG() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNotNull(result);
        assertEquals(4, result.size());

        int indexA = result.indexOf("A");
        int indexB = result.indexOf("B");
        int indexC = result.indexOf("C");
        int indexD = result.indexOf("D");

        assertTrue(indexA < indexB);
        assertTrue(indexA < indexC);
        assertTrue(indexB < indexD);
        assertTrue(indexC < indexD);
    }

    @Test
    void testTopologicalSortWithCycle() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNull(result);
    }

    @Test
    void testTopologicalSortEmptyGraph() {
        assertThrows(IllegalStateException.class, () -> {
            graph.topologicalSort(new TopologicalSorter<>());
        });
    }

    @Test
    void testTopologicalSortSingleVertex() {
        graph.addVertex("A");
        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0));
    }

    @Test
    void testTopologicalSortDisconnectedComponents() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        graph.addEdge("A", "B");
        graph.addEdge("C", "D");

        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNotNull(result);
        assertEquals(4, result.size());

        int indexA = result.indexOf("A");
        int indexB = result.indexOf("B");
        int indexC = result.indexOf("C");
        int indexD = result.indexOf("D");

        assertTrue(indexA < indexB);
        assertTrue(indexC < indexD);
    }

    @Test
    void testReadFromFileBasic(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test-graph.txt").toFile();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A B 5.0\n");
            writer.write("B C 3.0\n");
            writer.write("C D\n");
            writer.write("A C 2.0\n");
        }

        graph.readFromFile(testFile.getAbsolutePath());

        assertEquals(4, graph.getVertexCount());
        assertEquals(4, graph.getEdgeCount());

        assertTrue(graph.containsVertex("A"));
        assertTrue(graph.containsVertex("B"));
        assertTrue(graph.containsVertex("C"));
        assertTrue(graph.containsVertex("D"));

        assertEquals(5.0, graph.getEdge("A", "B").getWeight());
        assertEquals(3.0, graph.getEdge("B", "C").getWeight());
        assertEquals(0.0, graph.getEdge("C", "D").getWeight());
        assertEquals(2.0, graph.getEdge("A", "C").getWeight());
    }

    @Test
    void testReadFromFileWithEmptyLines(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test-graph-empty-lines.txt").toFile();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A B 5.0\n");
            writer.write("\n");
            writer.write("   \n");
            writer.write("B C 3.0\n");
        }

        graph.readFromFile(testFile.getAbsolutePath());

        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());
    }

    @Test
    void testReadFromFileInvalidFormat(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("invalid-graph.txt").toFile();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A\n");
        }

        assertThrows(IllegalArgumentException.class, () -> {
            graph.readFromFile(testFile.getAbsolutePath());
        });
    }

    @Test
    void testReadFromFileInvalidWeight(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("invalid-weight-graph.txt").toFile();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("A B invalid_weight\n");
        }

        assertThrows(NumberFormatException.class, () -> {
            graph.readFromFile(testFile.getAbsolutePath());
        });
    }

    @Test
    void testWriteToFile(@TempDir Path tempDir) throws IOException {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 5.0);
        graph.addEdge("B", "C", 3.0);
        graph.addEdge("A", "C", 0.0);

        File outputFile = tempDir.resolve("output-graph.txt").toFile();
        graph.writeToFile(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());

        TestGraph newGraph = new TestGraph();
        newGraph.readFromFile(outputFile.getAbsolutePath());

        assertEquals(graph.getVertexCount(), newGraph.getVertexCount());
        assertEquals(graph.getEdgeCount(), newGraph.getEdgeCount());

        for (String vertex : graph.getVertices()) {
            assertTrue(newGraph.containsVertex(vertex));
            for (String neighbor : graph.getNeighbors(vertex)) {
                assertTrue(newGraph.containsEdge(vertex, neighbor));
                assertEquals(graph.getEdge(vertex, neighbor).getWeight(),
                           newGraph.getEdge(vertex, neighbor).getWeight());
            }
        }
    }

    @Test
    void testWriteToFileEmptyGraph(@TempDir Path tempDir) throws IOException {
        File outputFile = tempDir.resolve("empty-graph.txt").toFile();
        graph.writeToFile(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertEquals(0, outputFile.length());
    }

    @Test
    void testComplexTopologicalSort() {
        String[] vertices = {"A", "B", "C", "D", "E", "F", "G"};
        for (String vertex : vertices) {
            graph.addVertex(vertex);
        }

        graph.addEdge("A", "D");
        graph.addEdge("B", "D");
        graph.addEdge("C", "E");
        graph.addEdge("D", "F");
        graph.addEdge("E", "F");
        graph.addEdge("F", "G");

        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNotNull(result);
        assertEquals(7, result.size());

        assertTrue(result.indexOf("A") < result.indexOf("D"));
        assertTrue(result.indexOf("B") < result.indexOf("D"));
        assertTrue(result.indexOf("C") < result.indexOf("E"));
        assertTrue(result.indexOf("D") < result.indexOf("F"));
        assertTrue(result.indexOf("E") < result.indexOf("F"));
        assertTrue(result.indexOf("F") < result.indexOf("G"));
    }

    @Test
    void testTopologicalSortSelfLoop() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.addEdge("A", "A");

        List<String> result = graph.topologicalSort(new TopologicalSorter<>());
        assertNull(result);
    }

    private static class TestGraph extends AbstractGraph<String> {
        private Map<String, Set<String>> adjacencyList;
        private Map<String, Map<String, Double>> weights;

        public TestGraph() {
            super();
            this.adjacencyList = new HashMap<>();
            this.weights = new HashMap<>();
        }

        @Override
        public boolean addVertex(String vertex) {
            if (super.addVertex(vertex)) {
                adjacencyList.put(vertex, new HashSet<>());
                weights.put(vertex, new HashMap<>());
                return true;
            }
            return false;
        }

        @Override
        public boolean removeVertex(String vertex) {
            if (!containsVertex(vertex)) {
                return false;
            }

            for (String neighbor : adjacencyList.get(vertex)) {
                edgeCount--;
            }

            for (String otherVertex : adjacencyList.keySet()) {
                if (adjacencyList.get(otherVertex).remove(vertex)) {
                    weights.get(otherVertex).remove(vertex);
                    edgeCount--;
                }
            }

            adjacencyList.remove(vertex);
            weights.remove(vertex);
            vertexToIndex.remove(vertex);

            int removedIndex = indexToVertex.indexOf(vertex);
            indexToVertex.remove(vertex);

            for (int i = removedIndex; i < indexToVertex.size(); i++) {
                vertexToIndex.put(indexToVertex.get(i), i);
            }

            return true;
        }

        @Override
        public boolean addEdge(String start, String end) {
            return addEdge(start, end, 0.0);
        }

        @Override
        public boolean addEdge(String start, String end, double weight) {
            if (!containsVertex(start) || !containsVertex(end)) {
                return false;
            }

            if (adjacencyList.get(start).add(end)) {
                weights.get(start).put(end, weight);
                edgeCount++;
                return true;
            }
            return false;
        }

        @Override
        public boolean removeEdge(String start, String end) {
            if (!containsVertex(start) || !containsVertex(end)) {
                return false;
            }

            if (adjacencyList.get(start).remove(end)) {
                weights.get(start).remove(end);
                edgeCount--;
                return true;
            }
            return false;
        }

        @Override
        public boolean containsEdge(String start, String end) {
            if (!containsVertex(start) || !containsVertex(end)) {
                return false;
            }
            return adjacencyList.get(start).contains(end);
        }

        @Override
        public Edge<String> getEdge(String start, String end) {
            if (!containsEdge(start, end)) {
                return null;
            }
            double weight = weights.get(start).get(end);
            return new Edge<>(start, end, weight);
        }

        @Override
        public List<String> getNeighbors(String vertex) {
            if (!containsVertex(vertex)) {
                return Collections.emptyList();
            }
            return new ArrayList<>(adjacencyList.get(vertex));
        }
    }
}
