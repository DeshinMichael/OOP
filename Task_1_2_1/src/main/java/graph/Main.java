package graph;

import graph.exceptions.FileException;
import graph.exceptions.VertexException;
import graph.impl.AdjacencyListGraph;
import graph.impl.AdjacencyMatrixGraph;
import graph.impl.IncidenceMatrixGraph;
import graph.model.Vertex;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Vertex<String> vertexA = new Vertex<>("A");
            Vertex<String> vertexB = new Vertex<>("B");
            Vertex<String> vertexC = new Vertex<>("C");
            Vertex<String> vertexD = new Vertex<>("D");

            System.out.println("=== Adjacency List Graph ===");
            AdjacencyListGraph listGraph = new AdjacencyListGraph();
            listGraph.addVertex(vertexA);
            listGraph.addVertex(vertexB);
            listGraph.addVertex(vertexC);
            listGraph.addVertex(vertexD);

            listGraph.addEdge(vertexA, vertexB, 2.5);
            listGraph.addEdge(vertexB, vertexC, 1.8);
            listGraph.addEdge(vertexA, vertexD, 3.2);
            listGraph.addEdge(vertexC, vertexD, 0.9);

            System.out.println(listGraph);
            listGraph.writeToFile("adjacency_list_output.txt");
            System.out.println("Graph written to file: adjacency_list_output.txt\n");

            System.out.println("=== Adjacency Matrix Graph ===");
            AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph();
            matrixGraph.addVertex(vertexA);
            matrixGraph.addVertex(vertexB);
            matrixGraph.addVertex(vertexC);

            matrixGraph.addEdge(vertexA, vertexB, 4.0);
            matrixGraph.addEdge(vertexB, vertexC, 2.0);
            matrixGraph.addEdge(vertexA, vertexC, 1.5);

            System.out.println(matrixGraph);
            matrixGraph.writeToFile("adjacency_matrix_output.txt");
            System.out.println("Graph written to file: adjacency_matrix_output.txt\n");

            System.out.println("=== Incidence Matrix Graph ===");
            IncidenceMatrixGraph incidenceGraph = new IncidenceMatrixGraph();
            incidenceGraph.addVertex(vertexA);
            incidenceGraph.addVertex(vertexB);
            incidenceGraph.addVertex(vertexC);
            incidenceGraph.addVertex(vertexD);

            incidenceGraph.addEdge(vertexA, vertexB, 1.0);
            incidenceGraph.addEdge(vertexB, vertexD, 2.5);
            incidenceGraph.addEdge(vertexC, vertexA, 3.0);

            System.out.println(incidenceGraph);
            incidenceGraph.writeToFile("incidence_matrix_output.txt");
            System.out.println("Graph written to file: incidence_matrix_output.txt\n");

            System.out.println("=== Reading graph from file ===");
            AdjacencyListGraph readGraph = new AdjacencyListGraph();
            readGraph.readFromFile("adjacency_list_output.txt");
            System.out.println("Graph read from file adjacency_list_output.txt:");
            System.out.println(readGraph);

        } catch (VertexException | IOException | FileException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
