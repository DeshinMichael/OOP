package pizzeria.lifecycle;

import org.junit.jupiter.api.Test;
import pizzeria.core.Order;
import pizzeria.core.OrderQueue;
import pizzeria.core.OrderState;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GracefulShutdownStrategyTest {

    private final PizzeriaLogger noopLogger = msg -> {};

    @Test
    void shutdownStopsAcceptingOrders() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        storage.close();

        Thread generator = new Thread(() -> {});
        generator.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(noopLogger);
        strategy.shutdown(generator, List.of(), List.of(), queue, storage);

        queue.add(new Order(99, null));
        assertNull(queue.take());
    }

    @Test
    void shutdownWaitsForAllBakersToFinish() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        List<Boolean> finished = new ArrayList<>();

        Thread bakerThread = new Thread(() -> {
            try { Thread.sleep(100); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            finished.add(true);
        });
        bakerThread.start();

        Thread generator = new Thread(() -> {});
        generator.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(noopLogger);
        strategy.shutdown(generator, List.of(bakerThread), List.of(), queue, storage);

        assertEquals(1, finished.size());
    }

    @Test
    void shutdownWaitsForAllCouriersToFinish() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        List<Boolean> finished = new ArrayList<>();

        Thread courierThread = new Thread(() -> {
            try { Thread.sleep(100); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            finished.add(true);
        });
        courierThread.start();

        Thread generator = new Thread(() -> {});
        generator.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(noopLogger);
        strategy.shutdown(generator, List.of(), List.of(courierThread), queue, storage);

        assertEquals(1, finished.size());
    }

    @Test
    void shutdownLogsMessages() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        List<String> logs = new ArrayList<>();
        PizzeriaLogger logger = logs::add;

        Thread generator = new Thread(() -> {});
        generator.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(logger);
        strategy.shutdown(generator, List.of(), List.of(), queue, storage);

        assertTrue(logs.stream().anyMatch(l -> l.contains("shutdown initiated")));
        assertTrue(logs.stream().anyMatch(l -> l.contains("bakers finished")));
        assertTrue(logs.stream().anyMatch(l -> l.contains("couriers finished")));
    }

    @Test
    void shutdownInterruptsGenerator() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        boolean[] interrupted = {false};
        Thread generator = new Thread(() -> {
            try { Thread.sleep(10_000); }
            catch (InterruptedException e) { interrupted[0] = true; }
        });
        generator.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(noopLogger);
        strategy.shutdown(generator, List.of(), List.of(), queue, storage);

        generator.join();
        assertTrue(interrupted[0]);
    }

    @Test
    void shutdownClosesStorageAfterBakers() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        Order order = new Order(1, null);
        storage.put(order);

        Thread generator = new Thread(() -> {});
        generator.start();

        Thread courierThread = new Thread(() -> {
            try {
                List<Order> orders = storage.take(1);
                orders.forEach(o -> o.setState(OrderState.DELIVERED));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        courierThread.start();

        GracefulShutdownStrategy strategy = new GracefulShutdownStrategy(noopLogger);
        strategy.shutdown(generator, List.of(), List.of(courierThread), queue, storage);

        assertEquals(OrderState.DELIVERED, order.getState());
    }
}
