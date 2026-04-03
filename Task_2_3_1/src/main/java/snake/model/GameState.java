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
    public IntegerProperty currentLengthProperty() { return currentLength; }
    public IntegerProperty targetLengthProperty() { return targetLength; }
    public IntegerProperty levelProperty() { return level; }
    public ObjectProperty<GameStatus> statusProperty() { return status; }
}
