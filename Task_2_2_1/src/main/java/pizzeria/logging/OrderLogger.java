package pizzeria.logging;

import pizzeria.core.Order;
import pizzeria.core.OrderState;

public class OrderLogger implements StateListener {
    private final PizzeriaLogger logger;

    public OrderLogger(PizzeriaLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onStateChanged(Order order, OrderState newState) {
        logger.log("[" + order.getId() + "] " + newState);
    }
}
