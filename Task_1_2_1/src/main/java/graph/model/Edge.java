package graph.model;

import java.util.Objects;

public class Edge<V> {
    private final V start;
    private final V end;
    private final double weight;

    public Edge(V start, V end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = 0;
    }

    public Edge(V start, V end, double weight) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end vertex cannot be null");
        }
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public V getStart() {
        return start;
    }

    public V getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
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
