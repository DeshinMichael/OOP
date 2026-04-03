package snake.model.rules;

import snake.model.GameState;

public interface LevelManager {
    void onLevelUp(GameState state, int newLevel, int previousLevel);
    void spawnFoods(GameState state);
    void initGame(GameState state, int botCount);
}

