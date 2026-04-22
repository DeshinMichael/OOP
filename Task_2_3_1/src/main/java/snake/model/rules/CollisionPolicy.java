package snake.model.rules;

import java.util.List;
import java.util.Map;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Snake;

public interface CollisionPolicy {
    CollisionResult resolve(GameState state, Map<Snake, Cell> plannedHeads, Map<Snake, Boolean> willGrow);

    record CollisionResult(List<Snake> deadSnakes, boolean playerLost) {
    }
}
