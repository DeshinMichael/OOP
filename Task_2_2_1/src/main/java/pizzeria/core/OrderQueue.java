package pizzeria.core;

import java.util.LinkedList;

public class OrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private volatile boolean accepting = true;

    public synchronized void add(Order order) throws InterruptedException {
        if (!accepting) return;
        queue.addLast(order);
        notifyAll();
    }

    public synchronized Order take() throws InterruptedException {
        while (queue.isEmpty()) {
            if (!accepting) {
                return null;
            }
            wait();
        }
        return queue.removeFirst();
    }

    public synchronized void stopAccepting() {
        accepting = false;
        notifyAll();
    }
}

