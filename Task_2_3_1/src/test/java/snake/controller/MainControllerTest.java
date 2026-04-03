package snake.controller;

import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest {

    @Test
    public void testInitialize() throws Exception {
        MainController controller = new MainController();

        Field f = MainController.class.getDeclaredField("gameHost");
        f.setAccessible(true);
        f.set(controller, new StackPane());

        Method initMethod = MainController.class.getDeclaredMethod("initialize");
        initMethod.setAccessible(true);

        assertDoesNotThrow(() -> initMethod.invoke(controller));
    }
}
