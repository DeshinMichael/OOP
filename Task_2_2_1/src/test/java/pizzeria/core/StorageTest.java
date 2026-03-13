package pizzeria.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @Test
    void putAndTakeReturnsSameOrder() throws InterruptedException {
        Storage storage = new Storage(5);
        Order order = new Order(1, null);
        boolean placed = storage.put(order);
        assertTrue(placed);

        List<Order> taken = storage.take(1);
        assertEquals(1, taken.size());
        assertSame(order, taken.get(0));
    }

    @Test
    void putSetsStateToInStorage() throws InterruptedException {
        Storage storage = new Storage(5);
        Order order = new Order(1, null);
        storage.put(order);
        assertEquals(OrderState.IN_STORAGE, order.getState());
    }

    @Test
    void takeReturnsEmptyWhenClosedAndEmpty() throws InterruptedException {
        Storage storage = new Storage(5);
        storage.close();
        List<Order> result = storage.take(3);
        assertTrue(result.isEmpty());
    }

    @Test
    void putReturnsFalseWhenClosedWhileWaiting() throws InterruptedException {
        Storage storage = new Storage(1);
        Order first = new Order(1, null);
        Order second = new Order(2, null);
        storage.put(first);

        boolean[] result = {true};
        Thread bakerThread = new Thread(() -> {
            try {
                result[0] = storage.put(second);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        bakerThread.start();

        Thread.sleep(100);
        storage.close();
        bakerThread.join();

        assertFalse(result[0]);
    }

    @Test
    void takeBlocksUntilPizzaAvailable() throws InterruptedException {
        Storage storage = new Storage(5);
        Order order = new Order(3, null);

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(100);
                storage.put(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();

        List<Order> taken = storage.take(1);
        producer.join();

        assertEquals(1, taken.size());
        assertSame(order, taken.get(0));
    }

    @Test
    void takeRespectsMaxCount() throws InterruptedException {
        Storage storage = new Storage(10);
        for (int i = 1; i <= 5; i++) {
            storage.put(new Order(i, null));
        }
        List<Order> taken = storage.take(3);
        assertEquals(3, taken.size());
    }

    @Test
    void putBlocksWhenFullThenSucceedsAfterTake() throws InterruptedException {
        Storage storage = new Storage(1);
        Order first = new Order(1, null);
        Order second = new Order(2, null);
        storage.put(first);

        boolean[] result = {false};
        Thread bakerThread = new Thread(() -> {
            try {
                result[0] = storage.put(second);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        bakerThread.start();

        Thread.sleep(100);
        storage.take(1);
        bakerThread.join();

        assertTrue(result[0]);
    }
}
