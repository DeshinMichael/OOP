package snake.model.controller;

import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;
public interface SnakeController {
    Direction getNextDirection(Snake snake, GameState state);
}
