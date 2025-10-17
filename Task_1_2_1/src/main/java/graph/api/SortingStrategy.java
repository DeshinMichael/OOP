package graph.api;

import java.util.List;

public interface SortingStrategy<V> {
    List<V> sort(Graph<V> graph);
}
