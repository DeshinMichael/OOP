package snake.model.controller;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Snake;
import snake.model.Direction;
import snake.model.entity.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

public class MoveSafetyUtilTest {
    @Test
    public void testSafeMove() {
        GameState state = new GameState();
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        state.getSnakes().add(snake);

        assertTrue(MoveSafetyUtil.isSafe(new Cell(6, 5), state, snake));
    }

    @Test
    public void testOutOfBoundsMove() {
        GameState state = new GameState();
        Snake snake = new Snake(new Cell(0, 0), Direction.LEFT, false);

        assertFalse(MoveSafetyUtil.isSafe(new Cell(-1, 0), state, snake));
    }

    @Test
    public void testObstacleMove() {
        GameState state = new GameState();
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        state.getObstacles().add(new Obstacle(new Cell(6, 5)));

        assertFalse(MoveSafetyUtil.isSafe(new Cell(6, 5), state, snake));
    }

    @Test
    public void testOtherSnakeBodyMove() {
        GameState state = new GameState();
        Snake snake1 = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        Snake snake2 = new Snake(new Cell(6, 6), Direction.UP, true);
        state.getSnakes().add(snake1);
        state.getSnakes().add(snake2);

        assertFalse(MoveSafetyUtil.isSafe(new Cell(6, 6), state, snake1));
    }
}

