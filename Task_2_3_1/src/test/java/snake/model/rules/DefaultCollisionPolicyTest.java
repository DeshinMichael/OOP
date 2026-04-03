package snake.model.rules;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Snake;
import snake.model.Direction;
import snake.model.entity.Obstacle;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultCollisionPolicyTest {
    @Test
    public void testWallCollision() {
        GameState state = new GameState();
        Snake player = new Snake(new Cell(0, 0), Direction.LEFT, false);
        state.getSnakes().add(player);

        Map<Snake, Cell> planned = new HashMap<>();
        planned.put(player, new Cell(-1, 0)); // Out of bounds

        DefaultCollisionPolicy policy = new DefaultCollisionPolicy();
        CollisionPolicy.CollisionResult result = policy.resolve(state, planned, new HashMap<>());

        assertTrue(result.playerLost());
        assertTrue(result.deadSnakes().contains(player));
    }

    @Test
    public void testObstacleCollision() {
        GameState state = new GameState();
        Snake player = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        state.getSnakes().add(player);
        state.getObstacles().add(new Obstacle(new Cell(6, 5)));

        Map<Snake, Cell> planned = new HashMap<>();
        planned.put(player, new Cell(6, 5));

        DefaultCollisionPolicy policy = new DefaultCollisionPolicy();
        CollisionPolicy.CollisionResult result = policy.resolve(state, planned, new HashMap<>());

        assertTrue(result.playerLost());
        assertTrue(result.deadSnakes().contains(player));
    }

    @Test
    public void testHeadToHeadCollision() {
        GameState state = new GameState();
        Snake p1 = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        Snake p2 = new Snake(new Cell(7, 5), Direction.LEFT, true);
        state.getSnakes().add(p1);
        state.getSnakes().add(p2);

        Map<Snake, Cell> planned = new HashMap<>();
        planned.put(p1, new Cell(6, 5));
        planned.put(p2, new Cell(6, 5));

        DefaultCollisionPolicy policy = new DefaultCollisionPolicy();
        CollisionPolicy.CollisionResult result = policy.resolve(state, planned, new HashMap<>());

        assertTrue(result.playerLost());
        assertTrue(result.deadSnakes().contains(p1));
        assertTrue(result.deadSnakes().contains(p2));
    }
}

