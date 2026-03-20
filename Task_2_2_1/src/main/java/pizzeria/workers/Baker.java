package pizzeria.workers;

import pizzeria.core.Order;
import pizzeria.core.OrderQueue;
import pizzeria.core.OrderState;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

public class Baker implements Runnable {
    private final int id;
    private final int cookingTimeMs;
    private final OrderQueue orderQueue;
    private final Storage storage;
    private final PizzeriaLogger logger;

    public Baker(int id, int cookingTimeMs, OrderQueue orderQueue, Storage storage, PizzeriaLogger logger) {
        this.id = id;
        this.cookingTimeMs = cookingTimeMs;
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("Baker-" + id + " started (cookingTime=" + cookingTimeMs + "ms)");
        try {
            while (true) {
                Order order = orderQueue.take();
                if (order == null) break;

                order.setState(OrderState.BEING_COOKED);
                logger.log("Baker-" + id + " is cooking order " + order.getId());

                Thread.sleep(cookingTimeMs);

                order.setState(OrderState.COOKED);
                logger.log("Baker-" + id + " finished cooking order " + order.getId());

                boolean placed = storage.put(order);
                if (!placed) {
                    logger.log("Baker-" + id + ": storage closed, cannot place order " + order.getId());
                    break;
                }
                logger.log("Baker-" + id + " placed order " + order.getId() + " to storage");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.log("Baker-" + id + " stopped.");
    }
}
