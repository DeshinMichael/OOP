package snake.model.controller;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.SimpleFood;
import snake.model.entity.Snake;
import snake.model.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class BotControllerTest {
    @Test
    public void testGetNextDirectionTowardsFood() {
        GameState state = new GameState();
        Snake bot = new Snake(new Cell(5, 5), Direction.UP, true);
        state.getSnakes().add(bot);

        state.getFoods().add(new SimpleFood(new Cell(5, 2)));

        BotController controller = new BotController();
        Direction nextDir = controller.getNextDirection(bot, state);

        assertEquals(Direction.UP, nextDir);
    }

    @Test
    public void testGetNextDirectionAvoidsWall() {
        GameState state = new GameState();

        Snake bot = new Snake(new Cell(0, 0), Direction.UP, true);
        state.getSnakes().add(bot);

        state.getFoods().add(new SimpleFood(new Cell(5, 0)));

        BotController controller = new BotController();
        Direction nextDir = controller.getNextDirection(bot, state);

        assertEquals(Direction.RIGHT, nextDir);
    }
}

