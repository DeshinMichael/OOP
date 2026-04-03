package snake.model.rules;

import org.junit.jupiter.api.Test;
import snake.model.Cell;
import snake.model.Direction;
import snake.model.GameState;
import snake.model.entity.Snake;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TargetLengthWinConditionTest {

    @Test
    void testWinConditionSatisfied() {
        TargetLengthWinCondition condition = new TargetLengthWinCondition();
        GameState state = new GameState();
        state.targetLengthProperty().set(5);

        Snake snake = new Snake(new Cell(0, 0), Direction.RIGHT, false);

        state.currentLengthProperty().set(4);
        assertFalse(condition.isSatisfied(snake, state), "Should be false when current length < target length");

        state.currentLengthProperty().set(5);
        assertTrue(condition.isSatisfied(snake, state), "Should be true when current length == target length");

        state.currentLengthProperty().set(6);
        assertTrue(condition.isSatisfied(snake, state), "Should be true when current length > target length");
    }

    @Test
    void testWinConditionNotSatisfiedForBot() {
        TargetLengthWinCondition condition = new TargetLengthWinCondition();
        GameState state = new GameState();
        state.targetLengthProperty().set(5);
        state.currentLengthProperty().set(5);

        Snake botSnake = new Snake(new Cell(0, 0), Direction.RIGHT, true);
        assertFalse(condition.isSatisfied(botSnake, state), "Should be false for bot snakes");
    }
}
