package snake.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import snake.model.entity.Consumable;
import snake.model.entity.Obstacle;
import snake.model.entity.Snake;

import java.util.ArrayList;
import java.util.List;
public final class GameState {
    private final IntegerProperty currentLength = new SimpleIntegerProperty(1);
    private final IntegerProperty targetLength = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final ObjectProperty<GameStatus> status = new SimpleObjectProperty<>(GameStatus.READY);
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Consumable> foods = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    public List<Snake> getSnakes() { return snakes; }
    public List<Consumable> getFoods() { return foods; }
    public List<Obstacle> getObstacles() { return obstacles; }

    public List<Cell> getFreeCells(int width, int height) {
        boolean[][] occupied = new boolean[width][height];

        for (Snake s : getSnakes()) {
            for (Cell c : s.getBody()) {
                if (c.x() >= 0 && c.x() < width && c.y() >= 0 && c.y() < height) {
                    occupied[c.x()][c.y()] = true;
                }
            }
        }
        for (Consumable f : getFoods()) {
            if (f.getPosition().x() >= 0 && f.getPosition().x() < width && f.getPosition().y() >= 0 && f.getPosition().y() < height) {
                occupied[f.getPosition().x()][f.getPosition().y()] = true;
            }
        }
        for (Obstacle o : getObstacles()) {
            if (o.getPosition().x() >= 0 && o.getPosition().x() < width && o.getPosition().y() >= 0 && o.getPosition().y() < height) {
                occupied[o.getPosition().x()][o.getPosition().y()] = true;
            }
        }

        List<Cell> freeCells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!occupied[x][y]) freeCells.add(new Cell(x, y));
            }
        }
        return freeCells;
    }

    public IntegerProperty currentLengthProperty() { return currentLength; }
    public IntegerProperty targetLengthProperty() { return targetLength; }
    public IntegerProperty levelProperty() { return level; }
    public ObjectProperty<GameStatus> statusProperty() { return status; }
}
