package snake.model.controller;

import java.util.List;
import snake.config.AppConfig;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Obstacle;
import snake.model.entity.Snake;

final class MoveSafetyUtil {
    private MoveSafetyUtil() {
    }

    static boolean isSafe(Cell cell, GameState state, Snake self) {
        if (cell.x() < 0 || cell.x() >= AppConfig.getInstance().getBoardWidth() || cell.y() < 0 || cell.y() >= AppConfig.getInstance().getBoardHeight()) {
            return false;
        }

        for (Obstacle obs : state.getObstacles()) {
            if (obs.getPosition().equals(cell)) {
                return false;
            }
        }

        for (Snake other : state.getSnakes()) {
            List<Cell> body = other.getBody();
            if (other == self) {
                for (int i = 0; i < Math.max(0, body.size() - 1); i++) {
                    if (body.get(i).equals(cell)) {
                        return false;
                    }
                }
            } else {
                for (Cell part : body) {
                    if (part.equals(cell)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}

