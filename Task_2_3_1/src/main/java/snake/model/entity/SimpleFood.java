package snake.model.entity;

import snake.model.Cell;
import snake.model.GameState;

public class SimpleFood implements Consumable {
    private final Cell pos;
    public SimpleFood(Cell pos) { this.pos = pos; }
    public Cell getPosition() { return pos; }
    public void onConsumed(Snake snake, GameState state) { }
}
