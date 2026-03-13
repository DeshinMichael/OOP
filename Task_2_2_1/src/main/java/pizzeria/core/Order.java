package pizzeria.core;

import pizzeria.logging.StateListener;

public class Order {
    private final int id;
    private volatile OrderState state;
    private final StateListener listener;

    public Order(int id, StateListener listener) {
        this.id = id;
        this.state = OrderState.QUEUED;
        this.listener = listener;
    }

    public int getId() { return id; }
    public OrderState getState() { return state; }

    public void setState(OrderState state) {
        this.state = state;
        if (listener != null) listener.onStateChanged(this, state);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", state=" + state + "}";
    }
}


