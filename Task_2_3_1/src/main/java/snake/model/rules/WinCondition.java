package snake.model.rules;

import snake.model.GameState;
import snake.model.entity.Snake;

public interface WinCondition {
    boolean isSatisfied(Snake snake, GameState state);
}

