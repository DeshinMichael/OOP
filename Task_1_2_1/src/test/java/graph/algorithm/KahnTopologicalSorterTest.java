package graph.algorithm;

import graph.api.Graph;
import graph.exceptions.SortException;
import graph.exceptions.VertexException;
import graph.impl.AdjacencyListGraph;
import graph.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KahnTopologicalSorterTest {
    private KahnTopologicalSorter sorter;
    private Graph graph;
    private Vertex<String> vertexA;
    private Vertex<String> vertexB;
    private Vertex<String> vertexC;
    private Vertex<String> vertexD;

    @BeforeEach
    void setUp() {
        sorter = new KahnTopologicalSorter();
        graph = new AdjacencyListGraph();
        vertexA = new Vertex<>("A");
        vertexB = new Vertex<>("B");
        vertexC = new Vertex<>("C");
        vertexD = new Vertex<>("D");
    }

    @Test
    void testSortNullGraph() {
        assertThrows(SortException.class, () -> sorter.sort(null));
    }

    @Test
    void testSortEmptyGraph() throws SortException, VertexException {
        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSortSingleVertex() throws VertexException, SortException {
        graph.addVertex(vertexA);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vertexA, result.get(0));
    }

    @Test
    void testSortTwoVerticesWithEdge() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addEdge(vertexA, vertexB);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(vertexA, result.get(0));
        assertEquals(vertexB, result.get(1));
    }

    @Test
    void testSortLinearChain() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexB, vertexC);
        graph.addEdge(vertexC, vertexD);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(vertexA, result.get(0));
        assertEquals(vertexB, result.get(1));
        assertEquals(vertexC, result.get(2));
        assertEquals(vertexD, result.get(3));
    }

    @Test
    void testSortDAG() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexA, vertexC);
        graph.addEdge(vertexB, vertexD);
        graph.addEdge(vertexC, vertexD);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(vertexA, result.get(0));
        assertEquals(vertexD, result.get(3));

        int indexA = result.indexOf(vertexA);
        int indexB = result.indexOf(vertexB);
        int indexC = result.indexOf(vertexC);
        int indexD = result.indexOf(vertexD);

        assertTrue(indexA < indexB);
        assertTrue(indexA < indexC);
        assertTrue(indexB < indexD);
        assertTrue(indexC < indexD);
    }

    @Test
    void testSortWithCycle() throws VertexException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexB, vertexC);
        graph.addEdge(vertexC, vertexA);

        assertThrows(SortException.class, () -> sorter.sort(graph));
    }

    @Test
    void testSortWithSelfLoop() throws VertexException {
        graph.addVertex(vertexA);
        graph.addEdge(vertexA, vertexA);

        assertThrows(SortException.class, () -> sorter.sort(graph));
    }

    @Test
    void testSortDisconnectedGraph() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexC, vertexD);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(4, result.size());

        int indexA = result.indexOf(vertexA);
        int indexB = result.indexOf(vertexB);
        int indexC = result.indexOf(vertexC);
        int indexD = result.indexOf(vertexD);

        assertTrue(indexA < indexB);
        assertTrue(indexC < indexD);
    }

    @Test
    void testInDegreeCalculation() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexA, vertexC);
        graph.addEdge(vertexB, vertexC);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(vertexA, result.get(0));
        assertEquals(vertexC, result.get(2));
    }

    @Test
    void testSortComplexDAG() throws VertexException, SortException {
        Vertex<String> v1 = new Vertex<>("1");
        Vertex<String> v2 = new Vertex<>("2");
        Vertex<String> v3 = new Vertex<>("3");
        Vertex<String> v4 = new Vertex<>("4");
        Vertex<String> v5 = new Vertex<>("5");

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v3, v4);
        graph.addEdge(v4, v5);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(5, result.size());

        assertTrue(result.indexOf(v1) < result.indexOf(v2));
        assertTrue(result.indexOf(v1) < result.indexOf(v3));
        assertTrue(result.indexOf(v2) < result.indexOf(v4));
        assertTrue(result.indexOf(v3) < result.indexOf(v4));
        assertTrue(result.indexOf(v4) < result.indexOf(v5));
    }

    @Test
    void testSortWithMultipleZeroInDegreeNodes() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);
        graph.addEdge(vertexA, vertexC);
        graph.addEdge(vertexB, vertexD);

        List<Vertex<?>> result = sorter.sort(graph);
        assertNotNull(result);
        assertEquals(4, result.size());

        assertTrue(result.indexOf(vertexA) < result.indexOf(vertexC));
        assertTrue(result.indexOf(vertexB) < result.indexOf(vertexD));
    }

    @Test
    void testQueueOperations() throws VertexException, SortException {
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);

        List<Vertex<?>> result = sorter.sort(graph);
        assertEquals(3, result.size());
    }
}

