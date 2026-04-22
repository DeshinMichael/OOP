package snake.model.entity;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.Direction;
import snake.model.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class SuperFoodTest {
    @Test
    public void testOnConsumed() {
        GameState state = new GameState();
        Snake snake = new Snake(new Cell(0, 0), Direction.RIGHT, false);
        SuperFood food = new SuperFood(new Cell(0, 0));

        food.onConsumed(snake, state);

        snake.move();
        assertEquals(2, snake.getBody().size());
        snake.move();
        assertEquals(3, snake.getBody().size());
    }
}

