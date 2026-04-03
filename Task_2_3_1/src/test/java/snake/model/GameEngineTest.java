package snake.model;

import org.junit.jupiter.api.Test;
import snake.model.entity.SimpleFood;
import snake.model.entity.Snake;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    @Test
    public void testResetWithBots() {
        GameState state = new GameState();
        GameEngine engine = new GameEngine(state);

        engine.reset(2);

        assertEquals(GameStatus.READY, state.statusProperty().get());
        assertEquals(3, state.getSnakes().size());
        assertNotNull(engine.getPlayerController());
        assertEquals(1, state.levelProperty().get());
        assertTrue(state.getFoods().size() > 0);
    }

    @Test
    public void testStartAndBasicUpdate() {
        GameState state = new GameState();
        GameEngine engine = new GameEngine(state);

        engine.reset(0);
        engine.start();

        assertEquals(GameStatus.RUNNING, state.statusProperty().get());

        Snake player = state.getSnakes().get(0);
        Cell initialHead = player.getHead();
        engine.update();

        assertNotEquals(initialHead, player.getHead());
    }

    @Test
    public void testUpdatePlayerDies() {
        GameState state = new GameState();
        GameEngine engine = new GameEngine(state);

        engine.reset(0);
        engine.start();

        Snake player = state.getSnakes().get(0);

        player.getBody().clear();
        player.getBody().add(new Cell(snake.config.AppConfig.BOARD_WIDTH - 1, 0));
        player.setDirection(Direction.RIGHT);

        engine.getPlayerController().setNextDirection(Direction.RIGHT);
        engine.update();

        assertEquals(GameStatus.LOST, state.statusProperty().get());
    }

    @Test
    public void testUpdateEatingFood() {
        GameState state = new GameState();
        GameEngine engine = new GameEngine(state);

        engine.reset(0);
        engine.start();

        Snake player = state.getSnakes().get(0);
        Cell head = player.getHead();
        Direction dir = player.getDirection();
        Cell nextCell = head.translate(dir);

        state.getFoods().clear();
        state.getFoods().add(new SimpleFood(nextCell));

        int initialLength = state.currentLengthProperty().get();

        engine.update();

        assertEquals(nextCell, player.getHead());

        assertEquals(initialLength + 1, player.getBody().size());

        assertTrue(state.getFoods().size() > 0);
        assertFalse(state.getFoods().stream().anyMatch(f -> f.getPosition().equals(nextCell) && f instanceof SimpleFood));
    }

    @Test
    public void testUpdatePlayerWins() {
        GameState state = new GameState();
        GameEngine engine = new GameEngine(state);

        engine.reset(0);
        engine.start();

        Snake player = state.getSnakes().get(0);

        state.targetLengthProperty().set(2);

        Cell nextCell = player.getHead().translate(player.getDirection());
        state.getFoods().clear();
        state.getFoods().add(new SimpleFood(nextCell));

        int initialLevel = state.levelProperty().get();

        engine.update();

        assertEquals(2, state.currentLengthProperty().get());

        assertEquals(GameStatus.WON, state.statusProperty().get());
    }
}
