package snake.model.entity;

import snake.model.GameState;

public interface Consumable extends Entity {
    void onConsumed(Snake snake, GameState state);
}
