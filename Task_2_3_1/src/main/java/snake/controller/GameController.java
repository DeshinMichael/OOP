package snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import snake.config.AppConfig;
import snake.model.Direction;
import snake.model.GameEngine;
import snake.model.GameState;
import snake.model.GameStatus;
import snake.view.BoardRenderer;

public final class GameController {
    @FXML
    private Canvas bgCanvas;

    @FXML
    private Canvas boardCanvas;

    @FXML
    private StackPane boardContainer;

    @FXML
    private Label lengthLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label statusLabel;

    private final GameState gameState = new GameState();
    private final GameEngine gameEngine = new GameEngine(gameState);
    private BoardRenderer renderer;
    private Timeline gameLoop;

    @FXML
    private void initialize() {
        gameEngine.reset(2);
        boardCanvas.widthProperty().bind(Bindings.min(boardContainer.widthProperty(), boardContainer.heightProperty()).subtract(40));
        boardCanvas.heightProperty().bind(boardCanvas.widthProperty());
        bgCanvas.widthProperty().bind(boardCanvas.widthProperty());
        bgCanvas.heightProperty().bind(boardCanvas.heightProperty());

        renderer = new BoardRenderer(bgCanvas, boardCanvas);
        renderer.render(gameState);

        boardCanvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            renderer.requestFullRender();
            renderer.render(gameState);
        });
        boardCanvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            renderer.requestFullRender();
            renderer.render(gameState);
        });

        lengthLabel.textProperty().bind(gameState.currentLengthProperty().asString());
        levelLabel.textProperty().bind(gameState.levelProperty().asString());
        statusLabel.textProperty().bind(gameState.statusProperty().asString());

        boardCanvas.setFocusTraversable(true);
        boardCanvas.setOnKeyPressed(this::handleKeyPress);

        gameLoop = new Timeline();
        setSpeedForLevel(gameState.levelProperty().get());
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        gameState.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == GameStatus.RUNNING) {
                gameLoop.play();
                boardCanvas.requestFocus();
            } else if (newStatus == GameStatus.WON || newStatus == GameStatus.LOST) {
                gameLoop.stop();
            }
        });

        gameState.levelProperty().addListener((obs, oldLevel, newLevel) -> {
            setSpeedForLevel(newLevel.intValue());
        });

        Platform.runLater(() -> boardCanvas.requestFocus());
    }

    private void setSpeedForLevel(int level) {
        boolean wasRunning = gameLoop.getStatus() == javafx.animation.Animation.Status.RUNNING;
        gameLoop.stop();
        gameLoop.getKeyFrames().clear();

        double newMillis = Math.max(50, AppConfig.TICK_MILLIS - (level - 1) * 10);
        gameLoop.getKeyFrames().add(new KeyFrame(Duration.millis(newMillis), e -> tick()));

        if (wasRunning) {
            gameLoop.play();
        }

        if (renderer != null) {
            renderer.requestFullRender();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (gameEngine.getPlayerController() == null) return;
        switch (event.getCode()) {
            case UP, W -> gameEngine.getPlayerController().setNextDirection(Direction.UP);
            case DOWN, S -> gameEngine.getPlayerController().setNextDirection(Direction.DOWN);
            case LEFT, A -> gameEngine.getPlayerController().setNextDirection(Direction.LEFT);
            case RIGHT, D -> gameEngine.getPlayerController().setNextDirection(Direction.RIGHT);
            case SPACE -> {
                if (gameState.statusProperty().get() == GameStatus.READY) {
                    gameEngine.start();
                } else if (gameState.statusProperty().get() == GameStatus.WON || gameState.statusProperty().get() == GameStatus.LOST) {
                    gameEngine.reset(2);
                    renderer.requestFullRender();
                    renderer.render(gameState);
                }
            }
        }
    }

    private void tick() {
        gameEngine.update();
        renderer.render(gameState);
    }
}
