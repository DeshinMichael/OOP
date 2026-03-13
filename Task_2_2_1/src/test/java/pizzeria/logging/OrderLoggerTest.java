package pizzeria.logging;

import org.junit.jupiter.api.Test;
import pizzeria.core.Order;
import pizzeria.core.OrderState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderLoggerTest {

    @Test
    void onStateChangedLogsCorrectFormat() {
        List<String> logged = new ArrayList<>();
        PizzeriaLogger mockLogger = logged::add;

        OrderLogger orderLogger = new OrderLogger(mockLogger);
        Order order = new Order(5, orderLogger);
        order.setState(OrderState.BEING_COOKED);

        assertEquals(1, logged.size());
        assertEquals("[5] BEING_COOKED", logged.get(0));
    }

    @Test
    void onStateChangedLogsEachStateChange() {
        List<String> logged = new ArrayList<>();
        PizzeriaLogger mockLogger = logged::add;

        OrderLogger orderLogger = new OrderLogger(mockLogger);
        Order order = new Order(3, orderLogger);

        order.setState(OrderState.BEING_COOKED);
        order.setState(OrderState.COOKED);
        order.setState(OrderState.IN_STORAGE);

        assertEquals(3, logged.size());
        assertEquals("[3] BEING_COOKED", logged.get(0));
        assertEquals("[3] COOKED", logged.get(1));
        assertEquals("[3] IN_STORAGE", logged.get(2));
    }
}
