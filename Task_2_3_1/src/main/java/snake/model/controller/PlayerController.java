package snake.model.controller;

import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;
public class PlayerController implements SnakeController {
    private Direction nextDir;
    public PlayerController(Direction initial) { this.nextDir = initial; }
    public void setNextDirection(Direction dir) { this.nextDir = dir; }
    public Direction getNextDirection(Snake snake, GameState state) {
        Direction current = snake.getDirection();
        if (!current.isOpposite(nextDir)) { return nextDir; }
        return current;
    }
}
