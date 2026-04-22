package snake.model.entity;

import snake.model.Cell;

public class Obstacle implements Entity {
    private final Cell pos;
    public Obstacle(Cell pos) { this.pos = pos; }
    public Cell getPosition() { return pos; }
}
