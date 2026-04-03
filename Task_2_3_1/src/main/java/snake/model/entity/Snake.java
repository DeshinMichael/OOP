package snake.model.entity;

import snake.model.Cell;
import snake.model.Direction;

import java.util.LinkedList;
import java.util.List;

public final class Snake {
    private final LinkedList<Cell> body = new LinkedList<>();
    private Direction direction;
    private Direction nextDirection;
    private boolean isBot;
    private boolean isDead = false;
    private int pendingGrowth = 0;

    public Snake(Cell startCell, Direction startDirection, boolean isBot) {
        this.body.add(startCell);
        this.direction = startDirection;
        this.nextDirection = startDirection;
        this.isBot = isBot;
    }

    public boolean isBot() { return isBot; }
    public boolean isDead() { return isDead; }
    public void die() { this.isDead = true; }

    public List<Cell> getBody() {
        return body;
    }

    public Cell getHead() {
        return body.getFirst();
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getNextDirection() {
        return nextDirection;
    }

    public void setDirection(Direction newDirection) {
        if (!this.direction.isOpposite(newDirection)) {
            this.nextDirection = newDirection;
        }
    }

    public void addGrowth(int amount) {
        this.pendingGrowth += amount;
    }

    public void move() {
        this.direction = this.nextDirection;
        Cell newHead = getHead().translate(direction);
        body.addFirst(newHead);
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            body.removeLast();
        }
    }

    public void grow() {
        this.direction = this.nextDirection;
        Cell newHead = getHead().translate(direction);
        body.addFirst(newHead);
    }

    public boolean isSelfCollision() {
        return isSelfCollision(getHead());
    }

    public boolean isSelfCollision(Cell next) {
        for (int i = 0; i < body.size() - 1; i++) {
            if (body.get(i).equals(next)) {
                return true;
            }
        }
        return false;
    }
}
