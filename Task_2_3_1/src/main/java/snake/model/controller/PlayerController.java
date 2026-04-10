package snake.model.controller;

import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerController implements SnakeController {
    private final Queue<Direction> inputQueue = new LinkedList<>();
    private Direction lastQueuedDir;

    public PlayerController(Direction initial) {
        this.lastQueuedDir = initial;
    }

    public void setNextDirection(Direction dir) {
        if (inputQueue.size() < 3 && dir != lastQueuedDir && !lastQueuedDir.isOpposite(dir)) {
            inputQueue.add(dir);
            lastQueuedDir = dir;
        }
    }

    @Override
    public Direction getNextDirection(Snake snake, GameState state) {
        if (!inputQueue.isEmpty()) {
            return inputQueue.poll();
        }
        Direction current = snake.getDirection();
        lastQueuedDir = current;
        return current;
    }
}
