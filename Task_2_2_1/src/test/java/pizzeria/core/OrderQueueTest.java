package pizzeria.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderQueueTest {

    @Test
    void addAndTakeReturnsOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1, null);
        queue.add(order);
        assertSame(order, queue.take());
    }

    @Test
    void takeReturnsNullWhenStoppedAndEmpty() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.stopAccepting();
        assertNull(queue.take());
    }

    @Test
    void addIgnoredAfterStopAccepting() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.stopAccepting();
        queue.add(new Order(1, null));
        assertNull(queue.take());
    }

    @Test
    void takeBlocksUntilOrderAdded() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(5, null);

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(100);
                queue.add(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();

        Order taken = queue.take();
        producer.join();
        assertSame(order, taken);
    }

    @Test
    void takeReturnsAllOrdersBeforeReturningNull() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order o1 = new Order(1, null);
        Order o2 = new Order(2, null);
        queue.add(o1);
        queue.add(o2);
        queue.stopAccepting();

        assertSame(o1, queue.take());
        assertSame(o2, queue.take());
        assertNull(queue.take());
    }
}
