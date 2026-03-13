package pizzeria.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Storage {
    private final LinkedList<Order> pizzas = new LinkedList<>();
    private final int capacity;
    private volatile boolean open = true;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized boolean put(Order order) throws InterruptedException {
        while (pizzas.size() >= capacity) {
            if (!open) return false;
            wait();
        }
        if (!open) return false;
        pizzas.addLast(order);
        order.setState(OrderState.IN_STORAGE);
        notifyAll();
        return true;
    }

    public synchronized List<Order> take(int maxCount) throws InterruptedException {
        while (pizzas.isEmpty()) {
            if (!open) return new ArrayList<>();
            wait();
        }
        List<Order> taken = new ArrayList<>();
        int count = Math.min(maxCount, pizzas.size());
        for (int i = 0; i < count; i++) {
            taken.add(pizzas.removeFirst());
        }
        notifyAll();
        return taken;
    }

    public synchronized void close() {
        open = false;
        notifyAll();
    }
}

