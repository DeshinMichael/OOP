package pizzeria.workers;

import pizzeria.core.Order;
import pizzeria.core.OrderState;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int trunkSize;
    private final int deliveryTimeMs;
    private final Storage storage;
    private final PizzeriaLogger logger;

    public Courier(int id, int trunkSize, int deliveryTimeMs, Storage storage, PizzeriaLogger logger) {
        this.id = id;
        this.trunkSize = trunkSize;
        this.deliveryTimeMs = deliveryTimeMs;
        this.storage = storage;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("Courier-" + id + " started (trunkSize=" + trunkSize + ")");
        try {
            while (true) {
                List<Order> orders = storage.take(trunkSize);
                if (orders.isEmpty()) break;

                for (Order order : orders) {
                    order.setState(OrderState.IN_DELIVERY);
                }
                logger.log("Courier-" + id + " delivering orders: " + orders);

                Thread.sleep(deliveryTimeMs);

                for (Order order : orders) {
                    order.setState(OrderState.DELIVERED);
                }
                logger.log("Courier-" + id + " delivered orders: " + orders);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.log("Courier-" + id + " stopped.");
    }
}
