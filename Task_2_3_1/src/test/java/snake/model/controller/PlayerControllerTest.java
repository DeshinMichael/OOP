package snake.model.controller;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Snake;
import snake.model.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControllerTest {
    @Test
    public void testGetNextDirectionValid() {
        PlayerController ctrl = new PlayerController(Direction.RIGHT);
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        GameState state = new GameState();

        ctrl.setNextDirection(Direction.UP);
        assertEquals(Direction.UP, ctrl.getNextDirection(snake, state));
    }

    @Test
    public void testGetNextDirectionInvalidOpposite() {
        PlayerController ctrl = new PlayerController(Direction.RIGHT);
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        GameState state = new GameState();

        ctrl.setNextDirection(Direction.LEFT);

        assertEquals(Direction.RIGHT, ctrl.getNextDirection(snake, state));
    }
}

