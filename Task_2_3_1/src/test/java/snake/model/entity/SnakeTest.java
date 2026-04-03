package snake.model.entity;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {
    @Test
    public void testSetDirectionValid() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getNextDirection());
    }

    @Test
    public void testSetDirectionInvalidOpposite() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getNextDirection());
    }

    @Test
    public void testMove() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.move();
        assertEquals(new Cell(6, 5), snake.getHead());
        assertEquals(1, snake.getBody().size());
    }

    @Test
    public void testGrow() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.grow();
        assertEquals(new Cell(6, 5), snake.getHead());
        assertEquals(2, snake.getBody().size());
        assertEquals(new Cell(5, 5), snake.getBody().get(1));
    }

    @Test
    public void testPendingGrowth() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.addGrowth(2);

        snake.move();
        assertEquals(2, snake.getBody().size());

        snake.move();
        assertEquals(3, snake.getBody().size());

        snake.move();
        assertEquals(3, snake.getBody().size());
    }

    @Test
    public void testSelfCollision() {
        Snake snake = new Snake(new Cell(5, 5), Direction.RIGHT, false);
        snake.addGrowth(4);
        snake.move();
        snake.setDirection(Direction.DOWN);
        snake.move();
        snake.setDirection(Direction.LEFT);
        snake.move();

        assertTrue(snake.isSelfCollision(new Cell(6, 5)));
        assertFalse(snake.isSelfCollision(new Cell(10, 10)));
    }
}

