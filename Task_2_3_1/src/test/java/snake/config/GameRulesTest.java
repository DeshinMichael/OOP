package snake.config;

import org.junit.jupiter.api.Test;
import snake.model.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class GameRulesTest {
    @Test
    public void testSpawnFoods() {
        GameState state = new GameState();
        GameRules rules = new GameRules();

        rules.spawnFoods(state);
        assertEquals(AppConfig.FOOD_COUNT, state.getFoods().size());
    }

    @Test
    public void testLevelUp() {
        GameState state = new GameState();
        GameRules rules = new GameRules();

        rules.onLevelUp(state, 2, 1);

        assertEquals(1, state.getObstacles().size());
    }
}

