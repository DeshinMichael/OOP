package snake.model.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import snake.config.AppConfig;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Obstacle;
import snake.model.entity.Snake;

public final class DefaultCollisionPolicy implements CollisionPolicy {

    @Override
    public CollisionResult resolve(GameState state, Map<Snake, Cell> plannedHeads, Map<Snake, Boolean> willGrow) {
        List<Snake> dead = new ArrayList<>();
        Set<Snake> deadSet = new HashSet<>();
        boolean playerLost = false;

        for (Map.Entry<Snake, Cell> e : plannedHeads.entrySet()) {
            Snake snake = e.getKey();
            Cell head = e.getValue();

            if (head.x() < 0 || head.x() >= AppConfig.getInstance().getBoardWidth() || head.y() < 0 || head.y() >= AppConfig.getInstance().getBoardHeight()) {
                deadSet.add(snake);
                if (!snake.isBot()) {
                    playerLost = true;
                }
                continue;
            }

            for (Obstacle obs : state.getObstacles()) {
                if (obs.getPosition().equals(head)) {
                    deadSet.add(snake);
                    if (!snake.isBot()) {
                        playerLost = true;
                    }
                    break;
                }
            }
        }

        for (Map.Entry<Snake, Cell> e : plannedHeads.entrySet()) {
            Snake snake = e.getKey();
            if (deadSet.contains(snake)) {
                continue;
            }

            Cell head = e.getValue();
            for (Snake other : state.getSnakes()) {
                List<Cell> body = other.getBody();
                int occupied = body.size();
                if (!willGrow.getOrDefault(other, false) && occupied > 0) {
                    occupied -= 1;
                }

                for (int i = 0; i < occupied; i++) {
                    if (body.get(i).equals(head)) {
                        deadSet.add(snake);
                        if (!snake.isBot()) {
                            playerLost = true;
                        }
                        break;
                    }
                }

                if (deadSet.contains(snake)) {
                    break;
                }
            }
        }

        for (Map.Entry<Snake, Cell> a : plannedHeads.entrySet()) {
            for (Map.Entry<Snake, Cell> b : plannedHeads.entrySet()) {
                if (a.getKey() == b.getKey()) {
                    continue;
                }
                if (a.getValue().equals(b.getValue())) {
                    deadSet.add(a.getKey());
                    deadSet.add(b.getKey());
                    if (!a.getKey().isBot() || !b.getKey().isBot()) {
                        playerLost = true;
                    }
                }
            }
        }

        dead.addAll(deadSet);
        return new CollisionResult(dead, playerLost);
    }
}

