package graph.model;

import graph.exceptions.VertexException;

import java.util.Objects;

public class Edge {
    private final Vertex<?> start;
    private final Vertex<?> end;
    private final double weight;

    public Edge(Vertex<?> start, Vertex<?> end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = 0;
    }

    public Edge(Vertex<?> start, Vertex<?> end, double weight) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Vertex<?> getStart() {
        return start;
    }

    public Vertex<?> getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Double.compare(weight, edge.weight) == 0 &&
                Objects.equals(start, edge.start) &&
                Objects.equals(end, edge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, weight);
    }
}
