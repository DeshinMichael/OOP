package snake.model.controller;

import java.util.ArrayList;
import java.util.List;
import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;
import snake.model.Cell;

public class BotController implements SnakeController {
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

        if (!state.getFoods().isEmpty()) {
            Cell foodTarget = state.getFoods().get(0).getPosition();
            Direction best = null;
            double minDist = Double.MAX_VALUE;
            for (Direction dir : safeDirections) {
                Cell next = snake.getHead().translate(dir);
                double d = mapDist(next, foodTarget);
                if (d < minDist) {
                    minDist = d;
                    best = dir;
                }
            }
            if (best != null) return best;
        }

        return safeDirections.get(0);
    }

    private double mapDist(Cell a, Cell b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
