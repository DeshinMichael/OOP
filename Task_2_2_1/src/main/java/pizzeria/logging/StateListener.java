package pizzeria.logging;

import pizzeria.core.Order;
import pizzeria.core.OrderState;

public interface StateListener {
    void onStateChanged(Order order, OrderState newState);
}
