package snake.model.entity;

import snake.model.Cell;
import snake.model.GameState;

public class SuperFood implements Consumable {
    private final Cell pos;

    public SuperFood(Cell pos) {
        this.pos = pos;
    }

    @Override
    public Cell getPosition() {
        return pos;
    }

    @Override
    public void onConsumed(Snake snake, GameState state) {
        snake.addGrowth(2);
    }
}

