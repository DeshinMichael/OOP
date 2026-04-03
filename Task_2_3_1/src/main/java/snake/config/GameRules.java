package snake.config;

import java.util.Random;
import snake.model.Cell;
import snake.model.entity.Consumable;
import snake.model.GameState;
import snake.model.entity.Obstacle;
import snake.model.entity.SimpleFood;
import snake.model.entity.SuperFood;
import snake.model.entity.Snake;
import snake.model.rules.LevelManager;

public class GameRules implements LevelManager {
    private final Random random = new Random();

    @Override
    public void onLevelUp(GameState state, int newLevel, int previousLevel) {
        int count = newLevel - previousLevel;
        for (int i = 0; i < count; i++) {
            Cell pos;
            do {
                pos = new Cell(random.nextInt(AppConfig.BOARD_WIDTH), random.nextInt(AppConfig.BOARD_HEIGHT));
            } while (isOccupied(pos, state));
            state.getObstacles().add(new Obstacle(pos));
        }
    }

    @Override
    public void spawnFoods(GameState state) {
        while (state.getFoods().size() < AppConfig.FOOD_COUNT) {
            Cell pos = new Cell(random.nextInt(AppConfig.BOARD_WIDTH), random.nextInt(AppConfig.BOARD_HEIGHT));
            if (isOccupied(pos, state)) {
                continue;
            }
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

    private boolean isOccupied(Cell cell, GameState state) {
        for (Snake s : state.getSnakes()) {
            if (s.getBody().contains(cell)) {
                return true;
            }
        }
        for (Consumable f : state.getFoods()) {
            if (f.getPosition().equals(cell)) {
                return true;
            }
        }
        for (Obstacle o : state.getObstacles()) {
            if (o.getPosition().equals(cell)) {
                return true;
            }
        }
        return false;
    }
}
