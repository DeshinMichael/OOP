package snake.model.entity;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.Direction;
import snake.model.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleFoodTest {
    @Test
    public void testOnConsumed() {
        GameState state = new GameState();
        Snake snake = new Snake(new Cell(0, 0), Direction.RIGHT, false);
        SimpleFood food = new SimpleFood(new Cell(0, 0));

        int initialPendingGrowth = 0;

        food.onConsumed(snake, state);

        assertEquals(new Cell(0, 0), food.getPosition());
    }
}

