package snake.config;

import java.util.List;
import java.util.Random;
import snake.model.Cell;
import snake.model.GameState;
import snake.model.entity.Obstacle;
import snake.model.entity.SimpleFood;
import snake.model.entity.SuperFood;
import snake.model.rules.LevelManager;

public class GameRules implements LevelManager {
    private final Random random = new Random();

    @Override
    public void onLevelUp(GameState state, int newLevel, int previousLevel) {
        int count = newLevel - previousLevel;
        List<Cell> freeCells = state.getFreeCells(AppConfig.getInstance().getBoardWidth(), AppConfig.getInstance().getBoardHeight());
        for (int i = 0; i < count; i++) {
            if (freeCells.isEmpty()) break;
            int idx = random.nextInt(freeCells.size());
            Cell pos = freeCells.remove(idx);
            state.getObstacles().add(new Obstacle(pos));
        }
    }

    @Override
    public void spawnFoods(GameState state) {
        List<Cell> freeCells = state.getFreeCells(AppConfig.getInstance().getBoardWidth(), AppConfig.getInstance().getBoardHeight());
        while (state.getFoods().size() < AppConfig.getInstance().getFoodCount()) {
            if (freeCells.isEmpty()) break;
            int idx = random.nextInt(freeCells.size());
            Cell pos = freeCells.remove(idx);
            if (random.nextDouble() < 0.2) {
                state.getFoods().add(new SuperFood(pos));
            } else {
                state.getFoods().add(new SimpleFood(pos));
            }
        }
    }

    @Override
    public void initGame(GameState state, int botCount) {
    }
}
