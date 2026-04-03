package snake.controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import snake.model.GameEngine;
import snake.model.GameState;
import snake.model.GameStatus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @BeforeAll
    public static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await();
        } catch (IllegalStateException | UnsupportedOperationException e) {

        }
    }

    @Test
    public void testControllerLogic() throws Exception {
        GameController controller = new GameController();

        setField(controller, "bgCanvas", new Canvas());
        setField(controller, "boardCanvas", new Canvas());
        setField(controller, "boardContainer", new StackPane());
        setField(controller, "lengthLabel", new Label());
        setField(controller, "levelLabel", new Label());
        setField(controller, "statusLabel", new Label());

        CountDownLatch initLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                Method initMethod = GameController.class.getDeclaredMethod("initialize");
                initMethod.setAccessible(true);
                initMethod.invoke(controller);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                initLatch.countDown();
            }
        });
        initLatch.await();

        GameState state = (GameState) getField(controller, "gameState");
        GameEngine engine = (GameEngine) getField(controller, "gameEngine");

        Method handleKeyPress = GameController.class.getDeclaredMethod("handleKeyPress", KeyEvent.class);
        handleKeyPress.setAccessible(true);

        CountDownLatch latch1 = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                KeyEvent evt = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false);
                handleKeyPress.invoke(controller, evt);
            } catch (Exception e) {} finally { latch1.countDown(); }
        });
        latch1.await();

        CountDownLatch latchSpace = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                KeyEvent evt = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);
                handleKeyPress.invoke(controller, evt);
            } catch (Exception e) {} finally { latchSpace.countDown(); }
        });
        latchSpace.await();

        assertEquals(GameStatus.RUNNING, state.statusProperty().get());

        CountDownLatch latchSpace2 = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                KeyEvent evt = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);
                handleKeyPress.invoke(controller, evt);
            } catch (Exception e) {} finally { latchSpace2.countDown(); }
        });
        latchSpace2.await();

        CountDownLatch tickLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                Method tickMethod = GameController.class.getDeclaredMethod("tick");
                tickMethod.setAccessible(true);
                tickMethod.invoke(controller);
            } catch (Exception e) {} finally { tickLatch.countDown(); }
        });
        tickLatch.await();

        CountDownLatch lostLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                state.statusProperty().set(GameStatus.LOST);
                KeyEvent evt = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);
                handleKeyPress.invoke(controller, evt);
            } catch (Exception e) {} finally { lostLatch.countDown(); }
        });
        lostLatch.await();

        assertEquals(GameStatus.READY, state.statusProperty().get());
    }

    private void setField(Object obj, String name, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, value);
    }

    private Object getField(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(obj);
    }
}

