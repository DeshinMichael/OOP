package snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import snake.config.AppConfig;
import snake.model.Cell;
import snake.model.entity.Consumable;
import snake.model.entity.Obstacle;
import snake.model.entity.Snake;
import snake.model.entity.SuperFood;

import java.util.ArrayList;
import java.util.List;

public final class BoardRenderer {
    private final Canvas bgCanvas;
    private final Canvas boardCanvas;

    private List<Cell> prevSnakeBody;
    private List<Consumable> prevFoods;
    private boolean forceFullRender = true;

    public BoardRenderer(Canvas bgCanvas, Canvas boardCanvas) {
        this.bgCanvas = bgCanvas;
        this.boardCanvas = boardCanvas;
    }

    public void requestFullRender() {
        this.forceFullRender = true;
    }

    public void render(snake.model.GameState gameState) {
        double w = bgCanvas.getWidth();
        double h = bgCanvas.getHeight();

        if (w <= 0 || h <= 0) return;

        double cellW = w / AppConfig.getInstance().getBoardWidth();
        double cellH = h / AppConfig.getInstance().getBoardHeight();

        GraphicsContext bgGc = bgCanvas.getGraphicsContext2D();
        GraphicsContext fgGc = boardCanvas.getGraphicsContext2D();

        if (forceFullRender || prevSnakeBody == null || prevFoods == null) {
            drawBackgroundAndGrid(bgGc, w, h, cellW, cellH);

            fgGc.clearRect(0, 0, w, h);

            if (gameState != null && !gameState.getSnakes().isEmpty()) {
                drawFoods(fgGc, gameState.getFoods(), cellW, cellH);
                drawObstacles(fgGc, gameState.getObstacles(), cellW, cellH);
                for (Snake snake : gameState.getSnakes()) {
                    drawSnake(fgGc, snake, cellW, cellH);
                }

                prevSnakeBody = new ArrayList<>();
                for (Snake snake : gameState.getSnakes()) {
                    prevSnakeBody.addAll(snake.getBody());
                }
                prevFoods = new ArrayList<>(gameState.getFoods());
            }
            forceFullRender = false;
        } else {
            if (gameState == null || gameState.getSnakes().isEmpty()) return;

            List<Cell> currentSnake = new ArrayList<>();
            for (Snake snake : gameState.getSnakes()) {
                currentSnake.addAll(snake.getBody());
            }

            List<Consumable> currentFoods = new ArrayList<>(gameState.getFoods());

            for (Consumable oldFood : prevFoods) {
                if (!currentFoods.contains(oldFood)) {
                    Cell pos = oldFood.getPosition();
                    fgGc.clearRect(pos.x() * cellW, pos.y() * cellH, cellW, cellH);
                }
            }

            for (Cell oldPart : prevSnakeBody) {
                if (!currentSnake.contains(oldPart)) {
                    fgGc.clearRect(oldPart.x() * cellW, oldPart.y() * cellH, cellW, cellH);
                }
            }

            for (Consumable newFood : currentFoods) {
                if (!prevFoods.contains(newFood)) {
                    drawFood(fgGc, newFood, cellW, cellH);
                }
            }

            for (Snake snake : gameState.getSnakes()) {
                drawSnake(fgGc, snake, cellW, cellH);
            }

            prevSnakeBody = new ArrayList<>(currentSnake);
            prevFoods = new ArrayList<>(currentFoods);
        }
    }

    private void drawBackgroundAndGrid(GraphicsContext gc, double w, double h, double cellW, double cellH) {
        gc.setFill(Color.web("#111827"));
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.web("#374151"));
        gc.setLineWidth(Math.max(0.5, Math.min(1.5, cellW * 0.05)));
        for (int x = 0; x <= AppConfig.getInstance().getBoardWidth(); x++) {
            double px = x * cellW;
            gc.strokeLine(px, 0, px, h);
        }
        for (int y = 0; y <= AppConfig.getInstance().getBoardHeight(); y++) {
            double py = y * cellH;
            gc.strokeLine(0, py, w, py);
        }
    }

    private void drawFoods(GraphicsContext gc, List<Consumable> foods, double cellW, double cellH) {
        for (Consumable food : foods) {
            drawFood(gc, food, cellW, cellH);
        }
    }

    private void drawObstacles(GraphicsContext gc, List<Obstacle> obstacles, double cellW, double cellH) {
        gc.setFill(Color.web("#4b5563"));
        for (Obstacle obs : obstacles) {
            Cell pos = obs.getPosition();
            gc.fillRect(pos.x() * cellW, pos.y() * cellH, cellW, cellH);
        }
    }

    private void drawFood(GraphicsContext gc, Consumable food, double cellW, double cellH) {
        if (food instanceof SuperFood) {
            gc.setFill(Color.web("#ffd700"));
        } else {
            gc.setFill(Color.web("#ef4444"));
        }
        double strokeW = Math.max(0.1, Math.min(1.0, cellW * 0.05));
        double inset = Math.max(strokeW, 0.5);
        Cell pos = food.getPosition();

        double fx = pos.x() * cellW + inset;
        double fy = pos.y() * cellH + inset;
        double w = cellW - inset * 2;
        double h = cellH - inset * 2;

        double padX = w > 10 ? 2 : w * 0.1;
        double padY = h > 10 ? 2 : h * 0.1;
        gc.fillOval(fx + padX, fy + padY, w - padX * 2, h - padY * 2);
    }

    private void drawSnake(GraphicsContext gc, Snake snake, double cellW, double cellH) {
        Cell head = snake.getHead();
        boolean isBot = snake.isBot();
        for (Cell segment : snake.getBody()) {
            drawSnakeSegment(gc, segment, cellW, cellH, segment.equals(head), isBot);
        }
    }

    private void drawSnakeSegment(GraphicsContext gc, Cell segment, double cellW, double cellH, boolean isHead, boolean isBot) {
        if (isBot) {
            gc.setFill(isHead ? Color.web("#7c3aed") : Color.web("#8b5cf6"));
        } else {
            gc.setFill(isHead ? Color.web("#059669") : Color.web("#10b981"));
        }
        double strokeW = Math.max(0.1, Math.min(1.0, cellW * 0.05));
        double inset = Math.max(strokeW, 0.5);

        double sx = segment.x() * cellW + inset;
        double sy = segment.y() * cellH + inset;
        double w = cellW - inset * 2;
        double h = cellH - inset * 2;

        double arcStart = w > 10 ? 6 : w * 0.2;
        gc.fillRoundRect(sx, sy, w, h, arcStart, arcStart);
    }
}
