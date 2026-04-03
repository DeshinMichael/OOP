package snake.model.rules;

import snake.model.GameState;
import snake.model.entity.Snake;

public final class TargetLengthWinCondition implements WinCondition {
    @Override
    public boolean isSatisfied(Snake snake, GameState state) {
        if (snake == null || snake.isBot()) {
            return false;
        }
        return state.currentLengthProperty().get() >= state.targetLengthProperty().get();
    }
}

