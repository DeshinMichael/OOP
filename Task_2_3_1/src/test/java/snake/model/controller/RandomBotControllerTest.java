package snake.model.controller;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Snake;
import snake.model.Direction;
import snake.model.entity.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

public class RandomBotControllerTest {
    @Test
    public void testGetNextDirectionAlwaysPickSafe() {
        GameState state = new GameState();
        Snake bot = new Snake(new Cell(0, 0), Direction.RIGHT, true);
        state.getSnakes().add(bot);

        state.getObstacles().add(new Obstacle(new Cell(1, 0)));

        RandomBotController controller = new RandomBotController();
        Direction nextDir = controller.getNextDirection(bot, state);

        assertEquals(Direction.DOWN, nextDir);
    }
}

