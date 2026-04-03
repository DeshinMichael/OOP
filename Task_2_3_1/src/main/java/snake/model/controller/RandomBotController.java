package snake.model.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import snake.model.Cell;
import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;

public class RandomBotController implements SnakeController {
    private final Random random = new Random();

    @Override
    public Direction getNextDirection(Snake snake, GameState state) {
        Direction current = snake.getDirection();
        List<Direction> safeDirections = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            if (dir.isOpposite(current)) continue;
            Cell next = snake.getHead().translate(dir);
            if (MoveSafetyUtil.isSafe(next, state, snake)) {
                safeDirections.add(dir);
            }
        }

        if (safeDirections.isEmpty()) return current;

        return safeDirections.get(random.nextInt(safeDirections.size()));
    }
}
