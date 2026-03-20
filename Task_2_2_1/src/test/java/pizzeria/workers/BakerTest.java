package pizzeria.workers;

import org.junit.jupiter.api.Test;
import pizzeria.core.Order;
import pizzeria.core.OrderQueue;
import pizzeria.core.OrderState;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BakerTest {

    private final PizzeriaLogger noopLogger = msg -> {};

    @Test
    void bakerCooksSingleOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        Order order = new Order(1, null);
        queue.add(order);
        queue.stopAccepting();

        Baker baker = new Baker(1, 50, queue, storage, noopLogger);
        Thread t = new Thread(baker);
        t.start();
        t.join(2000);

        assertEquals(OrderState.IN_STORAGE, order.getState());
    }

    @Test
    void bakerSetsBeingCookedThenCooked() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        List<OrderState> states = new ArrayList<>();
        Order order = new Order(1, (o, s) -> states.add(s));

        queue.add(order);
        queue.stopAccepting();

        Baker baker = new Baker(1, 50, queue, storage, noopLogger);
        Thread t = new Thread(baker);
        t.start();
        t.join(2000);

        assertTrue(states.contains(OrderState.BEING_COOKED));
        assertTrue(states.contains(OrderState.COOKED));
        assertTrue(states.contains(OrderState.IN_STORAGE));
    }

    @Test
    void bakerStopsWhenQueueEmptyAndClosed() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        queue.stopAccepting();

        Baker baker = new Baker(1, 50, queue, storage, noopLogger);
        Thread t = new Thread(baker);
        t.start();
        t.join(1000);

        assertFalse(t.isAlive());
    }

    @Test
    void bakerStopsWhenStorageClosed() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(1);
        storage.put(new Order(0, null));

        Order order = new Order(1, null);
        queue.add(order);

        Baker baker = new Baker(1, 50, queue, storage, noopLogger);
        Thread t = new Thread(baker);
        t.start();

        Thread.sleep(200);
        storage.close();
        t.join(2000);

        assertFalse(t.isAlive());
    }
}
