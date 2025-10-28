package graph.model;

import graph.exceptions.VertexException;

import java.util.Objects;

public class Edge {
    private final Object start;
    private final Object end;
    private final double weight;

    public Edge(Object start, Object end) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = 0;
    }

    public Edge(Object start, Object end, double weight) throws VertexException {
        if (start == null || end == null) {
            throw new VertexException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Object getStart() {
        return start;
    }

    public Object getEnd() {
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
        return Objects.equals(start, edge.start) && Objects.equals(end, edge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return start + " -> " + end + (weight != 0 ? " (" + weight + ")" : "");
    }
}
