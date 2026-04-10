package snake.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import snake.config.AppConfig;
import snake.config.GameRules;
import snake.model.controller.BotController;
import snake.model.controller.PlayerController;
import snake.model.controller.RandomBotController;
import snake.model.controller.SnakeController;
import snake.model.entity.Consumable;
import snake.model.entity.Obstacle;
import snake.model.entity.Snake;
import snake.model.rules.CollisionPolicy;
import snake.model.rules.DefaultCollisionPolicy;
import snake.model.rules.LevelManager;
import snake.model.rules.TargetLengthWinCondition;
import snake.model.rules.WinCondition;

public class GameEngine {
    private final GameState state;
    private final Map<Snake, SnakeController> controllers = new HashMap<>();
    private final Random random = new Random();
    private final CollisionPolicy collisionPolicy;
    private final WinCondition winCondition;
    private final LevelManager gameRules;
    private PlayerController playerController;

    public GameEngine(GameState state) {
        this(state, new DefaultCollisionPolicy(), new TargetLengthWinCondition(), new GameRules());
    }

    public GameEngine(GameState state, CollisionPolicy collisionPolicy, WinCondition winCondition, LevelManager gameRules) {
        this.state = state;
        this.collisionPolicy = collisionPolicy;
        this.winCondition = winCondition;
        this.gameRules = gameRules;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void start() {
        state.statusProperty().set(GameStatus.RUNNING);
    }

    public void reset(int botCount) {
        state.getSnakes().clear();
        state.getFoods().clear();
        state.getObstacles().clear();
        controllers.clear();
        state.currentLengthProperty().set(1);
        state.targetLengthProperty().set(AppConfig.getInstance().getTargetLength());
        state.levelProperty().set(1);
        state.statusProperty().set(GameStatus.READY);

        Snake mainSnake = new Snake(new Cell(AppConfig.getInstance().getBoardWidth() / 2, AppConfig.getInstance().getBoardHeight() / 2), Direction.RIGHT, false);
        state.getSnakes().add(mainSnake);
        playerController = new PlayerController(Direction.RIGHT);
        controllers.put(mainSnake, playerController);

        for (int i = 0; i < botCount; i++) {
            Cell pos;
            do {
                pos = new Cell(random.nextInt(AppConfig.getInstance().getBoardWidth()), random.nextInt(AppConfig.getInstance().getBoardHeight()));
            } while (isOccupied(pos));
            Snake bot = new Snake(pos, Direction.UP, true);
            state.getSnakes().add(bot);
            if (random.nextBoolean()) {
                controllers.put(bot, new BotController());
            } else {
                controllers.put(bot, new RandomBotController());
            }
        }

        gameRules.spawnFoods(state);
    }

    public void update() {
        if (state.statusProperty().get() != GameStatus.RUNNING) {
            return;
        }

        List<Snake> snakes = new ArrayList<>(state.getSnakes());
        if (snakes.isEmpty()) {
            return;
        }

        Map<Snake, Cell> plannedHeads = new LinkedHashMap<>();
        Map<Snake, Boolean> willGrow = new HashMap<>();
        Map<Snake, Consumable> eatenBySnake = new HashMap<>();

        for (Snake snake : snakes) {
            SnakeController controller = controllers.get(snake);
            if (controller == null) {
                continue;
            }

            Direction dir = controller.getNextDirection(snake, state);
            snake.setDirection(dir);
            Cell next = snake.getHead().translate(snake.getNextDirection());
            plannedHeads.put(snake, next);

            Consumable eaten = findFoodAt(next);
            if (eaten != null) {
                willGrow.put(snake, true);
                eatenBySnake.put(snake, eaten);
            } else {
                willGrow.put(snake, false);
            }
        }

        CollisionPolicy.CollisionResult collisionResult = collisionPolicy.resolve(state, plannedHeads, willGrow);
        if (collisionResult.playerLost()) {
            state.statusProperty().set(GameStatus.LOST);
            return;
        }

        for (Snake dead : collisionResult.deadSnakes()) {
            dead.die();
        }

        for (Snake snake : snakes) {
            if (!plannedHeads.containsKey(snake) || snake.isDead()) {
                continue;
            }

            if (willGrow.getOrDefault(snake, false)) {
                snake.grow();
                Consumable eaten = eatenBySnake.get(snake);
                if (eaten != null) {
                    eaten.onConsumed(snake, state);
                    state.getFoods().remove(eaten);
                }
            } else {
                snake.move();
            }
        }

        state.getSnakes().removeIf(Snake::isDead);
        controllers.keySet().removeIf(Snake::isDead);

        Snake playerSnake = getPlayerSnake();
        if (playerSnake == null) {
            state.statusProperty().set(GameStatus.LOST);
            return;
        }

        state.currentLengthProperty().set(playerSnake.getBody().size());
        int levelFromLength = 1 + Math.max(0, (state.currentLengthProperty().get() - 1) / 5);
        int previousLevel = state.levelProperty().get();
        if (levelFromLength > previousLevel) {
            state.levelProperty().set(levelFromLength);
            gameRules.onLevelUp(state, levelFromLength, previousLevel);
        }

        if (winCondition.isSatisfied(playerSnake, state)) {
            state.statusProperty().set(GameStatus.WON);
            return;
        }

        gameRules.spawnFoods(state);
    }

    private Consumable findFoodAt(Cell cell) {
        for (Consumable food : state.getFoods()) {
            if (food.getPosition().equals(cell)) {
                return food;
            }
        }
        return null;
    }

    private boolean isOccupied(Cell cell) {
        for (Snake s : state.getSnakes()) {
            if (s.getBody().contains(cell)) {
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

    private Snake getPlayerSnake() {
        for (Snake snake : state.getSnakes()) {
            if (!snake.isBot()) {
                return snake;
            }
        }
        return null;
    }
}
