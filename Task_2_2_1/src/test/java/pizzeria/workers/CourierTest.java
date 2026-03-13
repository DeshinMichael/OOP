package pizzeria.workers;

import org.junit.jupiter.api.Test;
import pizzeria.core.Order;
import pizzeria.core.OrderState;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourierTest {

    private final PizzeriaLogger noopLogger = msg -> {};

    @Test
    void courierDeliversOrder() throws InterruptedException {
        Storage storage = new Storage(5);
        Order order = new Order(1, null);
        storage.put(order);
        storage.close();

        Courier courier = new Courier(1, 3, 50, storage, noopLogger);
        Thread t = new Thread(courier);
        t.start();
        t.join(2000);

        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void courierSetsInDeliveryThenDelivered() throws InterruptedException {
        Storage storage = new Storage(5);
        List<OrderState> states = new ArrayList<>();
        Order order = new Order(1, (o, s) -> states.add(s));
        storage.put(order);
        storage.close();

        Courier courier = new Courier(1, 3, 50, storage, noopLogger);
        Thread t = new Thread(courier);
        t.start();
        t.join(2000);

        assertTrue(states.contains(OrderState.IN_DELIVERY));
        assertTrue(states.contains(OrderState.DELIVERED));
    }

    @Test
    void courierStopsWhenStorageClosedAndEmpty() throws InterruptedException {
        Storage storage = new Storage(5);
        storage.close();

        Courier courier = new Courier(1, 3, 50, storage, noopLogger);
        Thread t = new Thread(courier);
        t.start();
        t.join(1000);

        assertFalse(t.isAlive());
    }

    @Test
    void courierTakesUpToTrunkSize() throws InterruptedException {
        Storage storage = new Storage(10);
        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Order o = new Order(i, null);
            storage.put(o);
            orders.add(o);
        }
        storage.close();

        Courier courier = new Courier(1, 3, 50, storage, noopLogger);
        Thread t = new Thread(courier);
        t.start();
        t.join(3000);

        long deliveredCount = orders.stream()
                .filter(o -> o.getState() == OrderState.DELIVERED)
                .count();
        assertEquals(5, deliveredCount);
    }
}
