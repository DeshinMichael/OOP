package pizzeria.generator;

import org.junit.jupiter.api.Test;
import pizzeria.core.Order;
import pizzeria.core.OrderQueue;
import pizzeria.core.OrderState;
import pizzeria.logging.PizzeriaLogger;
import pizzeria.logging.StateListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderGeneratorTest {

    @Test
    void runGeneratesCorrectNumberOfOrders() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        PizzeriaLogger logger = msg -> {};
        StateListener listener = (o, s) -> {};

        OrderGenerator generator = new OrderGenerator(3, 0, queue, listener, logger);
        Thread t = new Thread(generator);
        t.start();
        t.join(2000);

        queue.stopAccepting();

        List<Order> orders = new ArrayList<>();
        Order o;
        while ((o = queue.take()) != null) {
            orders.add(o);
        }
        assertEquals(3, orders.size());
    }

    @Test
    void runSetsStateToQueued() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        List<OrderState> states = new ArrayList<>();
        StateListener listener = (order, state) -> states.add(state);
        PizzeriaLogger logger = msg -> {};

        OrderGenerator generator = new OrderGenerator(2, 0, queue, listener, logger);
        Thread t = new Thread(generator);
        t.start();
        t.join(2000);

        assertEquals(2, states.size());
        assertTrue(states.stream().allMatch(s -> s == OrderState.QUEUED));
    }

    @Test
    void runLogsEachOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        List<String> logs = new ArrayList<>();
        PizzeriaLogger logger = logs::add;
        StateListener listener = (o, s) -> {};

        OrderGenerator generator = new OrderGenerator(3, 0, queue, listener, logger);
        Thread t = new Thread(generator);
        t.start();
        t.join(2000);

        assertEquals(3, logs.size());
    }

    @Test
    void runStopsOnInterrupt() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        List<String> logs = new ArrayList<>();
        PizzeriaLogger logger = logs::add;
        StateListener listener = (o, s) -> {};

        OrderGenerator generator = new OrderGenerator(100, 500, queue, listener, logger);
        Thread t = new Thread(generator);
        t.start();
        Thread.sleep(600);
        t.interrupt();
        t.join(1000);

        assertFalse(t.isAlive());
        assertTrue(logs.size() < 100);
    }
}
