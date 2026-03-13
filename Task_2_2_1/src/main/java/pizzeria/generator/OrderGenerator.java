package pizzeria.generator;

import pizzeria.core.Order;
import pizzeria.core.OrderQueue;
import pizzeria.core.OrderState;
import pizzeria.logging.PizzeriaLogger;
import pizzeria.logging.StateListener;

public class OrderGenerator implements Runnable {
    private final int totalOrders;
    private final int intervalMs;
    private final OrderQueue orderQueue;
    private final StateListener stateListener;
    private final PizzeriaLogger logger;

    public OrderGenerator(int totalOrders, int intervalMs,
                          OrderQueue orderQueue,
                          StateListener stateListener,
                          PizzeriaLogger logger) {
        this.totalOrders = totalOrders;
        this.intervalMs = intervalMs;
        this.orderQueue = orderQueue;
        this.stateListener = stateListener;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= totalOrders; i++) {
                Order order = new Order(i, stateListener);
                order.setState(OrderState.QUEUED);
                orderQueue.add(order);
                logger.log("Order " + i + " added to queue.");
                Thread.sleep(intervalMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
